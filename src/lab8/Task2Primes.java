package lab8;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/*
 * Проміжні операції Stream API: map, filter, boxed, mapToObj.
 * Термінальні операції: toArray, toList.
 * IntStream.range(...) лише створює потік індексів; решето Ератосфена виконується окремим методом findPrimes.
 */
public class Task2Primes {
    private static final int MAX_INTERVAL_SIZE = 10_000_000;

    private record TwinPair(int first, int second) {}

    private record ReversedPair(int original, long reversed) {}

    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: java Task2Primes <a> <b>");
        }
        int a = parseInt(args[0], "a");
        int b = parseInt(args[1], "b");
        if (a < 2 || a > b) {
            throw new IllegalArgumentException("Bounds must satisfy 2 <= a <= b.");
        }
        long intervalSize = (long) b - a + 1;
        if (intervalSize > MAX_INTERVAL_SIZE) {
            throw new IllegalArgumentException(
                    "The interval contains " + intervalSize + " numbers; the maximum supported size is "
                            + MAX_INTERVAL_SIZE + ".");
        }

        int[] primeArray = findPrimes(a, b);
        List<Integer> primes = Arrays.stream(primeArray).boxed().toList();

        // Сусідні прості числа з різницею 2 (між ними не може бути іншого простого)
        List<TwinPair> twinPairs = IntStream.range(0, primeArray.length - 1)
                .filter(index -> primeArray[index + 1] - primeArray[index] == 2)
                .mapToObj(index -> new TwinPair(primeArray[index], primeArray[index + 1]))
                .toList();

        // Прості числа, розвернутий запис яких теж простий і відрізняється від початкового
        List<ReversedPair> reversiblePrimes = Arrays.stream(primeArray)
                .mapToObj(prime -> new ReversedPair(prime, reverseDigits(prime)))
                .filter(pair -> pair.reversed() != pair.original() && isPrime(pair.reversed()))
                .toList();

        System.out.println("Prime numbers: " + primes);
        System.out.println("Count: " + primes.size());
        System.out.println("Twin-prime pairs: " + twinPairs);
        System.out.println("Primes whose reversed digits also form a different prime: " + reversiblePrimes);
    }

    private static int[] findPrimes(int start, int end) {
        int size = (int) ((long) end - start + 1);
        boolean[] composite = new boolean[size];
        int limit = (int) Math.sqrt(end);
        boolean[] baseComposite = new boolean[limit + 1];
        for (int candidate = 2; candidate <= limit; candidate++) {
            if (!baseComposite[candidate]) {
                if ((long) candidate * candidate <= limit) {
                    for (int multiple = candidate * candidate; multiple <= limit; multiple += candidate) {
                        baseComposite[multiple] = true;
                    }
                }
                long firstMultiple = Math.max(
                        (long) candidate * candidate,
                        ((long) start + candidate - 1) / candidate * candidate);
                for (long multiple = firstMultiple; multiple <= end; multiple += candidate) {
                    composite[(int) (multiple - start)] = true;
                }
            }
        }
        return IntStream.range(0, size)
                .map(index -> index + start)
                .filter(number -> number >= 2 && !composite[number - start])
                .toArray();
    }

    private static boolean isPrime(long number) {
        if (number < 2) {
            return false;
        }
        if (number % 2 == 0) {
            return number == 2;
        }
        for (long divisor = 3; divisor <= number / divisor; divisor += 2) {
            if (number % divisor == 0) {
                return false;
            }
        }
        return true;
    }

    private static long reverseDigits(int number) {
        long remaining = number;
        long reversed = 0;
        while (remaining > 0) {
            reversed = reversed * 10 + remaining % 10;
            remaining /= 10;
        }
        return reversed;
    }

    private static int parseInt(String text, String name) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(name + " must be an integer: " + text, exception);
        }
    }
}