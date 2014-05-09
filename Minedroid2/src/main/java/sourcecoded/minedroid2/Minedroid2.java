package sourcecoded.minedroid2;

import net.minecraftforge.common.MinecraftForge;
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
    		//FMLCommonHandler.instance().bus().register(TickHandler.instance());    		
    	}
    	
    	
    	MinecraftForge.EVENT_BUS.register(this);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) { 
    }
}
