package skill;

import javafx.scene.image.Image;

import buff.*;
import entity.job.Novice;
import main.Main;

public class Heal extends ActiveSkill {
	private static final int DEFAULT_COOLDOWN = 45 * Main.FRAME_RATE;
	private static final int DEFAULT_DURATION = 10 * Main.FRAME_RATE;
	private static final int POSITION = 2;
	private static final int MAX_LEVEL = 5;
	private static final Image IMAGE = new Image("image/HealIcon.png");
	
	public Heal() {
		super(POSITION, MAX_LEVEL, DEFAULT_COOLDOWN);
	}
	
	@Override
	public void activateSkill() {
		super.activateSkill();
	}
	
	protected void drawEffect() {
		// ADD EFFECT
	}
	
	protected void activateEffect() {
		caster.addBuff(new HealBuff(caster, level, DEFAULT_DURATION));
	}
	
	public Image getImage() {
		return IMAGE;
	}
	
	@Override
	public String toString() {
		return "Heal";
	}
}
