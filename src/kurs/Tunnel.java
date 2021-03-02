package kurs;

import java.util.concurrent.Semaphore;
import static kurs.Main.CARS_COUNT;

public class Tunnel extends Stage {
    //пропускная способность - половина всех машин
    Semaphore smp = new Semaphore(CARS_COUNT / 2);

    public Tunnel() {
        this.length = 80;
        this.description = "Тоннель " + length + " метров";
    }

    @Override
    public void go(Car car, boolean isLast, Race race) {
        try {
            try {
                //сообщение об ожидании
                System.out.println(car.getWaitStageName(description));
                //проехать может только 2 машины сразу, ждем "место"
                //захватиываем туннель, если осободился/свободен
                smp.acquire();
                //сообщение о начале этапа
                System.out.println(car.getStartStageName(description));
                //ждем время
                Thread.sleep(car.calcTime(length));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                //освобождаем туннель
                smp.release();
                //сообщение об окончании этапа с блокировкой для проверки победителя
                race.checkWinner(car, isLast, car.getFinishStageName(description));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
