package sourcecoded.minedroid2.util;

import net.minecraft.world.World;

import java.util.ArrayList;

public class CacheUtils {

    public static int pingCache = 0;

    public static long lastWorldSeed;
    public static ArrayList<Long> seedSendCache = new ArrayList<Long>();

    public static String errorMessage = null;
}
