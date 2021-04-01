import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class AnagramDecoder {

	public static void main(String[] args) throws FileNotFoundException{
		long time = System.currentTimeMillis();
		Scanner dict = new Scanner(new File(args[0]));
		if(args.length < 2) {
			System.out.println("You didn't enter an anagram. Try again.");
			System.exit(0);
		}
		StringTable hash = new StringTable();
		while(dict.hasNextLine()) {
			hash.insert(dict.nextLine());
		}
		System.out.println("Deciphering " + args[1] + "...");
		hash.getWords(args[1]);
		ArrayList<ArrayList<String>> anagrams = hash.getAllPhrases(args[1]);
		for(int a = 0; a < anagrams.size(); a++) {
			for(int b = 0; b < anagrams.get(a).size(); b++) {
				System.out.print(anagrams.get(a).get(b) + " ");
			}
			System.out.println();
		}
		System.out.println(System.currentTimeMillis() - time);
		dict.close();
	}
	
	

}
