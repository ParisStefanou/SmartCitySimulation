/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.city.house.behaviors;

import upatras.agentsimulation.behaviour.AbstractBehaviour;
import upatras.agentsimulation.event.Event;
import upatras.city.house.SmartHouse;
import upatras.city.house.events.HungryEvent;

/**
 *
 * @author Paris
 */
public class EatFoodBehaviour extends AbstractBehaviour {

	SmartHouse house;

	public EatFoodBehaviour(SmartHouse house) {
		this.house = house;
	}

	@Override
	public void execute(Event event) {
		int foodleft = (int) house.getState("food amount");
		if (((String) event.origin.getState("location")).equals("home") && foodleft > 0) {
			house.setState("food amount", foodleft - 1);
		} else {
			event.origin.submitEvent(new HungryEvent(event.origin, event.event_time.plusHours(1)));
		}
	}

}
