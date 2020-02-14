package data.struture;

import java.util.Iterator;

public class BTree<T extends Comparable<T>> implements Iterable<T> {
	
	private class Node {
		Node left = null, right = null;
		T value = null;
		
		private Node(T value) {
			this.value = value;
		}
		
		void insert(T value) {
			var cmp = value.compareTo(this.value);
			
			if(cmp == 0) {
				// ignore, already stored
			} else if(cmp < 0) {
				if(left == null)
					left = new Node(value);
				else
					left.insert(value);
			} else {
				if(right == null)
					right = new Node(value);
				else
					right.insert(value);
			}
		}
		
		 boolean contains(T value) {
			 var cmp = value.compareTo(this.value);
				
			if(cmp == 0) 
				return true;
			else if(cmp < 0) {
				if(left == null)
					return false;
				else
					return left.contains(value);
			} else {
				if(right == null)
					return false;
				else
					return right.contains(value);
			}
		}
	}
	
	Node root = null;

	public void insert(T value) {
		if(root == null)
			root = new Node(value);
		else
			root.insert(value);
	}
	
	public boolean contains(T value) {
		if(root == null)
			return false;
		else
			return root.contains(value);
	}
	
	/**
	 * Iterator over nodes using coroutining with threads
	 *
	 */
	class NodeIterator implements Iterator<T>, Runnable {
		Thread thread;
		boolean terminated;
		T nextValue;
		
		NodeIterator() {
			thread = new Thread(this,"Node iterator");
			thread.start();
		}
		
		@Override
		public void run() {
			terminated = false;
			
			visitValues(root);
			
			synchronized (this) {
				terminated = true;
				handshake();
			}
		}
		
		@Override
		public boolean hasNext() {
			synchronized (this) {
				if(! terminated)
					handshake();
			}
			return nextValue != null;
		}

		@Override
		public T next() {
			var value= nextValue;
			
			synchronized (this) {
				nextValue = null;
			}
			return value;
		}
	

		private void visitValues(Node node) {
			if(node.left != null)
				visitValues(node.left);
			
			synchronized (this) {
				if(nextValue != null)
					handshake();
				nextValue = node.value;
				handshake();
			}
			
			if(node.right != null)
				visitValues(node.right);
			
		}

		
		private void handshake() {
			notify();
			try {
				wait();
			} catch (InterruptedException cause) {
				throw new RuntimeException("Unexpected interruption while waiting",cause);
			}
		}

	}
	
	public Iterator<T> iterator()  {
		return new NodeIterator();
	}
	
}
