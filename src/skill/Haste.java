package skill;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import main.Main;

public class Haste extends ActiveSkill {
	private static final int DEFAULT_COOLDOWN = 3 * Main.FRAME_RATE;
	private static final int DEFAULT_DURATION = 2 * Main.FRAME_RATE;
	private static final int POSITION = 1;
	private static final int MAX_LEVEL = 5;
	private static final Image IMAGE = new Image("resource/HasteIcon.png");
	
	public Haste() {
		super(POSITION, MAX_LEVEL, (int) DEFAULT_COOLDOWN, DEFAULT_DURATION);
	}
	
	protected void drawEffect() {
		// No effect
	}
	
	protected void undrawEffect() {
		// No effect
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
