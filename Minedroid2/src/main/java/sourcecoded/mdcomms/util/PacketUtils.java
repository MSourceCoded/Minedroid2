package sourcecoded.mdcomms.util;

import sourcecoded.mdcomms.network.packets.ISourceCommsPacket;

public class PacketUtils {

	@SuppressWarnings("rawtypes")
	public static boolean compareClass(ISourceCommsPacket pkt, Class target) {
		if (pkt.getClass().equals(target)) {
			return true;
		} else return false;
	}
	
}
