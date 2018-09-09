/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.city.house.behaviors;

import upatras.agentsimulation.agent.AbstractAgent;
import upatras.agentsimulation.behaviour.AbstractBehaviour;
import upatras.agentsimulation.event.Event;
import upatras.city.house.SmartHouse;
import upatras.city.house.events.FoodReadyEvent;
import upatras.simulationmodels.commands.TemperatureCommands;
import upatras.utilitylibrary.library.measurements.types.Temperature;
import upatras.utilitylibrary.library.randomnumbergenerators.RandomGenerator;

import java.util.Random;

/**
 *
 * @author Paris
 */
public class CookFoodBehaviour extends AbstractBehaviour {

	SmartHouse house;

	static Random rand = RandomGenerator.getRandom();

	public CookFoodBehaviour(SmartHouse house) {
		this.house = house;
	}

	@Override
	public void execute(Event event) {
		AbstractAgent cook = event.origin;

		if (!(boolean) house.getState("oven in use")) {
			house.oven.submitCommand(TemperatureCommands.targetTemperature(event.event_time, new Temperature(220)));
			int cooking_minutes = 30 + rand.nextInt(30);
			cook.submitEvent(new FoodReadyEvent(cook, event.event_time.plusMinutes(cooking_minutes), house));
			house.setState("oven in use", true);
		}
	}

}
