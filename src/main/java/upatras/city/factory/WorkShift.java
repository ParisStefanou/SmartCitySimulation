/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.city.factory;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import upatras.agentsimulation.agent.AbstractAgent;
import upatras.agentsimulation.agent.Population;
import upatras.agentsimulation.event.RepeatingPatternTimeEvent;

/**
 *
 * @author Paris
 */
public class WorkShift extends RepeatingPatternTimeEvent {

	static Duration[] pattern = {Duration.standardHours(24), Duration.standardHours(24), Duration.standardHours(24), Duration.standardHours(24), Duration.standardHours(24)};

	public WorkShift(AbstractAgent origin, Population target, DateTime event_time) {
		super(origin, target, event_time, pattern, Duration.standardDays(7));
	}

	public WorkShift(String name, AbstractAgent origin, Population target, DateTime event_time) {
		super(name, origin, target, event_time, pattern, Duration.standardDays(7));
	}

}
