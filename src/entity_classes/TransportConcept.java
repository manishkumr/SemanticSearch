/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity_classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author kikitt
 */
public class TransportConcept implements Serializable{

    private int id;
    private String name;
    private ArrayList<String> concept_desc = new ArrayList<>();
    private ArrayList<String> word_list = new ArrayList<>();
    private int[] vsm;
    private double[] fiscrm_vector;
    private ArrayList<ServiceDescriptionEntity> sdeList = new ArrayList<ServiceDescriptionEntity>();
    private ArrayList<Integer> linkedServiceList_vsm = new ArrayList<>(); 
    private ArrayList<Integer> linkedServiceList_fiscrm = new ArrayList<>();
    private ArrayList<Double> linkedServiceSimList_vsm = new ArrayList<>(); 
    private ArrayList<Double> linkedServiceSimList_fiscrm = new ArrayList<>();
    private LinkedHashMap<String, Integer> linkedSDEdescWordFrequency = new LinkedHashMap<String, Integer>();
    
    private Double similarityURL;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the concept_desc
     */
    public ArrayList<String> getConcept_desc() {
        return concept_desc;
    }

    /**
     * @param concept_desc the concept_desc to set
     */
    public void setConcept_desc(ArrayList<String> concept_desc) {
        this.concept_desc = concept_desc;
    }

    /**
     * @return the vsm
     */
    public int[] getVsm() {
        return vsm;
    }

    /**
     * @param vsm the vsm to set
     */
    public void setVsm(int[] vsm) {
        this.vsm = vsm;
    }

    /**
     * @return the fiscrm_vector
     */
    public double[] getFiscrm_vector() {
        return fiscrm_vector;
    }

    /**
     * @param fiscrm_vector the fiscrm_vector to set
     */
    public void setFiscrm_vector(double[] fiscrm_vector) {
        this.fiscrm_vector = fiscrm_vector;
    }

    /**
     * @return the word_list
     */
    public ArrayList<String> getWord_list() {
        return word_list;
    }

    /**
     * @param word_list the word_list to set
     */
    public void setWord_list(ArrayList<String> word_list) {
        this.word_list = word_list;
    }

    

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the linkedServiceList_vsm
     */
    public ArrayList<Integer> getLinkedServiceList_vsm() {
        return linkedServiceList_vsm;
    }

    /**
     * @param linkedServiceList_vsm the linkedServiceList_vsm to set
     */
    public void setLinkedServiceList_vsm(ArrayList<Integer> linkedServiceList_vsm) {
        this.linkedServiceList_vsm = linkedServiceList_vsm;
    }

    /**
     * @return the linkedServiceList_fiscrm
     */
    public ArrayList<Integer> getLinkedServiceList_fiscrm() {
        return linkedServiceList_fiscrm;
    }

    /**
     * @param linkedServiceList_fiscrm the linkedServiceList_fiscrm to set
     */
    public void setLinkedServiceList_fiscrm(ArrayList<Integer> linkedServiceList_fiscrm) {
        this.linkedServiceList_fiscrm = linkedServiceList_fiscrm;
    }

    /**
     * @return the linkedServiceSimList_vsm
     */
    public ArrayList<Double> getLinkedServiceSimList_vsm() {
        return linkedServiceSimList_vsm;
    }

    /**
     * @param linkedServiceSimList_vsm the linkedServiceSimList_vsm to set
     */
    public void setLinkedServiceSimList_vsm(ArrayList<Double> linkedServiceSimList_vsm) {
        this.linkedServiceSimList_vsm = linkedServiceSimList_vsm;
    }

    /**
     * @return the linkedServiceSimList_fiscrm
     */
    public ArrayList<Double> getLinkedServiceSimList_fiscrm() {
        return linkedServiceSimList_fiscrm;
    }

    /**
     * @param linkedServiceSimList_fiscrm the linkedServiceSimList_fiscrm to set
     */
    public void setLinkedServiceSimList_fiscrm(ArrayList<Double> linkedServiceSimList_fiscrm) {
        this.linkedServiceSimList_fiscrm = linkedServiceSimList_fiscrm;
    }

	public ArrayList<ServiceDescriptionEntity> getSdeList() {
		return sdeList;
	}

	public void setSdeList(ArrayList<ServiceDescriptionEntity> sdeList) {
		this.sdeList = sdeList;
	}

	public LinkedHashMap<String, Integer> getLinkedSDEdescWordFrequency() {
		return linkedSDEdescWordFrequency;
	}

	public void setLinkedSDEdescWordFrequency(LinkedHashMap<String, Integer> linkedSDEdescWordFrequency) {
		this.linkedSDEdescWordFrequency = linkedSDEdescWordFrequency;
	}

	public Double getSimilarityURL() {
		return similarityURL;
	}

	public void setSimilarityURL(Double similarityURL) {
		this.similarityURL = similarityURL;
	}
    
    
    
}
