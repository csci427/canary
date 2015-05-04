package edu.umt.csci427.canary;

import android.app.DialogFragment;

/**
 * Created by aaron on 5/2/15.
 */
public abstract class ThresholdFragment extends DialogFragment{
    abstract void setMonitor(Monitor monitor);
    abstract void setDefaultThresholds(int high, int low);
    abstract void setMax(int max);
}

