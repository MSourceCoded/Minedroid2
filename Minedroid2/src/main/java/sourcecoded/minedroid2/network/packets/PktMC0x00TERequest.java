package sourcecoded.minedroid2.network.packets;

import sourcecoded.minedroid2.network.IPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.DimensionManager;

public class PktMC0x00TERequest extends SimpleChannelInboundHandler<PktMC0x00TERequest> implements IPacket{

	public int dim, posX, posY, posZ;
	
	public PktMC0x00TERequest() {}
	public PktMC0x00TERequest(TileEntity te) {
		this.dim = te.getWorldObj().provider.dimensionId;
		this.posX = te.xCoord;
		this.posY = te.yCoord;
		this.posZ = te.zCoord;
	}
	
	@Override	
	public void encodeInto(ChannelHandlerContext ctx, IPacket msg, ByteBuf target) throws Exception {
		target.writeInt(dim);
		target.writeInt(posX);
		target.writeInt(posY);
		target.writeInt(posZ);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf dat, IPacket rawmsg) {
		try {
			PktMC0x00TERequest msg = (PktMC0x00TERequest)rawmsg;
			msg.dim  = dat.readInt();
			msg.posX = dat.readInt();
			msg.posY = dat.readInt();
			msg.posZ = dat.readInt();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, PktMC0x00TERequest msg) throws Exception {
		//MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        TileEntity      tentity = DimensionManager.getWorld(msg.dim).getTileEntity(msg.posX, msg.posY, msg.posZ);
        if (tentity != null){
        	try{
        		NBTTagCompound tag = new NBTTagCompound();
        		tentity.writeToNBT(tag);
        		ctx.writeAndFlush(new PktMC0x01TENBTData(tag)).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        }
	}

}
