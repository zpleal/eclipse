package loops;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TrieTest {
	static Trie trie;
	static List<String> names = Arrays.asList("ola","ole","pois", "porta", "portal"); 
	static List<String> others = Arrays.asList("hello","hola","haj", "door"); 

	@BeforeAll
	static void prepare() {
		trie = new Trie();
		
		names.stream().forEach(n -> trie.put(n));
	}
	
	@Test
	void testIterable() {
		var expected = new HashSet<String>(names); 
		var obtained = new HashSet<String>();
		
		
		for(String name: trie) 
			obtained.add(name);
		
		assertEquals(expected,obtained);
		
	}
	
	@Test
	void testContains() {
		
		for(String name: names) 
			assertTrue(trie.contains(name));
		
		for(String name: others) 
			assertFalse(trie.contains(name));
	}

}
