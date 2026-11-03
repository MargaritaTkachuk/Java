package lab04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Видання: спільна поведінка для всіх видань.
 */
interface Publication {

    String getTitle();

    int getYear();

    int getPages();

    String getType();

    String getInfo();
}

/**
 * Те, що можна шукати за ключовим словом.
 */
interface Searchable {

    boolean matches(String keyword);
}

/**
 * Абстрактне видання: спільні поля й частина методів.
 */
abstract class AbstractPublication implements Publication {

    private final String title;
    private final int year;
    private final int pages;

    protected AbstractPublication(String title, int year, int pages) {
        this.title = title;
        this.year = year;
        this.pages = pages;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public int getPages() {
        return pages;
    }

    // тип видання визначає кожен підклас сам
    @Override
    public abstract String getType();

    @Override
    public String getInfo() {
        return getType() + ": \"" + title + "\", " + year + " р., "
                + pages + " с.";
    }

    @Override
    public String toString() {
        return getInfo();
    }
}

/**
 * Книга.
 */
class Book extends AbstractPublication {

    private final String author;

    public Book(String title, String author, int year, int pages) {
        super(title, year, pages);
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String getType() {
        return "Книга";
    }

    @Override
    public String getInfo() {
        return super.getInfo() + ", автор: " + author;
    }
}

/**
 * Довідник.
 */
class ReferenceBook extends Book implements Searchable {

    private final String subject;

    public ReferenceBook(String title, String author, int year, int pages,
                         String subject) {
        super(title, author, year, pages);
        this.subject = subject;
    }

    @Override
    public String getType() {
        return "Довідник";
    }

    @Override
    public String getInfo() {
        return super.getInfo() + ", галузь: " + subject;
    }

    @Override
    public boolean matches(String keyword) {
        String key = keyword.toLowerCase();
        return subject.toLowerCase().contains(key)
                || getTitle().toLowerCase().contains(key);
    }
}

/**
 * Енциклопедія.
 */
class Encyclopedia extends Book implements Searchable {

    private final int volumes;
    private final String field;

    public Encyclopedia(String title, String author, int year, int pages,
                        int volumes, String field) {
        super(title, author, year, pages);
        this.volumes = volumes;
        this.field = field;
    }

    @Override
    public String getType() {
        return "Енциклопедія";
    }

    @Override
    public String getInfo() {
        return super.getInfo() + ", томів: " + volumes
                + ", галузь: " + field;
    }

    @Override
    public boolean matches(String keyword) {
        String key = keyword.toLowerCase();
        return field.toLowerCase().contains(key)
                || getTitle().toLowerCase().contains(key);
    }
}

public class PublicationDemo {

    public static void main(String[] args) throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        // Поліморфізм: масив інтерфейсного типу містить різні об'єкти
        Publication[] publications = new Publication[3];
        publications[0] = new Book("Кобзар", "Тарас Шевченко", 2015, 320);
        publications[1] = new ReferenceBook("Довідник з математики",
                "Іван Петренко", 2018, 450, "математика");
        publications[2] = new Encyclopedia("Універсальна енциклопедія",
                "Редакція", 2010, 1200, 10, "універсальна");

        System.out.println("=== Усі видання ===");
        int totalPages = 0;
        for (Publication p : publications) {
            System.out.println(p.getInfo());   // версія залежить від типу об'єкта
            totalPages += p.getPages();
        }
        System.out.println("Разом сторінок: " + totalPages);

        System.out.print("\nВведіть ключове слово для пошуку: ");
        String keyword = reader.readLine().trim();

        System.out.println("=== Результат пошуку ===");
        boolean found = false;
        for (Publication p : publications) {
            if (p instanceof Searchable && ((Searchable) p).matches(keyword)) {
                System.out.println(p.getInfo());
                found = true;
            }
        }
        if (!found) {
            System.out.println("Нічого не знайдено");
        }
    }
}
