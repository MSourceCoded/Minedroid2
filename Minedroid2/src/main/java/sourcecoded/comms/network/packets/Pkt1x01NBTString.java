package sourcecoded.comms.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import sourcecoded.comms.network.SourceCommsPacketHandler;

public class Pkt1x01NBTString implements ISourceCommsPacket {

	public String theTags;
	
	//Temporary until I can get around to handling NBT in HashMap form
	public Pkt1x01NBTString() {}
	public Pkt1x01NBTString(String tags) {
		this.theTags = tags;
	}
	
	@Override
	public void encode(DataOutputStream data) throws IOException {
		SourceCommsPacketHandler.INSTANCE.writeString(theTags, data);
	}

	@Override
	public void decode(DataInputStream data) throws IOException {
		theTags = SourceCommsPacketHandler.INSTANCE.readString(data);
	}

	@Override
	public void executeAfter() {
		
	}

}
