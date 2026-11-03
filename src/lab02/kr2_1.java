package lab02;

public class kr2_1 {
    public static void main(String[] args) {
        int a = 0b00101010_10101010_10101010_10101010; // старший біт = 0
        int b = 0b10101010_10101010_10101010_10101010; // старший біт = 1
        int n = 3;

//        System.out.println("a       = " + Integer.toBinaryString(a));
//        System.out.println("b       = " + Integer.toBinaryString(b));
//
//        System.out.println("a & b   = " + Integer.toBinaryString(a & b));
//        System.out.println("a | b   = " + Integer.toBinaryString(a | b));
//        System.out.println("a ^ b   = " + Integer.toBinaryString(a ^ b));
//
//        System.out.println("~a      = " + Integer.toBinaryString(~a));
//        System.out.println("~b      = " + Integer.toBinaryString(~b));
//
//        System.out.println("a << n  = " + Integer.toBinaryString(a << n));
//        System.out.println("b << n  = " + Integer.toBinaryString(b << n));
//
//        System.out.println("a >> n  = " + Integer.toBinaryString(a >> n));
//        System.out.println("b >> n  = " + Integer.toBinaryString(b >> n));
//
//        System.out.println("a >>> n = " + Integer.toBinaryString(a >>> n));
//        System.out.println("b >>> n = " + Integer.toBinaryString(b >>> n));

        System.out.println("a       = " + String.format("%32s", Integer.toBinaryString(a)).replace(' ', '0'));
        System.out.println("b       = " + String.format("%32s", Integer.toBinaryString(b)).replace(' ', '0'));

        System.out.println("a & b   = " + String.format("%32s", Integer.toBinaryString(a & b)).replace(' ', '0'));
        System.out.println("a | b   = " + String.format("%32s", Integer.toBinaryString(a | b)).replace(' ', '0'));
        System.out.println("a ^ b   = " + String.format("%32s", Integer.toBinaryString(a ^ b)).replace(' ', '0'));

        System.out.println("~a      = " + String.format("%32s", Integer.toBinaryString(~a)).replace(' ', '0'));
        System.out.println("~b      = " + String.format("%32s", Integer.toBinaryString(~b)).replace(' ', '0'));

        System.out.println("a << n  = " + String.format("%32s", Integer.toBinaryString(a << n)).replace(' ', '0'));
        System.out.println("b << n  = " + String.format("%32s", Integer.toBinaryString(b << n)).replace(' ', '0'));

        System.out.println("a >> n  = " + String.format("%32s", Integer.toBinaryString(a >> n)).replace(' ', '0'));
        System.out.println("b >> n  = " + String.format("%32s", Integer.toBinaryString(b >> n)).replace(' ', '0'));

        System.out.println("a >>> n = " + String.format("%32s", Integer.toBinaryString(a >>> n)).replace(' ', '0'));
        System.out.println("b >>> n = " + String.format("%32s", Integer.toBinaryString(b >>> n)).replace(' ', '0'));
    }
}
