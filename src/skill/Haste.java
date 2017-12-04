package skill;

import javafx.scene.image.Image;

import buff.*;
import entity.job.Novice;
import main.Main;

public class Haste extends ActiveSkill {
	private static final int DEFAULT_COOLDOWN = 5 * Main.FRAME_RATE;
	private static final double COOLDOWN_PER_LEVEL = 0.5;
	private static final int DEFAULT_DURATION = 2 * Main.FRAME_RATE;
	private static final int POSITION = 1;
	private static final int MAX_LEVEL = 5;
	private static final Image IMAGE = new Image("resource/image/HasteIcon.png");
	
	public Haste() {
		super(POSITION, MAX_LEVEL, DEFAULT_COOLDOWN);
	}
	
	@Override
	public void activateSkill() {
		setMaxCooldown(DEFAULT_COOLDOWN + (int) (COOLDOWN_PER_LEVEL * caster.getLevel() * Main.FRAME_RATE));
		super.activateSkill();
	}
	
	protected void drawEffect() {
		// ADD EFFECT
	}
	
	protected void activateEffect() {
		caster.addBuff(new HasteBuff(caster, (5 + level) * DEFAULT_DURATION / 10));
	}
	
	public Image getImage() {
		return IMAGE;
	}
	
	@Override
	public String toString() {
		return "Haste";
	}
}
