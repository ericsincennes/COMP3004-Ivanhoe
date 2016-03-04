package comp3004.ivanhoe;

import comp3004.ivanhoe.Card.CardColour;

public class ActionCardsEngine {
	
	/**
	 * General action card handler
	 * @param card Card 
	 * @param caster player playing action card
	 * @param players players that are effected by the card
	 */
	public void actionHandler(Card card, Player caster,  Player... players){
		
		switch(card.getCardName()){
		case "Unhorse":
			unhorse(caster);
			break;
		case "Change Weapon":
			changeWeapon(caster);
			break;
		case "Drop Weapon":
			dropWeapon(caster);
			break;
		}
	}
	
	public void unhorse(Player caster){
		
	}
	
	public void changeWeapon(Player caster){
		
	}
	
	public void dropWeapon(Player caster){
		
	}
	
}
