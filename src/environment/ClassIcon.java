package environment;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;

import main.game.*;
import utility.*;

public class ClassIcon extends Pane {
	public static final double RADIUS = 12;
	public static final int ICON_SIZE = 50;
	
	private Job job;
	private Canvas icon;
	private int position;
	
	public ClassIcon(Job job, int position) {
		this.job = job;
		
		icon = new Canvas(ICON_SIZE, ICON_SIZE);
		
		icon.setOpacity(0.8);
		
		this.setPosition(position);
		
		GraphicsContext gc = icon.getGraphicsContext2D();
		gc.setFill(Color.ANTIQUEWHITE);
		gc.fillRoundRect(0, 0, ICON_SIZE, ICON_SIZE, 10, 10);
		
		if(job == Job.TANK) {
			gc.setFill(Color.DARKGRAY);
			gc.fillRect(8, 4.5 + RADIUS, ICON_SIZE - 16, 5);
			gc.setFill(Color.GRAY);
			gc.fillOval(ICON_SIZE / 2 - RADIUS, 7, 2*RADIUS, 2*RADIUS);
		}
		else if(job == Job.MAGICIAN) {
			gc.setFill(Color.DARKGRAY);
			gc.fillRect(ICON_SIZE / 2, 4.5 + RADIUS, ICON_SIZE / 2 - 8, 5);
			gc.setFill(Color.MEDIUMPURPLE);
			gc.fillOval(ICON_SIZE / 2 - RADIUS, 7, 2*RADIUS, 2*RADIUS);
		}
		else if(job == Job.RANGER) {
			gc.setFill(Color.DARKGRAY);
			gc.fillRect(ICON_SIZE / 2, 2 + RADIUS, ICON_SIZE / 2 - 8, 4);
			gc.fillRect(ICON_SIZE / 2, 7 + RADIUS, ICON_SIZE / 2 - 8, 4);
			gc.setFill(Color.DARKSEAGREEN);
			gc.fillOval(ICON_SIZE / 2 - RADIUS, 7, 2*RADIUS, 2*RADIUS);
		}
		
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setFill(Color.BLACK);
		gc.setFont(Font.font("Telugu MN", 8));
		gc.fillText(job.toString(), ICON_SIZE / 2, ICON_SIZE - 5);
		gc.strokeRect(0, 0, icon.getWidth(), icon.getHeight());
		
		icon.setOnMouseClicked(event -> {
			if(event.getButton() == MouseButton.PRIMARY) {
				GameComponent.getInstance().getPlayer().requestChangeJob(job);
			}
		});
		
		getChildren().addAll(icon);
	}
	
	public void clear() {
		icon.getGraphicsContext2D().clearRect(0, 0, ICON_SIZE, ICON_SIZE);
	}
	
	public void setPosition(int position) {
		this.position = position;
		
		icon.setTranslateX(getShift().first);
		icon.setTranslateY(getShift().second);
		
	}
	
	public Pair getShift() {
		return new Pair(10 + (ICON_SIZE + 5) * (position), 
				10 + 2 * (ICON_SIZE + 5));
	}
}
