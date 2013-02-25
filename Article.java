import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Article {
	public String type, originator, org, contents;
	public static final String[] category = new String[]{"Sports", "Lifestyle", "Entertainment", "Business", "Technology", "Science", "Politics", "Health"};
	// author marks which client published the article
	public ClientModel author;
	// readerList used to mark the readers of this article
	public ArrayList<ClientModel> readerList = new ArrayList<ClientModel>();
	// construct an article with author information
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
	// construct an article without author information
	public Article(String cate, String originator, String org, String contents){
		if(searchCategory(cate))
			type = cate;
		else
			type = null;
		this.originator = originator;
		this.org = org;
		this.contents = contents;
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
		if(cate==null||cate.isEmpty()){
			return false;
		}
		for(String s : category){
			if(s.equals(cate))
				return true;
		}
		return false;
	}
	// Check whether an article is OK for publish
	public static boolean checkArticle(String article){
		if(!checkSemicolon(article)){
			System.out.println("semicolon problem");
			return false;
		}
			
		String[] fields = article.split(";");
		if(fields.length!=4){
			System.out.println("Format not accept");
			return false;
		}
		if(fields[0]==null||fields[0].isEmpty()){
			// No content is not allowed to publish
			if(fields[3]==null || fields[3].isEmpty()){
				System.out.println("content can't be empty");
				return false;
			}
			return true;
		}
		// if has category, then it must match given categroy
		if(!searchCategory(fields[0])){
			System.out.println("category doesn't match");
			return false;
		}
		// No content is not allowed to publish
		if(fields[3]==null || fields[3].isEmpty()){
			System.out.println(fields[3]);
			return false;
		}
		return true;
	}
	// An article must contains 3 semicolons
	public static boolean checkSemicolon(String article){
		Pattern pattern = Pattern.compile(";");
		Matcher matcher = pattern.matcher(article);
		int count = 0;
		while( matcher.find())
			count++;
		if(count==3)
			return true;
		return false;
	}
}