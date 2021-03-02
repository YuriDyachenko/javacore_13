package kurs;

public abstract class Stage {
    protected int length;
    protected String description;

    //чтобы вывести сообщение о победителе сразу после окончания этапа
    //и не дать втулиться там какому-то другому потоку
    //передаем признак, что это последняя стадия и всю гонку
    //которая обеспечит блокировку вывода двух сообщений подряд ну и саму проверку на победителя
    public abstract void go(Car car, boolean isLast, Race race);
}
