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
import upatras.city.house.events.CookFoodEvent;
import upatras.utilitylibrary.library.randomnumbergenerators.RandomGenerator;

import java.util.Random;

/**
 *
 * @author Paris
 */
public class ArrivedAtHomeBehaviour extends AbstractBehaviour {

	final SmartHouse house;
	
	final static Random rand=RandomGenerator.getRandom();

	public ArrivedAtHomeBehaviour(SmartHouse house) {
		this.house = house;
	}

	@Override
	public void execute(Event event) {
		AbstractAgent agent = event.origin;
		if (((int) house.getState("food amount")) == 0) {
			agent.submitEvent(new CookFoodEvent(agent, event.event_time.plusMinutes(rand.nextInt(60))));
		} else {
			System.out.println(this + " should watch tv");
		}
	}

}
