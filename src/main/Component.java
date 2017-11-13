package main;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Random;

import bot.*;
import entity.*;
import entity.bullet.*;
import entity.job.*;
import entity.property.HpBar;
import entity.food.*;
import entity.tower.*;
import utility.*;

public class Component {
	public static final double MAX_SIZE = 1000;
	public static final int GRID_SIZE = 25;
	public static final int GRID_NUMBER = Main.SCREEN_SIZE / GRID_SIZE;
	public static final int MAX_FOOD_COUNT = 25;
	
	private static final Component instance = new Component();
	
	private ArrayList<Novice> playerList;
	private ArrayList<Bot> botList;
	private ArrayList<Tower> towerList;
	private ArrayList<Bullet> bulletList;
	private ArrayList<Food> foodList;
	private Canvas[] boundaryList;
	
	private Novice player;
	
	private Canvas grid;
	private Pane boundaryPane;
	private Pane bulletPane;
	private Pane foodPane;
	private Pane towerPane;
	private Pane playerPane;
	private Pane hpBarPane;
	
	public Component() {
		playerPane = new Pane();
		towerPane = new Pane();
		boundaryPane = new Pane();
		bulletPane = new Pane();
		foodPane = new Pane();
		hpBarPane = new Pane();
		
		playerList = new ArrayList<Novice>();
		botList = new ArrayList<Bot>();
		bulletList = new ArrayList<Bullet>();
		foodList = new ArrayList<Food>();
		towerList = new ArrayList<Tower>();
		
		grid = new Canvas(GRID_SIZE * (GRID_NUMBER + 4), GRID_SIZE * (GRID_NUMBER + 4));
		grid.setTranslateX(-2 * GRID_SIZE);
		grid.setTranslateY(-2 * GRID_SIZE);
		GraphicsContext gc = grid.getGraphicsContext2D();
		gc.setStroke(Color.gray(0.95));
		gc.setLineWidth(1);
		for(int i = 0; i < GRID_NUMBER + 4; ++i) {
			for(int j = 0; j < GRID_NUMBER + 4; ++j) {
				grid.getGraphicsContext2D().strokeRect(GRID_SIZE * i, GRID_SIZE * j, GRID_SIZE, GRID_SIZE);
			}
		}
		
		addBoundary();
	}
	
	private void addBoundary() {
		boundaryList = new Canvas[4];
		
		// top canvas
		boundaryList[0] = new Canvas(Main.SCREEN_SIZE, Main.SCREEN_SIZE / 2);
		boundaryList[0].getGraphicsContext2D().setFill(Color.gray(0.8));
		boundaryList[0].getGraphicsContext2D().fillRect(0, 0, 
				Main.SCREEN_SIZE, Main.SCREEN_SIZE / 2);
		
		// bottom canvas
		boundaryList[1] = new Canvas(Main.SCREEN_SIZE, Main.SCREEN_SIZE / 2);
		boundaryList[1].getGraphicsContext2D().setFill(Color.gray(0.8));
		boundaryList[1].getGraphicsContext2D().fillRect(0, 0, 
				Main.SCREEN_SIZE, Main.SCREEN_SIZE / 2);
		
		// left canvas
		boundaryList[2] = new Canvas(Main.SCREEN_SIZE / 2, Main.SCREEN_SIZE);
		boundaryList[2].getGraphicsContext2D().setFill(Color.gray(0.8));
		boundaryList[2].getGraphicsContext2D().fillRect(0, 0, 
				Main.SCREEN_SIZE / 2, Main.SCREEN_SIZE);
		
		// right canvas
		boundaryList[3] = new Canvas(Main.SCREEN_SIZE / 2, Main.SCREEN_SIZE);
		boundaryList[3].getGraphicsContext2D().setFill(Color.gray(0.8));
		boundaryList[3].getGraphicsContext2D().fillRect(0, 0, 
				Main.SCREEN_SIZE / 2, Main.SCREEN_SIZE);
		
		shiftBoundary(new Pair(MAX_SIZE / 2, MAX_SIZE / 2));
		
		boundaryPane.getChildren().addAll(boundaryList[0], boundaryList[1], boundaryList[2], boundaryList[3]);
	}
	
	public void shiftBoundary(Pair center) {
		boundaryList[0].setTranslateY(-center.second);
		boundaryList[1].setTranslateY(MAX_SIZE - center.second + Main.SCREEN_SIZE / 2);
		
		boundaryList[2].setTranslateX(-center.first);
		boundaryList[3].setTranslateX(MAX_SIZE - center.first + Main.SCREEN_SIZE / 2);
	}
	
	public void initialize(Side side) {
		player = new Novice(new Pair(200, 200), side);
		addComponent(player);
		
		Novice shootTest = new Novice(new Pair(400, 200), Side.RED);
		addComponent(shootTest);
		
		Tower tower = new Tower(new Pair(500, 500), Side.RED);
		addComponent(tower);
		
		generateFood();
	}
	
	public void generateFood() {
		if(foodList.size() == MAX_FOOD_COUNT) {
			return;
		}
		
		Random random = new Random();
		boolean positionPossible = true;
		
		while(foodList.size() < MAX_FOOD_COUNT) {
			int posx = random.nextInt((int) MAX_SIZE);
			int posy = random.nextInt((int) MAX_SIZE);
			
			Food food = new Food(new Pair(posx, posy));
			positionPossible = true;
			
			if(collideWithList(food, playerList)) {
				continue;
			}
			
			if(collideWithList(food, bulletList)) {
				continue;
			}
			
			if(collideWithList(food, towerList)) {
				continue;
			}
			
			if(collideWithList(food, foodList)) {
				continue;
			}
			
			addComponent(food);
		}
	}
	
	private boolean collideWithList(Entity entity, ArrayList<? extends Entity> list) {
		for(Entity each: list) {
			if(each.isCollided(entity)) {
				return true;
			}
		}
		return false;
	}
	
	public void addComponent(Object component) {
		if(component instanceof Novice) {
			playerList.add((Novice) component);
			playerPane.getChildren().add(((Entity) component).getCanvas());
			
			if((Novice) component != player) {
				botList.add(new BotNovice((Novice) component));
			}
		} else if(component instanceof Bullet) {
			bulletList.add((Bullet) component);
			bulletPane.getChildren().add(((Entity) component).getCanvas());
		} else if(component instanceof HpBar) {
			hpBarPane.getChildren().add(((HpBar) component).getCanvas()); 
		} else if(component instanceof Tower) {
			towerList.add((Tower) component);
			towerPane.getChildren().add(((Entity) component).getCanvas());
		} else if(component instanceof Food) {
			foodList.add((Food) component);
			foodPane.getChildren().add(((Food) component).getCanvas());
		}
	}
	
	public void removeComponent(Object component) {
		if(component == null) {
			return;
		}
		
		if(component instanceof Novice) {
			playerList.remove((Novice) component);
			playerPane.getChildren().remove(((Entity) component).getCanvas());
		} else if(component instanceof Bullet) {
			bulletList.remove((Bullet) component);
			bulletPane.getChildren().remove(((Entity) component).getCanvas());
		} else if(component instanceof HpBar) {
			hpBarPane.getChildren().remove(((HpBar) component).getCanvas()); 
		} else if(component instanceof Food) {
			foodList.remove((Food) component);
			foodPane.getChildren().remove(((Food) component).getCanvas());
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
	
	public ArrayList<Novice> getPlayerList() {
		return playerList;
	}
	
	public ArrayList<Bot> getBotList() {
		return botList;
	}
	
	public ArrayList<Tower> getTowerList() {
		return towerList;
	}
	
	public ArrayList<Bullet> getBulletList() {
		return bulletList;
	}
	
	public ArrayList<Food> getFoodList() {
		return foodList;
	}
	
	public Pane getBoundaryPane() {
		return boundaryPane;
	}
	
	public Pane getHpBarPane() {
		return hpBarPane;
	}
	
	public Pane getPlayerPane() {
		return playerPane;
	}
	
	public Pane getTowerPane() {
		return towerPane;
	}
	
	public Pane getBulletPane() {
		return bulletPane;
	}
	
	public Pane getFoodPane() {
		return foodPane;
	}
	
	public Canvas[] getBoundaryList() {
		return boundaryList;
	}
	
	public Canvas getGrid() {
		return grid;
	}
}
