/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.entries;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import upatras.agentsimulation.agent.AbstractAgent;
import upatras.city.City;
import upatras.city.Job;
import upatras.city.house.SmartHouse;
import upatras.city.people.Adult;
import upatras.city.people.Child;
import upatras.combinedsimulation.SmartGridInstance;
import upatras.electricaldevicesimulation.simulationcore.DeviceBase.AbstractSimulatableDevice;
import upatras.electricaldevicesimulation.simulationcore.DeviceBase.DevicePowerProductionCollector;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerConsumptionVariable;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerProductionVariable;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerVariable;
import upatras.simulationmodels.devices.CoalPowerPlant;
import upatras.simulationmodels.devices.GasPowerPlant;
import upatras.smartdevices.gridcontrol.IdealPowerPlant;
import upatras.smartdevices.gridcontrol.PowerGenerationController;
import upatras.smartdevices.gridcontrol.VirtualPowerPlant;
import upatras.utilitylibrary.library.dataextraction.MeasurableItemCollector;
import upatras.utilitylibrary.library.measurements.types.Power;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;
import upatras.utilitylibrary.library.randomnumbergenerators.RandomGenerator;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Paris
 */
public class EntryPoint {

    public static void main(String[] args) {

        SmartGridInstance sgi = new SmartGridInstance(new DateTime(2018, 1, 1, 3, 0, 0));

        City city = new City(sgi, 100000);

        SmartHouse house = city.lookforahouse();
        Job job = city.lookforajob();

        ArrayList<SmartHouse> occupied_houses = new ArrayList<>();

        Random rand = RandomGenerator.getRandom();

        while (house != null && job != null) {

            Adult worker = new Adult(house, job);

            if (rand.nextInt(1) == 1) {
                Job job2 = city.lookforajob();
                if (job2 != null) {
                    Adult worker2 = new Adult(house, job2);
                }
            }
            int children = rand.nextInt(2);
            if (children > 2) {
                new Child(house);
            }


            occupied_houses.add(house);

            house = city.lookforahouse();
            job = city.lookforajob();
        }
        if (house == null) {
            System.out.println("City is out of houses");
        } else {
            System.out.println("City is out of jobs");
        }


        System.out.println(city.info());
        System.out.println("Agentcount : " + AbstractAgent.getAgentCount());
        System.out.println("Devicecount : " + AbstractSimulatableDevice.getDeviceCount());

        MeasurableItemCollector a = null;
        MeasurableItemCollector b = null;

        if (true) {
            PowerGenerationController generation_controller = new PowerGenerationController(city);

            CoalPowerPlant coalpp = new CoalPowerPlant(ComplexPower.MegaWatts(3.2, 0), ComplexPower.MegaWatts(6, 0), city);
            GasPowerPlant gaspp = new GasPowerPlant(ComplexPower.MegaWatts(0.4, 0), ComplexPower.MegaWatts(1.5, 0), city);
            //PowerPlant idpp = new PowerPlant(ComplexPower.MegaWatts(3.6, 0), ComplexPower.MegaWatts(10, 0), ComplexPower.KiloWatts(1, 0), city);


            generation_controller.addPowerPlant(coalpp);
            generation_controller.addPowerPlant(gaspp);
            //generation_controller.addPowerPlant(idpp);
            if (true) {
                VirtualPowerPlant virtpp = new VirtualPowerPlant(city.simulationinstance, occupied_houses);
                generation_controller.addAuxilaryPowerPlant(virtpp);
            }

            generation_controller.preprocess();

            a = DevicePowerProductionCollector.getCollector(coalpp);
            //b = DevicePowerProductionCollector.getCollector(gaspp);

        } else if (false) {
            IdealPowerPlant idpp = new IdealPowerPlant(city);
        }

        MeasurableItemCollector power_collector = new MeasurableItemCollector(city.getVariable(PowerVariable.class));
        MeasurableItemCollector power_collector1 = new MeasurableItemCollector(city.getVariable(PowerConsumptionVariable.class));
        MeasurableItemCollector power_collector2 = new MeasurableItemCollector(city.getVariable(PowerProductionVariable.class));

        sgi.preprocess(DeviceSimulationInstance.RunMode.parallel, Duration.standardMinutes(1), Duration.standardMinutes(1));

        boolean streaming_visualize = false;
        if (streaming_visualize) {
            new MeasurableItemCollector(occupied_houses.get(0).refrigerator.internal_temp).streamingVisualize();
            new MeasurableItemCollector(occupied_houses.get(1).refrigerator.internal_temp).streamingVisualize();
            new MeasurableItemCollector(occupied_houses.get(2).refrigerator.internal_temp).streamingVisualize();
        }

        Duration dur = Duration.standardHours(18);

        sgi.asi.event_resolver.erg.debug = true;
        sgi.advanceDuration(dur);

        System.out.println("Simulation Ended");

        if (true) {
            sgi.dsi.dag_executor.dag.compressedVisualize();
            sgi.asi.event_resolver.compressedVisualize();
        }

        if (!streaming_visualize) {
            power_collector.compressedVisualize();
            power_collector1.compressedVisualize();
            power_collector2.compressedVisualize();
        }

        power_collector.toCSV();
        power_collector1.toCSV();
        power_collector2.toCSV();


        if (a != null) {
            a.compressedVisualize().setTitle("Coal generation");
            Power coal_energy_sum = new Power();
            for (int i = 0; i < a.dataset.size(); i++) {
                coal_energy_sum = coal_energy_sum.plus(((ComplexPower) a.dataset.get(i).data).real);
            }
            coal_energy_sum = coal_energy_sum.dividedBy(a.dataset.size());
            System.out.println("Paid " + (coal_energy_sum.toEnergy(dur).value * 2.77778e-7 * 20.0) + "E for coal");
        }


        if (b != null) {
            b.compressedVisualize().setTitle("Gas generation");
            Power gas_energy_sum = new Power();
            for (int i = 0; i < b.dataset.size(); i++) {
                gas_energy_sum = gas_energy_sum.plus(((ComplexPower) b.dataset.get(i).data).real);
            }
            gas_energy_sum = gas_energy_sum.dividedBy(b.dataset.size());
            System.out.println("Paid " + (gas_energy_sum.toEnergy(dur).value * 2.77778e-7 * 40.0) + "E for gas");
        }
        sgi.shutdown();


        double error = 0;

        for (int i = 120; i < power_collector.dataset.size() - 10; i++) {
            Power power = ((ComplexPower) power_collector1.dataset.get(i).data).real.minus(((ComplexPower) power_collector2.dataset.get(i + 1).data).real);
            error += Math.pow(power.value, 2);
        }

        System.out.println("Error " + error / power_collector.dataset.size());


    }
}
