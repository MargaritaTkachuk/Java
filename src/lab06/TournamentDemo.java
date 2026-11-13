package lab06;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Запис про матч: номери команд і кількість забитих м'ячів.
 */
class T_Match {

    int n1;
    int n2;
    int b1;
    int b2;

    public T_Match(int n1, int n2, int b1, int b2) {
        this.n1 = n1;
        this.n2 = n2;
        this.b1 = b1;
        this.b2 = b2;
    }

    @Override
    public String toString() {
        return n1 + " - " + n2 + "  " + b1 + ":" + b2;
    }
}

/**
 * Турнір: файл з назвами команд і файл з результатами матчів.
 */
class Tournament {

    private final String teamsFile;
    private final String matchesFile;

    public Tournament(String teamsFile, String matchesFile) {
        this.teamsFile = teamsFile;
        this.matchesFile = matchesFile;
    }

    /**
     * Створює (перезаписує) файл команд: одна назва в рядку.
     */
    public void createTeamsFile(ArrayList<String> teams) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter(teamsFile));
        for (String team : teams) {
            out.println(team);
        }
        out.close();
    }

    /**
     * Створює порожній файл матчів.
     */
    public void createMatchesFile() throws IOException {
        DataOutputStream out =
                new DataOutputStream(new FileOutputStream(matchesFile));
        out.close();
    }

    /**
     * Дописує результат матчу в кінець файлу матчів.
     */
    public void addMatch(T_Match match) throws IOException {
        // true означає режим дописування в кінець файлу
        DataOutputStream out =
                new DataOutputStream(new FileOutputStream(matchesFile, true));
        out.writeInt(match.n1);
        out.writeInt(match.n2);
        out.writeInt(match.b1);
        out.writeInt(match.b2);
        out.close();
    }

    /**
     * Читає назви команд з файлу.
     */
    public ArrayList<String> readTeams() throws IOException {
        ArrayList<String> teams = new ArrayList<String>();
        BufferedReader in = new BufferedReader(new FileReader(teamsFile));
        String line = in.readLine();
        while (line != null) {
            teams.add(line);
            line = in.readLine();
        }
        in.close();
        return teams;
    }

    /**
     * Знаходить лідера: найбільше очок, а при рівності найкраща різниця м'ячів.
     * Повертає назву команди.
     */
    public String findLeader() throws IOException {
        ArrayList<String> teams = readTeams();
        int count = teams.size();

        int[] points = new int[count];
        int[] diff = new int[count];

        DataInputStream in =
                new DataInputStream(new FileInputStream(matchesFile));
        try {
            while (true) {
                int n1 = in.readInt() - 1;   // номери команд у файлі з 1
                int n2 = in.readInt() - 1;
                int b1 = in.readInt();
                int b2 = in.readInt();

                diff[n1] += b1 - b2;
                diff[n2] += b2 - b1;

                if (b1 > b2) {
                    points[n1] += 3;
                } else if (b1 < b2) {
                    points[n2] += 3;
                } else {
                    points[n1] += 1;
                    points[n2] += 1;
                }
            }
        } catch (EOFException e) {
            // кінець файлу: усі матчі прочитано
        }
        in.close();

        int best = 0;
        for (int i = 1; i < count; i++) {
            if (points[i] > points[best]
                    || (points[i] == points[best] && diff[i] > diff[best])) {
                best = i;
            }
        }

        return teams.get(best) + " (очок: " + points[best]
                + ", різниця м'ячів: " + diff[best] + ")";
    }
}

public class TournamentDemo {

    public static void main(String[] args) throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        Tournament tournament = new Tournament("teams.txt", "matches.bin");

        System.out.print("Скільки команд? ");
        int teamCount = Integer.parseInt(reader.readLine().trim());

        ArrayList<String> teams = new ArrayList<String>();
        for (int i = 1; i <= teamCount; i++) {
            System.out.print("Назва команди " + i + ": ");
            teams.add(reader.readLine().trim());
        }
        tournament.createTeamsFile(teams);
        tournament.createMatchesFile();

        System.out.print("Скільки матчів? ");
        int matchCount = Integer.parseInt(reader.readLine().trim());

        for (int i = 1; i <= matchCount; i++) {
            System.out.println("Матч " + i + ": n1 n2 b1 b2 (через пробіл)");
            String[] p = reader.readLine().trim().split("\\s+");
            tournament.addMatch(new T_Match(
                    Integer.parseInt(p[0]), Integer.parseInt(p[1]),
                    Integer.parseInt(p[2]), Integer.parseInt(p[3])));
        }

        System.out.println("\nЛідер: " + tournament.findLeader());
    }
}
