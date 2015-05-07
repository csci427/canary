package edu.umt.csci427.canary;

import android.content.Context;

/**
 * Created by aaron on 5/6/15.
 */
public class AlertServiceTimerTask {

    private Context context;
    private long seconds;
    AlertServiceCountDownTimer countDownTimer;


    public AlertServiceTimerTask(Context context, long seconds) {
        this.context = context;
        this.seconds = seconds;
        set();
    }

    public void set() {
        countDownTimer = new AlertServiceCountDownTimer(seconds*1000L, context);
        countDownTimer.start();
    }

    public void reset() {
        countDownTimer.cancel();
        set();
    }

    public void cancel() {
        countDownTimer.cancel();
    }
}
