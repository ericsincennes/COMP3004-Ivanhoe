
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
		if (TournamentColour == CardColour.Purple 
						&& (colour == CardColour.Purple)) {
			return false;
		}
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
	public boolean actionHandler(Card card, Player caster,  List<Object> target){
		Card taken;
		Random rand = new Random();
		Player opponent = null;
		String chosen = null;
		CardColour colour = null;
		int choiceIndex = 0; //used for outwit
		ArrayList<ArrayList<Integer>> keeping = null; //used for Adapt
		ArrayList<Card> temp = new ArrayList<Card>();
		int cardValue;
		
		for (Object o : target) {
			if (o.getClass().equals(Player.class)) {
				opponent = (Player) o;
			} else if (o.getClass().equals(String.class)) {
				chosen = (String) o;
			} else if (o.getClass().equals(CardColour.class)) {
				colour = (CardColour) o;
			} else if (o.getClass().equals(int.class)) {
				choiceIndex = (int) o;
			} else if (o.getClass().equals(ArrayList.class)){
				keeping.add((ArrayList<Integer>) o);
			}
		}
		
		//handle if opponent has shield shield
		if (opponent != null && opponent.getDisplay().contains("Shield")) {
			return false;
		}

		switch(card.getCardName()){
		case "Unhorse": //target: CardColour
			//color changes from purple to red, blue or yellow
			if ((TournamentColour == CardColour.Purple) && !(colour == CardColour.Green)) {
				TournamentColour = colour;
				return true;
			}
			break;
		case "Change Weapon": //target: CardColour
			// color changes from red, blue or yellow to a different one of these colors
			if (!(TournamentColour == CardColour.Purple) && !(TournamentColour == CardColour.Green)) {
				if (TournamentColour == CardColour.Yellow && colour != CardColour.Yellow) {
					TournamentColour = colour;
					return true;
				} else if (TournamentColour == CardColour.Red && colour != CardColour.Red) {
					TournamentColour = colour;
					return true;
				} else if (TournamentColour == CardColour.Blue && colour != CardColour.Blue) {
					TournamentColour = colour;
					return true;
				}
			}
			break;
		case "Drop Weapon": //target: none
			//color changes from red, blue or yellow to green
			if (!(TournamentColour == CardColour.Purple) && !(TournamentColour == CardColour.Green)) {
				TournamentColour = CardColour.Green;
				return true;
			}
			break;
		case "Break Lance": //target: Player
			temp = new ArrayList<Card>();
			//Force one opponent to discard all purple cards from his display
			if (opponent.isShielded()) { break; }
			
			if (opponent.getDisplay().getCards().size() > 1) {
				for (Card c : opponent.getDisplay().getCards()) {
					if (c.getCardName().contains("Purple")) {
						temp.addAll(opponent.getDisplay().removeColour(CardColour.Purple));
					}
				}
			}
				
			if (temp.size() > 0) {
				for(Card x : temp){
					//add removed cards to discard
					deck.addToDiscard(x);
				}
				return true;
			}
			break;
		case "Riposte": //target Player, Card (to be taken)
			//Take the last card played on any one opponent’s 
			//display and add it to your own display.
			if (opponent.isShielded()) { break; }
			
			if (opponent.getDisplay().getCards().size() > 1) { 
				taken = opponent.getDisplay().remove(opponent.getDisplay().getLastPlayed().getCardName());
				//need to remove the card from cardsPlayed list in deck
				caster.getDisplay().addCard(taken);
				return true;
			}
			break;
		case "Dodge": //target: Player, String (cardname)
			//Discard any one card from any one opponent’s display.
			if (opponent.isShielded()) { break; }
			
			if (opponent.getDisplay().getCards().size() > 1) {
				deck.addToDiscard(opponent.getDisplay().remove(chosen));
				return true;
			}
			break;
		case "Retreat": //target: String (cardname)
			//Take any one card from your own display back into your hand
			
			if (caster.getDisplay().getCards().size() > 1) {
				Card c = caster.getDisplay().remove(chosen);
				caster.getHand().add(c);
				return true;
			}
			break;
		case "Knock Down": //target: Player
			//Draw at random one card from any one opponent’s hand and 
			//add it to your hand, without revealing the card to other opponents.
			if (opponent.isShielded()) { break; }
			
			if (opponent.getHandSize() > 0) {
				int r = rand.nextInt(opponent.getHandSize() + 1);
				taken = opponent.getHand().getCardbyIndex(r);
				opponent.getHand().remove(taken.getCardName());
				caster.getHand().add(taken);
				return true;
			}
			break;
		case "Outmaneuver": //target: none
			//All opponents must remove the last card played on their displays
			temp = new ArrayList<Card>();

			for(Player p : playersList){
				if ((p != caster) && (!p.isShielded()) && (p.getDisplay().getCards().size() > 1)) {
					taken =	p.getDisplay().getLastPlayed();
					temp.add(p.getDisplay().remove(taken.getCardName()));
				}
			}
			
			if (temp.size() > 0) {
				for(Card x : temp){
					//add removed cards to discard
					deck.addToDiscard(x);
				}
				return true;
			}
			break;
		case "Charge": //target: none
			//Identify the lowest value card throughout all displays. 
			//All players must discard all cards of this value from their displays.
			temp = new ArrayList<Card>();
			cardValue = 9;

			for(Player p : playersList){
				if (p.getDisplay().lowestValue() < cardValue) {
					cardValue = p.getDisplay().lowestValue();
				}
			}
			
			for(Player p : playersList){
				if (p.isShielded() || (p.getDisplay().getCards().size() < 2)) { continue; }
				//remove lowest value
				temp.addAll(p.getDisplay().removeValue(cardValue));
			}
			
			if (temp.size() > 0) {
				for(Card x : temp){
					//add removed cards to discard
					deck.addToDiscard(x);
				}
				return true;
			}
			
			break;
		case "Countercharge": //target: none
			//Identify the highest value card throughout all displays.
			//All players must discard all cards of this value from their displays.
			temp = new ArrayList<Card>();
			cardValue = 0;

			for(Player p : playersList){
				if (p.getDisplay().highestValue() > cardValue) {
					cardValue = p.getDisplay().highestValue();
				}
			}
			
			for(Player p : playersList){
				if (p.isShielded() || (p.getDisplay().getCards().size() < 2)) { continue; }
				//remove highest value
				temp.addAll(p.getDisplay().removeValue(cardValue));
			}
			
			if (temp.size() > 0) {
				for(Card x : temp){
					//add removed cards to discard
					deck.addToDiscard(x);
				}
				return true;
			}
			break;
		case "Disgrace": //target: none
			temp = new ArrayList<Card>();
			
			for(Player p : playersList){
				if (p.isShielded() || (p.getDisplay().getCards().size() < 2)) { continue; }
				
				for (Card c : p.getDisplay().getCards()) {
					if (c.getCardName().contains("Squire") || c.getCardName().contains("Maiden")) {
						temp.addAll(p.getDisplay().removeColour(CardColour.White));
					}
				}
			}
			
			if (temp.size() > 0) {
				for(Card x : temp){
					//add removed cards to discard
					deck.addToDiscard(x);
				}
				return true;
			}
			break;
		case "Adapt": //target: none
			temp = new ArrayList<Card>();
			//Each player may only keep one card of each value in his display. 
			//All other cards with the same value are discarded. 
			//Each player decides which of the matching-value cards he will discard.
			
			//This loop assumes that the order of lists in keeping
			//matches the correct player in players list
			for(ArrayList<Integer> intArray : keeping ){
				for(Integer index: intArray){
					Player p = playersList.get(keeping.indexOf(intArray));
					List<Card> cards = p.getDisplay().getCards();
					for(Card x: cards){
						if(cards.indexOf(x) == index){
							temp.add(p.getDisplay().remove(index));
						}
					}
				}
			}
			
			for(Card y : temp){
				deck.addToDiscard(y);
			}
			
			break;
		case "Outwit": //target: Player, Card (yours), Card (opp's card) 
			//Place one of your faceup cards in front of an opponent, 
			//and take one faceup card from this opponent 
			//and place it face up in front of yourself. 
			//This may include the SHIELD and STUNNED cards.
			if (opponent.isShielded() || caster.isShielded()) { break; }
			
			//give card
			if (!caster.getDisplay().getCards().isEmpty() && !opponent.getDisplay().getCards().isEmpty()) {
				//give card
				Card give = caster.getDisplay().getCard(choiceIndex);
				opponent.getDisplay().addCard(give);
			
				//take card
				Card take = opponent.getDisplay().getCard(chosen);
				caster.getDisplay().addCard(take);
			
				//remove cards
				caster.getDisplay().remove(choiceIndex);
				opponent.getDisplay().remove(chosen);
				return true;
			}
			break;
		case "Shield": //target: none
			//A player plays this card face up in front of himself, 
			//but separate from his display. As long as a player has 
			//the SHIELD card in front of him, all action cards have 
			//no effect on his display.
			
			caster.getDisplay().addCard(card);
			
			break;
		case "Stunned": //target: none
			//Place this card separately face up in front of any one opponent.
			//As long as a player has the STUNNED card in front of him, 
			//he may add only one new card to his display each turn.
			
			opponent.getDisplay().addCard(card);
			
			break;
		case "Ivanhoe":
			//todo
			//ss-rank
			break;
		default:
			print("unexpected input");
			break;
		}
		return false;
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
	
	/**
	 * Gets the boardstate of a player, that is all publicly available info for that player
	 * @param p the player who's boardstate we're fetching
	 * @return the boardstate obj created
	 */
	public BoardState makeBoardState(Player p) {
		return new BoardState(p, playersList, p.getHand(), TournamentColour, deck);
	}

}