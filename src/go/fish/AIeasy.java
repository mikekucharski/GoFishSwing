/**
 * Created by: Mike Kucharski & Ashley Packard
 * Fall 2012 | COMP 285 - OOPS in Java
 */

package go.fish;

import java.util.Random;

public class AIeasy extends Person
{
	private Random randomGenerator;
	
	//constructor, creates a new random
	public AIeasy()
	{
		randomGenerator = new Random();
	}
	
	//returns a random player to ask between 0 and argument value
	public int getPlayerIndexToAsk(int players)
	{
		return randomGenerator.nextInt(players);
	}
	
	//returns a random card to ask between 0 and argument value
	public int getCardIndexToAsk()
	{
		return randomGenerator.nextInt(getHandSize());
	}
}
