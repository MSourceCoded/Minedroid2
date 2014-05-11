package sourcecoded.comms.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import sourcecoded.comms.eventsystem.EventBus;
import sourcecoded.comms.eventsystem.event.EventClientClosed;
import sourcecoded.comms.eventsystem.event.EventClientReady;
import sourcecoded.comms.exception.ErrorCodes;
import sourcecoded.comms.network.SourceCommsPacketHandler;
import sourcecoded.comms.network.PacketCodec;
import sourcecoded.comms.network.packets.ISourceCommsPacket;

public class SourceCommsClient {
	private String host;
	private int hostPort;
	private Socket client = null;
	private DataOutputStream outToServer = null;
	private DataInputStream inFromServer = null;

	private Thread listener;

	private boolean isListening = false;
	private boolean isReady = false;

	private static SourceCommsClient instance;
	private SourceCommsClient() {	}

	/**
	 * Get (and set) the client instance
	 * @return
	 */
	public static SourceCommsClient instance() {
		if (instance == null)
			instance = new SourceCommsClient();
		return instance;
	}

	/**
	 * Set the hostname and port of the server
	 * @param hostname 
	 * @param port
	 */
	public void setData(String hostname, int port) {
		host = hostname;
		hostPort = port;
	}

	/**
	 * Connect to the server.
	 * @throws UnknownHostException	The server was not found
	 * @throws IOException DataStream error
	 */
	public void connect() {
		try {
			client = new Socket(host, hostPort);
			inFromServer = new DataInputStream(client.getInputStream());
			outToServer = new DataOutputStream(client.getOutputStream());
			
			isReady = true;
			EventBus.Publisher.raiseEvent(new EventClientReady());
		} catch (UnknownHostException e) {
			EventBus.Publisher.raiseEvent(new EventClientClosed(hostPort, host, ErrorCodes.UNKNOWN_HOST, "Unknown Hostname"));
			e.printStackTrace();
		} catch (IOException e) {
			closeWithError(ErrorCodes.STREAM_EXCEPTION, "Could not connect to server data streams");
			e.printStackTrace();
		}
	}

	/**
	 * Start listening for connections. Will read packets and handle them
	 */
	public void listen() {
		listener = new Thread(new Runnable() {
			@Override
			public void run() {
				int counter = 0;
				try {
					while (!isReady && isListening) {
						counter++;
						if (counter < 20) {
							System.err.println("Client not ready, retrying " + counter + " of 20");
							Thread.sleep(250);
						} else {
							counter = 0;
							closeWithError(ErrorCodes.STREAM_READ_FAIL, "Could not read from client data streams");
							return;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					closeWithError(ErrorCodes.UNKNOWN, "An unknown error occurred");

				}
				
				while (isListening && isReady) {
					if (client != null && inFromServer != null && !client.isClosed()) {
						try {
							if (inFromServer.readInt() == PacketCodec.START_OF_MESSAGE) {
								//Read the thing
								int discriminator = inFromServer.readInt();
								SourceCommsPacketHandler.INSTANCE.matchPacket(discriminator, inFromServer);
							} 
						} catch (IOException e) {
							e.printStackTrace();
							//Close the client
							closeWithError(ErrorCodes.STREAM_READ_FAIL, "Could not read from server data streams");
						}
					} else if (client.isClosed()) {
						closeWithError(ErrorCodes.STREAM_READ_FAIL, "End of stream");
					}
				}
			}
		});
		listener.start();
	}


	/**
	 * Send a packet to the server from the client. 
	 * @param packet The packet to be sent
	 */
	public void sendToServer(ISourceCommsPacket packet) {
		if (client != null && outToServer != null && !client.isClosed())
			SourceCommsPacketHandler.INSTANCE.send(packet, outToServer);
	}

	/**
	 * Close the client
	 */
	public void close() {
		isListening = false;
		try {
			inFromServer.close();
			outToServer.close();
			client.close();
		} catch (Exception e) {
		}
	}
	
	/**
	 * Close the client on error
	 * @param code The Error Code
	 * @param reason The Reason for the close
	 */
	public void closeWithError(int code, String reason) {
		EventBus.Publisher.raiseEvent(new EventClientClosed(hostPort, host, code, reason));
		close();
	}
	
	/**
	 * Get the listening state of the server
	 * @return listening state
	 */
	public boolean isListening() {
		return isListening;
	}

	/**
	 * Sets the listening state of the server. Will stop the listen() thread.
	 * @param listen The listening state to set
	 */
	public void setListeningState(boolean listen) {
		isListening = listen;
	}
}
