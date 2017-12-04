package skill;

import javafx.scene.image.Image;

import buff.*;
import main.Main;

public class Frenzy extends ActiveSkill {
	private static final int DEFAULT_COOLDOWN = 30 * Main.FRAME_RATE;
	private static final int POSITION = 2;
	private static final int MAX_LEVEL = 5;
	private static final Image IMAGE = new Image("resource/image/FrenzyIcon.png");
	
	public Frenzy() {
		super(POSITION, MAX_LEVEL, DEFAULT_COOLDOWN);
	}
	
	protected void drawEffect() {
		// ADD EFFECT
	}
	
	protected void activateEffect() {
		caster.addBuff(new FrenzyBuff(caster, 1.5 + (0.1 * level)));
	}
	
	public Image getImage() {
		return IMAGE;
	}
	
	@Override
	public String toString() {
		return "Frenzy";
	}
}
