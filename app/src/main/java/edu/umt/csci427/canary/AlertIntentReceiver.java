package edu.umt.csci427.canary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

/**
 * Created by aaron on 5/6/15.
 */
public class AlertIntentReceiver extends BroadcastReceiver{
    private View view;

    public AlertIntentReceiver(View view){
        this.view = view;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getBooleanExtra(AlertService.ALERT_DATA, false)) {
            view.setBackgroundColor(Color.RED);
        } else {
            view.setBackgroundColor(Color.parseColor("#AA33b5e5"));
        }
    }
}
