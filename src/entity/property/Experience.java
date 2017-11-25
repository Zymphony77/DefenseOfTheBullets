package entity.property;

public class Experience {
	private static final int MAX_LEVEL = 50;
	
	private int level;
	private double currentExp;
	private int skillPoint;
	private int spentSkillPoint;
	private int pointStatus;
	
	public Experience(int level, double currentExp) {
		this.level = level;
		this.currentExp = currentExp;
		this.skillPoint = 1;
		this.spentSkillPoint = 0;
		this.pointStatus = 0;
		
		updateLevel();
	}
	
	public void updateLevel() {
		while(level < MAX_LEVEL && currentExp >= getMaxExp()) {
			currentExp -= getMaxExp();
			pointStatus++;
			++skillPoint;
			++level;
		}
	}
	
	public void addExp(double exp) {
		currentExp += exp;
		updateLevel();
		
		if(level == MAX_LEVEL) {
			currentExp = 0;
		}
	}
	
	public void reborn() {
		currentExp = getGainedExperience() / 2;
		level = 1;
		skillPoint = 1;
		spentSkillPoint = 0;
		pointStatus = 0;
		updateLevel();
	}
	
	public double getGainedExperience() {
		double exp = 0;
		
		for(int i = 1; i < level; ++i) {
			exp += Experience.getMaxExp(i);
		}
		
		exp += currentExp;
		
		return exp;
	}
	
	public int getLevel() {
		return level;
	}
	
	public double getCurrentExp() {
		return currentExp;
	}
	
	public double getMaxExp() {
		return Experience.getMaxExp(level);
	}
	
	private static double getMaxExp(int level) {
		return 258.08 * Math.pow(1.04, level) - 248.41;
	}
	
	public int getPointStatus() {
		return pointStatus;
	}
	
	public int getSkillPoint() {
		return skillPoint;
	}
	
	public boolean decreasePointStatus() {
		if(pointStatus > 0) {
			pointStatus--;
			return true;
		}
		return false;
	}
	
	public void decreaseSkillPoint() {
		--skillPoint;
		++spentSkillPoint;
	}
}
