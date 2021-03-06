/** 
 * ClientModel is used to store client information on the server 
 * a typical client should contain a IP address and a Port number
 * We use IP and Port to distinguish different clients
 * subscribeCategory is used to store the subscribe information for 
 * each client. It stores the category of articles.
 * 
 * @author Fan Zhang, Zhiqi Chen
 *
 */
import java.util.ArrayList;

public class ClientModel {
	private String ipAddress;
	private int portNumber;
	private boolean subscribe;
	// Use arraylist because client may subscribe more than one category of articles
	public ArrayList<String> subscribeCategory = new ArrayList<String>();
	
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
		if(subscribeCategory.size()>0)
			this.subscribe = true;
		else
			this.subscribe = false;
		return subscribe;
	}

	// client subscribe a type of articles
	public boolean sub(String type){
		// first check whether type is valid
		if(Article.searchCategory(type)){
			// then check whether client already subscribe this category of article
			if(!subscribeCategory.contains(type))
				return subscribeCategory.add(type);
		}
		return false;
	}
	// client unsubscribe a type of articles
	public boolean unsub(String type){
		if(Article.searchCategory(type)){
			if(subscribeCategory.contains(type))
				return subscribeCategory.remove(type);
		}
		return false;
	}
	public ArrayList<String> getSubscribe(){
		return subscribeCategory;
	}
	public String toString(){
		return getIpAddress()+";"+Integer.toString(getPortNumber())+";";
	}
	public String subscribeCategoryToString(){
		String category = "";
		if(!subscribeCategory.isEmpty()){
			for(String s : subscribeCategory){
				category += s + ";";
			}
		}
		return category;
	}

}
