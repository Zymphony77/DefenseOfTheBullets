package main;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import entity.job.*;
import utility.*;

public class Handler {
	public static void keyPressed(KeyEvent event, Novice player) {
		if(event.getCode() == KeyCode.SPACE) {
			player.shoot();
		}
	}
	
	public static void changeDirection(MouseEvent event, Novice player) {
		double x = event.getSceneX() - Main.SCREEN_SIZE / 2;
		double y = event.getSceneY() - Main.SCREEN_SIZE / 2;
		
		player.setDirection(Math.toDegrees(Math.atan2(y, x)));
		
		player.rotate();
	}
}
