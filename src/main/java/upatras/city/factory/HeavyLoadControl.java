/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.city.factory;

import upatras.agentsimulation.behaviour.AbstractSelfBehaviour;
import upatras.agentsimulation.event.Event;
import upatras.simulationmodels.commands.OnOffCommands;

/**
 *
 * @author Paris
 */
public class HeavyLoadControl extends AbstractSelfBehaviour {

	Factory factory;

	public HeavyLoadControl(Factory factory) {
		super(factory);
		this.factory = factory;
	}

	@Override
	public void execute(Event event) {

		if (event.name.equals("StartHeavyLoad")) {
			factory.spikeload.submitCommand(OnOffCommands.TurnOn(event.event_time));
			factory.submitEvent(new StopHeavyLoad(factory, event.event_time.plusHours(1)));

		} else if (event.name.equals("StopHeavyLoad")) {
			factory.spikeload.submitCommand(OnOffCommands.TurnOff(event.event_time));
		}

	}

}
