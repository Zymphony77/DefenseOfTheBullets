package buff;

import javafx.scene.image.Image;

import entity.job.*;
import skill.*;

public class FireOrbBuff extends Buff {
	private static final Skill SKILL = new FireOrb();
	private static final Image IMAGE = new Image("image/FireOrbIcon.png");
	
	private double burnDamage;
	
	public FireOrbBuff(Novice player, double burnDamage) {
		super(player, BuffType.BUFF);
		this.burnDamage = burnDamage;
		
		activateBuff();
	}
	
	public void drawEffect() {
		// NO EFFECT
	}
	
	public void undrawEffect() {
		// NO EFFECT
	}
	
	public Image getImage() {
		return IMAGE;
	}
	
	public Skill getSkill() {
		return SKILL;
	}
	
	public double getBurnDamage() {
		return burnDamage;
	}
}
