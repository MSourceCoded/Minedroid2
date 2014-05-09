package sourcecoded.minedroid2.client;
 
import net.minecraft.entity.player.EntityPlayer;
import sourcecoded.minedroid2.CommonProxy;
import cpw.mods.fml.client.FMLClientHandler;
 
public class ClientProxy extends CommonProxy {
	
        @Override
        public void registerProxy() {
        }
        
        
        @Override
    	public EntityPlayer getClientPlayer() {
    		return FMLClientHandler.instance().getClientPlayerEntity();
    	}
}