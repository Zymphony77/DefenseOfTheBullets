package skill;

import javafx.scene.image.Image;

import buff.*;
import main.Main;

public class DouAtt extends Skill {
	private static final int POSITION = 3;
	private static final int MAX_LEVEL = 10;
	private static final Image IMAGE = new Image("image/DouAttIcon.png");
	
	public DouAtt() {
		super(POSITION, MAX_LEVEL);
	}
	
	protected void drawEffect() {
		// ADD EFFECT
	}
	
	protected void activateEffect() {
		caster.addBuff(new DouAttBuff(caster, level));
	}
	
	public Image getImage() {
		return IMAGE;
	}
	
	@Override
	public String toString() {
		return "DouAtt";
	}
}
