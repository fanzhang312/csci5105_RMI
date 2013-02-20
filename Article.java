import java.util.ArrayList;

public class Article {
	public String type, originator, org, contents;
	public static final String[] category = new String[]{"Sports", "Lifestyle", "Entertainment", "Business", "Technology", "Science", "Politics", "Health"};
	// author marks which client published the article
	public ClientModel author;
	// readerList used to mark the readers of this article
	public ArrayList<ClientModel> readerList = new ArrayList<ClientModel>();
	public Article(String cate, String originator, String org, String contents, String ip, int port){
		if(searchCategory(cate))
			type = cate;
		else
			type = null;
		this.originator = originator;
		this.org = org;
		this.contents = contents;
		author =new ClientModel(ip, port);
	}
	public boolean checkPublish(){
		if(contents != null)
			return true;
		return false;
	}
	public boolean checkSubscribe(){
		if(type != null || originator != null || org != null)
			return true;
		return false;
	}
	// Once server push an article to a client, mark the client as a reader of that article
	public boolean addReader(ClientModel client){
		if(!readerList.contains(client)){
			readerList.add(client);
			return true;
		}
		return false;
	}
	public String toString(){
		return "<"+ type +";"+ originator +";"+ org +";"+ contents+">";
	}
	public static boolean searchCategory(String cate){
		for(String s : category){
			if(s.equals(cate))
				return true;
		}
		return false;
	}
}