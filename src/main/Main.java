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
	
	private Pane wholePane;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		wholePane = new Pane();
		wholePane.getChildren().addAll(Component.getInstance().getBulletPane());
		wholePane.getChildren().addAll(Component.getInstance().getPlayerPane());
		wholePane.getChildren().addAll(Component.getInstance().getHpBarPane());
		
		Component.getInstance().initialize(Side.BLUE);
		
		Scene scene = new Scene(wholePane, SCREEN_SIZE, SCREEN_SIZE);
		
		scene.setOnKeyPressed(event -> Handler.keyPressed(event));
		scene.setOnMouseMoved(event -> Handler.changeDirection(event));
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("DotB: Defense of the Bullets");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
}
