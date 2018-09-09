/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.city;

import upatras.agentsimulation.main.AgentSimulationInstance;
import upatras.city.factory.Factory;
import upatras.city.house.SmartHouse;
import upatras.city.shop.SpikePowerShop;
import upatras.simulationmodels.enviroments.OutdoorEnviroment;
import upatras.utilitylibrary.library.measurements.types.Temperature;
import upatras.utilitylibrary.library.randomnumbergenerators.RandomGenerator;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Paris
 */
public class CityArea extends OutdoorEnviroment {

	final double latitude;
	final double longitude;
	final double width;
	final double height;
	final double area;

	final AgentSimulationInstance asi;

	final public ArrayList<Factory> factories = new ArrayList<>();
	final public ArrayList<SmartHouse> houses = new ArrayList<>();
	final public ArrayList<SpikePowerShop> shops = new ArrayList<>();

	public CityArea(String area_name, AgentSimulationInstance asi, City city, double latitude, double longitude, double width, double height,
			double factory_ratio, double shop_ratio, double house_ratio) {
		super(area_name, city, new Temperature(27));
		this.asi = asi;
		this.latitude = latitude;
		this.longitude = longitude;
		this.width = width;
		this.height = height;
		this.area = width * height;

		generateStructures(shop_ratio, house_ratio, factory_ratio);
	}

	public void generateStructures(double commercial_ratio, double housing_ratio, double factory_ratio) {

		double commercial_area = area * commercial_ratio;
		double housing_area = area * housing_ratio;
		double factory_area = area * factory_ratio;

		Random rand = RandomGenerator.getRandom();
		int tries = 1;
		while (commercial_area > 100 && tries < 4) {
			int area = rand.nextInt(2000 / tries);
			double side = Math.sqrt(area);
			if (area < commercial_area) {
				shops.add(new SpikePowerShop(0, 0, side, side, asi, this));
				commercial_area -= area;
				tries = 1;
			}
			tries ++;
		}
		tries = 1;
		while (housing_area > 50 && tries < 4) {
			int area = rand.nextInt(150 / tries );
			double side = Math.sqrt(area);
			if (area < housing_area) {
				houses.add(new SmartHouse(0, 0, side, side, asi, this));
				housing_area -= area;
				tries = 0;
			}
			tries ++;
		}
		tries = 1;
		while (factory_area > 200 && tries < 4) {
			int area = rand.nextInt(10000 / tries);
			double side = Math.sqrt(area);
			if (area < factory_area) {
				factories.add(new Factory(0, 0, side, side, asi, this));
				factory_area -= area;
				tries = 1;
			}
			tries ++;
		}

	}

	public String info() {
		return "Area " + this.getClass().getSimpleName() + " " + name + " contains :\n"
				+ factories.size() + " factories\n"
				+ shops.size() + " shops\n"
				+ houses.size() + " houses\n";
	}

	public String toString() {
		return "CityArea " + name;
	}

	public int jobs_filled() {
		int sum = 0;
		for (SpikePowerShop s : shops) {
			sum += s.workercount();
		}
		for (Factory f : factories) {
			sum += f.workercount();
		}
		return sum;

	}

	public int jobs_vacant() {

		return jobs_total() - jobs_filled();
	}

	public int jobs_total() {
		int sum = 0;
		for (SpikePowerShop s : shops) {
			sum += s.maxworkercount();
		}
		for (Factory f : factories) {
			sum += f.maxworkercount();
		}
		return sum;
	}

	public int housing_filled() {
		int sum = 0;
		for (SmartHouse h : houses) {
			if (!h.members.isEmpty()) {
				sum += 1;
			}
		}
		return sum;

	}

	public int housing_vacant() {

		return jobs_total() - jobs_filled();
	}

	public int housing_total() {
		return houses.size();
	}

}
