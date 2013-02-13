
public class ClientModel {
	private String ipAddress;
	private int portNumber;
	private boolean subscribe;
	
	public ClientModel(String ip, int port){
		ipAddress = ip;
		portNumber = port;
		subscribe = false;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public int getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}
	public boolean isSubscribe() {
		return subscribe;
	}
	public void setSubscribe(boolean subscribe) {
		this.subscribe = subscribe;
	}
	public String toString(){
		return getIpAddress()+";"+Integer.toString(getPortNumber())+";";
	}

}
