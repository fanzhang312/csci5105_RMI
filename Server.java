import java.net.InetAddress;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;

public class Server implements Communicate {
	ArrayList<ClientModel> clientList = new ArrayList<ClientModel>();

	protected Server() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}
	// Print all clients who currently joined server
	public void printClientList(){
		for (ClientModel c : clientList) {
			System.out.println("Client: "+c.toString());
			System.out.println("Subscribe: "+c.subscribeCategoryToString());
		}
	}
	
	// Check whether a client is already joined server or not
	public int checkClient(String IP, int Port){
		for (Iterator<ClientModel> it = clientList.iterator(); it.hasNext();){
			ClientModel client = it.next();
			// Same client must have same IP and same Port number
			if(client.getIpAddress().equals(IP)&&client.getPortNumber()==Port){
				return clientList.indexOf(client);
			}
		}
		return -1;
	}
	
	// Method for client join or leave the server
	public boolean checkClient(String IP, int Port, String joinOrLeave){
		for (Iterator<ClientModel> it = clientList.iterator(); it.hasNext();){
			ClientModel client = it.next();
			// Same client must have same IP and same Port number
			if(client.getIpAddress().equals(IP)&&client.getPortNumber()==Port){
				if(joinOrLeave.equals("join")){
					// Same client don't allowed join server twice
					System.out.println("client has already joined server");
					return false;
				}else if(joinOrLeave.equals("leave")){
					clientList.remove(client);
					System.out.println("client leaved successfully");
					printClientList();
					return true;
				}
			}
		}
		if(joinOrLeave.equals("join")){
			ClientModel client = new ClientModel(IP, Port);
			clientList.add(client);
			int size = clientList.size();
			System.out.println(clientList.get(size - 1).getIpAddress() + ";"
					+ clientList.get(size - 1).getPortNumber() + " Joined server");
			printClientList();
			return true;
		}
		return false;
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
		if(index != -1){
			ClientModel client = clientList.get(index);
			if(client.sub(Article)){
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
		if(index != -1){
			ClientModel client = clientList.get(index);
			if(client.unsub(Article)){
				// if unsubscribe success, update clientList
				clientList.remove(index);
				clientList.add(client);
				printClientList();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean Publish(String Article, String IP, int Port)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean PublishServer(String Article, String IP, int Port)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Ping() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	public static void main(String args[]) {

		try {
			Server obj = new Server();
			Communicate stub = (Communicate) UnicastRemoteObject.exportObject(
					obj, 0);
			InetAddress address = InetAddress.getLocalHost();
			String hostIP = address.getHostAddress();
			// Bind the remote object's stub in the registry
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("server.Communicate", stub);

			System.err.println("Server ready: " + hostIP);
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}

}
