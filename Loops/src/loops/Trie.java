package loops;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class Trie implements Iterable<String> {
	
	private Node root = new Node();
	
	public static class Search {
		Node node;

		public Search(Node node) {
			this.node = node;
		}
		
		public Search(Search search) {
			this.node = search.node;
		}
		
		boolean continueWith(char letter) {
			if(node == null)
				return false;
			if(node.containsKey(letter))
				node = node.get(letter);
			else
				node = null;
			return node != null;
		}
	
		boolean isWord() {
			return node != null && node.isWord;
		}
	}
	
	private static class Node extends HashMap<Character,Node> {
		private static final long serialVersionUID = 1L;
		boolean isWord = false;
		
		private void put(String name, int index) {
			if (index == name.length()) {
				isWord = true;
			} else {
				char head = name.charAt(index);
				Node cont;
				
				if (containsKey(head)) {
					cont = get(head);
				} else {
					cont = new Node();
					put(head, cont);
				}
				cont.put(name, index + 1);
			}
		}

		Random randon = new Random();
		
		private void collectRandomLargeWord(StringBuffer buffer) {
			
			ArrayList<Character> chs = new ArrayList<Character>(keySet());
			if(chs.size() == 0)
				return;
			else {
				char ch = chs.get(randon.nextInt(chs.size()));
				buffer.append(ch);
				get(ch).collectRandomLargeWord(buffer);
			}
		
		}			
		
		private boolean contains(String name, int index) {
			if (index == name.length()) 
				return true;
			else {
				char head = name.charAt(index);
				
				if (containsKey(head)) 
					return get(head).contains(name, index + 1);
				else 
					return false;
			}
		}
	}
	
	public void put(String name) {
		root.put(name, 0);
	}

	public Search startSearch() {
		return new Search(root);
	}	
	
	public String getRandomLargeWord() {
		StringBuffer buffer = new StringBuffer();
		root.collectRandomLargeWord(buffer);
		return buffer.toString();
	}
	
	public boolean contains(String name) {
		return root.contains(name, 0);
	}

	/**
	 * Iterator over nodes using coroutining with threads
	 *
	 */
	class NodeIterator implements Iterator<String>, Runnable {
		Thread thread;
		boolean terminated;
		String nextWord;
		
		NodeIterator() {
			thread = new Thread(this,"Node iterator");
			thread.start();
		}
		
		@Override
		public void run() {
			terminated = false;
			
			visitWords(root, new StringBuilder());
			
			terminated = true;
			
			synchronized (this) {
				handshake();
			}
		}
		
		@Override
		public boolean hasNext() {
			synchronized (this) {
				if(! terminated)
					handshake();
			}
			return nextWord != null;
		}

		@Override
		public String next() {
			var word = nextWord;

			synchronized (this) {
				nextWord = null;
			}
			
			return word;
		}
	
		private void visitWords(Node node,StringBuilder builder) {
			
			if(node.isWord) {
				nextWord = builder.toString();
				synchronized (this) {
					handshake();
				}
			}
			
			for(var entry: node.entrySet()) {
				var entryBuilder = new StringBuilder(builder);
				
				entryBuilder.append(entry.getKey());
				visitWords(entry.getValue(), entryBuilder);
			}
			
		}
		
		private void handshake() {
				try {
					notify();
					wait();
				} catch (InterruptedException e) {
					throw new RuntimeException("Unexpected interruption while waiting",e);
				}
		}

	
	}
	
	public Iterator<String> iterator()  {
		return new NodeIterator();
	}
	
	
}