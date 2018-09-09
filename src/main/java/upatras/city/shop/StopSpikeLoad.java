/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.city.shop;

import org.joda.time.DateTime;
import upatras.agentsimulation.agent.AbstractAgent;
import upatras.agentsimulation.event.TimeEvent;

/**
 *
 * @author Paris
 */
public class StopSpikeLoad extends TimeEvent {

	public StopSpikeLoad( AbstractAgent origin, DateTime event_time) {
		super(origin, origin.toPopulation(), event_time);
		
	}

}
