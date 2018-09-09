/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.city.house.behaviors;

import upatras.agentsimulation.behaviour.AbstractBehaviour;
import upatras.agentsimulation.event.Event;
import upatras.city.house.SmartHouse;
import upatras.city.house.events.CheckRefrigeratorEvent;
import upatras.utilitylibrary.library.randomnumbergenerators.RandomGenerator;

import java.util.Random;

/**
 * @author Paris
 */
public class GetItemFromRefrigeratorBehaviour extends AbstractBehaviour {

    SmartHouse house;

    static Random rand = RandomGenerator.getRandom();

    public GetItemFromRefrigeratorBehaviour(SmartHouse house) {
        this.house = house;
    }

    @Override
    public void execute(Event event) {
        if (((String) event.origin.getState("location")).equals("home")) {
            //house.refrigerator.submitCommand(new Command("Get item",null));
        } else {
            event.origin.submitEvent(new CheckRefrigeratorEvent(event.origin, event.event_time.plusMinutes(rand.nextInt(60))));
        }
    }


}
