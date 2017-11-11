package main;

import javafx.application.Application;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.layout.Pane;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.LinkedList;

import entity.*;
import entity.bullet.*;
import entity.job.*;
import utility.*;

public class Main extends Application {
	public static final int SCREEN_SIZE = 750;
	
	public static LinkedList<Novice> playerList;
	public static LinkedList<Bullet> bulletList;
	
	private static Novice player;
	
	private static Pane playerPane;
	private static Pane bulletPane;
	private static Pane wholePane;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		playerPane = new Pane();
		bulletPane = new Pane();
		
		playerList = new LinkedList<Novice>();
		bulletList = new LinkedList<Bullet>();
		
		wholePane = new Pane();
		wholePane.getChildren().addAll(playerPane, bulletPane);
		
		Novice player = new Novice(new Pair(SCREEN_SIZE / 2, SCREEN_SIZE / 2), Side.BLUE);
		addEntity(player);
		
		Scene scene = new Scene(wholePane, SCREEN_SIZE, SCREEN_SIZE);
		
		scene.setOnKeyPressed(event -> Handler.keyPressed(event, player));
		scene.setOnMouseMoved(event -> Handler.changeDirection(event, player));
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("DotB: Defense of the Bullets");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public static void addEntity(Entity entity) {
		if(entity instanceof Novice) {
			playerList.add((Novice) entity);
			playerPane.getChildren().add(entity.getCanvas());
		} else if(entity instanceof Bullet) {
			bulletList.add((Bullet) entity);
			bulletPane.getChildren().add(entity.getCanvas());
		}
	}
	
	public static void removeEntity(Entity entity) {
		if(entity instanceof Novice) {
			playerList.remove((Novice) entity);
			playerPane.getChildren().remove(entity.getCanvas());
		} else if(entity instanceof Bullet) {
			bulletList.remove((Bullet) entity);
			bulletPane.getChildren().remove(entity.getCanvas());
		}
	}
	
	public static Pair getShift() {
		return player.getRefPoint();
	}
}
