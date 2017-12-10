package buff;

import entity.job.Novice;
import javafx.scene.image.Image;
import main.Main;
import skill.Haste;
import skill.Skill;

public class SlowBuff extends Buff implements Expirable {
	private static final Image IMAGE = new Image("image/IceOrbIcon.png");
	private static final int MAX_DURATION = 1 * Main.FRAME_RATE;
	
	private int duration;
	private double slowFactor;
	
	public SlowBuff(Novice player, double slowFactor) {
		super(player, BuffType.DEBUFF);
		
		this.duration = MAX_DURATION;
		this.slowFactor = slowFactor;
		
		activateBuff();
	}
	
	public void update() {
		--duration;
		
		if(duration <= 0) {
			deactivateBuff();
		}
	}
	
	public void drawEffect() {
		player.setSpeed(player.getSpeed() * slowFactor);
	}
	
	public void undrawEffect() {
		player.setSpeed(player.getSpeed() / slowFactor);
	}
	
	public int getDuration() {
		return duration;
	}
	
	public int getMaxDuration() {
		return MAX_DURATION;
	}
	
	public Image getImage() {
		return IMAGE;
	}
	
	public Skill getSkill() {
		return null;
	}
}
