package job;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import utility.*;

public class Novice extends Entity implements Shootable {
	private static double MAX_HP = 100;
	private static double SPEED = 5;
	
	public Novice(Pair refPoint, boolean hpBarVisible) {
		super(refPoint, MAX_HP, 0, SPEED, hpBarVisible);
		
		canvas.setWidth(70);
		canvas.setHeight(60);
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		gc.setFill(Color.DARKGRAY);
		gc.fillRect(15, 10, 25, 10);
		gc.setFill(Color.GRAY);
		gc.fillOval(0, 0, 30, 30);
	}
	
	public void shoot() {
		
	}
}
