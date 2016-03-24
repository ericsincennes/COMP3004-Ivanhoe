
package comp3004.ivanhoe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import comp3004.ivanhoe.Card.CardColour;
import comp3004.ivanhoe.Card.CardType;

public class RulesEngine {
	private HashMap<Long, Player> players;
	private List<Player> playersList;
	private int numPlayers = 0, expectedPlayers;
	private Card.CardColour TournamentColour = null;
	private Deck deck, discard;
	private boolean colourChosen = false;
	private int highestScore = 0;

	public RulesEngine(int i){
		expectedPlayers = i;
		players = new HashMap<Long, Player>();
		playersList = new ArrayList<Player>();
		discard = Deck.createDiscard();
		deck = Deck.createDeck(discard);
		deck.ivanhoeDeck();
	}
	
	/**
	 * Initalizes a testing rules engine. All cards are squires
	 * @param i number of expected players
	 * @return RulesEngine
	 */
	public static RulesEngine testRuleEngine(int i) {
		RulesEngine test = new RulesEngine(i);
		test.deck = Deck.createDeck(test.discard);
		test.deck.testDeck();
		return test;
	}
	
	/**
	 * Returns a list containing all players regardless
	 * of if they are playing in the current tournament or not
	 * @return List
	 */
	public synchronized List<Player> getPlayerList(){
		notifyAll();
		return playersList;
	}
	
	/**
	 * Returns the highest score recorded by the rules engine for the current tournament
	 * @return int
	 */
	public int getHighestScore() {
		return highestScore;
	}
	
	/**
	 * Returns if a colour has been chosen for the current tournament
	 * to allow for normal play to begin
	 * @return boolean
	 */
	public boolean isColourChosen() {
		return isTournamentRunning() && colourChosen;
	}
	
	/**
	 * Registers a player with the Rules engine
	 * @param ID this.currentThread.getID() of the player thread
	 * @return player number, as in order they joined game
	 */
	public synchronized int registerThread(long ID){
		//is game full?	
		if(players.containsKey(ID)){
			notifyAll();
			return -1;
		}
		else if(numPlayers >= expectedPlayers){
			notifyAll();
			return -1;
		}

		Player p = new Player();
		p.setid(ID);
		players.put(ID, p);
		playersList.add(p);
		numPlayers++;
		return numPlayers;
	}

	/**
	 * Checks if a tournament is running.
	 */
	public boolean isTournamentRunning(){
		int count = 0;
		for(Player p : playersList){
			if(p.getPlaying()){
				count++;
			}
		}
		if(count > 1) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * Does startup for the first tournament
	 * @return player number of the first tournament starter
	 */
	public synchronized long initFirstTournament(){
		Collections.shuffle(playersList);
		//notifyAll();
		for(Player p : playersList){
			for(int i = 0; i < 8; i++){
				drawCard(p.getID());
			}
		}
		return playersList.get(0).getID();
	}

	/**
	 * Checks if the player can start a tournament of any colour
	 * @param id - id of player
	 * @return boolean
	 */
	public boolean canStartTournament(long id){
		for(Card c:  getPlayerById(id).getHand().getHand()){
			if(c.getCardType() == CardType.Colour || c.getCardType() == CardType.Supporter){
				if(TournamentColour == CardColour.Purple 
						&& ((ColourCard) c).getColour() != CardColour.Purple){
					return true;
				} else if(TournamentColour != CardColour.Purple ) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Checks if the player can start a tournament of the specific colour
	 * @param id - id of player
	 * @param colour - colour player wants to set tournament to
	 * @return boolean
	 */
	public boolean canStartTournament(long id, CardColour colour){
		for(Card c:  getPlayerById(id).getHand().getHand()){
			if(c.getCardType() == CardType.Colour && ((ColourCard)c).getColour() == colour || c.getCardType() == CardType.Supporter){
				if(TournamentColour == CardColour.Purple 
						&& ((ColourCard) c).getColour() != CardColour.Purple){
					return true;
				} else if(TournamentColour != CardColour.Purple ) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * initialize the tournament colour, should only be called if a tournament can be started
	 * @param id ID of player initializing tournament colour
	 * @param colour CardColour
	 * @return Boolean
	 */
	public boolean initializeTournamentColour(long id, CardColour colour){
		if (!canStartTournament(playersList.get(0).getID(),colour)) {
			colourChosen = false;
			return false;
		}
		for(Player p : playersList){
			p.getDisplay().setColour(colour);
		}
		TournamentColour = colour;
		colourChosen = true;
		return true;
	}
	
	/**
	 * A player has failed to start a tournament, so we go to next player
	 */
	public void failInitTournamentColour() {
		Collections.rotate(playersList, -1);
	}
	
	/**
	 * Initializing gamestate for a tournament if no tournament is started
	 */
	public void initTournament(){
		if (isTournamentRunning()) {
			return;
		}
		for(Player p : playersList){
			p.setPlaying(true);
		}
		colourChosen = false;
	}
	
	
	/**
	 * Deals a hand of 8 cards to each player
	 */
	public void dealHand(){ //up for being replaced by initFirstTournament
		for(Player p : playersList){
			for(int i =0; i < 8; i++){
				drawCard(p.getID());
			}
		}
	}

	/**
	 * Checks if the player can play their turn
	 * if true then draw a card
	 * else go to next player
	 * @param id player id
	 * @return boolean
	 */
	public boolean startTurn(long id){
		Player p = getPlayerById(id);
		if(p.getPlaying()){
			p.addCard(deck.draw());
			return true;
		} else {
			Collections.rotate(playersList, -1);
			return false;
		}
	}
	
	/**
	 * Adds a card to the players hand
	 * @param id ID of player
	 */
	public void drawCard(long id){
		getPlayerById(id).addCard(deck.draw());
	}
	
	/**
	 * Removes  the player from the tournament
	 * @param id
	 * @return if a maiden is removed or not
	 */
	public boolean withdrawPlayer(Long id){
		Player p = getPlayerById(id);
		p.setPlaying(false);
		boolean hasMaiden = p.getDisplay().contains("Maiden");
		List<Card> toDiscard = p.getDisplay().clearBoard();
		for (Card c : toDiscard) {
			deck.addToDiscard(c);
		}
		return hasMaiden;
	}
	
	/**
	 * Does cleanup checks and position changes after withdrawing
	 * @return winner's ID, and puts them in pos 0 of player list
	 */
	public long withdrawCleanup(long id) {
		Player p = getPlayerById(id);

		if (isTournamentRunning()) {
			Collections.rotate(playersList, -1);
			return -1;
		}
		else {
			for(Player x : playersList){
				if(x.getPlaying()){
					Collections.rotate(playersList, -1*playersList.indexOf(x)); //might need to change where this gets done
					return x.getID();											//due to server synchro issues
				} 
			}
			return -1;
		}
	}
	
	public boolean giveToken(long id, CardColour c) {
		Player p = getPlayerById(id);
		if (TournamentColour == null) { print("wtf why is tourney colour null?"); }
		if (c!= null && TournamentColour != null
				&& (TournamentColour.equals(CardColour.Purple) || c.equals(TournamentColour))) {
			p.recieveToken(c);
			return true;
		}
		return false;
	}

	/**
	 * Plays a card to the players display and check if the play is valid
	 * @param cardname name of card
	 * @param id player id
	 * @return boolean
	 */
	public synchronized boolean playCard(int posinhand, Long id){
		Player p = players.get(id);
		Card c;
		//Check if the card is in the players hand
		if(posinhand > -1 && posinhand < p.getHandSize()){
			c = p.getHand().getCardbyIndex(posinhand);
		} else {
			return false;
		}
		//Null checker
		if (c == null) {
			notifyAll();
			return false; 
		}
		//Validate play
		boolean b = validatePlay(c.getCardName(), id);
		
		if(b){
			//if card is a valid colour card
			p.playColourCard(posinhand);
			notifyAll();
			return true;
		} else if(c != null && c.cardType == CardType.Action){
			//if card is an action card
			
			p.playActionCard(posinhand);
			notifyAll();
			return true;
		}
		return false;
	}
	
	/**
	 * Validates if playing a colour card is possible given the current tournament colour
	 * @param card String name of card
	 * @param id Long ID of player
	 * @return True if card is played
	 */
	public boolean validatePlay(String card, Long id){
		//if player exists
		if(!players.containsKey(id)){
			return false;
		}

		Player p = players.get(id);
		Card c;
		//System.out.println("ValidatePlay Card " + card + " ID " + id); //testing
		//if card in player hand
		if(p.hasInHand(card)){
			c = p.getHand().getCardByName(card);
			//if card is colour card
			if(c.getCardType() == CardType.Colour &&
					((ColourCard)c).getColour() == TournamentColour){
				return true;

				//Support cards don't care about tournament colour
			} else if(c.getCardType() == CardType.Supporter){
				return true;

				//Action cards
			} else if(c.getCardType() == CardType.Action){
				//TODO validate Action Cards
				return true;
			}
		}
		return false;
	}
	
	//nested rules engine action card handler
	//assuming one player target
	public void actionHandler(Card card, Player caster,  Object... target){
		Card taken;
		Random rand = new Random();
		Player opponent = null;
		String chosen = null;
		CardColour colour = null;
		int cardValue = 0;
		
		for (int i = 0; i < target.length; i++) {
			if (target[i].getClass().equals(Long.class)) {
				opponent = players.get(target[i]);
			} else if (target[i].getClass().equals(String.class)) {
				chosen = (String) target[i];
			} else if (target[i].getClass().equals(CardColour.class)) {
				colour = (CardColour) target[i];
			}
		}

		switch(card.getCardName()){
		case "Unhorse":
			//color changes from purple to red, blue or yellow
			if (TournamentColour == CardColour.Purple) {
				TournamentColour = colour;
			}
			break;
		case "Change Weapon":
			// color changes from red, blue or yellow to a different one of these colors
			if (!(TournamentColour == CardColour.Purple) || !(TournamentColour == CardColour.Green)) {
				if (TournamentColour == CardColour.Yellow && colour != CardColour.Yellow) {
					TournamentColour = colour;
				} else if (TournamentColour == CardColour.Red && colour != CardColour.Red) {
					TournamentColour = colour;
				} else if (TournamentColour == CardColour.Blue && colour != CardColour.Blue) {
					TournamentColour = colour;
				}
			}
			break;
		case "Drop Weapon":
			//color changes from red, blue or yellow to green
			if (!(TournamentColour == CardColour.Purple)) {
				TournamentColour = CardColour.Green;
			}
			break;
		case "Break Lance":
			//Force one opponent to discard all purple cards from his display
			if (target[0] instanceof Player){
				((Player) target[0]).getDisplay().removeColour(CardColour.Purple);
			}
			break;
		case "Riposte":
			//take card from opponent display
			taken = opponent.getDisplay().getLastPlayed();
			opponent.getDisplay().remove(taken.getCardName());
			//need to remove the card from cardsPlayed list in deck
			caster.getDisplay().addCard(taken);
			break;
		case "Dodge":
			//Discard any one card from any one opponent’s display.
			opponent.getDisplay().remove(chosen);
			break;
		case "Retreat":
			//Take any one card from your own display back into your hand
			//Card c = caster.getDisplay().remove(chosen);
			//caster.getHand().add(c);
			//need to remove the card from playedCards list in deck
			break;
		case "Knock Down":
			int r = rand.nextInt(opponent.getHandSize() + 1);
			taken = opponent.getHand().getCardbyIndex(r);
			opponent.getHand().remove(taken.getCardName());
			caster.getHand().add(taken);
			break;
		case "Outmaneuver":
			for(Player p : playersList){
				if (p != caster) {
					taken =	p.getDisplay().getLastPlayed();
					p.getDisplay().remove(taken.getCardName());
				}
			}
			break;
		case "Charge":
			for(Player p : playersList){
				if (p.getDisplay().lowestValue() > cardValue) {
					cardValue = p.getDisplay().lowestValue();
				}
			}
			
			for(Player p : playersList){
				p.getDisplay().removeValue(cardValue);
			}
			break;
		case "Countercharge":
			for(Player p : playersList){
				if (p.getDisplay().highestValue() > cardValue) {
					cardValue = p.getDisplay().highestValue();
				}
			}
			
			for(Player p : playersList){
				p.getDisplay().removeValue(cardValue);
			}
			break;
		case "Disgrace":
			for(Player p : playersList){
				p.getDisplay().removeColour(CardColour.White);
			}
			break;
		case "Adapt":
			//todo
			//a-rank
			break;
		case "Outwit":
			//todo
			//a-rank
			break;
		case "Shield":
			//todo
			//s-rank
			break;
		case "Stunned":
			//todo
			//s-rank
			break;
		case "Ivanhoe":
			//todo
			//ss-rank
			break;
		default:
			print("unexpected input");
			break;
		}
	}

	/**
	 * Checks the highest score and compares it with the players score
	 * to see if they can legally end their turn
	 * @param id player id
	 * @return boolean
	 */
	public boolean canEndTurn(Long id){
		return (players.get(id).getDisplay().calculatePoints() > highestScore);
	}

	/**
	 * Checks if a player can end their turn
	 * 	if yes checks if the player is the last player left
	 * 		if yes ends the round
	 * 	else moves to the next player
	 * @param id player id
	 * @return boolean
	 */
	public boolean endTurn(long id){
		if(canEndTurn(id)){
			highestScore = players.get(id).getDisplay().calculatePoints();
			Collections.rotate(playersList, -1);
			return true;
		}
		return false;
	}

	/**
	 * Clears the board for all players and resets the active player
	 */
	public void roundCleanup(){
		for(Player p : playersList){
			List<Card> toDiscard = p.getDisplay().clearBoard();
			for (Card c : toDiscard) {
				deck.addToDiscard(c);
			}
		}
		colourChosen = false;
	}
	
	/**
	 * checks if there is a winner to the game yet
	 * @returns the winning player or null if none exist
	 */
	public Player gameWinner() {
		for (Player p : playersList) {
			if ((numPlayers >=2 && numPlayers <=3 && p.getTokenCount() == 4) ||
					(numPlayers >=4 && p.getTokenCount() == 5)){
				return p;
			}
			
		}
		return null;
	}
	
	/**
	 * Convenience function
	 * @param s String to be printed
	 */
	private void print(String s){
		System.out.println(s);
	}

	/**
	 * Returns the player if the id exists
	 * @param id Long 
	 * @return Player
	 */
	public Player getPlayerById(long id){
		if(players.containsKey(id)){
			return players.get(id);
		}
		return null;
	}
	
	public Card.CardColour getTournamentColour() {
		return TournamentColour;
	}
	
	public Deck getDeck(){
		return deck;
	}

}