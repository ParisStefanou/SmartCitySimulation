/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.city;

import upatras.agentsimulation.agent.Population;
import upatras.agentsimulation.main.AgentSimulationInstance;
import upatras.city.people.Adult;
import upatras.electricaldevicesimulation.simulationcore.enviromentbase.BasicEnviroment;

/**
 *
 * @author Paris
 */
public abstract class WorkBuilding extends Building implements Job {

    public final Population<Adult> workers;

    public int max_worker_count;

    public WorkBuilding(double latitude, double longitude, double length, double width,int max_worker_count, AgentSimulationInstance asi, BasicEnviroment enviroment) {
        super(latitude, longitude, length, width, asi, enviroment);
		this.max_worker_count=max_worker_count;
        workers=new Population<>(max_worker_count);
    }

	@Override
    public boolean getJob(Adult worker) {
        if (workers.size() < max_worker_count) {
            worker.job = this;
            workers.add(worker);
            return true;
        }
        return false;
        
    }

    
    @Override
    public int workercount() {
        return workers.size();
    }
    
    @Override
    public int maxworkercount() {
        return max_worker_count;
    }

}
