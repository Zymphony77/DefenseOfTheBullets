package buff;

import javafx.scene.image.Image;

import entity.job.*;
import skill.*;

public class HealBuff extends Buff implements Expirable {
	private static final Skill SKILL = new Heal();
	private static final Image IMAGE = new Image("resource/image/HealIcon.png");
	
	private int maxDuration;
	private int duration;
	private int level;
	
	public HealBuff(Novice player, int level,int maxDuration) {
		super(player, BuffType.BUFF);
		
		this.maxDuration = maxDuration;
		this.duration = maxDuration;
		this.level = level;
		
		activateBuff();
	}
	
	public void update() {
		--duration;
		
		if(duration <= 0) {
			isActive = false;
			undrawEffect();
		}
	}
	
	public void drawEffect() {
		player.setHealthRegen(player.getHealthRegen()*(level + 1));
	}
	
	public void undrawEffect() {
		player.setHealthRegen(player.getHealthRegen()/(level + 1));
	}
	
	public int getDuration() {
		return duration;
	}
	
	public int getMaxDuration() {
		return maxDuration;
	}
	
	public Image getImage() {
		return IMAGE;
	}
	
	public Skill getSkill() {
		return SKILL;
	}
}
