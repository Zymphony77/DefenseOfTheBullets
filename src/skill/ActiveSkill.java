package skill;

import entity.job.*;

public abstract class ActiveSkill extends Skill {
	protected int maxCooldown;
	protected int remainingCooldown;
	
	public ActiveSkill(int position, int maxLevel, int maxCooldown) {
		super(position, maxLevel);
		
		this.maxCooldown = maxCooldown;
		remainingCooldown = 0;
	}
	
	public void update() {
		remainingCooldown = Math.max(remainingCooldown - 1, 0);
	}
	
	public void activateSkill() {
		remainingCooldown = maxCooldown;
		drawEffect();
		activateEffect();
	}
	
	public void reset() {
		remainingCooldown = 0;
	}
	
	@Override
	public boolean isReady() {
		return  (super.isReady() && remainingCooldown <= 0);
	}
	
	public int getMaxCooldown() {
		return maxCooldown;
	}
	
	public int getRemainingCooldown() {
		return remainingCooldown;
	}
	
	protected void setMaxCooldown(int maxCooldown) {
		this.maxCooldown = maxCooldown;
	}
}
