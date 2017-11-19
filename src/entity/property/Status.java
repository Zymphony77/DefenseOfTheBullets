package entity.property;

public class Status {
	public static final int MAX_STATUS = 12;
	
	private int STR, VIT, DEX, INT, AGI, LUK;
	
	/// STR -> bulletDamage, attack
	/// VIT -> healtRegen, hp
	/// DEX -> bulletPenetration
	/// AGI -> bulletSpeed, reload, speed
	/// LUK -> criticalDamage, criticalChance
	
	public Status() {
		STR = VIT = DEX = INT = AGI = LUK = 1;
	}
	
	public void updateStatus(int status) {
		switch (status) {
			case 1:
				STR++; break;
			case 2:
				VIT++; break;
			case 3:
				DEX++; break;
			case 4:
				INT++; break;
			case 5:
				AGI++; break;
			case 6:
				LUK++; break;
		}
	}

	public int getSTR() {
		return STR;
	}

	public int getVIT() {
		return VIT;
	}

	public int getDEX() {
		return DEX;
	}

	public int getINT() {
		return INT;
	}

	public int getAGI() {
		return AGI;
	}

	public int getLUK() {
		return LUK;
	}
	
	
}
