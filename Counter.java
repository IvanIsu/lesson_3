package lesson3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Counter implements Runnable {
    ReentrantLock lock;
    Condition condition;

    public Counter(ReentrantLock lock, Condition condition) {
        this.condition = condition;
        this.lock = lock;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            lock.lock();
            try {
                Main.shutdown++;
                System.out.println(Main.shutdown + " " + Thread.currentThread().getName() );
                condition.signal();
                if(i!=4){
                condition.await();}
            }catch (InterruptedException e){

            }finally {
                lock.unlock();
            }

        }
        System.out.println(lock.isLocked() + Thread.currentThread().getName());
    }
}
