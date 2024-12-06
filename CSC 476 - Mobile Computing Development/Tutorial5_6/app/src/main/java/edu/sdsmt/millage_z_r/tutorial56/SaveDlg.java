/**
 * Author: Zoe Millage
 * Description: The dialogue for letting the user save a
 * hatting to the database
 */

package edu.sdsmt.millage_z_r.tutorial56;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class SaveDlg extends DialogFragment {

    private AlertDialog dlg;


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());// Set the title
        builder.setTitle(R.string.save);

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.save_dlg, null);
        builder.setView(view);

        // Add a cancel button
        builder.setNegativeButton(android.R.string.cancel, (dialog, id) -> {
            // Cancel just closes the dialog box
        });

        // Add an OK button
        builder.setPositiveButton(android.R.string.ok, (dialog, id) -> {
            EditText editName = dlg.findViewById(R.id.editName);
            save(editName.getText().toString());
        });

        dlg = builder.create();


        return dlg;
    }



    /**
     * Actually save the hatting
     * @param name name to save it under
     */
    private void save(final String name) {
        if (!(getActivity() instanceof HatterActivity)) {
            return;
        }
        HatterActivity activity = (HatterActivity) getActivity();
        HatterView view = activity.findViewById(R.id.hatterView);

        // start firebase call

        Cloud cloud = new Cloud();
        cloud.saveToCloud(name, view);
    }
}
