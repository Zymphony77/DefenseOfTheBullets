package environment;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;

import main.game.*;
import utility.*;

public class ClassIcon extends Pane {
	public static final double RADIUS = 10;
	public static final int ICON_SIZE = 35;
	
	private Job job;
	private Canvas icon;
	private int position;
	
	public ClassIcon(Job job, int position) {
		this.job = job;
		
		icon = new Canvas(ICON_SIZE, ICON_SIZE);
		
		icon.setOpacity(0.65);
		
		this.setPosition(position);
		
		GraphicsContext gc = icon.getGraphicsContext2D();
		
		if(job == Job.TANK) {
			gc.setFill(Color.DARKGRAY);
			gc.fillRect(3, 12, 3*RADIUS-2, 5);
			gc.setFill(Color.GRAY);
			gc.fillOval(7, 5, 2*RADIUS, 2*RADIUS);
		}
		else if(job == Job.MAGICIAN) {
			gc.setFill(Color.DARKGRAY);
			gc.fillRect(RADIUS, 12, 2*RADIUS, 5);
			gc.setFill(Color.MEDIUMPURPLE);
			gc.fillOval(5, 5, 2*RADIUS, 2*RADIUS);
		}
		else if(job == Job.RANGER) {
			gc.setFill(Color.DARKGRAY);
			gc.fillRect(RADIUS, 12, 2*RADIUS, 5);
			gc.setFill(Color.DARKSEAGREEN);
			gc.fillOval(5, 5, 2*RADIUS, 2*RADIUS);
		}
		
		gc.setFill(Color.BLACK);
		gc.setFont(new Font(8));
		gc.fillText(job.toString(), 7.5, 32);
		gc.strokeRect(0, 0, icon.getWidth(), icon.getHeight());
		
		icon.setOnMouseClicked(event -> {
			if(event.getButton() == MouseButton.PRIMARY) {
				GameHandler.changeClass(GameComponent.getInstance().getPlayer(), job);
			}
		});
		
		getChildren().addAll(icon);
	}
	
	public void update() {
		
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
		return new Pair(10 + (ICON_SIZE + 3) * (position), 
				10 + 2 * (ICON_SIZE + 5));
	}
}
