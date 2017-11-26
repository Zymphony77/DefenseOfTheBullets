package main.scoreboard;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import main.Main;

public class ScoreboardComponent {
	private static final ScoreboardComponent instance = new ScoreboardComponent();
	
	private Pane backgroundPane;
	private Pane victoryPane;
	private Pane defeatPane;
	
	public ScoreboardComponent() {
		backgroundPane = new Pane();
		
		Canvas background = new Canvas(Main.SCREEN_SIZE, Main.SCREEN_SIZE);
		
		GraphicsContext gc = background.getGraphicsContext2D();
		gc.setFill(Color.gray(0.7));
		gc.fillRect(0, 0, Main.SCREEN_SIZE, Main.SCREEN_SIZE);
		gc.setFill(Color.DIMGRAY);
		
		Canvas logoBig = new Canvas(Main.SCREEN_SIZE, Main.SCREEN_SIZE);
		logoBig.setOpacity(0.08);
		
		gc = logoBig.getGraphicsContext2D();
		gc.drawImage(new Image("resource/image/GameLogo.png"), Main.SCREEN_SIZE / 2 - 450, 35, 900, 300 * 900 / 1100);
		
		Canvas logoSmall = new Canvas(Main.SCREEN_SIZE, Main.SCREEN_SIZE);
		
		gc = logoSmall.getGraphicsContext2D();
		gc.drawImage(new Image("resource/image/GameLogo.png"), Main.SCREEN_SIZE / 2 - 350, 75, 700, 300 * 700 / 1100);
		
		backgroundPane.getChildren().addAll(background, logoBig, logoSmall);
		
		victoryPane = new Pane();
		defeatPane = new Pane();
		
//		drawVictoryPane();
//		drawDefeatPane();
	}
	
	public static ScoreboardComponent getInstance() {
		return instance;
	}
	
	public Pane getBackgroundPane() {
		return backgroundPane;
	}
	
	public Pane getVictoryPane() {
		return victoryPane;
	}
	
	public Pane getDefeatPane() {
		return defeatPane;
	}
}
