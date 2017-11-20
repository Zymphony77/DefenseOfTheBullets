package skill;

import javafx.scene.image.Image;

import main.Main;

public class Frenzy extends ActiveSkill {
	private static final int DEFAULT_COOLDOWN = 30 * Main.FRAME_RATE;
	private static final int DEFAULT_DURATION = (int) 2e9;
	private static final int POSITION = 2;
	private static final int MAX_LEVEL = 5;
	private static final Image IMAGE = new Image("resource/image/FrenzyIcon.png");
	
	public Frenzy() {
		super(POSITION, MAX_LEVEL, DEFAULT_COOLDOWN, DEFAULT_DURATION);
	}
	
	protected void drawEffect() {
		// ADD EFFECT
	}
	
	protected void undrawEffect() {
		// REMOVE EFFECT
	}
	
	protected void activateEffect() {
		caster.setReloadDone((int) (caster.getReloadDone() / 1.5)); 
	}
	
	@Override
	public void deactivateSkill() {
		super.deactivateSkill();
	}
	
	protected void deactivateEffect() {
		caster.setReloadDone((int) (caster.getReloadDone() * 1.5));
		// caster.updateStatus();
	}
	
	public Image getImage() {
		return IMAGE;
	}
	
	@Override
	public String toString() {
		return "Frenzy";
	}
}
