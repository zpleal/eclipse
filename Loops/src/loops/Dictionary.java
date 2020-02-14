package loops;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import loops.Trie.Search;


public class Dictionary implements Iterable<String> {
	
	 private static final String DICT_FILE = "loops" + File.separator + "pt-PT-AO.dic";
	 private static final Map<Integer,Dictionary> dictionaries = new HashMap<>();
	 
	 public static Dictionary getInstance(int wordSize) {
		 Dictionary dictionary = dictionaries.get(wordSize);
		 
		 if(dictionary == null)
			 dictionaries.put(wordSize, dictionary = new Dictionary(wordSize));
		 
		 return dictionary;
	 }
	 
	 private Trie trie = new Trie();
	 
	 private Dictionary(int wordSize) {
		 
		 try (
		 InputStream stream = ClassLoader.getSystemResourceAsStream(DICT_FILE);
		 BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
				 ) {
			 loadWords(reader, wordSize);
		 } catch(Exception e) {
			 e.printStackTrace(System.err);
		 }
	 }
	 
	 private Pattern slashOrWhiteCharpattern = Pattern.compile("/|\\s");
	 private Pattern allLetterPattern = Pattern.compile("[A-Z]+");
	 private void loadWords(BufferedReader reader, int wordSize) throws IOException {
		 String line; // first line is number of lines
		 while((line = reader.readLine()) != null) 
			 if(Character.isAlphabetic(line.charAt(0))) {
				 String word = slashOrWhiteCharpattern.split(line)[0];
				 if(word.length() < 3 || word.length() != wordSize)
					 continue;
				 // Give this as an hint to students 
				 word = Normalizer.normalize(word.toUpperCase(Locale.ENGLISH),Form.NFD).
						 replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
				 if(! allLetterPattern.matcher(word).matches())
					 continue;
				 trie.put(word);
			 }
	 }
	 
	 public Search startSearch() {
		 return trie.startSearch();
	 }
	 
	 
	 public String getRandomLargeWord() {
		 return trie.getRandomLargeWord();
	 }

	 public boolean contains(String name) {
		 return trie.contains(name);
	 }
	 
	@Override
	public Iterator<String> iterator() {
		return trie.iterator();
	}
}
