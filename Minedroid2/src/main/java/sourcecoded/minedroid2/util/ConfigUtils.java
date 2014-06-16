package sourcecoded.minedroid2.util;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConfigUtils {

    static Configuration config;

    /*
     * CATEGORIES
     */
    public static String SERVER_DETAILS = "Server Details";

    /*
     * FIELDS
     */
    public static String PORT_NUMBER = "Port Number";

    /*
     *  PROPERTIES
     */
    public static Property PORT_PROPERTY;

    public static void init(File configFile) {
        config = new Configuration(configFile);

        doPrep();
    }

    public static void load() {
        config.load();
    }

    public static void save() {
        config.save();
    }

    public static Configuration getConfig() {
        return config;
    }

    static void doPrep() {
        PORT_PROPERTY = config.get(SERVER_DETAILS, PORT_NUMBER, 1337);
    }

}
