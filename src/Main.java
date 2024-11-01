import java.util.*;

public class Main {
    public static void main(String[] args) {

        BlockingQueue<Integer> queue = new BlockingQueue<>(5);
        Thread pt = new Thread(new Producer(queue, 10));
        Thread ct = new Thread(new Consumer(queue, 10));

        pt.start();
        ct.start();

        try {
            pt.join();
            ct.join();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("finished");
    }
}

class Producer implements Runnable {
    private final BlockingQueue<Integer> q;
    private final int itemsP;

    public Producer(BlockingQueue<Integer> q, int itemsP) {
        this.q = q;
        this.itemsP = itemsP;
    }

    @Override
    public void run() {
        for (int i = 0; i < itemsP; i++) {
            try {
                System.out.println("produces "+i);
                q.enqueue(i);
                System.out.println("qeue size extended"+q.size());
                Thread.sleep(500);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}

class Consumer extends Thread {
    private final BlockingQueue<Integer> q;
    private final int itemsC;

    public Consumer(BlockingQueue<Integer> q, int itemsC) {
        this.q = q;
        this.itemsC = itemsC;
    }

    @Override
    public void run() {
        for (int i=0; i<itemsC; i++) {
            try {
                int item = q.dequeue();
                System.out.println("consuming "+item+"\nqueue size shrunk "+q.size());
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}


class BlockingQueue<T> {
    private final Queue<T> q;
    private final int max;

    public BlockingQueue(int max) {
        this.q = new LinkedList<>();
        this.max = max;
    }

    public synchronized void enqueue(T t) throws Exception {
        while (q.size()==max) {
            wait();
        }
        q.add(t);
        notifyAll();
    }

    public synchronized T dequeue() throws Exception {
        while (q.isEmpty()) {
            wait();
        }
        T item = q.remove();
        notifyAll();
        return item;
    }

    public synchronized int size() {
        return q.size();
    }
}
