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
			if (args != null && args.length > 0 && args[0] != null)
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
			
			Minedroid2.proxy.getClientPlayer().addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "Connected to client:  " + EnumChatFormatting.AQUA + connect));
			Minedroid2.proxy.getClientPlayer().addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "     -Listening for messages: " + EnumChatFormatting.AQUA + listen));
		}

		@Override
		public String usage() {
			return "/minedroid status";
		}
		
	}
	
	public static class MDCmdUsage implements MDCommand {

		@Override
		public void processServer(ICommandSender sender, String[] args) {
		}

		@Override
		public void processClient(String[] args) {
			if (args.length != 1) {
				MinedroidCommandHandler.sendError(usage());
			} else {
				String usage = MinedroidCommandHandler.getUsage(args[0]);
				if (usage != null)
					Minedroid2.proxy.getClientPlayer().addChatComponentMessage(new ChatComponentText("Usage for: " + args[0] + usage));
			}
		}

		@Override
		public String usage() {
			return "/minedroid usage <Command Name>";
		}
		
	}
	
}
