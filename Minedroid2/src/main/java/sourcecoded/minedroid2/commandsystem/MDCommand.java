package sourcecoded.minedroid2.commandsystem;

import net.minecraft.command.ICommandSender;

public interface MDCommand {

	public void processServer(ICommandSender sender, String[] args);
	
	public void processClient(String[] args);
	
	public String usage();
}
