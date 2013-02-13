public class Article {
	private String type, originator, org, contents;
	private final String[] category = new String[]{"Sports", "Lifestyle", "Entertainment", "Business", "Technology", "Science", "Politics", "Health"};
	
	public Article(int cate, String originator, String org, String contents){
		if(cate>0 && cate<9)
			type = category[cate-1];
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
}