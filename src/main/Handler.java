package main;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import entity.job.*;
import utility.*;

public class Handler {
	public static void keyPressed(KeyEvent event) {
		if(event.getCode() == KeyCode.SPACE) {
			Component.getInstance().getPlayer().shoot();
		}
	}
	
	public static void changeDirection(MouseEvent event) {
		double x = event.getSceneX() - Main.SCREEN_SIZE / 2;
		double y = event.getSceneY() - Main.SCREEN_SIZE / 2;
		
		Component.getInstance().getPlayer().setDirection(Math.toDegrees(Math.atan2(y, x)));
		Component.getInstance().getPlayer().rotate();
	}
}
