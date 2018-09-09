/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.city.people;

import upatras.agentsimulation.behaviour.AbstractSelfBehaviour;
import upatras.agentsimulation.event.Event;
import upatras.agentsimulation.event.TimeEvent;
import upatras.city.Job;

/**
 *
 * @author Paris
 */
public class WorkBehaviour extends AbstractSelfBehaviour<Adult, TimeEvent> {

	Job job;

	public WorkBehaviour(Adult self) {
		super(self);
		job = self.job;
	}

	@Override
	public void execute(TimeEvent event) {

		if (event.equals("StartWorkingEvent")) {
			job.startworking(event.event_time);
		} else if (event.equals("StopWorkingEvent"))  {
			job.stopworking(event.event_time);
		}

	}

}
