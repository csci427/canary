package edu.umt.csci427.canary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MonitorFragment.OnMonitorFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MonitorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonitorFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "units";
    private static final String ARG_PARAM3 = "value";
    private static final String ARG_PARAM4 = "metric_id";

    private static Monitor monitor;
    private OnMonitorFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Title which will be displayed in the monitor.
     * @param units Units the value uses, if applicable.
     * @param metric_id Action of broadcasts that we should receive
     * @return A new instance of fragment MonitorFragment.
     */

    public static MonitorFragment newInstance(String title, String units, String metric_id) {
        MonitorFragment fragment = new MonitorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putString(ARG_PARAM2, units);
        args.putString(ARG_PARAM4, metric_id);
        fragment.setArguments(args);

        return fragment;
    }

    public static MonitorFragment newInstance(Monitor factMonitor) {
        MonitorFragment fragment = new MonitorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, factMonitor.getTitle());
        args.putString(ARG_PARAM2, factMonitor.getUnits());
        args.putString(ARG_PARAM4, factMonitor.getMetric_id());
        fragment.setArguments(args);
        monitor = factMonitor;

        return fragment;
    }

    public MonitorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Retain this fragment across configuration changes.
            setRetainInstance(true);
        }
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

        BroadcastReceiver receiver = new OpenICEIntentReceiver(this,
                (TextView)getView().findViewById(R.id.monitorValueTextView));

        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(receiver,
                new IntentFilter(monitor.getMetric_id()));

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
    }

    private class monitorButtonShortListener implements View.OnClickListener
    {
        @Override
        public void onClick(View V)
        {
          /*  getFragmentManager().beginTransaction()
                    .hide(getFragmentManager().findFragmentByTag(monitor.getTitle()));
            getFragmentManager().beginTransaction()
                    .add(R.id.container, ThresholdFragment.newInstance("bogus1", "bogus2"))
                    .commit();
        }*/}
    }

    private class monitorButtonLongListener implements View.OnLongClickListener
    {
        @Override
        public boolean onLongClick(View v) {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Remove Fragment")
                    .setMessage("Do you want to remove monitor?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            getFragmentManager().beginTransaction()
                                    .remove(getFragmentManager().findFragmentByTag(monitor.getTitle()))
                                    .commit();
                            getFragmentManager().executePendingTransactions();
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
            return true;
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnMonitorFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Override needed to fix crash when rotating screen
     **/
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMonitorFragmentInteractionListener {
        public void onFragmentInteraction();
        public void launchThresholdOnClick();

        public AlertService getAlertService();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }

}