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
import entity_classes.TransportConcept;

import java.io.File;
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
public class ReadAllTransportConcepts {

	private ArrayList<String> concept_list = new ArrayList<>();
	private HashMap<String, ArrayList<String>> conceptDescList = new HashMap<>();
	private ArrayList<String> selected_concept = new ArrayList<>();
	private ArrayList<TransportConcept> all_concept_objs = new ArrayList<>();

	public void init_test() {
		selected_concept.add("Airline");
		selected_concept.add("Airline_Agent");
		selected_concept.add("Airline_Airline_Agent");
		selected_concept.add("Airline_Booking");
		selected_concept.add("Airline_Quote");
		selected_concept.add("Airline_Ticketing");
		selected_concept.add("Airline_24hr_Turnaround");
		selected_concept.add("Airline_Gift_Vochure");
		selected_concept.add("Airline_Travel_Insurance");
	}

	public ArrayList<TransportConcept> query() {
		ArrayList<TransportConcept> concepts = new ArrayList<TransportConcept>();
		try {
			String uri = ServiceSelectionPropertiesDefaults.owl_file;

			OWLModel owlModel = ProtegeOWL.createJenaOWLModelFromURI(uri);

			Collection classes = owlModel.getUserDefinedOWLNamedClasses();
			owlModel.getUserDefinedOWLObjectProperties();

			// get each concept
			
			for (Iterator it = classes.iterator(); it.hasNext();) {
				OWLNamedClass cls = (OWLNamedClass) it.next();
				Collection instances = cls.getInstances(false);


				if (!cls.getBrowserText().equals("Transport_Service_Description_Entity")) {
					
					for (Iterator jt = instances.iterator(); jt.hasNext();) {
						OWLIndividual individual = (OWLIndividual) jt.next();
						Collection individual_property = individual.getRDFProperties();


						ArrayList<String> serviceDescription = new ArrayList<>();


						int i = 0;

						System.out.println("Class : " + cls.getBrowserText());
						TransportConcept concept = new TransportConcept();
						concept.setName(cls.getBrowserText());
						
						for (Iterator ip = individual_property.iterator(); ip.hasNext();) {
							 //System.out.println("ip : " + ip);          
							// System.out.println("value : " + individual.getPropertyValues((RDFProperty) ip.next()));

							i++;

							switch (i) {
							case 1:

								System.out.println(individual.getPropertyValue((RDFProperty) ip.next()).toString());

								break;
							case 2:
								//   service description
								Collection tmp = individual.getPropertyValues((RDFProperty) ip.next());
								ArrayList<String> conceptServiceDescriptionList = new ArrayList<String>();
								for (Object obj : tmp) {
									serviceDescription.add(obj.toString());
									conceptServiceDescriptionList.add(obj.toString());
								}
								concept.setConcept_desc(conceptServiceDescriptionList);
								System.out.println("serviceDescription : " + serviceDescription);
								break;
							case 3:
								//label
								Collection tmp2 = individual.getPropertyValues((RDFProperty) ip.next());
								for (Object object : tmp2) {
									System.out.println(object.toString());
								}
								break;
							case 4:
								//IRI
								Collection IRIlist = individual.getPropertyValues((RDFProperty) ip.next());
								ArrayList<ServiceDescriptionEntity> sdeList = getIRI(IRIlist);
								concept.setSdeList(sdeList);
								System.out.println(sdeList.size());
								break;
							default:
								ip.next();
								break;

							}

						}
						System.out.println(concept.getName() + ":" + concept.getSdeList().size()+"\n=========================");
						conceptDescList.put(cls.getBrowserText(), serviceDescription);
						concept_list.add(cls.getBrowserText());
						//                        System.out.println(cls.getBrowserText());
						concepts.add(concept);
					}
						
					
				}

					//all concepts
				

			}
			System.out.println(concepts.size());

		} catch (OntologyLoadException ex) {
			Logger.getLogger(ReadAllTransportConcepts.class.getName()).log(Level.SEVERE, null, ex);
		}

		// write aircraft service concepts to file (aircraftEntityList)
		//System.out.println("conceptDescList : " + conceptDescList);

		writeTransportConceptsToFile(ServiceSelectionPropertiesDefaults.all_transport_concept_file);
		writeTransportConceptObjectsToFile(ServiceSelectionPropertiesDefaults.all_transport_object_concept_file);
		return concepts;
	}

	private ArrayList<ServiceDescriptionEntity> getIRI(Collection IRIlist) {
		ArrayList<ServiceDescriptionEntity> sdeList = new ArrayList<ServiceDescriptionEntity>();
		for (Object iri : IRIlist) {
			OWLIndividual iriIndividual = (OWLIndividual) iri;
			Collection iriIndividual_property = iriIndividual.getRDFProperties();
			//System.out.println(iriIndividual_property.toString());
			ServiceDescriptionEntity sde = new ServiceDescriptionEntity();
			String provider = "";
			String address = "";
			String SDEDescription = "";
			String contactDetail = "";
			String location = "";
			String SDEName = "";

			int j =0;
			for (Iterator iriIter = iriIndividual_property.iterator(); iriIter.hasNext();) {
				   //System.out.println(iriIter);  
				j++;

				switch (j) {
				case 1:
			
					iriIndividual.getPropertyValue((RDFProperty) iriIter.next()).toString();
					//				                                        ip.next();
					break;
				case 2:
					//   individual.getPropertyValue((RDFProperty) ip.next()).toString();
					provider = iriIndividual.getPropertyValue((RDFProperty) iriIter.next()).toString();
					sde.setProvider(provider);
					break;
				case 3:
					address = (String) iriIndividual.getPropertyValue((RDFProperty) iriIter.next());
					sde.setAddress(address);
					break;
				case 4:

					SDEDescription = (String) iriIndividual.getPropertyValue((RDFProperty) iriIter.next());
					sde.setSDEDescription(SDEDescription);
					break;
				case 5:

					contactDetail = (String) iriIndividual.getPropertyValue((RDFProperty) iriIter.next());
					sde.setContactDetail(contactDetail);
					break;
				case 6:
					location = (String) iriIndividual.getPropertyValue((RDFProperty) iriIter.next());
					sde.setLocation(location);
					break;
				case 7:

					SDEName = (String) iriIndividual.getPropertyValue((RDFProperty) iriIter.next());
					sde.setSDEName(SDEName);
					break;

				default:
					iriIter.next();
					break;
				}
			}
			sdeList.add(sde);
			System.out.println(SDEName + " : "+provider +" : "+ SDEDescription );
		}
		return sdeList;
		
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

		all_concept_objs = createConceptObjList(concept_list, conceptDescList);

		FileOutputStream fout = null;
		ObjectOutputStream oout = null;
		try {
			fout = new FileOutputStream(filename);
			oout = new ObjectOutputStream(fout);

			oout.writeObject(all_concept_objs);

			System.out.println("Aircraft Size : " + conceptDescList.size());
			System.out.println("concept_list : " + concept_list);
			System.out.println("conceptDest : " + conceptDescList);

			oout.close();
			fout.close();
		} catch (FileNotFoundException ex) {
			Logger.getLogger(ReadAllTransportConcepts.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(ReadAllTransportConcepts.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void writeTransportConceptsToFile(String filename) {
		FileOutputStream fout = null;

		ObjectOutputStream oout = null;
		try {

			File fileToWrite = new File(filename);
			boolean testFile = fileToWrite.exists();

			if (!fileToWrite.exists()) {
				fileToWrite.createNewFile();
			}
			fout = new FileOutputStream(fileToWrite);
			oout = new ObjectOutputStream(fout);

			oout.writeObject(concept_list);
			oout.writeObject(conceptDescList);

			System.out.println("Transport Concept Size : " + conceptDescList.size());
			System.out.println("concept_list : " + concept_list);
			System.out.println("conceptDest : " + conceptDescList);

			oout.close();
			fout.close();
		} catch (FileNotFoundException ex) {
			Logger.getLogger(ReadAllTransportConcepts.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(ReadAllTransportConcepts.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void main(String[] args) {
		// TODO code application logic here
		ReadAllTransportConcepts obj = new ReadAllTransportConcepts();
		obj.init_test();
		obj.query();
	}
}
