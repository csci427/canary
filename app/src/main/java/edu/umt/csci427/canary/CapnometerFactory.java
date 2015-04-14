package edu.umt.csci427.canary;

import ice.Numeric;

/**
 * Created by RYELAPTOP on 4/6/2015.
 */
public class CapnometerFactory extends OpenICEAbstractFactory {
    @Override
    OpenICEDataPackage PackageOpenICESimulatedData(ice.Numeric data) {
        return null;
    }

    @Override
    OpenICEDataPackage PackageOpenICERealTimeData(ice.Numeric data) {
        return null;
    }
}
