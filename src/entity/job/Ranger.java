package entity.job;

import java.util.ArrayList;
import java.util.Random;

import buff.Buff;
import buff.DouAttBuff;
import entity.Entity;
import entity.bullet.Bullet;
import entity.property.Experience;
import entity.property.HpBar;
import entity.property.Side;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import main.game.GameComponent;
import skill.DouAtt;
import skill.Frenzy;
import skill.Haste;
import skill.Skill;
import utility.Job;
import utility.Pair;

public class Ranger extends Novice{
	private static final Job JOB = Job.RANGER;
	
	protected int RatioDoubleAtt;
	protected Random rand = new Random();
	
	public Ranger(Pair refPoint, Side side) {
		super(refPoint, side);
		// TODO Auto-generated constructor stub
		RatioDoubleAtt = 0;
		skillList.add(new DouAtt());
		skillList.add(new Frenzy());
		
	}
	
	public Ranger(Pair refPoint, Experience experience, Side side) {
		super(refPoint, experience, side);
		// TODO Auto-generated constructor stub
		RatioDoubleAtt = 0;
		skillList.add(new DouAtt());
		skillList.add(new Frenzy());
	}
	
	public Ranger(Novice oldPlayer) {
		// TODO Auto-generated constructor stub
		super(oldPlayer.getRefPoint(), oldPlayer.getExperience(), oldPlayer.getSide());
		skillList = oldPlayer.getSkillList();
		buffList = oldPlayer.getBuffList();
		status = oldPlayer.getStatus();
		RatioDoubleAtt = 0;
		skillList.add(new DouAtt());
		skillList.add(new Frenzy());
		isMoving = oldPlayer.isMoving;
		isPlayer = oldPlayer.isPlayer;
		moveDirection = oldPlayer.moveDirection;
		reloadCount = oldPlayer.reloadCount;
	}
	
	//--------------UI-------------\\
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
		
		reloadCount = 0;
		
		if(rand.nextInt(100) < RatioDoubleAtt) {
			reloadCount = (int)(getReloadDone() * 0.90);
		}
	}
	
	@Override
	public void upgradeSkill(int position) {
		super.upgradeSkill(position);
		if(position == 3) {
			addBuff(new DouAttBuff(this, skillList.get(2).getLevel()));
		}
	}

	public int getRatioDoubleAtt() {
		return RatioDoubleAtt;
	}

	public void setRatioDoubleAtt(int ratioDoubleAtt) {
		RatioDoubleAtt = ratioDoubleAtt;
	}
	
	public Job getJob() {
		return JOB;
	}

	@Override
	 public String toString() {
		 return JOB.toString();
	 }
}
