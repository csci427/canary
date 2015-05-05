package edu.umt.csci427.canary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.rti.dds.util.ArraySequence;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by robb on 4/16/15.
 */
public class ViewManager
{
    public static final int MAX_MONITORS = 4;

    private static HashMap<String, MonitorFragment> monitors = new HashMap<>();
    private static boolean[] openContainers = {true, true, true, true};
    private static HashMap<String, Integer> containerMap = new HashMap<>();
    private static int mCOUNT() { return monitors.size(); }
    private static MainActivity main;


    public static void attachMainActivity(MainActivity main) { ViewManager.main = main; }

    public static boolean addMonitorToScreen(MonitorFragment m)
    {
        if (mCOUNT() + 1 > MAX_MONITORS)
        {
            ///show toast saying they cant add more.
            Toast.makeText(ViewManager.main, "Maximum number of monitors reached.",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            monitors.put(m.getMonitorTitle(), m);
            ArrangeMonitors(mCOUNT());
        }
        return true;
    }

    private static void removeAllFragmentsFromView()
    {
        for (String k : monitors.keySet())
        {
            main.getFragmentManager().beginTransaction()
                    .remove(monitors.get(k))
                    .commit();
        }
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
            if(numberOfMonitorsInArray <= MAX_MONITORS) {
                //For each monitor fragment add them to the respective layout.
                int pos = 0;
                for (String k : monitors.keySet()) {
                        pos = findNextOpenPosition(pos);
                        if(!monitors.get(k).isAdded()) {
                            main.getFragmentManager().beginTransaction()
                                    .add(R.id.class.getField(layoutToPlaceMonitorIn + pos).getInt(0),
                                            monitors.get(k), monitors.get(k).getMonitorTitle())
                                    .commit();
                            success = true;
                            openContainers[pos] = false;
                            containerMap.put(monitors.get(k).getMonitorTitle(),pos);
                        }
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

    private static int findNextOpenPosition(int start)
    {
        if (!openContainers[start]) { findNextOpenPosition(++start); }
        return start;
    }

    public static boolean removeMonitorFromScreen(View v, final MonitorFragment m)
    {
        try
        {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Remove Fragment")
                    .setMessage("Do you want to remove monitor?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            main.getFragmentManager().beginTransaction()
                                    .remove(m)
                                    .commit();

                            resolveOpenContainers(m);
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        }
        catch (Exception ex)
        {
            Log.d("Canary Media Player", "Error arranging removing monitor || " + m.getTag());
        }
        return true;
    }

    public static void resolveOpenContainers(MonitorFragment monitor){
        int monitorPosition = containerMap.get(monitor.getMonitorTitle());
        monitors.remove(monitor.getMonitorTitle());
        openContainers[monitorPosition] = true;

    }
}
