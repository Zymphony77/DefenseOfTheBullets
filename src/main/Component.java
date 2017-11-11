package main;

import java.util.LinkedList;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas; 

import entity.Entity;
import entity.bullet.Bullet;
import entity.job.Novice;
import utility.*;

public class Component {
	private static final double MAX_SIZE = 4000;
	private static final Component instance = new Component();
	
	private LinkedList<Novice> playerList;
	private LinkedList<Bullet> bulletList;
	
	private Novice player;
	
	private Canvas grid;
	private Pane boundaryPane;
	private Pane bulletPane;
	private Pane playerPane;
	private Pane hpBarPane;
	
	public Component() {
		playerPane = new Pane();
		bulletPane = new Pane();
		hpBarPane = new Pane();
		boundaryPane = new Pane();
		
		playerList = new LinkedList<Novice>();
		bulletList = new LinkedList<Bullet>();
		
		grid = new Canvas(Main.SCREEN_SIZE * 32 / 30, Main.SCREEN_SIZE * 32 / 30);
		grid.getGraphicsContext2D().setFill(Color.gray(0.95));
		for(int i = 0; i < 32; ++i) {
			for(int j = 0; j < 32; ++j) {
				if((i + j) % 2 == 0) {
					grid.getGraphicsContext2D().fillRect(Main.SCREEN_SIZE * i / 30, 
							Main.SCREEN_SIZE * j / 30, Main.SCREEN_SIZE / 30, Main.SCREEN_SIZE / 30);
				}
			}
		}
		
		addBoundary();
	}
	
	private void addBoundary() {
		Canvas topCanvas = new Canvas(MAX_SIZE + Main.SCREEN_SIZE, Main.SCREEN_SIZE / 2);
		topCanvas.getGraphicsContext2D().setFill(Color.gray(0.8));
		topCanvas.getGraphicsContext2D().fillRect(-Main.SCREEN_SIZE / 2, 
				-Main.SCREEN_SIZE / 2, MAX_SIZE + Main.SCREEN_SIZE, Main.SCREEN_SIZE / 2);
		
		Canvas bottomCanvas = new Canvas(MAX_SIZE + Main.SCREEN_SIZE, Main.SCREEN_SIZE / 2);
		bottomCanvas.getGraphicsContext2D().setFill(Color.gray(0.8));
		bottomCanvas.getGraphicsContext2D().fillRect(-Main.SCREEN_SIZE / 2, 
				MAX_SIZE, MAX_SIZE + Main.SCREEN_SIZE, Main.SCREEN_SIZE / 2);
		
		Canvas leftCanvas = new Canvas(Main.SCREEN_SIZE / 2, MAX_SIZE + Main.SCREEN_SIZE);
		leftCanvas.getGraphicsContext2D().setFill(Color.gray(0.8));
		leftCanvas.getGraphicsContext2D().fillRect(-Main.SCREEN_SIZE / 2, 
				-Main.SCREEN_SIZE / 2, Main.SCREEN_SIZE / 2, MAX_SIZE + Main.SCREEN_SIZE);
		
		Canvas rightCanvas = new Canvas(Main.SCREEN_SIZE / 2, MAX_SIZE + Main.SCREEN_SIZE);
		rightCanvas.getGraphicsContext2D().setFill(Color.gray(0.8));
		rightCanvas.getGraphicsContext2D().fillRect(MAX_SIZE, MAX_SIZE,
				Main.SCREEN_SIZE / 2, MAX_SIZE + Main.SCREEN_SIZE);
		
		boundaryPane.getChildren().addAll(topCanvas, bottomCanvas, leftCanvas, rightCanvas);
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
	
	public static Component getInstance() {
		return instance;
	}
	
	public Pair getShift() {
		return player.getRefPoint();
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
	
	public Canvas getGrid() {
		return grid;
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
