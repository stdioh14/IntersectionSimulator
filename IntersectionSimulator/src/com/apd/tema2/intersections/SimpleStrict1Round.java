package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.Semaphore;

public class SimpleStrict1Round implements Intersection {
    public int maxCars;
    public int waitingTime;
    public Semaphore[] sems;
    public SimpleStrict1Round(int maxCars, int waitingTime) {
        this.maxCars = maxCars;
        this.waitingTime = waitingTime;
        sems = new Semaphore[4];
        for(int i = 0; i < 4; i++) {
            sems[i] = new Semaphore(1);
        }
        Main.sem = new Semaphore(maxCars);
    }
}
