package main;

import javafx.application.Application;
import javafx.animation.Timeline;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.scene.layout.Pane;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.LinkedList;

import bot.BotTower;
import entity.*;
import entity.bullet.*;
import entity.job.*;
import entity.tower.Tower;
import utility.*;

public class Main extends Application {
	public static final int SCREEN_SIZE = 750;
	public static final int FRAME_RATE = 40;
	
	private Pane wholePane;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		Component.getInstance().initialize(Side.BLUE);
		
		wholePane = new Pane();
		
		wholePane.getChildren().add(Component.getInstance().getGrid());
		wholePane.getChildren().add(Component.getInstance().getBoundaryPane());
		wholePane.getChildren().add(Component.getInstance().getBulletPane());
		wholePane.getChildren().add(Component.getInstance().getFoodPane());
		wholePane.getChildren().add(Component.getInstance().getTowerPane());
		wholePane.getChildren().add(Component.getInstance().getPlayerPane());
		wholePane.getChildren().add(Component.getInstance().getHpBarPane());
		
		Timeline timer = new Timeline(new KeyFrame(Duration.millis(1000.00 / FRAME_RATE), event -> {
			Handler.update();
			BotTower.update();
		}));
		timer.setCycleCount(Animation.INDEFINITE);
		timer.play();
		
		Scene scene = new Scene(wholePane, SCREEN_SIZE, SCREEN_SIZE);
		
		scene.setOnKeyPressed(event -> Handler.keyPressed(event));
		scene.setOnKeyReleased(event -> Handler.keyReleased(event));
		scene.setOnMouseMoved(event -> Handler.changeDirection(event));
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("DotB: Defense of the Bullets");
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event -> timer.stop());
		primaryStage.show();
	}
}
