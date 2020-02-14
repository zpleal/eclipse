package loops;

import java.util.ArrayList;
import java.util.List;

public class Loop {
	final static int SIZE = 4;
	Dictionary dictionary = Dictionary.getInstance(SIZE);


	List<String> look4loops() {
		var loops = new ArrayList<String>();
		
		for(var word: dictionary) {
			if(isLoop(word,0))
				loops.add(word);
		}
		
		return loops;
	}


	private boolean isLoop(String word, int pos) {
		if(pos == SIZE-1)
			return true;
		else {
			var next = word.substring(pos, word.length())+word.substring(0,pos);
			
			if(dictionary.contains(next))
				return isLoop(word, pos+1); 
			else
				return false;
		}
	}
	

}
