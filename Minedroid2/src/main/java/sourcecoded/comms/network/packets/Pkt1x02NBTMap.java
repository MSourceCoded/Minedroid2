package sourcecoded.comms.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")	//We do things differently around here, boy
public class Pkt1x02NBTMap implements ISourceCommsPacket {

	public Map tags = new HashMap();
	
	public Pkt1x02NBTMap() {}
	public Pkt1x02NBTMap(HashMap tags) {
		this.tags = tags;
	}
	
	@Override
	public void encode(DataOutputStream data) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(data);
		oos.writeObject(tags);
	}
	
	@Override
	public void decode(DataInputStream data) throws IOException {
		ObjectInputStream ois = new ObjectInputStream(data);
		try {
			tags = (Map) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void executeAfter() {
		
	}
	
	
}
