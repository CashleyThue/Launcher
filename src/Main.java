import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
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

    public static HashSet<App> getApps() throws IOException {
        String xdg = System.getenv("XDG_DATA_DIRS");
        if (xdg == null) {
            System.out.println("No directories in $XDG_DATA_DIRS");
            return new HashSet<>();
        }

        String[] xdgDirs = xdg.split(":");
        HashSet<Path> dirs = new HashSet<>();

        for (String path : xdgDirs) {
            dirs.add(Path.of(path, "applications"));
        }

        dirs.add(
                Path.of(
                        System.getProperty("user.home"),
                        ".local",
                        "share",
                        "applications"
                )
        );

        HashSet<App> apps = new HashSet<>();

        for (Path path : dirs) {
            if (!Files.exists(path) || !Files.isDirectory(path)) {
                continue;
            }
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