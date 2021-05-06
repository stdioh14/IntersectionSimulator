package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;
import com.apd.tema2.entities.Pedestrians;

import java.util.Arrays;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Crosswalk implements Intersection {
    public boolean finished;
    public boolean green[];
    public Crosswalk(int time, int nr_ped) {
        finished = false;
        green = new boolean[Main.carsNo];
        Arrays.fill(green, false);
        Main.pedestrians = new Pedestrians(time, nr_ped);
    }
}
