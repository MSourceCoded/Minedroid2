package sourcecoded.comms.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import sourcecoded.comms.eventsystem.EventBus;
import sourcecoded.comms.eventsystem.event.EventPacketHandled;
import sourcecoded.comms.network.packets.ISourceCommsPacket;
import sourcecoded.comms.network.packets.Pkt0x00Ping;
import sourcecoded.comms.network.packets.Pkt1x00Player;
import sourcecoded.comms.network.packets.Pkt1x01NBTString;
import sourcecoded.comms.network.packets.Pkt1x02NBTMap;

public enum SourceCommsPacketHandler {
	INSTANCE;

	SourceCommsPacketCodec codec;

	private SourceCommsPacketHandler() {
		codec = new SourceCommsPacketCodec();
	}

	public class SourceCommsPacketCodec extends PacketCodec<ISourceCommsPacket> {

		public SourceCommsPacketCodec() {
			addDiscriminator(0, Pkt0x00Ping.class);
			addDiscriminator(10, Pkt1x00Player.class);
			addDiscriminator(11, Pkt1x01NBTString.class);
			addDiscriminator(12, Pkt1x02NBTMap.class);
		}

		@Override
		public void encode(ISourceCommsPacket packet,
				DataOutputStream serverStream) throws IOException {
			packet.encode(serverStream);
		}

		@Override
		public void decode(ISourceCommsPacket packet, DataInputStream stream)
				throws IOException {
			packet.decode(stream);
			packet.executeAfter();
			EventPacketHandled p = new EventPacketHandled(packet);
			EventBus.Publisher.raiseEvent(p);
		}

	}

	/**
	 * Match a packet with its discriminator and a DIO, then handle the rest. Call this from the Server/Client instead.
	 * @param disc The discriminator
	 * @param dio The input stream
	 */
	public void matchPacket(int disc, DataInputStream dio) {
		Class<? extends ISourceCommsPacket> packetClass = codec.getPacketClass(disc);
		try {
			ISourceCommsPacket packet = packetClass.newInstance();;
			//Not an event publish, makes sure the server/client finishes reading the packet before moving on
			onReceivePacket(packet, dio);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Send a packet across a DOS. Call this from the Server/Client instead.
	 * @param thePacket The packet to write
	 * @param serverStream The stream to write to
	 */
	public void send(ISourceCommsPacket thePacket, DataOutputStream serverStream) {
		try {
			codec.encodeInto(thePacket, serverStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Triggers when a packet is received from the server. This will decode it and send an EventPacketHandled event
	 * @param event The event triggered
	 */
	void onReceivePacket(ISourceCommsPacket packet, DataInputStream dio) {
		try {
			codec.decodeInto(packet, dio);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write a string to an OutputStream in the form of a byteArray
	 * @param arg The string to write
	 * @param oos The stream to write to
	 * @throws IOException Something went wrong
	 */
	public void writeString(String arg, DataOutputStream oos)
			throws IOException {
		byte[] theBytes = arg.getBytes();
		oos.writeInt(theBytes.length);
		oos.write(theBytes);
	}

	/**
	 * Read a string from an InputStream assuming it is in the form of a byteArray
	 * @param ois The stream to read from
	 * @return The string that was read
	 * @throws IOException Something went wrong
	 */
	public String readString(DataInputStream ois) throws IOException {
		int length = ois.readInt();
		byte[] bytes = new byte[length];
		ois.read(bytes);
		return new String(bytes);
	}
}
