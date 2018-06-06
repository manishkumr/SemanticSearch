/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vector_creation;

import entity_classes.ServiceDescriptionEntity;
import entity_classes.TransportConcept;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.Synset;
import properties.ServiceSelectionPropertiesDefaults;
import user_profile.User;
import user_profile.WebPage;
import wordnet.WordNetUtilities;

/**
 *
 * @author kikitt
 */
public class FISCRM {

	private ArrayList<String> all_word_list = new ArrayList<>();
	private ArrayList<ServiceDescriptionEntity> serviceEntityList = new ArrayList<>();
	private ArrayList<String> stopword = new ArrayList<>();
	private WordNetUtilities wordnet_util = new WordNetUtilities();
	private ArrayList<String> strong_words = new ArrayList<>();
	private ArrayList<String> weak_words = new ArrayList<>();
	private HashMap<String, ArrayList<String>> selected_synonym_map = new HashMap<>();
	private HashMap<String, ArrayList<String>> all_synonym_map = new HashMap<>();
	private ArrayList<TransportConcept> conceptObjList = new ArrayList<>();
	private ArrayList<String> global_word_list = new ArrayList<>();
	
	public ArrayList<String> getAllWordList() {
		return all_word_list;
	}

	public void defineValueForTest() {
		// create all_word_list
		all_word_list.add("car");
		all_word_list.add("automobile");
		all_word_list.add("desk");
		all_word_list.add("table");
		all_word_list.add("chart");

		//create 3 services
		ServiceDescriptionEntity s1 = new ServiceDescriptionEntity();
		s1.setSDEName("car car");
		ServiceDescriptionEntity s2 = new ServiceDescriptionEntity();
		s2.setSDEName("automobile automobile automobile");
		ServiceDescriptionEntity s3 = new ServiceDescriptionEntity();
		s3.setSDEName("desk table");

		ArrayList<String> word_list1 = new ArrayList<>();
		word_list1.add("car");
		word_list1.add("car");
		s1.setWord_list(word_list1);

		int[] vsm1 = new int[5];
		vsm1[0] = 2;
		vsm1[1] = 0;
		vsm1[2] = 0;
		vsm1[3] = 0;
		vsm1[4] = 0;
		s1.setVsm(vsm1);

		double[] fiscrm1 = new double[5];
		s1.setFiscrm_vector(fiscrm1);

		// ************************************************

		ArrayList<String> word_list2 = new ArrayList<>();
		word_list2.add("automobile");
		word_list2.add("automobile");
		word_list2.add("automobile");
		s2.setWord_list(word_list2);

		int[] vsm2 = new int[5];
		vsm2[0] = 0;
		vsm2[1] = 3;
		vsm2[2] = 0;
		vsm2[3] = 0;
		vsm2[4] = 0;
		s2.setVsm(vsm2);

		double[] fiscrm2 = new double[5];
		s2.setFiscrm_vector(fiscrm2);

		// *************************************************

		ArrayList<String> word_list3 = new ArrayList<>();
		word_list3.add("desk");
		word_list3.add("table");
		s3.setWord_list(word_list3);

		int[] vsm3 = new int[5];
		vsm3[0] = 0;
		vsm3[1] = 0;
		vsm3[2] = 1;
		vsm3[3] = 1;
		vsm3[4] = 0;
		s3.setVsm(vsm3);

		double[] fiscrm3 = new double[5];
		s3.setFiscrm_vector(fiscrm3);

		// create strong word list
		strong_words.add("car");
		strong_words.add("automobile");
		strong_words.add("desk");
		strong_words.add("chart");

		// create weak word list
		weak_words.add("table");

		// create synonym_map
		ArrayList<String> syn1 = new ArrayList<>();
		ArrayList<String> syn2 = new ArrayList<>();
		ArrayList<String> syn3 = new ArrayList<>();
		ArrayList<String> syn4 = new ArrayList<>();
		ArrayList<String> syn5 = new ArrayList<>();

		ArrayList<String> syn6 = new ArrayList<>();
		ArrayList<String> syn7 = new ArrayList<>();
		ArrayList<String> syn8 = new ArrayList<>();
		ArrayList<String> syn9 = new ArrayList<>();
		ArrayList<String> syn10 = new ArrayList<>();

		syn1.add("automobile");
		syn2.add("car");
		syn3.add("table");
		syn3.add("chart");
		syn4.add("desk");
		syn4.add("chart");
		syn5.add("table");

		// create serviceEntityList
		serviceEntityList.add(s1);
		serviceEntityList.add(s2);
		serviceEntityList.add(s3);


		selected_synonym_map.put("car", syn1);
		selected_synonym_map.put("automobile", syn2);
		selected_synonym_map.put("desk", syn3);
		selected_synonym_map.put("table", syn4);
		selected_synonym_map.put("chart", syn5);

		syn6.add("automobile");
		syn6.add("vehicle");
		syn7.add("car");
		syn7.add("vehicle");
		syn8.add("table");
		syn8.add("chart");

		syn9.add("desk");
		syn9.add("chart");
		syn10.add("table");

		syn10.add("diagram");

		//        syn1.add("automobile");
		//        syn2.add("car");
		//        syn3.add("table");
		//        syn3.add("chart");
		//        syn4.add("desk");
		//        syn4.add("chart");
		//        syn5.add("table");
		//        syn5.add("desk");

		all_synonym_map.put("car", syn6);
		all_synonym_map.put("automobile", syn7);
		all_synonym_map.put("desk", syn8);
		all_synonym_map.put("table", syn9);
		all_synonym_map.put("chart", syn10);

	}

	public void init() throws Exception {
		wordnet_util.init();

		readServiceEntityFromFile(ServiceSelectionPropertiesDefaults.sde_entity_file);
		//stemming();
		//nonstemmingService(); // get the word list from SDE name & descriptions

		// new adding
		//            readConceptObjectFromFile(all_concept_file);
		if (ServiceSelectionPropertiesDefaults.focusAllConcept) {
			readConceptObjectFromFile(ServiceSelectionPropertiesDefaults.all_transport_object_concept_file);

		} else {
			readConceptObjectFromFile(ServiceSelectionPropertiesDefaults.leaf_transport_object_concept_file);

		}
		//stemming();
		if (ServiceSelectionPropertiesDefaults.sde_concept_sim.equals("cosine")
				|| ServiceSelectionPropertiesDefaults.query_concept_sim.equals("cosine")) {
			nonstemmingConcept(); // get the word list from Concept descriptions.
		}

		createVSMService(all_word_list, serviceEntityList);

		if (ServiceSelectionPropertiesDefaults.sde_concept_sim.equals("cosine")
				|| ServiceSelectionPropertiesDefaults.query_concept_sim.equals("cosine")) {
			createVSMConcept(all_word_list, conceptObjList);
		}

		getSelectedSynonymForAllWordsList(all_word_list);

		readjustmentAllElementService(serviceEntityList);
		
		if (ServiceSelectionPropertiesDefaults.sde_concept_sim.equals("cosine")
				|| ServiceSelectionPropertiesDefaults.query_concept_sim.equals("cosine")) {
			readjustmentAllElementConcept(conceptObjList);
		}


		writeServiceEntityFISCRM(ServiceSelectionPropertiesDefaults.sde_entity_file);
		writeConceptFISCRM(ServiceSelectionPropertiesDefaults.output_transport_object_concept_file);

		writeAllNeccessayForFISCRM(ServiceSelectionPropertiesDefaults.service_neccessary_file);

		//        Test FISCRM Vectors
		//        AircraftEntityForFISCRM service  = aircraftEntityFISCRMList.get(2);
		//        System.out.println(" service indx 2 ");
		//        int indx = 0;
		//        for (int i : service.getFiscrm_vector()){
		//            System.out.println(all_word_list.get(indx) + " : " + i);
		//            indx++;
		//        }



	}

	public void getVectorForConcepts(){
		wordnet_util.init(); 
		readConceptObjectFromFile(ServiceSelectionPropertiesDefaults.all_transport_object_concept_file);
		//get word list
		nonstemmingConcept();
		createVSMConcept(all_word_list, conceptObjList);
	}
	public ArrayList<TransportConcept> getWordFrequencyForConceptSDE(ArrayList<TransportConcept> concepts) throws Exception{
		wordnet_util.init();
		
		for (TransportConcept transportConcept : concepts) {
			//write concept name
		
			System.out.println("Concept Name: "+transportConcept.getName());
			nonstemmingService(transportConcept.getSdeList());
			//get count of words for SDE
			countWordFrequency(transportConcept);
			//sort 
			LinkedHashMap<String, Integer> sortedmap = getSortedMap(transportConcept.getLinkedSDEdescWordFrequency());
			//System.out.println("unsorted: "+transportConcept.getLinkedSDEdescWordFrequency());
			System.out.println("sorted:" + sortedmap);
			//write word, Frequency e.g "catch	2	train	2	plane	2"
			/*Iterator<Entry<String, Integer>> iter = sortedmap.entrySet().iterator();
			int i =1;
			while (iter.hasNext()) {
				Entry<String, Integer> elem = iter.next();
				fw.write(elem.getKey() + "," + elem.getValue()+"," );
				if(i>3){
					break;
				}
				i++;
			}
			
*/		}
	
		return concepts;
	}
	public void getWordFrequencyForURL(User user) {
		wordnet_util.init();
		for(WebPage webPage :user.getUserHistory()){
			getWebPageWordList(webPage);
			countWordFrequencyWebPage(webPage);
		}
	}
	public WebPage getWordFrequencyForURL(WebPage url) {
		wordnet_util.init();
		url = getWebPageWordList(url);
		countWordFrequencyWebPage(url);
		return url;
	}

	private void countWordFrequencyWebPage(WebPage webPage) {
		LinkedHashMap<String, Integer> wordmap = webPage.getWordFrequency();
		for (String word : webPage.getWords()) {
				if(wordmap.containsKey(word)){
					wordmap.put(word, wordmap.get(word)+1);
				}else{
					wordmap.put(word, 1);
				}
			
		}
		
	}

	private LinkedHashMap<String, Integer> getSortedMap(LinkedHashMap<String, Integer> map) {
		List<Map.Entry<String, Integer>> entries =
				  new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
				Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
				  public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b){
				    return b.getValue().compareTo(a.getValue());
				  }
				});
				LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
				for (Map.Entry<String, Integer> entry : entries) {
				  sortedMap.put(entry.getKey(), entry.getValue());
				}
		return sortedMap;
	}

	private void countWordFrequency(TransportConcept transportConcept) {
		// add words to map
		LinkedHashMap<String, Integer> wordmap = transportConcept.getLinkedSDEdescWordFrequency();
		for (ServiceDescriptionEntity sde : transportConcept.getSdeList()) {
			ArrayList<String> wordList = sde.getWord_list();
			for (String word : wordList) {
				if(wordmap.containsKey(word)){
					wordmap.put(word, wordmap.get(word)+1);
				}else{
					wordmap.put(word, 1);
				}
			}
		}
		
	}

	public ArrayList<TransportConcept> getVectorForConcepts(ArrayList<TransportConcept> concepts, LinkedHashMap<String, Integer> mergedWordsFrequency) throws Exception {
		wordnet_util.init();
		//all_word_list.addAll(allWordList);
		LinkedList<String> allWords = getKeyAsList(mergedWordsFrequency.keySet());
		all_word_list = new ArrayList<String>(allWords);
		strong_words = all_word_list;
		System.out.println(all_word_list.size());
		getSelectedSynonymForAllWordsList(all_word_list);
		for (TransportConcept transportConcept : concepts) {
			//get sdeList
			ArrayList<ServiceDescriptionEntity> sdeList = transportConcept.getSdeList();
			//System.out.println("Concept Name : " + transportConcept.getName() + " SDE List size: " + sdeList.size());
			//nonstemmingService(sdeList);
			int[] conceptVsm = createVSMService(all_word_list, sdeList);
			transportConcept.setVsm(conceptVsm);
			
			readjustmentConcept(transportConcept);
		}
		//createVSMService(all_word_list, serviceEntityList);
		
		return concepts;
	}
	
	public WebPage getVectorForWebPage(WebPage url, LinkedHashMap<String, Integer> mergedWordsFrequency) {
		wordnet_util.init();
		//create all word list for user
		
			
			createVSMurl(url,mergedWordsFrequency);
			//get sysnonym from word net
			getSelectedSynonymForAllWordsList(url,mergedWordsFrequency);
			// re-adjustment
			reAdjustWebPages(url,mergedWordsFrequency);
			
		
		return url;
	}
	
	public double[] getVector(LinkedHashMap<String, Integer> queryWordFrequency,
			LinkedHashMap<String, Integer> mergedWordsFrequency) {
		int [] vsmQuery = createVSMQuery(queryWordFrequency,mergedWordsFrequency);
		double[] extendedVSM = reAdjustQuery(vsmQuery,mergedWordsFrequency);
		return extendedVSM;
	}
	private double[] reAdjustQuery(int[] vsmQuery, LinkedHashMap<String, Integer> mergedWordsFrequency) {
		LinkedList<String> allWords = getKeyAsList(mergedWordsFrequency.keySet());
		strong_words = new ArrayList<String>(allWords);
		HashMap<String, ArrayList<String>> SelectedSynonymMap = new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<String>> AllSynonymMap = new HashMap<String, ArrayList<String>>();
		for (String word : allWords) {
			try {
				// get synonym set for each word
				ArrayList<String> synset = wordnet_util.lookupSynonyms(word);
				// select only terms in all word list
				ArrayList<String> selected_synset = new ArrayList<>();
				for (String syn : synset) {
					if (allWords.contains(syn.toLowerCase())) {
						selected_synset.add(syn.toLowerCase());
					}
				}
				AllSynonymMap.put(word, synset);
				SelectedSynonymMap.put(word, selected_synset);
			} catch (JWNLException ex) {
				Logger.getLogger(FISCRM.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		int[] vsm = vsmQuery;
		double[] extendedVSM = new double[vsm.length];
		for (int i = 0; i < extendedVSM.length; i++) {
			extendedVSM[i] = vsm[i];
		}
		ArrayList<String> allWordsList = new ArrayList<String>(allWords);
		//
		for (String term : allWords) {
	           
			// get synonym list of this term
			ArrayList<String> synonym_list = SelectedSynonymMap.get(term);
			ArrayList<String> strong_synlist = getStrongSynonymList(synonym_list);

		
			int term_indx = getIndex(allWords, term);
			//ArrayList<String> wordList = new ArrayList<String>(url.getWords());

			if ((vsm[term_indx] > 0)) {
				// get cooccurrences of all synonym words : C(term)
				int occur = getCooccurrenceSyn(vsm, term, strong_synlist, allWordsList);
				// get #words that represent the same concept --> # synonym words : T(term)
				int synnum = strong_synlist.size() + 1;

				double weight = occur * Math.sqrt(1.0 / synnum);

				extendedVSM = adjustWeight(extendedVSM, term, weight+occur, allWordsList);
				for (String syn : strong_synlist) {
					int occur_syn = getCooccurrenceSyn(vsm, syn, strong_synlist, allWordsList);
					extendedVSM = adjustWeight(extendedVSM, syn, weight+occur_syn, allWordsList);
				}

			}
		}
		
		/*for (int i = 0; i < extendedVSM.length; i++) {
			System.out.print(allWords.get(i)+":"+extendedVSM[i]+":");
		}
		System.out.println();*/
		return extendedVSM;
	}

	private int[] createVSMQuery(LinkedHashMap<String, Integer> queryWordFrequency,
			LinkedHashMap<String, Integer> mergedWordsFrequency) {
		LinkedList<String> allWords = getKeyAsList(mergedWordsFrequency.keySet());
		//LinkedList<String> allWords = (LinkedList<String>) mergedWordsFrequency.keySet();
		Set<String> word_list = queryWordFrequency.keySet();
		
		int[] vsm = new int[mergedWordsFrequency.size()];

		for (String word : word_list) {
			int index = getIndex(allWords, word);
			//System.out.print(word + ":");
			//System.out.print(index);
			vsm[index] = queryWordFrequency.get(word);
		}
		
		/*System.out.println("\nprinting VSM for query");
		
		for (int i = 0; i < vsm.length; i++) {
			System.out.print(allWords.get(i)+":"+vsm[i]+":");
		}
		System.out.println();*/
		
		return vsm;
	}

	private void reAdjustWebPages(WebPage url, LinkedHashMap<String, Integer> mergedWordsFrequency) {
			LinkedList<String> allWords = getKeyAsList(mergedWordsFrequency.keySet());
			strong_words = new ArrayList<String>(allWords);
			int[] vsm = url.getVsm();
			double[] extendedVSM = new double[vsm.length];
			for (int i = 0; i < extendedVSM.length; i++) {
				extendedVSM[i] = vsm[i];
			}
			ArrayList<String> allWordsList = new ArrayList<String>(allWords);
			//
			for (String term : allWords) {
		           
				// get synonym list of this term
				ArrayList<String> synonym_list = url.getSelectedSynonymMap().get(term);
				ArrayList<String> strong_synlist = getStrongSynonymList(synonym_list);

			
				int term_indx = getIndex(allWords, term);
				//ArrayList<String> wordList = new ArrayList<String>(url.getWords());

				if ((vsm[term_indx] > 0)) {
					// get cooccurrences of all synonym words : C(term)
					int occur = getCooccurrenceSyn(vsm, term, strong_synlist, allWordsList);
					// get #words that represent the same concept --> # synonym words : T(term)
					int synnum = strong_synlist.size() + 1;

					double weight = occur * Math.sqrt(1.0 / synnum);

					extendedVSM = adjustWeight(extendedVSM, term, weight+occur, allWordsList);
					for (String syn : strong_synlist) {
						int occur_syn = getCooccurrenceSyn(vsm, syn, strong_synlist, allWordsList);
						extendedVSM = adjustWeight(extendedVSM, syn, weight+occur_syn, allWordsList);
					}

				}
			}
			url.setExtendedVSM(extendedVSM);
			for (int i = 0; i < extendedVSM.length; i++) {
				System.out.print(allWords.get(i)+":"+extendedVSM[i]+":");
			}
			System.out.println();
			
	}

	private void reAdjustWebPage(WebPage page) {
		int[] vsm = page.getVsm();
		double[] evsm = page.getExtendedVSM();
		for (String term : page.getWords()) {
			           
			// get synonym list of this term
			ArrayList<String> synonym_list = page.getSelectedSynonymMap().get(term);
			ArrayList<String> strong_synlist = getStrongSynonymList(synonym_list);

		
			int term_indx = getIndex(page.getWords(), term);
			 ArrayList<String> wordList = new ArrayList<String>(page.getWords());

			if ((vsm[term_indx] > 0)) {
				// get cooccurrences of all synonym words : C(term)
				int occur = getCooccurrenceSyn(vsm, term, strong_synlist, wordList);
				// get #words that represent the same concept --> # synonym words : T(term)
				int synnum = strong_synlist.size() + 1;

				double weight = occur * Math.sqrt(1.0 / synnum);

				evsm = adjustWeight(evsm, term, weight, wordList);
				for (String syn : strong_synlist) {
					evsm = adjustWeight(evsm, syn, weight, wordList);
				}

			}
			/*
			else if ((vsm[term_indx] > 0) && isWeakWord(term)) {
				// a weak word --> get index of a weak term = term_indx                
				// C(w)--> occurrence of the weak word w
				int cw = vsm[term_indx];
				//                int cs = getCooccurrenceSyn(vsm, term, strong_synlist, all_word_list);
				double sd_sum = calSDSum(strong_synlist, term, vsm, all_word_list);

				//                System.out.println("term : " + term);

				for (String strong_word : strong_synlist) {

					int strong_indx = all_word_list.indexOf(strong_word);
					int cs = vsm[strong_indx];

					if (cs > 0) { // select only the strong word presenting in the document
						double sd_ws = calSynonymyDegree(term, strong_word, selected_synonym_map);
						//                        double sd_sw = calSynonymyDegree(strong_word, term, selected_synonym_map);
						double strong_weight = cs + (cw * Math.sqrt(sd_ws / (1 + sd_sum)));



						fiscrm[strong_indx] = strong_weight;

						//                    System.out.println("strong term : " + strong_word);
						//                    System.out.println("sd_sw : " + sd_sw);
						//                    System.out.println("sd_sum : " + sd_sum);
						//                    System.out.println("cw : " + cw);
						//                    System.out.println("cs : " + cs);
						//                    System.out.println("strong weight : " + strong_weight);
						//                    System.out.println("=================================================");

					}

				}

				double weak_weight = cw * Math.sqrt(1 / (1 + sd_sum));
				fiscrm[term_indx] = weak_weight;
				//                System.out.println("term : " + term);
				//                System.out.println("weak_weight : " + weak_weight);
				//                System.out.println("=================================================");

			}*/
		}
		
	}

	private void getSelectedSynonymForAllWordsList(WebPage webPage, LinkedHashMap<String, Integer> mergedWordsFrequency) {
		LinkedList<String> allWords = getKeyAsList(mergedWordsFrequency.keySet());
		webPage.setAllSynonymMap(new HashMap<String, ArrayList<String>>());
		webPage.setSelectedSynonymMap(new HashMap<String, ArrayList<String>>());
		for (String word : allWords) {
			try {
				// get synonym set for each word
				ArrayList<String> synset = wordnet_util.lookupSynonyms(word);
				// select only terms in all word list
				ArrayList<String> selected_synset = new ArrayList<>();
				for (String syn : synset) {
					if (allWords.contains(syn.toLowerCase())) {
						selected_synset.add(syn.toLowerCase());
					}
				}
				webPage.getAllSynonymMap().put(word, synset);
				webPage.getSelectedSynonymMap().put(word, selected_synset);
			} catch (JWNLException ex) {
				Logger.getLogger(FISCRM.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		
	}

	private void getAllwordList(User user) {
		LinkedHashSet<String> allWordList = user.getAllWordList();
		for (WebPage webPage : user.getUserHistory()) {
			allWordList.addAll(webPage.getWords());
		}
		
	}

	private void createVSMurl(WebPage url,LinkedHashMap<String, Integer> mergedWordsFrequency) {
		
			LinkedList<String> allWords = getKeyAsList(mergedWordsFrequency.keySet());
			//LinkedList<String> allWords = (LinkedList<String>) mergedWordsFrequency.keySet();
			LinkedList<String> word_list = getKeyAsList(url.getWordFrequency().keySet());
			url.setWords(word_list);
			int[] vsm = new int[mergedWordsFrequency.size()];

			for (String word : word_list) {
				int index = getIndex(allWords, word);
				//System.out.print(word + ":");
				//System.out.print(index);
				vsm[index] = url.getWordFrequency().get(word);
			}
			url.setVsm(vsm);
			System.out.println("\nprinting VSM for URL");
			
			for (int i = 0; i < vsm.length; i++) {
				System.out.print(allWords.get(i)+":"+vsm[i]+":");
			}
			System.out.println();
			
		
		
	}
	private LinkedList<String> getKeyAsList(Set<String> keySet) {
		LinkedList<String> keyList = new LinkedList<String>();
		for (String word : keySet) {
			keyList.add(word);
		}
		return keyList;
	}

	private ArrayList<String> mergeUserandallWordList(LinkedHashSet<String> allWordList, ArrayList<String> all_word_list2) {
		//remove duplicates
		all_word_list2.removeAll(allWordList);
		all_word_list2.addAll(allWordList);
		Collections.sort(all_word_list2);
		return all_word_list2;
	}

	public static int getIndex(LinkedList<String> mergedList, Object value) {
		   int result = 0;
		   for (Object entry:mergedList) {
		     if (entry.equals(value)) return result;
		     result++;
		   }
		   return -1;
		 }
	private WebPage getWebPageWordList(WebPage page) {
		createStopwordList();
		
			//stop word removal
			String webpageTextStopWordsRemoved = removeStopword(page.getText());
			String[] words = webpageTextStopWordsRemoved.split(" ");
			for (String word : words) {
				String word_without_suffix = deleteSuffixNoun(word);
				Synset[] senses = wordnet_util.getWordSenses(word_without_suffix);
				if (senses != null) {
					page.getWords().add(word_without_suffix);
				} else {
					//using original word
					senses = wordnet_util.getWordSenses(word);
					if (senses != null) {
						page.getWords().add(word);
					} else {
						String word_without_ing = "";
						if (word.endsWith("ing")) {
							word_without_ing = word.substring(0, word.length() - 3);

							senses = wordnet_util.getWordSenses(word_without_ing);

							if (senses != null) {
								page.getWords().add(word_without_ing);
							} else {
								word_without_ing += "e";
								senses = wordnet_util.getWordSenses(word_without_ing);
								if (senses != null) {
									page.getWords().add(word_without_ing);
								}
							}

						}
					}
				}
			}
			//
	
		return page;
	}

	//####################################################################    
	//######################## Readjustment method #######################
	//####################################################################
	public void readjustmentAllElementService(ArrayList<ServiceDescriptionEntity> serviceEntityList) {
		for (ServiceDescriptionEntity each_service : serviceEntityList) {
			// clone vsm to fiscrm vector
			int[] vsm = each_service.getVsm();
			if(vsm == null){
				continue;
			}
			double[] fiscrm_vector = new double[vsm.length];
			for (int i = 0; i < fiscrm_vector.length; i++) {
				fiscrm_vector[i] = vsm[i];
			}
			each_service.setFiscrm_vector(fiscrm_vector);

			// readjust fiscrm vector for each service
			double[] adjusted_fiscrm = readjustment(each_service.getVsm(), each_service.getFiscrm_vector());
			each_service.setFiscrm_vector(adjusted_fiscrm);
		}
	}
	private void readjustmentConcept(TransportConcept transportConcept) {
		// clone vsm to fiscrm vector
					int[] vsm = transportConcept.getVsm();
					if(vsm == null){
						return;
					}
					double[] fiscrm_vector = new double[vsm.length];
					for (int i = 0; i < fiscrm_vector.length; i++) {
						fiscrm_vector[i] = vsm[i];
					}
					transportConcept.setFiscrm_vector(fiscrm_vector);

					// readjust fiscrm vector for each service
					double[] adjusted_fiscrm = readjustment(vsm, fiscrm_vector);
					transportConcept.setFiscrm_vector(adjusted_fiscrm);
		
	}
	
	public void readjustmentAllElementConcept(ArrayList<TransportConcept> concepts) {
		for (TransportConcept each_concept : concepts) {
			// clone vsm to fiscrm vector
			int[] vsm = each_concept.getVsm();
			double[] fiscrm_vector = new double[vsm.length];
			for (int i = 0; i < fiscrm_vector.length; i++) {
				fiscrm_vector[i] = vsm[i];
			}
			each_concept.setFiscrm_vector(fiscrm_vector);

			// readjust fiscrm vector for each service
			double[] adjusted_fiscrm = readjustment(each_concept.getVsm(), each_concept.getFiscrm_vector());
			each_concept.setFiscrm_vector(adjusted_fiscrm);
		}
	}

	public double[] readjustment(int[] vsm, double[] fiscrm) {
		// get each term from all word list 
		for (String term : all_word_list) {
			// get each term from a service in order to adjust the weight           
			// get synonym list of this term
			ArrayList<String> synonym_list = selected_synonym_map.get(term);
			// get strong synonym terms
			ArrayList<String> strong_synlist = getStrongSynonymList(synonym_list);

			// if a term is strong --> find the other strong synonym terms
			// else if a term is weak --> find the other strong synonym terms
			int term_indx = all_word_list.indexOf(term);

			if ((vsm[term_indx] > 0)) {
				// get cooccurrences of all synonym words : C(term)
				int occur = getCooccurrenceSyn(vsm, term, strong_synlist, all_word_list);
				// get #words that represent the same concept --> # synonym words : T(term)
				int synnum = strong_synlist.size() + 1;

				double weight = occur * Math.sqrt(1.0 / synnum);
				//adjust weight for term
				fiscrm = adjustWeight(fiscrm, term, weight+occur, all_word_list);
				for (String syn : strong_synlist) {
					int occur_syn = getCooccurrenceSyn(vsm, syn, strong_synlist, all_word_list);
					fiscrm = adjustWeight(fiscrm, syn, weight+occur_syn, all_word_list);
				}

			}
			
			/*else if ((vsm[term_indx] > 0) && isWeakWord(term)) {
				// a weak word --> get index of a weak term = term_indx                
				// C(w)--> occurrence of the weak word w
				int cw = vsm[term_indx];
				//                int cs = getCooccurrenceSyn(vsm, term, strong_synlist, all_word_list);
				double sd_sum = calSDSum(strong_synlist, term, vsm, all_word_list);

				//                System.out.println("term : " + term);

				for (String strong_word : strong_synlist) {

					int strong_indx = all_word_list.indexOf(strong_word);
					int cs = vsm[strong_indx];

					if (cs > 0) { // select only the strong word presenting in the document
						double sd_ws = calSynonymyDegree(term, strong_word, selected_synonym_map);
						//                        double sd_sw = calSynonymyDegree(strong_word, term, selected_synonym_map);
						double strong_weight = cs + (cw * Math.sqrt(sd_ws / (1 + sd_sum)));



						fiscrm[strong_indx] = strong_weight;

						//                    System.out.println("strong term : " + strong_word);
						//                    System.out.println("sd_sw : " + sd_sw);
						//                    System.out.println("sd_sum : " + sd_sum);
						//                    System.out.println("cw : " + cw);
						//                    System.out.println("cs : " + cs);
						//                    System.out.println("strong weight : " + strong_weight);
						//                    System.out.println("=================================================");

					}

				}

				double weak_weight = cw * Math.sqrt(1 / (1 + sd_sum));
				fiscrm[term_indx] = weak_weight;
				//                System.out.println("term : " + term);
				//                System.out.println("weak_weight : " + weak_weight);
				//                System.out.println("=================================================");

			}*/

		}
		return fiscrm;
	}

	public double calSDSum(ArrayList<String> strong_synlist, String term, int[] vsm, ArrayList<String> all_word_list) {
		double sd_sum = 0;
		for (String strong_word : strong_synlist) {
			int strong_indx = all_word_list.indexOf(strong_word);
			int cs = vsm[strong_indx];
			if (cs > 0) {
				sd_sum += calSynonymyDegree(strong_word, term, selected_synonym_map);
			}
		}
		return sd_sum;
	}

	public double calSynonymyDegree(String term1, String term2, HashMap<String, ArrayList<String>> synmap) {

		double sd1 = 0;
		double sd2 = 0;

		ArrayList<String> synlist1 = synmap.get(term1);
		ArrayList<String> synlist2 = synmap.get(term2);

		ArrayList<String> commonsyns = new ArrayList<>();
		ArrayList<String> totalsyns = new ArrayList<>();

		Synset[] term1_senses = wordnet_util.getWordSenses(term1);

		for (String syn1 : synlist1) {
			for (String syn2 : synlist2) {
				if (syn1.equals(syn2)) {

					if (!commonsyns.contains(syn1)) {
						commonsyns.add(syn1);
					}
				}

				if (!totalsyns.contains(syn1)) {
					totalsyns.add(syn1);
				}

				if (!totalsyns.contains(syn2)) {
					totalsyns.add(syn2);
				}

			}
		}

		// SD2(A,B) = SD1(A,B)/#meanings of A

		sd1 = commonsyns.size() * 1.0 / totalsyns.size();
		sd2 = sd1 / term1_senses.length;

		return sd2;
	}

	public boolean isStrongWord(String word) {
		if (strong_words.contains(word)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isWeakWord(String word) {
		if (weak_words.contains(word)) {
			return true;
		} else {
			return false;
		}
	}

	public ArrayList<String> getStrongSynonymList(ArrayList<String> synlist) {
		ArrayList<String> strong_syn_list = new ArrayList<>();

		for (String term : synlist) {
			if (isStrongWord(term.toLowerCase())) {
				strong_syn_list.add(term);
			}
		}

		return strong_syn_list;
	}

	public int getCooccurrenceSyn(int[] vsm, String term,
			ArrayList<String> synlist, ArrayList<String> all_word) {

		int cooccur = 0;

		int indx = all_word.indexOf(term);

		if (isStrongWord(term)) {
			cooccur += vsm[indx];
		}

		for (String synterm : synlist) {
			indx = all_word.indexOf(synterm.toLowerCase());
			cooccur += vsm[indx];
		}

		return cooccur;
	}

	public double[] adjustWeight(double[] fiscrm, String term, double weight, ArrayList<String> all_word) {
		int indx = all_word.indexOf(term);
		fiscrm[indx] = weight;

		return fiscrm;
	}

	public void getSelectedSynonymForAllWordsList(ArrayList<String> all_word) {
		for (String word : all_word) {
			try {
				// get synonym set for each word
				ArrayList<String> synset = wordnet_util.lookupSynonyms(word);
				// select only terms in all word list
				ArrayList<String> selected_synset = new ArrayList<>();
				for (String syn : synset) {
					if (all_word.contains(syn)) {
						selected_synset.add(syn);
					}
				}
				all_synonym_map.put(word, synset);
				selected_synonym_map.put(word, selected_synset);
			} catch (JWNLException ex) {
				Logger.getLogger(FISCRM.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		//        System.out.println("SelectedSynonym Map : ");
		//        for (String key : selected_synonym_map.keySet()) {
		//            System.out.println(key + " : " + selected_synonym_map.get(key));
		//        }
		//
		//        System.out.println("AllSynonym Map : ");
		//        for (String key : all_synonym_map.keySet()) {
		//            System.out.println(key + " : " + all_synonym_map.get(key));
		//        }

	}

	//####################################################################    
	//######################## Non Stemming method #######################
	//####################################################################
	public void nonstemmingService(ArrayList<ServiceDescriptionEntity> sdeList) throws Exception {
		FileWriter fw ;
		
			fw = new FileWriter("WordFrequency.txt");
		
		createStopwordList();
		//        System.out.println("Word List for Each Service");
		//System.out.println(sdeList.size());
		for (ServiceDescriptionEntity each_service : sdeList) {
			String sdeDesc = each_service.getSDEDescription();
			String provide = each_service.getProvider();
			if(!(sdeDesc.equalsIgnoreCase("unknown"))){
				createWordListNonStemming(sdeDesc, each_service);
			}
			fw.write("\""+each_service.getSDEName() + "\",");
			//            String sdeName = each_service.getSDEName();
			
			fw.write("\""+sdeDesc + "\"\n");
			//createWordListNonStemming(provide, each_service);
			//            createWordListNonStemming(sdeName, each_service);
			

			//            aircraftEntityFISCRMList.add(serviceFIS);
		}
		fw.close();
		/*System.out.println("==========================================");
		System.out.println("All Word List : " + all_word_list);
		System.out.println("Size : " + all_word_list.size());

		System.out.println("==========================================");
		System.out.println("Strong Word List : " + strong_words);
		System.out.println("Size : " + strong_words.size());

		System.out.println("==========================================");
		System.out.println("Weak Word List : " + weak_words);
		System.out.println("Size : " + weak_words.size());*/
		

		/*for (String word : all_word_list) {
			System.out.print(word + ": ");
		}
		System.out.println("");*/
	}

	public void nonstemmingConcept() {
		createStopwordList();
		System.out.println("Word List for Each Concept");
		for (TransportConcept concept : conceptObjList) {
			ArrayList<String> concept_descs = concept.getConcept_desc();

			for (String desc : concept_descs) {
				createWordListNonStemming(desc, concept);
			}

			//            System.out.println(concept.getWord_list());
		}

		System.out.println("==========================================");
		System.out.println("All Word List : " + all_word_list);
		System.out.println("Size : " + all_word_list.size());

		System.out.println("==========================================");
		System.out.println("Strong Word List : " + strong_words);
		System.out.println("Size : " + strong_words.size());

		System.out.println("==========================================");
		System.out.println("Weak Word List : " + weak_words);
		System.out.println("Size : " + weak_words.size());

	}

	//####################################################################    
	//#################### Word Utilities Methods ########################
	//####################################################################
	public void createStopwordList() {
		stopword = new ArrayList<String>();
		File stopword_file = new File(ServiceSelectionPropertiesDefaults.filename_stopword);
		if (stopword_file.exists()) {
			try {
				FileReader fr = new FileReader(stopword_file);
				BufferedReader br = new BufferedReader(fr);

				String line = br.readLine();
				while (line != null) {
					stopword.add(line);
					line = br.readLine();
				}

				br.close();
				fr.close();
			} catch (Exception e) {
				System.out.println("Exception in createStopwordList()");
				e.printStackTrace();
			}

		} else {
			System.out.println("Stop Word File does not exist!!!");
		}
		//System.out.println("stop word list size: "+stopword.size());
	}

	public String removeStopword(String title) {
		//createStopwordList();

		title = title.toLowerCase();
		title = title.replace('.', ' ');
		title = title.replace(':', ' ');
		title = title.replace('?', ' ');
		title = title.replace(',', ' ');
		title = title.replace(';', ' ');
		title = title.replace('-', ' ');
		title = title.replace('(', ' ');
		title = title.replace(')', ' ');
		title = title.replace('&', ' ');
		title = title.replace('/', ' ');
		title = title.replace('!', ' ');
		String[] word = title.split(" ");
		String newTitle = "";
		for (String s : word) {
			if (!stopword.contains(s) && (!s.equals(""))) {
				newTitle += (s + " ");
			}
		}
		return newTitle;

	}

	public String deleteSuffixNoun(String str) { // delete plural
		//<param name="noun" value="|s=|ses=s|xes=x|zes=z|ches=ch|shes=sh|men=man|ies=y|"/>
		String result = "";
		if (str.endsWith("ches")) {
			result = str.substring(0, str.length() - 4);
			result += "ch";
		} else if (str.endsWith("shes")) {
			result = str.substring(0, str.length() - 4);
			result += "sh";

		} else if (str.endsWith("ses")) {
			result = str.substring(0, str.length() - 3);
			result += "s";

		} else if (str.endsWith("xes")) {
			result = str.substring(0, str.length() - 3);
			result += "x";

		} else if (str.endsWith("zes")) {
			result = str.substring(0, str.length() - 3);
			result += "z";

		} else if (str.endsWith("men")) {
			result = str.substring(0, str.length() - 3);
			result += "man";

		} else if (str.endsWith("ies")) {
			result = str.substring(0, str.length() - 3);
			result += "y";

		} else if (str.endsWith("s")) {
			result = str.substring(0, str.length() - 1);

		} else {
			result = str;
		}
		//System.out.println("Result : " + result);
		return result;
	}

	//####################################################################    
	//############# Create Word List with Non Stemming method ############
	//####################################################################
	public void createWordListNonStemming(String str, ServiceDescriptionEntity aircraftService) {
		// delete suffix for nouns
		String remove_stop_title = removeStopword(str);

		//remove_stop_title = steming.Stemmer.stemming(remove_stop_title);

		String[] word = remove_stop_title.split(" ");

		for (String eachword : word) {
			// just try to remove suffix
			//            System.out.println("word : " + eachword);
			// delete suffix
			String eachword_without_suffix = deleteSuffixNoun(eachword);

			Synset[] senses = wordnet_util.getWordSenses(eachword_without_suffix);
			if (senses != null) {
				aircraftService.getWord_list().add(eachword_without_suffix);
			} else {
				//using original word
				senses = wordnet_util.getWordSenses(eachword);
				if (senses != null) {
					aircraftService.getWord_list().add(eachword);
				} else {
					String word_without_ing = "";
					if (eachword.endsWith("ing")) {
						word_without_ing = eachword.substring(0, eachword.length() - 3);

						senses = wordnet_util.getWordSenses(word_without_ing);

						if (senses != null) {
							aircraftService.getWord_list().add(word_without_ing);
						} else {
							word_without_ing += "e";
							senses = wordnet_util.getWordSenses(word_without_ing);
							if (senses != null) {
								aircraftService.getWord_list().add(word_without_ing);
							}
						}

					}
				}
			}

		}
		for (String eachword : word) { // check redundancy

			// get senses of word
			//            System.out.println("word : " + eachword);
			// just try to remove suffix
			String eachword_without_suffix = deleteSuffixNoun(eachword);
			// get senses of word
			//            System.out.println("word : " + eachword);
			Synset[] senses = wordnet_util.getWordSenses(eachword_without_suffix);

			if (senses != null) { // if a word has meaning
				if (!all_word_list.contains(eachword_without_suffix)) {
					all_word_list.add(eachword_without_suffix);

					// fix this path
					//                    if (senses.length == 1) {
					//                        strong_words.add(eachword_without_suffix);
					//                    } else if (senses.length > 1) {
					//                        weak_words.add(eachword_without_suffix);
					//                    }

					//                    No distinction btw strong and weak words --> all synonym words = strong
					strong_words.add(eachword_without_suffix);
				}
			} else {
				senses = wordnet_util.getWordSenses(eachword);

				if (senses != null) { // if a word has meaning
					if (!all_word_list.contains(eachword)) {
						all_word_list.add(eachword);

						//                        if (senses.length == 1) {
						//                            strong_words.add(eachword);
						//                        } else if (senses.length > 1) {
						//                            weak_words.add(eachword);
						//                        }
						//                    No distinction btw strong and weak words --> all synonym words = strong
						strong_words.add(eachword);
					}
				} else {
					String word_without_ing = "";
					if (eachword.endsWith("ing")) {
						word_without_ing = eachword.substring(0, eachword.length() - 3);

						senses = wordnet_util.getWordSenses(word_without_ing);

						if (senses != null) {
							if (!all_word_list.contains(word_without_ing)) {
								all_word_list.add(word_without_ing);

								//                                if (senses.length == 1) {
								//                                    strong_words.add(word_without_ing);
								//                                } else if (senses.length > 1) {
								//                                    weak_words.add(word_without_ing);
								//                                }
								//                    No distinction btw strong and weak words --> all synonym words = strong
								strong_words.add(word_without_ing);
							}
						} else {
							word_without_ing += "e";
							senses = wordnet_util.getWordSenses(word_without_ing);
							if (senses != null) {
								if (!all_word_list.contains(word_without_ing)) {
									all_word_list.add(word_without_ing);

									//                                    if (senses.length == 1) {
									//                                        strong_words.add(word_without_ing);
									//                                    } else if (senses.length > 1) {
									//                                        weak_words.add(word_without_ing);
									//                                    }
									//                                    No distinction btw strong and weak words --> all synonym words = strong
									strong_words.add(word_without_ing);

								}
							}
						}

					}
				}

			}
		}
	}

	public void createWordListNonStemming(String str, TransportConcept tc) {
		// delete suffix for nouns
		String remove_stop_title = removeStopword(str);

		//remove_stop_title = steming.Stemmer.stemming(remove_stop_title);

		String[] word = remove_stop_title.split(" ");

		for (String eachword : word) {
			// just try to remove suffix
			//            System.out.println("word : " + eachword);
			// delete suffix
			String eachword_without_suffix = deleteSuffixNoun(eachword);

			Synset[] senses = wordnet_util.getWordSenses(eachword_without_suffix);
			if (senses != null) {
				tc.getWord_list().add(eachword_without_suffix);
			} else {
				//using original word
				senses = wordnet_util.getWordSenses(eachword);
				if (senses != null) {
					tc.getWord_list().add(eachword);
				} else {
					String word_without_ing = "";
					if (eachword.endsWith("ing")) {
						word_without_ing = eachword.substring(0, eachword.length() - 3);

						senses = wordnet_util.getWordSenses(word_without_ing);

						if (senses != null) {
							tc.getWord_list().add(word_without_ing);
						} else {
							word_without_ing += "e";
							senses = wordnet_util.getWordSenses(word_without_ing);
							if (senses != null) {
								tc.getWord_list().add(word_without_ing);
							}
						}
					}
				}
			}
		}

		for (String eachword : word) { // check redundancy

			// get senses of word
			//            System.out.println("word : " + eachword);
			// just try to remove suffix
			String eachword_without_suffix = deleteSuffixNoun(eachword);
			// get senses of word
			//            System.out.println("word : " + eachword);
			Synset[] senses = wordnet_util.getWordSenses(eachword_without_suffix);

			if (senses != null) { // if a word has meaning
				if (!all_word_list.contains(eachword_without_suffix)) {
					all_word_list.add(eachword_without_suffix);

					// fix this path
					//                    if (senses.length == 1) {
					//                        strong_words.add(eachword_without_suffix);
					//                    } else if (senses.length > 1) {
					//                        weak_words.add(eachword_without_suffix);
					//                    }

					//                    No distinction btw strong and weak words --> all synonym words = strong
					strong_words.add(eachword_without_suffix);
				}
			} else {
				senses = wordnet_util.getWordSenses(eachword);

				if (senses != null) { // if a word has meaning
					if (!all_word_list.contains(eachword)) {
						all_word_list.add(eachword);

						//                        if (senses.length == 1) {
						//                            strong_words.add(eachword);
						//                        } else if (senses.length > 1) {
						//                            weak_words.add(eachword);
						//                        }
						//                    No distinction btw strong and weak words --> all synonym words = strong
						strong_words.add(eachword);
					}
				} else {
					String word_without_ing = "";
					if (eachword.endsWith("ing")) {
						word_without_ing = eachword.substring(0, eachword.length() - 3);

						senses = wordnet_util.getWordSenses(word_without_ing);

						if (senses != null) {
							if (!all_word_list.contains(word_without_ing)) {
								all_word_list.add(word_without_ing);

								//                                if (senses.length == 1) {
								//                                    strong_words.add(word_without_ing);
								//                                } else if (senses.length > 1) {
								//                                    weak_words.add(word_without_ing);
								//                                }
								//                    No distinction btw strong and weak words --> all synonym words = strong
								strong_words.add(word_without_ing);
							}
						} else {
							word_without_ing += "e";
							senses = wordnet_util.getWordSenses(word_without_ing);
							if (senses != null) {
								if (!all_word_list.contains(word_without_ing)) {
									all_word_list.add(word_without_ing);

									//                                    if (senses.length == 1) {
									//                                        strong_words.add(word_without_ing);
									//                                    } else if (senses.length > 1) {
									//                                        weak_words.add(word_without_ing);
									//                                    }
									//                                    No distinction btw strong and weak words --> all synonym words = strong
									strong_words.add(word_without_ing);

								}
							}
						}

					}
				}

			}
		}
	}

	//####################################################################    
	//######################### Reading Methods ##########################
	//####################################################################
	public void readConceptObjectFromFile(String filename) {
		FileInputStream fin = null;
		ObjectInputStream oin = null;
		try {
			fin = new FileInputStream(filename);
			oin = new ObjectInputStream(fin);

			conceptObjList = (ArrayList<TransportConcept>) oin.readObject();
			System.out.println("Transport Concept Size : " + conceptObjList.size());

			oin.close();
			fin.close();

		} catch (ClassNotFoundException ex) {
			Logger.getLogger(FISCRM.class.getName()).log(Level.SEVERE, null, ex);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(FISCRM.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(FISCRM.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void readServiceEntityFromFile(String filename) {
		FileInputStream fin = null;
		ObjectInputStream oin = null;
		try {
			fin = new FileInputStream(filename);
			oin = new ObjectInputStream(fin);

			serviceEntityList = (ArrayList<ServiceDescriptionEntity>) oin.readObject();
			System.out.println("Service Size : " + serviceEntityList.size());
			System.out.println("First Service : " + serviceEntityList.get(0).getProvider());
			System.out.println("Word list : " + serviceEntityList.get(0).getWord_list());

			oin.close();
			fin.close();

		} catch (ClassNotFoundException ex) {
			Logger.getLogger(FISCRM.class.getName()).log(Level.SEVERE, null, ex);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(FISCRM.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(FISCRM.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	//####################################################################    
	//###################### Writting Methods ############################
	//####################################################################
	public void writeServiceEntityFISCRM(String filename) {
		FileOutputStream fout = null;
		ObjectOutputStream oout = null;
		try {
			fout = new FileOutputStream(filename);
			oout = new ObjectOutputStream(fout);


			oout.writeObject(serviceEntityList);
			System.out.println("Service Size : " + serviceEntityList.size());
			//            System.out.println("Writting to File");
			//            for (AircraftEntityForFISCRM service : aircraftEntityFISCRMList) {
			//                int[] vsm = service.getVsm();
			//                double[] fiscrm = service.getFiscrm_vector();
			//                System.out.print("VSM : ");
			//                printArray(vsm);
			//                System.out.print("FISCRM : ");
			//                printArray(fiscrm);
			//            }

			oout.close();
			fout.close();

		} catch (FileNotFoundException ex) {
			Logger.getLogger(FISCRM.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(FISCRM.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void writeConceptFISCRM(String filename) {
		FileOutputStream fout = null;
		ObjectOutputStream oout = null;
		try {
			fout = new FileOutputStream(filename);
			oout = new ObjectOutputStream(fout);


			oout.writeObject(conceptObjList);
			System.out.println("conceptObjList Size : " + conceptObjList.size());
			//            System.out.println("Writting Concepts to File");
			//            System.out.println("==========================================");
			//            for (TransportConcept concept : conceptObjList) {
			//                int[] vsm = concept.getVsm();
			//                double[] fiscrm = concept.getFiscrm_vector();
			//                System.out.print("VSM : ");
			//                printArray(vsm);
			//                System.out.print("FISCRM : ");
			//                printArray(fiscrm);
			//            }

			oout.close();
			fout.close();

		} catch (FileNotFoundException ex) {
			Logger.getLogger(FISCRM.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(FISCRM.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void writeAllNeccessayForFISCRM(String filename) {
		FileOutputStream fout = null;
		ObjectOutputStream oout = null;
		try {
			fout = new FileOutputStream(filename);
			oout = new ObjectOutputStream(fout);


			oout.writeObject(serviceEntityList);
			oout.writeObject(all_word_list);
			oout.writeObject(all_synonym_map);
			System.out.println("Service Size : " + serviceEntityList.size());

			oout.close();
			fout.close();

		} catch (FileNotFoundException ex) {
			Logger.getLogger(FISCRM.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(FISCRM.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	//####################################################################    
	//############################ Create VSM ############################
	//####################################################################    
	public int[] createVSMService(ArrayList<String> all_word, ArrayList<ServiceDescriptionEntity> serviceEntityList) {
		//System.out.println("=================================CREATING VSM======================================");
		//System.out.println("All words size : " + all_word.size());
		
		//System.out.println("SDEs : " + serviceEntityList.size());
		int [] globalVsm = new int[all_word.size()];
		for (ServiceDescriptionEntity each_service : serviceEntityList) {
			ArrayList<String> word_list = each_service.getWord_list();
			//System.out.println("service : " + each_service.getSDEName());
			//System.out.println("word list : " + word_list);
			if(word_list.size() == 0){
				continue;
			}
			//System.out.println("========================================");
			int[] vsm = new int[all_word.size()];

			for (String word : word_list) {
				int index = all_word.indexOf(word);
				//System.out.println("word : " + word);
				//System.out.println("index : " + index);
				vsm[index]++;
				globalVsm[index]++;
			}
			/*System.out.print("Vector for SDE: ");
			for (int i = 0; i < vsm.length; i++) {
				System.out.print(all_word.get(i)+":");
				System.out.print(vsm[i]+",");
				//System.out.print(globalVsm[i]+",");
			}*/
			//System.out.println("");
			each_service.setVsm(vsm);
		}
		//System.out.print("Concept Vector : ");
		/*for (int i = 0; i < globalVsm.length; i++) {
			System.out.print(all_word.get(i)+":");
			System.out.print(globalVsm[i]+",");
		}*/
		return globalVsm;
	}

	public void createVSMConcept(ArrayList<String> all_word, ArrayList<TransportConcept> concepts) {
		System.out.println("concept size: "+concepts.size());
		for (TransportConcept concept : concepts) {
			ArrayList<Integer> conceptSDE = concept.getLinkedServiceList_fiscrm();
			ArrayList<String> word_list = concept.getWord_list();
			System.out.println("concept Name: "+concept.getName()+" : "+word_list.size());
			int[] vsm = new int[all_word.size()];

			for (String word : word_list) {
				if (all_word.contains(word)) {
					int index = all_word.indexOf(word);
					vsm[index]++;
				} else {
					System.out.println("all_word_list does not contain this term --- Impossible");
				}
			}
			System.out.print("Vector: ");
			for (int i : vsm) {
				System.out.print(i+" ");
			}
			System.out.println("");
			concept.setVsm(vsm);
		}
	}

	//####################################################################    
	//########################## Main method #############################
	//####################################################################
	public static void main(String[] args) {
		// TODO code application logic here
		FISCRM fis = new FISCRM();
		//fis.init();
	}

	

	

	

	

}
