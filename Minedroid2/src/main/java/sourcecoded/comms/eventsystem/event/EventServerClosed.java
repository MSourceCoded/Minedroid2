package sourcecoded.comms.eventsystem.event;

import sourcecoded.comms.eventsystem.IEvent;

public class EventServerClosed implements IEvent {

	private int port, error;
	private String reason;
	
	public EventServerClosed(int port, int errorCode, String reason) {
		this.port = port;
		this.error = errorCode;
		this.reason = reason;
	}
	
	/**
	 * Get the port the server was running on
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Get the error code of exit
	 * @return code
	 */
	public int getCode() {
		return error;
	}
	
	/**
	 * Get the reason for exit
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
}
