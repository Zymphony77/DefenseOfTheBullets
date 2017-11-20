package entity.property;

public class Status {
	public static final int MAX_STATUS = 12;
	
	private int[] stat = new int[6];
	
	/// STR = 0, VIT = 1, DEX = 2, INT = 3, AGI = 4, LUK = 5
	
	/// STR -> bulletDamage, attack
	/// VIT -> healtRegen, hp
	/// DEX -> bulletPenetration
	/// AGI -> bulletSpeed, reload, speed
	/// LUK -> criticalDamage, criticalChance
	
	public Status() {
		for(int i = 0; i< 6; i++) {
			stat[i] = 1;
		}
	}
	
	public void updateStatus(int status) {
		if(stat[status] < MAX_STATUS) {
			stat[status]++;
		}
	}
	
	public int getStatus(int status) {
		if(status < 0 || status > 6)
			return 0;
		return stat[status];
	}
	
	public boolean canUpgradeStatus(int status) {
		if(status < 0 || status > 6)
			return false;
		if(stat[status] < MAX_STATUS)
			return true;
		return false;
	}
}
