package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Pedestrians;
import com.apd.tema2.entities.ReaderHandler;
import com.apd.tema2.intersections.*;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Returneaza sub forma unor clase anonime implementari pentru metoda de citire din fisier.
 */
public class ReaderHandlerFactory {

    public static ReaderHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of them)
        // road in maintenance - 1 lane 2 ways, X cars at a time
        // road in maintenance - N lanes 2 ways, X cars at a time
        // railroad blockage for T seconds for all the cars
        // unmarked intersection
        // cars racing
        switch (handlerType) {
            case "simple_semaphore" : return new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) {
                    // Exemplu de utilizare:
                    // Main.intersection = IntersectionFactory.getIntersection("simpleIntersection");
                }
            };
            case "simple_n_roundabout" : return new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    // To parse input line use:
                    String[] line = br.readLine().split(" ");
                    Main.intersection = new SimpleNRound(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
                }
            };
            case "simple_strict_1_car_roundabout" : return new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");
                    Main.intersection = new SimpleStrict1Round(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
                }
            };
            case "simple_strict_x_car_roundabout" : return new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");
                    Main.intersection = new SimpleStrictxRound(Integer.parseInt(line[0]), Integer.parseInt(line[1]),
                            Integer.parseInt(line[2]));
                }
            };
            case "simple_max_x_car_roundabout" : return new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");
                    Main.intersection = new SimpleMaxXRound(Integer.parseInt(line[0]), Integer.parseInt(line[1]),
                            Integer.parseInt(line[2]));
                }
            };
            case "priority_intersection" : return new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");
                    Main.intersection = new PriorIntersection(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
                }
            };
            case "crosswalk" : return new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");
                    Main.intersection = new Crosswalk(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
                }
            };
            case "simple_maintenance" : return new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");
                    Main.intersection = new Maintenance(Integer.parseInt(line[0]));
                }
            };
            case "complex_maintenance" : return new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    String[] line = br.readLine().split(" ");
                    Main.intersection = new Complex(Integer.parseInt(line[0]),Integer.parseInt(line[1]),
                                         Integer.parseInt(line[2]));
                }
            };
            case "railroad" : return new ReaderHandler() {
                @Override
                public void handle(final String handlerType, final BufferedReader br) throws IOException {
                    Main.intersection = new Railroad();
                }
            };
            default : return null;
        }
    }

}
