package lab02;

public class dr2_11 {
    public static void main(String[] args) {
        for (int a = 10; a <= 99; a++) {
            for (int b = a; b <= 99; b++) {
                int p = a * b;
                if (p < 1000 || p > 9999) {
                    continue;
                }
                if (a % 10 == 0 && b % 10 == 0) {
                    continue;
                }

                int[] cnt = new int[10];
                cnt[a / 10]++;
                cnt[a % 10]++;
                cnt[b / 10]++;
                cnt[b % 10]++;
                cnt[p / 1000]--;
                cnt[p / 100 % 10]--;
                cnt[p / 10 % 10]--;
                cnt[p % 10]--;

                boolean ok = true;
                for (int d = 0; d < 10; d++) {
                    if (cnt[d] != 0) {
                        ok = false;
                    }
                }

                if (ok) {
                    System.out.println(p + " = " + a + " * " + b);
                }
            }
        }
    }
}
