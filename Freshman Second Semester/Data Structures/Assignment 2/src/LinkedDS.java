
public class LinkedDS<T> implements PrimQ<T>, Reorder {

	protected Node firstNode;

	public LinkedDS(){
		firstNode = null;
	}

	public LinkedDS(LinkedDS<T> oldList) {
		if(oldList.firstNode == null) {
			firstNode = null;
			return;
		}
		Node currentNode = oldList.firstNode;
		while(currentNode
				!= null) {
			addItem(currentNode.getData());
			currentNode = currentNode.getNextNode();
		}
	}

	public boolean addItem(T newEntry) {
		if(firstNode == null) {
			firstNode = new Node(newEntry);
			return true;
		}
		Node currentNode = firstNode;
		while(currentNode.getNextNode() != null) {
			currentNode = currentNode.getNextNode();
		}
		currentNode.setNextNode(new Node(newEntry));
		return true;
	}

	public T removeItem() {
		if(firstNode == null)
			return null;
		Node node = firstNode;
		if(firstNode.getNextNode() == null) {
			firstNode = null;
		}else {
			firstNode = firstNode.getNextNode();
		}
		return node.getData();
	}

	public boolean empty() {
		return firstNode == null;
	}

	public int size() {
		int count = 0;
		Node currentNode = firstNode;
		while(currentNode != null) {
			count++;
			currentNode = currentNode.getNextNode();
		}
		return count;
	}

	public void clear() {
		firstNode = null;
	}

	@Override
	public void reverse() {
		Node currentNode = firstNode, previousNode = null, nextNode = null;
		while(currentNode != null) {
			nextNode = currentNode.getNextNode();
			currentNode.setNextNode(previousNode);
			previousNode = currentNode;
			currentNode = nextNode;
		}
		firstNode = previousNode;

	}

	@Override
	public void shiftRight() {
		int length = size();
		Node currentNode = firstNode;
		for(int i = 1; i < length-1; i++) {
			currentNode = currentNode.getNextNode();
		}
		currentNode.setNextNode(null);
	}

	@Override
	public void shiftLeft() {
		if(firstNode != null)
			firstNode = firstNode.getNextNode();

	}

	@Override
	public void leftShift(int num) {
		int i = 0;
		while(i < num && firstNode != null) {
			shiftLeft();
			i++;
		}

	}

	@Override
	public void rightShift(int num) {
		int length = size();
		if(num >= length)
			clear();
		Node currentNode = firstNode;
		for(int i = 0; i < length - num - 1; i++) {
			currentNode = currentNode.getNextNode();
		}
		currentNode.setNextNode(null);

	}

	@Override
	public void leftRotate(int num) {
		for(int i = 0; i < num; i++) {
			addItem(removeItem());
		}

	}

	@Override
	public void rightRotate(int num) {
		if(num <= 0 || empty())
			return;
		Node newFirstNode = firstNode, lastNode = firstNode, newLastNode = null;
		int length = size();
		for(int i = 0; i < length - 1; i++) {
			if(i < length - num % length) {
				newLastNode = newFirstNode;
				newFirstNode = newFirstNode.getNextNode();
			}
			lastNode = lastNode.getNextNode();
		}
		lastNode.setNextNode(firstNode);
		firstNode = newFirstNode;
		newLastNode.setNextNode(null);
		

	}

	public String toString() {
		Node currentNode = firstNode;
		String str = "{ ";
		if(currentNode != null) {
			do{
				str += "[" + currentNode.getData().toString() + "] ";
				currentNode = currentNode.getNextNode();
			}while(currentNode != null);
		}
		str += "}";
		return str;
	}

	protected class Node {

		private T data; // entry in bag
		private Node next; // link to next node

		protected Node(T dataPortion) {
			this(dataPortion, null);
		} // end constructor

		protected Node(T dataPortion, Node nextNode) {
			data = dataPortion;
			next = nextNode;
		} // end constructor

		protected T getData() {
			return data;
		} // end getData

		protected void setData(T newData) {
			data = newData;
		} // end setData

		protected Node getNextNode() {
			return next;
		} // end getNextNode

		protected void setNextNode(Node nextNode) {
			next = nextNode;
		} // end setNextNode
	} // end Node
}
