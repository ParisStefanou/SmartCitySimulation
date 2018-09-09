/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.city.people;

import upatras.agentsimulation.agent.AbstractAgent;
import upatras.city.house.SmartHouse;
import upatras.city.house.behaviors.EatFoodBehaviour;
import upatras.city.house.behaviors.GetItemFromRefrigeratorBehaviour;
import upatras.city.house.events.CheckRefrigeratorEvent;
import upatras.city.house.events.HungryEvent;
import upatras.utilitylibrary.library.randomnumbergenerators.RandomGenerator;

import java.util.Random;

/**
 *
 * @author Paris
 */
public abstract class HumanAgent extends AbstractAgent {

	public SmartHouse house;

	static Random rand = RandomGenerator.getRandom();

	public HumanAgent(SmartHouse house) {
		super(house.asi);
		this.house = house;
		house.members.add(this);

		attachBehaviour(HungryEvent.class, new EatFoodBehaviour(house));
		attachBehaviour(CheckRefrigeratorEvent.class, new GetItemFromRefrigeratorBehaviour(house));

		submitEvent(new CheckRefrigeratorEvent(this, this.asi.getPresent().plusMinutes(5 + rand.nextInt(600))));

		setState("location", "home");
	}

}
