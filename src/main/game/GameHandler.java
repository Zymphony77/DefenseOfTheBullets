package main.game;

import javafx.scene.input.MouseEvent;
import main.Main;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.canvas.Canvas;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.HashMap;
import java.util.function.Predicate;

import bot.*;
import buff.*;
import entity.*;
import entity.bullet.*;
import entity.food.*;
import entity.job.*;
import entity.property.*;
import entity.tower.*;
import environment.*;
import skill.*;
import utility.*;

public class GameHandler {
	private static final int SPAWN_TIME = 5 * Main.FRAME_RATE;
	
	private static HashSet<KeyCode> activeKey = new HashSet<KeyCode>();
	private static HashSet<MouseButton> activeMouse = new HashSet<MouseButton>();
	private static HashMap<Novice, Integer> deadPlayer = new HashMap<Novice, Integer>();
	private static boolean autoshoot = false;
	private static boolean skillUpgradable = false;
	private static boolean statusUpgradable = false;
	
	public static void keyPressed(KeyEvent event) {
		if(event.getCode() == KeyCode.SPACE) {
			activeKey.add(KeyCode.SPACE);
		}
		
		if(event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W) {
			activeKey.add(KeyCode.UP);
		}
		
		if(event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
			activeKey.add(KeyCode.DOWN);
		}
		
		if(event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
			activeKey.add(KeyCode.LEFT);
		}
		
		if(event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
			activeKey.add(KeyCode.RIGHT);
		}
		
		if(event.getCode().isDigitKey()) {
			String keyName = event.getCode().toString();
			int skillNumber = Integer.parseInt(keyName.substring(keyName.length() - 1));
			if(skillNumber <= GameComponent.getInstance().getPlayer().getSkillList().size()) {
				GameComponent.getInstance().getPlayer().useSkill(skillNumber);
			}
		}
		
		if(event.getCode() == KeyCode.E && !activeKey.contains(KeyCode.E)) {
			activeKey.add(KeyCode.E);
			autoshoot = !autoshoot;
		}
		
		// CHEAT -- FOR TESTING PURPOSES
		
		// OVER MAX STATUS
		if(event.getCode() == KeyCode.EQUALS) {
			for(int i = 0; i < 6; ++i) {
				for(int j = 0; j < 15; ++j) {
					GameComponent.getInstance().getPlayer().getStatus().updateStatus(i);
				}
			}
			
			for(int i = 0; i < GameComponent.getInstance().getPlayer().getSkillList().size(); ++i) {
				for(int j = 0; j < 10000; ++j) {
					GameComponent.getInstance().getPlayer().upgradeSkill(i + 1);
				}
			}
			
			for(StatusIcon icon: GameComponent.getInstance().getStatusPane().getIconList()) {
				icon.undrawUpgrade();
			}
			
			for(SkillIcon icon: GameComponent.getInstance().getSkillPane().getIconList()) {
				icon.undrawUpgrade();
			}
		}
		
		// INSTANT WIN
		if(event.getCode() == KeyCode.OPEN_BRACKET) {
			for(Tower tower: GameComponent.getInstance().getTowerList()) {
				tower.setSide(GameComponent.getInstance().getPlayer().getSide());
			}
		}
		
		// INSTANT LOSE
		if(event.getCode() == KeyCode.CLOSE_BRACKET) {
			for(Tower tower: GameComponent.getInstance().getTowerList()) {
				if(GameComponent.getInstance().getPlayer().getSide() == Side.RED) {
					tower.setSide(Side.BLUE);
				} else {
					tower.setSide(Side.RED);
				}
			}
		}
	}
	
	public static void keyReleased(KeyEvent event) {
		if(event.getCode() == KeyCode.SPACE) {
			activeKey.remove(KeyCode.SPACE);
		}
		
		if(event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W) {
			activeKey.remove(KeyCode.UP);
		}
		
		if(event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
			activeKey.remove(KeyCode.DOWN);
		}
		
		if(event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
			activeKey.remove(KeyCode.LEFT);
		}
		
		if(event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
			activeKey.remove(KeyCode.RIGHT);
		}
		
		if(event.getCode() == KeyCode.E ) {
			activeKey.remove(KeyCode.E);
		}
	}
	
	public static void mousePressed(MouseEvent event) {
		if(event.getButton() == MouseButton.PRIMARY) {
			activeMouse.add(MouseButton.PRIMARY);
		}
	}
	
	public static void mouseReleased(MouseEvent event) {
		if(event.getButton() == MouseButton.PRIMARY) {
			activeMouse.remove(MouseButton.PRIMARY);
		}
	}
	
	public static void changeDirection(MouseEvent event) {
		double x = event.getSceneX() - Main.SCREEN_SIZE / 2;
		double y = event.getSceneY() - Main.SCREEN_SIZE / 2;
		
		GameComponent.getInstance().getPlayer().setDirection(Math.toDegrees(Math.atan2(y, x)));
		GameComponent.getInstance().getPlayer().rotate();
	}
	
	public static void update() {
		if(activeKey.contains(KeyCode.SPACE) || activeMouse.contains(MouseButton.PRIMARY) || autoshoot) {
			GameComponent.getInstance().getPlayer().shoot();
		}
		
		updateDeadPlayer();
		checkPlayerUpgrade();
		updateReloadAndSkill();
		moveComponent();
		movePlayer();
		moveCenter(GameComponent.getInstance().getPlayer().getRefPoint());
		checkCollision();
		clearDeadComponent();
		
		GameComponent.getInstance().generateFood();
	}
	
	private static void updateDeadPlayer() {
		for(Novice player: deadPlayer.keySet()) {
			if(deadPlayer.get(player).intValue() >= SPAWN_TIME - 1) {
				player.getExperience().reborn();
				Novice newPlayer = new Novice(GameComponent.spawnPoint(player.getSide()), player.getExperience(), player.getSide());
				
				if(player.isPlayer()) {
					reset();
					
					GameComponent.getInstance().getSkillPane().clear();
					GameComponent.getInstance().getStatusPane().clear();
					
					GameComponent.getInstance().setPlayer(newPlayer);
					GameComponent.getInstance().getStatusPane().setPlayer(newPlayer);
					GameComponent.getInstance().getSkillPane().setPlayer(newPlayer);
					GameComponent.getInstance().getExperienceBar().setExperience(newPlayer.getExperience());
				} else {
					GameComponent.getInstance().getBotList().add(new BotNovice(newPlayer));
					newPlayer.setPlayer(false);
				}
				
				newPlayer.addBuff(new InvincibleBuff(newPlayer));
				GameComponent.getInstance().addComponent(newPlayer);
			}
			
			deadPlayer.put(player, new Integer(deadPlayer.get(player).intValue() + 1));
		}
		
		deadPlayer.keySet().removeIf(player -> deadPlayer.get(player).intValue() >= SPAWN_TIME);
	}
	
	private static void checkPlayerUpgrade() {
		// Status
		if(GameComponent.getInstance().getPlayer().getExperience().getPointStatus() > 0 && !statusUpgradable) {
			statusUpgradable = true;
			for(StatusIcon icon: GameComponent.getInstance().getStatusPane().getIconList()) {
				if(icon.isUpgradable()) {
					icon.drawUpgrade();
				}
			}
		} else if(GameComponent.getInstance().getPlayer().getExperience().getPointStatus() == 0 && statusUpgradable) {
			statusUpgradable = false;
			for(StatusIcon icon: GameComponent.getInstance().getStatusPane().getIconList()) {
				icon.undrawUpgrade();
			}
		}
		// Skill
		if(GameComponent.getInstance().getPlayer().getExperience().getSkillPoint() > 0 && !skillUpgradable) {
			skillUpgradable = true;
			for(SkillIcon icon: GameComponent.getInstance().getSkillPane().getIconList()) {
				if(icon.isUpgradable()) {
					icon.drawUpgrade();
				}
			}
		} else if(GameComponent.getInstance().getPlayer().getExperience().getSkillPoint() == 0 && skillUpgradable){
			skillUpgradable = false;
			for(SkillIcon icon: GameComponent.getInstance().getSkillPane().getIconList()) {
				icon.undrawUpgrade();
			}
		}
	}
	
	private static void updateReloadAndSkill() {
		// Player
		for(Novice player: GameComponent.getInstance().getPlayerList()) {
			player.reload();
			// Buff
			for(Buff buff: player.getBuffList()) {
				if(buff instanceof Expirable) {
					((Expirable) buff).update();
				} else if(buff instanceof IntervalAffectable) {
					((IntervalAffectable) buff).update();
				}
			}
			
			player.getBuffList().removeIf(buff -> !buff.isActive());
			
			// Skill
			for(Skill skill: player.getSkillList()) {
				if(skill.getCaster() == null) {
					skill.setCaster(player);
				}
				if(skill instanceof ActiveSkill) {
					((ActiveSkill) skill).update();
				}
			}
		}
		// Tower
		for(Tower tower: GameComponent.getInstance().getTowerList()) {
			tower.reload();
			tower.heal(tower.getMaxHp() / 200 / Main.FRAME_RATE);
		}
		// Status Panel
		GameComponent.getInstance().getStatusPane().update();
		// SkillPanel
		GameComponent.getInstance().getSkillPane().update();
		// BuffPanel
		GameComponent.getInstance().getBuffPane().update();
	}
	
	private static void moveComponent() {
		for(Bot bot: GameComponent.getInstance().getBotList()) {
			bot.getPlayer().upgradeAbility();
			bot.getPlayer().heal(bot.getPlayer().getMaxHp() / 60.0 / Main.FRAME_RATE);
			bot.move();
			bot.getPlayer().move();
		}
		for(Bullet bullet: GameComponent.getInstance().getBulletList()) {
			bullet.move();
		}
		for(Food food: GameComponent.getInstance().getFoodList()) {
			food.rotate();
		}
	}
	
	private static void movePlayer() {
		if(GameComponent.getInstance().getPlayer().isDead()) {
			return;
		}
		
		double x = 0;
		double y = 0;
		double sz;
		
		GameComponent.getInstance().getPlayer().heal(GameComponent.getInstance().getPlayer().getMaxHp() / 60.0 / Main.FRAME_RATE);
		GameComponent.getInstance().getPlayer().upgradeAbility();
		
		for(KeyCode key: activeKey) {
			if(key == KeyCode.UP) {
				y -= GameComponent.getInstance().getPlayer().getSpeed();
			}
			
			if(key == KeyCode.DOWN) {
				y += GameComponent.getInstance().getPlayer().getSpeed();
			}
			
			if(key == KeyCode.LEFT) {
				x -= GameComponent.getInstance().getPlayer().getSpeed();
			}
			
			if(key == KeyCode.RIGHT) {
				x += GameComponent.getInstance().getPlayer().getSpeed();
			}
		}
		
		sz = Math.sqrt(x*x + y*y);
		
		if(sz != 0) {
			GameComponent.getInstance().getPlayer().setMoving(Math.toDegrees(Math.atan2(y, x)));
			GameComponent.getInstance().getPlayer().move();
		} else {
			GameComponent.getInstance().getPlayer().stopMoving();
		}
	}
	
	private static void moveCenter(Pair center) {
		// Player and HpBar
		for(Novice player: GameComponent.getInstance().getPlayerList()) {
			player.changeCenter(center);
			player.getHpBar().changeCenter(center);
		}
		// Tower and HpBar
		for(Tower tower: GameComponent.getInstance().getTowerList()) {
			tower.changeCenter(center);
			tower.getHpBar().changeCenter(center);
		}
		// Bullet
		for(Bullet bullet: GameComponent.getInstance().getBulletList()) {
			bullet.changeCenter(center);
		}
		// Food
		for(Food food: GameComponent.getInstance().getFoodList()) {
			food.changeCenter(center);
		}
		// Boundary
		GameComponent.getInstance().shiftBoundary(center);
		//Grid
		double translatex = ((-center.first % (2 * GameComponent.GRID_SIZE))) - 2 * GameComponent.GRID_SIZE;
		double translatey = ((-center.second % (2 * GameComponent.GRID_SIZE))) - 2 * GameComponent.GRID_SIZE;
		
		GameComponent.getInstance().getGrid().setTranslateX(translatex);
		GameComponent.getInstance().getGrid().setTranslateY(translatey);
		
		GameComponent.getInstance().getMinimap().changeCenter(center);
	}
	
	private static void checkCollision() {
		ArrayList<Novice> playerList = GameComponent.getInstance().getPlayerList();
		ArrayList<Tower> towerList = GameComponent.getInstance().getTowerList();
		ArrayList<Bullet> bulletList = GameComponent.getInstance().getBulletList();
		ArrayList<Food> foodList = GameComponent.getInstance().getFoodList();
		
		pairwiseCheckCollision(bulletList, bulletList);
		pairwiseCheckCollision(bulletList, foodList);
		pairwiseCheckCollision(bulletList, towerList);
		pairwiseCheckCollision(bulletList, playerList);
		pairwiseCheckCollision(playerList, foodList);
		pairwiseCheckCollision(playerList, towerList);
		pairwiseCheckCollision(playerList, playerList);
		
		GameComponent.getInstance().getExperienceBar().draw();
	}
		
	private static void pairwiseCheckCollision(ArrayList<? extends Entity> list1, 
			ArrayList<? extends Entity> list2) {
		
		if(list1.size() == 0 || list2.size() == 0) {
			return;
		}
		
		ArrayList<Entity> list = new ArrayList<Entity>();
		list.addAll(list1);
		if(list1 != list2) {
			list.addAll(list2);
		}
		list.sort((entity1, entity2) -> {
			if(entity1.getRefPoint().second < entity2.getRefPoint().second) {
				return -1;
			} else if(entity1.getRefPoint().second == entity2.getRefPoint().second) {
				return 0;
			} else {
				return 1;
			}
		});
		pairwiseCheckCollision(list, 0, GameComponent.MAX_SIZE, 
				list1.get(0).getRadius() + list2.get(0).getRadius());
	}
	
	private static void pairwiseCheckCollision(ArrayList<? extends Entity> entityList, 
			double shift, double width, double maxDist) {
		if(width <= 4 * Tower.RADIUS) {
			for(int i = 0; i < entityList.size(); ++i) {
				int j = i + 1;
				while(j < entityList.size() && entityList.get(j).getRefPoint().second 
						- entityList.get(i).getRefPoint().second<= maxDist) {
					if(entityList.get(i).isCollided(entityList.get(j)) && 
							entityList.get(i).getSide() != entityList.get(j).getSide()) {
						entityList.get(i).takeDamage(entityList.get(j));
						entityList.get(j).takeDamage(entityList.get(i));
					}
					++j;
				}
			}
		} else {
			ArrayList<Entity> left = new ArrayList<Entity>();
			ArrayList<Entity> right = new ArrayList<Entity>();
			
			for(Entity each: entityList) {
				if(each.getRefPoint().first - shift < width / 2) {
					left.add(each);
				} else {
					right.add(each);
				}
			}
			
			pairwiseCheckCollision(left, shift, width / 2, maxDist);
			pairwiseCheckCollision(right, shift + width / 2, width / 2, maxDist);
		}
	}
	
	private static void clearDeadComponent() {
		GameComponent.getInstance().getPlayerList().removeIf(player -> {
			if(player.isDead()) {
				deadPlayer.put(player, 0);
				return true;
			} else {
				return false;
			}
		});
		
		GameComponent.getInstance().getBotList().removeIf(bot -> {
			return bot.getPlayer().isDead();
		});
		
		Predicate<Entity> deathPredicate = new Predicate<Entity>() {
			public boolean test(Entity entity) {
				return entity.isDead();
			}
		};
		
		GameComponent.getInstance().getBulletList().removeIf(deathPredicate);
		GameComponent.getInstance().getFoodList().removeIf(deathPredicate);
		
		for(Novice player: GameComponent.getInstance().getPlayerList()) {
			player.getBuffList().removeIf(buff -> {
				return !buff.isActive();
			});
		}
		
		GameComponent.getInstance().getMinimap().update();
	}
	
	public static void reset() {
		autoshoot = false;
		skillUpgradable = false;
		statusUpgradable = false;
	}
}
