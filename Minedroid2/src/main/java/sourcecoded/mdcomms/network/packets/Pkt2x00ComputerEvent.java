package sourcecoded.mdcomms.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import sourcecoded.mdcomms.network.SCSide;
import sourcecoded.mdcomms.network.SourceCommsPacketHandler;

public class Pkt2x00ComputerEvent implements ISourceCommsPacket {

	public int peripheral, action;
	public String[] args;
	
	public Pkt2x00ComputerEvent() {}
	
	/**
	 * Create a packet to trigger an os.pullEvent() on the computer. 
	 * @param peripheralIdentifier The peripheral IDs to trigger on. -1 for all.
	 * @param actionIdentifier The action ID. This is passed onto the Computer as an argument
	 * @param args Any extra data you want to pass onto the LUA event. 
	 */
	public Pkt2x00ComputerEvent(int peripheralIdentifier, int actionIdentifier, String[] args) {
		peripheral = peripheralIdentifier;
		action = actionIdentifier;
		this.args = args;
	}
	
	@Override
	public void encode(DataOutputStream data) throws IOException {
		data.writeInt(peripheral);
		data.writeInt(action);
		data.writeInt(args.length);
		
		for (String curr : args) {
			SourceCommsPacketHandler.INSTANCE.writeString(curr, data);
		}
	}

	@Override
	public void decode(DataInputStream data) throws IOException {
		peripheral = data.readInt();
		action = data.readInt();
		int arrayLength = data.readInt();
		
		args = new String[arrayLength];
		
		for (int i = 0; i < arrayLength; i++) {
			args[i] = SourceCommsPacketHandler.INSTANCE.readString(data);
		}
	}

	@Override
	public void executeAfter(SCSide side) {
		
	}

}
