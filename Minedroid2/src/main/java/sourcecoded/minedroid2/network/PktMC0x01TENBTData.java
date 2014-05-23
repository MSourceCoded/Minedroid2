package sourcecoded.minedroid2.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.nbt.NBTTagCompound;
import sourcecoded.minedroid2.tick.TickHandler;

public class PktMC0x01TENBTData extends SimpleChannelInboundHandler<PktMC0x01TENBTData> implements IPacket{

	NBTTagCompound tags;
	
	public PktMC0x01TENBTData() {
	}
	
	public PktMC0x01TENBTData(NBTTagCompound keys) {
		tags = keys;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, IPacket msg, ByteBuf target) throws Exception {
//		byte[] theTags = CompressedStreamTools.compress(tags);
//		target.writeShort(theTags.length);
//		target.writeBytes(theTags);
		MinedroidPacketHandler.INSTANCE.writeNBT(target, tags);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf dat, IPacket msg) {
		try {
//			short theLength = dat.readShort();
//			byte[] theTags = dat.readBytes(theLength).array();
//			tags = CompressedStreamTools.decompress(theTags);
			tags = MinedroidPacketHandler.INSTANCE.readNBT(dat);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, PktMC0x01TENBTData msg) throws Exception {
		//Dispatch an NBT Packet to the 'droid
		TickHandler.instance().meta.fireTEPacket(msg.tags);
	}

}
