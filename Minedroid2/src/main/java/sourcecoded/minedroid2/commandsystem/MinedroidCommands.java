package sourcecoded.minedroid2.commandsystem;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import sourcecoded.mdcomms.network.packets.Pkt0x00PingRequest;
import sourcecoded.mdcomms.socket.SourceCommsServer;
import sourcecoded.minedroid2.Minedroid2;

public class MinedroidCommands {

	public static class MDCmdPing implements MDCommand {

		@Override
		public void processServer(ICommandSender sender, String[] args) {
		}

		@Override
		public void processClient(String[] args) {
			SourceCommsServer.instance().sendToClient(new Pkt0x00PingRequest());
		}

		@Override
		public String usage() {
			return "/minedroid ping";
		}
		
	}
	
	public static class MDCmdServerOpen implements MDCommand {
		
		@Override
		public void processServer(ICommandSender sender, String[] args) {
		}

		@Override
		public void processClient(String[] args) {
			if (args[0] != null)
				SourceCommsServer.instance().setData(Integer.valueOf(args[0]));
			SourceCommsServer.instance().open();
		}

		@Override
		public String usage() {
			return "/minedroid open [port <default: 1337>]";
		}
		
	}
	
	public static class MDCmdServerKill implements MDCommand {

		@Override
		public void processServer(ICommandSender sender, String[] args) {
			
		}

		@Override
		public void processClient(String[] args) {
			SourceCommsServer.instance().stop();
		}

		@Override
		public String usage() {
			return "/minedroid kill";
		}
		
	}
	
	public static class MDCmdServerStatus implements MDCommand {

		@Override
		public void processServer(ICommandSender sender, String[] args) {
		}

		@Override
		public void processClient(String[] args) {
			boolean listen = SourceCommsServer.instance().isListening();
			boolean connect = SourceCommsServer.instance().isConnected();
			
			Minedroid2.proxy.getClientPlayer().addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "Listening for messages: " + EnumChatFormatting.AQUA + listen));
			Minedroid2.proxy.getClientPlayer().addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "Connected to client:  " + EnumChatFormatting.AQUA + connect));
		}

		@Override
		public String usage() {
			return "/minedroid status";
		}
		
	}
	
}
