/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.city.shop;

import upatras.agentsimulation.behaviour.AbstractSelfBehaviour;
import upatras.agentsimulation.event.Event;
import upatras.simulationmodels.commands.OnOffCommands;
import upatras.utilitylibrary.library.randomnumbergenerators.RandomGenerator;

import java.util.Random;

/**
 *
 * @author Paris
 */
public class SpikeLoadControl extends AbstractSelfBehaviour {

	SpikePowerShop shop;

	static Random rand = RandomGenerator.getRandom();

	public SpikeLoadControl(SpikePowerShop shop) {
		super(shop);
		this.shop = shop;
	}

	@Override
	public void execute(Event event) {

		if (event.name.equals("StartSpikeLoad")) {
			shop.spikeload.submitCommand(OnOffCommands.TurnOn(event.event_time));
			shop.submitEvent(new StopSpikeLoad(shop, event.event_time.plusMinutes(10 + rand.nextInt(10))));

		} else if (event.name.equals("StopSpikeLoad")) {
			shop.spikeload.submitCommand(OnOffCommands.TurnOff(event.event_time));
		}

	}

}
