/**
 * Created by: Mike Kucharski & Ashley Packard
 * Fall 2012 | COMP 285 - OOPS in Java
 */

package go.fish;

import java.util.ArrayList;

public class Person 
{
	private ArrayList<Card> hand;
	private int matches;
	//must assign panel number in player to differentiate between then in the main board
	private int pnlNumber;
	
	public Person()
	{
		hand = new ArrayList<Card>();
		matches = 0;
		pnlNumber = 0;
	}
	
	public void setPanelNumber(int n)
	{
		pnlNumber = n;
	}
	
	public int getPanelNumber()
	{
		return pnlNumber;
	}
	
	public int getMatches()
	{
		return matches;
	}
	
	public void incrementMatches()
	{
		matches++;
	}
	
	public int getHandSize()
	{
		return hand.size();
	}
	
	public void draw(Card c)
	{
		hand.add(c);
	}
	
	public Card getCard(int i)
	{
		return hand.get(i);
	}
	
	//removes the two cards passed in from the person's hand
	public void removePair(String firstCard, String secondCard)
	{
		int firstPos = 0, secondPos = 0;
		for(int i = 0; i < hand.size(); i++)
		{
			if(hand.get(i).toString().equals(firstCard))
				firstPos = i;
			if(hand.get(i).toString().equals(secondCard))
				secondPos = i;
		}
		
		if(firstPos > secondPos)
		{
			int tempPos = firstPos;
			firstPos = secondPos;
			secondPos = tempPos;
		}
		
		hand.remove(secondPos);
		hand.remove(firstPos);
		matches++;
	}
	
	//removes card from players hand
	public void removeCard(String cardName)
	{
		int firstPos = 0;
		for(int i = 0; i < hand.size(); i++)
		{
			if(hand.get(i).toString().equals(cardName))
				firstPos = i;
		}
		
		hand.remove(firstPos);
	}
	
	//removes all match pairs from players hand. 
	public ArrayList<String> autoRemovePairs()
	{
		ArrayList<String> pairsRemoved = new ArrayList<String>();
		// The most amount of pairs this player can have is the total cards divided by 2, so cycle that many times
		for(int possiblePairs = (hand.size()/2); possiblePairs > 0; possiblePairs--)
		{
			// Compare starting at the beginning to the next card
			for(int compareCard = 0; compareCard < hand.size()-1; compareCard++)
			{
				for(int i = (compareCard+1); i < hand.size(); i++)
				{
					if(hand.get(compareCard).getRankNumber() == (hand.get(i).getRankNumber()))
					{
						pairsRemoved.add(hand.get(compareCard).getRank((hand.get(compareCard).getRankNumber())));
						hand.remove(i);
						hand.remove(compareCard);
						matches++;
					}
				}
			}
		}
		
		return pairsRemoved;
		
	}

	//checks if player has a matching pair in their hand from the argument
	public boolean contains(String cardSelectedName) 
	{
		for(int i = 0; i < hand.size(); i++)
		{
			if(hand.get(i).toString().substring(0, 1).equals(cardSelectedName.substring(0, 1)))
			{
				hand.remove(i);
				return true;
			}
		}
		
		return false;
	}
}
