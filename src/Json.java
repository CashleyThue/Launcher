import com.google.gson.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

public class Json {

    private static final Path CONFIG_DIR =
            Path.of(
                    System.getProperty("user.home"),
                    ".config",
                    "cashlaunch"
            );

    private static final Path CONFIG_FILE =
            CONFIG_DIR.resolve("config.json");

    private static final Gson GSON =
            new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

    public static Config load() throws IOException {

        if (!Files.exists(CONFIG_FILE)) {

            Files.createDirectories(CONFIG_DIR);

            Config defaults = new Config();

            Files.writeString(
                    CONFIG_FILE,
                    GSON.toJson(defaults)
            );

            return defaults;
        }

        try (Reader reader =
                     Files.newBufferedReader(CONFIG_FILE)) {

            Config config =
                    GSON.fromJson(reader, Config.class);

            if (config == null) {
                return new Config();
            }

            if (config.extraApplicationDirs == null) {
                config.extraApplicationDirs =
                        new ArrayList<>();
            }

            return config;
        }
    }
}