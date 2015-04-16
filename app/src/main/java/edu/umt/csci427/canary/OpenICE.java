package edu.umt.csci427.canary;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.domain.DomainParticipantFactoryQos;
import com.rti.dds.infrastructure.ConditionSeq;
import com.rti.dds.infrastructure.Duration_t;
import com.rti.dds.infrastructure.RETCODE_NO_DATA;
import com.rti.dds.infrastructure.ResourceLimitsQosPolicy;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.infrastructure.StringSeq;
import com.rti.dds.infrastructure.WaitSet;
import com.rti.dds.subscription.InstanceStateKind;
import com.rti.dds.subscription.SampleInfo;
import com.rti.dds.subscription.SampleInfoSeq;
import com.rti.dds.subscription.SampleStateKind;
import com.rti.dds.subscription.ViewStateKind;
import com.rti.dds.topic.Topic;

import org.mdpnp.rtiapi.data.QosProfiles;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Rye on 3/17/2015.
 */
public class OpenICE implements Runnable {

    //connects with hello-openice
    private int domainId = 15;
    ///Thread to run hello-openice, this is needed so that it won't interfere or lock down the UI.
    private HandlerThread thread = null;
    ///Main Activity Context
    private Context ActivityContext = null;
    ///Needed to set custom qos profiles
    private DomainParticipantFactoryQos factoryQos = null;
    ///Creates participants
    private DomainParticipantFactory participantFactory = null;
    private DomainParticipant participant = null;
    private Topic sampleArrayTopic = null;
    private Topic numericTopic = null;
    private ice.SampleArrayDataReader saReader = null;
    private ice.NumericDataReader nReader = null;
    private WaitSet ws = null;
    private ConditionSeq cond_seq = null;
    private Duration_t timeout = null;
    private ice.SampleArraySeq sa_data_seq = null;
    private ice.NumericSeq n_data_seq = null;
    private SampleInfoSeq info_seq = null;

    //Sends out intents
    private LocalBroadcastManager broadcaster;
    private Service parentService;

    // intent action to store metric value in an intent
    static final public String METRIC_VALUE = "METRIC_VALUE";

    public OpenICE(Context context, int domain, Service serv){
        this.domainId = domain;
        this.ActivityContext = context;
        this.parentService = serv;
        this.broadcaster = LocalBroadcastManager.getInstance(parentService);
    }


    @Override
    public void run() {
        /********************************
         Things to be run on service thread
         ********************************/
        try {

            if (this.LoadRTILibraries()) {

                this.SetCustomQosProfiles();

                // A domain participant is the main access point into the DDS domain.  Endpoints are created within the domain participant
                this.CreateParticipant();

                // Inform the participant about the sample array data type we would like to use in our endpoints
                ice.SampleArrayTypeSupport.register_type(participant, ice.SampleArrayTypeSupport.get_type_name());

                // Inform the participant about the numeric data type we would like to use in our endpoints
                ice.NumericTypeSupport.register_type(participant, ice.NumericTypeSupport.get_type_name());

                // A topic the mechanism by which reader and writer endpoints are matched.
                this.CreateSampleArrayTopic();
                // A second topic if for Numeric data
                this.CreateNumericTopic();
                // Create a reader endpoint for samplearray data
                this.CreateICESamepleArrayDataReader();

                this.CreateICENumericDataReader();
                // A waitset allows us to wait for various status changes in various entities
                this.CreateWaitSet();

                // Here we configure the status condition to trigger when new data becomes available to the reader
                saReader.get_statuscondition().set_enabled_statuses(StatusKind.DATA_AVAILABLE_STATUS);

                nReader.get_statuscondition().set_enabled_statuses(StatusKind.DATA_AVAILABLE_STATUS);

                // And register that status condition with the waitset so we can monitor its triggering
                ws.attach_condition(saReader.get_statuscondition());

                ws.attach_condition(nReader.get_statuscondition());

                // will contain triggered conditions
                this.CreateConditionSeq();

                // we'll wait as long as necessary for data to become available
                this.CreateDurationTimeout();

                // Will contain the data samples we read from the reader
                this.CreateICESampleArraySeq();

                this.CreateICENumericSeq();

                // Will contain the SampleInfo information about those data

                this.CreateSampleInfoSeq();
                // This loop will repeat until the process is terminated
                this.Process();
            }//END TRY TOP
        }
        catch(Exception e)
        {

        }
    }

    public void Process(){

        for (; ; ) {
            // Wait for a condition to be triggered
            ws.wait(cond_seq, timeout);
            // Check that our status condition was indeed triggered
            if (cond_seq.contains(saReader.get_statuscondition())) {
                // read the actual status changes
                int status_changes = saReader.get_status_changes();

                // Ensure that DATA_AVAILABLE is one of the statuses that changed in the DataReader.
                // Since this is the only enabled status (see above) this is here mainly for completeness
                if (0 != (status_changes & StatusKind.DATA_AVAILABLE_STATUS)) {
                    try {
                        // Read samples from the reader
                        saReader.read(sa_data_seq, info_seq, ResourceLimitsQosPolicy.LENGTH_UNLIMITED, SampleStateKind.NOT_READ_SAMPLE_STATE, ViewStateKind.ANY_VIEW_STATE, InstanceStateKind.ALIVE_INSTANCE_STATE);

                        // Iterator over the samples
                        for (int i = 0; i < info_seq.size(); i++) {
                            SampleInfo si = (SampleInfo) info_seq.get(i);
                            ice.SampleArray data = (ice.SampleArray) sa_data_seq.get(i);
                            // If the updated sample status contains fresh data that we can evaluate
                            if (si.valid_data) {
                                System.out.println(data);
                            }

                        }
                    } catch (RETCODE_NO_DATA noData) {
                        // No Data was available to the read call
                    } finally {
                        // the objects provided by "read" are owned by the reader and we must return them
                        // so the reader can control their lifecycle
                        saReader.return_loan(sa_data_seq, info_seq);
                    }
                }
            }
            if (cond_seq.contains(nReader.get_statuscondition())) {
                // read the actual status changes
                int status_changes = nReader.get_status_changes();

                // Ensure that DATA_AVAILABLE is one of the statuses that changed in the DataReader.
                // Since this is the only enabled status (see above) this is here mainly for completeness
                if (0 != (status_changes & StatusKind.DATA_AVAILABLE_STATUS)) {
                    try {
                        // Read samples from the reader
                        nReader.read(n_data_seq, info_seq, ResourceLimitsQosPolicy.LENGTH_UNLIMITED, SampleStateKind.NOT_READ_SAMPLE_STATE, ViewStateKind.ANY_VIEW_STATE, InstanceStateKind.ALIVE_INSTANCE_STATE);

                        // Iterator over the samples
                        for (int i = 0; i < info_seq.size(); i++) {
                            SampleInfo si = (SampleInfo) info_seq.get(i);
                            ice.Numeric data = (ice.Numeric) n_data_seq.get(i);
                            // If the updated sample status contains fresh data that we can evaluate
                            if (si.valid_data) {

                                Log.v(this.getClass().getSimpleName(), data.metric_id + " " + data.value);

                                ///Broadcast an intent to the Media player fragments
                                this.SendIntentWithOpenICEData(
                                        ///Create the intent from valid data.
                                        this.CreateIntentFromICEData(data)
                                );
                               // System.out.println(data);
                            }

                        }
                    } catch (Exception ex) {
                        // No Data was available to the read cal
                        Log.d("Test", ex.toString());
                    } finally {
                        // the objects provided by "read" are owned by the reader and we must return them
                        // so the reader can control their lifecycle
                        nReader.return_loan(n_data_seq, info_seq);
                    }
              }
            }
        }//END FOR(;;)
    }//END LOAD LIBRARIES

    /***********************************************
     * Create the intent to send to the service
     * @param data the data we package up from the data received
     * @return Intent
     ***********************************************/
    public Intent CreateIntentFromICEData(ice.Numeric data){
        ///The intent to send out to the service
        Intent myIntent = null;
        try
        {
           if(data != null) {
               myIntent = new Intent(data.metric_id);
               ///create intent with string message with comma separated values
               myIntent.putExtra(METRIC_VALUE, (double)data.value);
           }
        }
        catch(Exception ex){
            System.out.println("Could not create Intent from ICE data, message: " + ex.toString());
        }
        return myIntent;
    }
    /**********************************************
     * Sends the intent to be caught by the service
     * @param intent the intent to be sent
     * @return TRUE if successful.
     **********************************************/
    public boolean SendIntentWithOpenICEData(Intent intent){
        boolean intentSent = false;
        try{
            //send the intent
            this.broadcaster.sendBroadcast(intent);
            intentSent = true;
        }
        catch(Exception ex){
            System.out.println("Could not send Intent from OpenICE, message: " + ex.toString());
        }
        return intentSent;
    }

    /**************************
    Load the RTI compiled files.
    **************************/
    private boolean LoadRTILibraries(){
        boolean loaded = false;
        try{
            System.loadLibrary("nddscore");
            System.loadLibrary("nddsc");
            System.loadLibrary("nddsjava");
            loaded = true;
        }
        catch(Exception ex){
            Log.d("DDS LOAD COMPILED FILES", "Error loading core libraries, " + ex.toString());
        }
        return loaded;
    }

    /*****************************************************
     Have to set the custom qos profiles provided by mdpnp.
     MUST SET OR WILL NOT WORK.
     *****************************************************/
    private boolean SetCustomQosProfiles(){
        boolean qosSet = false;
        try{
            if(this.factoryQos == null && this.participantFactory == null){
                this.factoryQos = new DomainParticipantFactoryQos();
                this.participantFactory = DomainParticipantFactory.get_instance();
            }
            //Read "USER_QOS_PROFILES.xml" from assets folder and set QOS
            this.factoryQos.profile.string_profile.copy_from(this.ReadQosProfileFromAssets("USER_QOS_PROFILES.xml"));
            this.participantFactory.set_qos(factoryQos);
            qosSet = true;
        }
        catch(Exception ex){
            Log.d("DDS SET QOS PROFILES", "Error setting custom QOS profiles, " + ex.toString());
        }
        return qosSet;
    }

    /*************************************************
     Read the custom QOS Profile from the assets folder.
     *************************************************/
    private StringSeq ReadQosProfileFromAssets(String filename){
        StringSeq xmlSeq = null;
        Collection xmlLines = new ArrayList<String>();
        String line = "";
        try{
            //Get xml file from assets folder
            InputStream xmlStream = this.ActivityContext.getResources().getAssets().open(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(xmlStream));
            while((line = reader.readLine())!=null)
            {
                xmlLines.add(line);
            }
            ///create the StringSeq for the DomainParticipantFactoryQos Object
            xmlSeq = new StringSeq(xmlLines);

        }
        catch(Exception ex){
            Log.d("LOAD QOS PROFILE ASSET", "Error reading custom QOS profiles from assets folder, " + ex.toString());
        }
        return xmlSeq;
    }

    /*************************
     Create participant for dds
     *************************/
    private void CreateParticipant(){
        if(this.participant == null){
            this.participant =  DomainParticipantFactory.get_instance().create_participant(domainId, DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
        }
    }

    /*************************
     Create sample array topic
     *************************/
    private void CreateSampleArrayTopic(){
        if(this.sampleArrayTopic == null){
            this.sampleArrayTopic =  participant.create_topic(ice.SampleArrayTopic.VALUE, ice.SampleArrayTypeSupport.get_type_name(), DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
        }
    }

    /*************************
     Create numeric topic
     *************************/
    private void CreateNumericTopic(){
        if(this.numericTopic == null){
            this.numericTopic =  participant.create_topic(ice.NumericTopic.VALUE, ice.NumericTypeSupport.get_type_name(), DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
        }
    }

    /*************************
     Create iceSampleArrayDataReader
     *************************/
    private void CreateICESamepleArrayDataReader(){
        if(this.saReader == null){
            this.saReader =  (ice.SampleArrayDataReader) participant.create_datareader_with_profile(sampleArrayTopic, QosProfiles.ice_library, QosProfiles.waveform_data, null, StatusKind.STATUS_MASK_NONE);
        }
    }

    /*************************
     Create iceNumericDataReader
     *************************/
    private void CreateICENumericDataReader(){
        if(this.nReader == null){
            this.nReader =  (ice.NumericDataReader) participant.create_datareader_with_profile(numericTopic, QosProfiles.ice_library, QosProfiles.numeric_data, null, StatusKind.STATUS_MASK_NONE);
        }
    }

    /*************************
     Create iceNumericDataReader
     *************************/
    private void CreateWaitSet(){
        if(this.ws == null){
            this.ws =  new WaitSet();
        }
    }

    private void CreateConditionSeq(){
        if(this.cond_seq == null){
            this.cond_seq = new ConditionSeq();
        }
    }

    private void CreateDurationTimeout(){
        if(this.timeout == null){
            this.timeout = new Duration_t(Duration_t.DURATION_INFINITE_SEC, Duration_t.DURATION_INFINITE_NSEC);
        }
    }

    private void CreateICESampleArraySeq(){
        if(this.sa_data_seq == null){
            this.sa_data_seq = new ice.SampleArraySeq();
        }
    }

    private void CreateICENumericSeq(){
        if(this.n_data_seq == null){
            this.n_data_seq =  new ice.NumericSeq();
        }
    }

    private void CreateSampleInfoSeq(){
        if(this.info_seq == null){
            this.info_seq =  new SampleInfoSeq();
        }
    }
}
