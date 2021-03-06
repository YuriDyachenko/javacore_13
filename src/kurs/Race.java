package kurs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static kurs.Main.CARS_COUNT;

public class Race {
    private final ArrayList<Stage> stages;

    //этот объект только для того, чтобы в главном потоке вывести "гонка началась"
    //когда все участники будут готовы
    private final CountDownLatch latchForStart = new CountDownLatch(CARS_COUNT);

    //этот объект для того, чтобы в главном потоке вывести "гонка закончилась"
    private  final CountDownLatch latchForFinish = new CountDownLatch(CARS_COUNT);

    //победителя будем проверять и сохранять в этой переменной
    //а так как это критический блок кода, то используем блокировку
    private Car winner = null;
    private final Lock winnerLock = new ReentrantLock();

    public Race(Stage... stages) {
        this.stages = new ArrayList<>(Arrays.asList(stages));
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
    }

    public void latchForStartCountDown() {
        latchForStart.countDown();
        //но без этого сообщение о старте выводится не там, где надо
        try {
            latchForStart.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void latchForFinishCountDown() {
        latchForFinish.countDown();
    }

    public void waitForReady() {
        //ждем, пока все участники не будут готовы, чтобы вывести сообщение о старте
        try {
            latchForStart.await();
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void waitForFinish() {
        //ждем, пока все участники не завершат гонку
        try {
            latchForFinish.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }

    public void checkWinner(Car car, boolean isLast, String msg) {
        //в блокировке критического блока кода
        //выводим сообщение о завершении этапа
        //и если он последний и победитель еще не определен, фиксируем и выводим победителя
        try {
            winnerLock.lock();
            System.out.println(msg);
            if (isLast && winner == null) {
                winner = car;
                System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> " + car.getName() + " - ПОБЕДИТЕЛЬ!!!");
            }
        } finally {
            winnerLock.unlock();
        }
    }

    public void goAllStages(Car car) {
        //просто проходим все этапы
        int size = stages.size();
        for (int i = 0; i < size; i++) {
            stages.get(i).go(car, i == (size - 1), this);
        }
    }
}
