package comp3004.ivanhoe;

import java.util.ArrayList;
import java.util.List;

/**\
 * Representation of an Action Card in the game Ivanhoe
 * 
 */
public class ActionCard extends Card {
	
	/**
	 * Generates an action card object
	 * @param name Name of the card
	 */
	public ActionCard(String name) {
		cardName = name;
		cardType = CardType.Action;
	}
}
