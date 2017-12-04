package entity.property;

public class Status {
	public static final int MAX_STATUS = 15;
	
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
	
	public static String getName(int status) {
		switch(status) {
		case 0:
			return "STR";
		case 1:
			return "VIT";
		case 2:
			return "DEX";
		case 3:
			return "INT";
		case 4:
			return "AGI";
		case 5:
			return "LUK";
		}
		return "";
	}
}
