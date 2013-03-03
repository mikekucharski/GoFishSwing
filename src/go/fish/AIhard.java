/**
 * Created by: Mike Kucharski & Ashley Packard
 * Fall 2012 | COMP 285 - OOPS in Java
 */

package go.fish;

import java.util.ArrayList;
import java.util.HashMap;

public class AIhard extends Person
{
	private int playerToAsk, cardIndex;
	private int cardsInHand, totalPlayers;
	private int[][] score;
	
	public AIhard()
	{
		score = new int[0][0];
		playerToAsk = 0;
		cardIndex = 0;
		cardsInHand = 0;
		totalPlayers = 0;
	}
	
	public void setScores(Person[] players, int turn, HashMap<String, Integer> pairs, HashMap<String, ArrayList<Integer>> knownCards)
	{
		cardsInHand = players[turn].getHandSize();
		totalPlayers = players.length;
		score = new int[cardsInHand][totalPlayers];
		
		//assign a score to every card in robots hand for every other player
		for(int i = 0; i < cardsInHand; i++)
		{
			for(int j = 0; j < totalPlayers; j++)
				score[i][j] = (players[j].getHandSize() * 5) - (pairs.get(players[turn].getCard(i).toString().substring(0,1)) * 2);
		}
		
		//assign the score of a 100 to any card that you know a person has in their hand
		for(int k = 0; k < cardsInHand; k++)
		{
			ArrayList<Integer> knownPlayers = knownCards.get(players[turn].getCard(k).toString().substring(0,1));
			
			if(knownPlayers != null && !knownPlayers.isEmpty())
			{
				for(int p = 0; p < knownPlayers.size(); p++) 
					score[k][knownPlayers.get(p)] = 100;
			}
		}
		
		//find the highest score and assign the card to ask and player to ask
		int highestScore = 0;
		for(int i = 0; i < cardsInHand; i++)
		{
			for(int j = 0; j < totalPlayers; j++)
			{
				if(score[i][j] > highestScore && j != turn) 
				{
					highestScore = score[i][j];
					playerToAsk = j;
					cardIndex = i;
				}
			}
		}
	}
	
	//used to print table
	public void printScoreTable()
	{
		for(int i = 0; i < cardsInHand; i++)
		{
			for(int j = 0; j < totalPlayers; j++)
			{
				System.out.print("  " + score[i][j] + "  ");
			}
			System.out.println();
		}
	}
	
	public int getPlayerToAsk()
	{
		return playerToAsk;
	}
	
	public int getCardIndex()
	{
		return cardIndex;
	}
	
}
