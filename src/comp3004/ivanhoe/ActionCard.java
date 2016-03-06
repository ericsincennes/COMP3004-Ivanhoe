package comp3004.ivanhoe;

public abstract class ActionCard extends Card {
	
	public enum ActionType{	Untargeted, TargetOne, TargetMultiple, Ivanhoe }
	
	ActionType ActionCardType;
	Object[] target;
	
	public ActionCard(String name) {
		cardName = name;
		cardType = (name.equals("Ivanhoe")) ? CardType.Ivanhoe : CardType.Action;
	}
}
