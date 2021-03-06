package edu.umt.csci427.canary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;


public class twoThresholdFragment extends ThresholdFragment {

    private TextView lowTextView;
    private TextView highTextView;
    private int highThreshold; // progress on seekBar
    private int lowThreshold; // progress on seekBar
    private int thresholdMax; // max value for seekBar

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // create layout
        View menuView = inflater.inflate(R.layout.fragment_threshold, null);
        final SeekBar lowBar = (SeekBar) menuView.findViewById(R.id.lowSeekBar);
        SeekBar highBar = (SeekBar) menuView.findViewById(R.id.highSeekBar);

        // set default progress on both threshold bars
        lowBar.setProgress(lowThreshold);
        highBar.setProgress(highThreshold);

        // set maximum
        highBar.setMax(thresholdMax);

        // low bar max will be highThreshold
        lowBar.setMax(highThreshold);

        // high seek bar text
        highTextView = (TextView) menuView.findViewById(R.id.highSeekBarText);
        highTextView.setText("High: " + highThreshold);

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

        // change listener for high seekBar
        highBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                highThreshold = progress;
                highTextView.setText("High: " + highThreshold);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                lowBar.setMax(highThreshold);
            }
        });

        // set PositiveButton to set thresholds in the MainActivity
        builder.setTitle(monitor.getTitle())
                .setView(menuView)
                .setPositiveButton("Set Thresholds", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onThresholdFragmentPositiveClick(twoThresholdFragment.this);
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
        highThreshold = high;
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
        return highThreshold;
    }
}
