import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import entity_classes.ServiceDescriptionEntity;
import entity_classes.TransportConcept;
import ontology_connection.ReadAllTransportConcepts;
import ontology_connection.ReadServiceDescriptionEntities;
import page_extract.GoogleResultsParser;
import page_extract.WebPageExtractor;
import properties.ServiceSelectionPropertiesDefaults;
import similarity.SimilarityCalculator;
import user_profile.User;
import user_profile.WebPage;
import vector_creation.FISCRM;


public class Main2 {

	public static void main(String[] args) {
		 
		initCreateDirectories();
		FISCRM fiscrm = new FISCRM();
		ArrayList<TransportConcept> concepts = null;
		//get concepts
		ReadAllTransportConcepts obj = new ReadAllTransportConcepts();
        obj.init_test();
        
        concepts = obj.query();
       
        //get SDE
      /* ReadServiceDescriptionEntities race = new ReadServiceDescriptionEntities();
       race.query();*/
        
        //vector creation for SDE
       
       String outputFolderPath = "C:\\Users\\rishi\\Documents\\workspace\\workspace\\SemanticSearch\\outputs\\New_results\\"; 
       ArrayList<ServiceDescriptionEntity> serviceEntityList = null;
       ArrayList<String> allWordListSDEdesc;
       User user = new User();
			//get word frequency for concept SDE
			 try {
				 /*
				 //concepts
				 concepts = fiscrm.getWordFrequencyForConceptSDE(concepts);
				 //serialize
				 writeObjectToFile(outputFolderPath+"user5concepts.dat", concepts);
				 System.out.println(concepts.get(0).getLinkedSDEdescWordFrequency().toString());
				 System.out.println(concepts.get(0).getSdeList().get(1).getWord_list().toString());
				
				 
				 //users
				
			     user = user.createUsers("./user_history/user5.txt","./user_history/bookmarks.txt");
				 //user = user.createUser("./user_history/user2_Airline.txt");
		         System.out.println("user name: "+user.getUserName());
		         System.out.println("Visted urls : "+ user.getUserHistory().size());
		         //extract text 
		         WebPageExtractor extractor = new WebPageExtractor();
		         extractor.extractWebPage(user, "");
		         //get word frequency for URLs
		         fiscrm.getWordFrequencyForURL(user);
		         System.out.println(user.getUserHistory().get(0).getWordFrequency());
		         
		         //write to file
		         writeObjectToFile(outputFolderPath+"user5.dat", user);
		       	  
		         //desearialize
		         Object connceptObject = readObject(outputFolderPath+"user5concepts.dat");
		         concepts = (ArrayList<TransportConcept>) connceptObject;
		         Object userObject = readObject(outputFolderPath+ "user5.dat");
		         user = (User) userObject;
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
		        	 Object connceptObject2 = readObject(outputFolderPath+"user5concepts.dat");
		        	 ArrayList<TransportConcept> concepts2 = (ArrayList<TransportConcept>) connceptObject2;
		        	 concepts2 = fiscrm.getVectorForConcepts(concepts2,mergedWordsFrequency);
		        	 url.setConcepts(concepts2);
		        	 webpages.add(url);
		        	 //calculate similarity
		         }
		        user.setUserHistory(webpages);
		        System.out.println(user.getUserHistory().get(0).getConcepts().get(0).getFiscrm_vector().length);
		        System.out.println(user.getUserHistory().get(1).getConcepts().get(0).getFiscrm_vector().length);
		        System.out.println(user.getUserHistory().get(2).getConcepts().get(0).getFiscrm_vector().length); 
		        System.out.println(user.getUserHistory().get(3).getConcepts().get(0).getFiscrm_vector().length); 
		         
		         //write to file
		         writeObjectToFile(outputFolderPath+"user5Vector.dat", user);
		        */
		         //calculate similarity
		        
       			Object userObject1 = readObject(outputFolderPath+ "user5Vector.dat");
       			user = (User) userObject1;
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
       			//write to object
       			System.out.println(user.getUserHistory().get(1).getConcepts().get(0).getSimilarityURL());
       			System.out.println(user.getUserHistory().get(2).getConcepts().get(0).getSimilarityURL());
				
       			writeObjectToFile(outputFolderPath+"user5VectorWithSimilarity.dat", user);
       			
       			/*
       			 * 
       			 * calculate concept weight
       			 * 
       			 * */
       			/*
       			//deserialize
       			Object userObject2 = readObject(outputFolderPath+ "user5VectorWithSimilarity.dat");
       			user = (User) userObject2;
       		   //calculate concept weights based on user interest
       			System.out.println("Calculating Concept Weight ....");
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
		       
		        //google == excel == 30 url
		        //get list of URLS for a query
		        
		        String[] queries = {"container flight","truck service","vehicle service","repair","car rent ","transport service"};
		        user.setGoogleResults(new LinkedHashMap<String,LinkedList<WebPage>>());
		        for (String query : queries) {
					LinkedList<String> result = GoogleResultsParser.getDataFromGoogle(query);
					LinkedList<WebPage> googleResults = getLinkedListOfWebPages(result);
					user.getGoogleResults().put(query, googleResults);
				}
		        //write to file
		        writeObjectToFile(outputFolderPath+"user5WithWebpage.dat", user);
		        */
		        /*
		         * 
		         * Reranking
		         * 
		         * 
		       
		        //read from file
		        Object userObject2 = readObject(outputFolderPath+ "user5WithWebpage.dat");
       			user = (User) userObject2;
       			ArrayList<TransportConcept> concepts1 = user.getUserHistory().get(0).getConcepts();
       			FileWriter fw = new FileWriter("resultsUser5.csv");
       			String[] queries = {"container flight","truck service","vehicle service","repair","car rent ","transport service"};
       			for (String query : queries) {
       				fw.write(query + "\n");
       				for (WebPage url : user.getGoogleResults().get(query)) {
    		        	//get URL concept vector
           				System.out.println(url.getUrl());
           				url.setWords(new LinkedList<String>());
           				url = getURLConceptVector(url,concepts1);
           				//match url with concept
           				ArrayList<TransportConcept> matchedConceppts = matchURLConceptVector(url,concepts1);
           				//find highest matching concept
    		        	TransportConcept highestSimilarityConcept = findHighestMatchingConcept(matchedConceppts);
    		        	if(highestSimilarityConcept != null){
    		        		System.out.println("\n"+highestSimilarityConcept.getName()+":"+ highestSimilarityConcept.getSimilarityURL());
    		        		Double weight = user.getConceptWeight().get(highestSimilarityConcept.getName());
    		        		//find similarity between query and highest concept Name
    		        		double queryConceptSimilarity = getSimilarityQueryAndConcept(query, highestSimilarityConcept);
    		        	    // find similarity between query and web page
    		        		double queryWebPageSimilarity = getSimilarityQueryAndwebPage(query, url);
    		        		//Find rank score
    		        		double rankScore = (queryWebPageSimilarity +0.00001) * (queryConceptSimilarity +0.00001) * weight;
    		        		System.out.println("rankScore: "+rankScore);
    		        		//fw.write(url.getUrl()+","+queryConceptSimilarity+","+queryWebPageSimilarity+","+rankScore+"\n");
    		        		fw.write(url.getUrl()+","+rankScore+"\n");
    		        	}else{
    		        		System.out.println("None");
    		        	}
    		        	
    				}
       				fw.write("\n");
				}
       			fw.close();
       			*/
			} catch (Exception e) {
				e.printStackTrace();
			}
			

	}
	private static ArrayList<TransportConcept> matchURLConceptVector(WebPage url, ArrayList<TransportConcept> concepts) {
		
		for (TransportConcept concept : concepts) {
			double[] conceptVec = concept.getFiscrm_vector();
			double[] urlVec = url.getExtendedVSM();
			SimilarityCalculator simcal = new SimilarityCalculator();
	        double similarity = simcal.getCosineSimilarity(conceptVec, urlVec);
	        concept.setSimilarityURL(similarity);
	        System.out.print(similarity+":");
		}
		return concepts;
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
	private static double getSimilarityQueryAndwebPage(String query, WebPage url) {
		String [] queryWords = query.split(" ");
		LinkedHashMap<String, Integer> queryWordFrequency = getWordFrequency(queryWords);
		String [] urlWords =  url.getWords().toArray( new String[url.getWords().size()]);
		LinkedHashMap<String, Integer> urlWordFrequency = getWordFrequency(urlWords);
		LinkedHashMap<String, Integer> mergedWordsFrequency = mergeMaps(queryWordFrequency, urlWordFrequency);
		FISCRM fiscrm = new FISCRM();
		double[] queryVector = fiscrm.getVector(queryWordFrequency,mergedWordsFrequency);
		double [] urlVector = fiscrm.getVector(urlWordFrequency,mergedWordsFrequency);
		SimilarityCalculator simcal = new SimilarityCalculator();
        double similarity = simcal.getCosineSimilarity(queryVector, urlVector);
        System.out.println("SimilarityQueryAndwebPage: "+similarity);
		return similarity;
	}
	private static double getSimilarityQueryAndConcept(String query, TransportConcept highestSimilarityConcept) {
		String [] words = query.split(" ");
		
		String [] conceptNameWords = highestSimilarityConcept.getLinkedSDEdescWordFrequency().keySet().toArray(new String [highestSimilarityConcept.getLinkedSDEdescWordFrequency().keySet().size()]);
		LinkedHashMap<String, Integer> queryWordFrequency = getWordFrequency(words);
		LinkedHashMap<String, Integer> conceptWordFrequency = getWordFrequency(conceptNameWords);
		LinkedHashMap<String, Integer> mergedWordsFrequency = mergeMaps(queryWordFrequency, conceptWordFrequency);
		FISCRM fiscrm = new FISCRM();
		double[] queryVector = fiscrm.getVector(queryWordFrequency,mergedWordsFrequency);
		double [] conceptNameVec = fiscrm.getVector(conceptWordFrequency,mergedWordsFrequency);
		SimilarityCalculator simcal = new SimilarityCalculator();
        double similarity = simcal.getCosineSimilarity(queryVector, conceptNameVec);
        System.out.println("SimilarityQueryAndConcept: "+similarity);
		return similarity;
	}
	private static LinkedHashMap<String, Integer> getWordFrequency(String[] words) {
		LinkedHashMap<String, Integer> wordFrequncyMap = new LinkedHashMap<String, Integer>();
		for (String word : words) {
			if(wordFrequncyMap.containsKey(word.toLowerCase())){
				wordFrequncyMap.put(word.toLowerCase(), wordFrequncyMap.get(word.toLowerCase())+1);
			}else{
				wordFrequncyMap.put(word.toLowerCase(), 1);
			}
		}
		return wordFrequncyMap;
	}
	/*
							 * **********************
							 * ******METHODS*********
							 * **********************
	 * */
	private static TransportConcept findHighestMatchingConcept(ArrayList<TransportConcept> concepts) {
		double similarityWeightHighest= 0.0;
		TransportConcept highestConcept = null;
		for (TransportConcept transportConcept : concepts) {
			Double similarityWeight = transportConcept.getSimilarityURL();
			if(similarityWeight > similarityWeightHighest){
				similarityWeightHighest = similarityWeight;
				highestConcept = transportConcept;
			}
		}
		return highestConcept;
	}

	private static Double findConceptSimilarity(String name, ArrayList<TransportConcept> concepts) {
		for (TransportConcept transportConcept : concepts) {
			if(transportConcept.getName().equals(name)){
				return transportConcept.getSimilarityURL();
			}
		}
		return null;
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

	public static void initCreateDirectories() {

        createDirectory(ServiceSelectionPropertiesDefaults.output_path);
        createDirectory(ServiceSelectionPropertiesDefaults.output_sim_path);
        createDirectory(ServiceSelectionPropertiesDefaults.concept_path);
        createDirectory(ServiceSelectionPropertiesDefaults.concept_mapping_path);
        createDirectory(ServiceSelectionPropertiesDefaults.entity_path);
        createDirectory(ServiceSelectionPropertiesDefaults.link_path);
    }
	
	 public static void createDirectory(String directory_path) {
	        System.out.println("directory : " + directory_path);
	        File theDir = new File(directory_path);

	        // if the directory does not exist, create it
	        if (!theDir.exists()) {

	            boolean result = theDir.mkdir();

	            if (result) {
	                System.out.println("DIR created");
	            }
	        }
	    }
	 public static void writeObjectToFile(String filename,Object obj) {
			FileOutputStream fout = null;
			ObjectOutputStream oout = null;
			try {
				fout = new FileOutputStream(filename);
				oout = new ObjectOutputStream(fout);


				oout.writeObject(obj);

				oout.close();
				fout.close();

			} catch (FileNotFoundException ex) {
				Logger.getLogger(FISCRM.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IOException ex) {
				Logger.getLogger(FISCRM.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	 public static Object readObject(String filename) {
	        FileInputStream fin = null;
	        ObjectInputStream oin = null;
	        Object obj = null;
	        try {
	            fin = new FileInputStream(filename);
	            oin = new ObjectInputStream(fin);


	             obj = oin.readObject();

	            oin.close();
	            fin.close();

	        } catch (ClassNotFoundException ex) {
	            Logger.getLogger(Main2.class.getName()).log(Level.SEVERE, null, ex);
	        } catch (IOException ex) {
	            Logger.getLogger(Main2.class.getName()).log(Level.SEVERE, null, ex);
	        }
			return obj;
	    }

}
