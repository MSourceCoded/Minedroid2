package sourcecoded.comms.eventsystem.event;

import sourcecoded.comms.eventsystem.IEvent;

public class EventClientClosed implements IEvent {

	private int port, error;
	private String reason, hostname;
	
	public EventClientClosed(int port, String hostname, int errorCode, String reason) {
		this.port = port;
		this.error = errorCode;
		this.reason = reason;
		this.hostname = hostname;
	}
	
	/**
	 * Get the port of the connected server
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
	
	/**
	 * get the hostname of the connected server
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}
}
