package edu.umt.csci427.canary;

import android.app.IntentService;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;
import android.widget.Toast;

public class LineService extends IntentService {
    //create variables
    private static final String TAG = "canary-media-player.LineService";
    private boolean stopped = false;
    private String statusInfo = "";
    private AudioRecord recorder = null;
    private AudioTrack track = null;
    private short[] buffer = new short[160];

    public LineService() {
        super("LineService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(LineService.this, "Service is created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(LineService.this, "Service is started", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override //what we want the service to do
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            run(); //start listening to mic/feed to speaker
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopped = true;
        //Frees resources after the loop completes so that it can be run again
        recorder.stop();
        recorder.release();
        track.stop();
        track.release();
        Toast.makeText(LineService.this, "Service is stopped", Toast.LENGTH_SHORT).show();
    }

    public void run()
    {
        //Initialize buffer to hold recorded audio data, start recording, and start playback
        try
        {
            //smaller buffer size = more cpu usage but less latency
            //decrease the buffer size by change the N*x functions where N=smallest allowed buffer
            int N = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
            recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT, N*5);//Arbitrarily set to N*10 in most examples
            track = new AudioTrack(AudioManager.STREAM_MUSIC, 8000,
                    AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT, N*5, AudioTrack.MODE_STREAM);
            recorder.startRecording();
            track.play();
            /*
             * Loops until stopped boolean is set to true
             * Reads the data from the recorder and writes it to the audio track object for playback
             */
            while (!stopped) {
                int n = recorder.read(buffer, 0, buffer.length);
                track.write(buffer, 0, n);
            }
        }
        catch(Throwable x)
        {
            Toast.makeText(LineService.this, "Failed to start", Toast.LENGTH_SHORT).show();
        }
    }
}
