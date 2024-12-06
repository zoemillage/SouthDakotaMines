/**
 * Author: Zoe Millage
 * Description: Holds functions related to the Firebase database
 */

package edu.sdsmt.millage_z_r.tutorial56;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Cloud {

    private final static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final static DatabaseReference hattingsList =
            database.getReference("hattings").child(MonitorFirebase.INSTANCE.getUserUid());

    /**
     * A class that holds a line's contents for later updating
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data line
        public final View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }



    /**
     * Nested class to store one catalog row underlying data
     */
    public static class Item {
        public String name = "";
        public String id = "";
    }



    /**
     * An adapter so that list boxes can display a list of filenames from
     * the cloud server.
     */
    public static class CatalogAdapter extends RecyclerView.Adapter<ViewHolder>{

        public final CatalogCallback clickEvent;

        /**
         * The items we display in the list box. Initially this is
         * null until we get items from the server.
         */
        private ArrayList<Item> items = new ArrayList<>();


        /**
         * Constructor
         */
        public CatalogAdapter(final View view, CatalogCallback click) {
            items = getCatalog(view);
            clickEvent = click;
        }



        /**
         * gets the catalog of hattings in the database and displays it
         * @param view view with elements that trigger this dialogue
         * @return true when finished
         */
        public ArrayList<Item> getCatalog(final View view) {
            ArrayList<Item> newItems = new ArrayList<>();

            //connect to the database (hattings child)

            database.goOnline();
            DatabaseReference myRef = hattingsList;


            // Read from the database
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    //look at each child
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Item tempItem = new Item();
                        tempItem.name = child.child("name").getValue().toString();
                        tempItem.id = child.getKey();
                        newItems.add(tempItem);
                    }

                    view.post(new Runnable() {

                        @Override
                        public void run() {
                            // Tell the adapter the data set has been changed
                            notifyItemRangeChanged(0, newItems.size());
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value
                    // Error condition!
                    view.post(() -> Toast.makeText(view.getContext(), R.string.catalog_fail, Toast.LENGTH_SHORT).show());

                }
            });

            return newItems;
        }



        public Item getItem(int position) {
            return items.get(position);
        }



        public long getItemId(int position) {
            return position;
        }



        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.catalog_item, parent, false);
            return new ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Get the item of the one we want to load
                    Item catItem = getItem(holder.getAdapterPosition());

                    // let the client class do its job
                    clickEvent.callback(catItem);
                }

            });

            TextView tv = holder.view.findViewById(R.id.textItem);
            String text =  items.get(position).name;
            tv.setText( text );
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }



    /**
     * method to load a specific hatting
     * @param view The view we might toast to
     * @param catId the id of the hatting
     */
    public void deleteFromCloud(final HatterView view, String catId)
    {
        // get the node to delete
        DatabaseReference myRef = hattingsList.child(catId);

        myRef.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if(error != null) {
                    // Error condition!
                    /*
                     * make a toast
                     */
                    Toast.makeText(view.getContext(), R.string.delete_fail, Toast.LENGTH_SHORT).show();
                }
            }});
    }



    /**
     * method to load a specific hatting
     * @param view The view we are loading the hatting into
     * @param catId the id of the hatting
     * @param dlg the dialog box showing the loading state
     */
    public void loadFromCloud(final HatterView view, String catId, final Dialog dlg)
    {
        DatabaseReference myRef = hattingsList.child(catId);

        // Read from the database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                view.loadJSON(dataSnapshot);
                dlg.dismiss();
                if (view.getContext() instanceof HatterActivity)
                    ((HatterActivity) view.getContext()).updateUI();
                view.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Error condition!
                view.post(() -> {
                    Toast.makeText(view.getContext(), R.string.loading_fail, Toast.LENGTH_SHORT).show();
                    dlg.dismiss();
                });
            }
        });
    }



    /**
     * Save a hatting to the cloud.
     * @param name name to save under
     * @param view view we are getting the data from
     */
    public void saveToCloud(String name, HatterView view) {
        name = name.trim();
        if(name.length() == 0) {
            /*
             *  If we fail to save, display a toast
             */
            view.post(() -> {
                Toast.makeText(view.getContext(), R.string.saving_fail_no_name, Toast.LENGTH_SHORT).show();
            });

            return;
        }

        String key = hattingsList.push().getKey();
        DatabaseReference tempHattingsList = hattingsList.child(key);
        tempHattingsList.child("name").setValue(name, new DatabaseReference.CompletionListener() {
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError != null) {
                    // Error condition!
                    /*
                     * make a toast
                     */
                    Toast.makeText(view.getContext(), R.string.saving_fail, Toast.LENGTH_SHORT).show();
                }else{
                    view.saveJSON(tempHattingsList);
                }
            }});
    }

}
