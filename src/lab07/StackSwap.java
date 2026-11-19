package lab07;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class StackSwap {
    public static void main(String[] args) throws IOException {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        String[] names = {"Перший", "Другий"};
        List<Stack<Integer>> stacks = new ArrayList<>();

        // Ініціалізація: числа вводяться від дна стека до вершини
        for (int s = 0; s < 2; s++) {
            Stack<Integer> stack = new Stack<>();
            System.out.print(names[s] + " стек, числа через пробіл (від дна до вершини, порожній рядок - порожній стек): ");
            String line = console.readLine().trim();
            if (!line.isEmpty()) {
                for (String token : line.split("\\s+")) {
                    stack.push(Integer.parseInt(token));
                }
            }
            stacks.add(stack);
        }
        Stack<Integer> first = stacks.get(0);
        Stack<Integer> second = stacks.get(1);

        // Копії початкового стану для перевірки результату
        Stack<Integer> originalFirst = new Stack<>();
        originalFirst.addAll(first);
        Stack<Integer> originalSecond = new Stack<>();
        originalSecond.addAll(second);

        System.out.println();
        System.out.println("До обміну:");
        System.out.println("  Перший: " + first + (first.isEmpty() ? "" : ", вершина: " + first.peek()));
        System.out.println("  Другий: " + second + (second.isEmpty() ? "" : ", вершина: " + second.peek()));

        new StackSwapper<Integer>().swap(first, second);

        System.out.println();
        System.out.println("Після обміну:");
        System.out.println("  Перший: " + first + (first.isEmpty() ? "" : ", вершина: " + first.peek()));
        System.out.println("  Другий: " + second + (second.isEmpty() ? "" : ", вершина: " + second.peek()));

        boolean correct = first.equals(originalSecond) && second.equals(originalFirst);
        System.out.println();
        System.out.println("Перевірка (вміст і порядок елементів збережено): " + (correct ? "успішно" : "ПОМИЛКА"));
    }
}

// Обмінює вміст двох стеків, користуючись лише push, pop і isEmpty
class StackSwapper<T> {
    public void swap(Stack<T> first, Stack<T> second) {
        Stack<T> temporaryFirst = new Stack<>();
        Stack<T> temporarySecond = new Stack<>();

        // Крок 1-2: перекладаємо стеки в тимчасові (порядок при цьому обертається)
        while (!first.isEmpty()) {
            temporaryFirst.push(first.pop());
        }
        while (!second.isEmpty()) {
            temporarySecond.push(second.pop());
        }

        // Крок 3-4: перекладаємо навхрест, і порядок обертається вдруге, тобто відновлюється
        while (!temporarySecond.isEmpty()) {
            first.push(temporarySecond.pop());
        }
        while (!temporaryFirst.isEmpty()) {
            second.push(temporaryFirst.pop());
        }
    }
}
