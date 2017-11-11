package bot;

import utility.Job;

public abstract class Bot {
	private int target;
	private boolean chkMove = false;
	private Job job;
	
	public abstract void move();
	public abstract void upgradeSkill();
	public abstract void upgradeStatus();
	public abstract void upgradeClass();
	public abstract void chooseTarget();
	
}
