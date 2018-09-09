/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package upatras.city;

import org.joda.time.DateTime;
import upatras.city.people.Adult;

/**
 *
 * @author Paris
 */
public interface Job {

    void startworking(DateTime timeinstant);

    void stopworking(DateTime timeinstant);

    boolean getJob(Adult worker);

    int workercount();

    int maxworkercount();
}
