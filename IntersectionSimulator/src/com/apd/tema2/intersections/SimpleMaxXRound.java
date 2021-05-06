package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class SimpleMaxXRound implements Intersection {
    public int maxCars;
    public int waitingTime;
    public int maxLane;
    public Semaphore[] sems_select;
    public Semaphore sem_max;
    public SimpleMaxXRound(int maxLane, int waitingTime, int maxCars) {

        this.maxCars = maxCars;
        this.maxLane = maxLane;
        this.waitingTime = waitingTime;

        sems_select = new Semaphore[maxLane];

        for(int i = 0; i < maxLane; i++) {
            sems_select[i] = new Semaphore(maxCars);
        }

        sem_max = new Semaphore(maxCars);

    }
}
