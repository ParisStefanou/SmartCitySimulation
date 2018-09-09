/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.smartdevices.gridcontrol;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import upatras.automaticparallelism.main.TimeStep;
import upatras.city.house.SmartHouse;
import upatras.electricaldevicesimulation.simulationcore.DeviceBase.AbstractSimulatableDevice;
import upatras.electricaldevicesimulation.simulationcore.DeviceSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.commands.Command;
import upatras.simulationmodels.devices.AuxilaryPowerDevice;
import upatras.simulationmodels.devices.AuxiliaryPowerPlantInterface;
import upatras.simulationmodels.devices.highaccuracy.SmartRefrigerator;
import upatras.utilitylibrary.library.measurements.types.Power;
import upatras.utilitylibrary.library.randomnumbergenerators.RandomGenerator;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Paris
 */
public class VirtualPowerPlant extends AbstractSimulatableDevice implements AuxiliaryPowerPlantInterface {

    final ArrayList<AuxilaryPowerDevice> available_devices = new ArrayList<>();

    public VirtualPowerPlant(DeviceSimulationInstance instance) {
        super(instance);
    }

    public VirtualPowerPlant(DeviceSimulationInstance instance, ArrayList<SmartHouse> houses) {
        super(instance);
        addHouse(houses);
    }

    public void addHouse(SmartHouse house) {
        addRefrigerator(house.refrigerator);
    }

    public void addHouse(ArrayList<SmartHouse> houses) {
        System.out.println("Added " + houses.size() + " refrigerators");
        for (SmartHouse sh : houses) {
            addHouse(sh);
        }
    }

    public void addRefrigerator(SmartRefrigerator refrigerator) {
        available_devices.add(refrigerator);
        refrigerator.dependsOn(this);
    }

    Random rand = RandomGenerator.getRandom();

    @Override
    protected void processCommand(Command c) {

        Duration duration = (Duration) c.args[0];
        Power amount = (Power) c.args[1];

        Power amount_per_device = amount.dividedBy(available_devices.size());
        if (amount_per_device.value != 0) {
            switch (c.name) {
                case "Produce power":
                    for (AuxilaryPowerDevice device : available_devices) {
                        device.setPowerGeneration(c.time, duration, amount_per_device);
                        device.equilizeStorage(c.time.plus(duration).plusMillis(1), duration.multipliedBy(3));
                    }
                    break;

                case "Consume power":
                    for (AuxilaryPowerDevice device : available_devices) {
                        device.setPowerConsumption(c.time, duration, amount_per_device);
                        device.equilizeStorage(c.time.plus(duration).plusMillis(1), duration.multipliedBy(3));
                    }
                    break;
            }
        }
    }

    @Override
    public void simulate(TimeStep simulation_step) {

    }

    AuxilaryPowerDevice getAverageDevice() {
        if (available_devices.size() > 0) {
            return available_devices.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void producePower(DateTime starttime, Duration duration, Power watts, double cosf) {
        submitCommand(new Command("Produce power", starttime, duration, watts));
    }

    @Override
    public void consumePower(DateTime starttime, Duration duration, Power watts, double cosf) {
        submitCommand(new Command("Consume power", starttime, duration, watts));
    }

    @Override
    public Power getMaxAvailablePowerProduction() {
        return getAverageDevice().getMaxPowerGeneration().multipliedBy(available_devices.size()*0.99);
    }

    @Override
    public Power getMaxAvailablePowerConsumption() {
        return getAverageDevice().getMaxPowerConsumption().multipliedBy(available_devices.size()*0.99);
    }

    @Override
    public Power getSteadyPowerProduction(Duration duration) {
        return getAverageDevice().getAvailablePowerGeneration(duration).multipliedBy(available_devices.size()*0.99);
    }

    @Override
    public Power getSteadyPowerConsumption(Duration duration) {
        return getAverageDevice().getAvailablePowerConsumption(duration).multipliedBy(available_devices.size()*0.99);
    }

    @Override
    public Duration getMaxAvailableProductionDuration(Power watts) {

        return getAverageDevice().getAvailableDurationGeneration(watts.dividedBy(available_devices.size()*0.99));
    }

    @Override
    public Duration getMaxAvailableConsumptionDuration(Power watts) {
        return getAverageDevice().getAvailableDurationConsumption(watts.dividedBy(available_devices.size()*0.99));
    }

}
