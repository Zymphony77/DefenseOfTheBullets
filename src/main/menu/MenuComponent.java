package main.menu;

import entity.property.Side;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import main.Main;
import main.SceneManager;

public class MenuComponent {
	public static final Image LEFT_ARROW = new Image("resource/image/LeftArrow.png");
	
	private static final MenuComponent instance = new MenuComponent("");
	
	private Pane backgroundPane;
	private Pane namePane;
	private Pane sidePane;
	private Canvas text;
	private Canvas red;
	private Canvas blue;
	private Canvas moveBack;
	private String name;

	public MenuComponent(String name) {
		this.name = name;
		
		// backgroundPane
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
		
		// namePane
		namePane = new Pane();
		
		text = new Canvas(Main.SCREEN_SIZE, Main.SCREEN_SIZE);
		setName("");
		
		namePane.getChildren().add(text);
		
		// sidePane
		sidePane = new Pane();
		
		red = new Canvas(203, 203);
		red.setTranslateX(Main.SCREEN_SIZE);
		red.setTranslateY(325);
		
		gc = red.getGraphicsContext2D();
		gc.setFill(Color.rgb(255, 180, 155));
		gc.fillRoundRect(0, 0, 200, 200, 50, 50);
		gc.setFill(Color.DIMGRAY);
		gc.fillRect(100, 95, 40, 10);
		gc.setFill(Color.ORANGERED);
		gc.fillOval(75, 75, 50, 50);
		gc.setStroke(Color.gray(0.4));
		gc.setLineWidth(6);
		gc.strokeRoundRect(3, 3, 197, 197, 44, 44);
		
		blue = new Canvas(203, 203);
		blue.setTranslateX(Main.SCREEN_SIZE);
		blue.setTranslateY(325);
		
		gc = blue.getGraphicsContext2D();
		gc.setFill(Color.rgb(130, 220, 255));
		gc.fillRoundRect(0, 0, 200, 200, 50, 50);
		gc.setFill(Color.DIMGRAY);
		gc.fillRect(100, 95, 40, 10);
		gc.setFill(Color.CORNFLOWERBLUE);
		gc.fillOval(75, 75, 50, 50);
		gc.setStroke(Color.gray(0.4));
		gc.setLineWidth(6);
		gc.strokeRoundRect(3, 3, 197, 197, 44, 44);
		
		moveBack = new Canvas(100, 100);
		moveBack.setTranslateX(Main.SCREEN_SIZE);
		moveBack.setTranslateY(575);
		
		gc = moveBack.getGraphicsContext2D();
		gc.setFill(Color.gray(0.6));
		gc.fillOval(0, 0, 75, 75);
		gc.setFill(Color.DIMGRAY);
		gc.drawImage(LEFT_ARROW, 7, 27, 60, 20);
		gc.setStroke(Color.DIMGRAY);
		
		red.setOnMouseEntered(event -> MenuHandler.drawBorder(red, Color.ORANGERED));
		blue.setOnMouseEntered(event -> MenuHandler.drawBorder(blue, Color.CORNFLOWERBLUE));
		moveBack.setOnMouseEntered(event -> MenuHandler.drawLightButtonBackground());
		
		red.setOnMouseExited(event -> MenuHandler.undrawBorder(red));
		blue.setOnMouseExited(event -> MenuHandler.undrawBorder(blue));
		moveBack.setOnMouseExited(event -> MenuHandler.drawDarkButtonBackground());
		
		red.setOnMouseClicked(event -> SceneManager.setGameScene(this.name, Side.RED));
		blue.setOnMouseClicked(event -> SceneManager.setGameScene(this.name, Side.BLUE));
		moveBack.setOnMouseClicked(event -> MenuHandler.moveCanvasBack());
		
		sidePane.getChildren().addAll(red, blue, moveBack);
	}
	
	public void setName(String name) {
		this.name = name;
		
		GraphicsContext gc = text.getGraphicsContext2D();
		
		gc.clearRect(150, 400, 450, 100);
		
		gc.setFont(Font.font("Telugu MN", 30));
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.BASELINE);
		gc.fillText("Enter Your Name", 375, 380);
		
		gc.setFill(Color.gray(0.925));
		gc.fillRoundRect(150, 400, Main.SCREEN_SIZE - 2*150, 100, 20, 20);
		
		gc.setFont(Font.font("Telugu MN", 40));
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.setFill(Color.BLACK);
		gc.fillText(name, 375, 450);
	}
	
	public static MenuComponent getInstance() {
		return instance;
	}
	
	public Pane getBackgroundPane() {
		return backgroundPane;
	}
	
	public Pane getNamePane() {
		return namePane;
	}
	
	public Pane getSidePane() {
		return sidePane;
	}
	
	public Canvas getRed() {
		return red;
	}
	
	public Canvas getBlue() {
		return blue;
	}
	
	public Canvas getMoveBack() {
		return moveBack;
	}
	
	public String getName() {
		return name;
	}
}
