/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontology_connection;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.*;
import entity_classes.TransportConcept;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import properties.ServiceSelectionPropertiesDefaults;

/**
 *
 * @author kikitt
 */
public class ReadLeafTransportConcepts {

    private ArrayList<String> leaf_concept_list = new ArrayList<>();
    private HashMap<String, ArrayList<String>> leafConceptDescList = new HashMap<>();
    private ArrayList<String> selected_concept = new ArrayList<>();
    private ArrayList<TransportConcept> all_leaf_concept_objs = new ArrayList<>();
    private OWLModel owlModel = null;

    public void init_test() {
        selected_concept.add("Airline");
        selected_concept.add("Airline_Agent");
        selected_concept.add("Airline_Booking");
        selected_concept.add("Airline_Quote");
        selected_concept.add("Airline_Ticketing");
        selected_concept.add("Airline_Gift_Vochure");
        selected_concept.add("Airline_Travel_Insurance");
    }

    public Collection getSubclasses(OWLNamedClass owlClass) {
        Collection subclasses = null;
        RDFProperty subClassOfProperty = owlModel.getRDFProperty(RDFSNames.Slot.SUB_CLASS_OF);

        subclasses = owlModel.getRDFResourcesWithPropertyValue(subClassOfProperty, owlClass);

        return subclasses;
    }

    public ArrayList<String> mergeArrayList(ArrayList<String> list1, ArrayList<String> list2) {

        for (String s : list2) {
            if (!list1.contains(s)) {
                list1.add(s);
            }
        }

        return list1;

    }

    public void traverseLeafNode(OWLNamedClass concept_class, ArrayList<String> parent_desc) {
        Collection subclasses = getSubclasses(concept_class);
//        System.out.println("subclasses size : " + subclasses.size());

        if (subclasses.isEmpty()) { // leaf node
            ArrayList<String> desc = getConceptDescription(concept_class.getLocalName());
            mergeArrayList(desc, parent_desc);

            leafConceptDescList.put(concept_class.getLocalName(), desc);
            leaf_concept_list.add(concept_class.getLocalName());
            System.out.println("===============================================");
            System.out.println("Concept : " + concept_class.getLocalName());
            System.out.println("Descriptions : " + desc);

        } else {
            // not leaf node
            for (Iterator it = subclasses.iterator(); it.hasNext();) {
                RDFResource each_subclass = (RDFResource) it.next();
//                System.out.println(" - " + each_subclass.getBrowserText());

//                ArrayList<String> desc = getConceptDescription(each_subclass.getBrowserText());
//                mergeArrayList(desc, parent_desc);
                ArrayList<String> desc = new ArrayList<>();

                OWLNamedClass owlClass = owlModel.getOWLNamedClass(each_subclass.getBrowserText());

                traverseLeafNode(owlClass, desc);


            }
        }
    }

    public ArrayList<String> getConceptDescription(String concept_name) {

        ArrayList<String> serviceDescription = new ArrayList<>();

        if (!concept_name.equals("Transport_Service_Description_Entity")) {

            OWLNamedClass owlClass = owlModel.getOWLNamedClass(concept_name);
            Collection instances = owlClass.getInstances(false);

            for (Iterator jt = instances.iterator(); jt.hasNext();) {
                OWLIndividual individual = (OWLIndividual) jt.next();
                Collection individual_property = individual.getRDFProperties();
//                ArrayList<String> serviceDescription = new ArrayList<>();
                int i = 0;

                for (Iterator ip = individual_property.iterator(); ip.hasNext();) {
                    i++;

                    switch (i) {
                        case 1:
                            ip.next();
                            //System.out.println(individual.getPropertyValue((RDFProperty) ip.next()).toString());

                            break;
                        case 2:
                            //   individual.getPropertyValue((RDFProperty) ip.next()).toString();
                            Collection tmp = individual.getPropertyValues((RDFProperty) ip.next());
                            for (Object obj : tmp) {
                                serviceDescription.add(obj.toString());
                            }
                            break;
                        default:
                            ip.next();
                            break;
                    }
                }
            }
        }

        return serviceDescription;
    }

    public void queryConceptTree() {
        try {
            String uri = ServiceSelectionPropertiesDefaults.owl_file;

            owlModel = ProtegeOWL.createJenaOWLModelFromURI(uri);

            OWLNamedClass owlClass = owlModel.getOWLNamedClass("Transport");

            ArrayList<String> desc = new ArrayList<>();
//            desc.add("Logistics");
//            desc.add("Transport");

            traverseLeafNode(owlClass, desc);

            writeTransportConceptsToFile(ServiceSelectionPropertiesDefaults.leaf_transport_concept_file);
            writeTransportConceptObjectsToFile(ServiceSelectionPropertiesDefaults.leaf_transport_object_concept_file);

        } catch (OntologyLoadException ex) {
            Logger.getLogger(ReadLeafTransportConcepts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<TransportConcept> createConceptObjList(ArrayList<String> concepts,
            HashMap<String, ArrayList<String>> concept_desc) {

        ArrayList<TransportConcept> conceptObjList = new ArrayList<>();
        for (int i = 0; i < concepts.size(); i++) {
            TransportConcept tc = new TransportConcept();
            tc.setId(i);
            tc.setName(concepts.get(i));

            ArrayList<String> desc = concept_desc.get(concepts.get(i));
            tc.setConcept_desc(desc);

            ArrayList<String> word_list = new ArrayList<>();
            tc.setWord_list(word_list);

            conceptObjList.add(tc);
        }

        return conceptObjList;
    }

    public void writeTransportConceptObjectsToFile(String filename) {

        all_leaf_concept_objs = createConceptObjList(leaf_concept_list, leafConceptDescList);

        FileOutputStream fout = null;
        ObjectOutputStream oout = null;
        try {
            fout = new FileOutputStream(filename);
            oout = new ObjectOutputStream(fout);

            oout.writeObject(all_leaf_concept_objs);

            System.out.println("Transport Concepts Size : " + leafConceptDescList.size());
            System.out.println("concept_list : " + leaf_concept_list);
            System.out.println("conceptDest : " + leafConceptDescList);

            oout.close();
            fout.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadLeafTransportConcepts.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadLeafTransportConcepts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeTransportConceptsToFile(String filename) {
        FileOutputStream fout = null;
        ObjectOutputStream oout = null;
        try {
            fout = new FileOutputStream(filename);
            oout = new ObjectOutputStream(fout);

            oout.writeObject(leaf_concept_list);
            oout.writeObject(leafConceptDescList);
            System.out.println("Leaf Size : " + leafConceptDescList.size());
            System.out.println("concept_list - leaf : " + leaf_concept_list);
            System.out.println("conceptDest - leaf : " + leafConceptDescList);

            oout.close();
            fout.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadLeafTransportConcepts.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadLeafTransportConcepts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
        ReadLeafTransportConcepts obj = new ReadLeafTransportConcepts();
        obj.queryConceptTree();
    }
}
