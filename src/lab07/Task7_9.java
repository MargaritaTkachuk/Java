package lab07;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Task7_9 {
    public static void main(String[] args) throws IOException {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("N (кількість людей): ");
        int n = Integer.parseInt(console.readLine().trim());
        System.out.print("K (вилучається кожна K-та людина): ");
        int k = Integer.parseInt(console.readLine().trim());

        Solver[] solvers = {new ArrayListSolver(), new LinkedListIndexSolver(), new LinkedListIteratorSolver()};

        // Прогрів JIT на малому N, щоб не спотворити вимірювання
        for (Solver solver : solvers) {
            solver.survivor(1000, k);
        }

        System.out.println();
        int firstResult = -1;
        for (int i = 0; i < solvers.length; i++) {
            long start = System.nanoTime();
            int result = solvers[i].survivor(n, k);
            double millis = (System.nanoTime() - start) / 1_000_000.0;
            if (i == 0) {
                firstResult = result;
            }
            System.out.println(String.format("%-28s остання людина: %d, час: %.1f мс",
                    solvers[i].name(), result, millis));
            if (result != firstResult) {
                System.out.println("УВАГА: результати не збігаються!");
            }
        }

        if (k == 2) {
            int power = Integer.highestOneBit(n);
            System.out.println("Формула J(N) = 2(N - 2^m) + 1: " + (2 * (n - power) + 1));
        }
    }
}

interface Solver {
    String name();
    int survivor(int n, int k);
}

// Варіант 1: ArrayList, доступ і видалення за індексом
class ArrayListSolver implements Solver {
    public String name() {
        return "ArrayList, remove(index)";
    }

    public int survivor(int n, int k) {
        List<Integer> people = new ArrayList<>(n);
        for (int i = 1; i <= n; i++) {
            people.add(i);
        }
        int index = 0;
        while (people.size() > 1) {
            index = (index + k - 1) % people.size();
            people.remove(index);
        }
        return people.get(0);
    }
}

// Варіант 2: LinkedList, ті самі виклики за індексом
class LinkedListIndexSolver implements Solver {
    public String name() {
        return "LinkedList, remove(index)";
    }

    public int survivor(int n, int k) {
        List<Integer> people = new LinkedList<>();
        for (int i = 1; i <= n; i++) {
            people.add(i);
        }
        int index = 0;
        while (people.size() > 1) {
            index = (index + k - 1) % people.size();
            people.remove(index);
        }
        return people.get(0);
    }
}

// Варіант 3: LinkedList, обхід по колу через Iterator
class LinkedListIteratorSolver implements Solver {
    public String name() {
        return "LinkedList, Iterator";
    }

    public int survivor(int n, int k) {
        LinkedList<Integer> people = new LinkedList<>();
        for (int i = 1; i <= n; i++) {
            people.add(i);
        }
        Iterator<Integer> it = people.iterator();
        while (people.size() > 1) {
            for (int step = 0; step < k; step++) {
                if (!it.hasNext()) {
                    it = people.iterator();
                }
                it.next();
            }
            it.remove();
        }
        return people.getFirst();
    }
}
