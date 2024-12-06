/**
 * Author: Zoe Millage
 * Description: The dialogue for deleting an entry from the catalogue
 */

package edu.sdsmt.millage_z_r.tutorial56;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DeleteDlg extends DialogFragment implements CatalogCallback{

    /**
     * makes sure the user wants to delete the entry
     * @param catItem the item to delete
     */
    @Override
    public void callback(Cloud.Item catItem) {
        YouSureDlg sureDlg = new YouSureDlg();
        sureDlg.setCatId(catItem.id);
        sureDlg.show(getParentFragmentManager(), "youSure");
        this.dismiss();
    }



    /**
     * Create the dialog box
     * @param savedInstanceState the saved state
     *
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());// Set the title
        builder.setTitle(R.string.delete);

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.catalog_dlg, null);
        builder.setView(view);

        // Add a cancel button
        builder.setNegativeButton(android.R.string.cancel, (dialog, id) -> {
            // Cancel just closes the dialog box
        });

        AlertDialog dlg = builder.create();

        RecyclerView list = view.findViewById(R.id.listHattings);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        final Cloud.CatalogAdapter adapter = new Cloud.CatalogAdapter(list, this);
        list.setAdapter(adapter);

        return dlg;
    }
}
