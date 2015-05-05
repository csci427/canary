package edu.umt.csci427.canary;

import android.app.Activity;
import android.app.DialogFragment;

/**
 * Abstract class with two and one threshold fragment as children. One threshold fragment is currenly
 * only used to set SpO2, as having a high threshold doesn't really make any sense.
 */
public abstract class ThresholdFragment extends DialogFragment{
    abstract void setMonitor(Monitor monitor);
    abstract void setDefaultThresholds(int high, int low);
    abstract void setMax(int max);
    abstract int getLowThreshold();
    abstract int getHighThreshold();

    public Monitor monitor;
    public String tag;
    public boolean thresholdsSet = false;

    // Use this instance of the interface to deliver action events
    protected ThresholdFragmentListener mListener;

    /** The activity that creates an instance of this dialog fragment must
    * implement this interface in order to receive event callbacks.
    * Each method passes the DialogFragment in case the host needs to query it. */
    public interface ThresholdFragmentListener {
        void onThresholdFragmentPositiveClick(ThresholdFragment dialog);
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ThresholdFragmentListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ThresholdFragmentListener");
        }
    }

    public void setTag(String tag){
        this.tag = tag;
    }

    public String getMonitorTag(){
        return tag;
    }
}

