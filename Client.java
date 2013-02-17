import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private Client() {}

    public static void main(String[] args) {

	try {
	    Registry registry = LocateRegistry.getRegistry();
	    String[] serverStubs = registry.list();
	    for(String stub : serverStubs){
	    	System.out.println(stub);
	    }
	     Communicate stub = (Communicate) registry.lookup("server.Communicate");
	     InetAddress address = InetAddress.getLocalHost(); 
	     String hostIP = address.getHostAddress() ;
	     // this port is used for UDP communications
	     int port = 2000;
	     boolean join = stub.Join(hostIP,port);
	     System.out.println("Join Server: " + join);
	     boolean subscribe = stub.Subscribe(hostIP, port, "Sports");
	     stub.Subscribe(hostIP, port, "ljldfj");
	     stub.Subscribe(hostIP, port, "Science");
	     stub.Subscribe(hostIP, port, "Science");
	     stub.Unsubscribe(hostIP, port, "Sports");
	     stub.Unsubscribe(hostIP, port, "Sports");
	     System.out.println("Subscribe:" + subscribe);
	     boolean leave = stub.Leave(hostIP, port);
	     System.out.println("Leave Server: "+ leave);
	} catch (Exception e) {
	    System.err.println("Client exception: " + e.toString());
	    e.printStackTrace();
	}
    }
}
