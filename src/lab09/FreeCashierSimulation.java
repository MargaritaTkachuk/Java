package lab09;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class FreeCashierSimulation {
    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("N (кількість кас): ");
        int cashiers = Integer.parseInt(console.readLine().trim());

        String[] prompts = {
                "t1 t2 - інтервал між приходом відвідувачів, мс (два числа): ",
                "t3 t4 - тривалість обслуговування, мс (два числа): ",
                "t5 t6 - інтервал між перевірками сусідніх черг, мс (два числа, t5 >= 1): "
        };
        MillisSpan[] ranges = new MillisSpan[3];
        for (int i = 0; i < 3; i++) {
            System.out.print(prompts[i]);
            String[] parts = console.readLine().trim().split("\\s+");
            long low = Long.parseLong(parts[0]);
            long high = Long.parseLong(parts[1]);
            if (low < 0 || low > high) {
                System.out.println("Некоректний діапазон: має бути 0 <= від <= до.");
                return;
            }
            ranges[i] = new MillisSpan(low, high);
        }
        if (ranges[2].getMin() < 1) {
            System.out.println("Інтервал між перевірками черг має бути не менше 1 мс.");
            return;
        }

        System.out.print("Скільки відвідувачів змоделювати: ");
        int clientCount = Integer.parseInt(console.readLine().trim());
        if (cashiers < 1 || clientCount < 0) {
            System.out.println("N має бути не менше 1, кількість відвідувачів - невід'ємна.");
            return;
        }

        System.out.println();
        TickWatch clock = new TickWatch();
        Restaurant restaurant = new Restaurant(cashiers, clock);

        // Кожна каса - окремий потік
        List<Cashier> cashierThreads = new ArrayList<>();
        for (int i = 0; i < cashiers; i++) {
            Cashier cashier = new Cashier(i, restaurant, clock, ranges[1]);
            cashierThreads.add(cashier);
            cashier.start();
        }

        // Головний потік - джерело відвідувачів: кожен відвідувач стартує як окремий потік
        List<Diner> clients = new ArrayList<>();
        for (int k = 1; k <= clientCount; k++) {
            long at = clock.now();
            clock.waitUntil(at + ranges[0].random());
            Diner client = new Diner(k, restaurant, clock, ranges[2]);
            clients.add(client);
            client.start();
        }
        for (Diner client : clients) {
            client.join();
        }

        // Усіх обслужено - закриваємо ресторан і зупиняємо каси
        restaurant.close();
        for (Cashier cashier : cashierThreads) {
            cashier.join();
        }

        System.out.println();
        System.out.println("Моделювання завершено за " + clock.now() + " мс");
        System.out.println("Відвідувачів обслужено: " + clientCount);
        System.out.println("Переходів між чергами: " + restaurant.getMoves());
        for (int i = 0; i < cashiers; i++) {
            System.out.println("  Каса " + (i + 1) + " обслужила відвідувачів: " + restaurant.getServed(i));
        }
    }
}

class MillisSpan {
    private final long min;
    private final long max;

    MillisSpan(long min, long max) {
        this.min = min;
        this.max = max;
    }

    public long getMin() {
        return min;
    }

    // Випадкове ціле з відрізка [min, max]
    public long random() {
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }
}

// Спільний годинник програми та очікування без перевірених винятків
class TickWatch {
    private final long startNanos = System.nanoTime();

    public long now() {
        return (System.nanoTime() - startNanos) / 1_000_000L;
    }

    // Чекає до моменту targetMillis (мс від старту програми)
    public void waitUntil(long targetMillis) {
        long targetNanos = startNanos + targetMillis * 1_000_000L;
        long remaining = targetNanos - System.nanoTime();
        while (remaining > 0) {
            LockSupport.parkNanos(remaining);
            remaining = targetNanos - System.nanoTime();
        }
    }

    // Чекає на latch не довше millis мс; true - якщо latch уже відпрацював
    public boolean awaitDone(CountDownLatch latch, long millis) {
        try {
            return latch.await(millis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return true;
        }
    }
}

// Ресторан: черги до кас під одним замком, щоб перехід між чергами був атомарним
class Restaurant {
    private final ReentrantLock lock = new ReentrantLock();
    private final TickWatch clock;
    private final List<LinkedList<Diner>> queues = new ArrayList<>();
    private final List<Condition> notEmpty = new ArrayList<>();
    private final boolean[] busy;
    private final int[] served;
    private boolean closed = false;
    private int moves = 0;

    Restaurant(int cashiers, TickWatch clock) {
        this.clock = clock;
        this.busy = new boolean[cashiers];
        this.served = new int[cashiers];
        for (int i = 0; i < cashiers; i++) {
            queues.add(new LinkedList<>());
            notEmpty.add(lock.newCondition());
        }
    }

    // Скільки людей буде перед новим відвідувачем біля каси i (черга + той, кого обслуговують)
    private int ahead(int i) {
        return queues.get(i).size() + (busy[i] ? 1 : 0);
    }

    // Відвідувач стає у чергу до випадкової каси; повертає її номер (з нуля)
    public int enqueue(Diner client) {
        lock.lock();
        try {
            int i = ThreadLocalRandom.current().nextInt(queues.size());
            queues.get(i).addLast(client);
            notEmpty.get(i).signal();
            System.out.println(String.format("[%6d мс] Відвідувач %d: став до каси %d (людей попереду: %d)",
                    clock.now(), client.getNumber(), i + 1, ahead(i) - 1));
            return i;
        } finally {
            lock.unlock();
        }
    }

    // Переходить у кращу чергу, якщо така є; повертає номер каси, у черзі якої відвідувач тепер
    public int tryMove(Diner client, int current) {
        lock.lock();
        try {
            int position = queues.get(current).indexOf(client);
            if (position < 0) {
                return current; // його вже обслуговують - переходити пізно
            }
            int bestAhead = position + (busy[current] ? 1 : 0);
            int best = current;
            for (int i = 0; i < queues.size(); i++) {
                if (i != current && ahead(i) < bestAhead) {
                    best = i;
                    bestAhead = ahead(i);
                }
            }
            if (best != current) {
                queues.get(current).remove(client);
                queues.get(best).addLast(client);
                notEmpty.get(best).signal();
                moves++;
                System.out.println(String.format(
                        "[%6d мс] Відвідувач %d: перейшов з каси %d на касу %d (людей попереду: %d)",
                        clock.now(), client.getNumber(), current + 1, best + 1, bestAhead));
            }
            return best;
        } finally {
            lock.unlock();
        }
    }

    // Каса бере наступного з черги (блокується, поки черга порожня); null - ресторан закрито
    public Diner next(int cashier) {
        lock.lock();
        try {
            while (queues.get(cashier).isEmpty()) {
                if (closed) {
                    return null;
                }
                notEmpty.get(cashier).awaitUninterruptibly();
            }
            busy[cashier] = true;
            return queues.get(cashier).pollFirst();
        } finally {
            lock.unlock();
        }
    }

    public void finishService(int cashier) {
        lock.lock();
        try {
            busy[cashier] = false;
            served[cashier]++;
        } finally {
            lock.unlock();
        }
    }

    public void close() {
        lock.lock();
        try {
            closed = true;
            for (Condition c : notEmpty) {
                c.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    public int getMoves() {
        lock.lock();
        try {
            return moves;
        } finally {
            lock.unlock();
        }
    }

    public int getServed(int cashier) {
        lock.lock();
        try {
            return served[cashier];
        } finally {
            lock.unlock();
        }
    }
}

// Одна каса - один потік
class Cashier extends Thread {
    private final int index;
    private final Restaurant restaurant;
    private final TickWatch clock;
    private final MillisSpan serviceTime;

    Cashier(int index, Restaurant restaurant, TickWatch clock, MillisSpan serviceTime) {
        this.index = index;
        this.restaurant = restaurant;
        this.clock = clock;
        this.serviceTime = serviceTime;
    }

    @Override
    public void run() {
        Diner client;
        while ((client = restaurant.next(index)) != null) {
            long start = clock.now();
            long duration = serviceTime.random();
            System.out.println(String.format("[%6d мс] Каса %d: обслуговує відвідувача %d, це триватиме %d мс",
                    start, index + 1, client.getNumber(), duration));
            clock.waitUntil(start + duration);

            // Рядок про завершення друкується ДО звільнення каси, щоб у журналі він не стояв після наступного обслуговування
            System.out.println(String.format("[%6d мс] Каса %d: відвідувача %d обслужено",
                    clock.now(), index + 1, client.getNumber()));
            restaurant.finishService(index);
            client.finish();
        }
    }
}

// Один відвідувач - один потік
class Diner extends Thread {
    private final int number;
    private final Restaurant restaurant;
    private final TickWatch clock;
    private final MillisSpan checkInterval;
    private final CountDownLatch served = new CountDownLatch(1);

    Diner(int number, Restaurant restaurant, TickWatch clock, MillisSpan checkInterval) {
        this.number = number;
        this.restaurant = restaurant;
        this.clock = clock;
        this.checkInterval = checkInterval;
    }

    public int getNumber() {
        return number;
    }

    // Викликається касою, коли обслуговування завершено
    public void finish() {
        served.countDown();
    }

    @Override
    public void run() {
        System.out.println(String.format("[%6d мс] Відвідувач %d: зайшов до ресторану", clock.now(), number));
        int cashier = restaurant.enqueue(this);

        // Поки чекаємо - періодично дивимося, чи не звільнилась сусідня каса
        while (!clock.awaitDone(served, checkInterval.random())) {
            cashier = restaurant.tryMove(this, cashier);
        }
        System.out.println(String.format("[%6d мс] Відвідувач %d: пішов", clock.now(), number));
    }
}