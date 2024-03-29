package org.example;

/**
 * Hello world!
 *
 */
public class App {
    private int counter;

    public static void main( String[] args ) throws InterruptedException {
        // java программа закончит свое выполнение, когда все потоки приложения закончат свое выполнение

        /*
            в Java за каждым объектом после его создания
            закрепляется сущность под названием монитор (intrinsic lock/monitor lock).
            Эта сущность в один момент времени может быть только у одного потока.

            Synchronized - только один поток может завладеть монитором объекта.
            Вообще этот монитор используется, для того чтобы дать понять Java, что
            в данный момент этот поток взаимодействует с объектом, поэтому другие потоки не должны
            взаимодействовать с этим объектом, они должны ждать.
            Монитор например он нужен для того чтобы поток мог получить доступ к полям объекта или к методам объекта.

            ! Синхронизация происходит на каком-то объекте/на его мониторе. Для синхронизации нужен объект
         */

        // за кулисами Java создает монитор который, закрепляется за объектом
        App app = new App();
        app.doWork();
    }

    /*
        Синхронизировали потоки.

        Только один поток в один момент времени
        получает доступ к выполнению тела метода.
        Один поток выполняет тело метода, другой ждет

        Synchronized -> неявная синхронизация потока на мониторе объекта
     */

    public synchronized void increment() {
        /*
            Все что находится в синхронизованном методе
            может выполнять только один поток в один момент времени
         */
        counter++;
    }

//    public void increment() {
//        // synchronized block -> явная синхронизация потока на мониторе объекта
//        synchronized (this) {
//            counter++;
//        }
//    }

    public void doWork() throws InterruptedException {
        /*
            Состояние гонки
            1: 100 -> 101 (100 + 1) -> counter = 101
            - поток_1 получил больше процессорного времени,
            соответственно он тоже прочитал значение переменной
            counter 100, увеличил это значение на 1 и присвоил его
            переменной counter

            2: 100 -> 101 (100 + 1) -> counter = 101
            - поток_2 успел достать из памяти значение
            переменной counter 100, затем он остановился, т.к.
            процессорное время перешло потоку_1
         */
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    // Операция не атомарна, состоит из 3х действий:
                    // 1. прочитать переменную counter
                    // 2. увеличить на 1
                    // 3. присвоить значение переменной counter
//                    counter = counter + 1;
                    increment();

                    System.out.println("Thread1 -> counter = " + counter);
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
//                    counter = counter + 1;
                    increment();

                    System.out.println("Thread2 -> counter = " + counter);
                }
            }
        });

        thread1.start();
        thread2.start();

        // заставляем текущий поток (main),
        // подождать другие потоки,
        // пока они не закончат свою работу
        thread1.join();
        thread2.join();

        System.out.println(counter);
    }
}
