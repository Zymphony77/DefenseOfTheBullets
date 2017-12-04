package buff;

import javafx.scene.image.Image;

import entity.job.*;
import skill.*;

public class FrenzyBuff extends Buff implements Expirable{
	private static final Skill SKILL = new Frenzy();
	private static final Image IMAGE = new Image("resource/image/FrenzyIcon.png");
	private double reloadFactor;
	private double slow;
	int duration;
	int maxDuration;
	
	public FrenzyBuff(Novice player, int level, int duration) {
		super(player, BuffType.BUFF);
		this.reloadFactor = 1.5 + 0.1 * level;
		this.slow = 1.1 - 0.1 * level;
		this.maxDuration = duration;
		this.duration = duration;
		activateBuff();
	}
	
	public void drawEffect() {
		player.setReloadDone((int) (player.getReloadDone() / reloadFactor));
		player.setSpeed(player.getSpeed() - player.getSpeed() * this.slow);
		player.setDamageFactor(player.getDamageFactor() * 1.25);
	}
	
	public void undrawEffect() {
		player.setReloadDone((int) (player.getReloadDone() * reloadFactor));
		player.setDamageFactor(player.getDamageFactor() / 1.25);
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		--duration;
		
		if(duration <= 0) {
			isActive = false;
			undrawEffect();
		}
	}

	@Override
	public int getMaxDuration() {
		// TODO Auto-generated method stub
		return maxDuration;
	}

	@Override
	public int getDuration() {
		// TODO Auto-generated method stub
		return duration;
	}
	
	public Image getImage() {
		return IMAGE;
	}
	
	public Skill getSkill() {
		return SKILL;
	}
}
