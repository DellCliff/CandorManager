package uk.co.innoxium.candor.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import uk.co.innoxium.candor.util.Logger;
import uk.co.innoxium.candor.util.Resources;
import uk.co.innoxium.cybernize.json.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class handles the games list logic, such as adding, removing.
 */
public final class GamesList {

    // Our games list.
    private static final List<Game> GAMES_LIST = new ArrayList<>();
    // The file to save the list to.
    private static final File GAMES_FILE = JsonUtil.getJsonFile(new File(Resources.CONFIG_PATH, "Games.json"), false);

    /**
     * Adds a game to the games list, only if the game is not already in it.
     * @param game - The game to add.
     */
    public static void addGame(Game game) {

        if (!GAMES_LIST.contains(game)) {

            GAMES_LIST.add(game);
        }
    }

    /**
     * Gets a game instance from a UUID
     * @param uuid - The UUID of the game to get.
     * @return - A game instance if the games list contains it, else null.
     */
    public static Game getGameFromUUID(UUID uuid) {

        return GAMES_LIST.stream()
            .filter(game -> game.getUUID().equals(uuid))
            .findFirst()
            .orElse(null);
    }

    /*
        Returns the games list object
     */
    public static List<Game> getGamesList() {

        return GAMES_LIST;
    }

    /**
     * Loads the games list from file, converts the JSON objects to Game objects and adds them to the games list.
     * @throws IOException - If there is an error loading the file.
     */
    public static void loadFromFile() throws IOException {

        // Get the contents fo the JSON file
        var contents = JsonUtil.getObjectFromPath(GAMES_FILE.toPath());
        // If the contents doesn't have a "games" array, add one, else convert to Game object, and add to list.
        if (!contents.has("games")) {

            // Add new JSONArray to the contents
            contents.add("games", new JsonArray());
            // Create a FileWriter and ask Gson to write to file.
            try (var writer = JsonUtil.getFileWriter(GAMES_FILE)) {
                JsonUtil.getGson().toJson(contents, writer);
            }
        } else {

            // Get the games array
            // For each object in the array, convert to Game object, and add to games list.
            JsonUtil.getArray(contents, "games").forEach(e -> {

                assert e instanceof JsonObject;

                GAMES_LIST.add(JsonUtil.getGson().fromJson((JsonObject)e, Game.class));
            });
        }
    }

    /**
     * Saves the list to file.
     * @throws IOException - If there is an error writing to file.
     */
    public static void writeToFile() throws IOException {

        Logger.info("Writing Games List to file");
        // Get the contents of the current file.
        var contents = JsonUtil.getObjectFromPath(GAMES_FILE.toPath());
        // Get the current "games" array of the file
        var gamesArray = JsonUtil.getArray(contents, "games");
        // For each game in the Games List, add to the array if not already there.
        GAMES_LIST.forEach(game -> {

            var obj = game.toJson();
            if (!gamesArray.contains(obj))
                gamesArray.add(obj);
        });
        // Create a file writer and ask Gson to write to file.
        try (var writer = JsonUtil.getFileWriter(GAMES_FILE)) {
            JsonUtil.getGson().toJson(contents, writer);
        }
    }

    private GamesList() {

    }
}
