import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Assig3 {

	public static char[][] board;
	public static String[] words;
	public static HashMap<String,String> coordinates = new HashMap<String,String>();

	public static void main(String[] args) {
		createboard();
		promptUser();
	}

	public static void createboard() {
		Scanner inScan = new Scanner(System.in);
		Scanner fReader;
		File fName;
		String fString = "", word = "";

		// Make sure the file name is valid
		while (true)
		{
			try
			{
				System.out.println("Please enter grid filename:");
				fString = inScan.nextLine();
				fName = new File(fString);
				fReader = new Scanner(fName);

				break;
			}
			catch (IOException e)
			{
				System.out.println("Problem " + e);
			}
		}

		// Parse input file to create 2-d grid of characters
		String [] dims = (fReader.nextLine()).split(" ");
		int rows = Integer.parseInt(dims[0]);
		int cols = Integer.parseInt(dims[1]);

		board = new char[rows][cols];

		for (int i = 0; i < rows; i++)
		{
			String rowString = fReader.nextLine();
			for (int j = 0; j < rowString.length(); j++)
			{
				board[i][j] = Character.toLowerCase(rowString.charAt(j));
			}
		}

		// Show user the grid
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < cols; j++)
			{
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}


	}

	public static void promptUser() {
		while(true) {
			Scanner in = new Scanner(System.in);
			String userInput = "";
			int numberOfWords;
			String[] startCoords;

			System.out.println("Please enter phrase (sep. by single spaces):");
			userInput = in.nextLine();

			numberOfWords = countNumWords(userInput);
			words = userInput.split(" ");
			startCoords = new String[words.length];
			String str = (words.length > 1) ? "words" : "word";

			System.out.println("\nLooking for the phrase \"" + userInput + 
					"\" which contains " + words.length + " " + str + "\n");
			boolean found = false;;

			for(int r = 0; r < board.length && !found; r++) {
				for(int c = 0; c < board[0].length && !found; c++) {
					found = findWord(r, c, 0, userInput, 0, 0, startCoords);
				}
			}

			str = (found) ? "found:" : "not found";

			System.out.println("The phrase \"" + userInput + "\" was " + str + "\n");

			if(found) {
				printboard();
				printLocations(startCoords);
			}

			reset();
			coordinates.clear();
		}
	}


	public static void printboard() {
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
	}

	public static void printLocations(String[] startCoords) {
		System.out.println();

		for(int i= 0; i < words.length; i++){
			System.out.println(words[i] + ": " + startCoords[i] + coordinates.get(startCoords[i]));
		}
	}

	public static void reset() {
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				board[i][j] = Character.toLowerCase(board[i][j]);
			}
		}
	}
	
	public static int countNumWords(String s) {
		String trim = s.trim();
		if(trim.isEmpty()) {
			System.out.println("\nExiting Assig3 >");
			System.exit(0);
		}
		return trim.split("\\s+").length;
	}

	public static boolean findWord(int r, int c, int loc, String phrase, int dir, int wordCount, String[] startCoords) {
		
		
		System.out.print("findWord: " + r + ":" + c + " " + phrase + ": " + loc); // trace code
		if(loc >= 0 && r>= 0 && c>=0)
			System.out.println(phrase.charAt(loc) + " " + board[r][c]);

		if (r >= board.length || r < 0 || c >= board[0].length || c < 0)
			return false;
		else if (board[r][c] != phrase.charAt(loc))  // char does not match
			return false;
		else  	// current character matches
		{
			board[r][c] = Character.toUpperCase(phrase.charAt(loc));

			boolean answer = false;

			if(dir == 0||phrase.substring(loc-1, loc).equals(" ")) {
				startCoords[wordCount] = "(" + r + "," + c + ") to ";
			}

			if(loc == phrase.length() - 1) {
				coordinates.put(startCoords[wordCount], "(" + r + "," + c + ")");
				answer = true;
			}else {
				if(phrase.substring(loc+1, loc+2).equals(" ")) {
					coordinates.put(startCoords[wordCount], "(" + r + "," + c + ")");
					wordCount++; loc++; dir = 0;
				}
				if(dir == 1 || dir == 0)
					answer = findWord(r, c+1, loc+1, phrase, 1, wordCount, startCoords);
				if(dir == 2 || (dir == 0 && !answer))
					//answer = findWord(r+1, c+1, board, loc+1, phrase, 2, wordCount);
				if(dir == 3 || (dir == 0 && !answer))
					answer = findWord(r+1, c, loc+1, phrase, 3, wordCount, startCoords);
				if(dir == 4 || (dir == 0 && !answer))
					//answer = findWord(r+1, c-1, board, loc+1, phrase, 4, wordCount);
				if(dir == 5 || (dir == 0 && !answer))
					answer = findWord(r, c-1, loc+1, phrase, 5, wordCount, startCoords);
				if(dir == 6 || (dir == 0 && !answer))
					//answer = findWord(r-1, c-1, board, loc+1, phrase, 6, wordCount);
				if(dir == 7 || (dir == 0 && !answer))
					answer = findWord(r-1, c, loc+1, phrase, 7, wordCount, startCoords);
				if(dir == 8 || (dir == 0 && !answer))
					//answer = findWord(r-1, c+1, board, loc+1, phrase, 8, wordCount);
				if(!answer) {
					board[r][c] = Character.toLowerCase(board[r][c]);
				}
			}
			return answer;


		}

	}



}
