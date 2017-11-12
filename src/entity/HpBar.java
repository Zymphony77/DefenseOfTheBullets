package entity;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import entity.job.*;
import main.*;
import utility.*;

public class HpBar {
	private Entity entity;
	private double maxWidth;
	private double width;
	private Canvas canvas;
	
	public HpBar(Entity entity) {
		this.entity = entity;
		
		if(entity instanceof Novice) {
			maxWidth = 60;
		} else {
			maxWidth = 150;
		}
		
		this.canvas = new Canvas();
		draw();
	}
	
	public void draw() {
		canvas.getGraphicsContext2D().clearRect(0, 0, maxWidth, 5);
		
		canvas.setWidth(maxWidth);
		canvas.setHeight(5);
		
		canvas.setTranslateX(entity.getRefPoint().first - maxWidth / 2);
		canvas.setTranslateY(entity.getRefPoint().second + maxWidth / 2 + 5);
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		if(entity.getSide() == Side.RED) {
			gc.setStroke(Color.ORANGERED);
			gc.setFill(Color.ORANGERED);
		} else if(entity.getSide() == Side.BLUE) {
			gc.setStroke(Color.CORNFLOWERBLUE);
			gc.setFill(Color.CORNFLOWERBLUE);
		} else {
			gc.setStroke(Color.GOLD);
			gc.setFill(Color.GOLD);
		}
		
		width = entity.getHp() / entity.getMaxHp() * maxWidth;
		
		gc.strokeRoundRect(0, 0, maxWidth, 5, 2, 2);
		gc.fillRoundRect(0, 0, width, 5, 2, 2);
	}
	
	public void changeCenter(Pair center) {
		canvas.setTranslateX(Main.SCREEN_SIZE / 2 - maxWidth / 2);
		canvas.setTranslateY(Main.SCREEN_SIZE / 2 + maxWidth / 2 + 5);
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public void die() {
		canvas.setOpacity(0);
	}
}
