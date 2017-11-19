package environment;

import javafx.scene.layout.Pane;

import java.util.LinkedList;

import skill.*;

public class SkillPanel extends Pane {
	private LinkedList<SkillIcon> iconList;
	
	public SkillPanel() {
		iconList = new LinkedList<SkillIcon>();
	}
	
	public void addSkill(Skill skill) {
		SkillIcon icon = new SkillIcon(skill);
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
}
