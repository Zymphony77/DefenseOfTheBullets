package main;

import java.util.LinkedList;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import entity.*;
import entity.bullet.Bullet;
import entity.job.Novice;
import entity.food.*;
import utility.*;

public class Component {
	private static final double MAX_SIZE = 15000;
	private static final Component instance = new Component();
	
	private LinkedList<Novice> playerList;
	private LinkedList<Bullet> bulletList;
	private LinkedList<Food> foodList;
	private Canvas[] boundaryList;
	
	private Novice player;
	
	private Canvas grid;
	private Pane boundaryPane;
	private Pane bulletPane;
	private Pane foodPane;
	private Pane playerPane;
	private Pane hpBarPane;
	
	public Component() {
		playerPane = new Pane();
		boundaryPane = new Pane();
		bulletPane = new Pane();
		foodPane = new Pane();
		hpBarPane = new Pane();
		boundaryPane = new Pane();
		
		playerList = new LinkedList<Novice>();
		bulletList = new LinkedList<Bullet>();
		foodList = new LinkedList<Food>();
		
		grid = new Canvas(Main.SCREEN_SIZE * 32 / 30, Main.SCREEN_SIZE * 32 / 30);
		
		GraphicsContext gc = grid.getGraphicsContext2D();
		gc.setStroke(Color.gray(0.95));
		gc.setLineWidth(1);
		for(int i = 0; i < 32; ++i) {
			for(int j = 0; j < 32; ++j) {
				grid.getGraphicsContext2D().strokeRect(Main.SCREEN_SIZE * i / 30, 
						Main.SCREEN_SIZE * j / 30, Main.SCREEN_SIZE / 30, Main.SCREEN_SIZE / 30);
			}
		}
		
		addBoundary();
	}
	
	private void addBoundary() {
		boundaryList = new Canvas[4];
		
		// top canvas
		boundaryList[0] = new Canvas(Main.SCREEN_SIZE, Main.SCREEN_SIZE / 2);
		boundaryList[0].getGraphicsContext2D().setFill(Color.gray(0.8));
		boundaryList[0].getGraphicsContext2D().fillRect(0, -375, Main.SCREEN_SIZE, Main.SCREEN_SIZE / 2);
		
		// bottom canvas
		boundaryList[1] = new Canvas(Main.SCREEN_SIZE, Main.SCREEN_SIZE / 2);
		boundaryList[1].getGraphicsContext2D().setFill(Color.gray(0.8));
		boundaryList[1].getGraphicsContext2D().fillRect(0, MAX_SIZE, Main.SCREEN_SIZE, Main.SCREEN_SIZE / 2);
		
		// left canvas
		boundaryList[2] = new Canvas(Main.SCREEN_SIZE / 2, Main.SCREEN_SIZE);
		boundaryList[2].getGraphicsContext2D().setFill(Color.gray(0.8));
		boundaryList[2].getGraphicsContext2D().fillRect(-375, 0, Main.SCREEN_SIZE / 2, Main.SCREEN_SIZE);
		
		// right canvas
		boundaryList[3] = new Canvas(Main.SCREEN_SIZE / 2, Main.SCREEN_SIZE);
		boundaryList[3].getGraphicsContext2D().setFill(Color.gray(0.8));
		boundaryList[3].getGraphicsContext2D().fillRect(0, MAX_SIZE, Main.SCREEN_SIZE / 2, Main.SCREEN_SIZE);
		
		boundaryPane.getChildren().addAll(boundaryList[0], boundaryList[1], boundaryList[2], boundaryList[3]);
	}
	
	public void moveBoundary() {
		boundaryList[0].setTranslateX(getShift().first);
		boundaryList[1].setTranslateX(getShift().first);
		
		boundaryList[2].setTranslateX(getShift().second);
		boundaryList[3].setTranslateX(getShift().second);
	}
	
	public void initialize(Side side) {
		player = new Novice(new Pair(Main.SCREEN_SIZE / 2, Main.SCREEN_SIZE / 2), side);
		addComponent(player);
	}
	
	public void addComponent(Object component) {
		if(component instanceof Novice) {
			playerList.add((Novice) component);
			playerPane.getChildren().add(((Entity) component).getCanvas());
		} else if(component instanceof Bullet) {
			bulletList.add((Bullet) component);
			bulletPane.getChildren().add(((Entity) component).getCanvas());
		} else if(component instanceof HpBar) {
			hpBarPane.getChildren().add(((HpBar) component).getCanvas()); 
		}
	}
	
	public void removeComponent(Object component) {
		if(component instanceof Novice) {
			playerList.remove((Novice) component);
			playerPane.getChildren().remove(((Entity) component).getCanvas());
		} else if(component instanceof Bullet) {
			bulletList.remove((Bullet) component);
			bulletPane.getChildren().remove(((Entity) component).getCanvas());
		} else if(component instanceof HpBar) {
			hpBarPane.getChildren().remove(((HpBar) component).getCanvas()); 
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
	
	public LinkedList<Food> getFoodList() {
		return foodList;
	}
	
	public Canvas getGrid() {
		return grid;
	}
	
	public Pane getBoundaryPane() {
		return boundaryPane;
	}
	
	public Pane getPlayerPane() {
		return playerPane;
	}
	
	public Pane getBulletPane() {
		return bulletPane;
	}
	
	public Pane getFoodPane() {
		return foodPane;
	}
	
	public Pane getHpBarPane() {
		return hpBarPane;
	}
}
