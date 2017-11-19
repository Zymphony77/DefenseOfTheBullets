package skill;

import entity.job.*;

public abstract class ActiveSkill extends Skill {
	protected int maxCooldown;
	protected int remainingCooldown;
	protected int maxDuration;
	protected int remainingDuration;
	protected boolean isActive;
	
	public ActiveSkill(int position, int maxLevel, int maxCooldown, int maxDuration) {
		super(position, maxLevel);
		
		this.maxCooldown = maxCooldown;
		remainingCooldown = 0;
		
		this.maxDuration = maxDuration;
		remainingDuration = 0;
		
		isActive = false;
	}
	
	public void update() {
		remainingCooldown = Math.max(remainingCooldown - 1, 0);
		drawCooldown();
		
		// drawDuration();
		
		if(isActive) {
			--remainingDuration;
			if(remainingDuration == 0) {
				deactivateSkill();
			}
		}
	}
	
	public void drawCooldown() {
		
	}
	
	public void activateSkill(Novice caster) {
		setCaster(caster);
		
		remainingDuration = maxDuration;
		remainingCooldown = maxCooldown;
		isActive = true;
		drawEffect();
		activateEffect();
	}
	
	protected void deactivateSkill() {
		remainingDuration = 0;
		isActive = false;
		undrawEffect();
		deactivateEffect();
	}
	
	protected abstract void deactivateEffect();
	
	public void reset() {
		remainingCooldown = 0;
		remainingDuration = 0;
		isActive = false;
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
	
	public boolean isActive() {
		return isActive;
	}
	
	protected void setMaxCooldown(int maxCooldown) {
		this.maxCooldown = maxCooldown;
	}
	
	protected void setMaxDuration(int maxDuration) {
		this.maxDuration = maxDuration;
	}
}
