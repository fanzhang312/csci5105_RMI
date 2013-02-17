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
		return subscribe;
	}
	public void setSubscribe(boolean subscribe) {
		this.subscribe = subscribe;
	}
	public boolean sub(String type){
		// first check whether type is valid
		if(Article.searchCategory(type)){
			// then check whether client already subscribe this category of article
			if(!subscribeCategory.contains(type))
				return subscribeCategory.add(type);
		}
		return false;
	}
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
