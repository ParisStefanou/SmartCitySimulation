/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.entries;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import upatras.agentsimulation.agent.AbstractAgent;
import upatras.combinedsimulation.SmartGridInstance;
import upatras.electricaldevicesimulation.simulationcore.DeviceBase.AbstractSimulatableDevice;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerConsumptionVariable;
import upatras.simulationmodels.devices.highaccuracy.SmartRefrigerator;
import upatras.simulationmodels.enviroments.OutdoorEnviroment;
import upatras.smartdevices.gridcontrol.VirtualPowerPlant;
import upatras.utilitylibrary.library.dataextraction.MeasurableItemCollector;
import upatras.utilitylibrary.library.measurements.types.Power;
import upatras.utilitylibrary.library.measurements.types.Temperature;
import upatras.utilitylibrary.library.measurements.types.Volume;
import upatras.utilitylibrary.library.randomnumbergenerators.RandomGenerator;

import java.util.Random;

/**
 * @author Paris
 */
public class VirtualPowerPlantTest {

    static Random rand = RandomGenerator.getRandom();

    public static void main(String[] args) {

        DateTime starttime = new DateTime(2018, 1, 1, 0, 0, 0);

        SmartGridInstance sgi = new SmartGridInstance(starttime);

        VirtualPowerPlant plant = new VirtualPowerPlant(sgi.dsi);

        OutdoorEnviroment out = new OutdoorEnviroment("outside", sgi.dsi, Temperature.degreecelsius(27));

        MeasurableItemCollector temp = null;

        for (int i = 0; i < 1000; i++) {

            OutdoorEnviroment house = new OutdoorEnviroment("A house", out, new Temperature(27));
            SmartRefrigerator refrigerator = new SmartRefrigerator(house, new Power(600), new Volume(6),
                    new Temperature(6), new Temperature(6), new Temperature(8), new Temperature(4), 0.15);
            plant.addRefrigerator(refrigerator);

            if (i == 0) {
                temp = new MeasurableItemCollector(refrigerator.internal_temp);
            }
        }
        MeasurableItemCollector evc1 = new MeasurableItemCollector(out.getVariable(PowerConsumptionVariable.class));

        Duration event_duration;
        if (false) {
            System.out.println("VPP max available production " + plant.getMaxAvailablePowerProduction());
            Power request = plant.getMaxAvailablePowerProduction();
            System.out.println("VPP remaining duration at max production " + plant.getMaxAvailableProductionDuration(request).toStandardSeconds());
            System.out.println("Will request extra production :" + request + " for :" + plant.getMaxAvailableProductionDuration(request).toStandardSeconds());

            event_duration = plant.getMaxAvailableProductionDuration(request);
            plant.producePower(starttime.plusMinutes(1), event_duration, request.multipliedBy(0.9), 1);
            System.out.println("Agentcount : " + AbstractAgent.getAgentCount());
            System.out.println("Devicecount : " + AbstractSimulatableDevice.getDeviceCount());

        }

        if (true) {
            Power request = plant.getMaxAvailablePowerProduction().multipliedBy(0.5);
            for (int i = 0; i < 36; i++) {
                event_duration = plant.getMaxAvailableProductionDuration(request);
                starttime = starttime.plusMinutes(1);
                double mod = Math.sin(2*i/36.0 * Math.PI);
                if (mod > 0.1)
                    plant.producePower(starttime, Duration.standardMinutes(1), request.multipliedBy(mod), 1);
                else {
                    if (mod < -0.1)
                        plant.consumePower(starttime, Duration.standardMinutes(1), request.multipliedBy(-mod), 1);
                    else
                        plant.consumePower(starttime, Duration.standardMinutes(1), new Power(0.1), 1);
                }
            }
        }
        //------------------------------------------------------------------------------------------
        if (false) {
            System.out.println("VPP max available consumption " + plant.getMaxAvailablePowerConsumption());
            Power request2 = plant.getMaxAvailablePowerConsumption();
            System.out.println("VPP remaining duration at max consumption " + plant.getMaxAvailableProductionDuration(request2).toStandardSeconds());
            System.out.println("Will request extra consumption :" + request2);

            plant.consumePower(starttime.plus(event_duration.multipliedBy(3)), plant.getMaxAvailableProductionDuration(request2), request2.multipliedBy(0.9), 1);
            System.out.println("Agentcount : " + AbstractAgent.getAgentCount());
            System.out.println("Devicecount : " + AbstractSimulatableDevice.getDeviceCount());
        }

        sgi.preprocess(DeviceSimulationInstance.RunMode.parallel, Duration.standardMinutes(1), Duration.standardMinutes(1));
        sgi.advanceDuration(Duration.standardMinutes(40));

        evc1.visualize();
        if (temp != null) temp.visualize();

        sgi.shutdown();

    }

}
