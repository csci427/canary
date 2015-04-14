package edu.umt.csci427.canary;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

/**
 * Created by Rye on 4/2/2015.
 *
 * Extends the BroadcastReceiver abstract class. Receives the intents sent out by OpenICE.java
 * These intents contain the medical data (simplified until abstract factory in place).
 */

public class OpenICEIntentReceiver extends BroadcastReceiver {

    private String METRIC_ID;
    private TextView dataView;
    private Activity MainActivity; ///Needs main activity to become registered
    private Fragment MonitorFrag;

    public OpenICEIntentReceiver(Activity mainActivity, TextView tv){
        if(mainActivity != null) this.MainActivity = mainActivity;
        if(tv != null) this.dataView = tv;
    }

    public OpenICEIntentReceiver(Fragment monitorFragment, TextView tv){
        if(monitorFragment != null) this.MonitorFrag = monitorFragment;
        if(tv != null) this.dataView = tv;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Double data = intent.getDoubleExtra(OpenICE.METRIC_VALUE, -1);
        dataView.setText(Double.toString(data));
    }
}
