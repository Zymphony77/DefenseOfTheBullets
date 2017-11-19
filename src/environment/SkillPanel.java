package environment;

import javafx.scene.layout.Pane;

import java.util.ArrayList;

import entity.job.*;
import skill.*;

public class SkillPanel extends Pane {
	private Novice player;
	private ArrayList<SkillIcon> iconList;
	
	public SkillPanel() {
		iconList = new ArrayList<SkillIcon>();
	}
	
	public void addSkill(Skill skill) {
		SkillIcon icon = new SkillIcon(player, skill);
		iconList.add(icon);
		getChildren().add(icon);
	}
	
	public void clear() {
		iconList.clear();
		getChildren().clear();
	}
	
	public void update() {
		for(SkillIcon icon: iconList) {
			icon.drawIcon();
		}
	}
	
	public void setPlayer(Novice player) {
		this.player = player;
		
		iconList.clear();
		getChildren().clear();
		
		// Filling backwards to prevent "Overlaying Canvases" which
		// (GUESS) cause the canvases underneath to NOT be able to pick Event
		for(int i = player.getSkillList().size() - 1; i >= 0; --i) {
			SkillIcon icon = new SkillIcon(player, player.getSkillList().get(i));
			getChildren().add(icon);
			iconList.add(icon);
		}
	}
	
	public ArrayList<SkillIcon> getIconList() {
		return iconList;
	}
}
