import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.io.File;

public class Assig4 {


	public static void main(String[] args) throws FileNotFoundException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter arrray size: ");
		int arrSize = sc.nextInt();
		System.out.println("Enter number of trials: ");
		int trials = sc.nextInt();
		System.out.println("Enter file name: ");
		String filename = sc.next();

		testSorts(arrSize, trials, filename);


	}


	private static void testSorts(int size, int trials, String filename) throws FileNotFoundException {
		File file = new File(filename);
		PrintWriter out = new PrintWriter(file);

		Integer[] reverse = new Integer[size];
		Integer[] sorted = new Integer[size];
		Integer[] random = new Integer[size];
		long totalComparisons = 0, totalDataMoves = 0;
		long start, end, time = 0;

		//Simple Quick Sort
		if(size <= 100000) {
			out.println("Algorithm: Simple Quick Sort");
			out.println("Array Size: " + size);
			out.println("Order: Reverse");
			out.println("Number of trials: " + trials);

			if(size <= 20) {
				for(int i = 0; i < size; i++) {
					reverse[i] = size - i;
				}
				out.println("Unsorted Array: " + Arrays.toString(reverse));
				Quick.quickSort(reverse, size);
				out.println("Sorted Array: " + Arrays.toString(reverse));
				Quick.resetData();
			}

			for(int a = 0; a < trials; a++) {
				for(int i = 0; i < size; i++) {
					reverse[i] = size - i;
				}
				start = System.nanoTime();
				Quick.quickSort(reverse, size);
				end = System.nanoTime();
				time += end - start;
			}
			totalComparisons = Quick.getComparisons();
			totalDataMoves = Quick.getDataMoves();
			Quick.resetData();
			out.println("Average Time: " + ( time / (double)trials / 1000000000));
			out.println("Average number of comparisons: " + (( totalComparisons / (double)trials)));
			out.println("Average number of data moves: " + ( totalDataMoves / (double)trials));
			time = 0;
			out.println();
			out.println();


			out.println("Algorithm: Simple Quick Sort");
			out.println("Array Size: " + size);
			out.println("Order: Random");
			out.println("Number of trials: " + trials);

			for(int i = 0; i < size; i++) {
				random[i] = i+1;
			}

			if(size <= 20) {
				shuffleArray(random);
				out.println("Unsorted Array: " + Arrays.toString(random));
				Quick.quickSort(random, size);
				out.println("Sorted Array: " + Arrays.toString(random));
				Quick.resetData();
			}

			for(int a = 0; a < trials; a++) {
				shuffleArray(random);
				start = System.nanoTime();
				Quick.quickSort(random, size);
				end = System.nanoTime();
				time += end - start;
			}
			totalComparisons = Quick.getComparisons();
			totalDataMoves = Quick.getDataMoves();
			Quick.resetData();
			out.println("Average Time: " + ( time / (double)trials / 1000000000));
			out.println("Average number of comparisons: " + (( totalComparisons / (double)trials)));
			out.println("Average number of data moves: " + ( totalDataMoves / (double)trials));
			time = 0;
			out.println();
			out.println();


			out.println("Algorithm: Simple Quick Sort");
			out.println("Array Size: " + size);
			out.println("Order: Sorted");
			out.println("Number of trials: " + trials);

			for(int i = 0; i < size; i++) {
				sorted[i] = i+1;
			}

			if(size <= 20) {
				out.println("Unsorted Array: " + Arrays.toString(random));
				Quick.quickSort(random, size);
				out.println("Sorted Array: " + Arrays.toString(random));
				Quick.resetData();
			}

			for(int a = 0; a < trials; a++) {
				start = System.nanoTime();
				Quick.quickSort(random, size);
				end = System.nanoTime();
				time += end - start;
			}
			totalComparisons = Quick.getComparisons();
			totalDataMoves = Quick.getDataMoves();
			Quick.resetData();
			out.println("Average Time: " + ( time / (double)trials / 1000000000));
			out.println("Average number of comparisons: " + (( totalComparisons / (double)trials)));
			out.println("Average number of data moves: " + ( totalDataMoves / (double)trials));
			time = 0;
			out.println();
			out.println();
		}



		//Quick Sort Base Case 5
		out.println("Algorithm: Quick Sort Base Case 5");
		out.println("Array Size: " + size);
		out.println("Order: Reverse");
		out.println("Number of trials: " + trials);

		if(size <= 20) {
			for(int i = 0; i < size; i++) {
				reverse[i] = size - i;
			}
			out.println("Unsorted Array: " + Arrays.toString(reverse));
			TextMergeQuick.quickSort(reverse, size, 5);
			out.println("Sorted Array: " + Arrays.toString(reverse));
			TextMergeQuick.resetData();
		}

		for(int a = 0; a < trials; a++) {
			for(int i = 0; i < size; i++) {
				reverse[i] = size - i;
			}
			start = System.nanoTime();
			TextMergeQuick.quickSort(reverse, size, 5);
			end = System.nanoTime();
			time += end - start;
		}
		totalComparisons = TextMergeQuick.getComparisons();
		totalDataMoves = TextMergeQuick.getDataMoves();
		TextMergeQuick.resetData();
		out.println("Average Time: " + ( time / (double)trials / 1000000000));
		out.println("Average number of comparisons: " + (( totalComparisons / (double)trials)));
		out.println("Average number of data moves: " + ( totalDataMoves / (double)trials));
		time = 0;
		out.println();
		out.println();

		out.println("Algorithm: Quick Sort Base Case 5");
		out.println("Array Size: " + size);
		out.println("Order: Random");
		out.println("Number of trials: " + trials);

		for(int i = 0; i < size; i++) {
			random[i] = i+1;
		}

		if(size <= 20) {
			shuffleArray(random);
			out.println("Unsorted Array: " + Arrays.toString(random));
			TextMergeQuick.quickSort(random, size, 5);
			out.println("Sorted Array: " + Arrays.toString(random));
			TextMergeQuick.resetData();
		}

		for(int a = 0; a < trials; a++) {
			shuffleArray(random);
			start = System.nanoTime();
			TextMergeQuick.quickSort(random, size, 5);
			end = System.nanoTime();
			time += end - start;
		}
		totalComparisons = TextMergeQuick.getComparisons();
		totalDataMoves = TextMergeQuick.getDataMoves();
		TextMergeQuick.resetData();
		out.println("Average Time: " + ( time / (double)trials / 1000000000));
		out.println("Average number of comparisons: " + (( totalComparisons / (double)trials)));
		out.println("Average number of data moves: " + ( totalDataMoves / (double)trials));
		time = 0;
		out.println();
		out.println();

		out.println("Algorithm: Quick Sort Base Case 5");
		out.println("Array Size: " + size);
		out.println("Order: Sorted");
		out.println("Number of trials: " + trials);

		for(int i = 0; i < size; i++) {
			sorted[i] = i+1;
		}

		if(size <= 20) {
			out.println("Unsorted Array: " + Arrays.toString(random));
			TextMergeQuick.quickSort(sorted, size, 5);
			out.println("Sorted Array: " + Arrays.toString(random));
			TextMergeQuick.resetData();
		}

		for(int a = 0; a < trials; a++) {
			start = System.nanoTime();
			TextMergeQuick.quickSort(sorted, size, 5);
			end = System.nanoTime();
			time += end - start;
		}
		totalComparisons = TextMergeQuick.getComparisons();
		totalDataMoves = TextMergeQuick.getDataMoves();
		TextMergeQuick.resetData();
		out.println("Average Time: " + ( time / (double)trials / 1000000000));
		out.println("Average number of comparisons: " + (( totalComparisons / (double)trials)));
		out.println("Average number of data moves: " + ( totalDataMoves / (double)trials));
		time = 0;
		out.println();
		out.println();





		//Quick Sort Base Case 20
		if(size >= 20) {
		out.println("Algorithm: Quick Sort Base Case 20");
		out.println("Array Size: " + size);
		out.println("Order: Reverse");
		out.println("Number of trials: " + trials);

		if(size <= 20) {
			for(int i = 0; i < size; i++) {
				reverse[i] = size - i;
			}
			out.println("Unsorted Array: " + Arrays.toString(reverse));
			TextMergeQuick.quickSort(reverse, size, 20);
			out.println("Sorted Array: " + Arrays.toString(reverse));
			TextMergeQuick.resetData();
		}

		for(int a = 0; a < trials; a++) {
			for(int i = 0; i < size; i++) {
				reverse[i] = size - i;
			}
			start = System.nanoTime();
			TextMergeQuick.quickSort(reverse, size, 20);
			end = System.nanoTime();
			time += end - start;
		}
		totalComparisons = TextMergeQuick.getComparisons();
		totalDataMoves = TextMergeQuick.getDataMoves();
		TextMergeQuick.resetData();
		out.println("Average Time: " + ( time / (double)trials / 1000000000));
		out.println("Average number of comparisons: " + (( totalComparisons / (double)trials)));
		out.println("Average number of data moves: " + ( totalDataMoves / (double)trials));
		time = 0;
		out.println();
		out.println();

		out.println("Algorithm: Quick Sort Base Case 20");
		out.println("Array Size: " + size);
		out.println("Order: Random");
		out.println("Number of trials: " + trials);

		for(int i = 0; i < size; i++) {
			random[i] = i+1;
		}

		if(size <= 20) {
			shuffleArray(random);
			out.println("Unsorted Array: " + Arrays.toString(random));
			TextMergeQuick.quickSort(random, size, 20);
			out.println("Sorted Array: " + Arrays.toString(random));
			TextMergeQuick.resetData();
		}

		for(int a = 0; a < trials; a++) {
			shuffleArray(random);
			start = System.nanoTime();
			TextMergeQuick.quickSort(random, size, 20);
			end = System.nanoTime();
			time += end - start;
		}
		totalComparisons = TextMergeQuick.getComparisons();
		totalDataMoves = TextMergeQuick.getDataMoves();
		TextMergeQuick.resetData();
		out.println("Average Time: " + ( time / (double)trials / 1000000000));
		out.println("Average number of comparisons: " + (( totalComparisons / (double)trials)));
		out.println("Average number of data moves: " + ( totalDataMoves / (double)trials));
		time = 0;
		out.println();
		out.println();

		out.println("Algorithm: TextMergeQuick Sort Base Case 20");
		out.println("Array Size: " + size);
		out.println("Order: Sorted");
		out.println("Number of trials: " + trials);

		for(int i = 0; i < size; i++) {
			sorted[i] = i+1;
		}

		if(size <= 20) {
			out.println("Unsorted Array: " + Arrays.toString(random));
			TextMergeQuick.quickSort(sorted, size, 20);
			out.println("Sorted Array: " + Arrays.toString(random));
			TextMergeQuick.resetData();
		}

		for(int a = 0; a < trials; a++) {
			start = System.nanoTime();
			TextMergeQuick.quickSort(sorted, size, 20);
			end = System.nanoTime();
			time += end - start;
		}
		totalComparisons = TextMergeQuick.getComparisons();
		totalDataMoves = TextMergeQuick.getDataMoves();
		TextMergeQuick.resetData();
		out.println("Average Time: " + ( time / (double)trials / 1000000000));
		out.println("Average number of comparisons: " + (( totalComparisons / (double)trials)));
		out.println("Average number of data moves: " + ( totalDataMoves / (double)trials));
		time = 0;
		out.println();
		out.println();
	}



		//Quick Sort Base Case 100
		if(size >= 100) {
		out.println("Algorithm: Quick Sort Base Case 100");
		out.println("Array Size: " + size);
		out.println("Order: Reverse");
		out.println("Number of trials: " + trials);

		if(size <= 20) {
			for(int i = 0; i < size; i++) {
				reverse[i] = size - i;
			}
			out.println("Unsorted Array: " + Arrays.toString(reverse));
			TextMergeQuick.quickSort(reverse, size, 100);
			out.println("Sorted Array: " + Arrays.toString(reverse));
			TextMergeQuick.resetData();
		}

		for(int a = 0; a < trials; a++) {
			for(int i = 0; i < size; i++) {
				reverse[i] = size - i;
			}
			start = System.nanoTime();
			TextMergeQuick.quickSort(reverse, size, 100);
			end = System.nanoTime();
			time += end - start;
		}
		totalComparisons = TextMergeQuick.getComparisons();
		totalDataMoves = TextMergeQuick.getDataMoves();
		TextMergeQuick.resetData();
		out.println("Average Time: " + ( time / (double)trials / 1000000000));
		out.println("Average number of comparisons: " + (( totalComparisons / (double)trials)));
		out.println("Average number of data moves: " + ( totalDataMoves / (double)trials));
		time = 0;
		out.println();
		out.println();

		out.println("Algorithm: Quick Sort Base Case 100");
		out.println("Array Size: " + size);
		out.println("Order: Random");
		out.println("Number of trials: " + trials);

		for(int i = 0; i < size; i++) {
			random[i] = i+1;
		}

		if(size <= 20) {
			shuffleArray(random);
			out.println("Unsorted Array: " + Arrays.toString(random));
			TextMergeQuick.quickSort(random, size, 100);
			out.println("Sorted Array: " + Arrays.toString(random));
			TextMergeQuick.resetData();
		}

		for(int a = 0; a < trials; a++) {
			shuffleArray(random);
			start = System.nanoTime();
			TextMergeQuick.quickSort(random, size, 100);
			end = System.nanoTime();
			time += end - start;
		}
		totalComparisons = TextMergeQuick.getComparisons();
		totalDataMoves = TextMergeQuick.getDataMoves();
		TextMergeQuick.resetData();
		out.println("Average Time: " + ( time / (double)trials / 1000000000));
		out.println("Average number of comparisons: " + (( totalComparisons / (double)trials)));
		out.println("Average number of data moves: " + ( totalDataMoves / (double)trials));
		time = 0;
		out.println();
		out.println();

		out.println("Algorithm: Quick Sort Base Case 100");
		out.println("Array Size: " + size);
		out.println("Order: Sorted");
		out.println("Number of trials: " + trials);

		for(int i = 0; i < size; i++) {
			sorted[i] = i+1;
		}

		if(size <= 20) {
			out.println("Unsorted Array: " + Arrays.toString(random));
			TextMergeQuick.quickSort(sorted, size, 100);
			out.println("Sorted Array: " + Arrays.toString(random));
			TextMergeQuick.resetData();
		}

		for(int a = 0; a < trials; a++) {
			start = System.nanoTime();
			TextMergeQuick.quickSort(sorted, size, 100);
			end = System.nanoTime();
			time += end - start;
		}
		totalComparisons = TextMergeQuick.getComparisons();
		totalDataMoves = TextMergeQuick.getDataMoves();
		TextMergeQuick.resetData();
		out.println("Average Time: " + ( time / (double)trials / 1000000000));
		out.println("Average number of comparisons: " + (( totalComparisons / (double)trials)));
		out.println("Average number of data moves: " + ( totalDataMoves / (double)trials));
		time = 0;
		out.println();
		out.println();
	}


		//Random Quick Sort
		out.println("Algorithm: Random Quick Sort");
		out.println("Array Size: " + size);
		out.println("Order: Reverse");
		out.println("Number of trials: " + trials);

		if(size <= 20) {
			for(int i = 0; i < size; i++) {
				reverse[i] = size - i;
			}
			out.println("Unsorted Array: " + Arrays.toString(reverse));
			TextMergeQuick.randomQuickSort(reverse, size);
			out.println("Sorted Array: " + Arrays.toString(reverse));
			TextMergeQuick.resetData();
		}

		for(int a = 0; a < trials; a++) {
			for(int i = 0; i < size; i++) {
				reverse[i] = size - i;
			}
			start = System.nanoTime();
			TextMergeQuick.randomQuickSort(reverse, size);
			end = System.nanoTime();
			time += end - start;
		}
		totalComparisons = TextMergeQuick.getComparisons();
		totalDataMoves = TextMergeQuick.getDataMoves();
		TextMergeQuick.resetData();
		out.println("Average Time: " + ( time / (double)trials / 1000000000));
		out.println("Average number of comparisons: " + (( totalComparisons / (double)trials)));
		out.println("Average number of data moves: " + ( totalDataMoves / (double)trials));
		time = 0;
		out.println();
		out.println();

		out.println("Algorithm: Random Quick Sort");
		out.println("Array Size: " + size);
		out.println("Order: Random");
		out.println("Number of trials: " + trials);

		for(int i = 0; i < size; i++) {
			random[i] = i+1;
		}

		if(size <= 20) {
			shuffleArray(random);
			out.println("Unsorted Array: " + Arrays.toString(random));
			TextMergeQuick.randomQuickSort(random, size);
			out.println("Sorted Array: " + Arrays.toString(random));
			TextMergeQuick.resetData();
		}

		for(int a = 0; a < trials; a++) {
			shuffleArray(random);
			start = System.nanoTime();
			TextMergeQuick.randomQuickSort(random, size);
			end = System.nanoTime();
			time += end - start;
		}
		totalComparisons = TextMergeQuick.getComparisons();
		totalDataMoves = TextMergeQuick.getDataMoves();
		TextMergeQuick.resetData();
		out.println("Average Time: " + ( time / (double)trials / 1000000000));
		out.println("Average number of comparisons: " + (( totalComparisons / (double)trials)));
		out.println("Average number of data moves: " + ( totalDataMoves / (double)trials));
		time = 0;
		out.println();
		out.println();

		out.println("Algorithm: Random Quick Sort");
		out.println("Array Size: " + size);
		out.println("Order: Sorted");
		out.println("Number of trials: " + trials);

		for(int i = 0; i < size; i++) {
			sorted[i] = i+1;
		}

		if(size <= 20) {
			out.println("Unsorted Array: " + Arrays.toString(random));
			TextMergeQuick.randomQuickSort(sorted, size);
			out.println("Sorted Array: " + Arrays.toString(random));
			TextMergeQuick.resetData();
		}

		for(int a = 0; a < trials; a++) {
			start = System.nanoTime();
			TextMergeQuick.randomQuickSort(sorted, size);
			end = System.nanoTime();
			time += end - start;
		}
		totalComparisons = TextMergeQuick.getComparisons();
		totalDataMoves = TextMergeQuick.getDataMoves();
		TextMergeQuick.resetData();
		out.println("Average Time: " + ( time / (double)trials / 1000000000));
		out.println("Average number of comparisons: " + (( totalComparisons / (double)trials)));
		out.println("Average number of data moves: " + ( totalDataMoves / (double)trials));
		time = 0;
		out.println();
		out.println();








		out.close();
	}


	public static void shuffleArray(Integer[] arr) {
		Random rnd = new Random();
		for (int i = arr.length - 1; i > 0; i--)
		{
			int index = rnd.nextInt(arr.length-1);
			// Simple swap
			int a = arr[index];
			arr[index] = arr[i];
			arr[i] = a;
		}
	}

}
