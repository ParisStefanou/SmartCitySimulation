/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.devices;

import upatras.electricaldevicesimulation.simulationcore.enviromentbase.PowerEnviroment;
import upatras.simulationmodels.devices.basic.SimpleLoad;

/**
 *
 * @author Paris
 */
public class ProductionMachine extends SimpleLoad{
    
    public ProductionMachine(PowerEnviroment enviroment, double active_power, double reactive_power) {
        super(enviroment, active_power, reactive_power);
    }
    
}
