package edu.umt.csci427.canary;

/**
 * Created by robb on 3/24/15.
 */
public class Monitor
{
    private String title;
    private String units;
    private float value;
    private String metric_id;

    private Monitor()
    {
        title = "";
        units = "";
        value = 0;
    }

    public String getTitle() { return title; }
    public String getUnits() { return units; }
    public float getValue() { return value; }
    public String getMetric_id() { return metric_id; }

    public static Monitor newInstance(String title, String units, float value, String metric_id)
    {
        Monitor m = new Monitor();
        m.title = title;
        m.units = units;
        m.value = value;
        m.metric_id = metric_id;
        return m;
    }


}
