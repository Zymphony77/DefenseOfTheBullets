package skill;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import main.Main;
import entity.job.*;
import environment.*;
import utility.*;

public abstract class Skill {
	protected Novice caster;
	protected Canvas effect;
	protected int position;
	protected int maxLevel;
	protected int level;
	
	public Skill(int position, int maxLevel) {
		this.position = position;
		
		this.maxLevel = maxLevel;
		this.level = 1;
	}
	
	protected abstract void drawEffect();
	protected abstract void activateEffect();
	protected abstract void undrawEffect();
	
	public boolean isReady() {
		return level > 0;
	}
	
	public void setCaster(Novice caster) {
		this.caster = caster;
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getPosition() {
		return position;
	}
	
	public abstract Image getImage();
}
