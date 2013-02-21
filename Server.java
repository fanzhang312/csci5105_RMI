import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;

public class Server extends Thread implements Communicate {
	ArrayList<ClientModel> clientList = new ArrayList<ClientModel>();
	ArrayList<Article> articleList = new ArrayList<Article>();
	public static final int SERVER_PORT = 6060;
	public String serverIP;
	protected Server() throws RemoteException {
		super();
		try {
			serverIP = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		start();
	}

	// Print all clients who currently joined server
	public void printClientList() {
		for (ClientModel c : clientList) {
			System.out.println("Client: " + c.toString());
			System.out.println("Subscribe: " + c.subscribeCategoryToString());
		}
	}

	// Print all articles
	public void printArticleList() {
		for (Article a : articleList) {
			System.out.println("Article: " + a.toString());
		}
	}

	// Check whether a client is already joined server or not
	public int checkClient(String IP, int Port) {
		for (Iterator<ClientModel> it = clientList.iterator(); it.hasNext();) {
			ClientModel client = it.next();
			// Same client must have same IP and same Port number
			if (client.getIpAddress().equals(IP)
					&& client.getPortNumber() == Port) {
				return clientList.indexOf(client);
			}
		}
		return -1;
	}

	// Method for client join or leave the server
	public boolean checkClient(String IP, int Port, String joinOrLeave) {
		for (Iterator<ClientModel> it = clientList.iterator(); it.hasNext();) {
			ClientModel client = it.next();
			// Same client must have same IP and same Port number
			if (client.getIpAddress().equals(IP)
					&& client.getPortNumber() == Port) {
				if (joinOrLeave.equals("join")) {
					// Same client don't allowed join server twice
					System.out.println("client has already joined server");
					return false;
				} else if (joinOrLeave.equals("leave")) {
					clientList.remove(client);
					System.out.println("client leaved successfully");
					printClientList();
					return true;
				}
			}
		}
		if (joinOrLeave.equals("join")) {
			ClientModel client = new ClientModel(IP, Port);
			clientList.add(client);
			int size = clientList.size();
			System.out.println(clientList.get(size - 1).getIpAddress() + ";"
					+ clientList.get(size - 1).getPortNumber()
					+ " Joined server");
			printClientList();
			return true;
		}
		return false;
	}

	// Split article strings and save as Article model with author information
	public Article articleFactory(String articleString, String ip, int port) {
		Article item;
		String[] items = articleString.split(";");
		if (items.length == 4) {
			item = new Article(items[0].trim(), items[1].trim(),
					items[2].trim(), items[3].trim(), ip, port);
			return item;
		}
		System.out
				.println("Illegal Input Article Format, please follow: type;originator;org;contents");
		return null;
	}

	// Split article strings and save as Article model without author
	// information
	public static Article articleFactory(String articleString) {
		Article item;
		String[] items = articleString.split(";");
		if (items.length == 4) {
			item = new Article(items[0].trim(), items[1].trim(),
					items[2].trim(), items[3].trim());
			return item;
		}
		System.out
				.println("Illegal Input Article Format, please follow: type;originator;org;contents");
		return null;
	}

	@Override
	public boolean JoinServer(String IP, int Port) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean LeaveServer(String IP, int Port) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Join(String IP, int Port) throws RemoteException {
		return checkClient(IP, Port, "join");
	}

	@Override
	public boolean Leave(String IP, int Port) throws RemoteException {
		return checkClient(IP, Port, "leave");
	}

	@Override
	// client subscribe a category of article
	public boolean Subscribe(String IP, int Port, String Article)
			throws RemoteException {
		int index = checkClient(IP, Port);
		if (index != -1) {
			ClientModel client = clientList.get(index);
			if (client.sub(Article)) {
				// if subscribe success update clientList
				clientList.remove(index);
				clientList.add(client);
				printClientList();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean Unsubscribe(String IP, int Port, String Article)
			throws RemoteException {
		int index = checkClient(IP, Port);
		if (index != -1) {
			ClientModel client = clientList.get(index);
			if (client.unsub(Article)) {
				// if unsubscribe success, update clientList
				clientList.remove(index);
				clientList.add(client);
				printClientList();
				return true;
			}
		}
		return false;
	}

	/*
	 * client publish article to server by using RMI. The server should
	 * propagate the article to subscriptions
	 */
	@Override
	public boolean Publish(String Article, String IP, int Port)
			throws RemoteException {
		Article item = articleFactory(Article, IP, Port);
		// If the format is illegal, item will be null
		if (item.equals(null)) {
			return false;
		}
		articleList.add(item);
		propagate(item, clientList);
		System.out.println("Publish success");
		printArticleList();
		return true;
	}

	/*
	 * propagate method focus on assign each article to its subscriptions
	 */
	public boolean propagate(Article article, ArrayList<ClientModel> clients) {
		String type = article.type;
		for (ClientModel client : clients) {
			if (client.isSubscribe()) {
				// Find whether the client subscribe this type of article
				if (client.subscribeCategory.contains(type)) {
					sendArticle(article.toString(), client);
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * sendArticle method use UDP send out article to client
	 */
	public void sendArticle(String article, ClientModel client) {
		try {
			byte message[] = new byte[Client.BUFFER_SIZE];
			String msgString = article;
			message = msgString.getBytes();
			String host = client.getIpAddress();
			InetAddress address = InetAddress.getByName(host);
			// Server send article to client
			DatagramSocket socket = new DatagramSocket();
			DatagramPacket packet = new DatagramPacket(message, message.length,
					address, client.getPortNumber()); 
			socket.send(packet);
			System.out.println("Article:" + article + " Sent to:" + host+":"+client.getPortNumber());
			
			// Waiting for Acknowledgement Message
			message = new byte[Client.BUFFER_SIZE];
			packet = new DatagramPacket(message, message.length);
			socket.receive(packet);
			String clientAreply = new String(packet.getData());
			System.out.println("Client at "+host+":"+client.getPortNumber()+" confirm "+clientAreply);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * Register to the RegistryServer 
	 */
	public void communicateRegistryServer(String type){
		String registerString = "";
		if(type.equals("Register")){
			registerString = "Register;RMI;"+this.serverIP+";"+SERVER_PORT+";server.Communicate;1099";
		}else if(type.equals("Deregister")){
			registerString = "Deregister;RMI;"+this.serverIP+";"+SERVER_PORT;
		}
		try{
			byte message[] = new byte[Client.BUFFER_SIZE];
			InetAddress registryServerAddress = InetAddress.getByName("128.101.35.147");
			int regisryServerPort = 5105;
			message = registerString.getBytes();
			DatagramSocket socket = new DatagramSocket();
			DatagramPacket packet = new DatagramPacket(message, message.length, registryServerAddress, regisryServerPort);
			socket.send(packet);
			System.out.println(type + " Success");
		}catch (Exception e){
			System.out.println(type + " Failed");
			e.printStackTrace();
		}
	}
	public void run(){
		communicateRegistryServer("Register");
	}
	@Override
	public boolean PublishServer(String Article, String IP, int Port)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	// Ping() need to be called periodically to make sure server status
	@Override
	public boolean Ping() throws RemoteException {
		Registry regi = LocateRegistry.getRegistry();
		String[] regiName = regi.list();
		// As long as there is remote object reference, we say the server is
		// running
		if (regiName.length > 0) {
			Date now = new Date();
			Timestamp time = new Timestamp(now.getTime());
			System.out.println("Server status: running at " + time);
			return true;
		}
		return false;
	}

	public static void main(String args[]) {

		try {
			Server obj = new Server();
			Communicate stub = (Communicate) UnicastRemoteObject.exportObject(
					obj, 0);
			// Bind the remote object's stub in the registry
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("server.Communicate", stub);

			System.err.println("Server ready: " + obj.serverIP);
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}

}
