/**
 * holds the how to play dialogue
 */

package edu.sdsmt.project1team3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * a dialogue box showing how to play
 */
public class HowToPlayDialog extends DialogFragment {
    /**
     * Gabe Jerome
     * Make the dialogue box
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment.
     * @return the dialogue
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Pass null as the parent view because it's going in the dialog layout.
        builder.setView(inflater.inflate(R.layout.dialog_how_to_play, null))
                .setPositiveButton(R.string.close, (dialog, id) -> {});
        return builder.create();
    }
}