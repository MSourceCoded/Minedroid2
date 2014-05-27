package sourcecoded.minedroid2.commandsystem;

import java.util.HashMap;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import sourcecoded.minedroid2.commandsystem.MinedroidCommands.MDCmdPing;
import sourcecoded.minedroid2.commandsystem.MinedroidCommands.MDCmdServerKill;
import sourcecoded.minedroid2.commandsystem.MinedroidCommands.MDCmdServerOpen;
import sourcecoded.minedroid2.commandsystem.MinedroidCommands.MDCmdServerStatus;
import sourcecoded.minedroid2.commandsystem.MinedroidCommands.MDCmdUsage;
import sourcecoded.minedroid2.network.MinedroidPacketHandler;
import sourcecoded.minedroid2.network.packets.PktMC1x00CommandToClient;
import sourcecoded.minedroid2.util.ChatUtils;

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
		accepted.put("usage", new MDCmdUsage());
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
//			sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Could not run command: " + commandName));
			ChatUtils.sendCmdError("Could not run command: " + commandName, sender);
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
//			Minedroid2.proxy.getClientPlayer().addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Could not run command: " + commandName));
			ChatUtils.sendError("Could not run command: " + commandName);
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
			ChatUtils.sendError("Could not find command: " + commandName);
			return null;
		}
	}
	
}
