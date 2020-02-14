package data.struture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class BTreeTest {
	static List<Integer> in  = Arrays.asList(1,3,6,8);
	static List<Integer> out = Arrays.asList(2,4,5,7);
	
	BTree<Integer> btree;
	
	@BeforeEach
	void setUp() throws Exception {
		btree = new BTree<Integer>();
		
		Collections.shuffle(in);
		
		in.forEach(v -> btree.insert(v));
	}


	@Test
	void testContains() {
		for(var value: in) 
			assertTrue(btree.contains(value));
		
		for(var value: out) 
			assertFalse(btree.contains(value));
		
	}
	
	@Test
	void testLarge() {
		var descBtree = new BTree<Integer>();
		var expected = new ArrayList<Integer>();
		var obtained = new ArrayList<Integer>();
		
		for(var n = 5000; n > 0; n--) {
			descBtree.insert(n);
			expected.add(n);
		};
		Collections.sort(expected);

		
		for(var value: descBtree)
			obtained.add(value);
		
		assertEquals(expected,obtained);
		
	}
	
	
	@RepeatedTest(5000)
	void testIterator() {
		var expected = new ArrayList<>(in);
		var obtained = new ArrayList<>();
		
		Collections.sort(expected);
		
		for(var value: btree) 
			obtained.add(value);
		
		// System.out.println(expected+" "+obtained);
		
		assertEquals(expected,obtained,"with "+in.toString());
	}

}
