package environment;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import buff.*;
import utility.*;

public class BuffIcon extends Pane {
	public static final int ICON_SIZE = 35;
	
	private Buff buff;
	private Canvas icon;
	private Canvas duration;
	private int position;
	
	public BuffIcon(Buff buff, int position) {
		this.buff = buff;
		
		icon = new Canvas(ICON_SIZE, ICON_SIZE);
		duration = new Canvas(ICON_SIZE, ICON_SIZE);
		
		icon.setOpacity(0.65);
		duration.setOpacity(0.65);
		
		this.setPosition(position);
		
		GraphicsContext gc = icon.getGraphicsContext2D();
		gc.drawImage(buff.getImage(), 0, 0, ICON_SIZE, ICON_SIZE);
		
		gc = duration.getGraphicsContext2D();
		if(buff.getBuffType() == BuffType.BUFF) {
			gc.setStroke(Color.GREEN);
			gc.setFill(Color.LIGHTGREEN);
		} else {
			gc.setStroke(Color.DARKRED);
			gc.setFill(Color.INDIANRED);
		}
		gc.fillRoundRect(0, 0, ICON_SIZE, ICON_SIZE, 5, 5);
		gc.strokeRoundRect(0, 0, ICON_SIZE, ICON_SIZE, 5, 5);
		
		getChildren().addAll(duration, icon);
	}
	
	public void update() {
		if(buff instanceof Expirable) {
			int maxDuration = ((Expirable) buff).getMaxDuration();
			int remainingDuration = maxDuration - ((Expirable) buff).getDuration();
			
			duration.getGraphicsContext2D().clearRect(0, 0, ICON_SIZE, 
					(double) remainingDuration / maxDuration * ICON_SIZE);
		}
	}
	
	public void clear() {
		icon.getGraphicsContext2D().clearRect(0, 0, ICON_SIZE, ICON_SIZE);
		duration.getGraphicsContext2D().clearRect(0, 0, ICON_SIZE, ICON_SIZE);
	}
	
	public void setPosition(int position) {
		this.position = position;
		
		icon.setTranslateX(getShift().first);
		icon.setTranslateY(getShift().second);
		
		duration.setTranslateX(getShift().first);
		duration.setTranslateY(getShift().second);
	}
	
	public Pair getShift() {
		return new Pair(3 + (ICON_SIZE + 3) * (position), 
				3 + (buff.getBuffType() == BuffType.BUFF? 0: 1) * (ICON_SIZE + 4));
	}
	
	public Buff getBuff() {
		return buff;
	}
}
