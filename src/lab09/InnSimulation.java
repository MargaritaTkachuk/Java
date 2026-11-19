package lab09;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

public class InnSimulation {
    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("N (кількість номерів): ");
        int rooms = Integer.parseInt(console.readLine().trim());

        String[] prompts = {
                "t1 t2 - інтервал між приходом клієнтів, мс (два числа): ",
                "t3 t4 - тривалість проживання, мс (два числа): "
        };
        DurationBand[] bands = new DurationBand[2];
        for (int i = 0; i < 2; i++) {
            System.out.print(prompts[i]);
            String[] parts = console.readLine().trim().split("\\s+");
            long low = Long.parseLong(parts[0]);
            long high = Long.parseLong(parts[1]);
            if (low < 0 || low > high) {
                System.out.println("Некоректний діапазон: має бути 0 <= від <= до.");
                return;
            }
            bands[i] = new DurationBand(low, high);
        }

        System.out.print("Скільки клієнтів змоделювати: ");
        int guestCount = Integer.parseInt(console.readLine().trim());
        if (rooms < 1 || guestCount < 0) {
            System.out.println("N має бути не менше 1, кількість клієнтів - невід'ємна.");
            return;
        }

        System.out.println();
        ChronoMeter clock = new ChronoMeter();
        Hostelry hotel = new Hostelry(rooms);
        List<Wanderer> guests = new ArrayList<>();

        // Головний потік - джерело клієнтів: кожен клієнт стартує як окремий потік
        for (int k = 1; k <= guestCount; k++) {
            long at = clock.now();
            clock.waitUntil(at + bands[0].random());
            Wanderer guest = new Wanderer(k, hotel, clock, bands[1]);
            guests.add(guest);
            guest.start();
        }
        for (Wanderer guest : guests) {
            guest.join();
        }

        System.out.println();
        System.out.println("Моделювання завершено за " + clock.now() + " мс");
        System.out.println("Клієнтів оброблено: " + guestCount);
        System.out.println("Клієнтів, яким довелося чекати: " + hotel.getWaitedCount());
        if (guestCount > 0) {
            System.out.println(String.format("Середній час очікування (по всіх клієнтах): %.2f мс",
                    (double) hotel.getTotalWait() / guestCount));
        }
        if (hotel.getWaitedCount() > 0) {
            System.out.println(String.format("Середній час очікування (лише тих, хто чекав): %.2f мс",
                    (double) hotel.getTotalWait() / hotel.getWaitedCount()));
        }
        System.out.println("Максимальний час очікування: " + hotel.getMaxWait() + " мс");
        for (int r = 1; r <= rooms; r++) {
            System.out.println("  Номер " + r + " прийняв клієнтів: " + hotel.getUsedCount(r));
        }
    }
}

class DurationBand {
    private final long min;
    private final long max;

    DurationBand(long min, long max) {
        this.min = min;
        this.max = max;
    }

    // Випадкове ціле з відрізка [min, max]
    public long random() {
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }
}

// Спільний годинник програми та очікування без перевірених винятків
class ChronoMeter {
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

// Готель: чесний семафор (черга очікування FIFO) + черга вільних номерів + статистика очікування
class Hostelry {
    private final Semaphore freeRooms;
    private final Queue<Integer> roomPool = new ConcurrentLinkedQueue<>();
    private final AtomicLong totalWait = new AtomicLong();
    private final AtomicLong maxWait = new AtomicLong();
    private final AtomicInteger waitedCount = new AtomicInteger();
    private final AtomicIntegerArray used;

    Hostelry(int rooms) {
        freeRooms = new Semaphore(rooms, true);
        used = new AtomicIntegerArray(rooms + 1);
        for (int r = 1; r <= rooms; r++) {
            roomPool.add(r);
        }
    }

    // Неблокувальна спроба заселитися: номер кімнати або null, якщо вільних немає
    public Integer tryCheckIn() {
        if (!freeRooms.tryAcquire()) {
            return null;
        }
        return takeFromPool();
    }

    // Блокувальне очікування на звільнення будь-якого номера
    public int awaitCheckIn() {
        freeRooms.acquireUninterruptibly();
        return takeFromPool();
    }

    private int takeFromPool() {
        // Номер повертається в чергу ДО звільнення дозволу, тому тут завжди є що взяти
        int room = roomPool.poll();
        used.incrementAndGet(room);
        return room;
    }

    // Спершу повертаємо номер у чергу, потім відкриваємо дозвіл наступному клієнту
    public void checkOut(int room) {
        roomPool.add(room);
        freeRooms.release();
    }

    public void recordWait(long waited) {
        totalWait.addAndGet(waited);
        maxWait.accumulateAndGet(waited, Math::max);
        if (waited > 0) {
            waitedCount.incrementAndGet();
        }
    }

    public long getTotalWait() {
        return totalWait.get();
    }

    public long getMaxWait() {
        return maxWait.get();
    }

    public int getWaitedCount() {
        return waitedCount.get();
    }

    public int getUsedCount(int room) {
        return used.get(room);
    }
}

// Один клієнт - один потік
class Wanderer extends Thread {
    private final int number;
    private final Hostelry hotel;
    private final ChronoMeter clock;
    private final DurationBand stayTime;

    Wanderer(int number, Hostelry hotel, ChronoMeter clock, DurationBand stayTime) {
        this.number = number;
        this.hotel = hotel;
        this.clock = clock;
        this.stayTime = stayTime;
    }

    @Override
    public void run() {
        long arrived = clock.now();
        System.out.println(String.format("[%6d мс] Клієнт %d: прийшов до готелю", arrived, number));

        Integer room = hotel.tryCheckIn();
        if (room == null) {
            System.out.println(String.format("[%6d мс] Клієнт %d: вільних номерів немає, очікує",
                    clock.now(), number));
            room = hotel.awaitCheckIn();
        }

        long start = clock.now();
        long waited = start - arrived;
        hotel.recordWait(waited);
        long duration = stayTime.random();
        System.out.println(String.format("[%6d мс] Клієнт %d: заселився в номер %d (чекав %d мс), проживатиме %d мс",
                start, number, room, waited, duration));
        clock.waitUntil(start + duration);

        // Рядок про виселення друкується ДО звільнення номера, щоб у журналі він не стояв після нового заселення
        System.out.println(String.format("[%6d мс] Клієнт %d: виселився з номера %d",
                clock.now(), number, room));
        hotel.checkOut(room);
    }
}