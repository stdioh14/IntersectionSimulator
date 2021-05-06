package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Maintenance implements Intersection {
    public int max;
    public int nw_active;
    public int ne_active;
    public int nw_waiting;
    public int ne_waiting;
    public Semaphore mutex;
    public Semaphore gateE;
    public Semaphore gateW;
    public int left;
    public boolean first;
    public Maintenance(int max) {

        this.max = max;
        this.nw_active = 0;
        this.ne_active = 0;
        this.nw_waiting = 0;
        this.ne_waiting = 0;
        this.left = 0;
        this.first = true;

        this.mutex = new Semaphore(1);
        this.gateE = new Semaphore(0);
        this.gateW = new Semaphore(0);

    }
}
