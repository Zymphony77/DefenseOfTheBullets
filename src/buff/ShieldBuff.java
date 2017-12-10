package buff;

import javafx.scene.image.Image;

import entity.job.*;
import skill.*;

public class ShieldBuff extends Buff implements Expirable{
	private static final Skill SKILL = new Shield();
	private static final Image IMAGE = new Image("image/ShieldIcon.png");
	private int duration;
	private int maxDuration;
	private int maxHPShield;
	
	public ShieldBuff(Novice player, int maxHPShield, int duration) {
		super(player, BuffType.BUFF);
		this.duration = duration;
		this.maxDuration = duration;
		this.maxHPShield = maxHPShield;
		activateBuff();
	}
	
	public void update() {
		--duration;
		
		if(duration <= 0) {
			deactivateBuff();
		}
	}
	
	public void drawEffect() {
		((Tank) player).setHPShield(maxHPShield);
	}
	
	public void undrawEffect() {
		((Tank) player).setHPShield(0);
	}
	
	public Image getImage() {
		return IMAGE;
	}
	
	public Skill getSkill() {
		return SKILL;
	}

	@Override
	public int getMaxDuration() {
		return maxDuration;
	}

	@Override
	public int getDuration() {
		return duration;
	}
}
