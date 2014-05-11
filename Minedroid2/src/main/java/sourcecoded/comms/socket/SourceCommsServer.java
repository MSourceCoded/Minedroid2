package sourcecoded.comms.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import sourcecoded.comms.eventsystem.EventBus;
import sourcecoded.comms.eventsystem.event.EventServerClosed;
import sourcecoded.comms.eventsystem.event.EventServerReady;
import sourcecoded.comms.exception.ErrorCodes;
import sourcecoded.comms.network.PacketCodec;
import sourcecoded.comms.network.SourceCommsPacketHandler;
import sourcecoded.comms.network.packets.ISourceCommsPacket;

public class SourceCommsServer {
	private ServerSocket server = null;
	private Socket theClient = null;
	private DataOutputStream outToClient = null;
	private DataInputStream inFromClient = null;

	private int port;

	private Thread listener;

	private boolean isListening = false;
	private boolean isReady = false;

	private static SourceCommsServer instance;
	private SourceCommsServer() {	}

	/**
	 * Get (and set) the server instance
	 * @return
	 */
	public static SourceCommsServer instance() {
		if (instance == null)
			instance = new SourceCommsServer();
		return instance;
	}

	/**
	 * Set the port to open the server on.
	 * @param port
	 */
	public void setData(int port) {
		this.port = port;
	}


	/**
	 * Opens the server to accept connections
	 */
	public void open() {
		Thread serverOpen = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					server = new ServerSocket(port);
					theClient = server.accept();
					inFromClient = new DataInputStream(theClient.getInputStream());
					outToClient = new DataOutputStream(theClient.getOutputStream());

					isReady = true;
					EventBus.Publisher.raiseEvent(new EventServerReady());
				} catch (BindException e) {
					closeWithError(ErrorCodes.PORT_USED, "This port is already in use");
					return;
				} catch (IOException e) {
					closeWithError(ErrorCodes.STREAM_EXCEPTION, "Could not connect to client data streams");
					e.printStackTrace();
				}

			}
		}); 

		serverOpen.start();
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
							System.err.println("Server not ready, retrying " + counter + " of 20");
							Thread.sleep(250);
						} else {
							counter = 0;
							closeWithError(ErrorCodes.STREAM_READ_FAIL, "Could not read from server data streams");
							return;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					closeWithError(ErrorCodes.UNKNOWN, "An unknown error occurred");

				}
				
				
				while (isListening && isReady) {
					if (server != null && theClient != null && inFromClient != null && !server.isClosed()) {
						try {
							if (inFromClient.readInt() == PacketCodec.START_OF_MESSAGE) {
								//Read the thing
								int discriminator = inFromClient.readInt();
								SourceCommsPacketHandler.INSTANCE.matchPacket(discriminator, inFromClient);
							} 
						} catch (IOException e) {
							e.printStackTrace();
							//Close the server
							closeWithError(ErrorCodes.STREAM_READ_FAIL, "Could not read from client data streams");
						}
					}
				}
			}
		});

		//Loop here for the isReady race condition
		listener.start();
	}


	/**
	 * Send a packet to the client from the server. 
	 * @param packet The packet to be sent
	 */
	public void sendToClient(ISourceCommsPacket packet) {
		if (theClient != null && outToClient != null && !server.isClosed())
			SourceCommsPacketHandler.INSTANCE.send(packet, outToClient);
	}

	/**
	 * Close the server and the client
	 */
	public void close() {
		isListening = false;
		try {
			inFromClient.close();
			outToClient.close();
			theClient.close();
			server.close();
		} catch (Exception e) {
		}
	}

	/**
	 * Close the server and client on error
	 * @param code The Error Code
	 * @param reason The Reason for the close
	 */
	public void closeWithError(int code, String reason) {
		EventBus.Publisher.raiseEvent(new EventServerClosed(port, code, reason));
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
