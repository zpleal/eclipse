package loops;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class LoopTest {

	@Test
	void testLook4loops() {
		for(var word: new Loop().look4loops())
			System.out.println(word);
	}
	
	@Disabled
	@Test
	void listAllWords() {
		for(var word: new Loop().dictionary)
			System.out.println(word);
	}

}
