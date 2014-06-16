package sourcecoded.minedroid2;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Property;
import sourcecoded.mdcomms.SourceComms;
import sourcecoded.mdcomms.eventsystem.EventBus;
import sourcecoded.mdcomms.eventsystem.SourceCommsEvent;
import sourcecoded.mdcomms.eventsystem.event.EventPacketHandled;
import sourcecoded.mdcomms.eventsystem.event.EventServerClosed;
import sourcecoded.mdcomms.eventsystem.event.EventServerReady;
import sourcecoded.mdcomms.network.packets.ISourceCommsPacket;
import sourcecoded.mdcomms.network.packets.Pkt0x01PingReply;
import sourcecoded.mdcomms.network.packets.Pkt1x05ChatMessageSend;
import sourcecoded.mdcomms.socket.SourceCommsServer;
import sourcecoded.mdcomms.util.PacketUtils;
import sourcecoded.minedroid2.events.MDEventHandler;
import sourcecoded.minedroid2.network.MinedroidPacketHandler;
import sourcecoded.minedroid2.tick.TickHandler;
import sourcecoded.minedroid2.util.CacheUtils;
import sourcecoded.minedroid2.util.ChatUtils;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;
import sourcecoded.minedroid2.util.ConfigUtils;

@Mod(modid = Minedroid2.MODID, version = Minedroid2.VERSION)
public class Minedroid2 {

	public static final String MODID = "minedroid";
	public static final String VERSION = "1.0.0";
	
	@SidedProxy(clientSide="sourcecoded.minedroid2.client.ClientProxy", serverSide="sourcecoded.minedroid2.CommonProxy")
    public static CommonProxy proxy;
	
	@EventHandler
    public void preInit(FMLPreInitializationEvent event){
        ConfigUtils.init(event.getSuggestedConfigurationFile());
        ConfigUtils.load();

        ConfigUtils.save();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event){
    	if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
    		FMLCommonHandler.instance().bus().register(TickHandler.instance());    		
    	}
    	
    	MinedroidPacketHandler.INSTANCE.init();
    	
    	SourceComms.init();
    	EventBus.Registry.register(Minedroid2.class);
    	
    	SourceCommsServer.instance().setData(1337);

    	MinecraftForge.EVENT_BUS.register(this);
    	MinecraftForge.EVENT_BUS.register(new MDEventHandler());
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) { 
    }
    
    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
    }
    
    @SourceCommsEvent
    public void onStart(EventServerReady e) {
    	SourceCommsServer.instance().setListeningState(true);
    	SourceCommsServer.instance().listen();
    }
    
    @SourceCommsEvent
    public void onEnd(EventServerClosed e) {
        CacheUtils.errorMessage = e.getReason();
    }
    
    @SourceCommsEvent
    public void onPkt(EventPacketHandled e) {
    	ISourceCommsPacket pkt = e.getPacket();
    	
    	if (PacketUtils.compareClass(pkt, Pkt0x01PingReply.class)) {
    		Pkt0x01PingReply newPkt = (Pkt0x01PingReply)pkt;
            CacheUtils.pingCache = (int) newPkt.diffMS;
    	} else if (PacketUtils.compareClass(pkt, Pkt1x05ChatMessageSend.class)) {
    		Pkt1x05ChatMessageSend newPkt = (Pkt1x05ChatMessageSend)pkt;
    		EntityClientPlayerMP player = (EntityClientPlayerMP) proxy.getClientPlayer();
    		if (player != null)
    			player.sendChatMessage(newPkt.message);
    	}
    }
    
}
