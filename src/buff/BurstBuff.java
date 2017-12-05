package buff;

import javafx.scene.image.Image;

import entity.job.*;
import skill.*;

public class BurstBuff extends Buff implements Expirable {
	private static final Skill SKILL = new Burst();
	private static final Image IMAGE = new Image("image/BurstIcon.png");
	
	private int maxDuration;
	private int duration;
	
	public BurstBuff(Novice player, int maxDuration) {
		super(player, BuffType.BUFF);
		
		this.duration = maxDuration;
		this.maxDuration = maxDuration;
		
		activateBuff();
	}
	
	public void update() {
		player.setDirection(duration * 360 / 15);
		player.rotate();
		player.setReloadCount();
		player.shoot();
		--duration;
		
		if(duration <= 0) {
			isActive = false;
			undrawEffect();
		}
	}
	
	public void drawEffect() {
		((Tank) player).setBurstBuff(true);
	}
	
	public void undrawEffect() {
		((Tank) player).setBurstBuff(false);
	}
	
	public int getDuration() {
		return duration;
	}
	
	public Image getImage() {
		return IMAGE;
	}
	
	public Skill getSkill() {
		return SKILL;
	}

	public int getMaxDuration() {
		return maxDuration;
	}
}
