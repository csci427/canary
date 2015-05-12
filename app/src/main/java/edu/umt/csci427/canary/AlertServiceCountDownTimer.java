package edu.umt.csci427.canary;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by aaron on 5/7/15.
 */
public class AlertServiceCountDownTimer extends CountDownTimer{

    private String metricId;

    private AudioManager.OnAudioFocusChangeListener myOnAudioFocusChangeListener;
    private AudioManager myAudioManager;
    private Context context;

    public AlertServiceCountDownTimer(long millisInFuture, String metricId,
                                      AudioManager myAudioManager,
                                      AudioManager.OnAudioFocusChangeListener myOnAudioFocusChangeListener,
                                      Context context){
        super(millisInFuture, Long.MAX_VALUE);
        this.metricId = metricId;
        this.myOnAudioFocusChangeListener = myOnAudioFocusChangeListener;
        this.myAudioManager = myAudioManager;
        this.context = context;
    }

    @Override
    public void onTick(long millisUntilFinished) {}

    @Override
    public void onFinish() {

        // silence music
        myAudioManager.requestAudioFocus(myOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        // broadcast status intent, used to set colors on the UI if it's currently active
        Intent alertIntent = new Intent(metricId + AlertService.ALERT);
        alertIntent.putExtra(AlertService.ALERT_DATA, true);
        LocalBroadcastManager.getInstance(context).sendBroadcast(alertIntent);

        // broadcast no data intent
        alertIntent = new Intent(metricId);
        alertIntent.putExtra(OpenICE.METRIC_VALUE, AlertService.NO_DATA_VALUE);
        LocalBroadcastManager.getInstance(context).sendBroadcast(alertIntent);
    }
}
