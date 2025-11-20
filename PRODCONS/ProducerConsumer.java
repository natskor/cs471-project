import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ProducerConsumer.java
 * - producer-consumer problem with semaphores and circular buffer.
 * - Run: java ProducerConsumer <p> <c> <b>
 *   where p = producers (also number of stores), c = consumers, b = buffer size
 */
public class ProducerConsumer {

    // Sales record
    static class SalesRecord {
        int DD, MM, YY;
        int storeId, register;
        float saleAmount;

        SalesRecord(int DD, int MM, int YY, int storeId, int register, float saleAmount) {
            this.DD = DD;
            this.MM = MM;
            this.YY = YY;
            this.storeId = storeId;
            this.register = register;
            this.saleAmount = saleAmount;
        }
    }

    // Statistics
    static class Statistics {
        final double[] storeTotals;
        final double[] monthTotals = new double[12];
        double aggregate = 0.0;

        Statistics(int numStores) {
            storeTotals = new double[numStores];
        }

        // add a single record
        synchronized void addRecord(SalesRecord r) {
            if (r.storeId >= 1 && r.storeId <= storeTotals.length)
                storeTotals[r.storeId - 1] += r.saleAmount;
            if (r.MM >= 1 && r.MM <= 12)
                monthTotals[r.MM - 1] += r.saleAmount;
            aggregate += r.saleAmount;
        }

        // merge another Stats into this one
        synchronized void mergeFrom(Statistics other) {
            for (int i = 0; i < storeTotals.length; i++)
                storeTotals[i] += other.storeTotals[i];
            for (int i = 0; i < 12; i++)
                monthTotals[i] += other.monthTotals[i];
            aggregate += other.aggregate;
        }

        // print global stats
        void printGlobal() {
            System.out.println("\n=== GLOBAL STATISTICS ===");
            for (int i = 0; i < storeTotals.length; i++) {
                System.out.printf("Store %d total: %.2f%n", i + 1, storeTotals[i]);
            }
            for (int i = 0; i < 12; i++) {
                System.out.printf("Month %d total: %.2f%n", i + 1, monthTotals[i]);
            }
            System.out.printf("Aggregate sales: %.2f%n", aggregate);
        }
    }

    // Shared circular buffer
    static class CircularBuffer {
        private final SalesRecord[] buf;
        private int in = 0;
        private int out = 0;
        final Semaphore empty;
        final Semaphore full;
        final Semaphore mutex; // binary semaphore for mutual exclusion

        CircularBuffer(int size) {
            buf = new SalesRecord[size];
            empty = new Semaphore(size);
            full = new Semaphore(0);
            mutex = new Semaphore(1);
        }

        // called by producers to insert a record into the buffer
        void put(SalesRecord r) throws InterruptedException {
            empty.acquire();   // wait for empty slot
            mutex.acquire();   // enter critical section

            // critical section
            buf[in] = r;
            in = (in + 1) % buf.length;

            mutex.release();   // exit critical section
            full.release();    // signal there is one more full slot
        }

        // called by consumers to remove and return a record
        SalesRecord take() throws InterruptedException {
            full.acquire();    // wait for full slot
            mutex.acquire();   // enter critical section

            // critical section
            SalesRecord r = buf[out];
            out = (out + 1) % buf.length;

            mutex.release();   // exit critical section
            empty.release();   // signal there is one more empty slot

            return r;
        }
    }

    // Producer
    static class Producer implements Runnable {
        private final int storeId;
        private final CircularBuffer buffer;
        private final AtomicInteger totalProduced;
        private final int maxItems;
        private final Random rand = new Random();

        Producer(int storeId, CircularBuffer buffer, AtomicInteger totalProduced, int maxItems) {
            this.storeId = storeId;
            this.buffer = buffer;
            this.totalProduced = totalProduced;
            this.maxItems = maxItems;
        }

        @Override
        public void run() {
            while (true) {
                int cur = totalProduced.get();
                if (cur >= maxItems) break;
                if (!totalProduced.compareAndSet(cur, cur + 1)) {
                    continue;
                }

                // produce random record
                int DD = rand.nextInt(30) + 1;            // 1-30
                int MM = rand.nextInt(12) + 1;            // 1-12
                int YY = 16;                              // fixed at 16
                int reg = rand.nextInt(6) + 1;            // 1-6
                float amt = 0.50f + rand.nextFloat() * (999.99f - 0.50f);

                SalesRecord r = new SalesRecord(DD, MM, YY, storeId, reg, amt);

                try {
                    buffer.put(r);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }

                // random sleep 5-40 ms
                try {
                    Thread.sleep(rand.nextInt(36) + 5);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    // Consumer 
    static class Consumer implements Runnable {
        private final int id;
        private final CircularBuffer buffer;
        private final Statistics globalStats;

        // local stats for consumer
        private final Statistics localStats;

        Consumer(int id, CircularBuffer buffer, Statistics globalStats) {
            this.id = id;
            this.buffer = buffer;
            this.globalStats = globalStats;
            this.localStats = new Statistics(globalStats.storeTotals.length);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    SalesRecord r = buffer.take(); // semaphore
                    // sentinel check for exiting
                    if (r.storeId == -1) break;
                    localStats.addRecord(r);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // print local stats atomically to prevent interleaving
            synchronized (System.out) {
                System.out.printf("Consumer %d local statistics:%n", id);
                for (int i = 0; i < localStats.storeTotals.length; i++)
                    System.out.printf("  Store %d total: %.2f%n", i + 1, localStats.storeTotals[i]);
                for (int i = 0; i < 12; i++)
                    System.out.printf("  Month %d total: %.2f%n", i + 1, localStats.monthTotals[i]);
                System.out.printf("  Aggregate: %.2f%n\n", localStats.aggregate);
            }

            // merge into global stats
            synchronized (globalStats) {
                globalStats.mergeFrom(localStats);
            }
        }
    }

    // MAIN
    public static void main(String[] args) throws InterruptedException {
        // defaults
        int p = 2;   // producers (also number of stores)
        int c = 2;   // consumers
        int b = 3;   // buffer size
        final int MAX_ITEMS = 1000; // max items

        // take input from command line
        if (args.length >= 3) {
            try {
                p = Integer.parseInt(args[0]);
                c = Integer.parseInt(args[1]);
                b = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid arguments. Usage: java ProducerConsumerSimulation <p> <c> <b>");
                return;
            }
        } else {
            System.out.println("Using defaults: p=2 c=2 b=3 (input <p> <c> <b> to replace these)");
        }

        System.out.printf("Running with p=%d producers, c=%d consumers, buffer size b=%d, maxItems=%d%n\n",
                p, c, b, MAX_ITEMS);

        CircularBuffer buffer = new CircularBuffer(b);
        AtomicInteger totalProduced = new AtomicInteger(0);
        Statistics globalStats = new Statistics(p);

        Thread[] producers = new Thread[p];
        Thread[] consumers = new Thread[c];

        long start = System.nanoTime(); // track time in ms

        // start producers
        for (int i = 0; i < p; i++) {
            producers[i] = new Thread(new Producer(i + 1, buffer, totalProduced, MAX_ITEMS));
            producers[i].start();
        }

        // start consumers
        for (int i = 0; i < c; i++) {
            consumers[i] = new Thread(new Consumer(i + 1, buffer, globalStats));
            consumers[i].start();
        }

        // wait for producers to finish producing
        for (Thread t : producers) t.join();

        // put one sentinel per consumer using semaphores
        for (int i = 0; i < c; i++) {
            // sentinel record: storeId = -1
            SalesRecord sentinel = new SalesRecord(-1, -1, -1, -1, -1, -1f);
            buffer.put(sentinel); // uses semaphores
        }

        // wait for consumers to finish
        for (Thread t : consumers) t.join();

        long end = System.nanoTime(); // track time in ms

        // print global statistics and time
        globalStats.printGlobal();
        System.out.printf("Total simulation time: %.2f ms%n", (end - start) / 1_000_000.0);
    }
}