package sourcecoded.minedroid2.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import sourcecoded.mdcomms.network.packets.Pkt1x06WorldSeed;
import sourcecoded.mdcomms.socket.SourceCommsServer;
import sourcecoded.minedroid2.util.CacheUtils;

public class WorldDataHandler {

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        World wrld = event.world;

        if (!wrld.isRemote) {
            long seed = wrld.getSeed();
            CacheUtils.lastWorldSeed = seed;
            CacheUtils.seedSendCache.add(seed);
        }
    }

    @SubscribeEvent
    public void onWorldQuit(WorldEvent.Unload event) {
        World wrld = event.world;

        if (!wrld.isRemote) {
            CacheUtils.lastWorldSeed = 0;
            CacheUtils.seedSendCache.clear();

            SourceCommsServer.instance().sendToClient(new Pkt1x06WorldSeed(0));
        }
    }

}
