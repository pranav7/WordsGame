package com.example.words;
import java.util.Locale;
import java.util.Random;


public class WordSearch {

	private int SIZE = 10;
	private final static int HORIZONTAL = 0;
	private final static int VERTICAL = 1;
	private final static int DIAGONALLEFTTORIGHT = 2;
	private final static int DIAGONALRIGHTTOLEFT = 3;
	private final static int FORWARD = 0;
	private final static int BACKWARD = 1;
	private int direction;
	private int orientation;
	private String[] wordList = {"connect", "window", "project", "source", "file", "edit", 
			"default", "package", "system", "word", "search", "smart", "insert", "sticky", "article", 
			"chrome", "time", "date", "tast", "tools", "java", "fire", "declare", "problem", "console" };

	private char[][] puzzle;

	public WordSearch() {

		//initializing the puzzle
		puzzle = new char[SIZE][SIZE];
		for(int i = 0; i < puzzle.length; i++){
			for(int j = 0; j < puzzle.length; j++){
				puzzle[i][j] = ' ';
			}
		}
		int maxCharacters = 80;
		int characterCount = 0;
		
		//adding words to the puzzle
		for(int i = 0; i < wordList.length; i++){			
			addWord(wordList[i], puzzle);
			System.out.println(wordList[i] + " successfully added!");
			characterCount += wordList[i].length();
			if(characterCount > maxCharacters)
				break;
		}
		
		//printing the puzzle
		System.out.println("PUZZLE\n");
		for(int i = 0; i < puzzle.length; i++){
			for(int j = 0; j < puzzle.length; j++){
				System.out.print(puzzle[i][j] + " ");
			}
			System.out.print("\n");
		}
		
		//filling empty spaces
		puzzle = fill(puzzle);
		System.out.println("\n");
		for(int i = 0; i < puzzle.length; i++){
			for(int j = 0; j < puzzle.length; j++){
				System.out.print(puzzle[i][j] + " ");
			}
			System.out.print("\n");
		}
	}
	public void addWord(String word, char puzzle[][]){
		word = word.toUpperCase(Locale.US);
		Random random = new Random();
		int row = 0;
		int col = 0;
		char originalPuzzle[][] = new char[puzzle.length][puzzle.length];

		for(int i = 0; i < puzzle.length; i++){
			for(int j = 0; j < puzzle.length; j++){
				originalPuzzle[i][j] = puzzle[i][j];
			}
		}
		
		int flag = 0;
		for(int tries = 1; tries <= 20; tries++){
			//deciding the orientation of the word
			orientation = random.nextInt(2);
			//deciding the direction of the word
			direction = random.nextInt(3);
			
			if(orientation == BACKWARD){
				word = flipWord(word);
			}
			if(direction == HORIZONTAL){
				row = random.nextInt(puzzle.length);
				col = random.nextInt(puzzle.length - word.length());
			}
			else if(direction == VERTICAL){
				row = random.nextInt(puzzle.length - word.length());
				col = random.nextInt(puzzle.length);
			}
			else if(direction == DIAGONALLEFTTORIGHT) {
				row = random.nextInt(puzzle.length - word.length());
				col = random.nextInt(puzzle.length - word.length());
			}
			/*else if(direction == DIAGONALRIGHTTOLEFT) {
				row = random.nextInt(puzzle.length - word.length());
				col = random.nextInt(puzzle.length - (puzzle.length - word.length()));
			}*/
			for(int i = 0; i < word.length(); i++){
				if(puzzle[row][col] == ' ' || puzzle[row][col] == word.charAt(i)){
					puzzle[row][col] = word.charAt(i);
					flag++;
					if(direction == HORIZONTAL)		col++;
					if(direction == VERTICAL)		row++;
					if(direction == DIAGONALLEFTTORIGHT) { col++; row++; }
					//if(direction == DIAGONALLEFTTORIGHT) { col--; row++; }
				}
				else {
					for(int j = i; j >= 0; j--){
						puzzle[row][col] = originalPuzzle[row][col];

						if(direction == HORIZONTAL)		col--;
						if(direction == VERTICAL)		row--;
						if(direction == DIAGONALLEFTTORIGHT) {		col--; row--; }
						//if(direction == DIAGONALRIGHTTOLEFT) {	col++; row--; }
					}
					flag = 0;
					break;
				}
			}
			if(flag == word.length())
				break;
		}
	}

	public String flipWord(String input) {
		StringBuilder output = new StringBuilder();
		for(int i = input.length()-1; i >= 0; i--){
			output.append(input.charAt(i));
		}
		return output.toString();
	}
	
	public char[][] fill(char[][] input){
		char output[][] = new char[input.length][input.length];
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random random = new Random();
		
		for(int i = 0; i < puzzle.length; i++){
			for(int j = 0; j < puzzle.length; j++){
				if(puzzle[i][j] == ' ') {
					output[i][j] = characters.charAt(random.nextInt(characters.length()));
				}
				else {
					output[i][j] = puzzle[i][j];
				}
			}
		}
		return output;
	}
}
