package sourcecoded.mdcomms.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import sourcecoded.mdcomms.network.SCSide;
import sourcecoded.mdcomms.socket.SourceCommsClient;
import sourcecoded.mdcomms.socket.SourceCommsServer;

public class Pkt0x00PingRequest implements ISourceCommsPacket {

	public long OnEncode;
	
	public Pkt0x00PingRequest() {
	}
	
	@Override
	public void encode(DataOutputStream data) throws IOException {
		OnEncode = System.nanoTime();
		data.writeLong(OnEncode);
	}

	@Override
	public void decode(DataInputStream data) throws IOException {
		OnEncode = data.readLong();
	}

	@Override
	public void executeAfter(SCSide side) {
		if (side == SCSide.Client) {
			SourceCommsServer.instance().sendToClient(new Pkt0x01PingReply(OnEncode));
		} else if (side == SCSide.Server) {
			SourceCommsClient.instance().sendToServer(new Pkt0x01PingReply(OnEncode));
		}
	}

	
	
}
