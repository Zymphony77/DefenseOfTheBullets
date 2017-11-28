package main;

import javafx.application.Application;
import javafx.animation.Timeline;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.scene.layout.Pane;
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
	public static final int FRAME_RATE = 30;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		SceneManager.setStage(primaryStage);
		SceneManager.setMenuScene();
		
		primaryStage.setTitle("DotB: Defense of the Bullets");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
}
