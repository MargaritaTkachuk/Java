package lab03;

public class StringArray {
    public static void main(String[] args) {
        String[] fruits = {"яблуко", "груша", "слива", "вишня", "персик"};

        for (int i = 0; i < fruits.length; i++) {
            System.out.print(fruits[i]);
            if (i < fruits.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();

        for (int i = 0; i < fruits.length; i++) {
            System.out.println("fruits[" + i + "] = " + fruits[i]);
        }
    }
}
