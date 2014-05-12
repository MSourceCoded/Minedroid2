package sourcecoded.mdcomms.exception;

import sourcecoded.mdcomms.eventsystem.SourceCommsEvent;
import sourcecoded.mdcomms.eventsystem.event.EventClientClosed;
import sourcecoded.mdcomms.eventsystem.event.EventServerClosed;

public class ErrorHandler {

	@SourceCommsEvent
	public static void serverClose(EventServerClosed e) {
		System.err.println("The server was closed! Code: " + e.getCode() + " Reason: " + e.getReason());
	}
	
	@SourceCommsEvent
	public static void clientClose(EventClientClosed e) {
		System.err.println("The client was closed! Code: " + e.getCode() + " Reason: " + e.getReason());
	}
	
}
