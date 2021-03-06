package entity.job;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

import buff.*;
import main.*;
import main.game.GameComponent;
import entity.*;
import entity.bullet.*;
import entity.property.*;
import skill.*;
import utility.*;

public class Novice extends Entity implements Movable, Rotatable, Shootable {
	private static final double DEFAULT_MAX_HP = 5000;
	private static final double DEFAULT_ATTACK = 50;
	private static final double DEFAULT_SPEED = 100;
	private static final double DEFAULT_BULLET_DAMAGE = 75;
	private static final double DEFAULT_BULLET_SPEED = 200;
	private static final double DEFAULT_BULLET_HP = 250;
	private static final double DEFAULT_CRITICAL_CHANCE = 0.08;
	private static final double DEFAULT_CRITICAL_DAMAGE = 1.3;
	private static final int DEFAULT_RELOAD = 20;
	private static final Job JOB = Job.NOVICE;
	
	public static final int CANVAS_SIZE = 60;
	public static final int RADIUS = 20;
	
	protected ArrayList<Skill> skillList;
	protected ArrayList<Buff> buffList;
	
	protected double bulletDamage;
	protected double healthRegen;
	protected double bulletHP;
	protected double bulletSpeed;
	protected double speed;
	protected double damageFactor;
	protected int reloadDone;
	protected double criticalDamage;
	protected double criticalChance;
	protected Status status;
	
	protected String name;
	protected int kill;
	protected int death;
	
	protected boolean isMoving;
	protected boolean isPlayer;
	protected boolean isChangeJobRequested;
	protected double moveDirection;
	protected Job newJob;
	
	protected HpBar hpBar;
	protected Identity identity;
	protected Experience experience;
	protected int reloadCount;
	
	protected Random random;
	
	/* ------------------- Constructor ------------------- */
	public Novice(Pair refPoint, Side side, String name) {
		super(refPoint, DEFAULT_MAX_HP, 0, side);
		this.name = name;
		
		buffList = new ArrayList<Buff>();
		
		status = new Status();
		upgradeAbility();
		
		hp = maxHp;
		
		isMoving = false;
		isPlayer = true;
		isChangeJobRequested = false;
		moveDirection = 0;
		
		skillList = new ArrayList<Skill>();
		skillList.add(new Haste());
		skillList.add(new Heal());
		
		reloadDone = DEFAULT_RELOAD;
		reloadCount = DEFAULT_RELOAD;

		kill = 0;
		death = 0;

		experience = new Experience(1, 0);
		
		random = new Random();
	}
	
	public Novice(Pair refPoint, Experience experience, Side side, int kill, int death, String name) {
		super(refPoint, DEFAULT_MAX_HP, 0, side);
		this.experience = experience;
		this.kill = kill;
		this.death = death;
		this.name = name;
		
		buffList = new ArrayList<Buff>();
		
		status = new Status();
		upgradeAbility();
		
		hp = maxHp;
		
		isMoving = false;
		isPlayer = true;
		isChangeJobRequested = false;
		moveDirection = 0;
		
		skillList = new ArrayList<Skill>();
		skillList.add(new Haste());
		skillList.add(new Heal());
		
		reloadDone = DEFAULT_RELOAD;
		reloadCount = DEFAULT_RELOAD;
		
		random = new Random();
	}
	
	public Novice(Novice oldPlayer) {
		super(oldPlayer.getRefPoint(), oldPlayer.getMaxHp(), oldPlayer.getDirection(), oldPlayer.getSide());
		name = oldPlayer.name;
		experience = oldPlayer.getExperience();
		skillList = oldPlayer.getSkillList();
		buffList = oldPlayer.getBuffList();
		status = oldPlayer.getStatus();
		isMoving = oldPlayer.isMoving;
		isPlayer = oldPlayer.isPlayer;
		isChangeJobRequested = false;
		moveDirection = oldPlayer.moveDirection;
		reloadCount = oldPlayer.reloadCount;
		kill = oldPlayer.kill;
		death = oldPlayer.death;
		random = oldPlayer.random;
	}
	
	/* ------------------- UI ------------------- */
	public void draw() {
		canvas.setWidth(CANVAS_SIZE);
		canvas.setHeight(CANVAS_SIZE);
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		if(GameComponent.getInstance().getPlayer() != null) {
			changeCenter(GameComponent.getInstance().getPlayer().getRefPoint());
		}
		
		gc.setFill(Color.DARKGRAY);
		gc.fillRect(2*RADIUS, CANVAS_SIZE / 2 - 5, RADIUS, 10);
		gc.setFill(Color.GRAY);
		gc.fillOval(10, 10, 2*RADIUS, 2*RADIUS);
		
		hpBar = new HpBar(this);
		GameComponent.getInstance().addComponent(hpBar);
	}
	
	public void drawIdentity() {
		identity = new Identity(this);
		GameComponent.getInstance().addComponent(identity);
	}
	
	public void changeCenter(Pair center) {
		canvas.setTranslateX(refPoint.first - CANVAS_SIZE/2 - center.first + Main.SCREEN_SIZE / 2);
		canvas.setTranslateY(refPoint.second - CANVAS_SIZE/2 - center.second + Main.SCREEN_SIZE / 2);
	}
	
	public void rotate() {
		canvas.setRotate(direction);
	}
	
	/* ------------------- Game Function ------------------- */
	public void setMoving(double moveDirection) {
		this.moveDirection = moveDirection;
		this.isMoving = true;
	}
	
	public void stopMoving() {
		this.isMoving = false;
	}
	
	public void move() {
		if(!isMoving) {
			return;
		}
		
		refPoint.first += Math.cos(Math.toRadians(moveDirection)) * speed / Main.FRAME_RATE;
		refPoint.first = Math.min(refPoint.first, (double) GameComponent.MAX_SIZE);
		refPoint.first = Math.max(refPoint.first, (double) 0);
		
		refPoint.second += Math.sin(Math.toRadians(moveDirection)) * speed / Main.FRAME_RATE;
		refPoint.second = Math.min(refPoint.second, (double) GameComponent.MAX_SIZE);
		refPoint.second = Math.max(refPoint.second, (double) 0);
		
	}
	
	public void heal(double amount) {
		hp = Math.min(hp + amount, maxHp);
		hpBar.draw();
	}
	
	public void takeDamage(Entity entity) {
		takeDamage(entity, entity.getAttack());
	}
	
	public void takeDamage(Entity entity, double damage) {
		damage *= damageFactor;
		
		if(hp - damage > 1e-4) {
			hp -= damage;
			hpBar.draw();
		} else {
			hp = 0;
			die(entity);
			return;
		}
		
		if(entity instanceof IceBullet) {
			addBuff(new SlowBuff(this, ((IceBullet) entity).getSlowFactor()));
		} else if(entity instanceof FireBullet) {
			addBuff(new BurnBuff(this, ((FireBullet) entity).getBurnDamage(), (Magician) ((Bullet) entity).getShooter()));
		}
	}
	
	public void die(Entity killer) {
		super.die();
		hpBar.die();
		identity.die();
		++death;
		reloadCount = 0;
		
		Novice realKiller = null;
		
		if(killer instanceof Bullet) {
			if(((Bullet) killer).getShooter() instanceof Novice) {
				realKiller = (Novice) ((Bullet) killer).getShooter();
			}
		} else if(killer instanceof Novice) {
			realKiller = (Novice) killer;
		}
		
		if(realKiller != null) {
			realKiller.gainExp(experience.getGainedExperience() / 3);		// Adjust given EXP here
			realKiller.setKill(realKiller.getKill() + 1);
		}
		
		for(Buff buff: buffList) {
			buff.deactivateBuff();
		}
		
		GameComponent.getInstance().getPlayerPane().getChildren().remove(canvas);
	}
	
	public void shoot() {
		if(reloadCount < reloadDone) {
			return;
		}
		
		double x = refPoint.first + Math.cos(Math.toRadians(direction))*(RADIUS + 17);
		double y = refPoint.second + Math.sin(Math.toRadians(direction))*(RADIUS + 17);
		
		double currentDamage = random.nextDouble() < criticalChance? bulletDamage * criticalDamage: bulletDamage;
		
		Bullet bullet = new Bullet(this, new Pair(x, y), bulletHP, direction, currentDamage, bulletSpeed, side);
		GameComponent.getInstance().addComponent(bullet);
		
		reloadCount = 0;
	}
	
	public void reload() {
		reloadCount = Math.min(reloadCount + 1, reloadDone);
	}
	
	public void gainExp(double exp) {
		experience.addExp(exp);
		identity.draw();
	}
	
	/* ------------------- Skill ------------------- */
	public void useSkill(int position) {
		if(position > skillList.size() || position <= 0) {
			return;
		}
		
		Skill skill = skillList.get(position - 1);
		
		if(!(skill instanceof ActiveSkill)) {
			return;
		}
		
		if(!skill.isReady()) {
			return;
		}
		
		for(Buff buff: buffList) {
			if(buff.getSkill() != null && buff.getSkill().getClass() == skill.getClass()) {
				if(skill instanceof Deactivable) {
					((Deactivable) skill).deactivateEffect();
					removeBuff(buff);
				}
				return;
			}
		}
		
		((ActiveSkill) skill).activateSkill();
	}
	
	public void upgradeSkill(int position) {
		experience.decreaseSkillPoint();
		skillList.get(position - 1).upgrade();
	}
	
	/* ------------------- Buff ------------------- */
	public void addBuff(Buff buff) {
		for(Buff currentBuff: buffList) {
			if(currentBuff.getClass() == buff.getClass()) {
				removeBuff(currentBuff);
				break;
			}
		}
		
		buffList.add(buff);
		
		if(GameComponent.getInstance().getPlayer() == this) {
			if(buff.getBuffType() == BuffType.BUFF) {
				GameComponent.getInstance().getBuffPane().addBuff(buff);
			} else {
				GameComponent.getInstance().getDebuffPane().addBuff(buff);
			}
		}
	}
	
	public void removeBuff(Buff buff) {
		buff.deactivateBuff();
		buffList.remove(buff);
		
		if(GameComponent.getInstance().getPlayer() == this) {
			if(buff.getBuffType() == BuffType.BUFF) {
				GameComponent.getInstance().getBuffPane().removeBuff(buff);
			} else {
				GameComponent.getInstance().getDebuffPane().removeBuff(buff);
			}
		}
	}
	
	/* ------------------- Status ------------------- */
	public void upgradeAbility() {
		// Deactivate Buff
		for(Buff buff: buffList) {
			buff.deactivateBuff();
		}
		
		calculateAbility();
		
		// Re-activate Buff
		for(Buff buff: buffList) {
			buff.activateBuff();
		}
	}
	
	public void calculateAbility() {
		// Status
		bulletDamage = DEFAULT_BULLET_DAMAGE + 6 * status.getStatus(0);
		attack = DEFAULT_ATTACK + 8 * status.getStatus(0);
		maxHp = DEFAULT_MAX_HP + 500 * status.getStatus(1);
		healthRegen = maxHp / Main.FRAME_RATE / 60;
		damageFactor = 1 - 0.016 * status.getStatus(1);
		bulletHP = DEFAULT_BULLET_HP + 30 * status.getStatus(2);
		bulletSpeed = DEFAULT_BULLET_SPEED + 15 * status.getStatus(2);
		speed = DEFAULT_SPEED + 5 * status.getStatus(4);
		reloadDone = DEFAULT_RELOAD - (int)(0.6667*status.getStatus(4));
		criticalChance = DEFAULT_CRITICAL_CHANCE + 0.02 * status.getStatus(5);
		criticalDamage = DEFAULT_CRITICAL_DAMAGE + 0.1 * status.getStatus(5);
	}
	
	public void upgradeStatus(int status) {
		if(this.status.canUpgradeStatus(status) == false) {
			return;
		}
		if(experience.decreasePointStatus()) {
			this.status.updateStatus(status);
			upgradeAbility();
			if(status == 1) {
				heal(500.0);
			}
		}
	}
	/* ------------------- Change Job ------------------- */
	public void requestChangeJob(Job newJob) {
		if(newJob == null) {
			isChangeJobRequested = false;
			return;
		}
		
		this.newJob = newJob;
		isChangeJobRequested = true;
	}
	
	/* ------------------- Getters&Setters ------------------- */
	public Status getStatus() {
		return status;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public void setDamageFactor(double damageFactor) {
		this.damageFactor = damageFactor;
	}
	
	public void setReloadDone(int reloadDone) {
		this.reloadDone = reloadDone;
	}
	
	public void setPlayer(boolean isPlayer) {
		this.isPlayer = isPlayer;
	}
	
	public void setReloadCount() {
		this.reloadCount = reloadDone;
	}
	
	public void setBulletDamage(double bulletDamage) {
		this.bulletDamage = bulletDamage;
	}
	
	public void setHealthRegen(double healthRegen) {
		this.healthRegen = healthRegen;
	}
	
	public void setKill(int kill) {
		this.kill = kill;
	}
	
	public void setDeath(int death) {
		this.death = death;
	}

	public int getRadius() {
		return RADIUS;
	}
	
	public int getMaxRadius() {
		return getRadius();
	}
	
	public ArrayList<Skill> getSkillList() {
		return skillList;
	}
	
	public ArrayList<Buff> getBuffList() {
		return buffList;
	}
	
	public Experience getExperience() {
		return experience;
	}
	
	public int getLevel() {
		return experience.getLevel();
	}
	
	public double getHealthRegen() {
		return healthRegen;
	}

	public HpBar getHpBar() {
		return hpBar;
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public double getDamageFactor() {
		return damageFactor;
	}
	
	public int getReloadDone() {
		return reloadDone;
	}
	
	public Job getJob() {
		return JOB;
	}
	
	public Job getNewJob() {
		return newJob;
	}
	
	public boolean isPlayer() {
		return isPlayer;
	}
	
	public boolean isMoving() {
		return isMoving;
	}
	
	public boolean isChangeJobRequested() {
		return isChangeJobRequested;
	}
	
	public double getBulletDamage() {
		return bulletDamage;
	}
	
	public int getKill() {
		return kill;
	}
	
	public int getDeath() {
		return death;
	}
	
	public String getName() {
		return name;
	}
	
	public Identity getIdentity() {
		return identity;
	}

	@Override
	public String toString() {
		return JOB.toString();
	}
}
