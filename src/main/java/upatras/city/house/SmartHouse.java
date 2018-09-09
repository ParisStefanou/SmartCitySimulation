/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.city.house;

import upatras.agentsimulation.agent.Population;
import upatras.agentsimulation.main.AgentSimulationInstance;
import upatras.city.Building;
import upatras.city.people.HumanAgent;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.BasicEnviroment;
import upatras.simulationmodels.devices.Computer;
import upatras.simulationmodels.devices.TV;
import upatras.simulationmodels.devices.highaccuracy.OvenHA;
import upatras.simulationmodels.devices.highaccuracy.PhotovoltaicHA;
import upatras.simulationmodels.devices.highaccuracy.SmartRefrigerator;
import upatras.utilitylibrary.library.measurements.types.Power;
import upatras.utilitylibrary.library.measurements.types.Temperature;
import upatras.utilitylibrary.library.measurements.types.Volume;
import upatras.utilitylibrary.library.measurements.units.composite_units.HeatAbsorbingMaterial;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;
import upatras.utilitylibrary.library.randomnumbergenerators.RandomGenerator;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Paris
 */
public class SmartHouse extends Building {

	public final int max_size;
	public Population<HumanAgent> members;

	static int houseid = 0;

	public final PhotovoltaicHA pv;
	public final TV tv;
	public final SmartRefrigerator refrigerator;
	public final OvenHA oven;
	public final Computer computer;

	public static Random rand = RandomGenerator.getRandom();

	public SmartHouse(double latitude, double longitude, double length, double width, AgentSimulationInstance asi, BasicEnviroment parent_enviroment) {
		super(latitude, longitude, length, width, asi, new BasicEnviroment("House#" + houseid, parent_enviroment, Volume.cubicMeters(width * length), Temperature.degreecelsius(27), HeatAbsorbingMaterial.air().getSpecificHeatM(width * length) + HeatAbsorbingMaterial.brick().getSpecificHeatM(Math.cbrt(width * length)),1000.0,null));
		houseid++;
		this.max_size = (int) (area / 50);
		this.members = new Population<>();
		if (rand.nextInt(100) < 30) {
			pv = new PhotovoltaicHA(building_enviroment, new ComplexPower(5000, 0));
		} else {
			pv = null;
		}

		tv = new TV(building_enviroment);
		refrigerator = new SmartRefrigerator(building_enviroment, new Power(600), new Volume(6),
				new Temperature(6).minus((rand.nextInt(100))/100.0), new Temperature(6), new Temperature(8), new Temperature(4),0.1);
		oven = new OvenHA(building_enviroment, new Power(1000), new Volume(0.5));
		computer = new Computer(building_enviroment);

		setState("food amount", 0);
		setState("oven in use", false);
	}

	public boolean isFull() {
		return max_size <= members.size();
	}

	public void addMember(HumanAgent human) {
		if (members.size() < max_size) {
			members.add(human);
		} else {
			try {
				throw new Exception("House is fully occupied");
			} catch (Exception ex) {
				Logger.getLogger(SmartHouse.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

	}

	@Override
	public String toString() {
		StringBuilder toreturn = new StringBuilder();
		toreturn.append("House : " + name + "\n");
		toreturn.append("Member count : " + members.size() + "\n");
		for (HumanAgent a : members) {
			toreturn.append(a.getClass().getSimpleName() + " " + a.name + "\n");
		}

		return toreturn.toString();
	}
}
