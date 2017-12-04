package skill;

import javafx.scene.image.Image;

import buff.*;
import main.Main;

public class FireOrb extends ActiveSkill implements Deactivable {
	private static final int POSITION = 3;
	private static final int MAX_LEVEL = 10;
	private static final Image IMAGE = new Image("resource/image/FireOrbIcon.png");
	
	public FireOrb() {
		super(POSITION, MAX_LEVEL, 0);
	}
	
	protected void drawEffect() {
		// ADD EFFECT
	}
	
	protected void activateEffect() {
		caster.addBuff(new FireOrbBuff(caster, 100));
	}
	
	public void deactivateEffect() {
		caster.getBuffList().removeIf(buff -> buff instanceof FireOrbBuff);
	}
	
	public Image getImage() {
		return IMAGE;
	}
	
	@Override
	public String toString() {
		return "Fire Orb";
	}
}
