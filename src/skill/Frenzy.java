package skill;

import javafx.scene.image.Image;

import buff.*;
import main.Main;

public class Frenzy extends ActiveSkill {
	private static final int DEFAULT_COOLDOWN = 15 * Main.FRAME_RATE;
	private static final int DEFAULT_DURATION = 4 * Main.FRAME_RATE;
	private static final int POSITION = 4;
	private static final int MAX_LEVEL = 10;
	private static final Image IMAGE = new Image("image/FrenzyIcon.png");
	
	public Frenzy() {
		super(POSITION, MAX_LEVEL, DEFAULT_COOLDOWN);
	}
	
	protected void drawEffect() {
		// ADD EFFECT
	}
	
	protected void activateEffect() {
		caster.addBuff(new FrenzyBuff(caster, level, DEFAULT_DURATION));
	}
	
	public Image getImage() {
		return IMAGE;
	}
	
	@Override
	public String toString() {
		return "Frenzy";
	}
}
