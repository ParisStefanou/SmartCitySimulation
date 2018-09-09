/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.city.house.events;

import org.joda.time.DateTime;
import upatras.agentsimulation.agent.AbstractAgent;
import upatras.agentsimulation.event.TimeEvent;
import upatras.city.house.SmartHouse;
import upatras.simulationmodels.commands.TemperatureCommands;

/**
 *
 * @author Paris
 */
public class FoodReadyEvent extends TimeEvent {

	SmartHouse house;

	public FoodReadyEvent(AbstractAgent origin, DateTime event_time, SmartHouse house) {
		super(origin, house.members, event_time);
		this.house = house;
	}

	public FoodReadyEvent(String name, AbstractAgent origin, DateTime event_time, SmartHouse house) {
		super(name, origin, house.members, event_time);
		this.house = house;
	}

	@Override
	public void preprocessing() {
		house.setState("oven in use", false);
		house.oven.submitCommand(TemperatureCommands.TurnOff(event_time));
		house.setState("food amount", house.members.size());
	}

}
