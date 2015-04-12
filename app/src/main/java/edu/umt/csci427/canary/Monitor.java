package edu.umt.csci427.canary;

/**
 * Created by robb on 3/24/15.
 */
public class Monitor
{
    private String title;
    private String units;
    private float value;

    private Monitor()
    {
        title = "";
        units = "";
        value = 0;
    }

    public String getTitle() { return title; }
    public String getUnits() { return units; }
    public float getValue() { return value; }

    public static Monitor newInstance(String title, String units, float value)
    {
        Monitor m = new Monitor();
        m.title = title;
        m.units = units;
        m.value = value;
        return m;
    }


}
