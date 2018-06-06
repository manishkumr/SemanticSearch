package user_profile;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class User implements Serializable{

	private String userName;

	private LinkedList<WebPage> userHistory;

	private LinkedHashSet<String> allWordList;
	
	private HashMap<String, ArrayList<String>> AllSynonymMap;
	
	private HashMap<String, ArrayList<String>> selectedSynonymMap;
	
    private LinkedHashMap<String, Integer> wordFrequency = new LinkedHashMap<String, Integer>();
    
    private LinkedHashMap<String, Double> conceptWeight = new LinkedHashMap<String, Double>();
    
	private LinkedHashMap<String, LinkedList<WebPage>> googleResults = new LinkedHashMap<String, LinkedList<WebPage>>();
	
	private static final Pattern urlPattern = Pattern.compile(
	        "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
	                + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
	                + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
	        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public LinkedList<WebPage> getUserHistory() {
		return userHistory;
	}

	public void setUserHistory(LinkedList<WebPage> userHistory) {
		this.userHistory = userHistory;
	}

	public User createUsers(String file, String bookMarkFile) {
		User user = new User();
		//set user
		user.setUserHistory(new LinkedList<WebPage>());
		user.setAllWordList(new LinkedHashSet<String>());
		user.setAllSynonymMap(new HashMap<String, ArrayList<String>>());
		user.setSelectedSynonymMap(new HashMap<String, ArrayList<String>>());
		try {
			
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String JSONline = br.readLine(); //skips header
			//create object from JSON string
			JsonElement jelement = new JsonParser().parse(JSONline);
		    JsonObject  jobject = jelement.getAsJsonObject();
		    String userName = jobject.get("name").getAsString();
		    //getbookmark
		    String [] bookmarks = getBookmark(userName,bookMarkFile);
		    user.setUserName(userName);
		    JsonArray jarray = jobject.getAsJsonArray("action");
			for (JsonElement jsonElement : jarray) {
				String url = jsonElement.getAsJsonObject().get("data").toString();
				String matchedURL = matchURL(url);
				if(matchedURL.length()>0 && !matchedURL.contains("https://www.google.com.au")){
					System.out.println(matchedURL);
					WebPage webPage = new WebPage();
					int status = findStatus(matchedURL,bookmarks);
					webPage.setStatus(status);
					webPage.setUrl(matchedURL);
					webPage.setWords(new LinkedList<String>());
					user.getUserHistory().add(webPage);
				}
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	private String[] getBookmark(String userName2, String bookMarkFile) throws IOException {
		FileReader fr = new FileReader(bookMarkFile);
		BufferedReader br =  new BufferedReader(fr);
		String line;
		while((line=br.readLine())!= null){
			String[] splits = line.split(",");
			String thisUserName = splits[0];
			if(thisUserName.equalsIgnoreCase(userName2)){
				return Arrays.copyOfRange( splits, 1, splits.length);
			}
		}
		return null;
	}
	private int findStatus(String matchedURL,String[] bookmarks){
		for (String bookmark : bookmarks) {
			if(bookmark.equalsIgnoreCase(matchedURL)){
				return 1;
			}
		}
		return 0;
		
		
	}
	private String matchURL(String url) {
		Matcher matcher = urlPattern.matcher(url);
		int matchStart = 0;
		int matchEnd = 0;
		while (matcher.find()) {
		    matchStart = matcher.start(1);
		    matchEnd = matcher.end();
		    
		}
		return url.substring(matchStart, matchEnd);
	}

	
	public LinkedHashSet<String> getAllWordList() {
		return allWordList;
	}

	public void setAllWordList(LinkedHashSet<String> allWordList) {
		this.allWordList = allWordList;
	}

	public HashMap<String, ArrayList<String>> getAllSynonymMap() {
		return AllSynonymMap;
	}

	public void setAllSynonymMap(HashMap<String, ArrayList<String>> allSynonymMap) {
		AllSynonymMap = allSynonymMap;
	}

	public HashMap<String, ArrayList<String>> getSelectedSynonymMap() {
		return selectedSynonymMap;
	}

	public void setSelectedSynonymMap(HashMap<String, ArrayList<String>> selectedSynonymMap) {
		this.selectedSynonymMap = selectedSynonymMap;
	}

	public LinkedHashMap<String, Integer> getWordFrequency() {
		return wordFrequency;
	}

	public void setWordFrequency(LinkedHashMap<String, Integer> wordFrequency) {
		this.wordFrequency = wordFrequency;
	}

	public LinkedHashMap<String, Double> getConceptWeight() {
		return conceptWeight;
	}

	public void setConceptWeight(LinkedHashMap<String, Double> conceptWeight) {
		this.conceptWeight = conceptWeight;
	}

	public User createUser(String filePath) {
		User user = new User();
		//set user
		user.setUserHistory(new LinkedList<WebPage>());
		user.setAllWordList(new LinkedHashSet<String>());
		user.setAllSynonymMap(new HashMap<String, ArrayList<String>>());
		user.setSelectedSynonymMap(new HashMap<String, ArrayList<String>>());
		FileReader fr;
		try {
			fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while((line=br.readLine())!=null){
				WebPage webPage = new WebPage();
				webPage.setStatus(0);
				webPage.setUrl(line);
				webPage.setWords(new LinkedList<String>());
				user.getUserHistory().add(webPage);
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return user;
	}

	public LinkedHashMap<String, LinkedList<WebPage>> getGoogleResults() {
		return googleResults;
	}

	public void setGoogleResults(LinkedHashMap<String, LinkedList<WebPage>> googleResults) {
		this.googleResults = googleResults;
	}

	

}
