import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import communicate.Communicate;

public class Client {

    private Client() {}

    public static void main(String[] args) {

	try {
	    Registry registry = LocateRegistry.getRegistry();
	    String[] serverStubs = registry.list();
	    for(String stub : serverStubs){
	    	System.out.println(stub);
	    }
	     Communicate stub = (Communicate) registry.lookup("Hello");
	     InetAddress address = InetAddress.getLocalHost(); 
	     String hostIP = address.getHostAddress() ;
	     int port = 1099;
	     boolean join = stub.Join(hostIP,port);
	     System.out.println("Join Server: " + join);
	     boolean leave = stub.Leave(hostIP, port);
	     System.out.println("Leave Server: "+ leave);
	} catch (Exception e) {
	    System.err.println("Client exception: " + e.toString());
	    e.printStackTrace();
	}
    }
}
