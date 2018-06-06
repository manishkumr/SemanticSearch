import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import entity_classes.ServiceDescriptionEntity;
import entity_classes.TransportConcept;
import ontology_connection.ReadAllTransportConcepts;
import ontology_connection.ReadServiceDescriptionEntities;
import page_extract.WebPageExtractor;
import similarity.SimilarityCalculator;
import user_profile.User;
import user_profile.WebPage;
import vector_creation.FISCRM;

public class OntologicalUserProfileBuilder {
	
	static String OUTPUT_FOLDER_PATH = "./outputs/";
	
	static String USER_NAME = "user5";
	
	static String USER_HISTORY_FILE_PATH = "./user_history/";
	
	static String BOOKMARK_FILE = USER_HISTORY_FILE_PATH + "bookmarks.txt";
	
	public static void main(String[] args) {
		
		//read all transport concepts
		ArrayList<TransportConcept> concepts = null;
		ReadAllTransportConcepts obj = new ReadAllTransportConcepts();
        obj.init_test();
        concepts = obj.query();
        //read SDE
        ReadServiceDescriptionEntities race = new ReadServiceDescriptionEntities();
        race.query();
        //create vector for SDE
        
        ArrayList<ServiceDescriptionEntity> serviceEntityList = null;
        ArrayList<String> allWordListSDEdesc;
        
        
        FISCRM fiscrm = new FISCRM();
        try {
			concepts = fiscrm.getWordFrequencyForConceptSDE(concepts);
			System.out.println(concepts.get(0).getLinkedSDEdescWordFrequency().toString());
			System.out.println(concepts.get(0).getSdeList().get(1).getWord_list().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //user
        User user = new User();
        user = user.createUsers(USER_HISTORY_FILE_PATH + USER_NAME + ".txt",BOOKMARK_FILE);
        WebPageExtractor extractor = new WebPageExtractor();
        extractor.extractWebPage(user, "");
        //get word frequency for URLs
        fiscrm.getWordFrequencyForURL(user);
        System.out.println(user.getUserHistory().get(0).getWordFrequency());
        
        //get All concepts word frequency
        LinkedHashMap<String, Integer> allConceptsWordFrequency = getAllConceptsWordFrequency(concepts);
        System.out.println("all concepts word frequency: "+ allConceptsWordFrequency);
        
        //get urls
        LinkedHashMap<String, Integer> mergedWordsFrequency = null;
        LinkedList<WebPage> webpages = new LinkedList<WebPage>();
        
        for(WebPage url :user.getUserHistory()){
       	 LinkedHashMap<String, Integer> urlWordFrequency = url.getWordFrequency();
       	 //merge word frequency
       	 mergedWordsFrequency = mergeMaps(allConceptsWordFrequency,urlWordFrequency);
       	 //get vector for URL
       	 System.out.println(url.getUrl());
       	 fiscrm.getVectorForWebPage(url,mergedWordsFrequency);
 
       	 try {
				concepts = fiscrm.getVectorForConcepts(concepts,mergedWordsFrequency);
				url.setConcepts(concepts);
		       	webpages.add(url);
	       	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        user.setUserHistory(webpages);
        System.out.println(user.getUserHistory().get(0).getConcepts().get(0).getFiscrm_vector().length);
        System.out.println(user.getUserHistory().get(1).getConcepts().get(0).getFiscrm_vector().length);
        System.out.println(user.getUserHistory().get(2).getConcepts().get(0).getFiscrm_vector().length); 
        System.out.println(user.getUserHistory().get(3).getConcepts().get(0).getFiscrm_vector().length); 
        //calculate similarity between URL and concepts
        for (WebPage url : user.getUserHistory()) {
				System.out.println(url.getUrl());
			ArrayList<TransportConcept> concepts1 = url.getConcepts();
			for (TransportConcept concept : concepts1) {
				double[] conceptVec = concept.getFiscrm_vector();
				double[] urlVec = url.getExtendedVSM();
				//System.out.println(conceptVec.length + ":" + urlVec.length);
				SimilarityCalculator simcal = new SimilarityCalculator();
		        double similarity = simcal.getCosineSimilarity(conceptVec, urlVec);
		        concept.setSimilarityURL(similarity);
		        System.out.print(similarity+":");
			}
			url.setConcepts(concepts1);
			System.out.println("");
		}
        //calculate concept weights
        ArrayList<TransportConcept> concepts1 = user.getUserHistory().get(0).getConcepts();
			LinkedHashMap<String, Double> conceptWeight = new LinkedHashMap<String, Double>();
			Double totalSimilarity = 0.0;
			for (TransportConcept transportConcept : concepts1) {
			//find weight of this concepts in URLs
				Double sumSimilarity = 0.0;
				for (WebPage url : user.getUserHistory()) {
				Double similarity = findConceptSimilarity(transportConcept.getName(),url.getConcepts());
				if(url.getStatus() == 1){
					similarity = similarity *1.0;
				}else if(url.getStatus() == 0){
					similarity = similarity *0.75;
				}else{
					similarity = similarity *0.50;
				}
				sumSimilarity+=similarity;
			} 
				totalSimilarity+=sumSimilarity;
				
				//divide it by sum of all concept
				
				//System.out.println(transportConcept.getName()+":"+sumSimilarity);
				conceptWeight.put(transportConcept.getName(), sumSimilarity);
		}
			//normalize
			for (Entry<String, Double> concept : conceptWeight.entrySet()) {
			concept.setValue(concept.getValue()/totalSimilarity);
		}
        user.setConceptWeight(conceptWeight);
        Utils.writeObjectToFile(OUTPUT_FOLDER_PATH+"/"+USER_NAME+"_user_profile.dat", user);
	}
	
	private static Double findConceptSimilarity(String name, ArrayList<TransportConcept> concepts) {
		for (TransportConcept transportConcept : concepts) {
			if(transportConcept.getName().equals(name)){
				return transportConcept.getSimilarityURL();
			}
		}
		return null;
	}
	
	private static LinkedHashMap<String, Integer> getAllConceptsWordFrequency(ArrayList<TransportConcept> concepts) {
		LinkedHashMap<String, Integer> wordFrequncyMap = new LinkedHashMap<>();
		for (TransportConcept transportConcept : concepts) {
			LinkedHashMap<String, Integer> linkedSDEwordFrequency = transportConcept.getLinkedSDEdescWordFrequency();
			for (Entry<String, Integer> entry : linkedSDEwordFrequency.entrySet()) {
				String word = entry.getKey();
				Integer frequency = entry.getValue();
				if(wordFrequncyMap.containsKey(word)){
					wordFrequncyMap.put(word, wordFrequncyMap.get(word)+frequency);
				}else{
					wordFrequncyMap.put(word, frequency);
				}
			}
		}
		return wordFrequncyMap;
	}
	
	private static WebPage getURLConceptVector(WebPage url, ArrayList<TransportConcept> concepts) throws Exception {
		System.out.println(url.getUrl());
		LinkedHashMap<String, Integer> allConceptsWordFrequency = getAllConceptsWordFrequency(concepts);
		FISCRM fiscrm = new FISCRM();
		url = fiscrm.getWordFrequencyForURL(url);
		LinkedHashMap<String, Integer> mergedWordsFrequency = mergeMaps(allConceptsWordFrequency,url.getWordFrequency());
    	url = fiscrm.getVectorForWebPage(url,mergedWordsFrequency);
    	concepts = fiscrm.getVectorForConcepts(concepts,mergedWordsFrequency);
   	 	url.setConcepts(concepts);
		return url;
	}
	
	private static LinkedHashMap<String, Integer> mergeMaps(LinkedHashMap<String, Integer> allConceptsWordFrequency,
			LinkedHashMap<String, Integer> urlWordFrequency) {
		//System.out.println("concepts word Frequency: "+allConceptsWordFrequency.size());
		//System.out.println("all Word frequency: "+urlWordFrequency.size());
		LinkedHashMap<String, Integer> mergedMap = new LinkedHashMap<String, Integer>();
		mergedMap.putAll(allConceptsWordFrequency);
		mergedMap.putAll(urlWordFrequency);
		//System.out.println("merged word frequency: "+mergedMap.size());
		return mergedMap;
	}
	
	
}
