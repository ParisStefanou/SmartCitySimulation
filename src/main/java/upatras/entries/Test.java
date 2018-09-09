package upatras.entries;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.simulationmodels.devices.highaccuracy.SmartRefrigerator;
import upatras.simulationmodels.enviroments.OutdoorEnviroment;
import upatras.utilitylibrary.library.measurements.types.Power;
import upatras.utilitylibrary.library.measurements.types.Temperature;
import upatras.utilitylibrary.library.measurements.types.Volume;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

import java.util.Date;

public class Test {

        public static void main(String[] args) {

            DeviceSimulationInstance di=new DeviceSimulationInstance();

            OutdoorEnviroment out=new OutdoorEnviroment("Outdoors",di,null);
            SmartRefrigerator refrigerator = new SmartRefrigerator(out, new Power(600), new Volume(6),
                    new Temperature(6), new Temperature(6), new Temperature(8), new Temperature(4), 0.15);
            refrigerator.chargeFullyNow(DateTime.now(),Duration.standardHours(1));
            di.advanceDuration(Duration.standardHours(2));
            System.out.println(refrigerator.getStoredEnergy());

        }

    }

