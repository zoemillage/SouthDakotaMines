/**
 * Author: Zoe Millage
 * Description: Confirmation dialogue before deleting a hatting
 */

package edu.sdsmt.millage_z_r.tutorial56;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class YouSureDlg extends DialogFragment {
        private final static String ID = "deleteId";

        /**
         * Id for the image we are deleting
         */
        private String catId;


        /**
         * Delete the hatting
         */
        private void delete() {
            if (!(getActivity() instanceof HatterActivity)) {
                return;
            }
            HatterActivity activity = (HatterActivity) getActivity();
            HatterView view = activity.findViewById(R.id.hatterView);

            // start firebase call

            Cloud cloud = new Cloud();
            cloud.deleteFromCloud(view, catId);
        }



        public String getCatId() {
            return catId;
        }



        /**
         * Create the dialog box
         */
        @Override
        public Dialog onCreateDialog(Bundle bundle) {
            if(bundle != null) {
                catId = bundle.getString(ID);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            // Set the title
            builder.setTitle(R.string.delete);

            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            // Pass null as the parent view because its going in the dialog layout
            View view = inflater.inflate(R.layout.you_sure_dlg, null);
            builder.setView(view);

            builder.setNegativeButton(android.R.string.cancel, (dialog, id) -> {
                dialog.dismiss();
            });

            // Add an OK button
            builder.setPositiveButton(android.R.string.ok, (dialog, id) -> {
                delete();
            });

            // Create the dialog box
            final AlertDialog dlg = builder.create();


            return dlg;
        }



        /**
         * Save the instance state
         */
        @Override
        public void onSaveInstanceState(Bundle bundle) {
            super.onSaveInstanceState(bundle);

            bundle.putString(ID, catId);
        }



        public void setCatId(String catId) {
            this.catId = catId;
        }
}
