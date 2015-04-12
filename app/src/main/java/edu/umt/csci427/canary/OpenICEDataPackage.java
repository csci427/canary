package edu.umt.csci427.canary;

import java.util.Dictionary;

/**
 * Open ICE data to be adapted for the Media Player
 */
public class OpenICEDataPackage {

    /********************
     *Attributes for the data package.
     ********************/

    ///Device receiving from
    public String MetricID = "";
    ///User data, I don't know what this I think we want to avoid caring about this.
    public double[] UserData = null;
    ///Will store data we care about
    public double MetricValue = -1;
    ///Simulated or real data
    public String DataType = "";

    public static final String SIMULATED = "SIMULATED";
    public static final String REAL_TIME = "REAL_TIME";

    //TODO: store time?

    ///Default Constructor
    public OpenICEDataPackage(String metricId, double metricValue, boolean isSimulatedData){
        ///Set the values of the package to be returned.
        if((!metricId.isEmpty() || metricId == null) && metricValue > 0){
            this.MetricID = metricId;
            this.MetricValue = metricValue;

            //Set simulated or realtime data
            if(isSimulatedData){
                this.DataType = SIMULATED;
            }
            else{
                this.DataType = REAL_TIME;
            }
        }
    }


}
