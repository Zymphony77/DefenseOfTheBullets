package entity.job;

import entity.Entity;
import entity.property.Experience;
import entity.property.Side;
import skill.Burst;
import skill.Frenzy;
import skill.Haste;
import skill.Shield;
import utility.Pair;

public class Tank extends Novice{
	
	protected double HPShield;
	protected boolean isBurstBuff;

	public Tank(Pair refPoint, Side side) {
		super(refPoint, side);
		// TODO Auto-generated constructor stub
		skillList.add(new Shield());
		skillList.add(new Burst());
		isBurstBuff = false;
		HPShield = 0;
	}
	
	public Tank(Pair refPoint, Experience experience, Side side) {
		super(refPoint, experience, side);
		// TODO Auto-generated constructor stub
		skillList.add(new Shield());
		skillList.add(new Burst());
		isBurstBuff = false;
		HPShield = 0;
	}
	
	@Override
	protected void takeDamage(Entity entity, double damage) {
		// TODO Auto-generated method stub
		damage *= damageFactor;
		
		if(HPShield - damage > 1e-4) {
			HPShield -= damage;
			hpBar.draw();
		} else {
			HPShield = 0;
			super.takeDamage(entity, damage);
		}
	}

	public double getHPShield() {
		return HPShield;
	}

	public void setHPShield(double hPShield) {
		HPShield = hPShield;
	}
	
	public boolean isBurstBuff() {
		return isBurstBuff;
	}

	public void setBurstBuff(boolean isBurstBuff) {
		this.isBurstBuff = isBurstBuff;
	}

	@Override
	public String toString() {
		return "Tank";
	}
}
