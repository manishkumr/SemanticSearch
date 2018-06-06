import java.util.LinkedHashMap;
import java.util.LinkedList;


import page_extract.GoogleResultsParser;
import page_extract.WebPageExtractor;
import user_profile.User;
import user_profile.WebPage;

public class ReRanking {
	
	static String OUTPUT_FOLDER_PATH = "./outputs/";
	static String USER_NAME = "user5";
	static String[] queries = {"container flight","truck service","vehicle service","repair","car rent ","transport service"};

	public static void main(String[] args) {
		
		
		
		Object userObject2 = Utils.readObject(OUTPUT_FOLDER_PATH+"/"+USER_NAME+"_user_profile.dat");
		User user = (User) userObject2;
		user.setGoogleResults(new LinkedHashMap<String,LinkedList<WebPage>>());
        for (String query : queries) {
			LinkedList<String> result = GoogleResultsParser.getDataFromGoogle(query);
			LinkedList<WebPage> googleResults;
			try {
				googleResults = getLinkedListOfWebPages(result);
				user.getGoogleResults().put(query, googleResults);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}

	/************************
	 * 
	 * 
	 * METHODS
	 * 
	 * 
	 * **********************/
	
	private static LinkedList<WebPage> getLinkedListOfWebPages(LinkedList<String> result) throws Exception {
		LinkedList<WebPage> googleResults = new LinkedList<WebPage>();
		for (String url : result) {
			WebPage page = new WebPage();
			page.setUrl(url);
			WebPageExtractor extractor = new WebPageExtractor();
	        String text = extractor.getText(url);
	        page.setText(text);
	        googleResults.add(page);
		}
		return googleResults;
	}
}
