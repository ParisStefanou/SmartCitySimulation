/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.city.factory;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import upatras.agentsimulation.agent.Population;
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
 *
 * @author Paris
 */
public class Factory extends WorkBuilding {

	SimpleLoad baseload;
	SimpleRampLoad spikeload;

	public final Population<Adult> workers8to16 = new Population<>();
	public final Population<Adult> workers16to24 = new Population<>();
	public final Population<Adult> workers0to8 = new Population<>();

	private static int factoryid = 0;

	Random rand = RandomGenerator.getRandom();

	public Factory(double latitude, double longitude, double length, double width, AgentSimulationInstance asi, BasicEnviroment parent_enviroment) {
		super(latitude, longitude, length, width, 3 * (int) (width * length)*5, asi,new BasicEnviroment("Factory#" + factoryid, parent_enviroment, Volume.cubicMeters(width * length), Temperature.degreecelsius(27),
                        HeatAbsorbingMaterial.air().getSpecificHeatM(width * length) + HeatAbsorbingMaterial.brick().getSpecificHeatM(Math.cbrt(width * length)),null,null));

		factoryid++;
		baseload = new SimpleLoad(building_enviroment, area * 80, area * 20);
		baseload.submitCommand(OnOffCommands.TurnOn(new DateTime(2018, 1, 1, 0, 0, 0)));
		spikeload = new SimpleRampLoad(false, new ComplexPower(area * 120, area * 10), Duration.standardMinutes(15), parent_enviroment);

		submitEvent(new StartWorkingEvent(this, workers8to16, new DateTime(2018, 1, 1, 8, 0, 0)));
		submitEvent(new StopWorkingEvent(this, workers8to16, new DateTime(2018, 1, 1, 8, 0, 0).plusHours(8)));

		submitEvent(new StartWorkingEvent(this, workers16to24, new DateTime(2018, 1, 1, 16, 0, 0)));
		submitEvent(new StopWorkingEvent(this, workers16to24, new DateTime(2018, 1, 1, 16, 0, 0).plusHours(8)));

		submitEvent(new StartWorkingEvent(this, workers0to8, new DateTime(2018, 1, 2, 0, 0, 0)));
		submitEvent(new StopWorkingEvent(this, workers0to8, new DateTime(2018, 1, 2, 0, 0, 0).plusHours(8)));

		attachBehaviour("StartHeavyLoad", new HeavyLoadControl(this));
		attachBehaviour("StopHeavyLoad", new HeavyLoadControl(this));

		submitEvent(HeavyLoadPatternGenerator.generate(this, new DateTime(2018, 1, 1, rand.nextInt(24), 0, 0)));

	}

	int popcounter = 0;

	@Override
	public boolean getJob(Adult worker) {
		if (workers.size() < max_worker_count) {
			worker.job = this;
			workers.add(worker);
			switch (popcounter) {
				case 0:
				case 1:
					workers8to16.add(worker);
					break;
				case 3:
				case 4:
					workers16to24.add(worker);
					break;
				case 5:
					workers0to8.add(worker);
					break;
			}
			popcounter++;
			if (popcounter == 6) {
				popcounter = 0;
			}

			return true;
		}
		return false;

	}

	@Override
	public synchronized void startworking(DateTime timeinstant) {
	}

	@Override
	public synchronized void stopworking(DateTime timeinstant) {
	}

	public boolean jobAvailable() {
		return workers.size() < max_worker_count;
	}

}
