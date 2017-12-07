package entity.job;

import java.util.ArrayList;
import java.util.Random;

import buff.Buff;
import entity.Entity;
import entity.bullet.Bullet;
import entity.property.Experience;
import entity.property.HpBar;
import entity.property.Side;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.Main;
import main.game.GameComponent;
import skill.Burst;
import skill.Frenzy;
import skill.Haste;
import skill.Shield;
import utility.Job;
import utility.Pair;

public class Tank extends Novice{
	private static final Job JOB = Job.TANK;
	
	protected double HPShield;
	protected boolean isBurstBuff;

//	public Tank(Pair refPoint, Side side) {
//		super(refPoint, side);
//		// TODO Auto-generated constructor stub
//		skillList.add(new Shield());
//		skillList.add(new Burst());
//		isBurstBuff = false;
//		HPShield = 0;
//	}
//	
//	public Tank(Pair refPoint, Experience experience, Side side) {
//		super(refPoint, experience, side);
//		// TODO Auto-generated constructor stub
//		skillList.add(new Shield());
//		skillList.add(new Burst());
//		isBurstBuff = false;
//		HPShield = 0;
//	}
	
	public Tank(Novice oldPlayer) {
		// TODO Auto-generated constructor stub
//		super(oldPlayer.getRefPoint(), oldPlayer.getExperience(), oldPlayer.getSide());
//		skillList = oldPlayer.getSkillList();
//		buffList = oldPlayer.getBuffList();
//		status = oldPlayer.getStatus();
//		skillList.add(new Shield());
//		skillList.add(new Burst());
//		isMoving = oldPlayer.isMoving;
//		isPlayer = oldPlayer.isPlayer;
//		moveDirection = oldPlayer.moveDirection;
//		reloadCount = oldPlayer.reloadCount;
		super(oldPlayer);
		isBurstBuff = false;
		HPShield = 0;
		skillList.add(new Shield());
		skillList.add(new Burst());
	}
	
	//--------------UI-------------\\
	@Override
	public void draw() {
		canvas.setWidth(CANVAS_SIZE);
		canvas.setHeight(CANVAS_SIZE);
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		if(GameComponent.getInstance().getPlayer() != null) {
			changeCenter(GameComponent.getInstance().getPlayer().getRefPoint());
		}
		
		gc.setFill(Color.DARKGRAY);
		gc.fillRect(0, 25, 3*RADIUS, 10);
		gc.setFill(Color.GRAY);
		gc.fillOval(10, 10, 2*RADIUS, 2*RADIUS);
		
		GameComponent.getInstance().removeComponent(hpBar);
		hpBar = new HpBar(this);
		GameComponent.getInstance().addComponent(hpBar);
	}
	
	@Override
	public void shoot() {
		if(reloadCount < reloadDone) {
			return;
		}
		
		double x = refPoint.first + Math.cos(Math.toRadians(direction))*(RADIUS + 17);
		double y = refPoint.second + Math.sin(Math.toRadians(direction))*(RADIUS + 17);
		
		double currentDamage = (new Random()).nextDouble() < criticalChance? bulletDamage * criticalDamage: bulletDamage;
		
		Bullet bullet = new Bullet(this, new Pair(x, y), bulletHP, direction, currentDamage, bulletSpeed, side);
		GameComponent.getInstance().addComponent(bullet);
		
		x = refPoint.first - Math.cos(Math.toRadians(direction))*(RADIUS + 17);
		y = refPoint.second - Math.sin(Math.toRadians(direction))*(RADIUS + 17);
		
		currentDamage = (new Random()).nextDouble() < criticalChance? bulletDamage * criticalDamage: bulletDamage;
		bullet = new Bullet(this, new Pair(x, y), bulletHP, direction + 180, currentDamage, bulletSpeed, side);
		GameComponent.getInstance().addComponent(bullet);
		
		reloadCount = 0;
	}

	@Override
	public void takeDamage(Entity entity, double damage) {
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
	
	@Override
	public void upgradeAbility() {
		super.upgradeAbility();
		maxHp = 8000 + 750 * status.getStatus(1);
		healthRegen = maxHp / Main.FRAME_RATE / 50;
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
	
	public Job getJob() {
		return JOB;
	}

	@Override
	 public String toString() {
		 return JOB.toString();
	 }
}
