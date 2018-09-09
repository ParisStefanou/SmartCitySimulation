/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.city.people;

import upatras.city.house.SmartHouse;
import upatras.city.house.behaviors.EatFoodBehaviour;
import upatras.city.house.events.FoodReadyEvent;

/**
 *
 * @author Paris
 */
public class Child extends HumanAgent {

	public Child(SmartHouse house) {
		super(house);

		attachBehaviour(FoodReadyEvent.class, new EatFoodBehaviour(house));

	}

}
