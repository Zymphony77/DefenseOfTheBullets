package main;

import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import utility.*;

import bot.*;
import entity.property.Side;
import main.*;
import main.game.*;
import main.menu.*;
import main.ranking.*;

public class SceneManager {
	private static Stage primaryStage;
	private static Scene gameScene;
	private static Scene menuScene;
	private static Scene rankingScene;
	
	private static boolean isMuted = false;;
	
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
		wholePane.getChildren().add(GameComponent.getInstance().getIdentityPane());
		wholePane.getChildren().add(GameComponent.getInstance().getMinimap());
		wholePane.getChildren().add(GameComponent.getInstance().getExperienceBar());
		wholePane.getChildren().add(GameComponent.getInstance().getBloodPane());
		wholePane.getChildren().add(GameComponent.getInstance().getSkillPane());
		wholePane.getChildren().add(GameComponent.getInstance().getStatusPane());
		wholePane.getChildren().add(GameComponent.getInstance().getDebuffPane());
		wholePane.getChildren().add(GameComponent.getInstance().getBuffPane());
		wholePane.getChildren().add(GameComponent.getInstance().getClassPane());
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
		primaryStage.setOnCloseRequest(event -> {
			GameHandler.stopTimer();
			GameComponent.getInstance().stopSound();
			for(Thread thread: GameComponent.getInstance().getThreadList()) {
				thread.interrupt();
			}
		});
	}
	
	public static void setMenuScene() {
		GameComponent.getInstance().reset();
		
		Pane wholePane = new Pane();
		
		wholePane.getChildren().add(MenuComponent.getInstance().getBackgroundPane());
		wholePane.getChildren().add(MenuComponent.getInstance().getNamePane());
		wholePane.getChildren().add(MenuComponent.getInstance().getSidePane());
		
		menuScene = new Scene(wholePane, Main.SCREEN_SIZE, Main.SCREEN_SIZE);
		
		menuScene.setOnKeyPressed(event -> MenuHandler.keyPressed(event));
		menuScene.setOnKeyReleased(event -> MenuHandler.keyReleased(event));
		
		primaryStage.setScene(menuScene);
		primaryStage.setOnCloseRequest(event -> {
			MenuComponent.getInstance().stopBackground();
			MenuComponent.getInstance().stopSound();
		});
	}
	
	public static void setRankingScene() {
		Pane wholePane = new Pane();
		
		wholePane.getChildren().add(RankingComponent.getInstance().getBackgroundPane());
		wholePane.getChildren().add(RankingComponent.getInstance().getRankingPane());
		
		rankingScene = new Scene(wholePane, Main.SCREEN_SIZE, Main.SCREEN_SIZE);
		
		rankingScene.setOnKeyPressed(event -> RankingHandler.keyPressed(event));
		
		primaryStage.setScene(rankingScene);
		primaryStage.setOnCloseRequest(event -> {
			RankingComponent.getInstance().stopBackground();
			MenuComponent.getInstance().stopSound();
		});
	}
	
	public static void setStage(Stage primaryStage) {
		SceneManager.primaryStage = primaryStage;
	}
	
	public static void setMuted(boolean isMuted) {
		SceneManager.isMuted = isMuted;
		MenuComponent.getInstance().drawMute();
		MenuComponent.getInstance().setMute(isMuted);
		GameComponent.getInstance().setMute(isMuted);
	}
	
	public static boolean isMuted() {
		return isMuted;
	}
	
	public static void closeProgram() {
		primaryStage.fireEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
		Platform.exit();
	}
}
