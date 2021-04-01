import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;

public class StringTable {
	LinkedList<String>[] hashTable;
	ArrayList<String> words;
	ArrayList<ArrayList<String>> phrases;
	
	private final static int HASH_LENGTH = 100;
	
	public StringTable() {
		hashTable = new LinkedList[HASH_LENGTH];
		for(int i = 0; i < HASH_LENGTH; i++) {
			hashTable[i] = new LinkedList<String>();
		}
		words = new ArrayList<String>();
		phrases = new ArrayList<ArrayList<String>>();
		
	}
	
	public void insert(String s) {
		hashTable[getHashValue(s)].add(s);
	}
	
	public void printLength() {
		for(int i = 0; i < HASH_LENGTH; i++) {
			System.out.println(hashTable[i].size());
		}
	}
	
	/**
	 * Returns an array list of all the anagrams of String s 
	 */
	public ArrayList<String> anagramSolver(String s){
		int hashValue = getHashValue(s);
		ArrayList<String> anagrams = new ArrayList<String>();
		ListIterator<String> iterator = hashTable[hashValue].listIterator();
		while (iterator.hasNext()) {
			String temp = iterator.next();
		    if(isAnagram(s, temp)) 
		    	anagrams.add(temp);
		}
		return anagrams;
	}
	
	private boolean isAnagram(String s1, String s2) {
		if(s1.length() != s2.length())
			return false;
		int[] letters = new int[26];
		for(int i = 0; i < s1.length(); i++) {
			letters[s1.charAt(i) - 97]++;
			letters[s2.charAt(i) - 97]--;
		}
		for(int i:letters) {
			if(i!=0)
				return false;
		}
		return true;
		
	}
	
	/**
	 * Checks if s1 can be made using the letters in s2
	 * s1 can be shorter than s2 but can't be longer 
	 */
	public boolean isAnagramOf(String s1, String s2) {
		int[] letters = new int[26];
		for(int i = 0; i < s1.length(); i++) {
			letters[s1.charAt(i) - 97]--;
		}
		for(int i = 0; i < s2.length(); i++) {
			letters[s2.charAt(i) - 97]++;	
		}
		for(int i:letters) {
			if(i<0)
				return false;
		}
		return true;
	}
	
	private int getHashValue(String s) {
		int hashVal = 0;
		for(int i = 0 ; i < s.length(); i++) {
			hashVal += s.charAt(i) - 97;
		}
		hashVal %= HASH_LENGTH;
		return hashVal;
	}
	
	
	public ArrayList<String> getWords(String s){
		getAllWords(s, 0);
		Set<String> wordsWithoutDuplicates =  new LinkedHashSet<String>(words);
		words.clear();
		words.addAll(wordsWithoutDuplicates);
		return words;
	}
	
	public void getAllWords(String s, int numRemoved){
		ArrayList<String> anagrams = anagramSolver(s);
		words.addAll(anagrams);
		if(s.length() == 1)
			return;
		for(int i = numRemoved; i < s.length(); i++) {
			getAllWords(s.substring(0,i) + s.substring(i+1), i);
		}
		return;		
	}
	
	public ArrayList<ArrayList<String>> getAllPhrases(String s){
		getAllPhrases(new ArrayList<String>(), s);
		return phrases;
	}
	
	public void getAllPhrases(ArrayList<String> anagrams, String s) {
		if(s.length() == 0) {
			phrases.add(anagrams);
		}
//		System.out.println(s);
		for(String a: words) {
			if(isAnagramOf(a, s)) {
				String str = s;
				for(int i = 0; i < a.length(); i++) {
					str = str.replaceFirst(a.substring(i, i+1), "");
				}
				ArrayList<String> nextCall = new ArrayList<String>();
				nextCall.addAll(anagrams);
				nextCall.add(a);
				getAllPhrases(nextCall, str);
			}
		}
	}
	
	
}
