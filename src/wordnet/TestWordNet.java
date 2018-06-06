/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wordnet;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Pointer;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.PointerUtils;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.list.PointerTargetNodeList;
import net.didion.jwnl.data.list.PointerTargetTree;
import net.didion.jwnl.data.relationship.AsymmetricRelationship;
import net.didion.jwnl.data.relationship.Relationship;
import net.didion.jwnl.data.relationship.RelationshipFinder;
import net.didion.jwnl.data.relationship.RelationshipList;
import net.didion.jwnl.dictionary.Dictionary;

/**
 *
 * @author kikitt
 */
public class TestWordNet {

    private IndexWord ACCOMPLISH;
    private IndexWord DOG;
    private IndexWord CAT;
    private IndexWord FUNNY;
    private IndexWord DROLL;
    private String MORPH_PHRASE = "running-away";

    public TestWordNet() throws JWNLException {
        ACCOMPLISH = Dictionary.getInstance().getIndexWord(POS.VERB, "accomplish");
        DOG = Dictionary.getInstance().getIndexWord(POS.NOUN, "dog");
        CAT = Dictionary.getInstance().lookupIndexWord(POS.NOUN, "cat");
        FUNNY = Dictionary.getInstance().lookupIndexWord(POS.ADJECTIVE, "funny");
        DROLL = Dictionary.getInstance().lookupIndexWord(POS.ADJECTIVE, "droll");
    }

    public void go() throws JWNLException {
//        demonstrateMorphologicalAnalysis(MORPH_PHRASE);
//        demonstrateListOperation(ACCOMPLISH);
//        demonstrateTreeOperation(DOG);
//        demonstrateAsymmetricRelationshipOperation(DOG, CAT);
        demonstrateSymmetricRelationshipOperation(FUNNY, DROLL);
    }

    private void demonstrateMorphologicalAnalysis(String phrase) throws JWNLException {
        // "running-away" is kind of a hard case because it involves
        // two words that are joined by a hyphen, and one of the words
        // is not stemmed. So we have to both remove the hyphen and stem
        // "running" before we get to an entry that is in WordNet
        System.out.println("Base form for \"" + phrase + "\": "
                + Dictionary.getInstance().lookupIndexWord(POS.VERB, phrase));
    }

    private void demonstrateListOperation(IndexWord word) throws JWNLException {
        // Get all of the hypernyms (parents) of the first sense of <var>word</var>
        PointerTargetNodeList hypernyms = PointerUtils.getInstance().getDirectHypernyms(word.getSense(1));
        System.out.println("Direct hypernyms of \"" + word.getLemma() + "\":");
        hypernyms.print();
    }

    private void demonstrateTreeOperation(IndexWord word) throws JWNLException {
        // Get all the hyponyms (children) of the first sense of <var>word</var>
        PointerTargetTree hyponyms = PointerUtils.getInstance().getHyponymTree(word.getSense(1));
        System.out.println("Hyponyms of \"" + word.getLemma() + "\":");
        hyponyms.print();
    }

    private void demonstrateAsymmetricRelationshipOperation(IndexWord start, IndexWord end) throws JWNLException {
        // Try to find a relationship between the first sense of <var>start</var> and the first sense of <var>end</var>
        RelationshipList list = RelationshipFinder.getInstance().findRelationships(start.getSense(1), end.getSense(1), PointerType.HYPERNYM);
        System.out.println("Hypernym relationship between \"" + start.getLemma() + "\" and \"" + end.getLemma() + "\":");
        for (Iterator itr = list.iterator(); itr.hasNext();) {
            ((Relationship) itr.next()).getNodeList().print();
        }
        System.out.println("Common Parent Index: " + ((AsymmetricRelationship) list.get(0)).getCommonParentIndex());
        System.out.println("Depth: " + ((Relationship) list.get(0)).getDepth());
    }

    private void demonstrateSymmetricRelationshipOperation(IndexWord start, IndexWord end) throws JWNLException {
        // find all synonyms that <var>start</var> and <var>end</var> have in common
        RelationshipList list = RelationshipFinder.getInstance().findRelationships(start.getSense(1), end.getSense(1), PointerType.SIMILAR_TO);
        System.out.println("Synonym relationship between \"" + start.getLemma() + "\" and \"" + end.getLemma() + "\":");
        for (Iterator itr = list.iterator(); itr.hasNext();) {
            ((Relationship) itr.next()).getNodeList().print();
        }
        System.out.println("Depth: " + ((Relationship) list.get(0)).getDepth());
    }

    public void testMorphological() {
        try {
            JWNL.initialize(WordNetPropertiesDefaults.getInputStream());
            IndexWord iw = Dictionary.getInstance().lookupIndexWord(POS.VERB, "airfare");

            System.out.println("Index word : " + iw.toString());

        } catch (JWNLException e) {
            e.printStackTrace();
        }


    }

//    public void testSynSet() {
//        try {
//            ArrayList<String> synonyms = new ArrayList<>();
//
//            Dictionary dictionary = Dictionary.getInstance();
//            IndexWord word = dictionary.lookupIndexWord(POS.NOUN, "wing");
//            Set<String> oSyn = lookupSynonyms(args[0]);
//
//            String wordForm = "make";
//            Synset[] synsets = dictionary.getSynsets(word, SynsetType.VERB);
//            if (synsets.length > 0) {
//                for (int i = 0; i < synsets.length; i++) {
//                    String[] wordForms = synsets[i].getWordForms();
//                    for (int j = 0; j < wordForms.length; j++) {
//                        if (!synonyms.contains(wordForms[j])) {
//                            synonyms.add(wordForms[j]);
//                        }
//                    }
//                }
//            }
//        } catch (JWNLException ex) {
//            Logger.getLogger(TestWordNet.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }
    public static ArrayList<String> lookupSynonyms(String lexicalForm) throws JWNLException {
        ArrayList<String> synonyms = new ArrayList<>();

        Dictionary dictionary = Dictionary.getInstance();
        IndexWord indexWord = dictionary.getIndexWord(
                POS.NOUN, lexicalForm);
        if (indexWord == null) {
            return synonyms;
        }
        Synset[] synSets = indexWord.getSenses();
        for (Synset synset : synSets) {
            net.didion.jwnl.data.Word[] words = synset.getWords();
            for (net.didion.jwnl.data.Word word : words) {
                if (!synonyms.contains(word.getLemma())) {
                    synonyms.add(word.getLemma());
                }

            }
        }
        synonyms.remove(lexicalForm);
        return synonyms;
    }

    public void testSenses(String w) {
        try {
//            String w = "storage";
            Dictionary dictionary = Dictionary.getInstance();
            IndexWord word = dictionary.lookupIndexWord(POS.NOUN, w);
            System.out.println("Senses of the word '" + w + "' : ");

            try {
                Synset[] senses = word.getSenses();
                for (int i = 0; i < senses.length; i++) {
                    Synset sense = senses[i];
                    System.out.println("");
                    System.out.println("********************************************************");
                    System.out.println((i + 1) + ". " + sense.getGloss());
                    System.out.println("********************************************************");
                    System.out.println("");

                    System.out.println("=========Synonym========================");
//                    ArrayList<String> synonyms = new ArrayList<>();
                    net.didion.jwnl.data.Word[] words = sense.getWords();
                    System.out.println("Synset Size : " + sense.getWordsSize());
                    for (net.didion.jwnl.data.Word each_word : words) {
//                        if (!synonyms.contains(each_word.getLemma())) {
//                            synonyms.add(each_word.getLemma());
//                        }
                        System.out.println(each_word.getLemma());
                    }
//                    System.out.println(synonyms);

                    Pointer[] holo = sense.getPointers(PointerType.HYPERNYM);
                    System.out.println("=========Hypernym=======================");
                    for (int j = 0; j < holo.length; j++) {
                        Synset synset = (Synset) (holo[j].getTarget());
                        System.out.println("Hyper " + j + " : " + synset.getGloss());
                        System.out.println("--------------------------------------");
                        System.out.println("Synset Size : " + synset.getWordsSize());
                        net.didion.jwnl.data.Word[] hyper_words = synset.getWords();
                        for (net.didion.jwnl.data.Word hyper : hyper_words) {
                            System.out.println(hyper.getLemma());
                            Synset test_hyper = hyper.getSynset();
                            Pointer[] holo_parent = test_hyper.getPointers(PointerType.HYPERNYM);
                            for (int k = 0; k < holo_parent.length; k++) {
                                Synset synset_parent = (Synset) (holo_parent[k].getTarget());
                                System.out.println("Parent-Hyper " + k + " : " + synset_parent.getGloss());
                                System.out.println("--------------------------------------");
                                System.out.println("Synset Size : " + synset_parent.getWordsSize());
                                net.didion.jwnl.data.Word[] hyper_parent_words = synset_parent.getWords();
                                for (net.didion.jwnl.data.Word hyper_parent : hyper_parent_words) {
                                    System.out.println(hyper_parent.getLemma());
                                }
                            }
                        }
//                        net.didion.jwnl.data.Word synsetWord = synset.getWord(0);
//                        System.out.println(synsetWord.getLemma());
//                        System.out.println(" = " + synset.getGloss());
                    }

                    Pointer[] mero = sense.getPointers(PointerType.HYPONYM);
                    System.out.println("=========Hyponym=======================");
                    for (int j = 0; j < mero.length; j++) {
                        Synset synset = (Synset) (mero[j].getTarget());
                        System.out.println("Hypo " + j + " : " + synset.getGloss());
                        System.out.println("--------------------------------------");
                        System.out.println("Synset Size : " + synset.getWordsSize());
                        net.didion.jwnl.data.Word[] hypo_words = synset.getWords();
                        for (net.didion.jwnl.data.Word hypo : hypo_words) {
                            System.out.println(hypo.getLemma());
                        }


//                        net.didion.jwnl.data.Word synsetWord1 = synset.getWord(0);
//                        System.out.println(synsetWord1.getLemma());
//                        System.out.println(" = " + synset.getGloss());
                    }
                }
            } catch (NullPointerException e) {
                System.out.println("No meaning");
            }



        } catch (JWNLException ex) {
            Logger.getLogger(TestWordNet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        // new TestWordNet().testMorphological();
        String propsFile = System.getProperty("user.dir") + "/wordnet_config/file_properties.xml";
        try {
            // initialize JWNL (this must be done before JWNL can be used)
            System.out.println("propsFile : " + propsFile);
            JWNL.initialize(new FileInputStream(propsFile));
//            new TestWordNet().go();
            new TestWordNet().testSenses("booking");
//            ArrayList<String> synonym = new TestWordNet().lookupSynonyms("victoria&#039");
//            System.out.println("synonym : " + synonym);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }
}
