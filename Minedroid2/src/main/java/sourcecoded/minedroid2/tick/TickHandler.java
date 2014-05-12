package sourcecoded.minedroid2.tick;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import sourcecoded.mdcomms.socket.SourceCommsServer;
import sourcecoded.minedroid2.util.MetaUtils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TickHandler {

	int tick;
	private Minecraft mc = Minecraft.getMinecraft();
	
	public MetaUtils meta = new MetaUtils();
	
	private static TickHandler instance;
	private TickHandler(){}
	
	public static TickHandler instance() {
		if (instance == null)
			instance = new TickHandler();
		return instance;
	}
	
	@SubscribeEvent 
	@SideOnly(Side.CLIENT)
	public void onClientTick(TickEvent.ClientTickEvent e) {
		tick++;
		ItemStack targetStack = null;
		if (tick == 20) {
			//Dispatch
			if (SourceCommsServer.instance().isConnected()) {
				World world = mc.theWorld;
				EntityPlayer player = mc.thePlayer;
				
				if (world != null && player != null) {
					RayTracing.instance().fire();
					MovingObjectPosition targ = RayTracing.instance().getTarget();
					
					if (targ != null && targ.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
						targetStack = RayTracing.instance().getTargetStack();
						
					}
					//Dispatch a packet
					meta.handleText(targetStack, world, player, targ);
				}
			}
			tick = 0;
		}
	}
	
}
