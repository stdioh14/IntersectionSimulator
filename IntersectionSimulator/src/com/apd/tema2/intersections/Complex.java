package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;
import com.apd.tema2.utils.Task;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Complex implements Intersection {

    public Semaphore mutex;
    public Semaphore permission[];
    public int cars_out[];
    public int cars_in[];
    public int cars_per_lane[];
    public LinkedList<Integer> order[];
    public int old_lanes;
    public int new_lanes;
    public int max;
    public ArrayList<LinkedList<Task>> lane_tasks;


    public Complex(int new_lanes, int old_lanes, int max) {
        this.old_lanes = old_lanes;
        this.new_lanes = new_lanes;
        this.max = max;

        mutex = new Semaphore(1, true);
        permission = new Semaphore[old_lanes];

        order = new LinkedList[old_lanes];
        for(int i = 0; i < old_lanes ; i++) {
            order[i] = new LinkedList<Integer>();
        }

        for(int i = 0; i < old_lanes; i++){
            permission[i] = new Semaphore(1, true);
        }

        cars_per_lane = new int[old_lanes];
        Arrays.fill(cars_per_lane, 0);

        cars_in = new int[old_lanes];
        Arrays.fill(cars_in, 0);

        cars_out = new int[new_lanes];
        Arrays.fill(cars_out, 0);

        lane_tasks = new ArrayList<LinkedList<Task>>(new_lanes);

        for(int i = 0; i < new_lanes; i++) {
            lane_tasks.add(new LinkedList<Task>());
        }

        int id;
        for(int i = 0; i < old_lanes; i++) {
            id = Math.min(new_lanes - 1, (new_lanes * (i+1)) / old_lanes);
            lane_tasks.get(id).addLast(new Task(i, max));
        }
    }

    public int get_newLane(int lane){
        return Math.min(new_lanes - 1, (new_lanes * (lane+1)) / old_lanes);
    }


}

