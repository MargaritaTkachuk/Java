package lab09;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.locks.LockSupport;

public class CallCenterSimulation {
    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("N (кількість операторів): ");
        int operators = Integer.parseInt(console.readLine().trim());

        String[] prompts = {
                "t1 t2 - інтервал між дзвінками клієнтів, мс (два числа): ",
                "t3 t4 - тривалість обслуговування, мс (два числа): ",
                "t5 t6 - пауза перед повторним дзвінком, мс (два числа): "
        };
        Range[] ranges = new Range[3];
        for (int i = 0; i < 3; i++) {
            System.out.print(prompts[i]);
            String[] parts = console.readLine().trim().split("\\s+");
            long low = Long.parseLong(parts[0]);
            long high = Long.parseLong(parts[1]);
            if (low < 0 || low > high) {
                System.out.println("Некоректний діапазон: має бути 0 <= від <= до.");
                return;
            }
            ranges[i] = new Range(low, high);
        }

        System.out.print("Скільки клієнтів змоделювати: ");
        int clientCount = Integer.parseInt(console.readLine().trim());
        if (operators < 1 || clientCount < 0) {
            System.out.println("N має бути не менше 1, кількість клієнтів - невід'ємна.");
            return;
        }

        System.out.println();
        SimulationClock clock = new SimulationClock();
        CallCenter center = new CallCenter(operators);
        List<Client> clients = new ArrayList<>();

        // Головний потік - джерело дзвінків: кожен клієнт стартує як окремий потік
        for (int k = 1; k <= clientCount; k++) {
            long at = clock.now();
            clock.waitUntil(at + ranges[0].random());
            Client client = new Client(k, center, clock, ranges[1], ranges[2]);
            clients.add(client);
            client.start();
        }
        for (Client client : clients) {
            client.join();
        }

        System.out.println();
        System.out.println("Моделювання завершено за " + clock.now() + " мс");
        System.out.println("Клієнтів обслужено: " + clientCount);
        System.out.println("Усього дзвінків (з повторними): " + (clientCount + center.getRefusals()));
        System.out.println("Відмов через зайнятість усіх операторів: " + center.getRefusals());
        System.out.println("Максимум одночасно зайнятих операторів: " + center.getMaxBusy() + " з " + operators);
        for (int op = 1; op <= operators; op++) {
            System.out.println("  Оператор " + op + " обслужив клієнтів: " + center.getServed(op));
        }
    }
}

class Range {
    private final long min;
    private final long max;

    Range(long min, long max) {
        this.min = min;
        this.max = max;
    }

    // Випадкове ціле з відрізка [min, max]
    public long random() {
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }
}

// Спільний годинник програми та очікування без перевірених винятків
class SimulationClock {
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
}

// Call-центр: черга вільних операторів і лічильники статистики
class CallCenter {
    private final Queue<Integer> freeOperators = new ConcurrentLinkedQueue<>();
    private final AtomicInteger busy = new AtomicInteger();
    private final AtomicInteger maxBusy = new AtomicInteger();
    private final AtomicInteger refusals = new AtomicInteger();
    private final AtomicIntegerArray served;

    CallCenter(int operators) {
        served = new AtomicIntegerArray(operators + 1);
        for (int op = 1; op <= operators; op++) {
            freeOperators.add(op);
        }
    }

    // Неблокувальна спроба з'єднатися: номер вільного оператора або null, якщо всі зайняті
    public Integer tryConnect() {
        Integer operator = freeOperators.poll();
        if (operator == null) {
            refusals.incrementAndGet();
            return null;
        }
        int nowBusy = busy.incrementAndGet();
        maxBusy.accumulateAndGet(nowBusy, Math::max);
        return operator;
    }

    // Спершу зменшуємо лічильник, потім повертаємо оператора в чергу: зайнятих ніколи не більше N
    public void release(int operator) {
        busy.decrementAndGet();
        served.incrementAndGet(operator);
        freeOperators.add(operator);
    }

    public int getRefusals() {
        return refusals.get();
    }

    public int getMaxBusy() {
        return maxBusy.get();
    }

    public int getServed(int operator) {
        return served.get(operator);
    }
}

// Один клієнт - один потік
class Client extends Thread {
    private final int number;
    private final CallCenter center;
    private final SimulationClock clock;
    private final Range serviceTime;
    private final Range redialPause;

    Client(int number, CallCenter center, SimulationClock clock, Range serviceTime, Range redialPause) {
        this.number = number;
        this.center = center;
        this.clock = clock;
        this.serviceTime = serviceTime;
        this.redialPause = redialPause;
    }

    @Override
    public void run() {
        int attempt = 1;
        Integer operator = null;
        while (operator == null) {
            System.out.println(String.format("[%6d мс] Клієнт %d: дзвінок (спроба %d)", clock.now(), number, attempt));
            operator = center.tryConnect();
            if (operator == null) {
                long at = clock.now();
                long pause = redialPause.random();
                System.out.println(String.format("[%6d мс] Клієнт %d: усі оператори зайняті, передзвонить через %d мс",
                        at, number, pause));
                clock.waitUntil(at + pause);
                attempt++;
            }
        }

        long start = clock.now();
        long duration = serviceTime.random();
        System.out.println(String.format("[%6d мс] Клієнт %d: з'єднано з оператором %d, розмова триватиме %d мс",
                start, number, operator, duration));
        clock.waitUntil(start + duration);

        // Рядок про завершення друкується ДО звільнення оператора, щоб у журналі він не стояв після нового з'єднання
        System.out.println(String.format("[%6d мс] Клієнт %d: розмову завершено, оператор %d вільний",
                clock.now(), number, operator));
        center.release(operator);
    }
}