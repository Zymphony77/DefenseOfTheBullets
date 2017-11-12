package main;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.canvas.Canvas;

import java.util.HashSet;

import entity.bullet.*;
import entity.food.*;
import entity.job.*;
import entity.tower.*;
import utility.*;

public class Handler {
	private static HashSet<KeyCode> activeKey = new HashSet<KeyCode>();
	
	public static void keyPressed(KeyEvent event) {
		if(event.getCode() == KeyCode.SPACE) {
			Component.getInstance().getPlayer().shoot();
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
		// Move every component
		for(Novice player: Component.getInstance().getPlayerList()) {
			player.move();
		}
		
		for(Bullet bullet: Component.getInstance().getBulletList()) {
			bullet.move();
		}
		
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
			
		// Move center
		Pair center = new Pair(Component.getInstance().getPlayer().getRefPoint());
		
		// Player and HpBar
		for(Novice player: Component.getInstance().getPlayerList()) {
			player.changeCenter(center);
			player.getHpBar().changeCenter(player.getRefPoint());
		}
		// Tower and HpBar
		System.out.println();
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
	}
}
