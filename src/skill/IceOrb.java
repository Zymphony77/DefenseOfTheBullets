package skill;

import javafx.scene.image.Image;

import buff.*;
import main.Main;

public class IceOrb extends ActiveSkill implements Deactivable {
	private static final int POSITION = 4;
	private static final int MAX_LEVEL = 10;
	private static final Image IMAGE = new Image("resource/image/IceOrbIcon.png");
	
	public IceOrb() {
		super(POSITION, MAX_LEVEL, 0);
	}
	
	protected void drawEffect() {
		// ADD EFFECT
	}
	
	protected void activateEffect() {
		caster.addBuff(new IceOrbBuff(caster));
	}
	
	public void deactivateEffect() {
		caster.getBuffList().removeIf(buff -> buff instanceof IceOrbBuff);
	}
	
	public Image getImage() {
		return IMAGE;
	}
	
	@Override
	public String toString() {
		return "Ice Orb";
	}
}
