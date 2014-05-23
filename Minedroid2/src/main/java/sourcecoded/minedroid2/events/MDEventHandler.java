package sourcecoded.minedroid2.events;

import sourcecoded.mdcomms.network.packets.Pkt1x04ChatMessageUnf;
import sourcecoded.mdcomms.socket.SourceCommsServer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MDEventHandler {

	@SubscribeEvent
	public void chatMessage(ClientChatReceivedEvent evnt) {
		String chatMessage = evnt.message.getUnformattedText();
		
		Pkt1x04ChatMessageUnf packet = new Pkt1x04ChatMessageUnf(chatMessage);
		SourceCommsServer.instance().sendToClient(packet);
	}
	
}
