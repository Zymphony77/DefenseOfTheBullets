package buff;

import javafx.scene.image.Image;

import entity.job.*;
import skill.*;

public class IceOrbBuff extends Buff {
	private static final Skill SKILL = new IceOrb();
	private static final Image IMAGE = new Image("image/IceOrbIcon.png");
	
	public IceOrbBuff(Novice caster) {
		super(caster, BuffType.BUFF);
		
		activateBuff();
	}
	
	public void drawEffect() {
		// NO EFFECT
	}
	
	public void undrawEffect() {
		// NO EFFECT
	}
	
	public Image getImage() {
		return IMAGE;
	}
	
	public Skill getSkill() {
		return SKILL;
	}
}
