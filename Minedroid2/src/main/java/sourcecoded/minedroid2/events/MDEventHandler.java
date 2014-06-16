package sourcecoded.minedroid2.events;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraftforge.client.event.GuiScreenEvent;
import sourcecoded.mdcomms.network.packets.Pkt1x04ChatMessageUnf;
import sourcecoded.mdcomms.socket.SourceCommsServer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import sourcecoded.minedroid2.client.gui.GuiMainMenuHandler;

public class MDEventHandler {

	@SubscribeEvent
	public void chatMessage(ClientChatReceivedEvent evnt) {
		String chatMessage = evnt.message.getUnformattedText();
		
		Pkt1x04ChatMessageUnf packet = new Pkt1x04ChatMessageUnf(chatMessage);
		SourceCommsServer.instance().sendToClient(packet);
	}

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent event) {
        if (event.gui instanceof GuiOptions)
            GuiMainMenuHandler.initGui(event.gui, event.buttonList);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onGuiActionPerformed(GuiScreenEvent.ActionPerformedEvent event) {
        if (event.gui instanceof GuiOptions)
            GuiMainMenuHandler.onActionPerformed(event.button);
    }

}
