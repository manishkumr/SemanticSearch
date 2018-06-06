package page_extract;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class GoogleResultsParser {
	  private static Pattern patternDomainName;
	  private static Matcher matcher;
	  private static final String DOMAIN_NAME_PATTERN
		= "([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}";
	  private static final String URL_PATTERN = "(?<=url\\?q=).*";
	  static {
			patternDomainName = Pattern.compile(URL_PATTERN);
		  }
	public static void main(String[] args) {
		
		try {
			FileReader fr = new FileReader("./user_history/UserQuery");
			BufferedReader br = new BufferedReader(fr);
			String line;
			while((line=br.readLine())!=null){
				String[] splits = line.split(",");
				String userName = splits[0];
				FileWriter fw = new FileWriter("./user_history/"+userName+"Results.txt");
				for (int i = 1; i < splits.length; i++) {
					GoogleResultsParser obj = new GoogleResultsParser();
					LinkedList<String> result = obj.getDataFromGoogle(splits[i]);
					fw.write(splits[i]+"\n");
					for(String temp : result){
						System.out.println(temp);
						fw.write(temp);
						fw.write("\n");
					}
					fw.write("\n\n");
				}
				fw.close();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		

	}
	
	public static LinkedList<String> getDataFromGoogle(String query) {

		LinkedList<String> result = new LinkedList<String>();
		String request = "https://www.google.com/search?q=" + query + "&num=30";
		System.out.println("Sending request..." + request);

		try {

			// need http protocol, set this as a Google bot agent :)
			Document doc = Jsoup
				.connect(request)
				.userAgent(
				  "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
				.timeout(5000).get();

			// get all links
			Elements elems = doc.getElementsByClass("_NId");
			Elements links = doc.select("h3[class=r]");
			for (Element link : links) {
				Node child = link.childNode(0);
				String href = child.attr("href");
				//System.out.println(child.outerHtml());
				
	                                //use regex to get domain name
					result.add(getDomainName(href));
				

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	  }
	
	 public static String getDomainName(String url){

			String domainName = "";
			matcher = patternDomainName.matcher(url);
			if (matcher.find()) {
				domainName = matcher.group(0).split("&")[0];
				System.out.println(domainName);
			}
			return domainName;

		  }

}
