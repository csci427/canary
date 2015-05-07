package edu.umt.csci427.canary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;


public class oneThresholdFragment extends ThresholdFragment {

    private TextView lowTextView;
    private int lowThreshold; // progress on seekBar
    private int thresholdMax; // max value for seekBar

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // create layout
        View menuView = inflater.inflate(R.layout.fragment_one_threshold, null);
        SeekBar lowBar = (SeekBar) menuView.findViewById(R.id.lowSeekBar);

        // set default progress on both threshold bars
        lowBar.setProgress(lowThreshold);

        // set maximum
        lowBar.setMax(thresholdMax);

        // low seek bar text
        lowTextView = (TextView) menuView.findViewById(R.id.lowSeekBarText);
        lowTextView.setText("Low: " + lowThreshold);

        // change listener for low seekBar
        lowBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lowThreshold = progress;
                lowTextView.setText("Low: " + lowThreshold);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // set PositiveButton to set thresholds in the MainActivity
        builder.setTitle(monitor.getTitle());
        builder.setView(menuView);
        builder.setPositiveButton("Set Thresholds", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mListener.onThresholdFragmentPositiveClick(oneThresholdFragment.this);
                thresholdsSet = true;
            }
        });
        return builder.create();
    }

    public void setMonitor(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void setDefaultThresholds(int high, int low) {
        lowThreshold = low;
    }

    @Override
    void setMax(int max) {
        thresholdMax = max;
    }

    @Override
    int getLowThreshold() {
        return lowThreshold;
    }

    @Override
    int getHighThreshold() {
        return Integer.MAX_VALUE    ;
    }
}
