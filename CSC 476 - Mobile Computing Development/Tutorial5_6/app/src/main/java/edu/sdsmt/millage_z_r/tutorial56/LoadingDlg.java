/**
 * Author: Zoe Millage
 * Description: Dialogue indicating that the chosen hatting is being loaded
 */

package edu.sdsmt.millage_z_r.tutorial56;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class LoadingDlg extends DialogFragment {
    private final static String ID = "id";

    /**
     * Id for the image we are loading
     */
    private String catId;



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
        builder.setTitle(R.string.loading);

        builder.setNegativeButton(android.R.string.cancel, (dialog, id) -> {
            dialog.dismiss();
        });

        // Create the dialog box
        final AlertDialog dlg = builder.create();

        // Get a reference to the view we are going to load into
        final HatterView view = getActivity().findViewById(R.id.hatterView);
        Cloud cloud = new Cloud();
        cloud.loadFromCloud(view, catId, dlg);

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
