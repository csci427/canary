package edu.umt.csci427.canary;

/**
 * Created by robb on 3/24/15.
 */
public class Monitor
{
    private String title;
    private String units;
    private String metric_id;
    private ThresholdFragment thresholdFragment;

    private Monitor()
    {
        title = "";
        units = "";
    }

    public String getTitle() { return title; }
    public String getUnits() { return units; }
    public String getMetric_id() { return metric_id; }
    public ThresholdFragment getThresholdFragment() { return thresholdFragment; }

    public static Monitor newInstance(String title, String units, String metric_id)
    {
        Monitor m = new Monitor();
        m.title = title;
        m.units = units;
        m.metric_id = metric_id;
        return m;
    }

    public void setThresholdFragment(ThresholdFragment thresholdFragment) {
        this.thresholdFragment = thresholdFragment;
    }
}
