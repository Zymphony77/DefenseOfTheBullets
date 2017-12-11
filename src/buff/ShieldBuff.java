package buff;

import javafx.scene.image.Image;

import entity.job.*;
import skill.*;

public class ShieldBuff extends Buff implements Expirable{
	private static final Skill SKILL = new Shield();
	private static final Image IMAGE = new Image("image/ShieldIcon.png");
	private int duration;
	private int maxDuration;
	private double HPShield;
	
	public ShieldBuff(Novice player, double HPShield, int duration) {
		super(player, BuffType.BUFF);
		this.duration = duration;
		this.maxDuration = duration;
		this.HPShield = HPShield;
		activateBuff();
	}
	
	public void update() {
		--duration;
		
		if(duration <= 0) {
			deactivateBuff();
		}
	}
	
	public void drawEffect() {
		((Tank) player).setHPShield(HPShield);
	}
	
	public void undrawEffect() {
		HPShield = ((Tank) player).getHPShield();
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
