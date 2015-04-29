package edu.umt.csci427.canary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by robb on 4/12/15.
 */

public class AddMonitorFragment extends DialogFragment {

    // Use this instance of the interface to deliver action events
    AddMonitorListener aml;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.add_monitor_menu_item)
                .setItems(R.array.monitor_list, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                         aml.onMonitorListClick(AddMonitorFragment.this, which);
                    }
                });
        return builder.create();
    }

    public interface AddMonitorListener {
        public void onMonitorListClick(DialogFragment dialog, int title);
            // The 'which' argument contains the index position
            // of the selected item
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            aml = (AddMonitorListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement AddMonitorListener");
        }
    }
}