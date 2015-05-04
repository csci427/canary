package edu.umt.csci427.canary;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends ActionBarActivity implements
        MonitorFragment.OnMonitorFragmentInteractionListener,
        AddMonitorFragment.AddMonitorListener {

    AlertService mService;
    boolean mBound = false;
    //Will create the monitors
    private OpenICEAbstractFactory factory = null;

    //List that contains the tags of all the Monitors added,
    //This allows us to remove them all and add them all on resume.
    private List<String> attachedFragmentsList = null;


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
            mService = binder.getService();
            mBound = true;
            Log.v("ZZZ", "Service bound");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
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

    }

    @Override
    public void launchThresholdOnClick()
    {
        onFragmentInteraction();
    }

    @Override
    public AlertService getAlertService() {
        return mService;
    }


    //@Override
    public void onFragmentInteraction(Uri uri) {}


    @Override
    public void onMonitorListClick(DialogFragment dialog, int which) {

        //Retrieve selection array
        String[] monitorSelection = getResources().getStringArray(R.array.monitor_list);
        //Create factory
        //TODO: switch to just normal factory
        factory = OpenICEAbstractFactory.GetSimulatedFactory(monitorSelection[which]);

        if(factory != null){
            //Create monitor object
            Monitor myMonitor = factory.PackageOpenICESimulatedData(monitorSelection[which]);

            //Add to View Manager.
            ViewManager.addMonitorToScreen(MonitorFragment.newInstance(myMonitor));

            //Prompt user to set thresholds
            ThresholdFragment thresholdsFrag = myMonitor.getThresholdFragment();
            thresholdsFrag.show(getFragmentManager(), "ThresholdsFragment");

        }
        else{
            ///show toast saying they cant add more.
            Toast.makeText(this, monitorSelection[which] + " Factory not found.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy(){
        stopService(new Intent(this, OpenICEService.class));
        super.onDestroy();
    }

    /*****************************************
     * When a fragment is attached to the activity
     * add its tag to the list this allows us to
     * remove it later if necessary
     ****************************************/
    @Override
    public void onAttachFragment(Fragment fragment){
        //Instantiate the list if it needs to be.
     /*   if(attachedFragmentsList == null){
            attachedFragmentsList = new ArrayList<>();
        }
            //Check to make sure the tag is not null or whitespace.
        if(fragment != null && !fragment.getTag().isEmpty() && fragment.getTag() != null){
            //Add this tag to the attachments list so we can find it later and remove it
            //in the on pause, and potentially re add it onResume.
            attachedFragmentsList.add(fragment.getTag());
        }*/
        super.onAttachFragment(fragment);
    }

    /********************************************
    Removes all of the fragments from the activity
    when the onPause is called in the life cycle
    Allows the screen to rotate without errors.
     ********************************************/
   /* @Override
    public void onPause(){
    /*    try{
            //If the fragment list isn't null proceed in removing them all.
            if(attachedFragmentsList != null){
                for(String fragTag : attachedFragmentsList){
                    //Find the fragment by tag
                    Fragment frag = this.getFragmentManager().findFragmentByTag(fragTag);
                    //Remove the fragment from the activity.
                    this.getFragmentManager().beginTransaction().remove(frag).commit();
                }//END FOR
            }//END IF
        }
        catch(Exception e){
            //Log to verbose.
            Log.v("Canary Media Player", "Error removing all fragments from main activity on rotate || " + e.toString());
        }//END TRY CATCH 
        super.onPause();

    }*/

   /* @Override
    public void onResume(){
        try{
            //If the fragment list isn't null proceed in removing them all.
            if(attachedFragmentsList != null){
                for(String fragTag : attachedFragmentsList){
                    //Find the fragment by tag
                    Fragment frag = this.getFragmentManager().findFragmentByTag(fragTag);
                    //Remove the fragment from the activity.
                    this.getFragmentManager().beginTransaction().add(frag, frag.getTag()).commit();
                }//END FOR
            }//END IF
        }
        catch(Exception e){
            //Log to verbose.
            Log.v("Canary Media Player", "Error adding all fragments from main activity on rotate || " + e.toString());
        }//END TRY CATCH
        super.onResume();
    }*/


}
