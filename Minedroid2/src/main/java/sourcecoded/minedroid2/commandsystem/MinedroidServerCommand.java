package sourcecoded.minedroid2.commandsystem;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class MinedroidServerCommand extends CommandBase {

	@Override
	public String getCommandName() {
		return "minedroid";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/minedroid <option>";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		
	}

}
