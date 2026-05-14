import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);

        App app = FuzzySearch.check(args[0], getApps());

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

    public static ArrayList<App> getApps() throws IOException {
        Path[] dirs = {Path.of("/usr/share/applications"),
        Path.of("/var/lib/flatpak/exports/share/applications"),
        Path.of("/home/Camille/.local/share/applications/")};

        ArrayList<App> apps = new ArrayList<>();

        for (Path path : dirs) {
            try (DirectoryStream<Path> stream =
                     Files.newDirectoryStream(path, "*.desktop")) {

                for (Path file : stream) {

                    List<String> lines = Files.readAllLines(file);

                    boolean hidden = false;
                    boolean application = false;

                    String name = null;
                    String exec = null;

                    for (String line : lines) {

                        line = line.trim();

                        if (line.equalsIgnoreCase("NoDisplay=true") ||
                            line.equalsIgnoreCase("Hidden=true")) {

                            hidden = true;
                            break;
                        }

                        if (line.equals("Type=Application")) {
                            application = true;
                        }

                        if (name == null &&
                        (line.startsWith("Name=") ||
                            line.startsWith("Name["))) {

                            int idx = line.indexOf('=');

                            if (idx != -1) {
                                name = line.substring(idx + 1).trim();
                            }
                        }

                        if (line.startsWith("Exec=")) {

                            exec = line.substring(5);

                            // remove desktop placeholders
                            exec = exec.replaceAll("%[fFuUdDnNickvm]", "")
                                    .trim();
                        }
                    }

                    if (!hidden &&
                        application &&
                        name != null &&
                        exec != null) {

                        App app = new App();

                        app.name = name;
                        app.exec = exec;

                        apps.add(app);
                    }
                }
            }
        }

        App discord = new App();

        discord.name = "Discord";
        discord.exec = "discord";

        apps.add(discord);

        return apps;
    }
}