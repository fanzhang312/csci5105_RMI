import java.rmi.RemoteException;

/*
 * Test Case for the client side
 * 
 * @author Fan Zhang, Zhiqi Chen
 */
public class ClientTestCase {
	public static void main(String[] args) throws RemoteException{
		Client client = new Client(args[0], 2000);
		client.clientJoin();
		client.clientPing();
		client.clientSubscribe("Sports");
		client.clientSubscribe("Science");
		client.clientUnsubscribe("Science");
//		client.serverJoin();
		// client.clientLeave();
		client.clientPublish("Sports;fan;UMN;Hello World");
		client.clientPublish("Business;fan;UMN;Who moved my cheese");
		Client client2 = new Client(args[0], 2001);
		client2.clientJoin();
		client2.clientPing();
		client2.clientSubscribe("Sports");
		// client2.clientSubscribe("Science");
		client2.clientUnsubscribe("Science");
	}
}
