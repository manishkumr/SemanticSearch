/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package properties;

import java.util.ArrayList;

/**
 *
 * @author kikitt
 */
public class ServiceSelectionPropertiesDefaults {
    
    public static boolean norm = false;
    public static double sc_link_threshold = 0; // service_concept_linkage_threshold --> LT
    public static double select_threshold = 0.8;
    
    public static boolean focusAllConcept = true; // focus all concepts or just leaf nodes
    
    public static String sde_concept_sim = "ECBR"; // cosine or ECBR
    public static String query_concept_sim = "ECBR"; // cosine or ECBR
   
// File Path ...    
    
    public static String p = System.getProperty("user.dir");
    public static String path = p.replace('\\', '/');
    
    public static String output_path = path + "/outputs";
    
    public static String owl_file = "file:///" + path + "/owl_file/Transport_Service_Ontology.owl";
    
    public static String concept_scope_file = "all"; 
//    public static String concept_scope_file = path + "/concept_scope/aircraft.txt";
    // all or scope filename e.g. path + "/concept_scope/aircraft.txt"
    
    public static String filename_stopword = path + "/stopword/stopword.txt";
    public static String filename_search_history = path + "/user_history/user_search_history.csv";
    public static String output_sim_path = output_path + "/annotation_" + sde_concept_sim;
    
    public static String concept_path = output_sim_path + "/concept_files";
    public static String concept_mapping_path = output_sim_path + "/concept_mapping_files";
    public static String entity_path = output_sim_path + "/entity_files";
    public static String link_path = output_sim_path + "/linking_files";
    public static String link_path_norm = output_sim_path + "/linking_files/norm_vector";
    public static String link_path_no_norm = output_sim_path + "/linking_files/no_norm_vector";
    
    public static String annotation_path = output_sim_path + "/annotation_files";
    public static String annotation_path_norm_vsm = output_sim_path + "/annotation_files/norm_vector/vsm";
    public static String annotation_path_no_norm_vsm = output_sim_path + "/annotation_files/no_norm_vector/vsm";
    public static String annotation_path_norm_fiscrm = output_sim_path + "/annotation_files/norm_vector/fiscrm";
    public static String annotation_path_no_norm_fiscrm = output_sim_path + "/annotation_files/no_norm_vector/fiscrm";
    
    // Concept files
    public static String all_transport_concept_file = concept_path + "/allTransportConcepts.dat";
    public static String leaf_transport_concept_file = concept_path + "/leafTransportConcepts.dat";
    public static String output_transport_concept_file = concept_path + "/outputTransportConcepts.dat";
    
    public static String all_transport_object_concept_file = concept_path + "/allTransportObjectConcepts.dat";
    public static String leaf_transport_object_concept_file = concept_path + "/leafTransportObjectConcepts.dat";
    public static String output_transport_object_concept_file = concept_path + "/outputTransportObjectConcepts.dat";
    
    // Service Entity files
    public static String sde_entity_file = entity_path + "/sdeEntity.dat";
    public static String sde_entity_size_file = entity_path + "/sdeEntitySize.dat";
    public static String concept_service_file = entity_path + "/concept_service.dat";
    public static String service_neccessary_file = entity_path + "/serviceNeccessary.dat";
    
    // Concept Mapping
    public static String mapping_output_file = concept_mapping_path + "/mappingConceptOutput.dat";
    
    // Service-Concept Linking 
    public static String serviceConceptLink_file_norm = link_path_norm + 
            "/serviceConceptLinkOutput_lt_" + sc_link_threshold + ".dat";
    public static String serviceConceptLink_file_no_norm = link_path_no_norm + 
            "/serviceConceptLinkOutput_lt_" + sc_link_threshold + ".dat";
    
    // Service-Concept Annotation
    public static String serviceAnnotation_file_norm_vsm = annotation_path_norm_vsm + 
            "/serviceAnnotation_lt_" + sc_link_threshold + ".dat";
    public static String serviceAnnotation_file_no_norm_vsm = annotation_path_no_norm_vsm + 
            "/serviceAnnotation_lt_" + sc_link_threshold + ".dat";
    public static String serviceAnnotation_file_norm_fiscrm = annotation_path_norm_fiscrm + 
            "/serviceAnnotation_lt_" + sc_link_threshold + ".dat";
    public static String serviceAnnotation_file_no_norm_fiscrm = annotation_path_no_norm_fiscrm + 
            "/serviceAnnotation_lt_" + sc_link_threshold + ".dat";
    
    public static String serviceAnnotation_file_norm_vsm_txt = annotation_path_norm_vsm + 
            "/serviceAnnotation_lt_" + sc_link_threshold + ".txt";
    public static String serviceAnnotation_file_no_norm_vsm_txt = annotation_path_no_norm_vsm + 
            "/serviceAnnotation_lt_" + sc_link_threshold + ".txt";
    public static String serviceAnnotation_file_norm_fiscrm_txt = annotation_path_norm_fiscrm + 
            "/serviceAnnotation_lt_" + sc_link_threshold + ".txt";
    public static String serviceAnnotation_file_no_norm_fiscrm_txt = annotation_path_no_norm_fiscrm + 
            "/serviceAnnotation_lt_" + sc_link_threshold + ".txt";
    
    // Service-Concept Annotation - Similarity values
    public static String serviceAnnotationSim_file_norm_vsm = annotation_path_norm_vsm + 
            "/serviceAnnotationSim_lt_" + sc_link_threshold + ".dat";
    public static String serviceAnnotationSim_file_no_norm_vsm = annotation_path_no_norm_vsm + 
            "/serviceAnnotationSim_lt_" + sc_link_threshold + ".dat";
    public static String serviceAnnotationSim_file_norm_fiscrm = annotation_path_norm_fiscrm + 
            "/serviceAnnotationSim_lt_" + sc_link_threshold + ".dat";
    public static String serviceAnnotationSim_file_no_norm_fiscrm = annotation_path_no_norm_fiscrm + 
            "/serviceAnnotationSim_lt_" + sc_link_threshold + ".dat";
    
    // Answer Set
    public static String answerset_file = path + "/TestData_Query/QA4.txt";
    
    public static String qa0 = path + "/TestData_Query/QA0.txt";
    public static String qa1 = path + "/TestData_Query/QA1.txt";
    public static String qa2 = path + "/TestData_Query/QA2.txt";
    public static String qa3 = path + "/TestData_Query/QA3.txt";
    public static String qa4 = path + "/TestData_Query/QA4.txt";
    public static String qa5 = path + "/TestData_Query/QA5.txt";
    public static String qa6 = path + "/TestData_Query/QA6.txt";
    public static String qa7 = path + "/TestData_Query/QA7.txt";

    
    // Terms
    public static String conceptTermsList = path + "/Dataset/conceptTermsList.dat";
    public static String conceptsList = path + "/Dataset/conceptsList.dat";
    public static String conceptTermsDataset = path + "/Dataset/transport_dataset.data";
    
    // Concept Description Structure
    public static String descStruct = path + "/Dataset/conceptDescStruct.dat";
    public static String wordStruct = path + "/Dataset/conceptWordStruct.dat";
    
    // KMeans Clusters for Concept Terms
    public static String kmeans_wupalm = path + "/Dataset/KMeansTerm/WuPalmer/sem_0_ont_1/clusters.dat";
    
    // Ontology based clusters for concept terms
    public static String ontology_cluster = path + "/Dataset/OntologyCluster/level_2/clusters.dat";
    // Data set
    public static String dataset = path + "/Dataset/dataset.dat";
    public static String dataset_without_unknown = path + "/Dataset/dataset_without_unknown.dat";
    
    public static String dataset_single_label = path + "/Dataset/dataset_single_label.dat";
    public static String dataset_single_label_without_unknown = path + "/Dataset/dataset_single_label_without_unknown.dat";
    
    // Dataset for word clusters
    public static String dataset_cluster = path + "/Dataset/dataset_cluster.dat";
    public static String dataset_without_unknown_cluster = path + "/Dataset/dataset_without_unknown_cluster.dat";
    
    public static String dataset_cluster_nonempty = path + "/Dataset/dataset_cluster_nonempty.dat";
    public static String dataset_without_unknown_cluster_nonempty = path + "/Dataset/dataset_without_unknown_cluster_nonempty.dat";
    
    public static String dataset_single_label_cluster = path + "/Dataset/dataset_single_label_cluster.dat";
    public static String dataset_single_label_without_unknown_cluster = path + "/Dataset/dataset_single_label_without_unknown_cluster.dat";
    
    public static String dataset_single_label_cluster_nonempty = path + "/Dataset/dataset_single_label_cluster_nonempty.dat";
    public static String dataset_single_label_without_unknown_cluster_nonempty = path + "/Dataset/dataset_single_label_without_unknown_cluster_nonempty.dat";
    
    // Dataset for Matlab
    public static String input_Matlab = path + "/Dataset/input_matlab.txt";
    public static String output_Matlab_prefix = path + "/Dataset/output_matlab/";
    
    public static String input_Matlab_without_unknown = path + "/Dataset/input_matlab_without_unknown.txt";
    public static String output_Matlab_without_unknown_prefix = path + "/Dataset/output_matlab_without_unknown/";
    
    public static String input_Matlab_single_label = path + "/Dataset/input_matlab_single_label.txt";
    public static String output_Matlab_single_label = path + "/Dataset/output_matlab_single_label.txt";
    
    public static String input_Matlab_single_label_without_unknown = path + "/Dataset/input_matlab_single_label_without_unknown.txt";
    public static String output_Matlab_single_label_without_unknown = path + "/Dataset/output_matlab_single_label_without_unknown.txt";

    // Dataset for Matlab with term clusters
    public static String input_Matlab_cluster = path + "/Dataset/input_matlab_cluster.txt";
    public static String output_Matlab_prefix_cluster = path + "/Dataset/output_matlab_cluster/";
    
    public static String input_Matlab_cluster_nonempty = path + "/Dataset/input_matlab_cluster_nonempty.txt";
    public static String output_Matlab_prefix_cluster_nonempty = path + "/Dataset/output_matlab_cluster_nonempty/";
    
    public static String input_Matlab_without_unknown_cluster = path + "/Dataset/input_matlab_without_unknown_cluster.txt";
    public static String output_Matlab_without_unknown_prefix_cluster = path + "/Dataset/output_matlab_without_unknown_cluster/";
    
    public static String input_Matlab_without_unknown_cluster_nonempty = path + "/Dataset/input_matlab_without_unknown_cluster_nonempty.txt";
    public static String output_Matlab_without_unknown_prefix_cluster_nonempty = path + "/Dataset/output_matlab_without_unknown_cluster_nonempty/";
    
    public static String input_Matlab_single_label_cluster = path + "/Dataset/input_matlab_single_label_cluster.txt";
    public static String output_Matlab_single_label_cluster = path + "/Dataset/output_matlab_single_label_cluster.txt";
    
    public static String input_Matlab_single_label_cluster_nonempty = path + "/Dataset/input_matlab_single_label_cluster_nonempty.txt";
    public static String output_Matlab_single_label_cluster_nonempty = path + "/Dataset/output_matlab_single_label_cluster_nonempty.txt";
    
    public static String input_Matlab_single_label_without_unknown_cluster = path + "/Dataset/input_matlab_single_label_without_unknown_cluster.txt";
    public static String output_Matlab_single_label_without_unknown_cluster = path + "/Dataset/output_matlab_single_label_without_unknown_cluster.txt";
    
    public static String input_Matlab_single_label_without_unknown_cluster_nonempty = path + "/Dataset/input_matlab_single_label_without_unknown_cluster_nonempty.txt";
    public static String output_Matlab_single_label_without_unknown_cluster_nonempty = path + "/Dataset/output_matlab_single_label_without_unknown_cluster_nonempty.txt";
    
    // Testing data : contains list of concepts
    public static String output_ecbr_single_label = path + "/Dataset/TestingData/SingleLabel/output_ECBR.txt";
    public static String output_ann_single_label = path + "/Dataset/TestingData/SingleLabel/output_ANN.txt";
    public static String target_single_label = path + "/Dataset/TestingData/SingleLabel/target.txt";
    public static String all_target_dataset = path + "/Dataset/TestingData/SingleLabel/target_dataset.txt";
    
    // Testing data : contains list of concepts -- Multilabel
    public static String output_ecbr_multi_label = path + "/Dataset/TestingData/MultiLabel/output_ECBR_multi.dat";
    public static String output_ann_multi_label = path + "/Dataset/TestingData/MultiLabel/output_ANN_multi.dat";
    public static String target_multi_label = path + "/Dataset/TestingData/MultiLabel/target_multi.dat";
    
    // Output Ann from file
     public static String output_ann_single_label_detail = path + "/Dataset/Output_Matlab/testY_100_without_unknown.txt";
     public static String output_ann_multi_label_result_noncluster = path + "/Dataset/DatasetForMatlab/non_cluster/_result/";
     public static String output_ann_multi_label_result_cluster = path + "/Dataset/DatasetForMatlab/cluster/_result/";
     public static String ann_output_path = path + "/Dataset/Output_ANN/";

// Data ....
    
}
