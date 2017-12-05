package buff;

import javafx.scene.image.Image;

import entity.job.*;
import skill.*;

public class DouAttBuff extends Buff {
	private static final Skill SKILL = new Haste();
	private static final Image IMAGE = new Image("resource/image/DouAttIcon.png");
	
	private int level;
	
	public DouAttBuff(Novice player, int level) {
		super(player, BuffType.BUFF);
		this.level = level;
		activateBuff();
	}
	
	public void drawEffect() {
		((Ranger) player).setRatioDoubleAtt(4 * level);
	}
	
	public void undrawEffect() {
	}
	
	public Image getImage() {
		return IMAGE;
	}
	
	public Skill getSkill() {
		return SKILL;
	}
}
