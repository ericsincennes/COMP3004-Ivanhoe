
package comp3004.ivanhoe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import comp3004.ivanhoe.Card.CardColour;
import comp3004.ivanhoe.Card.CardType;

public class RulesEngine {
	private HashMap<Long, Player> players;
	private List<Player> playersList;
	private int numPlayers = 0, expectedPlayers;
	private Card.CardColour TournamentColor = null;
	private Deck deck, discard;
	private boolean firstTournament = true;
	private int highestScore = 0;
	private int activePlayer;

	public RulesEngine(int i){
		expectedPlayers = i;
		players = new HashMap<Long, Player>();
		playersList = new ArrayList<Player>();
		discard = Deck.createDiscard();
		deck = Deck.createDeck(discard);
		deck.testDeck();
	}
	
	/**
	 * Registers a player with the Rules engine
	 * @param ID this.currentThread.getID() of the player thread
	 * @return boolean of success
	 */
	public synchronized int registerThread(long ID){
		//is game full?	
		if(players.containsKey(ID)){
			//notifyAll();
			return -1;
		}
		else if(numPlayers >= expectedPlayers){
			//notifyAll();
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
	 * initialize the tournament colour
	 * @param colour CardColour
	 * @return Boolean
	 */
	public boolean initializeTournamentColour(CardColour colour){
		for(Player p : playersList){
			p.getDisplay().setColour(colour);
		}
		TournamentColor = colour;
		return true;
	}
	
	/**
	 * Choose who starts the first tournament
	 * @return player number of the first tournament starter
	 */
	public synchronized long chooseFirstTournament(){
		Collections.shuffle(playersList);
		//notifyAll();
		activePlayer = 0;
		return playersList.get(0).getid();
	}

	/**
	 * Checks if the player can start a tournament 
	 * @param id id of player
	 * @return boolean
	 */
	private boolean canStartTournament(long id){
		List<Card> hand = getPlayerById(id).getHand();
		for(Card c: hand){
			if(c.getCardType() == CardType.Colour || c.getCardType() == CardType.Supporter){
				if(TournamentColor == CardColour.Purple 
						&& ((ColourCard) c).getColour() != CardColour.Purple){
					return true;
				} else if(TournamentColor != CardColour.Purple ) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Initializing a tournament that is not the first tournament
	 * @param colour 
	 */
	public boolean initTournament(CardColour colour){
		Collections.rotate(playersList, -1*activePlayer);
		activePlayer = 0;

		if(TournamentColor == CardColour.Purple && colour == CardColour.Purple){
			activePlayer++;
			return false;
		} else {
			if(canStartTournament(playersList.get(0).getid())){
				initializeTournamentColour(colour);
				return true;
			}
		}
		activePlayer++;
		return false;
	}
	
	/**
	 * Deals a hand of 8 cards to each player
	 */
	public void dealHand(){
		for(Player p : playersList){
			for(int i =0; i < 8; i++){
				drawCard(p.getid());
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
			activePlayer++;
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
	 */
	public void withdrawPlayer(Long id){
		Player p = getPlayerById(id);
		//TODO: Maiden check
		p.setPlaying(false);
		p.getDisplay().clearBoard();
	}

	/**
	 * Plays a card to the playes display and check if the play is valid
	 * @param cardname name of card
	 * @param id player id
	 * @return boolean
	 */
	public boolean playCard(String cardname, Long id){
		Player p = players.get(id);
		Card c = p.getCardByName(cardname);
		boolean b = validatePlay(cardname, id);
		if(b){
			p.playColourCard(cardname);
			p.removeCard(cardname);
			return true;
		} else if(c.cardType == CardType.Action){
			p.playActionCard(cardname);
			p.removeCard(cardname);
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
		System.out.println("ValidatePlay Card " + card + " ID " + id); //testing
		//if card in player hand
		if(p.hasInHand(card)){
			c = p.getCardByName(card);
			//if card is colour card
			if(c.getCardType() == CardType.Colour &&
					((ColourCard)c).getColour() == TournamentColor){
				return true;

				//Support cards dont care about tournement colour
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
			boolean win = false;
			for(Player p: playersList){
				if(p.getid() != id){
					win |= p.getPlaying();
				}
			}
			if(!win){ 
				roundCleanup(); 
			} else {
				activePlayer++;
				return true;
			}

		}
		return false;
	}

	/**
	 * Clears the board for all players and resets the active player
	 */
	public void roundCleanup(){
		for(Player p : playersList){
			p.getDisplay().clearBoard();
		}
		activePlayer = 0;
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
}