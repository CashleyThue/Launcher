import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Config config = Json.load();

        Scanner in = new Scanner(System.in);

        App app = FuzzySearch.check(args[0], AppScanner.getApps(config));

        if (app == null) {
            System.out.println("No match found.");
            in.close();
            return;
        }

        System.out.println("Found: " + app.name);
        System.out.print("Execute? y/n: ");
        if (in.next().equals("y")) {
            new ProcessBuilder(app.exec.split(" ")).start();
        }
        in.close();
    }
}