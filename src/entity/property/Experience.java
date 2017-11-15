package entity.property;

public class Experience {
	private static final int MAX_LEVEL = 50;
	
	private int level;
	private double currentExp;
	
	public Experience(int level, double currentExp) {
		this.level = level;
		this.currentExp = currentExp;
		
		updateLevel();
	}
	
	public void updateLevel() {
		while(level < MAX_LEVEL && currentExp >= getMaxExp()) {
			currentExp -= getMaxExp();
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
		return 258.08 * Math.pow(1.04, level) - 248.40;
	}
}
