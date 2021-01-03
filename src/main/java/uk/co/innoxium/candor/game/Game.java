package uk.co.innoxium.candor.game;

import com.github.f4b6a3.uuid.UuidCreator;
import com.google.gson.JsonObject;
import uk.co.innoxium.candor.module.ModuleSelector;
import uk.co.innoxium.candor.module.RunConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * An object to represent a "game".
 */
public final class Game {

    // The path to the executable of the game
    private final String gameExe;
    // The path to the game's mods folder
    private final String modsFolder;
    // The module that game should use, not currently used.
    private final String moduleClass;
    // A list of Run Configs
    public final List<RunConfig> runConfigs = new ArrayList<>();

    /**
     * Creates a game instance
     * @param gameExe - The executable of the game
     * @param modsFolder - The mods folder for the game
     * @param moduleClass - The module class
     */
    public Game(String gameExe, String modsFolder, String moduleClass) {

        // set the objects fields
        this.gameExe = gameExe;
        this.modsFolder = modsFolder;
        this.moduleClass = moduleClass;
    }

    /**
     * @return - A String of the game executable
     */
    public String getGameExe() {

        return gameExe;
    }

    /**
     * @return - A strong of the mods folder.
     */
    public String getModsFolder() {

        return modsFolder;
    }

    /**
     * @return - A string of the module class
     */
    public String getModuleClass() {

        return moduleClass;
    }

    /**
     * Converts the object to a json object
     * @return - A JSON Object representing the game.
     */
    public JsonObject toJson() {

        var ret = new JsonObject();

        ret.addProperty("uuid", String.valueOf(getUUID()));
        ret.addProperty("gameExe", gameExe);
        ret.addProperty("modsFolder", modsFolder);
        ret.addProperty("moduleClass", moduleClass);

        return ret;
    }

    // Will only return true UUID's match.
    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        var game = (Game) o;
        return getUUID().equals(game.getUUID());
    }

    // returns a hashcode to match the equals, here in case the games are even used in a hashmap
    @Override
    public int hashCode() {

        return Objects.hash(getUUID());
    }

    /**
     * @return - A string representation of the game.
     */
    @Override
    public String toString() {

        try {

            return ModuleSelector.getModuleForGame(this).getReadableGameName();
        } catch(NullPointerException e) {

            var gameFile = new File(gameExe);
            return gameFile.getName().substring(0, gameFile.getName().indexOf("."));
        }
    }

    /**
     * @return - The UUID of this game.
     */
    public UUID getUUID() {

        return UuidCreator.getNameBasedSha1(gameExe);
    }

    // A toString instance used for debugging, will stay for the time being.
    //    @Override
//    public String toString() {
//        return "Game{" +
//                "gameExe='" + gameExe + '\'' +
//                ", modsFolder='" + modsFolder + '\'' +
//                ", moduleClass='" + moduleClass + '\'' +
//                '}';
//    }
}
