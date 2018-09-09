/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.city;

import upatras.agentsimulation.agent.Population;
import upatras.city.factory.Factory;
import upatras.city.house.SmartHouse;
import upatras.city.people.HumanAgent;
import upatras.city.shop.SpikePowerShop;
import upatras.combinedsimulation.SmartGridInstance;
import upatras.simulationmodels.enviroments.OutdoorEnviroment;
import upatras.utilitylibrary.library.measurements.types.Temperature;

import java.util.HashMap;

/**
 * @author Paris
 */
public class City extends OutdoorEnviroment {

    final HashMap<String, CityArea> city_areas = new HashMap<>();

    public City(SmartGridInstance sgi, int area) {
        super("City Enviroment", sgi.dsi, new Temperature(27));

        double single_area = area / 3;
        double single_area_side = Math.sqrt(single_area);

        addArea(new CityArea("Housing", sgi.asi, this, 0, 0, single_area_side, single_area_side, 0, 0.3, 0.7));

        addArea(new CityArea("Commercial", sgi.asi, this, single_area_side, 0, single_area_side, single_area_side, 0, 0.4, 0.6));

        addArea(new CityArea("Industrial", sgi.asi, this, -single_area_side, 0, single_area_side, single_area_side, 0.3, 0.2, 0.5));
    }

    final public void addArea(CityArea ca) {
        city_areas.put(ca.name, ca);
    }

    final public CityArea getArea(String area_name) {
        return city_areas.get(area_name);
    }

    public Job lookforajob() {
        for (CityArea ca : city_areas.values()) {
            for (SpikePowerShop s : ca.shops) {
                if (s.jobAvailable()) {
                    return s;
                }
            }
        }
        for (CityArea ca : city_areas.values()) {
            for (Factory f : ca.factories) {
                if (f.jobAvailable()) {
                    return f;
                }
            }
        }
        return null;
    }

    public SmartHouse lookforahouse() {
        for (CityArea ca : city_areas.values()) {
            for (SmartHouse h : ca.houses) {
                if (h.members.isEmpty()) {
                    return h;
                }
            }
        }
        return null;
    }

    public SmartHouse lookforahouse(Population<HumanAgent> family) {
        for (CityArea ca : city_areas.values()) {
            for (SmartHouse h : ca.houses) {
                if (h.members.isEmpty() && h.max_size < family.size()) {
                    return h;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {

        return "City " + name;
    }

    public String info() {

        String s = "";
        for (CityArea ca : city_areas.values()) {
            s += ca.info() + "\n";
            s += "Job slots " + ca.jobs_total() + " occupied " + ca.jobs_filled() + "\n";
            s += "Housing slots  " + ca.housing_total() + " occupied " + ca.housing_filled() + "\n";
            s += "--------------------" + "\n";
        }

        return s;
    }

}
