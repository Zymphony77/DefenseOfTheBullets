package entity.property;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import entity.*;
import entity.job.*;
import entity.tower.*;
import main.*;
import main.game.GameComponent;
import utility.*;

public class HpBar {
	private Entity entity;
	private double maxWidth;
	private double space;
	private double width;
	private Canvas canvas;
	
	public HpBar(Entity entity) {
		this.entity = entity;
		
		if(entity instanceof Novice) {
			maxWidth = 60;
			space = 3;
		} else {
			maxWidth = 150;
			space = 20;
		}
		
		this.canvas = new Canvas();
		draw();
	}
	
	public void draw() {
		canvas.getGraphicsContext2D().clearRect(0, 0, maxWidth, 5);
		
		canvas.setWidth(maxWidth);
		canvas.setHeight(5);
		
		if(GameComponent.getInstance().getPlayer() != null) {
			changeCenter(GameComponent.getInstance().getPlayer().getRefPoint());
		}
		
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
		
		if(entity instanceof Tank) {
			gc.setFill(Color.AZURE);
			width = ((Tank) entity).getHPShield() * maxWidth / 5000;
			gc.fillRoundRect(0, 0, width, 5, 2, 2);
		}
	}
	
	public void changeCenter(Pair center) {
		canvas.setTranslateX(entity.getRefPoint().first - center.first 
				+ Main.SCREEN_SIZE / 2 - maxWidth / 2);
		canvas.setTranslateY(entity.getRefPoint().second - center.second 
				+ Main.SCREEN_SIZE / 2 + maxWidth / 2 + space);
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public void die() {
		canvas.setOpacity(0);
	}
	
	public Entity getEntity() {
		return entity;
	}
}
