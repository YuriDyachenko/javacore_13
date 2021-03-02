package kurs;

import java.util.LinkedList;

public class Main {
    public static final int CARS_COUNT = 4;

    public static void main(String[] args) {

        Race race = new Race(
                new Road(60),
                new Tunnel(),
                new Road(40));

        LinkedList<Car> cars = new LinkedList<>();
        for (int i = 0; i < CARS_COUNT; i++) {
            cars.add(new Car(race, 20 + (int) (Math.random() * 10)));
        }
        for (Car car: cars) {
            new Thread(car).start();
        }

        //гонка начнется, когда все участники подготовятся
        race.waitForReady();

        //вот тут сначала нужно дождаться, когда приедут все
        race.waitForFinish();
    }
}
