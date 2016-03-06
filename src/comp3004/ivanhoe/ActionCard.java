package comp3004.ivanhoe;

import java.util.ArrayList;
import java.util.List;

public class ActionCard extends Card {
	
	public enum TargetType{	Untargeted, TargetPlayers, TargetCards, Ivanhoe }
	
	TargetType targetting;
	List<Object> targetList;
	
	public ActionCard(String name, TargetType tType, Object... targets) {
		cardName = name;
		cardType = (name.equals("Ivanhoe")) ? CardType.Ivanhoe : CardType.Action;
		targetting = tType;
		targetList = new ArrayList<Object>();
		for (Object t : targets) {
			targetList.add(t);
		}
	}
	
	public TargetType getTargetType() {
		return targetting;
	}
	
	public List<Object> getTargetList() {
		return targetList;
	}
}
