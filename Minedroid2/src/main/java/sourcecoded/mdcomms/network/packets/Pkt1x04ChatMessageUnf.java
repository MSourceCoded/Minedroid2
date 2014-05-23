package sourcecoded.mdcomms.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import sourcecoded.mdcomms.network.SCSide;
import sourcecoded.mdcomms.network.SourceCommsPacketHandler;

public class Pkt1x04ChatMessageUnf implements ISourceCommsPacket {

	public String message;
	
	public Pkt1x04ChatMessageUnf() {}
	
	public Pkt1x04ChatMessageUnf(String unformattedMessage) {
		message = unformattedMessage;
	}
	
	@Override
	public void encode(DataOutputStream data) throws IOException {
		SourceCommsPacketHandler.INSTANCE.writeString(message, data);
	}

	@Override
	public void decode(DataInputStream data) throws IOException {
		message = SourceCommsPacketHandler.INSTANCE.readString(data);
	}

	@Override
	public void executeAfter(SCSide side) {
	}

}
