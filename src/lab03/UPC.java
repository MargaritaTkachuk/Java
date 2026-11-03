package lab03;

public class UPC {
    public static void main(String[] args) {
        if (args.length == 0 || !args[0].matches("[0-9]{1,11}")) {
            System.out.println("Вкажіть 11-значне ціле число як аргумент командного рядка.");
            return;
        }

        long x = Long.parseLong(args[0]);
        long rest = x;
        int sum = 0;

        for (int i = 2; i <= 12; i++) {
            int d = (int) (rest % 10);
            rest /= 10;
            if (i % 2 == 0) {
                sum += 3 * d;
            } else {
                sum += d;
            }
        }

        int d1 = (10 - sum % 10) % 10;

        String code = String.format("%011d", x) + d1;
        System.out.println("Контрольна цифра: " + d1);
        System.out.println("UPC: " + code);
        System.out.println("UPC: " + code.charAt(0) + "-" + code.substring(1, 6) + "-"
                + code.substring(6, 11) + "-" + code.charAt(11));
    }
}
