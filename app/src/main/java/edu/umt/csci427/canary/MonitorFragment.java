package edu.umt.csci427.canary;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MonitorFragment extends Fragment {

    private Monitor monitor;

    public MonitorFragment() {
        // Required empty public constructor
    }

    public void setMonitor(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_monitor, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();


        // register receiver for data intents
        BroadcastReceiver receiver = new OpenICEIntentReceiver(this,
                (TextView)getView().findViewById(R.id.monitorValueTextView));
        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(receiver,
                new IntentFilter(monitor.getMetric_id()));

        // register receiver for alert intents
        BroadcastReceiver receiver1 = new AlertIntentReceiver(getView());
        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(receiver1,
                new IntentFilter(monitor.getMetric_id() + AlertService.ALERT));

        TextView valTv;
        valTv = (TextView)getView().findViewById(R.id.monitorTitleTextView);
        valTv.setText(monitor.getTitle());
        valTv.setOnClickListener(new monitorButtonShortListener());
        valTv.setOnLongClickListener(new monitorButtonLongListener());
        valTv = (TextView)getView().findViewById(R.id.monitorUnitsTextView);
        valTv.setText(monitor.getUnits());
        valTv.setOnClickListener(new monitorButtonShortListener());
        valTv.setOnLongClickListener(new monitorButtonLongListener());
        valTv = (TextView)getView().findViewById(R.id.monitorValueTextView);
        valTv.setOnClickListener(new monitorButtonShortListener());
        valTv.setOnLongClickListener(new monitorButtonLongListener());

        //Prompt user to set thresholds if not already set
        ThresholdFragment thresholdsFrag = monitor.getThresholdFragment();

        if (!thresholdsFrag.thresholdsSet) {
            //set tag for later use in callback method (onThresholdFragmentPositiveClick)
            thresholdsFrag.setTag(this.getTag());
            thresholdsFrag.show(getFragmentManager(), "ThresholdsFragment");
        }
    }

    public String getMonitorTitle() { return monitor.getTitle(); }

    private class monitorButtonShortListener implements View.OnClickListener
    {
        @Override
        public void onClick(View V)
        {
            // Prompt user to set thresholds
            ThresholdFragment thresholdsFrag = monitor.getThresholdFragment();
            thresholdsFrag.show(getFragmentManager(), "ThresholdsFragment");
        }
    }

    // set the threshold views to values in the monitor (thresholdFragment has the same instance)
    public void setThresholdTextViews() {
        TextView valTv;
        // set low threshold test
        valTv = (TextView)getView().findViewById(R.id.lowThreshold);
        valTv.setText("low threshold: " + monitor.getThresholdFragment().getLowThreshold());

        if(!(monitor.getThresholdFragment() instanceof oneThresholdFragment)) {

            // set high threshold test
            valTv = (TextView) getView().findViewById(R.id.highThreshold);
            valTv.setText("high threshold: " + monitor.getThresholdFragment().getHighThreshold());
        } else {
            // set high threshold test
            valTv = (TextView) getView().findViewById(R.id.highThreshold);
            valTv.setText("");
        }
    }

    private class monitorButtonLongListener implements View.OnLongClickListener
    {
        @Override
        public boolean onLongClick(View v) {
                ViewManager.removeMonitorFromScreen(v,
                        (MonitorFragment)getFragmentManager().findFragmentByTag(monitor.getTitle()));
            return true;
        }
    }
}