package edu.umt.csci427.canary;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Toast;

/**
 * Created by aaron on 5/7/15.
 */
public class AlertServiceCountDownTimer extends CountDownTimer{

    private Context context;

    public AlertServiceCountDownTimer(long millisInFuture, Context context){
        super(millisInFuture, Long.MAX_VALUE);
        this.context = context;
    }

    @Override
    public void onTick(long millisUntilFinished) {}

    @Override
    public void onFinish() {
        Toast toast = Toast.makeText(context, "Test", Toast.LENGTH_SHORT);
        toast.show();
    }
}
