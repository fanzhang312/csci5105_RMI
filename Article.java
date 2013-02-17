public class Article {
	public String type, originator, org, contents;
	public static final String[] category = new String[]{"Sports", "Lifestyle", "Entertainment", "Business", "Technology", "Science", "Politics", "Health"};
	
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