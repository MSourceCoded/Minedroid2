package sourcecoded.minedroid;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = Minedroid.MODID, version = Minedroid.VERSION)
public class Minedroid {

	public static final String MODID = "minedroid";
	public static final String VERSION = "2.0.0";
	
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
