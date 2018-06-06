/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ontology_connection;

import edu.stanford.smi.protege.exception.OntologyLoadException;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLIndividual;
import entity_classes.ServiceDescriptionEntity;
import java.io.*;
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
public class ReadServiceDescriptionEntities {

    /**
     * @param args the command line arguments
     */
    private ArrayList<String> scope_concept_list = new ArrayList<>();
    private ArrayList<ServiceDescriptionEntity> serviceEntityList = new ArrayList<>();
    private ArrayList<String> all_concept_list = new ArrayList<>();
    private HashMap<String, ArrayList<Integer>> concept_service = new HashMap<>();
    private ArrayList<String> service_test = new ArrayList<>();
    private boolean without_unknown = true;

    public void readConceptData(String filename) {
        FileInputStream fin = null;
        ObjectInputStream oin = null;
        try {
            fin = new FileInputStream(filename);
            oin = new ObjectInputStream(fin);

            all_concept_list = (ArrayList<String>) oin.readObject();
            oin.readObject();

//            System.out.println("Concept List : " + all_concept_list);
//            System.out.println("===========================================");
//            System.out.println("Concept Map : " + all_concept_map);
//            System.out.println("===========================================");
            oin.close();
            fin.close();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReadServiceDescriptionEntities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadServiceDescriptionEntities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadServiceDescriptionEntities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void readScopeConcept(String filename) {
        FileReader fr = null;
        try {
            fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();
            while (line != null) {
                scope_concept_list.add(line);
                line = br.readLine();
            }

            br.close();
            fr.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadServiceDescriptionEntities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadServiceDescriptionEntities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init_test() {
        service_test.add("Transport_Service_Description_Entity_524");
        service_test.add("Transport_Service_Description_Entity_3333");
        service_test.add("Transport_Service_Description_Entity_1167");
        service_test.add("Transport_Service_Description_Entity_3334");
        service_test.add("Transport_Service_Description_Entity_3661");
        service_test.add("Transport_Service_Description_Entity_2306");
        service_test.add("Transport_Service_Description_Entity_4005");
        service_test.add("Transport_Service_Description_Entity_765");
        service_test.add("Transport_Service_Description_Entity_3524");
        service_test.add("Transport_Service_Description_Entity_3900");
        service_test.add("Transport_Service_Description_Entity_2036");
    }

    private void createConceptList() {
        if (ServiceSelectionPropertiesDefaults.concept_scope_file.equals("all")) {

            if (ServiceSelectionPropertiesDefaults.focusAllConcept) {
                readConceptData(ServiceSelectionPropertiesDefaults.all_transport_concept_file);
            } else {
                readConceptData(ServiceSelectionPropertiesDefaults.leaf_transport_concept_file);
            }

            scope_concept_list = all_concept_list;
        } else {
            readScopeConcept(ServiceSelectionPropertiesDefaults.concept_scope_file);
        }

    }

    public void query() {

        createConceptList();

        try {
            String uri = ServiceSelectionPropertiesDefaults.owl_file;

            OWLModel owlModel = ProtegeOWL.createJenaOWLModelFromURI(uri);

            Collection classes = owlModel.getUserDefinedOWLNamedClasses();
            owlModel.getUserDefinedOWLObjectProperties();

            // get each concept
            for (Iterator it = classes.iterator(); it.hasNext();) {
                OWLNamedClass cls = (OWLNamedClass) it.next();
                Collection instances = cls.getInstances(false);

                // Transport Service Description Entity
                if (cls.getBrowserText().equals("Transport_Service_Description_Entity")) {
//                    System.out.println("Class " + cls.getBrowserText() + " (" + instances.size() + ")");
                    // for each service entity
                    int id = 0;
                    for (Iterator jt = instances.iterator(); jt.hasNext();) {
                        // get each instance
                        OWLIndividual individual = (OWLIndividual) jt.next();
//                        System.out.println(" - " + individual.getBrowserText());
//                        System.out.println(" + " + individual.getRDFProperties());


                        Collection individual_property = individual.getRDFProperties();

                        int i = 0;

                        String provider = "";
                        String address = "";
                        String SDEDescription = "";
                        String contactDetail = "";
                        String location = "";
                        String SDEName = "";
                        ArrayList<String> iri_list = new ArrayList<>();

                        boolean airCraftFlag = false;
                        ServiceDescriptionEntity ae;

                        for (Iterator ip = individual_property.iterator(); ip.hasNext();) {
                            // System.out.println("ip : " + ip);          
                            i++;

                            switch (i) {
                                case 1:
//                                        System.out.println("check : " + individual.getPropertyValue((RDFProperty) ip.next()).toString());
                                    individual.getPropertyValue((RDFProperty) ip.next()).toString();
//                                        ip.next();
                                    break;
                                case 2:
                                    //   individual.getPropertyValue((RDFProperty) ip.next()).toString();
                                    provider = individual.getPropertyValue((RDFProperty) ip.next()).toString();
                                    break;
                                case 3:
                                    address = (String) individual.getPropertyValue((RDFProperty) ip.next());
                                    break;
                                case 4:

                                    SDEDescription = (String) individual.getPropertyValue((RDFProperty) ip.next());

                                    break;
                                case 5:

                                    contactDetail = (String) individual.getPropertyValue((RDFProperty) ip.next());

                                    break;
                                case 6:
                                    location = (String) individual.getPropertyValue((RDFProperty) ip.next());

                                    break;
                                case 7:

                                    SDEName = (String) individual.getPropertyValue((RDFProperty) ip.next());

                                    break;
                                case 8:

                                    Collection iri = individual.getPropertyValues((RDFProperty) ip.next());
                                    for (Iterator irit = iri.iterator(); irit.hasNext();) {
                                        Object tmp_individual = irit.next();
                                        if (tmp_individual instanceof String) {
//                                                System.out.println("String test : " + tmp_individual.toString());
                                        } else {
                                            try {
                                                DefaultOWLIndividual e = (DefaultOWLIndividual) tmp_individual;
                                                String tmp_concept = e.getBrowserText();

//                                              System.out.println("Before cut : " + tmp_concept);
                                                String cutted_concept = cuttedConceptString(tmp_concept);

                                                if (cutted_concept != null) {
                                                    iri_list.add(cutted_concept);
                                                }

                                                if (scope_concept_list.contains(cutted_concept)) {
                                                    airCraftFlag = true;

                                                }
//                                                System.out.println("IRI : " + cutted_concept);
//                                                System.out.println("IRI : " + tmp_concept);
                                            } catch (ClassCastException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    break;
                                default:
                                    ip.next();
                                    break;
                            }
                        }
                        if (airCraftFlag) {
                            // create AirCraftEntity Object
                            ae = new ServiceDescriptionEntity();
                            ae.setId(id);
                            ae.setProvider(provider);
                            ae.setAddress(address);
                            ae.setSDEDescription(SDEDescription);
                            ae.setContactDetail(contactDetail);
                            ae.setLocation(location);
                            ae.setSDEName(SDEName);
                            ae.setIri(iri_list);

                            if (without_unknown) {
                                if (!SDEDescription.equals("unknown;")) {
                                    id++;

                                    for (String iri : iri_list) {
                                        if (scope_concept_list.contains(iri)) {
                                            ArrayList<Integer> service_list = concept_service.get(iri);
                                            if (service_list == null) {
                                                service_list = new ArrayList<>();

                                            }
                                            service_list.add(ae.getId());
                                            concept_service.put(iri, service_list);

                                        }
                                    }

                                    System.out.println("==========================================");
                                    System.out.println("ID : " + (id - 1));
                                    System.out.println("Provider : " + provider);
                                    System.out.println("Address : " + address);
                                    System.out.println("SDEDescription : " + SDEDescription);
                                    System.out.println("contactDetail : " + contactDetail);
                                    System.out.println("location : " + location);
                                    System.out.println("SDEName : " + SDEName);
                                    System.out.println("IRI List : " + iri_list);

//                                    System.out.println("Concept_Service : " + concept_service);
                                    serviceEntityList.add(ae);
                                }
                            } else {
                                id++;

                                for (String iri : iri_list) {
                                    if (scope_concept_list.contains(iri)) {
                                        ArrayList<Integer> service_list = concept_service.get(iri);
                                        if (service_list == null) {
                                            service_list = new ArrayList<>();

                                        }
                                        service_list.add(ae.getId());
                                        concept_service.put(iri, service_list);

                                    }
                                }

                                if (SDEDescription.equals("unknown;")) {
                                    System.out.println("==========================================");
                                    System.out.println("ID : " + (id - 1));
                                    System.out.println("Provider : " + provider);
                                    System.out.println("Address : " + address);
                                    System.out.println("SDEDescription : " + SDEDescription);
                                    System.out.println("contactDetail : " + contactDetail);
                                    System.out.println("location : " + location);
                                    System.out.println("SDEName : " + SDEName);
                                    System.out.println("IRI List : " + iri_list);

//                                    System.out.println("Concept_Service : " + concept_service);
                                }

                                serviceEntityList.add(ae);
                            }

                        }


                    }
                }
            }

        } catch (OntologyLoadException ex) {
            Logger.getLogger(ReadServiceDescriptionEntities.class.getName()).log(Level.SEVERE, null, ex);
        }

        // write aircraft service entities to file (aircraftEntityList)

        writeServiceEntityToFile(ServiceSelectionPropertiesDefaults.sde_entity_file);
        writeServiceEntitySizeToFile(ServiceSelectionPropertiesDefaults.sde_entity_size_file);
        writeConceptServiceMapToFile(ServiceSelectionPropertiesDefaults.concept_service_file);


    }

    public String cuttedConceptString(String s) {
        int i = 0;
        while (s.indexOf("_", i) != -1) {
            int index = s.indexOf("_", i);
            i = index + 1;
        }

        if (i == 0) {
            return null;
        } else {
            return s.substring(0, i - 1);
        }

    }

    public void writeServiceEntityToFile(String filename) {
        FileOutputStream fout = null;
        ObjectOutputStream oout = null;
        try {
            fout = new FileOutputStream(filename);
            oout = new ObjectOutputStream(fout);

            oout.writeObject(serviceEntityList);
            System.out.println("Service Entity Size : " + serviceEntityList.size());

            oout.close();
            fout.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadServiceDescriptionEntities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadServiceDescriptionEntities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeServiceEntitySizeToFile(String filename) {
        FileOutputStream fout = null;
        ObjectOutputStream oout = null;
        try {
            fout = new FileOutputStream(filename);
            oout = new ObjectOutputStream(fout);

            oout.writeObject(serviceEntityList.size());
            System.out.println("Service Entity Size : " + serviceEntityList.size());

            oout.close();
            fout.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadServiceDescriptionEntities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadServiceDescriptionEntities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeConceptServiceMapToFile(String filename) {
        FileOutputStream fout = null;
        ObjectOutputStream oout = null;
        try {
            fout = new FileOutputStream(filename);
            oout = new ObjectOutputStream(fout);

            oout.writeObject(concept_service);
            System.out.println("Service Entity Size : " + serviceEntityList.size());

            oout.close();
            fout.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadServiceDescriptionEntities.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadServiceDescriptionEntities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
        ReadServiceDescriptionEntities race = new ReadServiceDescriptionEntities();
        race.query();

    }
}
