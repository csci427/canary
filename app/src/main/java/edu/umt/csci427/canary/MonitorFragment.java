package edu.umt.csci427.canary;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
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
 * {@link MonitorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MonitorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonitorFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "units";
    private static final String ARG_PARAM3 = "value";
    private static final String ARG_PARAM4 = "metric_id";

    private Monitor monitor;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Title which will be displayed in the monitor.
     * @param units Units the value uses, if applicable.
     * @param value Value of the monitor.
     * @param metric_id Action of broadcasts that we should receive
     * @return A new instance of fragment MonitorFragment.
     */

    public static MonitorFragment newInstance(String title, String units, float value,
                                              String metric_id) {
        MonitorFragment fragment = new MonitorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putString(ARG_PARAM2, units);
        args.putFloat(ARG_PARAM3, value);
        args.putString(ARG_PARAM4, metric_id);
        fragment.setArguments(args);

        return fragment;
    }

    public MonitorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            monitor = Monitor.newInstance(
                    getArguments().getString(ARG_PARAM1),
                    getArguments().getString(ARG_PARAM2),
                    getArguments().getFloat(ARG_PARAM3),
                    getArguments().getString(ARG_PARAM4)
            );
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
        valTv = (TextView)getView().findViewById(R.id.monitorUnitsTextView);
        valTv.setText(monitor.getUnits());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
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
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction();
        public void launchThresholdOnClick();
    }

}
