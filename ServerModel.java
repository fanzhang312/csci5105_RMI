/** 
 * ServerModel is used to store server information: IP, BindingName, Port
 * 
 * @author Fan Zhang, Zhiqi Chen
 *
 */
public class ServerModel {
	String ip;
	String bindingName;
	int port;
	public ServerModel(String ip, String bindingName, String port){
		this.ip = ip.trim();
		this.bindingName = bindingName.trim();
		this.port = Integer.parseInt(port.trim());
	}
	public String toString(){
		return ip+";"+"bindingName"+";"+port;
	}
}
