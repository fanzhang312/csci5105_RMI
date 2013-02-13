package communicate;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Communicate extends Remote 
{
	boolean JoinServer (String IP, int Port) throws RemoteException;
	boolean LeaveServer (String IP, int Port) throws RemoteException;
	boolean Join (String IP, int Port) throws RemoteException;
	boolean Leave (String IP, int Port) throws RemoteException;
	boolean Subscribe(String IP, int Port, String Article) throws RemoteException;
	boolean Unsubscribe (String IP, int Port, String Article) throws RemoteException;
	boolean Publish (String Article, String IP, int Port) throws RemoteException;
	boolean Ping () throws RemoteException;
}
