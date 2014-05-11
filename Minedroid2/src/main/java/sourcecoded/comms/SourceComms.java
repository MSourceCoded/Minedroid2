package sourcecoded.comms;

import sourcecoded.comms.eventsystem.EventBus;
import sourcecoded.comms.exception.ErrorHandler;

public class SourceComms {

	public static void init() {
		EventBus.Registry.register(ErrorHandler.class);
	}
	
}
