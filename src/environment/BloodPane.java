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
		
		text = new Canvas(Main.SCREEN_SIZE, Main.SCREEN_SIZE);
		text.setOpacity(0);
		
		GraphicsContext gc = text.getGraphicsContext2D();
		if(side == Side.BLUE) {
			gc.setFill(Color.RED);
		} else {
			gc.setFill(Color.ORANGERED);
		}
		
		gc.setStroke(Color.WHITESMOKE);
		gc.setLineWidth(2);
		gc.setFont(Font.font("Telugu MN", FontWeight.EXTRA_BOLD, 80));
		gc.setTextAlign(TextAlignment.CENTER);
		gc.fillText("RESPAWN IN", Main.SCREEN_SIZE / 2, 250);
		gc.strokeText("RESPAWN IN", Main.SCREEN_SIZE / 2, 250);
		
		gc.setFont(Font.font("Telugu MN", FontWeight.EXTRA_BOLD, 400));
		
		getChildren().add(text);
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
			GraphicsContext gc = text.getGraphicsContext2D();
			gc.clearRect(0, 350, Main.SCREEN_SIZE, 400);
			gc.fillText("" + countDown, Main.SCREEN_SIZE / 2, 575);
			gc.strokeText("" + countDown, Main.SCREEN_SIZE / 2, 575);
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
		for(Canvas each: bloodSpill) {
			each.setOpacity(0);
		}
		text.setOpacity(0);
	}
}
