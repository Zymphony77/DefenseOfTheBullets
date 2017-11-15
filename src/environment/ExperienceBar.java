package environment;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import main.Component;
import main.Main;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import entity.property.*;

public class ExperienceBar extends Pane {
	private static final int MAX_WIDTH = 500;
	private static final int MAX_HEIGHT = 20;
	
	Experience experience;
	
	private Canvas expCanvas;
	private double maxExp;
	private double currentExp;
	
	private Canvas textCanvas;
	
	public ExperienceBar() {
		expCanvas = new Canvas(MAX_WIDTH, MAX_HEIGHT);
		expCanvas.setTranslateX(Main.SCREEN_SIZE / 2 - MAX_WIDTH / 2);
		expCanvas.setTranslateY(Main.SCREEN_SIZE - MAX_HEIGHT - 2);
		
		textCanvas = new Canvas(MAX_WIDTH, MAX_HEIGHT);
		textCanvas.setTranslateX(Main.SCREEN_SIZE / 2 - MAX_WIDTH / 2);
		textCanvas.setTranslateY(Main.SCREEN_SIZE - MAX_HEIGHT - 2);
		
		getChildren().addAll(expCanvas, textCanvas);
	}
	
	public void setExperience(Experience experience) {
		this.experience = experience;
	}
	
	public void draw() {
		drawExperience();
		drawText();
	}
	
	public void drawExperience() {
		currentExp = experience.getCurrentExp();
		maxExp = experience.getMaxExp();
		
		GraphicsContext gc = expCanvas.getGraphicsContext2D();
		
		gc.clearRect(0, 0, MAX_WIDTH, MAX_HEIGHT);
		
		gc.setFill(Color.FLORALWHITE);
		gc.fillRoundRect(0, 0, MAX_WIDTH, MAX_HEIGHT, 20, 20);
		gc.setFill(Color.ANTIQUEWHITE);
		gc.fillRoundRect(0, 0, MAX_WIDTH * currentExp / maxExp, MAX_HEIGHT, 20, 20);
	}
	
	public void drawText() {
		GraphicsContext gc = textCanvas.getGraphicsContext2D();
		
		gc.clearRect(0, 0, MAX_WIDTH, MAX_HEIGHT);
		
		gc.setFill(Color.DIMGRAY);
		gc.setFont(Font.font("Telugu MN"));
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		
		gc.fillText("-ZymphonY-", MAX_WIDTH / 5, MAX_HEIGHT / 2);
		gc.fillText("|", 3 * MAX_WIDTH / 8, MAX_HEIGHT / 2);
		gc.fillText(Component.getInstance().getPlayer().toString(), MAX_WIDTH / 2, MAX_HEIGHT / 2); 
		gc.fillText("|", 5 * MAX_WIDTH / 8, MAX_HEIGHT / 2);
		gc.fillText("Level " + experience.getLevel(), 4 * MAX_WIDTH / 5, MAX_HEIGHT / 2);
	}
}
