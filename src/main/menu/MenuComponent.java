package main.menu;

import javafx.animation.Timeline;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import exception.*;
import entity.property.Side;
import main.Main;
import main.SceneManager;

public class MenuComponent {
	public static final Image MUTE = new Image("image/MuteIcon.png");
	public static final Image UNMUTE = new Image("image/UnmuteIcon.png");
	public static final Image LEFT_ARROW = new Image("image/LeftArrow.png");
	
	private static final Media START_SOUND = new Media(ClassLoader.getSystemResource("sound/MenuStart.wav").toString());
	private static final Media LOOP_SOUND = new Media(ClassLoader.getSystemResource("sound/MenuLoop.wav").toString());
	private static final Media TRANSITION_SOUND = new Media(ClassLoader.getSystemResource("sound/MenuTransitionSound.wav").toString());
	
	private static final MenuComponent instance = new MenuComponent("");

	private MediaPlayer startMP;
	private MediaPlayer loopMP;
	private MediaPlayer transitionMP;
	
	private Pane backgroundPane;
	private Pane namePane;
	private Pane sidePane;
	private Canvas background;
	private Canvas text;
	private Canvas red;
	private Canvas blue;
	private Canvas moveBack;
	private Canvas mute;
	private String name;
	
	private Timeline mover;
	private int moveCount;
	
	public MenuComponent(String name) {
		this.name = name;
		
		// backgroundPane
		backgroundPane = new Pane();
		
		background = new Canvas(Main.SCREEN_SIZE + 300, Main.SCREEN_SIZE + 200);
		
		GraphicsContext gc = background.getGraphicsContext2D();
		gc.drawImage(new Image("image/MenuBackground.jpg"), 0, 0);
		
		Canvas logoBig = new Canvas(Main.SCREEN_SIZE, Main.SCREEN_SIZE);
		logoBig.setOpacity(0.08);
		
		gc = logoBig.getGraphicsContext2D();
		gc.drawImage(new Image("image/GameLogo.png"), - Main.SCREEN_SIZE / 10, Main.SCREEN_SIZE * 35 / 750, 
				Main.SCREEN_SIZE * 6 / 5, Main.SCREEN_SIZE * 2 / 5 * 900 / 1100);
		
		Canvas logoSmall = new Canvas(Main.SCREEN_SIZE, Main.SCREEN_SIZE);
		
		gc = logoSmall.getGraphicsContext2D();
		gc.drawImage(new Image("image/GameLogo.png"), Main.SCREEN_SIZE / 50, Main.SCREEN_SIZE / 10, 
				Main.SCREEN_SIZE * 24 / 25, Main.SCREEN_SIZE * 2 / 5 * 700 / 1100);
		
		backgroundPane.getChildren().addAll(background, logoBig, logoSmall);
		
		// namePane
		namePane = new Pane();
		
		text = new Canvas(Main.SCREEN_SIZE, Main.SCREEN_SIZE);
		text.setOpacity(0.8);
		name = "";
		drawName();
		
		mute = new Canvas(35, 35);
		mute.setTranslateY(Main.SCREEN_SIZE - 35);
		
		drawMute();
		
		mute.setOnMouseClicked(event -> {
			if(event.getButton() == MouseButton.PRIMARY) {
				SceneManager.setMuted(!SceneManager.isMuted());
			}
		});
		
		namePane.getChildren().addAll(text, mute);
		
		// sidePane
		sidePane = new Pane();
		
		red = new Canvas(203, 203);
		red.setTranslateX(2 * Main.SCREEN_SIZE);
		red.setTranslateY(Main.SCREEN_SIZE * 13 / 30);
		
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
		blue.setTranslateX(2 * Main.SCREEN_SIZE);
		blue.setTranslateY(Main.SCREEN_SIZE * 13 / 30);
		
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
		moveBack.setTranslateX(2 * Main.SCREEN_SIZE);
		moveBack.setTranslateY(Main.SCREEN_SIZE * 23 / 30);
		
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
		
		red.setOnMouseClicked(event -> MenuHandler.moveToGameScene(this.name, Side.RED));
		blue.setOnMouseClicked(event -> MenuHandler.moveToGameScene(this.name, Side.BLUE));
		moveBack.setOnMouseClicked(event -> MenuHandler.moveCanvasBack());
		
		sidePane.getChildren().addAll(red, blue, moveBack);
		
		new Thread(() -> {
			startMP = new MediaPlayer(START_SOUND);
			loopMP = new MediaPlayer(LOOP_SOUND);
			transitionMP = new MediaPlayer(TRANSITION_SOUND);
			
			startSound();
		}).start();
		
		moveBackground();
	}
	
	public void addCharacter(KeyEvent input) throws Exception {
		if(input.getCode() == KeyCode.BACK_SPACE) {
			if(name.length() == 0) {
				throw new DeleteNullException();
			} else {
				name = name.substring(0, name.length() - 1);
			}
		} else if(input.getText().length() == 1 && !input.getCode().isWhitespaceKey()) {
			if(name.length() >= 10) {
				throw new LongNameException();
			} else {
				name += input.getText();
			}
		} else {
			if(input.getText().length() >= 1) {
				throw new UnsupportedCharacterException(input.getText());
			} else {
				throw new UnsupportedCharacterException(input.getCode().getName());
			}
		}
		
		drawName();
	}
	
	public void drawName() {
		GraphicsContext gc = text.getGraphicsContext2D();
		
		gc.clearRect(Main.SCREEN_SIZE / 5, Main.SCREEN_SIZE / 7.5 * 4, 450, 100);
		
		gc.setFont(Font.font("Telugu MN", 30));
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.BASELINE);
		gc.setFill(Color.BLACK);
		gc.fillText("Enter Your Name", Main.SCREEN_SIZE / 2, Main.SCREEN_SIZE / 2);
		
		gc.setFill(Color.gray(0.75));
		gc.fillRoundRect((Main.SCREEN_SIZE - 450) / 2, Main.SCREEN_SIZE / 7.5 * 4, 450, Main.SCREEN_SIZE / 7.5, 20, 20);
		
		gc.setFont(Font.font("Telugu MN", 40));
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		gc.setFill(Color.BLACK);
		gc.fillText(name, Main.SCREEN_SIZE / 2, Main.SCREEN_SIZE * 3 / 5);
	}
	
	public void drawMute() {
		mute.getGraphicsContext2D().clearRect(0, 0, 35, 35);
		if(SceneManager.isMuted()) {
			mute.getGraphicsContext2D().drawImage(MUTE, 0, 0, 35, 35);
		} else {
			mute.getGraphicsContext2D().drawImage(UNMUTE, 0, 0, 35, 35);
		}
	}
	
	public void setErrorMessage(String error) {
		clearErrorMessage();
		
		GraphicsContext gc = text.getGraphicsContext2D();
		gc.setFont(Font.font("Telugu MN", 20));
		gc.setFill(Color.FIREBRICK);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.TOP);
		gc.fillText(error, Main.SCREEN_SIZE / 2, Main.SCREEN_SIZE * 7 / 10);
	}
	
	public void clearErrorMessage() {
		GraphicsContext gc = text.getGraphicsContext2D();
		gc.clearRect(0, Main.SCREEN_SIZE * 7 / 10, Main.SCREEN_SIZE, Main.SCREEN_SIZE * 3 / 10);
	}
	
	public void moveBackground() {
		mover = new Timeline(new KeyFrame(Duration.millis(15), event -> {
			moveCount = (moveCount + 1) % 200;
			background.setTranslateX(-209 * moveCount / 200.0);
			background.setTranslateY(-121 * moveCount / 200.0);
		}));
		mover.setCycleCount(Animation.INDEFINITE);
		mover.play();
	}
	
	public void stopBackground() {
		mover.stop();
	}
	
	public void startSound() {
		startMP.setMute(SceneManager.isMuted());
		loopMP.setMute(SceneManager.isMuted());
		startMP.setCycleCount(1);
		loopMP.setCycleCount(MediaPlayer.INDEFINITE);
		startMP.setOnEndOfMedia(() -> {
			loopMP.play();
		});
		startMP.play();
	}
	
	public void startTransitionSound() {
		transitionMP.setMute(SceneManager.isMuted());
		transitionMP.setCycleCount(1);
		transitionMP.play();
	}
	
	public void setMute(boolean isMuted) {
		startMP.setMute(isMuted);
		loopMP.setMute(isMuted);
		transitionMP.setMute(isMuted);
	}
	
	public void stopSound() {
		if(startMP != null) {
			startMP.stop();
		}
	
		if(loopMP != null) {
			loopMP.stop();
		}
		
	}
	
	public void resetName() {
		name = "";
		drawName();
		
		transitionMP = new MediaPlayer(TRANSITION_SOUND);
	}
	
	public void setMoveCount(int moveCount) {
		this.moveCount = moveCount;
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
