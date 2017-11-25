package buff;

import javafx.scene.image.Image;

import entity.job.*;
import skill.*;

public abstract class Buff {
	protected BuffType buffType;
	protected Novice player;
	protected boolean isActive;
	
	public Buff(Novice player, BuffType buffType) {
		this.player = player;
		this.buffType = buffType;
	}
	
	public void activateBuff() {
		isActive = true;
		drawEffect();
	}
	
	public void deactivateBuff() {
		isActive = false;
		undrawEffect();
	}
	
	public abstract void drawEffect();
	public abstract void undrawEffect();
	public abstract Image getImage();
	
	public Novice getPlayer() {
		return player;
	}
	
	public BuffType getBuffType() {
		return buffType;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public abstract Skill getSkill();
}
