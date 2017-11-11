package main;

import java.util.LinkedList;

import entity.Entity;
import entity.bullet.Bullet;
import entity.job.Novice;
import javafx.scene.layout.Pane;
import utility.*;

public class Component {
	private static final Component instance = new Component();
	
	public LinkedList<Novice> playerList;
	public LinkedList<Bullet> bulletList;
	
	private Novice player;
	
	private Pane bulletPane;
	private Pane playerPane;
	private Pane hpBarPane;
	
	public Component() {
		playerPane = new Pane();
		bulletPane = new Pane();
		hpBarPane = new Pane();
		
		playerList = new LinkedList<Novice>();
		bulletList = new LinkedList<Bullet>();
	}
	
	public void initialize(Side side) {
		player = new Novice(new Pair(Main.SCREEN_SIZE / 2, Main.SCREEN_SIZE / 2), side);
		addEntity(player);
	}
	
	public void addEntity(Entity entity) {
		if(entity instanceof Novice) {
			playerList.add((Novice) entity);
			playerPane.getChildren().add(entity.getCanvas());
		} else if(entity instanceof Bullet) {
			bulletList.add((Bullet) entity);
			bulletPane.getChildren().add(entity.getCanvas());
		}
	}
	
	public void removeEntity(Entity entity) {
		if(entity instanceof Novice) {
			playerList.remove((Novice) entity);
			playerPane.getChildren().remove(entity.getCanvas());
		} else if(entity instanceof Bullet) {
			bulletList.remove((Bullet) entity);
			bulletPane.getChildren().remove(entity.getCanvas());
		}
	}
	
	public Pair getShift() {
		return player.getRefPoint();
	}
	
	public static Component getInstance() {
		return instance;
	}
	
	public Novice getPlayer() {
		return player;
	}
	
	public LinkedList<Novice> getPlayerList() {
		return playerList;
	}
	
	public LinkedList<Bullet> getBulletList() {
		return bulletList;
	}
	
	public Pane getPlayerPane() {
		return playerPane;
	}
	
	public Pane getBulletPane() {
		return bulletPane;
	}
	
	public Pane getHpBarPane() {
		return hpBarPane;
	}
}
