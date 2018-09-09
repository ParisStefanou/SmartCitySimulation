/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.city.shop;

import org.joda.time.DateTime;
import upatras.agentsimulation.main.AgentSimulationInstance;
import upatras.city.WorkBuilding;
import upatras.city.people.Adult;
import upatras.city.people.StartWorkingEvent;
import upatras.city.people.StopWorkingEvent;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.BasicEnviroment;
import upatras.simulationmodels.commands.OnOffCommands;
import upatras.simulationmodels.devices.basic.SimpleLoad;
import upatras.utilitylibrary.library.measurements.types.Temperature;
import upatras.utilitylibrary.library.measurements.types.Volume;
import upatras.utilitylibrary.library.measurements.units.composite_units.HeatAbsorbingMaterial;

/**
 *
 * @author Paris
 */
public class FlatPowerShop extends WorkBuilding {

	SimpleLoad[] baseloads;

	private static int shopid = 0;

	public FlatPowerShop(double latitude, double longitude, double length, double width, AgentSimulationInstance asi, BasicEnviroment parent_enviroment) {
		super(latitude, longitude, length, width, (int) (length * width / 50), asi, new BasicEnviroment("Shop#" + shopid, parent_enviroment, Volume.cubicMeters(width * length), Temperature.degreecelsius(27), HeatAbsorbingMaterial.air().getSpecificHeatM(width * length) + HeatAbsorbingMaterial.brick().getSpecificHeatM(Math.cbrt(width * length)),null,null));
		shopid++;

		baseloads = new SimpleLoad[max_worker_count];
		for (int i = 0; i < max_worker_count; i++) {
			baseloads[i] = new SimpleLoad(building_enviroment, 500, 0);
		}

		submitEvent(new StartWorkingEvent(this, workers, new DateTime(2018, 1, 1, 9, 0, 0)));
		submitEvent(new StopWorkingEvent(this, workers, new DateTime(2018, 1, 1, 17, 0, 0)));
	}

	int load_loc = 0;

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
		baseloads[load_loc].submitCommand(OnOffCommands.TurnOn(timeinstant));
		load_loc++;
	}

	@Override
	public synchronized void stopworking(DateTime timeinstant) {
		baseloads[load_loc - 1].submitCommand(OnOffCommands.TurnOff(timeinstant));
		load_loc--;
	}

}
