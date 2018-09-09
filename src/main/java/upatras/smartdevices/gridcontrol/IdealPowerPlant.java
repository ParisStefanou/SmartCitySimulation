/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.smartdevices.gridcontrol;

import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerConsumptionVariable;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerProductionVariable;
import upatras.simulationmodels.devices.basic.VariableLoad;
import upatras.utilitylibrary.library.dataextraction.MeasurableItemExtractor;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

/**
 *
 * @author Paris
 */
public class IdealPowerPlant extends VariableLoad {

	MeasurableItemExtractor get_last_consumption;
	MeasurableItemExtractor get_last_production;

	public IdealPowerPlant(PowerEnviroment to_control_enviroment) {
		super(to_control_enviroment, new ComplexPower(Double.MIN_VALUE, Double.MIN_VALUE));

		get_last_consumption = new MeasurableItemExtractor(to_control_enviroment.getVariable(PowerConsumptionVariable.class));
		get_last_production = new MeasurableItemExtractor(to_control_enviroment.getVariable(PowerProductionVariable.class));

	}

	@Override
	public void simulate(TimeStep simulation_step) {

		if (get_last_consumption.measurement != null && get_last_production.measurement != null) {

			ComplexPower consumption = (ComplexPower)get_last_consumption.measurement.data;
			ComplexPower production = (ComplexPower)get_last_production.measurement.data;
			ComplexPower total = consumption.minus(production);

			target_power = target_power.minus(total);

			super.simulate(simulation_step);
		}
	}
}
