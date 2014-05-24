package sourcecoded.minedroid2.commandsystem;

import java.util.HashMap;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import sourcecoded.minedroid2.Minedroid2;
import sourcecoded.minedroid2.commandsystem.MinedroidCommands.MDCmdPing;
import sourcecoded.minedroid2.commandsystem.MinedroidCommands.MDCmdServerKill;
import sourcecoded.minedroid2.commandsystem.MinedroidCommands.MDCmdServerOpen;
import sourcecoded.minedroid2.commandsystem.MinedroidCommands.MDCmdServerStatus;
import sourcecoded.minedroid2.network.MinedroidPacketHandler;
import sourcecoded.minedroid2.network.PktMC1x00CommandToClient;

public class MinedroidCommandHandler {

	public static HashMap<String, MDCommand> accepted = new HashMap<String, MDCommand>();
	
	/**
	 * Register all the commands
	 */
	public static void register() {
		accepted.put("ping", new MDCmdPing());
		accepted.put("open", new MDCmdServerOpen());
		accepted.put("kill", new MDCmdServerKill());
		accepted.put("status", new MDCmdServerStatus());
	}
	
	/**
	 * Trigger a command
	 * @param commandName the command name
	 * @param sender the sender
	 * @param args the arguments
	 */
	public static void trigger(String commandName, ICommandSender sender, String[] args) {
		if (commandExists(commandName)) {
			MDCommand theCommand = accepted.get(commandName);
			
			theCommand.processServer(sender, args);
			
			if (sender instanceof EntityPlayer)
				MinedroidPacketHandler.INSTANCE.sendTo(new PktMC1x00CommandToClient(commandName, args), (EntityPlayerMP)sender);
		} else {
			sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Could not run command: " + commandName));
		}
	}
	
	/**
	 * Trigger on the client-side
	 * @param commandName The command to be called
	 * @param args The args to pass
	 */
	public static void triggerClient(String commandName, String[] args) {
		if (commandExists(commandName)) {
			MDCommand theCommand = accepted.get(commandName);
			
			theCommand.processClient(args);
		} else {
			Minedroid2.proxy.getClientPlayer().addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Could not run command: " + commandName));
		}
	}
	
	/**
	 * @param commandName The command name
	 * @return Whether or not the command exists
	 */
	public static boolean commandExists(String commandName) {
		return accepted.containsKey(commandName);
	}
	
	/**
	 * Get the usage for the command. Null if command doesn't exist
	 * @param commandName The command name
	 * @return
	 */
	public static String getUsage(String commandName) {
		if (commandExists(commandName)) {
			return ((MDCommand)accepted.get(commandName)).usage();
		} else {
			sendError("Could not find command: " + commandName);
			return null;
		}
	}
	
	/**
	 * Send a error message, usually for usage
	 * @param message The message
	 */
	public static void sendError(String message) {
		Minedroid2.proxy.getClientPlayer().addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + message));
	}
	
}
