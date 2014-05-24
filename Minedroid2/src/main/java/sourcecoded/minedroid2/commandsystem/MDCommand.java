package sourcecoded.minedroid2.commandsystem;

import net.minecraft.command.ICommandSender;

public interface MDCommand {

	/**
	 * Process on the server-side
	 * @param sender The sender of the command
	 * @param args The arguments passed
	 */
	public void processServer(ICommandSender sender, String[] args);
	
	/**
	 * Process on the client side after packet sending
	 * @param args The arguments to pass
	 */
	public void processClient(String[] args);
	
	/**
	 * Get the usage of this command
	 * @return The usage
	 */
	public String usage();
}
