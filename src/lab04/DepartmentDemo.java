package lab04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Співробітник. Окремий клас, спадкування не потрібне.
 */
class Employee {

    private String fullName;
    private int hireYear;
    private String type;

    public Employee() {
        this("Невідомо", 2000, "штатний");
    }

    public Employee(String fullName, int hireYear, String type) {
        this.fullName = fullName;
        this.hireYear = hireYear;
        this.type = type;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getHireYear() {
        return hireYear;
    }

    public void setHireYear(int hireYear) {
        this.hireYear = hireYear;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employee other = (Employee) o;
        return hireYear == other.hireYear
                && Objects.equals(fullName, other.fullName)
                && Objects.equals(type, other.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, hireYear, type);
    }

    @Override
    public String toString() {
        return fullName + " (" + type + ", з " + hireYear + " р.)";
    }
}

/**
 * Відділ: має посади й керує ними.
 */
class Department {

    private String name;
    private ArrayList<Position> positions = new ArrayList<Position>();

    public Department() {
        this("Без назви");
    }

    public Department(String name) {
        this.name = name;
    }

    /**
     * Внутрішній клас: посада відділу з історією співробітників.
     * Кожна посада прив'язана до конкретного відділу.
     */
    public class Position {

        private String title;
        private String type;
        private ArrayList<Employee> employees = new ArrayList<Employee>();

        public Position(String title, String type) {
            this.title = title;
            this.type = type;
            // доступ до списку зовнішнього класу: посада сама додається у свій відділ
            positions.add(this);
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public ArrayList<Employee> getEmployees() {
            return employees;
        }

        /**
         * Додає співробітника в історію посади.
         */
        public void addEmployee(Employee employee) {
            employees.add(employee);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Position other = (Position) o;
            return Objects.equals(title, other.title)
                    && Objects.equals(type, other.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, type);
        }

        @Override
        public String toString() {
            return title + " [" + type + "], співробітників за весь час: "
                    + employees.size();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Position> getPositions() {
        return positions;
    }

    /**
     * Шукає посаду за назвою. Якщо не знайдено, повертає null.
     */
    public Position findPosition(String title) {
        for (Position position : positions) {
            if (position.getTitle().equalsIgnoreCase(title)) {
                return position;
            }
        }
        return null;
    }

    /**
     * Створює масив посад із заготовленими даними.
     * Посади автоматично потрапляють і в список відділу.
     */
    public Position[] createPositions() {
        Position[] result = new Position[3];
        result[0] = new Position("Начальник", "керівна");
        result[1] = new Position("Програміст", "технічна");
        result[2] = new Position("Тестувальник", "технічна");

        result[0].addEmployee(new Employee("Петренко Іван", 2005, "штатний"));
        result[0].addEmployee(new Employee("Коваль Ольга", 2015, "штатний"));
        result[1].addEmployee(new Employee("Шевчук Андрій", 2012, "штатний"));
        result[1].addEmployee(new Employee("Мельник Оксана", 2020, "сумісник"));
        result[1].addEmployee(new Employee("Бондар Максим", 2023, "штатний"));
        result[2].addEmployee(new Employee("Ткачук Марія", 2018, "штатний"));
        return result;
    }

    /**
     * Критерій вибору: співробітники всіх посад, прийняті не раніше року year.
     */
    public ArrayList<Employee> selectByHireYear(int year) {
        ArrayList<Employee> result = new ArrayList<Employee>();
        for (Position position : positions) {
            for (Employee employee : position.getEmployees()) {
                if (employee.getHireYear() >= year) {
                    result.add(employee);
                }
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Department other = (Department) o;
        return Objects.equals(name, other.name)
                && Objects.equals(positions, other.positions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, positions);
    }

    @Override
    public String toString() {
        return "Відділ \"" + name + "\", посад: " + positions.size();
    }
}

public class DepartmentDemo {

    public static void main(String[] args) throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        Department department = new Department("Розробка");
        Department.Position[] positions = department.createPositions();

        System.out.println(department);
        System.out.println("\n=== Посади та історія співробітників ===");
        for (Department.Position position : positions) {
            System.out.println(position);
            for (Employee employee : position.getEmployees()) {
                System.out.println("    " + employee);
            }
        }

        System.out.print("\nПоказати співробітників, прийнятих не раніше року: ");
        int year = Integer.parseInt(reader.readLine().trim());

        System.out.println("=== Результат вибору ===");
        ArrayList<Employee> selected = department.selectByHireYear(year);
        if (selected.isEmpty()) {
            System.out.println("Нічого не знайдено");
        }
        for (Employee employee : selected) {
            System.out.println(employee);
        }
    }
}
