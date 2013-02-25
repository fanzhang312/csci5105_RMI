import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Test Case for the client side
 * 
 * @author Fan Zhang, Zhiqi Chen
 */
public class ClientTestCase {
	public static Client client;
	/*
	 * check if a String is an integer. This method is from the Internet.
	 * http://
	 * stackoverflow.com/questions/237159/whats-the-best-way-to-check-to-see
	 * -if-a-string-represents-an-integer-in-java
	 */
	public static boolean isInteger(String str) {
		if (str == null) {
			return false;
		}
		int length = str.length();
		if (length == 0) {
			return false;
		}
		int i = 0;
		if (str.charAt(0) == '-') {
			if (length == 1) {
				return false;
			}
			i = 1;
		}
		for (; i < length; i++) {
			char c = str.charAt(i);
			if (c <= '/' || c >= ':') {
				return false;
			}
		}
		return true;
	}
	/*
	 * testInput try to decide whether the input is legal or not
	 * based on two input situate: yes or no; 1,2,3,4,5....
	 */
	public static boolean testInput(String input, String testCase) {
		if (input == null || input.isEmpty())
			return false;
		String in = input.trim().toLowerCase();
		if (testCase.equals("yesno")) {
			if (in.equals("yes") || in.equals("no"))
				return true;
			return false;
		}
		if (testCase.equals("number")) {
			if (isInteger(in)) {
				return true;
			}
			return false;
		}
		return false;

	}
	
	/*
	 * valid IP address 
	 * This method is from Internet
	 */
	private static final String PATTERN = 
	        "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	public static boolean validate(final String ip){          
	      Pattern pattern = Pattern.compile(PATTERN);
	      Matcher matcher = pattern.matcher(ip);
	      return matcher.matches();             
	}

	/*
	 * createClient() enable user to create client
	 */
	public static void createClient(){
		Scanner input = new Scanner(System.in);
		System.out.print("Please enter the server ip(You need to run the server first): ");
		String serverIP = input.nextLine().trim();
		while(!validate(serverIP)){
			System.out.println("invalid ip address, please check input!");
			System.out.print("Please enter the server ip(You need to run the server first): ");
			serverIP = input.nextLine().trim();
		}
		System.out.print("Please enter client port: ");
		String port = input.nextLine().trim();
		while(!testInput(port,"number")){
			System.out.print("Be smart, enter a port number: ");
			port = input.nextLine().trim();
		}
		client = new Client(serverIP, Integer.parseInt(port));
		if(Client.clientCreated==false){
			System.out.println("Create client fail");
			createClient();
		}
		System.out.println("Create client success!");

	}
	
	public static void clientMenu() throws RemoteException{
		System.out.println("\nPlease select following options");
		System.out.println("1. Join");
		System.out.println("2. Leave");
		System.out.println("3. Subscribe");
		System.out.println("4. Unsubscribe");
		System.out.println("5. Publish");
		System.out.println("6. Ping");
		System.out.print("Command: ");
		Scanner option = new Scanner(System.in);
		String selection = option.nextLine().trim();
		if(!testInput(selection,"number")||Integer.parseInt(selection)>6||Integer.parseInt(selection)<1){
			System.out.println("\n Invalid input!Enter a number bewteen 1 to 6");
			return;
		}
			
		switch(Integer.parseInt(selection)){
		case 1:client.clientJoin();break;
		case 2:client.clientLeave(); break;
		case 3: {
			for(String type: Article.category){
				System.out.print(type+" ");
			}
			System.out.println("\nWhich type of article you want to subscribe: ");
			String articleType = option.nextLine().trim();
			if(Article.searchCategory(articleType)){
				if(client.clientSubscribe(articleType)){
					System.out.println("Client subscribe "+articleType+" success");
				}
			}else{
				System.out.println("Invalid Type");
			}
			break;
		}
		case 4: {
			for(String type: Article.category){
				System.out.print(type+" ");
			}
			System.out.println("\nWhich type of article you want to unsubscribe: ");
			String articleType = option.nextLine().trim();
			if(Article.searchCategory(articleType)){
				if(client.clientUnsubscribe(articleType)){
					System.out.println("Client unsubscribe "+articleType+" success");
				}
			}else{
				System.out.println("Invalid Type");
			}
			break; 
		}
		case 5: {
			System.out.println("Please enter an article follow the format: type;originator;org;contents ");
			String article = option.nextLine().trim();
			client.clientPublish(article);
			
		}break;
		case 6: client.clientPing(); break;
		default: break;
		}
		
	}
	public static void main(String[] args) throws RemoteException{
		System.out.println("\n-----------Welcome to the Client Console-----------");
		System.out.println("Author: Fan Zhang, Zhiqi Chen\n");
		System.out.println("Do you want to create a client now? Yes/No:");
		Scanner user_input = new Scanner(System.in);
		String input = user_input.nextLine().trim().toLowerCase();
		while(!testInput(input,"yesno")){
			System.out.println("\nPlease follow instruction: Just type Yes/No");
			System.out.println("Do you want to create a client now? Yes/No:");
			input = user_input.nextLine().trim().toLowerCase();
		}
		if(input.equals("yes")){
			createClient();
			while(true){
				clientMenu();
			}
		}else{
			System.out.println("Thanks for using Client Console!");
			System.exit(0);
		}
			
	}
}
