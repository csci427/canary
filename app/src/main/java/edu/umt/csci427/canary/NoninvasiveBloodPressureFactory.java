package edu.umt.csci427.canary;

import rosetta.MDC_PRESS_CUFF_DIA;
import rosetta.MDC_PRESS_CUFF_SYS;
import rosetta.MDC_PULS_RATE_NON_INV;

import static edu.umt.csci427.canary.ThresholdData.*;

/**
 * Created by RYELAPTOP on 4/6/2015.
 */
public class NoninvasiveBloodPressureFactory extends OpenICEAbstractFactory{

    public static final String factoryName = "NoninvasiveBloodPressureFactory";
    @Override
    Monitor PackageOpenICESimulatedData(String data) {

        // set threshold fragment with appropriate defaults
        ThresholdFragment thresholdFragment = new twoThresholdFragment();

        Monitor myData = null;
        if(data != null && data.equals("Simulated Noninvasive BP SYS")){//comment out if you want all values to go through
            myData = Monitor.newInstance(data, "mmHg", MDC_PRESS_CUFF_SYS.VALUE);//TODO: true should match a unique device id.

            // set thresholds to appropriate values
            thresholdFragment.setDefaultThresholds(SYSTOLIC_BP_HIGH, SYSTOLIC_BP_LOW);
            thresholdFragment.setMax(SYSTOLIC_BP_MAX);
        }
        else if(data != null && data.equals("Simulated Noninvasive BP DIA")){//comment out if you want all values to go through
            myData = Monitor.newInstance(data, "mmHg", MDC_PRESS_CUFF_DIA.VALUE);//TODO: true should match a unique device id.

            // set thresholds to appropriate values
            thresholdFragment.setDefaultThresholds(DIASYSTOLIC_HIGH, DIASYSTOLIC_LOW);
            thresholdFragment.setMax(DIASYSTOLIC_MAX);
        }
        else if(data != null && data.equals("Simulated Noninvasive BP pulse rate")){//comment out if you want all values to go through
            myData = Monitor.newInstance(data, "BPM", MDC_PULS_RATE_NON_INV.VALUE);//TODO: true should match a unique device id.

            // set thresholds to appropriate values
            thresholdFragment.setDefaultThresholds(PULSE_RATE_HIGH, PULSE_RATE_LOW);
            thresholdFragment.setMax(PULSE_RATE_MAX);
        }
        else{
            System.out.println(factoryName + "Data is null or not simulated.");
        }

        thresholdFragment.setMonitor(myData);
        myData.setThresholdFragment(thresholdFragment);

        return myData;
    }

    @Override
    Monitor PackageOpenICERealTimeData(String data) {
        return null;
    }
}
