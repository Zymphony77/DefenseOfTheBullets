package environment;

import javafx.scene.layout.Pane;

import java.util.ArrayList;

import entity.job.*;
import skill.*;

public class SkillPane extends Pane {
	private Novice player;
	private ArrayList<SkillIcon> iconList;
	
	public SkillPane() {
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
		clear();
		this.player = player;
		
		// Filling backwards to prevent "Overlaying Canvases" which
		// (GUESS) cause the canvases underneath to NOT be able to pick Event
		for(int i = player.getSkillList().size() - 1; i >= 0; --i) {
			SkillIcon icon = new SkillIcon(player, player.getSkillList().get(i));
			iconList.add(icon);
			getChildren().add(icon);
			if(icon.isUpgradable()) {
				icon.drawUpgrade();
			} else {
				icon.undrawUpgrade();
			}
		}
	}
	
	public ArrayList<SkillIcon> getIconList() {
		return iconList;
	}
	
	public Novice getPlayer() {
		return player;
	}
	
	public SkillIcon getIcon(int position) {
		for(SkillIcon icon: iconList) {
			if(icon.getPosition() == position) {
				return icon;
			}
		}
		return null;
	}
}
