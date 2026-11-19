package lab09;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.LockSupport;

public class MessageProcessing {
    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("t1 - інтервал між повідомленнями, мс: ");
        long t1 = Long.parseLong(console.readLine().trim());
        System.out.print("t2 - тривалість обробки одного повідомлення, мс: ");
        long t2 = Long.parseLong(console.readLine().trim());
        System.out.print("Скільки повідомлень згенерувати: ");
        int count = Integer.parseInt(console.readLine().trim());

        System.out.println();
        Clock clock = new Clock();
        MessageQueue queue = new MessageQueue();
        Producer producer = new Producer(queue, clock, t1, count);
        Consumer consumer = new Consumer(queue, clock, t2, count);

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        System.out.println();
        System.out.println("Оброблено повідомлень: " + count + ", загальний час: " + clock.now() + " мс");
    }
}

// Спільний годинник програми та очікування без перевірених винятків
class Clock {
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

class Message {
    private final int number;
    private final long createdAt;

    Message(int number, long createdAt) {
        this.number = number;
        this.createdAt = createdAt;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "повідомлення №" + number;
    }
}

// Потокобезпечна черга FIFO; семафор лічить повідомлення, що вже лежать у черзі
class MessageQueue {
    private final Queue<Message> messages = new ConcurrentLinkedQueue<>();
    private final Semaphore available = new Semaphore(0);

    public void put(Message message) {
        messages.add(message);
        available.release();
    }

    // Блокує обробник, доки черга порожня
    public Message take() {
        available.acquireUninterruptibly();
        return messages.poll();
    }

    public int size() {
        return messages.size();
    }
}

class Producer extends Thread {
    private final MessageQueue queue;
    private final Clock clock;
    private final long interval;
    private final int count;

    Producer(MessageQueue queue, Clock clock, long interval, int count) {
        this.queue = queue;
        this.clock = clock;
        this.interval = interval;
        this.count = count;
    }

    @Override
    public void run() {
        for (int k = 1; k <= count; k++) {
            // Момент k * t1 від старту, а не "t1 після попереднього", тому похибки не накопичуються
            clock.waitUntil(k * interval);
            Message message = new Message(k, clock.now());
            System.out.println(String.format("[%6d мс] Генератор: створено %s", clock.now(), message));
            queue.put(message);
        }
    }
}

class Consumer extends Thread {
    private final MessageQueue queue;
    private final Clock clock;
    private final long processingTime;
    private final int count;

    Consumer(MessageQueue queue, Clock clock, long processingTime, int count) {
        this.queue = queue;
        this.clock = clock;
        this.processingTime = processingTime;
        this.count = count;
    }

    @Override
    public void run() {
        for (int i = 0; i < count; i++) {
            Message message = queue.take();
            long start = clock.now();
            System.out.println(String.format("[%6d мс] Обробник:  показ: %s (чекало %d мс, у черзі лишилось %d)",
                    start, message, start - message.getCreatedAt(), queue.size()));
            clock.waitUntil(start + processingTime);
            System.out.println(String.format("[%6d мс] Обробник:  %s оброблено", clock.now(), message));
        }
    }
}