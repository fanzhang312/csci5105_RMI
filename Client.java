/**
 * Client class is used to create client communicate to server by RMI
 * 
 * @author Fan Zhang, zhiqi Chen
 * @param 
 * @param
 * 
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Client extends Thread {
	public static final int BUFFER_SIZE = 1024;
	public Registry registry;
	public Communicate stub;
	public InetAddress address;
	// clientIP and clientPort will distinguish different clients
	public String clientIP;
	// clientPort will use for UDP communication
	public int clientPort;
	// PeriodicChecker is a class to create a thread to periodically call ping()
	public PeriodicChecker pingCheck;
	public ArrayList<Article> articleList = new ArrayList<Article>();

	public Client(int port) {
		super("Client thread");
		try {
			// locate the registry on the server machine by enter the server's
			// ip and port
			registry = LocateRegistry.getRegistry("10.0.0.8", 1099);
			stub = (Communicate) registry.lookup("server.Communicate");
			address = InetAddress.getLocalHost();
			clientIP = address.getHostAddress();
			clientPort = port;
			pingCheck = new PeriodicChecker(stub);
			// Start the UDP server and keep it listening the incoming articles
			start();
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}

	}

	// Start UDP server to listen the incoming packets
	public void run() {
		DatagramSocket socket = null;
		try {
			// create new connection on that port
			socket = new DatagramSocket(clientPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		byte buffer[] = new byte[BUFFER_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		while (true) {
			try {
				// listen for incoming packet
				socket.receive(packet);

				// Receive article
				String articleString = new String(packet.getData());
				Article article = Server.articleFactory(articleString);
				// Add new article to ArrayList
				articleList.add(article);
				InetAddress address = packet.getAddress();
				int port = packet.getPort();
				System.out.println("Receive article: " + articleString + " from: "
						+ address);
				// Send back Ack message
				buffer = null;
				String ackString = "Article received";
				buffer = ackString.getBytes();
				packet = new DatagramPacket(buffer, buffer.length, address,
						port);

				// send Ack message back
				socket.send(packet);
				System.out.println("Acknowledgement Message Sent");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean clientJoin() throws RemoteException {
		boolean join = stub.Join(clientIP, clientPort);
		return join;
	}

	public boolean clientLeave() throws RemoteException {
		boolean leave = stub.Leave(clientIP, clientPort);
		return leave;
	}

	public boolean clientSubscribe(String articleType) throws RemoteException {
		boolean subscribe = stub.Subscribe(clientIP, clientPort, articleType);
		return subscribe;
	}

	public boolean clientUnsubscribe(String articleType) throws RemoteException {
		boolean unsubscribe = stub.Unsubscribe(clientIP, clientPort,
				articleType);
		return unsubscribe;
	}

	public boolean clientPublish(String article) throws RemoteException {
		boolean publish = stub.Publish(article, clientIP, clientPort);
		return publish;
	}

	public void clientPing() throws RemoteException {
		pingCheck.start();
	}

	public static void main(String[] args) throws RemoteException {

		Client client = new Client(2000);
		client.clientJoin();
		client.clientPing();
		client.clientSubscribe("Sports");
		client.clientSubscribe("Science");
		client.clientUnsubscribe("Science");
		// client.clientLeave();
		client.clientPublish("Sports;fan;UMN;Hello World");
		client.clientPublish("Business;fan;UMN;Who moved my cheese");
		Client client2 = new Client(2001);
		client2.clientJoin();
		client2.clientPing();
		client2.clientSubscribe("Sports");
		// client2.clientSubscribe("Science");
		client2.clientUnsubscribe("Science");

	}
}
