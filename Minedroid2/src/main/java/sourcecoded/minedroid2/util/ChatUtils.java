package sourcecoded.minedroid2.util;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import sourcecoded.minedroid2.Minedroid2;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ChatUtils {

	/**
	 * Displays a message to the client player in Plain White text.
	 * @param message The message to display
	 */
	@SideOnly(Side.CLIENT)
	public static void displayMessage(String message) {
		Minedroid2.proxy.getClientPlayer().addChatComponentMessage(new ChatComponentText(message));;
	}
	
	/**
	 * Displays a data message in Gold/Aqua
	 * @param message The message identifier
	 * @param status The status of the message
	 */
	@SideOnly(Side.CLIENT)
	public static void displayInfo(String message, String status) {
		Minedroid2.proxy.getClientPlayer().addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GOLD + message + " " + EnumChatFormatting.AQUA + status));
	}
	
	/**
	 * Send error to the command sender
	 * @param error
	 * @param sender
	 */
	public static void sendCmdError(String error, ICommandSender sender) {
		sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + error));
	}
	
	/**
	 * Send error to the client
	 * @param error
	 * @param sender
	 */
	public static void sendError(String error) {
		Minedroid2.proxy.getClientPlayer().addChatMessage(new ChatComponentText(EnumChatFormatting.RED + error));
	}
}
