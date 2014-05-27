package sourcecoded.minedroid2.util;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import sourcecoded.mdcomms.network.packets.Pkt1x00Player;
import sourcecoded.mdcomms.network.packets.Pkt1x02NBTMap;
import sourcecoded.mdcomms.network.packets.Pkt1x03NBTCancel;
import sourcecoded.mdcomms.socket.SourceCommsServer;
import sourcecoded.minedroid2.network.MinedroidPacketHandler;
import sourcecoded.minedroid2.network.packets.PktMC0x00TERequest;

public class MetaUtils {

	ItemStack is;
	World world;
	EntityPlayer thePlayer;
	MovingObjectPosition mop;
	boolean wasTile;
	boolean isTile;
	
	public void handleText(ItemStack is, World world, EntityPlayer player, MovingObjectPosition mop) {
		//Block block = world.getBlock(mop.blockX, mop.blockY, mop.blockZ);
		TileEntity tile = null;
		if (mop != null)
			tile = world.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
		
		this.is = is;
		this.world = world;
		this.thePlayer = player;
		this.mop = mop;
		
		String targ;
		
		
		
		if (tile != null) {
			//Sends a packet to the server to retrieve the NBT tag info
			MinedroidPacketHandler.INSTANCE.sendToServer(new PktMC0x00TERequest(tile));			
			isTile = true;
		} else isTile = false;
		
		if (isTile) {
			wasTile = true;
		} if (!isTile && wasTile) {
			SourceCommsServer.instance().sendToClient(new Pkt1x03NBTCancel());
			wasTile = false;
		}
		
		if (is == null) {
			targ = "Null";
		} else {
			try {
				targ = is.getDisplayName();
			} catch (Exception e) {
				targ = "Undefined";
			}
		}
		
		Pkt1x00Player packet = new Pkt1x00Player((int)player.posX, (int)player.posY, (int)player.posZ, targ);
		SourceCommsServer.instance().sendToClient(packet);
	}
	
	@SuppressWarnings("rawtypes")
	public void fireTEPacket(NBTTagCompound tags) {
		//NBT Packet
		HashMap map = NBTUtils.getCompoundAsMap(tags);
		Pkt1x02NBTMap packet = new Pkt1x02NBTMap(map);
		SourceCommsServer.instance().sendToClient(packet);
	}
	
}
