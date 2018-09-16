/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.smartdevices.gridcontrol;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import upatras.automaticparallelism.main.TimeStep;
import upatras.electricaldevicesimulation.simulationcore.DeviceBase.AbstractSimulatableDevice;
import upatras.electricaldevicesimulation.simulationcore.commands.Command;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerConsumptionVariable;
import upatras.electricaldevicesimulation.simulationcore.environmentalvariables.PowerProductionVariable;
import upatras.simulationmodels.devices.AuxiliaryPowerPlantInterface;
import upatras.simulationmodels.devices.PowerPlant;
import upatras.utilitylibrary.library.dataextraction.MeasurableItemExtractor;
import upatras.utilitylibrary.library.measurements.types.Power;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;
import upatras.utilitylibrary.library.measurements.units.electrical.ReactivePower;

import java.util.ArrayList;

/**
 * @author Paris
 */
public class PowerGenerationController extends AbstractSimulatableDevice {

    public ArrayList<PowerPlant> power_sources = new ArrayList<>();
    public ArrayList<AuxiliaryPowerPlantInterface> auxilary_power_sources = new ArrayList<>();

    MeasurableItemExtractor get_last_consumption;
    MeasurableItemExtractor get_last_production;


    boolean debug = false;

    public PowerGenerationController(PowerEnviroment enviroment_to_monitor) {
        super(enviroment_to_monitor.simulationinstance);

        get_last_consumption = new MeasurableItemExtractor(enviroment_to_monitor.getVariable(PowerConsumptionVariable.class));
        get_last_production = new MeasurableItemExtractor(enviroment_to_monitor.getVariable(PowerProductionVariable.class));

    }

    public void preprocess() {
        power_sources.sort((o1, o2) -> {

            if (o1.getRampRateMillisecond().real.value < o2.getRampRateMillisecond().real.value) {
                return 1;
            } else {
                return -1;
            }
        });

    }

    Power prevcons = null;
    Power additional_expected_production = new Power();

    @Override
    public void simulate(TimeStep simulation_step) {

        if (get_last_consumption.measurement != null && get_last_production.measurement != null) {


            Power consumption = ((ComplexPower) get_last_consumption.measurement.data).real;
            Power production = ((ComplexPower) get_last_production.measurement.data).real.plus(additional_expected_production);
            additional_expected_production = new Power();
            Power total = consumption.minus(production);
            Power originaltotal = total.clone();
            Duration duration_to_plan_for = simulation_step.duration;
            DateTime start = simulation_step.step_start;


            if (debug && prevcons != null)
                System.out.println("previous consumption : " + prevcons.value + " and current production : " + production.value + " diff : " + prevcons.minus(production).value);


            for (PowerPlant powerplant : power_sources) {
                total = utilize_power_source(powerplant, total, start, duration_to_plan_for);
            }
            if (auxilary_power_sources.size() > 0 && Math.abs(total.value) > 10000) {

                total=total.dividedBy(3);

                System.out.println("Will try to fix : " + total.value);

                for (AuxiliaryPowerPlantInterface a_powerplant : auxilary_power_sources) {
                    total = utilize_auxilary_power_source(a_powerplant, total, originaltotal, start, duration_to_plan_for);
                }

            }

            if (debug) prevcons = consumption.clone();

        }
    }

    private Power utilize_power_source(PowerPlant powerplant, Power total, DateTime start, Duration duration_to_plan_for) {
        ComplexPower ramp_speed = powerplant.getRampRateMillisecond();

        if (total.value > 1) {
            ComplexPower remaining_power = powerplant.getRemainingPower();

            double step_s = duration_to_plan_for.getMillis() / 1000.0;
            double power = total.value;
            double l = ramp_speed.real.value * 1000.0;

            double ans1 = step_s + Math.sqrt(step_s * step_s - 2 * power * step_s / l);
            double ans2 = step_s - Math.sqrt(step_s * step_s - 2 * power * step_s / l);

            double ramp_s;
            if (ans1 >= 0 && ans1 <= step_s && !Double.isNaN(ans1)) {
                ramp_s = ans1;
            } else if (ans2 >= 0 && ans2 <= step_s && !Double.isNaN(ans2)) {
                ramp_s = ans2;
            } else {
                ramp_s = Double.MAX_VALUE;
            }

            double max_ramp_s = remaining_power.real.value / l;

            if (ramp_s > max_ramp_s) {
                ramp_s = max_ramp_s;
            }
            if (ramp_s > step_s) {
                ramp_s = step_s;
            }

            double final_power = ramp_s * l;
            double average_power = l * ramp_s * ramp_s / 2 / step_s + l * ramp_s * (step_s - ramp_s) / step_s;

            additional_expected_production = additional_expected_production.plus(l * ramp_s * ramp_s / 2/ step_s);

            Power power_change = new Power(final_power);
            Power actual_producable_power = new Power(average_power);
            if (debug)
                System.out.println("will change by " + power_change.value + " in order to produce " + actual_producable_power.value);
            powerplant.increasePower(start, new ComplexPower(power_change, new ReactivePower()));
            total = total.minus(actual_producable_power);

        } else if (total.value < -1) {
            ComplexPower current_power = powerplant.getCurrentPower();

            double step_s = duration_to_plan_for.getMillis() / 1000.0;
            double power = -total.value;
            double l = ramp_speed.real.value * 1000.0;

            double ans1 = step_s + Math.sqrt(step_s * step_s - 2 * power * step_s / l);
            double ans2 = step_s - Math.sqrt(step_s * step_s - 2 * power * step_s / l);

            double ramp_s;
            if (ans1 >= 0 && ans1 <= step_s && !Double.isNaN(ans1)) {
                ramp_s = ans1;
            } else if (ans2 >= 0 && ans2 <= step_s && !Double.isNaN(ans2)) {
                ramp_s = ans2;
            } else {
                ramp_s = Double.MAX_VALUE;
            }

            double max_ramp_s = current_power.real.value / l;

            if (ramp_s > max_ramp_s) {
                ramp_s = max_ramp_s;
            }
            if (ramp_s > step_s) {
                ramp_s = step_s;
            }

            double final_power = ramp_s * l;
            double average_power = l * ramp_s * ramp_s / 2 / step_s + l * ramp_s * (step_s - ramp_s) / step_s;

            additional_expected_production = additional_expected_production.minus(l * ramp_s * ramp_s / 2/ step_s);

            Power power_change = new Power(final_power);
            Power actual_producable_power = new Power(average_power);
            if (debug)
                System.out.println("will change by " + -power_change.value + " in order to produce " + -actual_producable_power.value);
            powerplant.decreasePower(start, new ComplexPower(power_change, new ReactivePower()));
            total = total.plus(actual_producable_power);

        }

        return total;
    }

    private Power utilize_auxilary_power_source(AuxiliaryPowerPlantInterface powerplant, Power total, Power originaltotal, DateTime start, Duration duration_to_plan_for) {

        if (total.value > 1) {
            Power producable_power = powerplant.getSteadyPowerProduction(duration_to_plan_for);

            if (producable_power.value < total.value) {
                powerplant.producePower(start, duration_to_plan_for, producable_power, 1);
                if (debug)
                    System.out.println("Produced1 " + producable_power.value + " power for " + duration_to_plan_for.toString());
                total = total.minus(producable_power);

            } else if (producable_power.value > 0.1) {
                powerplant.producePower(start, duration_to_plan_for, total, 1);
                if (debug)
                    System.out.println("Produced2 " + total.value + " power for " + duration_to_plan_for.toString());
                total = new Power();
            } else {
                if (debug) System.out.println("Out of power");
            }
        } else if (total.value < -1) {

            Power consumable_power = powerplant.getSteadyPowerConsumption(duration_to_plan_for);
            if (consumable_power.value < -total.value) {
                powerplant.consumePower(start, duration_to_plan_for, consumable_power, 1);
                total = total.plus(consumable_power);
                if (debug)
                    System.out.println("Consumed1 " + consumable_power.value + " power for " + duration_to_plan_for.toString());
            } else if (consumable_power.value > 0.1) {
                powerplant.consumePower(start, duration_to_plan_for, total.multipliedBy(-1), 1);
                if (debug)
                    System.out.println("Consumed2 " + total.multipliedBy(-1).value + " power for " + duration_to_plan_for.toString());
                total = new Power();
            } else {
                if (debug) System.out.println("Full of power");
            }
        }
        return total;
    }

    public void addPowerPlant(PowerPlant power_plant) {
        power_sources.add(power_plant);
        power_plant.dependsOn(this);
    }

    public void addAuxilaryPowerPlant(AuxiliaryPowerPlantInterface auxilary_power_plant) {
        auxilary_power_sources.add(auxilary_power_plant);
        ((AbstractSimulatableDevice) auxilary_power_plant).dependsOn(this);
    }

    @Override
    protected void processCommand(Command c) {
    }
}
