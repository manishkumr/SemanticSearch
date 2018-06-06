package user_profile;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import entity_classes.TransportConcept;

public class WebPage implements Serializable{
	
	private String url;
	
	private String text;
	
	private Integer status;
	
	private LinkedList<String> words;
	
	private int [] vsm;
	
	private double[] extendedVSM;
	
	private HashMap<String, ArrayList<String>> AllSynonymMap;
	
	private HashMap<String, ArrayList<String>> selectedSynonymMap;
	
    private LinkedHashMap<String, Integer> wordFrequency = new LinkedHashMap<String, Integer>();
    
    private ArrayList<TransportConcept>	concepts = new ArrayList<TransportConcept>();


	public LinkedList<String> getWords() {
		return words;
	}

	public void setWords(LinkedList<String> words) {
		this.words = words;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public int [] getVsm() {
		return vsm;
	}

	public void setVsm(int [] vsm) {
		this.vsm = vsm;
	}

	public double[] getExtendedVSM() {
		return extendedVSM;
	}

	public void setExtendedVSM(double[] extendedVSM) {
		this.extendedVSM = extendedVSM;
	}

	public LinkedHashMap<String, Integer> getWordFrequency() {
		return wordFrequency;
	}

	public void setWordFrequency(LinkedHashMap<String, Integer> wordFrequency) {
		this.wordFrequency = wordFrequency;
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

	public ArrayList<TransportConcept> getConcepts() {
		return concepts;
	}

	public void setConcepts(ArrayList<TransportConcept> concepts) {
		this.concepts = concepts;
	}

	
}
