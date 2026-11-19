package lab8;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

/*
 * Проміжні операції Stream API (лінивні, повертають новий потік):
 *   Files.walk(...).filter(...).sorted(); lines.filter(...); Arrays.stream(...).sorted(); .filter(...); .skip(...); .limit(...)
 * Термінальні операції (запускають обробку, закривають потік):
 *   toList(), forEachOrdered(...), toArray(), summaryStatistics(), average(), count()
 */
public class Task1Statistics {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: java Task1Statistics <directory> <delta>");
        }

        Path root = Path.of(args[0]);
        double delta = parseFiniteDouble(args[1], "delta");
        if (delta < 0) {
            throw new IllegalArgumentException("delta must be non-negative.");
        }
        if (!Files.isDirectory(root)) {
            throw new IllegalArgumentException("Directory does not exist or is not a directory: " + root);
        }

        // 1. Рекурсивний пошук звичайних файлів log*.txt (символічні посилання не обходимо)
        List<Path> files;
        try (Stream<Path> walk = Files.walk(root)) {
            files = walk.filter(path -> Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS))
                    .filter(Task1Statistics::isMeasurementFile)
                    .sorted()
                    .toList();
        } catch (UncheckedIOException exception) {
            throw new IOException("Cannot traverse directory " + root + ": "
                    + exception.getCause().getMessage(), exception.getCause());
        }
        if (files.isEmpty()) {
            throw new IllegalArgumentException("No files matching log*.txt were found in: " + root);
        }
        System.out.println("Files found (" + files.size() + "):");
        files.forEach(file -> System.out.println("  " + file));

        // 2-3. Читання значень з усіх файлів та їх об'єднання
        List<Double> measurements = new ArrayList<>();
        for (Path file : files) {
            readMeasurements(file, measurements);
        }
        if (measurements.isEmpty()) {
            throw new IllegalArgumentException("The files contain no measurements.");
        }

        double[] values = measurements.stream().mapToDouble(Double::doubleValue).toArray();
        double[] sorted = Arrays.stream(values).sorted().toArray();

        // 4. Кількість, середнє, мінімум, максимум
        DoubleSummaryStatistics statistics = Arrays.stream(values).summaryStatistics();
        double mean = statistics.getAverage();
        System.out.println("Count: " + statistics.getCount());
        System.out.println("Mean: " + mean);
        System.out.println("Min: " + statistics.getMin());
        System.out.println("Max: " + statistics.getMax());

        // 5. Усі вимірювання за зростанням
        System.out.println("Measurements in ascending order: " + Arrays.toString(sorted));

        // 6. Вимірювання, для яких |x - mean| > delta
        double[] deviating = Arrays.stream(values)
                .filter(value -> Math.abs(value - mean) > delta)
                .toArray();
        System.out.println("Values with |x - mean| > " + delta + ": " + Arrays.toString(deviating));

        // 7. Середнє після вилучення одного мінімального та одного максимального значення
        if (statistics.getMin() == statistics.getMax()) {
            System.out.println("Mean without extreme values: " + statistics.getMin()
                    + " (all values are equal)");
        } else if (sorted.length <= 2) {
            System.out.println("Mean without extreme values cannot be computed: "
                    + "no measurements remain after removing the minimum and the maximum.");
        } else {
            // sorted відсортований, тому перший елемент - мінімум, останній - максимум
            double trimmedMean = Arrays.stream(sorted)
                    .skip(1)
                    .limit(sorted.length - 2L)
                    .average()
                    .orElseThrow();
            System.out.println("Mean without extreme values: " + trimmedMean);
        }
    }

    private static boolean isMeasurementFile(Path path) {
        String name = path.getFileName().toString().toLowerCase(Locale.ROOT);
        return name.startsWith("log") && name.endsWith(".txt");
    }

    private static void readMeasurements(Path file, List<Double> destination) throws IOException {
        try (Stream<String> lines = Files.lines(file, StandardCharsets.UTF_8)) {
            // var iterator = lines.iterator();
            // int lineNumber = 0;
            // while (iterator.hasNext()) {
            //     String line = iterator.next();
            //     lineNumber++;
            //     if (line.isBlank()) {
            //         continue;
            //     }
            //     try {
            //         destination.add(parseFiniteDouble(line.trim(), "value"));
            //     } catch (IllegalArgumentException exception) {
            //         throw new IOException(file + ":" + lineNumber + ": " + exception.getMessage(), exception);
            //     }
            // }
            lines.filter(line -> !line.isBlank())
                    .forEachOrdered(line -> destination.add(parseFiniteDouble(line.trim(), "value")));
        } catch (UncheckedIOException exception) {
            // наприклад, некоректне кодування UTF-8
            throw new IOException("Cannot read file " + file + ": " + exception.getCause().getMessage(),
                    exception.getCause());
        } catch (IllegalArgumentException exception) {
            throw new IOException("Invalid data in file " + file + ": " + exception.getMessage(), exception);
        }
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