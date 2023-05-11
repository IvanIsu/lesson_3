package lesson3;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static int shutdown = 0;
    public static int count = 10;
    public static Object OBJ_LOCK;
    public static ReentrantLock lock = new ReentrantLock();
    public static Condition condition = lock.newCondition();

    public static void main(String[] args) {

        System.out.println("Task 1:");
        OBJ_LOCK = new Object();

        Thread ping = new Thread(() -> {
            synchronized (OBJ_LOCK) {
                while (count > 0) {
                    System.out.println("Ping");
                    count--;
                    OBJ_LOCK.notify();
                    try {
                        OBJ_LOCK.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                OBJ_LOCK.notifyAll();
            }
        });

        Thread pong = new Thread(() -> {
            synchronized (OBJ_LOCK) {
                while (count > 0) {
                    System.out.println("Pong");
                    count--;
                    OBJ_LOCK.notify();
                    try {
                        OBJ_LOCK.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                OBJ_LOCK.notifyAll();
            }
        });
        ping.start();
        pong.start();
        try {
            ping.join();
            pong.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Task 2 :");
        count = 10;
        Thread pingLock = new Thread(() -> {
            while (count > 0) {
                lock.lock();
                try {
                    System.out.println("Ping");
                    count--;
                    condition.signal();
                    condition.await();
                    if (count == 0) {
                        condition.signalAll();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        });
        Thread pongLock = new Thread(() -> {
            while (count > 0) {
                lock.lock();
                try {
                    System.out.println("Pong");
                    count--;
                    condition.signal();
                    condition.await();
                    if (count == 0) {
                        condition.signalAll();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        });

        pingLock.start();
        pongLock.start();


        try {
            pingLock.join();
            pongLock.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition1 = reentrantLock.newCondition();
        for (int i = 0; i < 5; i++) {
            Thread counter = new Thread(new Counter(reentrantLock, condition1));
            counter.setName("Thread " + i);
            counter.start();
        }

    }

}
