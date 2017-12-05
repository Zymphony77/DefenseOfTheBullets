package skill;

import javafx.scene.image.Image;

import buff.*;
import entity.job.Novice;
import main.Main;

public class Burst extends ActiveSkill {
	private static final int DEFAULT_COOLDOWN = 40 * Main.FRAME_RATE;
	private static final double COOLDOWN_PER_LEVEL = 4;
	private static final int DEFAULT_DURATION = 15;
	private static final int POSITION = 4;
	private static final int MAX_LEVEL = 10;
	private static final Image IMAGE = new Image("image/BurstIcon.png");
	
	public Burst() {
		super(POSITION, MAX_LEVEL, DEFAULT_COOLDOWN);
	}
	
	@Override
	public void activateSkill() {
		setMaxCooldown(DEFAULT_COOLDOWN - (int) (COOLDOWN_PER_LEVEL * level * Main.FRAME_RATE));
		super.activateSkill();
	}
	
	protected void drawEffect() {
		// ADD EFFECT
	}
	
	protected void activateEffect() {
		caster.addBuff(new BurstBuff(caster, DEFAULT_DURATION));
	}
	
	public Image getImage() {
		return IMAGE;
	}
	
	@Override
	public String toString() {
		return "Burst";
	}
}
