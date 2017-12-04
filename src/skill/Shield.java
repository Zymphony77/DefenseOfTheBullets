package skill;

import javafx.scene.image.Image;

import buff.*;
import main.Main;

public class Shield extends ActiveSkill {
	private static final int DEFAULT_COOLDOWN = 30 * Main.FRAME_RATE;
	private static final int DEFAULT_DURATION = 15 * Main.FRAME_RATE;
	private static final int POSITION = 3;
	private static final int MAX_LEVEL = 10;
	private static final Image IMAGE = new Image("resource/image/FrenzyIcon.png");
	
	public Shield() {
		super(POSITION, MAX_LEVEL, DEFAULT_COOLDOWN, DEFAULT_DURATION);
	}
	
	protected void drawEffect() {
		// ADD EFFECT
	}
	
	protected void activateEffect() {
		caster.addBuff(new ShieldBuff(caster, 500 + (200 * level), DEFAULT_DURATION));
	}
	
	public Image getImage() {
		return IMAGE;
	}
	
	@Override
	public String toString() {
		return "Shield";
	}
}
