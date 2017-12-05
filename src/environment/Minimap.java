package environment;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import main.*;
import main.game.*;
import entity.job.*;
import entity.property.Side;
import entity.tower.*;
import utility.*;

public class Minimap extends Pane {
	public static final Image BLUE_TOWER = new Image("image/BlueTower.png");
	public static final Image RED_TOWER = new Image("image/RedTower.png");
	public static final Image NEUTRAL_TOWER = new Image("image/NeutralTower.png");
	public static final Image BLUE_TOWER_BORDER = new Image("image/BlueTowerBorder.png");
	public static final Image RED_TOWER_BORDER = new Image("image/RedTowerBorder.png");
	public static final Image NEUTRAL_TOWER_BORDER = new Image("image/NeutralTowerBorder.png");
	public static final int MAP_SIZE = Main.SCREEN_SIZE / 5;
	public static final double RATIO = MAP_SIZE / (Main.SCREEN_SIZE + GameComponent.MAX_SIZE);
	
	private Canvas boundary;
	private Canvas player;
	private Canvas tower;
	private Canvas viewBox;
	
	public Minimap() {
		super();
		
		tower = new Canvas(MAP_SIZE, MAP_SIZE);
		tower.setTranslateX(onScreen(new Pair(0, 0)).first);
		tower.setTranslateY(onScreen(new Pair(0, 0)).second);
		tower.setOpacity(0.75);
		
		player = new Canvas(MAP_SIZE, MAP_SIZE);
		player.setTranslateX(onScreen(new Pair(0, 0)).first);
		player.setTranslateY(onScreen(new Pair(0, 0)).second);
		player.setOpacity(0.75);
		
		drawBoundary();
		getChildren().addAll(player, tower);
	}
	
	private void drawBoundary() {
		boundary = new Canvas();
		boundary.setWidth(MAP_SIZE);
		boundary.setHeight(MAP_SIZE);
		
		boundary.setTranslateX(onScreen(new Pair(0, 0)).first);
		boundary.setTranslateY(onScreen(new Pair(0, 0)).second);
		
		GraphicsContext gc = boundary.getGraphicsContext2D();
		
		gc.setFill(Color.gray(0.8));
		gc.fillRect(0, 0, MAP_SIZE, MAP_SIZE);
		gc.setLineWidth(3);
		gc.setStroke(Color.ANTIQUEWHITE);
		gc.strokeRect(0, 0, MAP_SIZE, MAP_SIZE);
		gc.setFill(Color.AZURE);
		gc.fillRect(Main.SCREEN_SIZE / 2 * RATIO, Main.SCREEN_SIZE / 2 * RATIO, 
				GameComponent.MAX_SIZE * RATIO, GameComponent.MAX_SIZE * RATIO);
		
		boundary.setOpacity(0.75);
		
		getChildren().add(boundary);
	}
	
	public void drawViewBox() {
		viewBox = new Canvas();
		viewBox.setWidth(Main.SCREEN_SIZE * RATIO);
		viewBox.setHeight(Main.SCREEN_SIZE * RATIO);
		
		changeCenter(GameComponent.getInstance().getPlayer().getRefPoint());
		
		GraphicsContext gc = viewBox.getGraphicsContext2D();
		gc.setLineWidth(2);
		gc.setStroke(Color.BLACK);
		gc.strokeRect(0, 0, Main.SCREEN_SIZE * RATIO, Main.SCREEN_SIZE * RATIO);
		
		getChildren().add(viewBox);
	}
	
	public void changeCenter(Pair center) {
		Pair showCenter = onScreen(new Pair(center.first * RATIO, center.second * RATIO));
		
		viewBox.setTranslateX(showCenter.first);
		viewBox.setTranslateY(showCenter.second);
	}
	
	public void update() {
		// Tower
		Image towerImage;
		Image borderImage;
		GraphicsContext gc = tower.getGraphicsContext2D();
		for(Tower tw: GameComponent.getInstance().getTowerList()) {
			if(tw.getSide() == Side.BLUE) {
				towerImage = BLUE_TOWER;
				borderImage = BLUE_TOWER_BORDER;
			} else if(tw.getSide() == Side.RED) {
				towerImage = RED_TOWER;
				borderImage = RED_TOWER_BORDER;
			} else {
				towerImage = NEUTRAL_TOWER;
				borderImage = NEUTRAL_TOWER_BORDER;
			}
			
			gc.clearRect((tw.getRefPoint().first + Main.SCREEN_SIZE / 2 - 5 * tw.getRadius()) * RATIO, 
					(tw.getRefPoint().second + Main.SCREEN_SIZE / 2 - 5 * tw.getRadius()) * RATIO,
					tw.getRadius() * 10 * RATIO, tw.getRadius() * 10 * RATIO);
			gc.drawImage(towerImage, (tw.getRefPoint().first + Main.SCREEN_SIZE / 2 - 4.5 * tw.getRadius()) * RATIO, 
					(tw.getRefPoint().second + Main.SCREEN_SIZE / 2 - 4.5 * tw.getRadius()) * RATIO,
					tw.getRadius() * 9 * RATIO, tw.getRadius() * 9 * RATIO);
			gc.clearRect((tw.getRefPoint().first + Main.SCREEN_SIZE / 2 - 4.5 * tw.getRadius()) * RATIO, 
					(tw.getRefPoint().second + Main.SCREEN_SIZE / 2 - 4.5 * tw.getRadius()) * RATIO,
					tw.getRadius() * 9 * RATIO, tw.getRadius() * 9 * RATIO * (1 - tw.getHp() / tw.getMaxHp()));
			gc.drawImage(borderImage, (tw.getRefPoint().first + Main.SCREEN_SIZE / 2 - 5 * tw.getRadius()) * RATIO, 
					(tw.getRefPoint().second + Main.SCREEN_SIZE / 2 - 5 * tw.getRadius()) * RATIO,
					tw.getRadius() * 10 * RATIO, tw.getRadius() * 10 * RATIO);
		}
		
		// Player
		gc = player.getGraphicsContext2D();
		gc.clearRect(0, 0, MAP_SIZE, MAP_SIZE);
		for(Novice each: GameComponent.getInstance().getPlayerList()) {
			if(each.getSide() == Side.BLUE) {
				gc.setFill(Color.CORNFLOWERBLUE);
			} else {
				gc.setFill(Color.ORANGERED);
			}
			
			gc.fillOval((each.getRefPoint().first + Main.SCREEN_SIZE / 2 - 3 * each.getRadius()) * RATIO, 
					(each.getRefPoint().second + Main.SCREEN_SIZE / 2 - 3 * each.getRadius()) * RATIO, 
					each.getRadius() * 6 * RATIO, each.getRadius() * 6 * RATIO);
		}
	}
	
	private Pair onScreen(Pair position) {
		double posx = position.first + 0.8 * Main.SCREEN_SIZE - 5;
		double posy = position.second + 5;
		
		return new Pair(posx, posy);
	}
}
