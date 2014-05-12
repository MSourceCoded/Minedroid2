package sourcecoded.mdcomms.eventsystem.event;

import sourcecoded.mdcomms.eventsystem.IEvent;
import sourcecoded.mdcomms.network.packets.ISourceCommsPacket;

public class EventPacketHandled implements IEvent{
	
	private ISourceCommsPacket p;
	
	public EventPacketHandled(ISourceCommsPacket packet) {
		p = packet;
	}
	
	public ISourceCommsPacket getPacket() {
		return p;
	}
}
