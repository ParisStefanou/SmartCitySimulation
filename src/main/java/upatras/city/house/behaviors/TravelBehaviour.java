/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.city.house.behaviors;

import upatras.agentsimulation.behaviour.AbstractBehaviour;
import upatras.agentsimulation.event.Event;
import upatras.city.house.events.ArrivedAtHomeEvent;
import upatras.city.people.HumanAgent;

/**
 *
 * @author Paris
 */
public class TravelBehaviour extends AbstractBehaviour {

	double distance = 100;
	double speed = 10;

	final HumanAgent human;

	public TravelBehaviour(HumanAgent human) {
		this.human = human;
	}

	@Override
	public void execute(Event event) {
		human.submitEvent(new ArrivedAtHomeEvent(human, event.event_time.plusMinutes((int) (distance / speed))));

	}

}
