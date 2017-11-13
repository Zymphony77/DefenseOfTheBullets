package main;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.canvas.Canvas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Predicate;

import bot.*;
import entity.*;
import entity.bullet.*;
import entity.food.*;
import entity.job.*;
import entity.property.*;
import entity.tower.*;
import utility.*;

public class Handler {
	private static HashSet<KeyCode> activeKey = new HashSet<KeyCode>();
	
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
	}
	
	public static void changeDirection(MouseEvent event) {
		double x = event.getSceneX() - Main.SCREEN_SIZE / 2;
		double y = event.getSceneY() - Main.SCREEN_SIZE / 2;
		
		Component.getInstance().getPlayer().setDirection(Math.toDegrees(Math.atan2(y, x)));
		Component.getInstance().getPlayer().rotate();
	}
	
	public static void update() {
		if(activeKey.contains(KeyCode.SPACE)) {
			Component.getInstance().getPlayer().shoot();
		}
		
		reloadAmmo();
		moveComponent();
		movePlayer();
		moveCenter(Component.getInstance().getPlayer().getRefPoint());
		checkCollision();
		clearDeadComponent();
		
		Component.getInstance().generateFood();
	}
	
	public static void reloadAmmo() {
		for(Novice player: Component.getInstance().getPlayerList()) {
			player.reload();
		}
		for(Tower tower: Component.getInstance().getTowerList()) {
			tower.reload();
		}
	}
	
	public static void moveComponent() {
		// Move every component
		for(Bot bot: Component.getInstance().getBotList()) {
			bot.move();
			bot.getPlayer().move();
		}
		for(Bullet bullet: Component.getInstance().getBulletList()) {
			bullet.move();
		}
		for(Food food: Component.getInstance().getFoodList()) {
			food.rotate();
		}
	}
	
	public static void movePlayer() {
		// Move player
		double x = 0;
		double y = 0;
		double sz;
		
		for(KeyCode key: activeKey) {
			if(key == KeyCode.UP) {
				y -= Component.getInstance().getPlayer().getSpeed();
			}
			
			if(key == KeyCode.DOWN) {
				y += Component.getInstance().getPlayer().getSpeed();
			}
			
			if(key == KeyCode.LEFT) {
				x -= Component.getInstance().getPlayer().getSpeed();
			}
			
			if(key == KeyCode.RIGHT) {
				x += Component.getInstance().getPlayer().getSpeed();
			}
		}
		
		sz = Math.sqrt(x*x + y*y);
		
		if(sz != 0) {
			Component.getInstance().getPlayer().setMoving(Math.toDegrees(Math.atan2(y, x)));
			Component.getInstance().getPlayer().move();
		} else {
			Component.getInstance().getPlayer().stopMoving();
		}
	}
	
	public static void moveCenter(Pair center) {
		// Player and HpBar
		for(Novice player: Component.getInstance().getPlayerList()) {
			player.changeCenter(center);
			player.getHpBar().changeCenter(center);
		}
		// Tower and HpBar
		for(Tower tower: Component.getInstance().getTowerList()) {
			tower.changeCenter(center);
			tower.getHpBar().changeCenter(center);
		}
		// Bullet
		for(Bullet bullet: Component.getInstance().getBulletList()) {
			bullet.changeCenter(center);
		}
		// Food
		for(Food food: Component.getInstance().getFoodList()) {
			food.changeCenter(center);
		}
		// Boundary
		Component.getInstance().shiftBoundary(center);
		//Grid
		double translatex = ((-center.first % (2 * Component.GRID_SIZE))) - 2 * Component.GRID_SIZE;
		double translatey = ((-center.second % (2 * Component.GRID_SIZE))) - 2 * Component.GRID_SIZE;
		
		Component.getInstance().getGrid().setTranslateX(translatex);
		Component.getInstance().getGrid().setTranslateY(translatey);
		
		Component.getInstance().getMinimap().changeCenter(center);
	}
	
	public static void checkCollision() {
		ArrayList<Novice> playerList = Component.getInstance().getPlayerList();
		ArrayList<Tower> towerList = Component.getInstance().getTowerList();
		ArrayList<Bullet> bulletList = Component.getInstance().getBulletList();
		ArrayList<Food> foodList = Component.getInstance().getFoodList();
		
		pairwiseCheckCollision(bulletList, bulletList);
		pairwiseCheckCollision(bulletList, foodList);
		pairwiseCheckCollision(bulletList, towerList);
		pairwiseCheckCollision(bulletList, playerList);
		pairwiseCheckCollision(playerList, foodList);
		pairwiseCheckCollision(playerList, towerList);
		pairwiseCheckCollision(playerList, playerList);
	}
	
	private static void pairwiseCheckCollision(ArrayList<? extends Entity> list1, 
			ArrayList<? extends Entity> list2) {
		for(int i = 0; i < list1.size(); ++i) {
			for(int j = (list1 == list2? i + 1: 0); j < list2.size(); ++j) {
				if(list1.get(i).getSide() == list2.get(j).getSide()) {
					continue;
				}
				
				if(list1.get(i).isCollided(list2.get(j))) {
					list1.get(i).takeDamage(list2.get(j));
					list2.get(j).takeDamage(list1.get(i));
				}
			}
		}
	}
	
	public static void clearDeadComponent() {
		Predicate<Entity> deathPredicate = new Predicate<Entity>() {
			public boolean test(Entity entity) {
				return entity.isDead();
			}
		};
		
		Component.getInstance().getPlayerList().removeIf(deathPredicate);
		Component.getInstance().getBulletList().removeIf(deathPredicate);
		Component.getInstance().getFoodList().removeIf(deathPredicate);
	}
}
