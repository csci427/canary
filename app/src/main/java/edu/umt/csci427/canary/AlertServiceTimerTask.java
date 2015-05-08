package edu.umt.csci427.canary;

import android.content.Context;
import android.media.AudioManager;

/**
 * Created by aaron on 5/6/15.
 */
public class AlertServiceTimerTask {

    private long seconds;
    AlertServiceCountDownTimer countDownTimer;
    String metricId;
    Context context;
    AudioManager.OnAudioFocusChangeListener myOnAudioFocusChangeListener;
    AudioManager myAudioManager;


    public AlertServiceTimerTask(long seconds, String metricId,
                                 AudioManager myAudioManager,
                                 AudioManager.OnAudioFocusChangeListener myOnAudioFocusChangeListener,
                                 Context context) {
        this.seconds = seconds;
        this.metricId = metricId;
        this.myOnAudioFocusChangeListener = myOnAudioFocusChangeListener;
        this.myAudioManager = myAudioManager;
        this.context = context;
        set();
    }

    public void set() {
        countDownTimer = new AlertServiceCountDownTimer(seconds*1000L, metricId,
                myAudioManager, myOnAudioFocusChangeListener, context);
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
