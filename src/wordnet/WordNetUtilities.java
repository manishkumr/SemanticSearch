/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wordnet;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.*;
import net.didion.jwnl.dictionary.Dictionary;

/**
 *
 * @author kikitt
 */
public class WordNetUtilities {

	/**
	 * @param args the command line arguments
	 */
	//    public boolean isStrongWord(Synset[] senses){
	//        if (senses.length == 1) {
	//            return true;
	//        } else {
	//            return false;
	//        }
	//    }
	//    
	//    public boolean isWeakWord(Synset[] senses){
	//        if (senses.length > 1) {
	//            return true;
	//        } else {
	//            return false;
	//        }
	//    }
	//    
	//    public boolean hasMeaning(Synset[] senses){
	//        if (senses != null){
	//            return true;
	//        } else {
	//            return false;
	//        }
	//    }
	public static Synset[] getWordSenses(String input_word) {
		Synset[] senses = null;
		try {
			Dictionary dictionary = Dictionary.getInstance();
			IndexWord word = dictionary.lookupIndexWord(POS.NOUN, input_word);
			//System.out.println("Senses of the word '" + input_word + "':");
			senses = word.getSenses();

			if (senses == null) {
				word = dictionary.lookupIndexWord(POS.ADJECTIVE, input_word);
				senses = word.getSenses();
			} else {                
				//System.out.println(senses[0]);
			}


		} catch (JWNLException ex) {
			Logger.getLogger(TestWordNet.class.getName()).log(Level.SEVERE, null, ex);
		} catch (NullPointerException ex) {
			//            System.out.println("No meaning");
		}
		return senses;
	}

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

	public static ArrayList<String> lookupSynonyms(String lexicalForm, int sense) throws JWNLException {
		ArrayList<String> synonyms = new ArrayList<>();

		Dictionary dictionary = Dictionary.getInstance();

		//        System.out.println("lexicalForm : " + lexicalForm);

		IndexWord indexWord = dictionary.getIndexWord(POS.NOUN, lexicalForm);


		if (indexWord == null) {
			return synonyms;
		}
		Synset[] synSets = indexWord.getSenses();

		net.didion.jwnl.data.Word[] words = synSets[sense].getWords();
		for (net.didion.jwnl.data.Word word : words) {
			if (!synonyms.contains(word.getLemma())) {
				synonyms.add(word.getLemma());
			}

		}

		synonyms.remove(lexicalForm);
		return synonyms;
	}

	public static void init() {
		try {
			// initialize JWNL (this must be done before JWNL can be used)
			JWNL.initialize(WordNetPropertiesDefaults.getInputStream());
			getWordSenses("storage");
			System.out.println(lookupSynonyms("ticket",1));


		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(-1);
		}
	}

	public static void main(String[] args) {
		// TODO code application logic here
		new WordNetUtilities().init();

	}
}
