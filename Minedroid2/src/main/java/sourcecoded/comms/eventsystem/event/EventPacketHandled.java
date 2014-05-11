package sourcecoded.comms.eventsystem.event;

import sourcecoded.comms.eventsystem.IEvent;
import sourcecoded.comms.network.packets.ISourceCommsPacket;

public class EventPacketHandled implements IEvent{
	
	private ISourceCommsPacket p;
	
	public EventPacketHandled(ISourceCommsPacket packet) {
		p = packet;
	}
	
	public ISourceCommsPacket getPacket() {
		return p;
	}
}
