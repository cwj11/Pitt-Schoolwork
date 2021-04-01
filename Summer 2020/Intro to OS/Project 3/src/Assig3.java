import java.util.Arrays;

public class Assig3 {

	final int FUCKER = 1;
	
	public static void main(String[] args) {
		double x=25;
		int y=10;
		double z = 13/4 + x; 
		
		System.out.println("Sum" + z);
		int[] arr = new int[10];
		for(int i = 0; i < 10; i++) {
			insertInOrder(arr,i,10-i);
			System.out.println(Arrays.toString(arr));
		}

	}

	
	public static void insertInOrder( int[] arr, int count, int newVal ) {
		arr[count] = newVal;
		while(count>0 && arr[count] > arr[count-1]) {
			int temp = arr[count];
			arr[count] = arr[count-1];
			arr[count-1] = temp;
			count--;
		}
		
	}
}
