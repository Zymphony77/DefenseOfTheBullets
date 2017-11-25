package buff;

import javafx.scene.image.Image;

import entity.job.*;
import skill.*;

public class HasteBuff extends Buff implements Expirable {
	private static final Skill SKILL = new Haste();
	private static final Image IMAGE = new Image("resource/image/HasteIcon.png");
	
	private int maxDuration;
	private int duration;
	
	public HasteBuff(Novice player, int maxDuration) {
		super(player, BuffType.BUFF);
		
		this.maxDuration = maxDuration;
		this.duration = maxDuration;
		
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
		player.setSpeed(player.getSpeed() * 1.5);
	}
	
	public void undrawEffect() {
		player.setSpeed(player.getSpeed() / 1.5);
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
