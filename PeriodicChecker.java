import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Date;

// PeriodicChecker use a thread to periodically call the ping() and note the current time
// interval time is set to be 5 seconds 
public class PeriodicChecker extends Thread {
	Communicate stub;

	public PeriodicChecker(Communicate stub) {
		this.stub = stub;
	}

	@Override
	public void run() {
		while (true) {
			try {
				boolean status = stub.Ping();
				Date date = new Date();
				Timestamp time = new Timestamp(date.getTime());
				System.out.println("\nServer is alive: "+status+" at "+ time + " Next ping will be sent out in 1 minute");
				Thread.sleep(60000); // Delay 60s
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

}
