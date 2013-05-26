package com.example.words;

import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class WordsActivity extends Activity {

	private int SIZE = 8;
	private final static int HORIZONTAL = 0;
	private final static int VERTICAL = 1;
	private final static int DIAGONALLEFTTORIGHT = 2;
	private final static int DIAGONALRIGHTTOLEFT = 3;
	private final static int FORWARD = 0;
	private final static int BACKWARD = 1;
	private int direction;
	private int orientation;

	private char[][] puzzle;
	String[] wordList = new String[58];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_words);

		//Populating List of Words from dictionary
		Scanner scan = new Scanner(getResources().openRawResource(R.raw.words));
		try {
			int i = 0;
			while(scan.hasNext()){
				wordList[i++] = scan.next();
			}			
		} catch(Exception e){
			e.printStackTrace();
		}
		finally {
			scan.close();
		}

		//initializing the puzzle
		puzzle = new char[SIZE][SIZE];
		for(int i = 0; i < puzzle.length; i++){
			for(int j = 0; j < puzzle.length; j++){
				puzzle[i][j] = ' ';
			}
		}
		int maxCharacters = 56;
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

		//printing the complete puzzle
		for(int i = 0; i < puzzle.length; i++){
			for(int j = 0; j < puzzle.length; j++){
				System.out.print(puzzle[i][j] + " ");
			}
			System.out.print("\n");
		}
		//creating the grid
		createGrid(puzzle);
	}

	//creating the user layout
	public void createGrid(char[][] input){
		TableLayout table = (TableLayout) findViewById(R.id.mainLayout);

		for(int i = 0; i < input.length; i++){
			//LinearLayout rowLayout = new LinearLayout(this);
			//rowLayout.setOrientation(LinearLayout.HORIZONTAL);
			//rowLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			TableRow row = new TableRow(this);
			row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
			row.setGravity(Gravity.CENTER);

			for(int j = 0; j < input.length; j++){
				TextView text = new TextView(this);
				Character temp = input[i][j];
				text.setText(temp.toString());
				text.setPadding(10, 5, 10, 5);
				text.setTextSize(35);
				text.setGravity(Gravity.CENTER);
				row.addView(text);
			}
			table.addView(row);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.words, menu);
		return true;
	}

}
