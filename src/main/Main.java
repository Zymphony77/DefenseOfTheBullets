package main;

import javafx.application.Application;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import job.*;
import utility.*;

public class Main extends Application {
	int x = 0;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		Novice novice = new Novice(new Pair(100, 100), true);
		
		StackPane pane = new StackPane();
		pane.getChildren().add(novice.getCanvas());
		
		Scene scene = new Scene(pane, 500, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(5), event -> {
			novice.getCanvas().setTranslateX(x);
			novice.getCanvas().setRotate(-x);
			--x;
		}));
		timeline.setCycleCount(200);
		timeline.play();
	}
}
