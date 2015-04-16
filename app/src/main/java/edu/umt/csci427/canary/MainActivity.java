package edu.umt.csci427.canary;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import rosetta.MDC_ECG_HEART_RATE;
import rosetta.MDC_PRESS_CUFF_SYS;
import rosetta.MDC_PULS_OXIM_PULS_RATE;
import rosetta.MDC_PULS_OXIM_SAT_O2;


public class MainActivity extends ActionBarActivity implements
        MonitorFragment.OnFragmentInteractionListener,
        ThresholdFragment.OnFragmentInteractionListener,
        AddMonitorFragment.AddMonitorListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {


            Intent intent = new Intent(this, OpenICEService.class);
            startService(intent);

            Intent startServiceIntent = new Intent(this, AlertService.class);
            startService(startServiceIntent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent i = new Intent(this, LineService.class);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.add_monitor)
        {
            DialogFragment addMonitorFrag = new AddMonitorFragment();
            addMonitorFrag.show(getSupportFragmentManager(), "monitors");

            return true;
        }
        else if (id == R.id.action_line_start) {
            startService(i);
            return true;
        }
        else if (id == R.id.action_line_stop) {
            stopService(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction()
    {
        getFragmentManager().beginTransaction()
                .add(R.id.container, ThresholdFragment.newInstance("bogus1", "bogus2"))
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {}


    @Override
    public void onMonitorListClick(DialogFragment dialog, int which) {

        // TODO: These are horrible hard coded magic numbers... fix it... someone... besides me

        String title = "";
        String units = "";
        String metric_id = "";

        switch (which){
            case 0:
                title = "Pulse rate (ox)";
                units = "BPM";
                metric_id = MDC_PULS_OXIM_PULS_RATE.VALUE;
                break;
            case 1:
                title = "Heart (ECG)";
                units = "BPM";
                metric_id = MDC_ECG_HEART_RATE.VALUE;
                break;
            case 2:
                title = "SpO2 (ox)";
                units = "%";
                metric_id = MDC_PULS_OXIM_SAT_O2.VALUE;
                break;
            case 3:
                title = "Sys BP (cuff)";
                units = "mmHg";
                metric_id = MDC_PRESS_CUFF_SYS.VALUE;
                break;
        }


        getFragmentManager().beginTransaction()
                .add(R.id.container, MonitorFragment.newInstance(title, units, 1.2f, metric_id))
                .commit();
    }


}
