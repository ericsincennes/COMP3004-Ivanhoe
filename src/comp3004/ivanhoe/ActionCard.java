package comp3004.ivanhoe;

public class ActionCard extends Card {
	
	public enum ActionType{	Type1, Type2, Type3, Type4 }
	
	
	public ActionCard(String name) {
		cardName = name;
		cardType = (name.equals("Ivanhoe")) ? CardType.Ivanhoe : CardType.Action;
		
	}
}
