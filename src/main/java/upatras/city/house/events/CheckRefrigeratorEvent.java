/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.city.house.events;

import org.joda.time.DateTime;
import upatras.agentsimulation.agent.AbstractAgent;
import upatras.agentsimulation.event.SelfTimeEvent;

/**
 *
 * @author Paris
 */
public class CheckRefrigeratorEvent extends SelfTimeEvent {

	public CheckRefrigeratorEvent(AbstractAgent origin, DateTime event_time) {
		super(origin, event_time);
	}

	public CheckRefrigeratorEvent(String name, AbstractAgent origin, DateTime event_time) {
		super(name, origin, event_time);
	}
	
	
	
}
