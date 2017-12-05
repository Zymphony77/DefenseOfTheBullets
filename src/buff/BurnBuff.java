package buff;

import entity.job.*;
import javafx.scene.image.Image;
import main.Main;
import skill.Skill;

public class BurnBuff extends Buff implements Expirable {
	private static final Image IMAGE = new Image("image/FireOrbIcon.png");
	private static final int MAX_DURATION = 2 * Main.FRAME_RATE;
	
	private Magician caster;
	private int duration;
	private double burnDamage;
	
	public BurnBuff(Novice player, double burnDamage, Magician caster) {
		super(player, BuffType.DEBUFF);
		
		this.duration = MAX_DURATION;
		this.burnDamage = burnDamage;
		
		activateBuff();
	}
	
	public void drawEffect() {
		// NO EFFECT
	}
	
	public void undrawEffect() {
		// NO EFFECT
	}
	
	public void update() {
		--duration;
		player.takeDamage(caster, burnDamage / Main.FRAME_RATE);
		
		if(duration <= 0) {
			isActive = false;
			undrawEffect();
		}
	}
	
	public Image getImage() {
		return IMAGE;
	}
	
	public Skill getSkill() {
		return null;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public int getMaxDuration() {
		return MAX_DURATION;
	}
	
	public double getBurnDamage() {
		return burnDamage;
	}
}
