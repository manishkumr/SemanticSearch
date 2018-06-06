/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity_classes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author kikitt
 */
public class ServiceDescriptionEntity implements Serializable{

    private int id;
    private String provider;
    private String address;
    private String SDEDescription;
    private String contactDetail;
    private String location;
    private String SDEName;
    private ArrayList<String> iri;
    
    private ArrayList<String> word_list = new ArrayList<>();
    private int[] vsm;
    private double[] fiscrm_vector;
    private double[] vsm_concept_vector;
    private double[] fiscrm_concept_vector;
    
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
     * @return the vsm_concept_vector
     */
    public double[] getVsm_concept_vector() {
        return vsm_concept_vector;
    }

    /**
     * @param vsm_concept_vector the vsm_concept_vector to set
     */
    public void setVsm_concept_vector(double[] vsm_concept_vector) {
        this.vsm_concept_vector = vsm_concept_vector;
    }

    /**
     * @return the fiscrm_concept_vector
     */
    public double[] getFiscrm_concept_vector() {
        return fiscrm_concept_vector;
    }

    /**
     * @param fiscrm_concept_vector the fiscrm_concept_vector to set
     */
    public void setFiscrm_concept_vector(double[] fiscrm_concept_vector) {
        this.fiscrm_concept_vector = fiscrm_concept_vector;
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
     * @return the provider
     */
    public String getProvider() {
        return provider;
    }

    /**
     * @param provider the provider to set
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the SDEDescription
     */
    public String getSDEDescription() {
        return SDEDescription;
    }

    /**
     * @param SDEDescription the SDEDescription to set
     */
    public void setSDEDescription(String SDEDescription) {
        this.SDEDescription = SDEDescription;
    }

    /**
     * @return the contactDetail
     */
    public String getContactDetail() {
        return contactDetail;
    }

    /**
     * @param contactDetail the contactDetail to set
     */
    public void setContactDetail(String contactDetail) {
        this.contactDetail = contactDetail;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the SDEName
     */
    public String getSDEName() {
        return SDEName;
    }

    /**
     * @param SDEName the SDEName to set
     */
    public void setSDEName(String SDEName) {
        this.SDEName = SDEName;
    }

    /**
     * @return the iri
     */
    public ArrayList<String> getIri() {
        return iri;
    }

    /**
     * @param iri the iri to set
     */
    public void setIri(ArrayList<String> iri) {
        this.iri = iri;
    }
    
}
