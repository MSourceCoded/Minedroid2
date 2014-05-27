package sourcecoded.minedroid2.network.packets;

import sourcecoded.minedroid2.commandsystem.MinedroidCommandHandler;
import sourcecoded.minedroid2.network.IPacket;
import sourcecoded.minedroid2.network.MinedroidPacketHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PktMC1x00CommandToClient extends SimpleChannelInboundHandler<PktMC1x00CommandToClient> implements IPacket{
	
	String[] args;
	String command;
	
	public PktMC1x00CommandToClient() {
	}
	
	public PktMC1x00CommandToClient(String command, String[] args) {
		this.command = command;
		this.args = args;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, IPacket msg, ByteBuf target) throws Exception {
		MinedroidPacketHandler.INSTANCE.writeString(target, command);
		MinedroidPacketHandler.INSTANCE.writeStringArray(target, args);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf dat, IPacket msg) {
		try {
			command = MinedroidPacketHandler.INSTANCE.readString(dat);
			args = MinedroidPacketHandler.INSTANCE.readStringArray(dat);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, PktMC1x00CommandToClient msg) throws Exception {
		MinedroidCommandHandler.triggerClient(msg.command, msg.args);
	}

}
