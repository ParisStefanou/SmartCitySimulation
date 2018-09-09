/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.entries;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import upatras.agentsimulation.agent.AbstractAgent;
import upatras.city.house.SmartHouse;
import upatras.city.people.Adult;
import upatras.city.people.Child;
import upatras.city.shop.FlatPowerShop;
import upatras.combinedsimulation.SmartGridInstance;
import upatras.electricaldevicesimulation.simulationcore.DeviceBase.AbstractSimulatableDevice;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.BasicEnviroment;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerConsumptionVariable;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerProductionVariable;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerVariable;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.TemperatureVariable;
import upatras.simulationmodels.enviroments.OutdoorEnviroment;
import upatras.utilitylibrary.library.dataextraction.MeasurableItemCollector;
import upatras.utilitylibrary.library.measurements.types.Temperature;

/**
 * @author Paris
 */
public class HouseTest {

    public static void main(String[] args) {

        SmartGridInstance sgi = new SmartGridInstance(new DateTime(2018, 1, 1, 0, 0, 0));

        BasicEnviroment outdoors = new OutdoorEnviroment("Outdors", sgi.dsi, Temperature.degreecelsius(27));
        SmartHouse house;

        do {
            house = new SmartHouse(0, 0, 1000, 1000, sgi.asi, outdoors);
        } while (house.pv == null);

        FlatPowerShop shop = new FlatPowerShop(100, 100, 100, 100, sgi.asi, outdoors);

        Adult a1 = new Adult(house, shop);
        Adult a2 = new Adult(house, shop);

        Child c1 = new Child(house);
        Child c2 = new Child(house);

        house.addMember(a1);
        house.addMember(a2);
        house.addMember(c1);
        house.addMember(c2);

        sgi.preprocess(DeviceSimulationInstance.RunMode.parallel, Duration.standardMinutes(5), Duration.standardSeconds(1));

        //sgi.asi.event_resolver.debug = true;

        MeasurableItemCollector evc1 = new MeasurableItemCollector(house.building_enviroment.getVariable(PowerConsumptionVariable.class));
        MeasurableItemCollector evc2 = new MeasurableItemCollector(house.building_enviroment.getVariable(PowerProductionVariable.class));
        MeasurableItemCollector evc3 = new MeasurableItemCollector(house.building_enviroment.getVariable(PowerVariable.class));

        MeasurableItemCollector evc4 = new MeasurableItemCollector(house.building_enviroment.getVariable(TemperatureVariable.class));
        MeasurableItemCollector evc5 = new MeasurableItemCollector(outdoors.getVariable(TemperatureVariable.class));


        System.out.println("Agentcount : " + AbstractAgent.getAgentCount());
        System.out.println("Devicecount : " + AbstractSimulatableDevice.getDeviceCount());


        sgi.advanceDuration(Duration.standardDays(7));

        evc1.toCSV();
        evc1.visualize();
        evc2.visualize();
        evc3.visualize();
        evc4.visualize();
        evc5.visualize();

        System.out.println(house);

        sgi.shutdown();

    }

}
