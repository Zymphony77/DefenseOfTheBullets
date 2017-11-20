package skill;

import javafx.scene.image.Image;

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
		super(POSITION, MAX_LEVEL, DEFAULT_COOLDOWN, DEFAULT_DURATION);
	}
	
	protected void drawEffect() {
		// No effect
	}
	
	protected void undrawEffect() {
		// No effect
	}
	
	@Override
	public void activateSkill(Novice caster) {
		setCaster(caster);
		setMaxCooldown(DEFAULT_COOLDOWN + (int) (COOLDOWN_PER_LEVEL * caster.getLevel() * Main.FRAME_RATE));
		super.activateSkill(caster);
	}
	
	protected void activateEffect() {
		caster.setSpeed(caster.getSpeed() * 1.5);
	}
	
	protected void deactivateEffect() {
		caster.setSpeed(caster.getSpeed() / 1.5);
		// caster.updateStatus();
	}
	
	public Image getImage() {
		return IMAGE;
	}
	
	@Override
	public String toString() {
		return "Haste";
	}
}
