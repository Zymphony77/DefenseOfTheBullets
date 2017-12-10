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
	
	protected double ratioDoubleAtt;
	protected int shootingState; 
	
//	public Ranger(Pair refPoint, Side side) {
//		super(refPoint, side);
//		// TODO Auto-generated constructor stub
//		ratioDoubleAtt = 0;
//		shootingState = 0;
//		skillList.add(new DouAtt());
//		skillList.add(new Frenzy());
//		
//	}
//	
//	public Ranger(Pair refPoint, Experience experience, Side side) {
//		super(refPoint, experience, side);
//		// TODO Auto-generated constructor stub
//		ratioDoubleAtt = 0;
//		skillList.add(new DouAtt());
//		skillList.add(new Frenzy());
//	}
	
	public Ranger(Novice oldPlayer) {
		// TODO Auto-generated constructor stub
//		super(oldPlayer.getRefPoint(), oldPlayer.getExperience(), oldPlayer.getSide());
//		skillList = oldPlayer.getSkillList();
//		buffList = oldPlayer.getBuffList();
//		status = oldPlayer.getStatus();
//		ratioDoubleAtt = 0;
//		skillList.add(new DouAtt());
//		skillList.add(new Frenzy());
//		isMoving = oldPlayer.isMoving;
//		isPlayer = oldPlayer.isPlayer;
//		moveDirection = oldPlayer.moveDirection;
//		reloadCount = oldPlayer.reloadCount;
		super(oldPlayer);
		
		shootingState = 0;
		ratioDoubleAtt = 0;
		skillList.add(new DouAtt());
		skillList.add(new Frenzy());
	}
	
	//--------------UI-------------\\
	public void draw() {
		canvas.setWidth(CANVAS_SIZE);
		canvas.setHeight(CANVAS_SIZE);
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		if(GameComponent.getInstance().getPlayer() != null) {
			changeCenter(GameComponent.getInstance().getPlayer().getRefPoint());
		}
		
		gc.setFill(Color.DARKGRAY);
		gc.fillRect(2*RADIUS, CANVAS_SIZE / 2 - 9, RADIUS, 8);
		gc.fillRect(2*RADIUS, CANVAS_SIZE / 2, RADIUS, 8);
		gc.setFill(Color.DARKSEAGREEN);
		gc.fillOval(10, 10, 2*RADIUS, 2*RADIUS);
		
		hpBar = new HpBar(this);
		GameComponent.getInstance().addComponent(hpBar);
	}
	
	@Override
	public void shoot() {
		if(reloadCount < reloadDone) {
			return;
		}
		
		double x = refPoint.first + Math.cos(Math.toRadians(direction) + (shootingState == 0? 1: -1) * 
				Math.atan2(4, RADIUS + 17))*(Math.sqrt(Math.pow((RADIUS + 17), 2) + Math.pow(4, 2)));
		double y = refPoint.second + Math.sin(Math.toRadians(direction) + (shootingState == 0? 1: -1) * 
				Math.atan2(4, RADIUS + 17))*(Math.sqrt(Math.pow((RADIUS + 17), 2) + Math.pow(4, 2)));
		
		double currentDamage = random.nextDouble() < criticalChance? bulletDamage * criticalDamage: bulletDamage;
		
		Bullet bullet = new Bullet(this, new Pair(x, y), bulletHP, direction, currentDamage, bulletSpeed, side);
		GameComponent.getInstance().addComponent(bullet);
		
		reloadCount = 0;
		shootingState = 1 - shootingState;
		
		if(random.nextDouble() * 100.0 < ratioDoubleAtt) {
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

	public double getRatioDoubleAtt() {
		return ratioDoubleAtt;
	}

	public void setRatioDoubleAtt(double ratioDoubleAtt) {
		this.ratioDoubleAtt = ratioDoubleAtt;
	}
	
	public Job getJob() {
		return JOB;
	}

	@Override
	 public String toString() {
		 return JOB.toString();
	 }
}
