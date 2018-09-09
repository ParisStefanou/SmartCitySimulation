/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.city.factory;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import upatras.agentsimulation.event.RepeatingPatternTimeEvent;
import upatras.utilitylibrary.library.randomnumbergenerators.RandomGenerator;

import java.util.Random;

/**
 *
 * @author Paris
 */
public class HeavyLoadPatternGenerator {

	public static RepeatingPatternTimeEvent generate(Factory factory, DateTime start_time) {
		Duration[] pattern = new Duration[50];

		int total_delay = 0;
		int patternloc = 0;
		Random rand = RandomGenerator.getRandom();
		for (int i = 0; i < pattern.length; i++) {
			int delay = rand.nextInt(2) + 3;
			total_delay += delay;
			if (total_delay + 4 < 24 * 5) {
				pattern[patternloc++] = Duration.standardHours(delay);
			} else {
				break;
			}
		}

		return new RepeatingPatternTimeEvent("StartHeavyLoad", factory, factory.toPopulation(), start_time, pattern, Duration.standardDays(7));
	}
}
