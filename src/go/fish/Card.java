/**
 * Created by: Mike Kucharski & Ashley Packard
 * Fall 2012 | COMP 285 - OOPS in Java
 */

package go.fish;

public class Card implements GameConstants
{
	private int rank;
	private int suit;
	private boolean faceDown;
	
	private String[] possibleRanks = allRanks;
	private String[] possibleSuits = allSuits;
	
	public Card(int r, int s)
	{
		rank = r;
		suit = s;
		faceDown = true;
	}
	
	public String toString()
	{
		return getRank(getRankNumber()) + " of " + getSuit(getSuitType());
	}
	
	public String getRank(int r)
	{
		return possibleRanks[r];
	}

	public String getSuit(int s)
	{
		return possibleSuits[s];
	}
	
	public int getRankNumber()
	{
		return rank;
	}
	
	public int getSuitType()
	{
		return suit;
	}
	
	public boolean isFaceDown()
	{
		return faceDown;
	}
	
	public void flipCard()
	{
		faceDown = (faceDown) ? false : true;
	}

}
