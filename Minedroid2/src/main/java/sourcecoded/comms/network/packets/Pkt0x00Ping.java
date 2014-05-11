package sourcecoded.comms.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Pkt0x00Ping implements ISourceCommsPacket {

	long OnEncode, OnDecode;
	
	public Pkt0x00Ping() {
		OnEncode = System.currentTimeMillis();
	}
	
	@Override
	public void encode(DataOutputStream data) throws IOException {
		data.writeLong(OnEncode);
	}

	@Override
	public void decode(DataInputStream data) throws IOException {
		OnEncode = data.readLong();
		OnDecode = System.currentTimeMillis();
	}

	@Override
	public void executeAfter() {
		System.err.println("Delay = " + (OnDecode - OnEncode) + "ms");
	}

}
