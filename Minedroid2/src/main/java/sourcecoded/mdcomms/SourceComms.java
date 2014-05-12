package sourcecoded.mdcomms;

import sourcecoded.mdcomms.eventsystem.EventBus;
import sourcecoded.mdcomms.exception.ErrorHandler;

public class SourceComms {

	public static void init() {
		EventBus.Registry.register(ErrorHandler.class);
	}
	
}
