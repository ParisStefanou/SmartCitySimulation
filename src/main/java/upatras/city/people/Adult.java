/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.city.people;

import upatras.city.Job;
import upatras.city.house.SmartHouse;
import upatras.city.house.behaviors.ArrivedAtHomeBehaviour;
import upatras.city.house.behaviors.CookFoodBehaviour;
import upatras.city.house.behaviors.EatFoodBehaviour;
import upatras.city.house.behaviors.TravelBehaviour;
import upatras.city.house.events.ArrivedAtHomeEvent;
import upatras.city.house.events.CookFoodEvent;
import upatras.city.house.events.FoodReadyEvent;

/**
 *
 * @author Paris
 */
public class Adult extends HumanAgent {

	public Job job = null;

	public Adult(SmartHouse house, Job job) {
		super(house);
		if (job != null) {
			job.getJob(this);
			this.job = job;

			attachBehaviour(StartWorkingEvent.class, new WorkBehaviour(this));
			attachBehaviour(StopWorkingEvent.class, new WorkBehaviour(this));

			attachBehaviour(StopWorkingEvent.class, new TravelBehaviour(this));
			attachBehaviour(ArrivedAtHomeEvent.class, new ArrivedAtHomeBehaviour(house));
		}

		attachBehaviour(CookFoodEvent.class, new CookFoodBehaviour(house));
		attachBehaviour(FoodReadyEvent.class, new EatFoodBehaviour(house));
	}

}
