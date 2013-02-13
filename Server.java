import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;

import communicate.Communicate;
	
public class Server implements Communicate {
	ArrayList<ClientModel> clientInfo = new ArrayList<ClientModel>();
    protected Server() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean JoinServer(String IP, int Port) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean LeaveServer(String IP, int Port) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Join(String IP, int Port)  {
		ClientModel client = new ClientModel(IP, Port);
		clientInfo.add(client);
		int size = clientInfo.size();
		System.out.println(clientInfo.get(size-1).getIpAddress()+";"+clientInfo.get(size-1).getPortNumber()+" Joined server");
		return true;
	}

	@Override
	public boolean Leave(String IP, int Port)  {
		for(Iterator<ClientModel> it = clientInfo.iterator(); it.hasNext();){
			ClientModel client = it.next();
			if(client.getIpAddress().equals(IP)){
				// Once client removed, return true
				clientInfo.remove(client);
				for(ClientModel c : clientInfo){
					System.out.println(c.toString());
				}
				return true;
			}
		}	
		return false;
	}

	@Override
	public boolean Subscribe(String IP, int Port, String Article) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Unsubscribe(String IP, int Port, String Article) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Publish(String Article, String IP, int Port) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Ping()  {
		// TODO Auto-generated method stub
		return false;
	}
	
    public static void main(String args[]) {
	
	try {
	    Server obj = new Server();
	    Communicate stub = (Communicate) UnicastRemoteObject.exportObject(obj, 0);

	    // Bind the remote object's stub in the registry
	    Registry registry = LocateRegistry.getRegistry();
	    registry.bind("Hello", stub);

	    System.err.println("Server ready");
	} catch (Exception e) {
	    System.err.println("Server exception: " + e.toString());
	    e.printStackTrace();
	}
    }

	
}
