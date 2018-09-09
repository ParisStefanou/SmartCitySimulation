/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.city.shop;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import upatras.agentsimulation.event.RepeatingPatternTimeEvent;
import upatras.utilitylibrary.library.randomnumbergenerators.RandomGenerator;

import java.util.Random;

/**
 *
 * @author Paris
 */
public class SpikeLoadPatternGenerator {

	public static RepeatingPatternTimeEvent generate(SpikePowerShop shop, DateTime start_time) {
		Duration[] pattern = new Duration[10];

		int pattern_loc = 0;

		Random rand = RandomGenerator.getRandom();

		int firststartminute = 0;
		int firstmaxminute = 5 * 60;
		int secondstartminute = 8 * 60;
		int secondendminute = 12 * 60;

		int currminute = firststartminute;

		for (int i = 0; i < pattern.length; i++) {

			currminute += 6 + rand.nextInt(100);
			if (currminute < firstmaxminute) {
				pattern[pattern_loc++] = Duration.standardMinutes(currminute);
			} else {
				if (currminute >= firstmaxminute && currminute < secondstartminute) {
					currminute = secondstartminute;
				}
				if (currminute < secondendminute) {
					pattern[pattern_loc++] = Duration.standardMinutes(currminute);
				} else {
					break;
				}

			}
		}

		return new RepeatingPatternTimeEvent(
				"StartSpikeLoad", shop, shop.toPopulation(), start_time, pattern, Duration.standardDays(1));
	}
}
