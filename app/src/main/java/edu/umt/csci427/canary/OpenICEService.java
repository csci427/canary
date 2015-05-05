package edu.umt.csci427.canary;

import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.os.Process;

/**
 * Created by Rye on 3/15/2015.
 */

/**
OpenICEService.java, turns hello-openice into an Android Service to be used
by the Canary Media Player. This service should be started when the application is started
and destroyed when the app is shut down.
 */
public class OpenICEService extends Service {

    //connects with hello-openice not giving the user the ability to change this becasue
    //it is specific for the adapter, which they cannot change.
    private int domainId = 15;
    ///Thread to run hello-openice, this is needed so that it won't interfere or lock down the UI.
    private Thread thread = null;
    //Wrapper class of OpenICE Interface
    private OpenICE myICE = null;


    @Override
    public void onCreate(){

        //Create new instance of OpenICE interface if needed/
        if(myICE == null){
            myICE = new OpenICE(getApplicationContext(), domainId, this);
        }

        /**
        CREATE THREAD AND SET PRIORITY TO RUN IN BACKGROUND
         */
        if(this.thread == null){
            thread = new Thread(myICE);
            thread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        //Only start the thread if it is not already running.
        if(this.thread.getState() == Thread.State.NEW){
            this.thread.start();
        }

        ///Returning START_STICKY keeps this service alive if it
        ///somehow shuts down.
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    //Interrupt the thread to stop it. Android currently does not support
    //Thread.stop(); it has issues.
    @Override
    public void onDestroy(){
        this.thread.interrupt();
        super.onDestroy();

    }




}
