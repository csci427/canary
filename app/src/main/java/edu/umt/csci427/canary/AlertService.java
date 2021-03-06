package edu.umt.csci427.canary;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import rosetta.MDC_PRESS_CUFF_DIA;
import rosetta.MDC_PRESS_CUFF_SYS;

/**
 * Created by aaron on 3/24/15.
 */
public class AlertService extends IntentService {

    private AudioManager myAudioManager;
    private AudioManager.OnAudioFocusChangeListener myOnAudioFocusChangeListener;
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    public final static String ALERT = "_ALERT";
    public final static String ALERT_DATA = "status";
    public final static double NO_DATA_VALUE = Double.MIN_VALUE;

    // hold
    ConcurrentHashMap<String, double[]> thresholds = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, AlertServiceTimerTask> noDataTimers = new ConcurrentHashMap<>();
    HashMap<String, Boolean> receiverBound = new HashMap<>();

    public AlertService() {super(AlertService.class.getSimpleName());}

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String metricId = intent.getAction();

            if (receiverBound.get(metricId) != null && receiverBound.get(metricId)) {

                // reset no data timer
                AlertServiceTimerTask alertServiceTimerTask = noDataTimers.get(metricId);
                alertServiceTimerTask.reset();

                // get threshold values
                double[] thresholdValues = thresholds.get(metricId);
                double high = thresholdValues[0];
                double low = thresholdValues[1];

                // get value from intent
                double value = intent.getDoubleExtra(OpenICE.METRIC_VALUE, -1);

                Log.v("ZZZ", intent.getAction() + " " + Double.toString(value) + " " + high + " " + low + " " + (value >= high || value <= low));

                // broadcast status intent, used to set colors on the UI if it's currently active
                Intent alertIntent = new Intent(metricId + ALERT);
                if (value >= high || value <= low) {
                    myAudioManager.requestAudioFocus(myOnAudioFocusChangeListener,
                    AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                    Intent i = new Intent(context, LineService.class);
                    stopService(i);

                    alertIntent.putExtra(ALERT_DATA, true);

                } else {
                    alertIntent.putExtra(ALERT_DATA, false);
                }
                LocalBroadcastManager.getInstance(getApplication()).sendBroadcast(alertIntent);
            }
        }
    };

    public class LocalBinder extends Binder {
        AlertService getService() {
            // Return this instance of LocalService so clients can call public methods
            return AlertService.this;
        }
    }

    public void removeListener(String metric_id){
        receiverBound.put(metric_id,false);
        AlertServiceTimerTask countDownTimer = noDataTimers.get(metric_id);
        countDownTimer.cancel();
    }

    public void CreateOrModifyListener(String metric_id, double high, double low){

        // set threshold values
        thresholds.put(metric_id, new double[]{high,low});

        // set no data timer

        int seconds;
        if (metric_id == MDC_PRESS_CUFF_DIA.VALUE || metric_id == MDC_PRESS_CUFF_SYS.VALUE){
            seconds = ThresholdData.NO_DATA_TIMEOUT_BP;
        } else {
            seconds = ThresholdData.NO_DATA_TIMEOUT_NON_BP;
        }

        AlertServiceTimerTask countDownTimer = noDataTimers.get(metric_id);
        if (countDownTimer != null) {
            AlertServiceTimerTask timerTask = noDataTimers.get(metric_id);
            timerTask.cancel();
            timerTask = new AlertServiceTimerTask(seconds, metric_id,
                    myAudioManager, myOnAudioFocusChangeListener, getApplication());
            noDataTimers.put(metric_id, timerTask);
        } else {
            // create no data timer
            AlertServiceTimerTask timerTask = new AlertServiceTimerTask(seconds, metric_id,
                    myAudioManager, myOnAudioFocusChangeListener, getApplication());
            noDataTimers.put(metric_id, timerTask);
        }

        // check that receiver is registered for that intent
        if (receiverBound.get(metric_id) == null) {
            // create intent filter
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(metric_id);

            // register receiver
            LocalBroadcastManager.getInstance(getApplication())
                    .registerReceiver(receiver, intentFilter);

            receiverBound.put(metric_id,true);
        } else {
            receiverBound.put(metric_id,true);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

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

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        ///Returning START_STICKY keeps this service alive if it
        ///somehow shuts down.
        return START_STICKY;
    }
}