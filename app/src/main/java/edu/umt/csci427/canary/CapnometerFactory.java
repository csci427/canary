package edu.umt.csci427.canary;


import rosetta.MDC_AWAY_CO2_ET;
import rosetta.MDC_CO2_RESP_RATE;

/**
 * Created by RYELAPTOP on 4/6/2015.
 */
public class CapnometerFactory extends OpenICEAbstractFactory {

    public static final String factoryName = "CAPNOMETER_FACTORY";

    @Override
    Monitor PackageOpenICESimulatedData(String data) {

        Monitor myData = null;
        if(data != null && data.equals("Simulated Capnometer etCO2")){//comment out if you want all values to go through
            myData = Monitor.newInstance(MDC_AWAY_CO2_ET.VALUE, "etCO2", data);//TODO: true should match a unique device id.
        }
        else if(data != null && data.equals("Simulated Capnometer Resp Rate")){
            myData = Monitor.newInstance(MDC_CO2_RESP_RATE.VALUE, "Respiratory Rate", data);
        }
        else{
            System.out.println(factoryName + "Data is null or not simulated.");
        }
        return myData;
    }

    @Override
    Monitor PackageOpenICERealTimeData(String data) {
        return null;
    }
}
