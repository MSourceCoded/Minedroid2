package sourcecoded.comms.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import sourcecoded.comms.network.packets.ISourceCommsPacket;

public abstract class PacketCodec<PacketType extends ISourceCommsPacket> {

	public static int START_OF_MESSAGE = -126;
	
	private HashMap<Integer, Class<? extends PacketType>> discriminators = new HashMap<Integer, Class<? extends PacketType>>();
	private HashMap<Class<? extends PacketType>, Integer> types = new HashMap<Class<? extends PacketType>, Integer>();
	
	public void addDiscriminator(int key, Class<? extends PacketType> packet) {
		discriminators.put(key, packet);
		types.put(packet, key);
	}
	
	public Class<? extends PacketType> getPacketClass(int disc) {
		return discriminators.get(disc);
	}
	
	public int getDiscriminator(Class<? extends PacketType> p) {
		return types.get(p);
	}
	
	public abstract void encode(PacketType packet, DataOutputStream serverStream) throws IOException;
	
	@SuppressWarnings("unchecked")
	public void encodeInto(PacketType packet, DataOutputStream serverStream) throws IOException {
		serverStream.writeInt(START_OF_MESSAGE);
		int disc = getDiscriminator((Class<? extends PacketType>) packet.getClass());
		serverStream.writeInt(disc);
		encode(packet, serverStream);
	}
	
	public abstract void decode(PacketType packet, DataInputStream stream) throws IOException;
	
	public void decodeInto(PacketType packet, DataInputStream stream) throws IOException {
		decode(packet, stream);
	}
}
