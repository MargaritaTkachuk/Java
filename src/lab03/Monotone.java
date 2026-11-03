package lab03;

import java.util.Scanner;

public class Monotone {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        int[] starts = new int[n];
        int[] ends = new int[n];
        int count = 0;
        int max = 0;

        int start = 0;
        while (start < n) {
            int j = start;
            if (start < n - 1 && a[start] != a[start + 1]) {
                boolean up = a[start] < a[start + 1];
                while (j < n - 1 && (up ? a[j] < a[j + 1] : a[j] > a[j + 1])) {
                    j++;
                }
            }

            starts[count] = start;
            ends[count] = j;
            count++;
            if (j - start + 1 > max) {
                max = j - start + 1;
            }

            if (j == n - 1) {
                break;
            }
            if (j > start) {
                start = j;
            } else {
                start = start + 1;
            }
        }

        for (int k = 0; k < count; k++) {
            if (ends[k] - starts[k] + 1 == max) {
                StringBuilder line = new StringBuilder();
                for (int i = starts[k]; i <= ends[k]; i++) {
                    if (i > starts[k]) {
                        line.append(' ');
                    }
                    line.append(a[i]);
                }
                System.out.println(line);
            }
        }
    }
}
