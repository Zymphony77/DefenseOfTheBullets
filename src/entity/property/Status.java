package entity.property;

import entity.job.Novice;

public class Status {
	private static final int MAX_STATUS = 12;
	
	private int STR, VIT, DEX, INT, AGI, LUK;
	private int minAttack, maxAttack, criticalDamage, criticalChance, healthRegen, 
		maxHealth, bodyDamage, bulletSpeed, bulletPenetration, reload, movementSpeed;
	
	/// STR -> minAttack, maxAttack, bodyDamage
	/// VIT -> healtRegen, maxHealth
	/// DEX -> bulletPenetration
	/// INT -> nothing
	/// AGI -> bulletSpeed, reload, movementSpeed
	/// LUK -> criticalDamage, criticalChance
	
	public Status(Novice player) {
		STR = VIT = DEX = INT = AGI = LUK = 1;
		updateStatus();
		update(player);
	}
	
	public void update(Novice player) {
		//change any property in this *_*
	}
	
	public void updateStatus() {
		
	}
}
