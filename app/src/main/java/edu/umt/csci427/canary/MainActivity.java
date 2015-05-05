package edu.umt.csci427.canary;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements
        AddMonitorFragment.AddMonitorListener,
        ThresholdFragment.ThresholdFragmentListener{

    AlertService alertService;
    boolean alertServiceBound = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            Intent intent = new Intent(this, OpenICEService.class);
            startService(intent);

            Intent startServiceIntent = new Intent(this, AlertService.class);
            startService(startServiceIntent);

            ViewManager.attachMainActivity(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, AlertService.class);
        getApplicationContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            AlertService.LocalBinder binder = (AlertService.LocalBinder) service;
            alertService = binder.getService();
            alertServiceBound = true;
            Log.v("ZZZ", "Service bound");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            alertServiceBound = false;
        }
    };

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
        AudioManager am= (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);

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
            //check for a connected headset/adapter
            Boolean tStat = am.isWiredHeadsetOn();
            if (tStat == true){
                startService(i);
            }
            else{
                Toast.makeText(getBaseContext(), "Error: Connect Adapter!", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        else if (id == R.id.action_line_stop) {
            stopService(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMonitorListClick(DialogFragment dialog, int which) {

        //Retrieve selection array
        String[] monitorSelection = getResources().getStringArray(R.array.monitor_list);
        //Create factory
        OpenICEAbstractFactory factory = OpenICEAbstractFactory.GetSimulatedFactory(monitorSelection[which]);

        if(factory != null){
            //Create monitor object
            Monitor myMonitor = factory.PackageOpenICESimulatedData(monitorSelection[which]);

            // create new monitorFragment
            MonitorFragment monitorFragment = new MonitorFragment();
            monitorFragment.setMonitor(myMonitor); // set data
            ViewManager.addMonitorToScreen(monitorFragment);

        }
        else{
            ///show toast saying they cant add more.
            Toast.makeText(this, monitorSelection[which] + " Factory not found.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Overridden methods necessary.
     */
    @Override
    public void onDestroy(){
        //Stop the OpenICE service.
        stopService(new Intent(this, OpenICEService.class));
        super.onDestroy();
    }

    //Overriding this method keeps the back button from being pressed and executing.
    @Override
    public void onBackPressed(){}

    /*****************************************
     * When a fragment is attached to the activity
     * add its tag to the list this allows us to
     * remove it later if necessary
     ****************************************/
    @Override
    public void onAttachFragment(Fragment fragment){
        super.onAttachFragment(fragment);
    }

    @Override
    public void onThresholdFragmentPositiveClick(ThresholdFragment dialog) {
        alertService.CreateOrModifyListener(dialog.monitor.getMetric_id(),
                dialog.getHighThreshold(), dialog.getLowThreshold());

        // get the tag for the Monitor fragment stored in the thresholdFragment. Find this monitor
        // fragment by tag and communicate that data has been changed.
        ((MonitorFragment)getFragmentManager().findFragmentByTag(dialog.getMonitorTag())).setThresholdTextViews();
    }
}
