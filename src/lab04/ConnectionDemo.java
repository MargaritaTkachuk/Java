package lab04;

class ConnectionManager {

    // Вкладений клас: конструктор приватний, тому new працює лише в ConnectionManager
    static class Connection {
        private int id;

        private Connection(int id) {
            this.id = id;
        }

        void doSomething() {
            System.out.println("Connection #" + id + " працює");
        }
    }

    private static final int SIZE = 3;
    private static Connection[] connections = new Connection[SIZE];
    private static int index = 0;   // номер наступного вільного об'єкта

    // Виконується один раз, коли клас завантажується
    static {
        for (int i = 0; i < SIZE; i++) {
            connections[i] = new Connection(i + 1);
        }
    }

    // Забороняємо створювати сам ConnectionManager
    private ConnectionManager() {
    }

    // Єдиний спосіб отримати Connection
    static Connection getConnection() {
        if (index < SIZE) {
            Connection c = connections[index];
            index++;
            return c;
        }
        return null;   // об'єкти закінчилися
    }
}

public class ConnectionDemo {
    public static void main(String[] args) {

        // ConnectionManager.Connection c = new ConnectionManager.Connection(9);
        // ПОМИЛКА КОМПІЛЯЦІЇ: Connection(int) has private access

        for (int i = 1; i <= 5; i++) {
            System.out.println("Запит " + i);
            ConnectionManager.Connection c = ConnectionManager.getConnection();

            if (c == null) {
                System.out.println("Об'єкти закінчилися, завершуємо роботу");
                break;
            }
            c.doSomething();
        }
    }
}
