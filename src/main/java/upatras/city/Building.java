/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.city;

import upatras.agentsimulation.agent.AbstractAgent;
import upatras.agentsimulation.main.AgentSimulationInstance;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.BasicEnviroment;

/**
 *
 * @author Paris
 */
public class Building extends AbstractAgent {

    public final double latitude;
    public final double longitude;
    public final double length;
    public final double width;
    public final double area;

    public final BasicEnviroment building_enviroment;

    public Building(double latitude, double longitude, double length, double width, AgentSimulationInstance asi, BasicEnviroment enviroment) {
        super(asi);
        this.latitude = latitude;
        this.longitude = longitude;
        this.length = length;
        this.width = width;
        this.area = width * length;
        this.building_enviroment = enviroment;
		
    }

}
