import java.util.Arrays;

public class Test {
		
	public static void main(String[] args) {
		Integer[] numbers = new Integer[100];
		for(int i = 0; i < 100; i++) {
			numbers[i] = (int) (Math.random() * 100);
		}
		System.out.println(Arrays.toString(numbers));
		TextMergeQuick.iterativeQuickSort(numbers, 100);
		System.out.println(Arrays.toString(numbers));
	}
}
