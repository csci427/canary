package edu.umt.csci427.canary;



import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;


public class MainActivity extends ActionBarActivity implements
        MonitorFragment.OnFragmentInteractionListener,
        ThresholdFragment.OnFragmentInteractionListener,
        AddMonitorFragment.AddMonitorListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {

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
        getFragmentManager().beginTransaction()
                .add(R.id.container, MonitorFragment.newInstance("title1", "some", 1.2f))
                .commit();
    }


}
