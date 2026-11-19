package lab8;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

/*
 * Проміжні операції Stream API: rangeClosed, range, filter, mapToObj, mapToDouble, map.
 * Термінальні операції: toList, toArray, count, max.
 *
 * Що природно виражається через IntStream.range: перебір індексів i = 1..n-2 для пошуку
 * локальних екстремумів і різниць сусідніх елементів для підрахунку переходів.
 * Найдовший зростаючий фрагмент потребує знання меж серій: спочатку окремим проходом
 * знаходимо індекси, з яких починається нова серія (перший елемент або a[i] <= a[i-1]),
 * а межі серій отримуємо як сусідні початки.
 *
 * Використання: java Task5Sequence 1 3 5 4 2 6 8 7 9 11 10
 */
public class Task5Sequence {
    private record Extremum(int index, double value) {}

    private record Run(int start, int end) {
        int length() {
            return end - start + 1;
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Usage: java Task5Sequence <number> [<number> ...]");
        }
        double[] sequence = Arrays.stream(args)
                .mapToDouble(text -> parseFiniteDouble(text, "element"))
                .toArray();
        int n = sequence.length;

        // 1-2. Локальні максимуми та мінімуми (для n < 3 діапазон порожній)
        List<Extremum> maxima = IntStream.rangeClosed(1, n - 2)
                .filter(i -> sequence[i] > sequence[i - 1] && sequence[i] > sequence[i + 1])
                .mapToObj(i -> new Extremum(i, sequence[i]))
                .toList();
        List<Extremum> minima = IntStream.rangeClosed(1, n - 2)
                .filter(i -> sequence[i] < sequence[i - 1] && sequence[i] < sequence[i + 1])
                .mapToObj(i -> new Extremum(i, sequence[i]))
                .toList();
        System.out.println("Sequence: " + Arrays.toString(sequence));
        System.out.println("Local maxima: " + format(maxima));
        System.out.println("Local minima: " + format(minima));

        // 3. Переходи між сусідніми різницями: знак різниці a[i+1] - a[i] (>0 зростання, <0 спадання, 0 рівність)
        int[] signs = IntStream.range(0, n - 1)
                .map(i -> direction(sequence[i], sequence[i + 1]))
                .toArray();
        long risingToFalling = IntStream.range(0, signs.length - 1)
                .filter(i -> signs[i] > 0 && signs[i + 1] < 0)
                .count();
        long fallingToRising = IntStream.range(0, signs.length - 1)
                .filter(i -> signs[i] < 0 && signs[i + 1] > 0)
                .count();
        System.out.println("Transitions from strictly rising to strictly falling: " + risingToFalling);
        System.out.println("Transitions from strictly falling to strictly rising: " + fallingToRising);

        // 4-6. Найдовший неперервний строго зростаючий фрагмент
        int[] starts = IntStream.range(0, n)
                .filter(i -> i == 0 || sequence[i] <= sequence[i - 1])
                .toArray();
        Run longest = IntStream.range(0, starts.length)
                .mapToObj(k -> new Run(starts[k], (k + 1 < starts.length ? starts[k + 1] : n) - 1))
                // при однаковій довжині перемагає фрагмент, що починається раніше
                .max(Comparator.comparingInt(Run::length)
                        .thenComparingInt(run -> -run.start()))
                .orElseThrow();
        System.out.println("Longest strictly increasing run: start index " + longest.start()
                + ", end index " + longest.end() + ", length " + longest.length());
        System.out.println("Elements: " + Arrays.toString(
                Arrays.copyOfRange(sequence, longest.start(), longest.end() + 1)));
    }

    // 1 - зростання, -1 - спадання, 0 - рівність (на відміну від Double.compare, 0.0 і -0.0 рівні)
    private static int direction(double from, double to) {
        return to > from ? 1 : to < from ? -1 : 0;
    }

    private static String format(List<Extremum> extrema) {
        if (extrema.isEmpty()) {
            return "none";
        }
        return extrema.stream()
                .map(e -> e.value() + " at index " + e.index())
                .toList()
                .toString();
    }

    private static double parseFiniteDouble(String text, String name) {
        try {
            double value = Double.parseDouble(text);
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException(name + " must be a finite number: " + text);
            }
            return value;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(name + " must be a number: " + text, exception);
        }
    }
}