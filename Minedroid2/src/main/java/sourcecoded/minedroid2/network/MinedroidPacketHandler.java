package sourcecoded.minedroid2.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.EnumMap;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.base.Charsets;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

public enum MinedroidPacketHandler {

	INSTANCE;

	public EnumMap<Side, FMLEmbeddedChannel> channels;

	private MinedroidPacketHandler() {
		this.channels = NetworkRegistry.INSTANCE.newChannel("SC|MD", new MinedroidCodec());
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT){
            addClientHandlers();
        	addServerHandlers();
        } else {
        	addServerHandlers();
        }
	}

	private void addClientHandlers() {
		FMLEmbeddedChannel channel = this.channels.get(Side.CLIENT);
		String codec = channel.findChannelHandlerNameForType(MinedroidCodec.class);

		channel.pipeline().addAfter(codec, "TENBTData", new PktMC0x01TENBTData());
	}

	private void addServerHandlers() {
		FMLEmbeddedChannel channel = this.channels.get(Side.SERVER);
		String codec = channel.findChannelHandlerNameForType(MinedroidCodec.class);
		
		channel.pipeline().addAfter(codec, "TERequest", new PktMC0x00TERequest());
	}

		private class MinedroidCodec extends FMLIndexedMessageToMessageCodec<IPacket> {
	
			public MinedroidCodec() {
				addDiscriminator(0, PktMC0x00TERequest.class);
				addDiscriminator(1, PktMC0x01TENBTData.class);
			}
	
			@Override
			public void encodeInto(ChannelHandlerContext ctx, IPacket msg,
					ByteBuf target) throws Exception {
				msg.encodeInto(ctx, msg, target);
			}
	
			@Override
			public void decodeInto(ChannelHandlerContext ctx, ByteBuf dat,
					IPacket msg) {
				msg.decodeInto(ctx, dat, msg);
			} 
			
		}
		
	public void sendTo(IPacket message, EntityPlayerMP player){
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
		channels.get(Side.SERVER).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);    	
	}

	public void sendToServer(IPacket message){
		channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		channels.get(Side.CLIENT).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}

	public void writeNBT(ByteBuf target, NBTTagCompound tag) throws IOException{
        if (tag == null)
        	target.writeShort(-1);
        else{
            byte[] abyte = CompressedStreamTools.compress(tag);
            target.writeShort((short)abyte.length);
            target.writeBytes(abyte);
        }
    }

    public NBTTagCompound readNBT(ByteBuf dat) throws IOException
    {
        short short1 = dat.readShort();

        if (short1 < 0)
            return null;
        else{
            byte[] abyte = new byte[short1];
            dat.readBytes(abyte);
            return CompressedStreamTools.decompress(abyte);
        }
    }
    
    public void writeString(ByteBuf buffer, String data) throws IOException
    {
        byte[] abyte = data.getBytes(Charsets.UTF_8);
       	buffer.writeShort(abyte.length);
       	buffer.writeBytes(abyte);
    }
    
    public String readString(ByteBuf buffer) throws IOException
    {
        int j = buffer.readShort();
        String s = new String(buffer.readBytes(j).array(), Charsets.UTF_8);
        return s;
    } 
    
    public void writeInt(ByteBuf buffer, int data) throws IOException
    {
        buffer.writeInt(data);
    }
    
    public int readInt(ByteBuf buffer) throws IOException
    {
        return buffer.readInt();
    }

}
