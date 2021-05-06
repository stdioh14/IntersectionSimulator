package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.Semaphore;

public class SimpleNRound implements Intersection {
    public int maxCars;
    public int waitingTime;
    public SimpleNRound(int maxCars, int waitingTime) {
        this.maxCars = maxCars;
        this.waitingTime = waitingTime;
        Main.sem = new Semaphore(maxCars);
    }
}
