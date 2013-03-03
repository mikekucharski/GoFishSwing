/**
 * Created by: Mike Kucharski & Ashley Packard
 * Fall 2012 | COMP 285 - OOPS in Java
 */

package go.fish;

import java.util.ArrayList;
import java.util.HashMap;

public class GameState implements GameConstants
{
	private GUIGameBoard mainBoard;
	private Person playerList[];
	private HashMap<String, Integer> pairsPlayed;
	private HashMap<String, ArrayList<Integer>> knownCards;
	private Deck deck;
	private boolean goFish, removedPair, goAgain;
	private int turn, difficultyLevel;
	
	public GameState(GUIGameBoard dis, int difficulty)
	{
		mainBoard = dis;
		difficultyLevel = difficulty;
		deck = new Deck();
		turn = PLAYER0TURN;
		goFish = false;
		removedPair = false;
		goAgain = true;
		
		//new map to keep track of pairs of ranks played. initially all pairs played are 0
		pairsPlayed = new HashMap<String, Integer>();
		for(int i = 0; i < allRanks.length; i++)
			pairsPlayed.put(allRanks[i].substring(0, 1), 0);
		
		//keeps track of the cards a player definitely has based off when they ask for cards
		//initially creates an empty list associated with every rank
		knownCards = new HashMap<String, ArrayList<Integer>>();
		for(int i = 0; i < allRanks.length; i++)
			knownCards.put(allRanks[i].substring(0, 1), new ArrayList<Integer>());
		
		playerList = new Person[4];
		playerList[0] = new Person();
		playerList[0].setPanelNumber(PLAYER0TURN);
		
		//if the difficulty was selected easy, fill the rest of the players with randomAI,
		// else fill with hardAI
		if(difficultyLevel == 1)
		{
			for(int i = 1; i < playerList.length; i++){
				playerList[i] = new AIeasy();
				playerList[i].setPanelNumber(i);
			}
		}
		else
		{
			for(int i = 1; i < playerList.length; i++){
				playerList[i] = new AIhard();
				playerList[i].setPanelNumber(i);
			}
		}
	}
	
	//sets and gets for our gameState variables
	public void setTurn(int turnNumber)     {turn = turnNumber;}
	public Deck getDeck()	{return deck;}
	public boolean isPairRemoved()	{return removedPair;}
	public Person[] getPlayers()	{return playerList;}
	public boolean isGoFish()	{return goFish;}
	public int getTurnNumber()	{return turn;}

	//this method assigns cards to every player's hand and flips them up if necessary
	public void dealCards() 
	{
		for(int players = 1; players < playerList.length; players++)
		{
			for(int i = 0; i < STARTING_CARDS; i++)	{
				//cards start face down, no need to turn them over for the robots!
				Card temp = deck.drawFromDeck();
				playerList[players].draw(temp);
			}
		}
		
		for(int i = 0; i < STARTING_CARDS; i++){
			//get all the user's cards and flip them face up
			Card temp = deck.drawFromDeck();
			temp.flipCard();
			playerList[0].draw(temp);
		}
		
		mainBoard.drawGame(playerList);
	}

	//used for user to remove pairs from their hand, removes the two cards if they have equal ranks
	public void checkUserPair(String firstCard, String secondCard)
	{
		if( firstCard.substring(0, 1).equals(secondCard.substring(0, 1) )){
			playerList[0].removePair(firstCard, secondCard);
			
			//get the pair name that has been removed and display it
			String pair = firstCard.substring(0, firstCard.indexOf(" "));
			mainBoard.addTextToLog("You have removed a pair of " + pair + "'s!");
			//update maps
			incrementMapPair(firstCard);
			updateKnownPairs(firstCard, turn);
			
			//give option to skip your turn when the deck and your hand is empty
			if(isHandEmpty(0) && deck.isDeckEmpty())	{mainBoard.setButtonClickable(true);}
			
			mainBoard.drawGame(playerList);
		}
	}

	//increments turn, loops through robot's hand and removes their pairs, sets button text
	public void processAIFirstTurn() 
	{
		//increment turn
		incrementTurn();

		//loop through robot's hand and removes their pairs. updates map accordingly
		ArrayList<String> pairsFound = playerList[turn].autoRemovePairs();
		for (int i = 0; i < pairsFound.size(); i++) {
			mainBoard.addTextToLog("Player " + turn + " has removed a pair of " + pairsFound.get(i) + "'s!");
			incrementMapPair(pairsFound.get(i));
		}
		
		mainBoard.drawGame(playerList);

		//update "continue button" button text
		if (turn == PLAYER3TURN)
			mainBoard.setButtonText("Your Turn!");
		else
			mainBoard.setButtonText("Player " + (turn + 1) + "'s First Turn");
	}
	
	//increment turn by 1, unless the turn is 3 in which case it is the user's turn again.
	public void incrementTurn()
	{
		if(turn+1 > PLAYER3TURN)
			setTurn(PLAYER0TURN);
		else
			turn++;
	}

	//if opponent has what you are looking for, remove cards from both hands and update maps accordingly
	//if the opponent does not have the card, player must go fish. robots automatically draw from deck
	public void checkOpponentHandForMatch(String cardSelectedName, int playerAsked) 
	{		
		//if player asked does have the card we're looking for
		if(playerList[playerAsked].contains(cardSelectedName))
		{
			//remove card from both player's hands
			playerList[turn].removeCard(cardSelectedName);
			int numPairs = pairsPlayed.get(cardSelectedName.substring(0, 1));
			numPairs++;
			
			//update maps
			pairsPlayed.put(cardSelectedName.substring(0, 1), numPairs);
			updateKnownPairs(cardSelectedName, playerAsked);
			
			playerList[turn].incrementMatches();
			removedPair = true;
		
			//update log when pairs are removed
			if(turn == PLAYER0TURN)
				mainBoard.addTextToLog(cardSelectedName + " has been removed from your hand");
			else
				mainBoard.addTextToLog(cardSelectedName + " has been removed from Player " + turn + "'s hand");
		}
		else{
			// goAgain is false so the opponents drop out of loop in AI turn
			goAgain = false;
			//set and display go fish true as long as there are cards in the deck
			if(!deck.isDeckEmpty()){
				goFish = true;
				mainBoard.displayGoFish(goFish);
			}
			else{
				mainBoard.addTextToLog("The deck is empty and your turn is now over, please advance to Player 1's turn.");
				mainBoard.setButtonClickable(true);
			}
	
			//update known cards map
			ArrayList<Integer> players = knownCards.get(cardSelectedName.substring(0, 1));
			if(!players.contains(turn))	    {players.add(turn);}
			knownCards.put(cardSelectedName.substring(0, 1), players);
			
			//if it isn't the user's turn, auto draw from deck, remove pairs, update maps
			if(turn != PLAYER0TURN){
				drawFromDeck(turn);
				
				ArrayList<String> pairsFound = playerList[turn].autoRemovePairs();
				for(int i = 0; i < pairsFound.size(); i++){
					mainBoard.addTextToLog("Player " + turn + " has removed a pair of " + pairsFound.get(i) + "'s!");
					incrementMapPair(pairsFound.get(i));
					updateKnownPairs(pairsFound.get(i), turn);
				}
			}
		}
		
		//if the current player's hand is empty, enable the continue button to skip their turn
		if(isHandEmpty(turn))	{mainBoard.setButtonClickable(true);}
		
		mainBoard.drawGame(playerList);
	}
	
	//draws a card from the deck and adds it to the proper player's hand. updates log.
	//sets go fish false and if deck is empty, display it.
	public void drawFromDeck(int player)
	{
		//if the deck is empty, display that and exit draw function
		if(deck.isDeckEmpty()){
			mainBoard.displayDeckEmpty(true);
			mainBoard.drawGame(playerList);
			return;
		}

		//deal a card, if the card is for the user, flip it. add it to the player's hand
		Card temp = deck.drawFromDeck();
		if(player == PLAYER0TURN)	{temp.flipCard();}
		playerList[player].draw(temp);
		
		//update log to display card drawn
		if(turn == PLAYER0TURN)
			mainBoard.addTextToLog("You drew a(n) " + temp.toString());
		else
			mainBoard.addTextToLog("Player " + turn + " drew a card from the deck.");
		
		//set go fish false and display it
		goFish = false;
		if(player == PLAYER0TURN)	{mainBoard.displayGoFish(goFish);}

		//if deck is empty, display it
		if(deck.isDeckEmpty())	{mainBoard.displayDeckEmpty(true);}
		
		//draw game
		mainBoard.drawGame(playerList);
	}
	
	//changes the text of the button to the next turn
	public void updateButtonText()
	{
		if(turn == PLAYER3TURN){
			mainBoard.setButtonText("Your Turn!");
			removedPair = false;
		}
		else
			mainBoard.setButtonText("Player " + (turn+1) + "'s Turn");
	}

	//return true if all other players hands are empty. else returns false
	public boolean isAllOtherHandsEmpty(int player)
	{
		boolean allHandsEmpty = true;
		for(int i = 0; i < playerList.length; i++){
			if(i != player && !isHandEmpty(i))	{allHandsEmpty = false;}
		}
		
		return allHandsEmpty;
	}
	
	// opponent's turn is processed, dealing with choosing a player and a card
	public void processAITurn() 
	{
		// increment turn at the beginning
		incrementTurn();
		
		// if no one has any cards then automatically draw a card and update the button
		if(isAllOtherHandsEmpty(turn)){
			drawFromDeck(turn);
			updateButtonText();
			return;
		}
		
		// if the player's hand is empty then refill their hand, remove any pairs, and update maps
		if(isHandEmpty(turn))
		{
			for(int i = 0; i < REFILL_CARDS; i++)
				drawFromDeck(turn);
			
			ArrayList<String> pairsFound = playerList[turn].autoRemovePairs();
			for(int i = 0; i < pairsFound.size(); i++)
			{
				// update log and maps
				mainBoard.addTextToLog("Player " + turn + " has removed a pair of " + pairsFound.get(i) + "'s!");
				incrementMapPair(pairsFound.get(i));
				updateKnownPairs(pairsFound.get(i), turn);
			}
			
			updateButtonText();
			return;
		}
		
		// while the opponent has not been told go gish, they may continue to ask for pairs
		goAgain = true;
		while(goAgain)
		{
			// if the player's hand is empty then exit from loop
			if(isHandEmpty(turn))
			{
				goAgain = false;
				break;
			}

			int cardNumber = 0, playerNumber = 0;

			// if this player was initialized as an easy robot 
			// then create a temporary robot in order to access the AIeasy functions
			// since originally we had all robots in a person array
			if(playerList[turn] instanceof AIeasy)
			{
				// an easy robot will randomly select a card from the hand
				// an easy robot will randomly select a player to ask
				AIeasy tempRobot = (AIeasy) playerList[turn];
				cardNumber = tempRobot.getCardIndexToAsk();
				playerNumber = tempRobot.getPlayerIndexToAsk(playerList.length);
				
				// this checks to make sure the robot did not select either itself
				// or someone who does not have any cards in their hand
			   	while(playerNumber == turn || isHandEmpty(turn))
		      	{
		      		playerNumber = tempRobot.getPlayerIndexToAsk(playerList.length);
		      	}
			}
			// if this player was initialized as a hard robot 
			// then create a temporary robot in order to access the AIhard functions
			// since originally we had all robots in a person array
			else if(playerList[turn] instanceof AIhard)
			{
				// a hard robot selects a player and a card using a scoring algorithm
				// the AIhard class uses the maps
				AIhard tempRobot = (AIhard) playerList[turn];
				tempRobot.setScores(playerList, turn, pairsPlayed, knownCards);
				cardNumber = tempRobot.getCardIndex();
				playerNumber = tempRobot.getPlayerToAsk();
			}
			else
			{
				System.out.println("Static casting went wrong");
			}
			
	      	Card card = playerList[turn].getCard(cardNumber);
	      	String playerCard = card.toString();
	      	
	      	// Update log
	      	if(playerNumber == PLAYER0TURN)
	      		mainBoard.addTextToLog(" *** Player " + turn + " is looking for a(n) " + playerCard + " and is asking you");
	      	else
	      		mainBoard.addTextToLog(" *** Player " + turn + " is looking for a(n) " + playerCard + " and is asking Player " + playerNumber);
	      	
	      	// check to see if the player asked has that card
	      	checkOpponentHandForMatch(playerCard, playerNumber);
		}
		updateButtonText();
		
		// if it's player three's turn then check to see if the user has card 
		// or whether the deck is empty so we can skip them
		if(turn == PLAYER3TURN)
		{
			// Check to see if deck empty, my hand empty, skip me
			if(isHandEmpty(0) && deck.isDeckEmpty())
			{
				mainBoard.setButtonClickable(true);
			}
		}
	}

	// return true if the player has no cards, otherwise false
	public boolean isHandEmpty(int player) 
	{
		if(playerList[player].getHandSize() == 0)
			return true;
		return false;
	}

	// returns true if the deck is empty and all players have no cards in hand
	public boolean isGameOver() 
	{
		for(int i = 0; i < playerList.length; i++){
			if(!isHandEmpty(i))
				return false;
		}

		if(deck.isDeckEmpty())
			return true;

		return true;
	}

	// return the winner!
	public int getWinner() 
	{
		int player = 0;
		for(int i = 1; i < playerList.length; i++){
			if(playerList[i].getMatches() > playerList[player].getMatches())
				player = i;
		}
		
		for(int j = 0; j < playerList.length; j++)
		{
			if(j!= player && playerList[j].getMatches() == playerList[player].getMatches())
				return -1;
		}
		
		return player;
	}
	
	// removes users from knownPairs map
	public void updateKnownPairs(String cardName, int playerAsked)
	{
		//remove from knownpairs map
		ArrayList<Integer> players = knownCards.get(cardName.substring(0, 1));
		if(players.contains(turn))
			players.remove((Object) turn);
		if(playerAsked != turn && players.contains(playerAsked)){
			// Static cast our integer into an object so its not considered an index!
			players.remove((Object) playerAsked);
		}
		
		knownCards.put(cardName, players);
	}
	
	// increments the number of pairs associated with the key
	public void incrementMapPair(String cardName)
	{
		//incremement pair map
		int numPairs = pairsPlayed.get(cardName.substring(0, 1));
		numPairs++;
		pairsPlayed.put(cardName.substring(0, 1), numPairs);
	}
	
	// these are for testing purposes only
	public void printPairMap()
	{
		System.out.println("New Map \n ------------------ \n");
		for(int p = 0; p < allRanks.length; p++)
			System.out.println(""+ allRanks[p] + " : " + pairsPlayed.get(allRanks[p].substring(0,1)));
	}
	
	public void printKnownCardsMap()
	{
		System.out.println("Known Cards \n ------------------ \n");
		for(int p = 0; p < allRanks.length; p++)
		{
			System.out.print("\n"+ allRanks[p] + " :");
			ArrayList<Integer> players = knownCards.get(allRanks[p].substring(0,1));
			for(int i = 0; i < players.size(); i++)
			{
				System.out.print(" " + players.get(i));
			}
		}
		System.out.println();
	}
	
}
