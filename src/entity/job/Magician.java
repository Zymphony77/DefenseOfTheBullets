package entity.job;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

import buff.*;
import entity.bullet.*;
import entity.property.*;
import main.game.GameComponent;
import skill.*;
import utility.Job;
import utility.Pair;

public class Magician extends Novice {
	private static final Job JOB = Job.MAGICIAN;
	
	public Magician(Novice oldPlayer) {
//		super(oldPlayer.getRefPoint(), oldPlayer.getExperience(), oldPlayer.getSide());
//		skillList = oldPlayer.getSkillList();
//		buffList = oldPlayer.getBuffList();
//		status = oldPlayer.getStatus();
//		skillList.add(new FireOrb());
//		skillList.add(new IceOrb());
//		isMoving = oldPlayer.isMoving;
//		isPlayer = oldPlayer.isPlayer;
//		moveDirection = oldPlayer.moveDirection;
//		reloadCount = oldPlayer.reloadCount;
		super(oldPlayer);
		skillList.add(new FireOrb());
		skillList.add(new IceOrb());
	}
	
	@Override
	public void draw() {
		canvas.setWidth(CANVAS_SIZE);
		canvas.setHeight(CANVAS_SIZE);
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		if(GameComponent.getInstance().getPlayer() != null) {
			changeCenter(GameComponent.getInstance().getPlayer().getRefPoint());
		}
		
		gc.setFill(Color.DARKGRAY);
		gc.fillRect(2*RADIUS, 25, RADIUS, 10);
		gc.setFill(Color.MEDIUMPURPLE);
		gc.fillOval(10, 10, 2*RADIUS, 2*RADIUS);
		
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
		
		double currentDamage = random.nextDouble() < criticalChance? bulletDamage * criticalDamage: bulletDamage;
		
		Bullet bullet = null;
		
		for(Buff buff: buffList) {
			if(buff instanceof FireOrbBuff) {
				bullet = new FireBullet(this, new Pair(x, y), bulletHP, direction, currentDamage, bulletSpeed, side, 
						((FireOrbBuff) buff).getBurnDamage());
				break;
			}
			
			if(buff instanceof IceOrbBuff) {
				bullet = new IceBullet(this, new Pair(x, y), bulletHP, direction, currentDamage, bulletSpeed, 
						side, ((IceOrbBuff) buff).getSlowFactor());
				break;
			}
		}
		
		if(bullet == null) {
			bullet = new Bullet(this, new Pair(x, y), bulletHP, direction, currentDamage, bulletSpeed, side);
		} else {
			bullet.setAttack(bullet.getAttack() * (1.25 + 0.05 * status.getStatus(4)));
		}
		
		GameComponent.getInstance().addComponent(bullet);
		
		reloadCount = 0;
	}
	
	 @Override
	 public void useSkill(int position) {
		 if(position == 3 && skillList.get(2).isReady()) {
			 for(Buff buff: buffList) {
				 if(buff instanceof IceOrbBuff) {
					 removeBuff(buff);
					 break;
				 }
			 }
		 } else if(position == 4 && skillList.get(3).isReady()) {
			 for(Buff buff: buffList) {
				 if(buff instanceof FireOrbBuff) {
					 removeBuff(buff);
					 break;
				 }
			 }
		 }
		 
		 super.useSkill(position);
	 }
	 
	 public Job getJob() {
			return JOB;
		}
	 
	 @Override
	 public String toString() {
		 return JOB.toString();
	 }
}
