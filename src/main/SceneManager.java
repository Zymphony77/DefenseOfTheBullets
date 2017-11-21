package main;

import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import utility.*;
import bot.*;
import main.*;

public class SceneManager {
	private static Stage primaryStage;
	private static Scene gameScene;
	private static Scene menuScene;
	private static Scene endScene;
	
	static void setGameScene(Side side) {
		Component.getInstance().initialize(side);
		
		Pane wholePane = new Pane();
		
		wholePane.getChildren().add(Component.getInstance().getGrid());
		wholePane.getChildren().add(Component.getInstance().getBoundaryPane());
		wholePane.getChildren().add(Component.getInstance().getBulletPane());
		wholePane.getChildren().add(Component.getInstance().getFoodPane());
		wholePane.getChildren().add(Component.getInstance().getTowerPane());
		wholePane.getChildren().add(Component.getInstance().getPlayerPane());
		wholePane.getChildren().add(Component.getInstance().getHpBarPane());
		wholePane.getChildren().add(Component.getInstance().getMinimap());
		wholePane.getChildren().add(Component.getInstance().getExperienceBar());
		wholePane.getChildren().add(Component.getInstance().getSkillPane());
		wholePane.getChildren().add(Component.getInstance().getStatusPane());
		
		Timeline timer = new Timeline(new KeyFrame(Duration.millis(1000.00 / Main.FRAME_RATE), event -> {
			Handler.update();
			BotTower.update();
		}));
		timer.setCycleCount(Animation.INDEFINITE);
		timer.play();
		
		gameScene = new Scene(wholePane, Main.SCREEN_SIZE, Main.SCREEN_SIZE);
		
		gameScene.setOnKeyPressed(event -> Handler.keyPressed(event));
		gameScene.setOnKeyReleased(event -> Handler.keyReleased(event));
		gameScene.setOnMouseMoved(event -> Handler.changeDirection(event));
		
		primaryStage.setScene(gameScene);
		primaryStage.setOnCloseRequest(event -> timer.stop());
	}
	
	public static void setStage(Stage primaryStage) {
		SceneManager.primaryStage = primaryStage;
	}
}
