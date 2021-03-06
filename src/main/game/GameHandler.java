package main.game;

import javafx.animation.Timeline;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.canvas.Canvas;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Random;
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
import main.*;
import main.menu.*;
import main.ranking.*;
import skill.*;
import utility.*;

public class GameHandler {
	public static class PairFx<S, T> extends javafx.util.Pair<S, T> {
		public PairFx(S s, T t) {
			super(s, t);
		}
	}
	
	private static final int SPAWN_TIME = 5 * Main.FRAME_RATE;
	
	private static HashSet<KeyCode> activeKey;
	private static HashSet<MouseButton> activeMouse;
	private static HashSet<PairFx<Entity, Entity>> collisionPair;
	private static HashMap<Novice, Integer> deadPlayer;
	private static boolean autoshoot;
	private static boolean justDead;
	private static boolean isEnd;
	private static boolean changeSceneReady;
	
	private static Timeline timer;
	
	static {
		activeKey = new HashSet<KeyCode>();
		activeMouse = new HashSet<MouseButton>();
		collisionPair = new HashSet<PairFx<Entity, Entity>>();
		deadPlayer = new HashMap<Novice, Integer>();
		autoshoot = false;
		justDead = false;
		isEnd = false;
		changeSceneReady = false;
	}
	
	public static void keyPressed(KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER && changeSceneReady) {
			reset();
			GameComponent.getInstance().stopSound();
			MenuComponent.getInstance().startSound();
			RankingComponent.getInstance().moveBackground();
			SceneManager.setRankingScene();
		}
		
		if(event.getCode() == KeyCode.SHIFT) {
			activeKey.add(KeyCode.SHIFT);
		}
		
		if(event.getCode() == KeyCode.CONTROL || event.getCode() == KeyCode.COMMAND) {
			activeKey.add(KeyCode.CONTROL);
		}
		
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
		
		if(event.getCode().isDigitKey() && !activeKey.contains(event.getCode())) {
			activeKey.add(event.getCode());
			
			String keyName = event.getCode().toString();
			int number = Integer.parseInt(keyName.substring(keyName.length() - 1));
			
			if(activeKey.contains(KeyCode.CONTROL)) {
				if(0 < number && number <= 6 && GameComponent.getInstance().getPlayer().getExperience().getPointStatus() > 0 && 
						GameComponent.getInstance().getStatusPane().getIcon(number - 1).isUpgradable()) {
					GameComponent.getInstance().getPlayer().upgradeStatus(number - 1);
				}
			} else if(activeKey.contains(KeyCode.SHIFT)) {
				if(0 < number && number <= GameComponent.getInstance().getPlayer().getSkillList().size() &&
						GameComponent.getInstance().getSkillPane().getIcon(number).isUpgradable()) {
					GameComponent.getInstance().getPlayer().upgradeSkill(number);
				}
			} else {
				if(0 < number && number <= GameComponent.getInstance().getPlayer().getSkillList().size()) {
					GameComponent.getInstance().getPlayer().useSkill(number);
				}
			}
		}
		
		if(event.getCode() == KeyCode.E && !activeKey.contains(KeyCode.E)) {
			activeKey.add(KeyCode.E);
			autoshoot = !autoshoot;
		}
		
		if(event.getCode() == KeyCode.M && !activeKey.contains(event.getCode())) {
			activeKey.add(KeyCode.M);
			SceneManager.setMuted(!SceneManager.isMuted());
		}
		
		// CHEAT -- FOR TESTING PURPOSES
		
		// MAX STATUS
		if(event.getCode() == KeyCode.EQUALS) {
			while(GameComponent.getInstance().getPlayer().getExperience().getLevel() < Experience.MAX_LEVEL) {
				GameComponent.getInstance().getPlayer().gainExp(GameComponent.getInstance().getPlayer().getExperience().getMaxExp());
			}
			
			for(int i = 0; i < 6; ++i) {
				for(int j = 0; j < 15; ++j) {
					GameComponent.getInstance().getPlayer().getStatus().updateStatus(i);
				}
			}
			
			for(int i = 0; i < GameComponent.getInstance().getPlayer().getSkillList().size(); ++i) {
				while(GameComponent.getInstance().getPlayer().getSkillList().get(i).isUpgradable()) {
					GameComponent.getInstance().getPlayer().upgradeSkill(i + 1);
				}
			}
			
			for(StatusIcon icon: GameComponent.getInstance().getStatusPane().getIconList()) {
				icon.undrawUpgrade();
			}
			
			for(SkillIcon icon: GameComponent.getInstance().getSkillPane().getIconList()) {
				icon.undrawUpgrade();
			}
			
			GameComponent.getInstance().getPlayer().upgradeAbility();
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
		
		if(event.getCode() == KeyCode.SHIFT) {
			activeKey.remove(KeyCode.SHIFT);
		}
		
		if(event.getCode() == KeyCode.CONTROL || event.getCode() == KeyCode.COMMAND) {
			activeKey.remove(KeyCode.CONTROL);
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
		
		if(event.getCode() == KeyCode.E) {
			activeKey.remove(KeyCode.E);
		}
		
		if(event.getCode() == KeyCode.M) {
			activeKey.remove(KeyCode.M);
		}
		
		if(event.getCode().isDigitKey()) {
			activeKey.remove(event.getCode());
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
	
	public static void startGame() {
		timer = new Timeline(new KeyFrame(Duration.millis(1000.00 / Main.FRAME_RATE), event -> {
			GameHandler.update();
			BotTower.update();
		}));
		timer.setCycleCount(Animation.INDEFINITE);
		timer.play();
	}
	
	public static void changeDirection(MouseEvent event) {
		if(isEnd) {
			return;
		}
		
		if(GameComponent.getInstance().getPlayer() instanceof Tank && ((Tank) GameComponent.getInstance().getPlayer()).isBurstBuff()) {
			return;
		}
		
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
		changeClass();
		checkEnding();
		
		GameComponent.getInstance().generateFood();
	}
	
	private static void updateDeadPlayer() {
		for(Novice player: deadPlayer.keySet()) {
			if(deadPlayer.get(player).intValue() >= SPAWN_TIME || isEnd) {
				GameComponent.getInstance().getBloodPane().undrawDeadScene();
				player.getExperience().reborn();
				
				Novice newPlayer = new Novice(GameComponent.spawnPoint(player.getSide()), player.getExperience(), 
						player.getSide(), player.getKill(), player.getDeath(), player.getName());
				
				if(player.isPlayer()) {
					rebornReset();
					
					GameComponent.getInstance().getSkillPane().clear();
					GameComponent.getInstance().getStatusPane().clear();
					GameComponent.getInstance().getClassPane().clear();
					
					GameComponent.getInstance().setPlayer(newPlayer);
					GameComponent.getInstance().getStatusPane().setPlayer(newPlayer);
					GameComponent.getInstance().getSkillPane().setPlayer(newPlayer);
					GameComponent.getInstance().getExperienceBar().setExperience(newPlayer.getExperience());
				} else {
					newPlayer.setPlayer(false);
				}
				
				newPlayer.drawIdentity();
				newPlayer.addBuff(new InvincibleBuff(newPlayer));
				GameComponent.getInstance().addComponent(newPlayer);
			}
			
			deadPlayer.put(player, new Integer(deadPlayer.get(player).intValue() + 1));
		}
		
		deadPlayer.keySet().removeIf(player -> deadPlayer.get(player).intValue() > SPAWN_TIME || isEnd);
	}
	
	private static void checkPlayerUpgrade() {
		// Status
		for(StatusIcon icon: GameComponent.getInstance().getStatusPane().getIconList()) {
			if(icon.isUpgradable() && GameComponent.getInstance().getPlayer().getExperience().getPointStatus() > 0) {
				icon.drawUpgrade();
			} else {
				icon.undrawUpgrade();
			}
		}
		// Skill
		for(SkillIcon icon: GameComponent.getInstance().getSkillPane().getIconList()) {
			if(icon.isUpgradable()) {
				icon.drawUpgrade();
			} else {
				icon.undrawUpgrade();
			}
		}
		
		// Job
		if(GameComponent.getInstance().getPlayer().getJob() == Job.NOVICE && 
				GameComponent.getInstance().getPlayer().getLevel() > 10 &&
				!GameComponent.getInstance().getClassPane().isShowing()) {
			GameComponent.getInstance().getClassPane().showClassPane();
		}
	}
	
	private static void updateReloadAndSkill() {
		// Player
		for(Novice player: GameComponent.getInstance().getPlayerList()) {
			player.heal(player.getHealthRegen());
			player.reload();
			// Buff
			for(Buff buff: player.getBuffList()) {
				if(buff instanceof Expirable) {
					((Expirable) buff).update();
				}
			}
			
			int currentBuff = player.getBuffList().size();
			player.getBuffList().removeIf(buff -> !buff.isActive());
			if(currentBuff != player.getBuffList().size()) {
				player.upgradeAbility();
			}
			
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
		// StatusPane
		GameComponent.getInstance().getStatusPane().update();
		// SkillPane
		GameComponent.getInstance().getSkillPane().update();
		// BuffPane
		GameComponent.getInstance().getBuffPane().update();
		// DebuffPane
		GameComponent.getInstance().getDebuffPane().update();
	}
	
	private static void moveComponent() {
		for(Bot bot: GameComponent.getInstance().getBotList()) {
			bot.move();
			bot.getPlayer().move();
		}
		for(Bullet bullet: GameComponent.getInstance().getBulletList()) {
			bullet.move();
			if(bullet instanceof Rotatable) {
				((Rotatable) bullet).rotate();
			}
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
		
		GameComponent.getInstance().getPlayer().heal(GameComponent.getInstance().getPlayer().getHealthRegen());
		
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
		// Player, HpBar and Identity
		for(Novice player: GameComponent.getInstance().getPlayerList()) {
			player.changeCenter(center);
			player.getHpBar().changeCenter(center);
			player.getIdentity().changeCenter(center);
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
		
		pairwiseCheckCollision(bulletList, foodList);
		pairwiseCheckCollision(bulletList, towerList);
		pairwiseCheckCollision(bulletList, playerList);
		pairwiseCheckCollision(playerList, foodList);
		pairwiseCheckCollision(playerList, towerList);
		
		if(GameComponent.getInstance().getPlayer().isDead() && !justDead) {
			justDead = true;
			GameComponent.getInstance().getBloodPane().drawDeadScene();
		}
		
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
		collisionPair.clear();
		pairwiseCheckCollision(list, 0, GameComponent.MAX_SIZE, 
				Math.max(list1.get(0).getMaxRadius(), list2.get(0).getMaxRadius()) * 2);
	}
	
	private static void pairwiseCheckCollision(ArrayList<? extends Entity> entityList, 
			double shift, double width, double maxDist) {
		ArrayList<Entity> middle = new ArrayList<Entity>();
		for(Entity each: entityList) {
			if(Math.abs(each.getRefPoint().first - shift - width/2) <= maxDist) {
				middle.add(each);
			}
		}
		
		for(int i = 0; i < middle.size(); ++i) {
			int j = i + 1;
			while(j < middle.size() && middle.get(j).getRefPoint().second - middle.get(i).getRefPoint().second <= maxDist) {
				if(middle.get(i).isCollided(middle.get(j)) && middle.get(i).getSide() != middle.get(j).getSide() &&
						!middle.get(i).isDead() && !middle.get(j).isDead() && 
						!collisionPair.contains(new PairFx<Entity, Entity>(middle.get(i), middle.get(j)))) {
					middle.get(i).takeDamage(middle.get(j));
					middle.get(j).takeDamage(middle.get(i));
					collisionPair.add(new PairFx<Entity, Entity>(middle.get(i), middle.get(j)));
				}
				++j;
			}
		}
		
		if(width > maxDist) {
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
		
//		for(Novice player: GameComponent.getInstance().getPlayerList()) {
//			player.getBuffList().removeIf(buff -> {
//				return !buff.isActive();
//			});
//		}
//		
		GameComponent.getInstance().getMinimap().update();
	}
	
	private static void changeClass() {
		ArrayList<Novice> newPlayerList = new ArrayList<Novice>();
		
		for(Novice player: GameComponent.getInstance().getPlayerList()) {
			if(!player.isChangeJobRequested()) {
				continue;
			}
			
			if(!player.isPlayer()) {
				for(Bot bot: GameComponent.getInstance().getBotList()) {
					if(bot.getPlayer() == player) {
						GameComponent.getInstance().getBotList().remove(bot);
						break;
					}
				}
			}
			
			Novice newPlayer;
			if(player.getNewJob() == Job.TANK) {
				newPlayer = new Tank(player);
			} else if(player.getNewJob() == Job.MAGICIAN) {
				newPlayer = new Magician(player);
			} else {
				newPlayer = new Ranger(player);
			}
			
			for(Skill skill: newPlayer.getSkillList()) {
				skill.setCaster(newPlayer);
			}
			
			newPlayer.drawIdentity();
			newPlayer.upgradeAbility();
			
			if(newPlayer.isPlayer()) {
				GameComponent.getInstance().getClassPane().clear();
				
				GameComponent.getInstance().setPlayer(newPlayer);
				GameComponent.getInstance().getSkillPane().setPlayer(newPlayer);
				GameComponent.getInstance().getStatusPane().setPlayer(newPlayer);
				GameComponent.getInstance().getExperienceBar().setExperience(newPlayer.getExperience());
				
				for(StatusIcon icon: GameComponent.getInstance().getStatusPane().getIconList()) {
					icon.requestFocus();
				}
				
				for(SkillIcon icon: GameComponent.getInstance().getSkillPane().getIconList()) {
					icon.requestFocus();
				}
			}
			
			newPlayerList.add(newPlayer);
		}
		
		GameComponent.getInstance().getPlayerList().removeIf(player -> {
			if(player.isChangeJobRequested()) {
				GameComponent.getInstance().getPlayerPane().getChildren().remove(player.getCanvas());
				GameComponent.getInstance().getHpBarPane().getChildren().remove(player.getHpBar().getCanvas());
				GameComponent.getInstance().getIdentityPane().getChildren().remove(player.getIdentity().getCanvas());
				return true;
			} else {
				return false;
			}
		});
		
		for(Novice newPlayer: newPlayerList) {
			GameComponent.getInstance().addComponent(newPlayer);
			newPlayer.requestChangeJob(null);
		}
	}
	
	private static void checkEnding() {
		Side side = Side.NEUTRAL;
		for(Tower tower: GameComponent.getInstance().getTowerList()) {
			if(side == Side.NEUTRAL) {
				side = tower.getSide();
			}
			
			if(side == Side.NEUTRAL) {
				return;
			}
			
			if(side != tower.getSide()) {
				return;
			}
		}
		
		if(GameComponent.getInstance().getPlayer().getSide() == side) {
			endGame("Victory");
		} else {
			endGame("Defeat");
		}
	}
	
	private static void endGame(String mode) {
		isEnd = true;
		updateDeadPlayer();
		stopTimer();
		
		GameComponent.getInstance().stopBGM();
		GameComponent.getInstance().playShakeSound();
		
		Random random = new Random();
		Pair pos = GameComponent.getInstance().getPlayer().getRefPoint();
		Timeline shake = new Timeline(new KeyFrame(Duration.millis(20), event -> {
			moveCenter(new Pair(pos.first + random.nextInt(21) - 10, pos.second + random.nextInt(21) - 10));
		}));
		shake.setCycleCount(50);
		shake.setOnFinished(event -> {
			moveCenter(pos);
			Timeline delay = new Timeline(new KeyFrame(Duration.seconds(2), e1 -> {}));
			delay.setCycleCount(1);
			delay.setOnFinished(e2 -> GameComponent.getInstance().setEnding(mode));
			delay.playFromStart();
		});
		shake.playFromStart();
	}
	
	public static void stopTimer() {
		timer.stop();
	}
	
	public static void allowChangeScene() {
		changeSceneReady = true;
	}
	
	public static void rebornReset() {
		autoshoot = false;
		justDead = false;
	}
	
	public static void reset() {
		rebornReset();
		isEnd = false;
		changeSceneReady = false;
	}
}
