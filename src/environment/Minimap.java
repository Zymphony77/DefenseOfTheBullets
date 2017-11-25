package environment;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import main.*;
import main.game.GameComponent;
import entity.property.Side;
import entity.tower.*;
import utility.*;

public class Minimap extends Pane {
	public static final int MAP_SIZE = Main.SCREEN_SIZE / 5;
	public static final double RATIO = MAP_SIZE / (Main.SCREEN_SIZE + GameComponent.MAX_SIZE);
	
	private Canvas boundary;
	private Canvas tower;
	private Canvas viewBox;
	
	public Minimap() {
		super();
		
		tower = new Canvas(MAP_SIZE, MAP_SIZE);
		tower.setTranslateX(onScreen(new Pair(0, 0)).first);
		tower.setTranslateY(onScreen(new Pair(0, 0)).second);
		tower.setOpacity(0.75);
		
		drawBoundary();
		getChildren().add(tower);
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
		GraphicsContext gc = tower.getGraphicsContext2D();
		for(Tower tw: GameComponent.getInstance().getTowerList()) {
			if(tw.getSide() == Side.BLUE) {
				gc.setFill(Color.CORNFLOWERBLUE);
			} else if(tw.getSide() == Side.RED) {
				gc.setFill(Color.ORANGERED);
			} else {
				gc.setFill(Color.GOLD);
			}
			
			gc.fillOval((tw.getRefPoint().first + Main.SCREEN_SIZE / 2 - tw.getRadius()) * RATIO, 
					(tw.getRefPoint().second + Main.SCREEN_SIZE / 2 - tw.getRadius()) * RATIO,
					tw.getRadius() * 2 * RATIO, tw.getRadius() * 2 * RATIO);
		}
	}
	
	private Pair onScreen(Pair position) {
		double posx = position.first + 0.8 * Main.SCREEN_SIZE - 5;
		double posy = position.second + 5;
		
		return new Pair(posx, posy);
	}
}
