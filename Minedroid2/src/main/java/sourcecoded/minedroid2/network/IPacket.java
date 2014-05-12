package sourcecoded.minedroid2.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface IPacket {
	public void encodeInto(ChannelHandlerContext ctx, IPacket msg, ByteBuf target) throws Exception;
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf dat, IPacket msg);
}
