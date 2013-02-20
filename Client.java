import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
	public Registry registry;
	public Communicate stub;
	public InetAddress address;
	// clientIP and clientPort will distinguish different clients
	public String clientIP;
	// clientPort will use for UDP communication
	public int clientPort;
	// PeriodicChecker is a class to create a thread to periodically call ping()
	public PeriodicChecker pingCheck;

	public Client(int port) {
		try {
			// locate the registry on the server machine by enter the server's
			// ip and port
			registry = LocateRegistry.getRegistry("128.101.248.132", 1099);
			stub = (Communicate) registry.lookup("server.Communicate");
			address = InetAddress.getLocalHost();
			clientIP = address.getHostAddress();
			clientPort = port;
			pingCheck = new PeriodicChecker(stub);
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
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
		client.clientLeave();
		Client client2 = new Client(2001);
		client2.clientJoin();
		client2.clientPing();
		client2.clientSubscribe("Sports");
		client2.clientSubscribe("Science");
		client2.clientUnsubscribe("Science");
		client2.clientLeave();
//		try {
//			// locate the registry on the server machine by enter the server's
//			// ip and port
//			Registry registry = LocateRegistry.getRegistry("128.101.248.132",
//					1099);
//			String[] serverStubs = registry.list();
//			for (String stub : serverStubs) {
//				System.out.println(stub);
//			}
//			Communicate stub = (Communicate) registry
//					.lookup("server.Communicate");
//			InetAddress address = InetAddress.getLocalHost();
//			String hostIP = address.getHostAddress();
//			// this port is used for UDP communications
//			int port = 2000;
//			boolean join = stub.Join(hostIP, port);
//			System.out.println("Join Server: " + join);
//			boolean subscribe = stub.Subscribe(hostIP, port, "Sports");
//			stub.Subscribe(hostIP, port, "ljldfj");
//			stub.Subscribe(hostIP, port, "Science");
//			stub.Subscribe(hostIP, port, "Science");
//			stub.Unsubscribe(hostIP, port, "Sports");
//			stub.Unsubscribe(hostIP, port, "Sports");
//			stub.Publish("Sports;someone;UMN;Hello World!", hostIP, port);
//			System.out.println("Subscribe:" + subscribe);
//			PeriodicChecker check = new PeriodicChecker(stub);
//			check.start();
//			boolean leave = stub.Leave(hostIP, port);
//			System.out.println("Leave Server: " + leave);
//		} catch (Exception e) {
//			System.err.println("Client exception: " + e.toString());
//			e.printStackTrace();
//		}
	}
}
