package lab02;

import java.util.TreeMap;

public class Ramanujan {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Вкажіть n як аргумент командного рядка.");
            return;
        }

        long n = Long.parseLong(args[0]);

        TreeMap<Long, Integer> count = new TreeMap<>();
        TreeMap<Long, String> ways = new TreeMap<>();

        for (long a = 1; 2 * a * a * a <= n; a++) {
            for (long b = a; a * a * a + b * b * b <= n; b++) {
                long sum = a * a * a + b * b * b;
                String way = a + "^3 + " + b + "^3";

                count.put(sum, count.getOrDefault(sum, 0) + 1);
                if (ways.containsKey(sum)) {
                    ways.put(sum, ways.get(sum) + " = " + way);
                } else {
                    ways.put(sum, way);
                }
            }
        }

        for (long sum : count.keySet()) {
            if (count.get(sum) >= 2) {
                System.out.println(sum + " = " + ways.get(sum));
            }
        }
    }
}
