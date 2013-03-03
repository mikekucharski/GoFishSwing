/**
 * Created by: Mike Kucharski & Ashley Packard
 * Fall 2012 | COMP 285 - OOPS in Java
 */

package go.fish;

import java.util.ArrayList;
import java.util.Random;

public class Deck
{
	private ArrayList<Card> deck;
	
	//constructor, creates a new ArrayList of 52 unique cards, shuffles
	public Deck()
	{
		deck = new ArrayList<Card>();
		for (int suitNumber = 0; suitNumber < 4; suitNumber++)
		{
			for (int rankNumber = 0; rankNumber < 13; rankNumber++)
				deck.add(new Card(rankNumber, suitNumber));
		}
		
		shuffle();
	}
	
	//gets the size of deck
	public int getNumberOfCards()
	{
		return deck.size();
	}
	
	//used to test when the deck is empty
	public boolean isDeckEmpty() 
	{
		if(getNumberOfCards() == 0)
			return true;
		return false;
	}
	
	//removes the last card in the deck and returns that card
	public Card drawFromDeck()
	{
		Card temp = deck.get(deck.size()-1);
		deck.remove(deck.size()-1);
		return temp;
	}
	
	//randomize the indexes of each card
	public void shuffle()
	{
		int numCards = (getNumberOfCards() - 1);
		Random randomGenerator = new Random();
	    while (numCards > 1) 
	    {
	      	int randomInt = randomGenerator.nextInt(getNumberOfCards());
	        Card temp = deck.get(numCards);
	        deck.set(numCards, deck.get(randomInt));
	        deck.set(randomInt, temp);
	        numCards--;
	    }
		
	}

}
