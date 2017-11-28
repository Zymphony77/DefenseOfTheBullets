package environment;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import main.Main;
import entity.property.*;

public class BloodPane extends Pane {
	private Canvas[] bloodSpill;
	private Canvas[] respawn;
	private Canvas text;
	private double textOpacity;
	private int spillStage;
	private int countDown;
	
	public BloodPane(Side side) {
		spillStage = 0;
		countDown = 3;
		
		String path = "resource/image/" + (side == Side.RED? "Red": "Blue") + "BloodSpill";
		bloodSpill = new Canvas[10];
		for(int i = 0; i < 10; ++i) {
			bloodSpill[i] = new Canvas(Main.SCREEN_SIZE, Main.SCREEN_SIZE);
			bloodSpill[i].setOpacity(0);
			bloodSpill[i].getGraphicsContext2D().drawImage(new Image(path + i + ".png"), 0, 0, Main.SCREEN_SIZE, Main.SCREEN_SIZE);
			
			getChildren().add(bloodSpill[i]);
		}
		
		path = "resource/image/Respawn";
		
		text = new Canvas(Main.SCREEN_SIZE, Main.SCREEN_SIZE);
		text.setOpacity(0);
		text.getGraphicsContext2D().drawImage(new Image(path + "Text.png"), 0, 0, Main.SCREEN_SIZE, Main.SCREEN_SIZE);
		
		getChildren().add(text);
		
		respawn = new Canvas[4];
		for(int i = 0; i < 4; ++i) {
			respawn[i] = new Canvas(Main.SCREEN_SIZE, Main.SCREEN_SIZE);
			respawn[i].setOpacity(0);
			respawn[i].getGraphicsContext2D().drawImage(new Image(path + i + ".png"), 0, 0, Main.SCREEN_SIZE, Main.SCREEN_SIZE);
			
			getChildren().add(respawn[i]);
		}
	}
	
	public void drawDeadScene() {
		Timeline bloodAnimate = new Timeline(new KeyFrame(Duration.millis(10), event -> {
			bloodSpill[spillStage].setOpacity(1);
			++spillStage;
		}));
		bloodAnimate.setCycleCount(10);
		
		textOpacity = 0;
		Timeline showText = new Timeline(new KeyFrame(Duration.millis(1), event -> {
			textOpacity += 0.001;
			text.setOpacity(textOpacity);
		}));
		showText.setCycleCount(1000);
		
		Timeline count = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
			if(countDown < 3) {
				respawn[countDown + 1].setOpacity(0);
			} else {
				text.setOpacity(0);
			}
			respawn[countDown].setOpacity(1);
			--countDown;
		}));
		count.setCycleCount(4);
		
		bloodAnimate.setOnFinished(event -> showText.play());
		showText.setOnFinished(event -> count.play());
		count.setOnFinished(event -> undrawDeadScene());
		
		bloodAnimate.play();
	}
	public void undrawDeadScene() {
		spillStage = 0;
		textOpacity = 0;
		countDown = 3;
		
		text.setOpacity(0);
		for(Canvas each: bloodSpill) {
			each.setOpacity(0);
		}
		for(Canvas each: respawn) {
			each.setOpacity(0);
		}
	}
}
