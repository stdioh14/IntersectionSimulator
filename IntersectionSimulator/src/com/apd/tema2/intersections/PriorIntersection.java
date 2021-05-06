package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class PriorIntersection implements Intersection {
    public int nr_high;
    public int nr_low;
    public int high_in;
    public Semaphore sem_prior;
    public PriorIntersection(int nr_high, int nr_low) {

        this.nr_high = nr_high;
        this.nr_low = nr_low;

        sem_prior = new Semaphore(1,true);

        high_in = 0;
    }
}
