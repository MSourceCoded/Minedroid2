package sourcecoded.minedroid2.commandsystem;

import java.util.HashMap;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import sourcecoded.minedroid2.commandsystem.MinedroidCommands.MDCmdPing;
import sourcecoded.minedroid2.commandsystem.MinedroidCommands.MDCmdServerKill;
import sourcecoded.minedroid2.commandsystem.MinedroidCommands.MDCmdServerOpen;
import sourcecoded.minedroid2.commandsystem.MinedroidCommands.MDCmdServerStatus;

public class MinedroidCommandHandler {

	public static HashMap<String, MDCommand> accepted = new HashMap<String, MDCommand>();
	
	public static void register() {
		accepted.put("ping", new MDCmdPing());
		accepted.put("open", new MDCmdServerOpen());
		accepted.put("kill", new MDCmdServerKill());
		accepted.put("status", new MDCmdServerStatus());
	}
	
	public static void trigger(String commandName, ICommandSender sender, String[] args) {
		if (commandExists(commandName)) {
			MDCommand theCommand = accepted.get(commandName);
			
			theCommand.processServer(sender, args);
			
			//Send a client packet to handle this
		} else {
			sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Could not run command: Not Found"));
		}
	}
	
	static boolean commandExists(String commandName) {
		return accepted.containsKey(commandName);
	}
	
}
