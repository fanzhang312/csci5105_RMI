import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private Client() {}

    public static void main(String[] args) {

	try {
		// locate the registry on 10.0.0.8
	    Registry registry = LocateRegistry.getRegistry("10.0.0.8",1099);
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
	     stub.Publish("Sports;someone;UMN;Hello World!", hostIP, port);
	     System.out.println("Subscribe:" + subscribe);
	     PeriodicChecker check = new PeriodicChecker(stub);
	     check.start();
	     boolean leave = stub.Leave(hostIP, port);
	     System.out.println("Leave Server: "+ leave);
	} catch (Exception e) {
	    System.err.println("Client exception: " + e.toString());
	    e.printStackTrace();
	}
    }
}
