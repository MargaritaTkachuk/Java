package lab02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class kr2_3 {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] parts = reader.readLine().trim().split("\\s+");
        int n = Integer.parseInt(parts[0]);
        int k = Integer.parseInt(parts[1]);

        System.out.println("Перестановки:");
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = i + 1;
        }
        while (true) {
            StringBuilder line = new StringBuilder();
            for (int x : p) {
                line.append(x);
            }
            System.out.println(line);

            int i = n - 2;
            while (i >= 0 && p[i] > p[i + 1]) {
                i--;
            }
            if (i < 0) {
                break;
            }
            int j = n - 1;
            while (p[j] < p[i]) {
                j--;
            }
            int tmp = p[i];
            p[i] = p[j];
            p[j] = tmp;

            int left = i + 1;
            int right = n - 1;
            while (left < right) {
                tmp = p[left];
                p[left] = p[right];
                p[right] = tmp;
                left++;
                right--;
            }
        }

        System.out.println("Розміщення по " + k + " з " + n + ":");
        int[] a = new int[k];
        boolean[] used = new boolean[n + 1];
        for (int i = 0; i < k; i++) {
            a[i] = i + 1;
            used[i + 1] = true;
        }
        while (true) {
            StringBuilder line = new StringBuilder();
            for (int i = 0; i < k; i++) {
                if (i > 0) {
                    line.append(' ');
                }
                line.append(a[i]);
            }
            System.out.println(line);

            int i = k - 1;
            boolean found = false;
            while (i >= 0 && !found) {
                used[a[i]] = false;
                int v = a[i] + 1;
                while (v <= n && used[v]) {
                    v++;
                }
                if (v <= n) {
                    a[i] = v;
                    used[v] = true;
                    found = true;
                } else {
                    i--;
                }
            }
            if (!found) {
                break;
            }

            int v = 1;
            for (int t = i + 1; t < k; t++) {
                while (used[v]) {
                    v++;
                }
                a[t] = v;
                used[v] = true;
            }
        }
    }
}
