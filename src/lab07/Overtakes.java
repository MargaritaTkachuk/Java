package lab07;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Overtakes {
    public static void main(String[] args) throws IOException {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("N (кількість автомобілів): ");
        int n = Integer.parseInt(console.readLine().trim());

        List<Car> cars = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            System.out.print("Автомобіль " + (i + 1) + " (положення швидкість): ");
            String[] parts = console.readLine().trim().split("\\s+");
            cars.add(new Car(Double.parseDouble(parts[0]), Double.parseDouble(parts[1])));
        }

        OvertakeCounter counter = new OvertakeCounter();
        long overtakes = counter.count(cars);
        System.out.println();
        System.out.println("Кількість обгонів: " + overtakes);

        // Для невеликих N додатково перевіряємо перебором усіх пар
        if (n <= 5000) {
            long check = counter.countBruteForce(cars);
            System.out.println("Перевірка перебором усіх пар: " + check
                    + (check == overtakes ? " (збігається)" : " (НЕ ЗБІГАЄТЬСЯ!)"));
        }
    }
}

class Car {
    final double position;
    final double speed;

    Car(double position, double speed) {
        this.position = position;
        this.speed = speed;
    }
}

class OvertakeCounter {
    // O(N log N): сортуємо за положенням, потім рахуємо інверсії в послідовності швидкостей
    public long count(List<Car> cars) {
        List<Car> sorted = new ArrayList<>(cars);
        // Однакове положення: за зростанням швидкості, щоб такі пари не рахувалися інверсіями
        Collections.sort(sorted, Comparator.comparingDouble((Car c) -> c.position)
                .thenComparingDouble(c -> c.speed));

        int n = sorted.size();
        double[] speeds = new double[n];
        for (int i = 0; i < n; i++) {
            speeds[i] = sorted.get(i).speed;
        }
        return sortAndCount(speeds, new double[n], 0, n);
    }

    // Сортування злиттям півінтервалу [from, to) з підрахунком пар i < j, де a[i] > a[j]
    private long sortAndCount(double[] a, double[] buffer, int from, int to) {
        if (to - from < 2) {
            return 0;
        }
        int mid = (from + to) >>> 1;
        long inversions = sortAndCount(a, buffer, from, mid) + sortAndCount(a, buffer, mid, to);

        int i = from;
        int j = mid;
        int k = from;
        while (i < mid && j < to) {
            if (a[i] <= a[j]) {
                buffer[k++] = a[i++];
            } else {
                buffer[k++] = a[j++];
                inversions += mid - i;
            }
        }
        while (i < mid) {
            buffer[k++] = a[i++];
        }
        while (j < to) {
            buffer[k++] = a[j++];
        }
        System.arraycopy(buffer, from, a, from, to - from);
        return inversions;
    }

    // O(N^2): для кожної пари перевіряємо, чи наздожене задній автомобіль передній
    public long countBruteForce(List<Car> cars) {
        long result = 0;
        for (int i = 0; i < cars.size(); i++) {
            for (int j = i + 1; j < cars.size(); j++) {
                Car p = cars.get(i);
                Car q = cars.get(j);
                if (p.position < q.position && p.speed > q.speed) {
                    result++;
                } else if (q.position < p.position && q.speed > p.speed) {
                    result++;
                }
            }
        }
        return result;
    }
}
