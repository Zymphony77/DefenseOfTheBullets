package buff;

import entity.job.Novice;
import javafx.scene.image.Image;
import main.Main;
import skill.Skill;

public class BurnBuff extends Buff implements Expirable {
	private static final Image IMAGE = new Image("resource/image/FireOrbIcon.png");
	private static final int MAX_DURATION = 2 * Main.FRAME_RATE;
	
	private int duration;
	
	public BurnBuff(Novice caster) {
		super(caster, BuffType.DEBUFF);
		
		this.duration = MAX_DURATION;
	}
	
	public void drawEffect() {
		// NO EFFECT
	}
	
	public void undrawEffect() {
		// NO EFFECT
	}
	
	public void update() {
		--duration;
		
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
}
