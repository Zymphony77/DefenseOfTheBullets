package main.menu;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import main.Main;
import utility.*;

public class MenuHandler {
	private static int shift;
	private static boolean onSideScreen;
	
	static {
		shift = 0;
		onSideScreen = false;
	}
	
	public static void keyPressed(KeyEvent event) {
		if(onSideScreen) {
			return;
		}
		
		if(event.getCode() == KeyCode.BACK_SPACE) {
			if(MenuComponent.getInstance().getName().length() > 0) {
				MenuComponent.getInstance().setName(MenuComponent.getInstance().getName().substring(0, 
						MenuComponent.getInstance().getName().length() - 1));
			}
		} else if(event.getCode() == KeyCode.ENTER) {
			if(MenuComponent.getInstance().getName().length() > 0) {
				onSideScreen = true;
				Timeline shifter = new Timeline(new KeyFrame(Duration.millis(0.5), timelineEvent -> {
					--shift;
					shiftCanvas();
				}));
				shifter.setCycleCount(Main.SCREEN_SIZE);
				shifter.playFromStart();
			}
		} else if(event.getText().length() == 1 && !event.getCode().isWhitespaceKey()) {
			if(MenuComponent.getInstance().getName().length() < 10) {
				MenuComponent.getInstance().setName(MenuComponent.getInstance().getName() + event.getText());
			}
		}
	}
	
	public static void moveCanvasBack() {
		if(onSideScreen) {
			onSideScreen = false;
			
			Timeline shifter = new Timeline(new KeyFrame(Duration.millis(0.5), timelineEvent -> {
				++shift;
				shiftCanvas();
			}));
			shifter.setCycleCount(Main.SCREEN_SIZE);
			shifter.playFromStart();
		}
	}
	
	public static void shiftCanvas() {
		for(Node canvas: MenuComponent.getInstance().getNamePane().getChildren()) {
			if(canvas instanceof Canvas) {
				((Canvas) canvas).setTranslateX(shift);
			}
		}
		
		MenuComponent.getInstance().getRed().setTranslateX(Main.SCREEN_SIZE + 125 + shift);
		MenuComponent.getInstance().getBlue().setTranslateX(Main.SCREEN_SIZE + 425 + shift);
		MenuComponent.getInstance().getMoveBack().setTranslateX(Main.SCREEN_SIZE + 337.5 + shift);
	}
	
	public static void drawBorder(Canvas canvas, Color color) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		gc.setStroke(color);
		gc.strokeRoundRect(3, 3, 197, 197, 44, 44);
	}
	
	public static void undrawBorder(Canvas canvas) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		gc.setStroke(Color.gray(0.4));
		gc.strokeRoundRect(3, 3, 197, 197, 44, 44);
	}
}
