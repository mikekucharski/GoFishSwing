/**
 * Created by: Mike Kucharski & Ashley Packard
 * Fall 2012 | COMP 285 - OOPS in Java
 */

package go.fish;

public interface GameConstants 
{
	final String[] allSuits = {"Clubs", "Hearts", "Spades", "Diamonds"};
	final String[] allRanks = {"Ace", "2", "3", "4", "5", "6", 
			"7", "8", "9", "10", "Jack", "Queen", "King"};
	
	final int PLAYER0TURN = 0;
	final int PLAYER1TURN = 1;
	final int PLAYER2TURN = 2;
	final int PLAYER3TURN = 3;
	
	final int STARTING_CARDS = 7;
	final int REFILL_CARDS = 3;
}
