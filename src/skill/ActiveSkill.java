package skill;

import entity.job.*;

public abstract class ActiveSkill extends Skill {
	protected int maxCooldown;
	protected int remainingCooldown;
	protected int maxDuration;
	protected int remainingDuration;
	
	public ActiveSkill(int position, int maxLevel, int maxCooldown, int maxDuration) {
		super(position, maxLevel);
		
		this.maxCooldown = maxCooldown;
		remainingCooldown = 0;
		
		this.maxDuration = maxDuration;
		remainingDuration = 0;
	}
	
	public void update() {
		remainingCooldown = Math.max(remainingCooldown - 1, 0);
	}
	
	public void activateSkill() {
		remainingDuration = maxDuration;
		remainingCooldown = maxCooldown;
		drawEffect();
		activateEffect();
	}
	
	public void reset() {
		remainingCooldown = 0;
		remainingDuration = 0;
	}
	
	@Override
	public boolean isReady() {
		return  (super.isReady() && remainingCooldown == 0);
	}
	
	public int getMaxCooldown() {
		return maxCooldown;
	}
	
	public int getRemainingCooldown() {
		return remainingCooldown;
	}
	
	public int getMaxDuration() {
		return maxDuration;
	}
	
	public int getRemainingDuration() {
		return remainingDuration;
	}
	
	protected void setMaxCooldown(int maxCooldown) {
		this.maxCooldown = maxCooldown;
	}
	
	protected void setMaxDuration(int maxDuration) {
		this.maxDuration = maxDuration;
	}
}
