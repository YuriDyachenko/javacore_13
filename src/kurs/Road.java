package kurs;

public class Road extends Stage {

    public Road(int length) {
        this.length = length;
        this.description = "Дорога " + length + " метров";
    }

    @Override
    public void go(Car car, boolean isLast, Race race) {
        try {
            //сообщение о начале этапа
            System.out.println(car.getStartStageName(description));
            //ждем время
            Thread.sleep(car.calcTime(length));
            //сообщение об окончании этапа с блокировкой для проверки победителя
            race.checkWinner(car, isLast, car.getFinishStageName(description));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}