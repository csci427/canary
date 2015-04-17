package edu.umt.csci427.canary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robb on 4/16/15.
 */
public class ViewManager
{

    public static final int MAX_MONITORS = 1;

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
        arrange();

        return true;
    }

    private static boolean arrange()
    {
        boolean ret = true;
        switch(mCOUNT())
        {
            case(1):
                arrange1();
                break;
            case(2):
                arrange2();
                break;
            case(3):
                arrange3();
                break;
            case(4):
                arrange4();
                break;
            default:
                ret = false;
                break;

        }
        return ret;
    }

    private static boolean arrange1()
    {
        main.getFragmentManager().beginTransaction()
                .add(R.id.container, monitors.get(0), monitors.get(0).getTag())
                .commit();
        return true;
    }

    private static boolean arrange2() { throw new UnsupportedOperationException(); }
    private static boolean arrange3() { throw new UnsupportedOperationException(); }
    private static boolean arrange4() { throw new UnsupportedOperationException(); }

    public boolean removeMonitorFromScreen(MonitorFragment m)
    {
        if (!monitors.contains(m))
        {
            //throw new IndexOutOfBoundsException("Monitor does not exist.");
        }
        monitors.remove(m);
        arrange();
        return true;
    }

}
