import java.util.Random;

public class MultiDS<T> implements PrimQ<T>, Reorder {

	// array that stores all the T objects
	private T[] arr;
	
	// number of items in the stored in MultiDS
	private int numItems;

	@SuppressWarnings("unchecked")
	public MultiDS(int size) {
		arr = (T[])new Object[size];
		numItems = 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void reverse() {
		T[] newArr = (T[])new Object[arr.length];
		for(int i = 0; i < numItems; i++) {
			newArr[i] = arr[numItems-i-1];
		}
		arr = newArr;
	}

	@Override
	public void shiftRight() {
		T shiftItem = arr[numItems-1];
		for(int i = numItems-1; i > 0; i--) {
			arr[i] = arr[i-1];
		}
		arr[0] = shiftItem;

	}

	@Override
	public void shiftLeft() {
		T shiftItem = arr[0];
		for(int i = 0; i < numItems-1; i++) {
			arr[i] = arr[i+1];
		}
		arr[numItems-1] = shiftItem;
	}

	@Override
	public void shuffle() {
		Random rand = new Random();
		for(int i = 0; i < numItems; i++) {
			int randInt = rand.nextInt(numItems);
			T item = arr[0];
			arr[0] = arr[randInt];
			arr[randInt] = item;
		}

	}

	public boolean addItem(T item) {
		if(numItems < arr.length) {
			arr[numItems] = item;
			numItems++;
			return true;
		}
		return false;
	}

	public T removeItem() {
		if(numItems == 0)
			return null;
		T item = (T) arr[0];
		for(int i = 0; i < numItems - 1; i++) {
			arr[i] = arr[i+1];
		}
		numItems--;
		if(numItems != 0)
			arr[numItems] = null;
		return item;
	}

	public T top() {
		if(numItems == 0)
			return null;
		return arr[0];
	}
	
	public T bottom() {
		if(numItems == 0)
			return null;
		return arr[numItems-1];
	}

	public boolean full() {
		return numItems == arr.length;
	}

	public boolean empty() {
		return numItems == 0;
	}

	public int size() {
		return numItems;
	}

	public void clear() {
		while(!empty()) {
			removeItem();
		}
	}
	
	public String toString() {
		String str = "";
		for(int i = 0; i < numItems; i++) {
			str += arr[i].toString() + " ";
		}
		return str;
	}

}
