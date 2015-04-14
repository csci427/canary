package edu.umt.csci427.canary;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by aaron on 3/24/15.
 */
public class AlertService extends IntentService {

    private AudioManager myAudioManager;
    private AudioManager.OnAudioFocusChangeListener myOnAudioFocusChangeListener;

    public AlertService() {super(AlertService.class.getSimpleName());}

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            double value = intent.getDoubleExtra("METRIC_VALUE", -1);

            if (value > 150){
                myAudioManager.requestAudioFocus(myOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        // create intent filter
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(OpenICE.ICE_DATA);

        // register receiver
        LocalBroadcastManager.getInstance(getApplication())
                .registerReceiver(receiver, intentFilter);

        // create AudioManager
        myAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        // if AudioFocus gained set volume to 0
        myOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                if (focusChange == myAudioManager.AUDIOFOCUS_GAIN) {
                    myAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                }
            }
        };
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    }


}
