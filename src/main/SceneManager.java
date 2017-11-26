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
import entity.property.Side;
import main.*;
import main.game.GameComponent;
import main.game.GameHandler;
import main.menu.MenuComponent;
import main.menu.MenuHandler;
import main.scoreboard.ScoreboardComponent;

public class SceneManager {
	private static Stage primaryStage;
	private static Scene gameScene;
	private static Scene menuScene;
	private static Scene scoreboardScene;
	
	public static void setGameScene(String name, Side side) {
		GameComponent.getInstance().initialize(side, name);
		
		Pane wholePane = new Pane();
		
		wholePane.getChildren().add(GameComponent.getInstance().getGrid());
		wholePane.getChildren().add(GameComponent.getInstance().getBoundaryPane());
		wholePane.getChildren().add(GameComponent.getInstance().getBulletPane());
		wholePane.getChildren().add(GameComponent.getInstance().getFoodPane());
		wholePane.getChildren().add(GameComponent.getInstance().getTowerPane());
		wholePane.getChildren().add(GameComponent.getInstance().getPlayerPane());
		wholePane.getChildren().add(GameComponent.getInstance().getHpBarPane());
		wholePane.getChildren().add(GameComponent.getInstance().getMinimap());
		wholePane.getChildren().add(GameComponent.getInstance().getExperienceBar());
		wholePane.getChildren().add(GameComponent.getInstance().getSkillPane());
		wholePane.getChildren().add(GameComponent.getInstance().getStatusPane());
		wholePane.getChildren().add(GameComponent.getInstance().getBuffPane());
		wholePane.getChildren().add(GameComponent.getInstance().getEndPane());
		
		gameScene = new Scene(wholePane, Main.SCREEN_SIZE, Main.SCREEN_SIZE);
		
		gameScene.setOnKeyPressed(event -> GameHandler.keyPressed(event));
		gameScene.setOnKeyReleased(event -> GameHandler.keyReleased(event));
		gameScene.setOnMouseMoved(event -> GameHandler.changeDirection(event));			// Move
		gameScene.setOnMouseDragged(event -> GameHandler.changeDirection(event));			// Click + Move
		gameScene.setOnMousePressed(event -> GameHandler.mousePressed(event));
		gameScene.setOnMouseReleased(event -> GameHandler.mouseReleased(event));
		
		GameHandler.startGame();
		
		primaryStage.setScene(gameScene);
		primaryStage.setOnCloseRequest(event -> GameHandler.stopTimer());
	}
	
	public static void setMenuScene() {
		Pane wholePane = new Pane();
		
		wholePane.getChildren().add(MenuComponent.getInstance().getBackgroundPane());
		wholePane.getChildren().add(MenuComponent.getInstance().getNamePane());
		wholePane.getChildren().add(MenuComponent.getInstance().getSidePane());
		
		menuScene = new Scene(wholePane, Main.SCREEN_SIZE, Main.SCREEN_SIZE);
		
		menuScene.setOnKeyPressed(event -> MenuHandler.keyPressed(event));
		
		primaryStage.setScene(menuScene);
	}
	
	public static void setScoreboardScene() {
		Pane wholePane = new Pane();
		
		wholePane.getChildren().add(ScoreboardComponent.getInstance().getBackgroundPane());
		wholePane.getChildren().add(ScoreboardComponent.getInstance().getVictoryPane());
		wholePane.getChildren().add(ScoreboardComponent.getInstance().getDefeatPane());
		
		scoreboardScene = new Scene(wholePane, Main.SCREEN_SIZE, Main.SCREEN_SIZE);
		
		primaryStage.setScene(scoreboardScene);
	}
	
	public static void setStage(Stage primaryStage) {
		SceneManager.primaryStage = primaryStage;
	}
}
