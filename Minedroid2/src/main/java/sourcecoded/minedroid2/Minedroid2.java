package sourcecoded.minedroid2;

import net.minecraftforge.common.MinecraftForge;
import sourcecoded.mdcomms.SourceComms;
import sourcecoded.mdcomms.eventsystem.EventBus;
import sourcecoded.mdcomms.eventsystem.SourceCommsEvent;
import sourcecoded.mdcomms.eventsystem.event.EventPacketHandled;
import sourcecoded.mdcomms.eventsystem.event.EventServerReady;
import sourcecoded.mdcomms.socket.SourceCommsServer;
import sourcecoded.minedroid2.tick.TickHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = Minedroid2.MODID, version = Minedroid2.VERSION)
public class Minedroid2 {

	public static final String MODID = "minedroid";
	public static final String VERSION = "2.0.0";
	
	@SidedProxy(clientSide="sourcecoded.minedroid2.client.ClientProxy", serverSide="sourcecoded.minedroid2.CommonProxy")
    public static CommonProxy proxy;
	
	@EventHandler
    public void preinit(FMLPreInitializationEvent event){
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event){
    	if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
    		FMLCommonHandler.instance().bus().register(TickHandler.instance());    		
    	}
    	
    	SourceComms.init();
    	EventBus.Registry.register(Minedroid2.class);
    	
    	SourceCommsServer.instance().setData(1337);
    	SourceCommsServer.instance().open();
    	
    	MinecraftForge.EVENT_BUS.register(this);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) { 
    }
    
    @SourceCommsEvent
    public void onStart(EventServerReady e) {
    	SourceCommsServer.instance().setListeningState(true);
    	SourceCommsServer.instance().listen();
    }
    
    @SourceCommsEvent
    public void onpkt(EventPacketHandled e) {
    }
    
}
