/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.city.shop;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import upatras.agentsimulation.main.AgentSimulationInstance;
import upatras.city.WorkBuilding;
import upatras.city.people.Adult;
import upatras.city.people.StartWorkingEvent;
import upatras.city.people.StopWorkingEvent;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.BasicEnviroment;
import upatras.simulationmodels.commands.OnOffCommands;
import upatras.simulationmodels.devices.basic.SimpleLoad;
import upatras.simulationmodels.devices.basic.SimpleRampLoad;
import upatras.utilitylibrary.library.measurements.types.Temperature;
import upatras.utilitylibrary.library.measurements.types.Volume;
import upatras.utilitylibrary.library.measurements.units.composite_units.HeatAbsorbingMaterial;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;
import upatras.utilitylibrary.library.randomnumbergenerators.RandomGenerator;

import java.util.Random;

/**
 * @author Paris
 */
public class SpikePowerShop extends WorkBuilding {

    SimpleLoad baseload;
    SimpleRampLoad spikeload;

    private static int shopid = 0;

    static Random rand = RandomGenerator.getRandom();

    public SpikePowerShop(double latitude, double longitude, double length, double width, AgentSimulationInstance asi, BasicEnviroment parent_enviroment) {
        super(latitude, longitude, length, width, (int) (length * width * 3), asi, new BasicEnviroment("Shop#" + shopid, parent_enviroment, Volume.cubicMeters(width * length), Temperature.degreecelsius(27), HeatAbsorbingMaterial.air().getSpecificHeatM(width * length) + HeatAbsorbingMaterial.brick().getSpecificHeatM(Math.cbrt(width * length)),null,null));
        shopid++;

        baseload = new SimpleLoad(building_enviroment, area * 100, area * 10);
        baseload.submitCommand(OnOffCommands.TurnOn(new DateTime(2018, 1, 1, 0, 0, 0)));

        spikeload = new SimpleRampLoad(false, new ComplexPower(area * 25, area * 1), Duration.standardMinutes(1), parent_enviroment);

        submitEvent(new StartWorkingEvent(this, workers, new DateTime(2018, 1, 1, 9, 0, 0)));
        submitEvent(new StopWorkingEvent(this, workers, new DateTime(2018, 1, 1, 14, 0, 0)));

        submitEvent(new StartWorkingEvent(this, workers, new DateTime(2018, 1, 1, 17, 0, 0)));
        submitEvent(new StopWorkingEvent(this, workers, new DateTime(2018, 1, 1, 21, 0, 0)));

        attachBehaviour("StartSpikeLoad", new SpikeLoadControl(this));
        attachBehaviour("StopSpikeLoad", new SpikeLoadControl(this));

        submitEvent(SpikeLoadPatternGenerator.generate(this, new DateTime(2018, 1, 1, 9, rand.nextInt(30), 0)));
    }

    @Override
    public boolean getJob(Adult worker) {
        if (workers.size() < max_worker_count) {
            worker.job = this;
            workers.add(worker);
            return true;
        }
        return false;

    }

    public boolean jobAvailable() {
        return workers.size() < max_worker_count;

    }

    @Override
    public synchronized void startworking(DateTime timeinstant) {

    }

    @Override
    public synchronized void stopworking(DateTime timeinstant) {

    }

}
