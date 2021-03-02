package kurs;

public class Car implements Runnable {
    private static int CARS_COUNT;
    private final Race race;
    private final int speed;
    private final String name;
    private final int timeForReady;

    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        timeForReady = 500 + (int) (Math.random() * 800);
        CARS_COUNT++;
        this.name = " участник #" + CARS_COUNT;
    }

    public String getName() {
        return name;
    }

    public long calcTime(int length) {
        return (length / speed) * 1000L;
    }

    public String getStartStageName(String stageName) {
        return name + " начал этап: " + stageName;
    }

    public String getFinishStageName(String stageName) {
        return name + " закончил этап: " + stageName;
    }

    public String getWaitStageName(String stageName) {
        return name + " готовится к этапу (ждет): " + stageName;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(timeForReady);
            System.out.println(this.name + " готов");

            //сообщаем, что этот участник готов, уменьшая счетчик
            race.latchForStartCountDown();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //ждем, пока все участники не будут готовы
        //отдельный барьер, без него часто сообщение о начале этапа проскакивает
        //раньше, чем сообщение "началась гонка"
        race.waitForStart();

        //сама гонка по всем участкам
        //там же определится и выведется победитель
        race.goAllStages(this);

        //сообщаем, что этот участник завершил гонку, уменьшая счетчик
        race.latchForFinishCountDown();
    }
}