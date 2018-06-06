package page_extract;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import user_profile.User;
import user_profile.WebPage;

public class WebPageExtractor {


	public  User extractWebPage(User user, String element) {

		
			for (WebPage webPage : user.getUserHistory()) {
				String url = webPage.getUrl();
				
				String text = "";
					Document doc;
					try {
						System.out.println("getting text for: " + url);
						Response res = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36").timeout(5000).ignoreHttpErrors(true).followRedirects(true).execute();
						doc = res.parse();
						String title = doc.title();
						System.out.println(title);
						text = doc.body().text();
						webPage.setText(text);
						System.out.println(text);
					} catch (Exception e) {
						System.out.println("error: "+e.getMessage());
						webPage.setText(text);
					}
					//get text from body
			}
		return user;
	}

	public String getText(String url) throws IOException{
		String text = "";
		Document doc;
		try {
			System.out.println("getting text for: " + url);
			Response res = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36").timeout(5000).ignoreHttpErrors(true).followRedirects(true).execute();
			doc = res.parse();
			String title = doc.title();
			System.out.println(title);
			text = doc.body().text();
		} catch (Exception e) {
			System.out.println("error: "+e.getMessage());
		}
		return text;

	}

}
