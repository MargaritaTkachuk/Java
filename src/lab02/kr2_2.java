package lab02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class kr2_2 {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] parts = reader.readLine().trim().split("\\s+");

        long x = Long.parseLong(parts[0]);
        int k = Integer.parseInt(parts[1]);
        int bit = Integer.parseInt(parts[2]);

        long mask = 1L << (k - 1);
        long result;
        if (bit == 1) {
            result = x | mask;
        } else {
            result = x & ~mask;
        }

        System.out.println(result + " 0x" + Long.toHexString(result).toUpperCase() + " " + Long.toBinaryString(result));
    }
}
