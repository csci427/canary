package edu.umt.csci427.canary;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robb on 4/16/15.
 */
public class ViewManager
{

    public static final int MAX_MONITORS = 4;

    private static List<MonitorFragment> monitors = new ArrayList<>();
    private static int mCOUNT() { return monitors.size(); }
    private static MainActivity main;

    public static void attachMainActivity(MainActivity main) { ViewManager.main = main; }

    public static boolean addMonitorToScreen(MonitorFragment m)
    {
        if (mCOUNT() + 1 > MAX_MONITORS)
        {
            throw new UnsupportedOperationException("Cannot add more monitors to screen.");
        }

        monitors.add(m);
        ArrangeMonitors(mCOUNT());

        return true;
    }

    /**
     * Arrange all of the monitors. We will only support 4 due to screen constraints.
     * @param numberOfMonitorsInArray
     * @return
     */
    private static boolean ArrangeMonitors(int numberOfMonitorsInArray) {
        boolean success = false;
        String layoutToPlaceMonitorIn = "monitor_container_";
        try{
            //If the number of monitors matches at this point we want to add the entire array
            //If not we don't want to cause exceptions.
            if(numberOfMonitorsInArray <= MAX_MONITORS){
                //For each monitor fragment add them to the respective layout.
                for(int i = numberOfMonitorsInArray - 1; i < mCOUNT(); i++){

                    main.getFragmentManager().beginTransaction()
                            .add(R.id.class.getField(layoutToPlaceMonitorIn + i).getInt(0), monitors.get(i), layoutToPlaceMonitorIn + i)//TODO: Debug.
                            .commit();
                    success = true;
                }
            }
            else{
                Log.d("Canary Media Player", "Error arranging " + numberOfMonitorsInArray + " monitors.");
            }
        }
        catch(Exception ex){
            Log.d("Canary Media Player", "Error arranging monitors || " + ex.toString());
        }
        return success;
    }

    public boolean removeMonitorFromScreen(MonitorFragment m)
    {
        if (!monitors.contains(m))
        {
            //throw new IndexOutOfBoundsException("Monitor does not exist.");
        }
        monitors.remove(m);
        //arrange();
        return true;
    }

}
