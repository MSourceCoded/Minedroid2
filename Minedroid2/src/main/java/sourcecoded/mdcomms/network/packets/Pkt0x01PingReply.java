package sourcecoded.mdcomms.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import sourcecoded.mdcomms.network.SCSide;

public class Pkt0x01PingReply implements ISourceCommsPacket {

	public long OnEncode, OnDecode, diff, diffMS;
	
	public Pkt0x01PingReply() {}
	public Pkt0x01PingReply(long encode) {
		this.OnEncode = encode;
	}
	
	@Override
	public void encode(DataOutputStream data) throws IOException {
		data.writeLong(OnEncode);
	}

	@Override
	public void decode(DataInputStream data) throws IOException {
		OnEncode = data.readLong();
		OnDecode = System.nanoTime();
	}

	@Override
	public void executeAfter(SCSide side) {
		diff = OnDecode - OnEncode;
		diffMS = diff / 1000000L;
	}

	

}
