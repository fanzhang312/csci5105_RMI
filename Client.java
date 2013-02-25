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
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Client extends Thread {
	public static final int BUFFER_SIZE = 1024;
	public static boolean clientCreated = false;
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

	/*
	 * Create a client by enter the Server IP as args[0]
	 */
	public Client(String serverIP, int port) {
		super("Client thread");
		try {
			// locate the registry on the server machine by enter the server's
			// ip and port
			registry = LocateRegistry.getRegistry(serverIP, 1099);
			stub = (Communicate) registry.lookup("server.Communicate");
			address = InetAddress.getLocalHost();
			clientIP = address.getHostAddress();
			clientPort = port;
			pingCheck = new PeriodicChecker(stub);
			// Start the UDP server and keep it listening the incoming articles
			start();
			clientCreated = true;
		} catch (Exception e) {
			clientCreated = false;
			System.err.println("Cannot connect to server. Please confirm the server's IP is entered correctly");
//			e.printStackTrace();
		}

	}
	/*
	 * Used for Server communicate with other servers. Make own server as client
	 *  
	 */
	public Client(String serverIP, int port, String bindingName) {
		super("Group Server thread");
		try {
			// locate the registry on the server machine by enter the server's
			// ip and port
			registry = LocateRegistry.getRegistry(serverIP, 1099);
			System.out.println("-------Registry-------"+registry);
			registry.rebind(bindingName, stub);
			System.out.println("-------Stub-------"+stub);
//			stub = (Communicate) registry.lookup(bindingName);
			address = InetAddress.getLocalHost();
			clientIP = address.getHostAddress();
			clientPort = port;
//			pingCheck = new PeriodicChecker(stub);
			// Start the UDP server and keep it listening the incoming articles
			start();
		} catch (Exception e) {
			System.err.println("Unable to binding to other server: " + e.toString());
//			e.printStackTrace();
		}

	}

	/*
	 * Start UDP server to listen the incoming packets. Client should ALWAYS
	 * check if there is new subscribed articles propagated from group server.
	 */
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
				System.out.println("Receive article: " + articleString
						+ " from: " + address);
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

	public boolean clientJoin() {
		boolean join = false;
		try {
			if(stub == null){
				System.out.println("Create Remote object failed, can't join other server");
				return false;
			}
			join = stub.Join(clientIP, clientPort);
			if(join)
				System.out.println("Client joined server success");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return join;
	}

	public boolean clientLeave() throws RemoteException {
		boolean leave = stub.Leave(clientIP, clientPort);
		if(leave)
			System.out.println("Client leave server success");
		return leave;
	}
	
	// JoinServer() have two fake parameters
	public boolean serverJoin() throws RemoteException {
		boolean join = stub.JoinServer(clientIP, clientPort);
		return join;
	}
	// LeaveServer() have two fake parameters
	public boolean serverLeave() throws RemoteException {
		boolean leave = stub.LeaveServer(clientIP, clientPort);
		return leave;
	}
	public boolean clientSubscribe(String articleType) throws RemoteException {
		if(articleType==null || articleType.isEmpty()){
			System.out.print("Warning: Type is empty, please check the input");
			return false;
		}
		boolean subscribe = stub.Subscribe(clientIP, clientPort, articleType);
		return subscribe;
	}

	public boolean clientUnsubscribe(String articleType) throws RemoteException {
		boolean unsubscribe = stub.Unsubscribe(clientIP, clientPort,
				articleType);
		return unsubscribe;
	}

	public boolean clientPublish(String article) throws RemoteException {
		if(article==null || article.isEmpty()){
			System.out.print("Warning: Article is empty, please check the input");
			return false;
		}
		if(!Article.checkArticle(article)){
			System.out.print("Warning: Article format is illegal, please check the input");
			return false;
		}
			
		boolean publish = stub.Publish(article, clientIP, clientPort);
		if(publish)
			System.out.println("client publish success");
		return publish;
	}

	public void clientPing() throws RemoteException {
		pingCheck.start();
	}

}
