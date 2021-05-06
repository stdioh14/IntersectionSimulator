package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Railroad implements Intersection {
    public int carsNo;
    public Semaphore sem;
    public Railroad() {
        this.carsNo = 0;
        sem = new Semaphore(1, true);
    }
    public void enter(Car car){
        synchronized (this){
            carsNo ++;
            if(carsNo != Main.carsNo) {
                System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection()
                        + " has stopped by the railroad");
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection()
                        + " has started driving");
                this.notify();
            }
            else {
                System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection()
                        + " has stopped by the railroad");
                System.out.println("The train has passed, cars can now proceed");
                this.notify();
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection()
                        + " has started driving");
            }
        }
    }
}
