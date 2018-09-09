/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.city.people;

import org.joda.time.DateTime;
import upatras.agentsimulation.agent.Population;
import upatras.city.WorkBuilding;
import upatras.city.factory.WorkShift;

/**
 *
 * @author Paris
 */
public class StopWorkingEvent extends WorkShift {

	public StopWorkingEvent(WorkBuilding work, Population<Adult> workers, DateTime event_time) {
		super(work, workers, event_time);
	}

}
