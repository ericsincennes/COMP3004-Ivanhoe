package comp3004.ivanhoe;

public class ActionCard extends Card {
	
	public ActionCard(String name) {
		cardName = name;
		cardType = (name.equals("Ivanhoe")) ? CardType.Ivanhoe : CardType.Action;
	}
}
