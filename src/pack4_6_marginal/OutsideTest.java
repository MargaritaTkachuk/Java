package pack4_6_marginal;

import pack4_6.Base;

public class OutsideTest {
    public static void main(String[] args) {
        Base b = new Base();
        b.publicMethod(); // працює: public
        //b.protectedMethod(); - ПОМИЛКА КОМПІЛЯЦІЇ
    }
}
