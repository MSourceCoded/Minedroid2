package sourcecoded.minedroid2.commandsystem;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

public class MinedroidServerCommand extends CommandBase {

	public MinedroidServerCommand() {
		MinedroidCommandHandler.register();
	}
	
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
		if (args.length > 0) {
			String[] newArgs = new String[args.length - 1];
			
			for (int i = 0; i < newArgs.length; i++) {
				newArgs[i] = args[i+1];
			}
			
			MinedroidCommandHandler.trigger(args[0], sender, newArgs);
		} else {
			throw new WrongUsageException(getCommandUsage(sender), new Object[0]);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        return par2ArrayOfStr.length == 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, MinedroidCommandHandler.accepted.keySet().toArray(new String[0])): null;
    }
	
}
