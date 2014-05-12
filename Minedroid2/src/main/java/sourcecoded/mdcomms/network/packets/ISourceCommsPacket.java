package sourcecoded.mdcomms.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import sourcecoded.mdcomms.network.SCSide;

public interface ISourceCommsPacket {
	
	/**
	 * Encode a packet to the stream. Pass your stream.write things here
	 * @param data The stream to write to
	 * @throws IOException Something happened that wasn't meant to
	 */
	public void encode(DataOutputStream data) throws IOException;
	
	/**
	 * Decode a packet from the stream. Pass your stream.read things here
	 * @param data The stream to read from
	 * @throws IOException Something happened that wasn't meant to
	 */
	public void decode(DataInputStream data) throws IOException;

	/**
	 * Executed after the packet is received
	 * @param side The side the packet was sent from
	 */
	public void executeAfter(SCSide side);
	
}
