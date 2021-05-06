package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.*;
import com.apd.tema2.intersections.*;
import com.apd.tema2.utils.Constants;
import com.apd.tema2.utils.Task;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

/**
 * Clasa Factory ce returneaza implementari ale InterfaceHandler sub forma unor
 * clase anonime.
 */
public class IntersectionHandlerFactory {

    public static IntersectionHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of
        // them)
        // road in maintenance - 2 ways 1 lane each, X cars at a time
        // road in maintenance - 1 way, M out of N lanes are blocked, X cars at a time
        // railroad blockage for s seconds for all the cars
        // unmarked intersection
        // cars racing
        switch (handlerType) {
            case "simple_semaphore" : return new IntersectionHandler() {
                @Override
                public void handle(Car car) {

                    System.out.println("Car " + car.getId() + " has reached the semaphore, now waiting...");

                    try {
                        sleep(car.getWaitingTime() / 10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Car " + car.getId() + " has waited enough, now driving...");
                }
            };
            case "simple_n_roundabout" : return new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    SimpleNRound myIntersection = (SimpleNRound) Main.intersection;

                    System.out.println("Car " + car.getId() + " has reached the roundabout, now waiting...");

                    
                    try {
                        Main.sem.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Car " + car.getId() + " has entered the roundabout");

                    try {
                        sleep(myIntersection.waitingTime / 10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Car " + car.getId() + " has exited the roundabout after " + myIntersection.waitingTime / 1000 + " seconds");

                    Main.sem.release();

                }
            };
            case "simple_strict_1_car_roundabout" : return new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    SimpleStrict1Round myIntersection = (SimpleStrict1Round) Main.intersection;
                    System.out.println("Car " + car.getId() + " has reached the roundabout");

                    try {
                        Main.sem.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        myIntersection.sems[car.getStartDirection()].acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Car " + car.getId() + " has entered the roundabout from lane " + car.getStartDirection());

                    try {
                        sleep(myIntersection.waitingTime / 10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Car " + car.getId() + " has exited the roundabout after " + myIntersection.waitingTime / 1000 + " seconds");

                    myIntersection.sems[car.getStartDirection()].release();

                    Main.sem.release();


                }
            };
            case "simple_strict_x_car_roundabout" : return new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    SimpleStrictxRound myIntersection = (SimpleStrictxRound) Main.intersection;

                    System.out.println("Car " + car.getId() + " has reached the roundabout, now waiting...");

                    try {
                        Main.cb.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                    try {
                        myIntersection.sems_select[car.getStartDirection()].acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Car "+ car.getId() + " was selected to enter the roundabout from lane " +
                            car.getStartDirection());

                    try {
                        myIntersection.cb.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Car " + car.getId() +" has entered the roundabout from lane "
                            + car.getStartDirection());

                    try {
                        sleep(myIntersection.waitingTime / 10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Car " + car.getId() + " has exited the roundabout after " +
                           myIntersection.waitingTime/1000 + " seconds");

                    myIntersection.sems_select[car.getStartDirection()].release();


                }
            };
            case "simple_max_x_car_roundabout" : return new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // Get your Intersection instance
                    SimpleMaxXRound myIntersection = (SimpleMaxXRound) Main.intersection;

                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } // NU MODIFICATI

                    System.out.println("Car " + car.getId() + " has reached the roundabout from lane " +
                            car.getStartDirection());

                    try {
                        myIntersection.sems_select[car.getStartDirection()].acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Car " + car.getId() + " has entered the roundabout from lane " +
                            car.getStartDirection());

                    try {
                        sleep(myIntersection.waitingTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Car " + car.getId() + " has exited the roundabout after " +
                            myIntersection.waitingTime/1000 + " seconds");

                    myIntersection.sems_select[car.getStartDirection()].release();

                }
            };
            case "priority_intersection" : return new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // Get your Intersection instance

                    PriorIntersection myIntersection = (PriorIntersection) Main.intersection;

                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } // NU MODIFICATI

                    if(car.getPriority() == 2) {
                        synchronized (this){
                            if(myIntersection.high_in == 0) {
                                try {
                                    myIntersection.sem_prior.acquire();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            myIntersection.high_in++;
                        }
                        System.out.println("Car " + car.getId() + " with high priority has entered the intersection");

                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        System.out.println("Car " + car.getId() + " with high priority has exited the intersection");

                        synchronized (this) {
                            myIntersection.high_in--;
                            if(myIntersection.high_in == 0) myIntersection.sem_prior.release();
                        }

                            return;

                    }
                    if(car.getPriority() == 1) {
                        System.out.println("Car " + car.getId() + " with low priority is trying to enter the intersection...");
                        try {
                            myIntersection.sem_prior.acquire();
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                        System.out.println("Car " + car.getId() + " with low priority has entered the intersection");
                        myIntersection.sem_prior.release();
                    }
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            case "crosswalk" : return new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    Crosswalk myIntersection = (Crosswalk) Main.intersection;
                    while(!Main.pedestrians.isFinished()) {
                        synchronized (this) {
                            if (myIntersection.green[car.getId()] && Main.pedestrians.isPass()) {
                                System.out.println("Car " + car.getId() + " has now red light");
                                myIntersection.green[car.getId()] ^= true;
                            } else if (!myIntersection.green[car.getId()] && !Main.pedestrians.isPass()) {
                                System.out.println("Car " + car.getId() + " has now green light");
                                myIntersection.green[car.getId()] ^= true;
                            }
                        }
                    }
                }
            };
            case "simple_maintenance" : return new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    Maintenance i = (Maintenance) Main.intersection;

                    while(car.getStartDirection() == 1 && i.first) {
                        synchronized (this) {
                            continue;
                        }
                    }

                    System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() +
                            " has reached the bottleneck");



                    if(car.getStartDirection() == 0) {


                        try {
                            i.mutex.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        i.first = false;

                        if(i.nw_active == 0 && i.ne_active + i.left != i.max) {
                            i.gateE.release(i.max);   // imi dau voie la i.max masini
                            i.ne_active = i.max;
                        }

                        i.mutex.release();


                        try {
                            i.gateE.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        try {
                            i.mutex.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        i.left++;
                        i.ne_active --;

                        System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() +
                                " has passed the bottleneck");

                        if(i.ne_active == 0 && i.left == i.max){
                            i.left = 0;
                            i.gateW.release(i.max);
                            i.nw_active = i.max;
                        }
                        i.mutex.release();

                    } else {



                        try {
                            i.mutex.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        synchronized (this){
                            if(i.first){
                                try {
                                    this.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        if(i.ne_active == 0 && i.nw_active + i.left != i.max) {
                            i.gateW.release(i.max);
                            i.nw_active = i.max;
                        }

                        i.mutex.release();

                        try {
                            i.gateW.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        try {
                            i.mutex.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        i.left++;
                        i.nw_active --;

                        System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() +
                                " has passed the bottleneck");

                        if(i.nw_active == 0 && i.left == i.max){
                            i.left = 0;
                            i.gateE.release(i.max);
                            i.ne_active = i.max;
                        }
                        i.mutex.release();

                    }

                }
            };
            case "complex_maintenance" : return new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    Complex i = (Complex) Main.intersection;
                    int new_lane = i.get_newLane(car.getStartDirection());

                    try {
                        i.mutex.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    i.cars_per_lane[car.getStartDirection()] ++;
                    System.out.println("Car " + car.getId() + " has come from the lane number "
                                    + car.getStartDirection());

                    i.order[car.getStartDirection()].addLast(car.getId());

                    i.mutex.release();

                    try {
                        Main.cb.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                    while(true) {

                        try {
                            i.mutex.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                        LinkedList<Task> tasks = i.lane_tasks.get(new_lane);



                        Task crt_task = tasks.getFirst();
                        if(crt_task.lane == car.getStartDirection() && crt_task.cars_to_pass != 0 &&
                                        i.order[car.getStartDirection()].getFirst() == car.getId()) {
                            crt_task.cars_to_pass --;
                            break;
                        }
                        i.mutex.release();
                    }

                    i.mutex.release();

                    try {
                        i.mutex.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    i.order[car.getStartDirection()].poll();

                    i.cars_per_lane[car.getStartDirection()]--;
                    i.cars_in[car.getStartDirection()]++;


                    System.out.println("Car " + car.getId() + " from the lane " + car.getStartDirection() +
                                " has entered lane number " + new_lane);

                    if(i.cars_per_lane[car.getStartDirection()] == 0) {
                        System.out.println("The initial lane " + car.getStartDirection()
                                + " has been emptied and removed from the new lane queue");
                        LinkedList<Task> tasks = i.lane_tasks.get(new_lane);
                        tasks.poll();
                    } else if(i.cars_in[car.getStartDirection()] == i.max) {
                        System.out.println("The initial lane " + car.getStartDirection() +
                                " has no permits and is moved to the back of the new lane queue");
                        LinkedList<Task> tasks = i.lane_tasks.get(new_lane);
                        Task t = tasks.poll();
                        t.cars_to_pass = i.max;
                        i.cars_in[car.getStartDirection()] = 0;
                        tasks.addLast(t);
                    }
                    i.mutex.release();

                }
            };
            case "railroad" : return new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    Railroad myIntersection = (Railroad) Main.intersection;

                    myIntersection.enter(car);
                }
            };
            default : return null;
        }
    }
}
