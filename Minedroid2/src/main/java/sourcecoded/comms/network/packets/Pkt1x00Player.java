package sourcecoded.comms.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import sourcecoded.comms.network.SourceCommsPacketHandler;

public class Pkt1x00Player implements ISourceCommsPacket{

	public int xPos, yPos, zPos;
	
	public String target;
	
	public Pkt1x00Player() {}
	public Pkt1x00Player(int x, int y, int z, String target) {
		this.xPos = x;
		this.yPos = y;
		this.zPos = z;
		this.target = target;
	}
	
	@Override
	public void encode(DataOutputStream data) throws IOException {
		data.writeInt(xPos);
		data.writeInt(yPos);
		data.writeInt(zPos);
		SourceCommsPacketHandler.INSTANCE.writeString(target, data);
	}
	
	@Override
	public void decode(DataInputStream data) throws IOException {
		this.xPos = data.readInt();
		this.yPos = data.readInt();
		this.zPos = data.readInt();
		this.target = SourceCommsPacketHandler.INSTANCE.readString(data);
	}
	@Override
	public void executeAfter() {
		
	}
}
