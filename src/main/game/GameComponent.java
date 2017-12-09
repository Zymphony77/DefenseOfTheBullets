package main.game;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

import buff.*;
import bot.*;
import entity.*;
import entity.bullet.*;
import entity.job.*;
import entity.property.*;
import entity.food.*;
import entity.tower.*;
import environment.*;
import main.Main;
import main.SceneManager;
import main.ranking.RankingComponent;
import skill.*;
import utility.*;

public class GameComponent {
	public static final double MAX_SIZE = 7500;
	public static final int GRID_SIZE = 25;
	public static final int GRID_NUMBER = Main.SCREEN_SIZE / GRID_SIZE;
	public static final int MAX_FOOD_COUNT = 750;
	public static final int MAX_TRACK_NUMBER = 3;
	
	public static final Media SHAKE_SOUND = new Media(ClassLoader.getSystemResource("sound/ShakeSound.mp3").toString());
	public static final Media[] GAME_SOUND;
	public static final Media[] RESULT_SOUND;
	
	private static final GameComponent instance = new GameComponent();
	
	private ArrayList<Novice> playerList;
	private ArrayList<Bot> botList;
	private ArrayList<Tower> towerList;
	private ArrayList<Bullet> bulletList;
	private ArrayList<Food> foodList;
	private ArrayList<Thread> threadList;
	private Canvas[] boundaryList;
	
	private Novice player;
	
	private int trackNumber;
	private MediaPlayer gameMP;
	private MediaPlayer shakeMP;
	private MediaPlayer resultMP;
	
	private Pane endPane;
	private BloodPane bloodPane;
	private ClassPane classPane;
	private BuffPane buffPane;
	private BuffPane debuffPane;
	private StatusPane statusPane;
	private SkillPane skillPane;
	private ExperienceBar expBar;
	private Minimap minimap;
	private Pane identityPane;
	private Pane hpBarPane;
	private Pane playerPane;
	private Pane towerPane;
	private Pane bulletPane;
	private Pane foodPane;
	private Pane boundaryPane;
	private Canvas grid;
	
	static {
		GAME_SOUND = new Media[MAX_TRACK_NUMBER];
		
		for(int i = 0; i < MAX_TRACK_NUMBER; ++i) {
			GAME_SOUND[i] = new Media(ClassLoader.getSystemResource("sound/Gameplay" + i + ".mp3").toString());
		}
		
		RESULT_SOUND = new Media[2];
		RESULT_SOUND[0] = new Media(ClassLoader.getSystemResource("sound/VictorySound.mp3").toString());
		RESULT_SOUND[1] = new Media(ClassLoader.getSystemResource("sound/DefeatSound.mp3").toString());
	}
	
	public GameComponent() {
		endPane = new Pane();
		classPane = new ClassPane();
		buffPane = new BuffPane();
		debuffPane = new BuffPane();
		statusPane = new StatusPane();
		skillPane = new SkillPane();
		expBar = new ExperienceBar();
		minimap = new Minimap();
		identityPane = new Pane();
		hpBarPane = new Pane();
		playerPane = new Pane();
		towerPane = new Pane();
		bulletPane = new Pane();
		foodPane = new Pane();
		boundaryPane = new Pane();
		
		playerList = new ArrayList<Novice>();
		botList = new ArrayList<Bot>();
		towerList = new ArrayList<Tower>();
		bulletList = new ArrayList<Bullet>();
		foodList = new ArrayList<Food>();
		threadList = new ArrayList<Thread>();
		
		grid = new Canvas(GRID_SIZE * (GRID_NUMBER + 4), GRID_SIZE * (GRID_NUMBER + 4));
		grid.setTranslateX(-2 * GRID_SIZE);
		grid.setTranslateY(-2 * GRID_SIZE);
		GraphicsContext gc = grid.getGraphicsContext2D();
		gc.setStroke(Color.gray(0.95));
		gc.setLineWidth(1);
		for(int i = 0; i < GRID_NUMBER + 4; ++i) {
			for(int j = 0; j < GRID_NUMBER + 4; ++j) {
				grid.getGraphicsContext2D().strokeRect(GRID_SIZE * i, GRID_SIZE * j, GRID_SIZE, GRID_SIZE);
			}
		}
		
		addBoundary();
		
		new Thread(() -> {
			trackNumber = -1;
			
			gameMP = new MediaPlayer(SHAKE_SOUND);
			shakeMP = new MediaPlayer(SHAKE_SOUND);
			resultMP = new MediaPlayer(SHAKE_SOUND);
		}).start();
	}
	
	private void addBoundary() {
		boundaryList = new Canvas[4];
		
		// top canvas
		boundaryList[0] = new Canvas(Main.SCREEN_SIZE, Main.SCREEN_SIZE / 2);
		boundaryList[0].getGraphicsContext2D().setFill(Color.gray(0.8));
		boundaryList[0].getGraphicsContext2D().fillRect(0, 0, 
				Main.SCREEN_SIZE, Main.SCREEN_SIZE / 2);
		
		// bottom canvas
		boundaryList[1] = new Canvas(Main.SCREEN_SIZE, Main.SCREEN_SIZE / 2);
		boundaryList[1].getGraphicsContext2D().setFill(Color.gray(0.8));
		boundaryList[1].getGraphicsContext2D().fillRect(0, 0, 
				Main.SCREEN_SIZE, Main.SCREEN_SIZE / 2);
		
		// left canvas
		boundaryList[2] = new Canvas(Main.SCREEN_SIZE / 2, Main.SCREEN_SIZE);
		boundaryList[2].getGraphicsContext2D().setFill(Color.gray(0.8));
		boundaryList[2].getGraphicsContext2D().fillRect(0, 0, 
				Main.SCREEN_SIZE / 2, Main.SCREEN_SIZE);
		
		// right canvas
		boundaryList[3] = new Canvas(Main.SCREEN_SIZE / 2, Main.SCREEN_SIZE);
		boundaryList[3].getGraphicsContext2D().setFill(Color.gray(0.8));
		boundaryList[3].getGraphicsContext2D().fillRect(0, 0, 
				Main.SCREEN_SIZE / 2, Main.SCREEN_SIZE);
		
		shiftBoundary(new Pair(MAX_SIZE / 2, MAX_SIZE / 2));
		
		boundaryPane.getChildren().addAll(boundaryList[0], boundaryList[1], boundaryList[2], boundaryList[3]);
	}
	
	public void shiftBoundary(Pair center) {
		boundaryList[0].setTranslateY(-center.second);
		boundaryList[1].setTranslateY(MAX_SIZE - center.second + Main.SCREEN_SIZE / 2);
		
		boundaryList[2].setTranslateX(-center.first);
		boundaryList[3].setTranslateX(MAX_SIZE - center.first + Main.SCREEN_SIZE / 2);
	}
	
	public static Pair spawnPoint(Side side) {
		Random rand = new Random();
		if(side == Side.BLUE)
			return new Pair(rand.nextDouble() * MAX_SIZE / 10.0, rand.nextDouble() * MAX_SIZE / 10.0);
		else
			return new Pair(MAX_SIZE - rand.nextDouble() * MAX_SIZE / 10.0, MAX_SIZE - rand.nextDouble() * MAX_SIZE / 10.0);
	}
	
	public void initialize(Side side, String name) {
		player = new Novice(spawnPoint(side), side, name);
		expBar.setName(name);
		expBar.setExperience(player.getExperience());
		addComponent(player);
		
		int[] bucket = new int[9];
		Random random = new Random();
		
		for(int i = 0; i < 9; i++) {
			int x;
			while(true) {
				x = random.nextInt(9);
				if(bucket[x] == 0) {
					bucket[x] = 1;
					++x;
					break;
				}
			}
			
			if((i + (side == Side.RED? 1: 0)) % 2 == 0) {
				addComponent(new Novice(spawnPoint(Side.RED), Side.RED, "Homemade AI #" + x));
			} else {
				addComponent(new Novice(spawnPoint(Side.BLUE), Side.BLUE, "Homemade AI #" + x));
			}
		}
		
		// Central
		addComponent(new Tower(new Pair(GameComponent.MAX_SIZE / 2.0, GameComponent.MAX_SIZE / 2.0), Side.NEUTRAL));
		// West
		addComponent(new Tower(new Pair(GameComponent.MAX_SIZE / 5.0, GameComponent.MAX_SIZE / 2.0), Side.NEUTRAL));
		// East
		addComponent(new Tower(new Pair(GameComponent.MAX_SIZE * 4.0 / 5.0, GameComponent.MAX_SIZE / 2.0), Side.NEUTRAL));
		// North
		addComponent(new Tower(new Pair(GameComponent.MAX_SIZE / 2.0, GameComponent.MAX_SIZE / 5.0), Side.NEUTRAL));
		// South
		addComponent(new Tower(new Pair(GameComponent.MAX_SIZE / 2.0, GameComponent.MAX_SIZE * 4.0 / 5.0), Side.NEUTRAL));
		
		minimap.drawViewBox();
		skillPane.setPlayer(player);
		statusPane.setPlayer(player);
		generateFood();
		
		for(Novice each: playerList) {
			each.drawIdentity();
			each.addBuff(new InvincibleBuff(each));
		}
		
		bloodPane = new BloodPane(side);
		
		playBGM();
	}
	
	public void generateFood() {
		if(foodList.size() == MAX_FOOD_COUNT) {
			return;
		}
		
		Random random = new Random();
		
		while(foodList.size() < MAX_FOOD_COUNT) {
			double posx;
			double posy;
			
			double x = random.nextDouble();
			double y = random.nextDouble();
			
			// Probability linearly increases towards center (consider each dimension individually)
			// Minimum probability (near border) is half of maximum probability (near center) 
			if(x < 0.5) {
				posx = -2 + Math.sqrt(1 + 6*x);
			} else {
				posx = 2 - Math.sqrt(7 - 6*x);
			}
			
			if(y < 0.5) {
				posy = -2 + Math.sqrt(1 + 6*y);
			} else {
				posy = 2 - Math.sqrt(7 - 6*y);
			}
			
			posx = (posx + 1) * MAX_SIZE / 2;
			posy = (posy + 1) * MAX_SIZE / 2;
			
			Food food = new Food(new Pair(posx, posy));
			
			if(collideWithList(food, playerList)) {
				continue;
			}
			
			if(collideWithList(food, bulletList)) {
				continue;
			}
			
			if(collideWithList(food, towerList)) {
				continue;
			}
			
			if(collideWithList(food, foodList)) {
				continue;
			}
			
			addComponent(food);
		}
	}
	
	private boolean collideWithList(Entity entity, ArrayList<? extends Entity> list) {
		for(Entity each: list) {
			if(each.isCollided(entity)) {
				return true;
			}
		}
		return false;
	}
	
	public void addComponent(Object component) {
		if(component instanceof Novice) {
			playerList.add((Novice) component);
			playerPane.getChildren().add(((Entity) component).getCanvas());
			
			if(player == component) {
				return;
			}
			
			if(component instanceof Tank) {
				botList.add(new BotTank((Tank) component));
			} else if(component instanceof Magician) {
				botList.add(new BotMagician((Magician) component));
			} else if(component instanceof Ranger) {
				botList.add(new BotRanger((Ranger) component));
			} else {
				botList.add(new BotNovice((Novice) component));
			}
			
			((Novice) component).setPlayer(false);
		} else if(component instanceof Bullet) {
			bulletList.add((Bullet) component);
			bulletPane.getChildren().add(((Entity) component).getCanvas());
		} else if(component instanceof HpBar) {
			hpBarPane.getChildren().add(((HpBar) component).getCanvas()); 
		} else if(component instanceof Tower) {
			towerList.add((Tower) component);
			towerPane.getChildren().add(((Entity) component).getCanvas());
		} else if(component instanceof Food) {
			foodList.add((Food) component);
			foodPane.getChildren().add(((Food) component).getCanvas());
		} else if(component instanceof Identity) {
			identityPane.getChildren().add(((Identity) component).getCanvas());
		}
	}
	
	public void removeComponent(Object component) {
		if(component == null) {
			return;
		}
		
		if(component instanceof Novice) {
			playerList.remove((Novice) component);
			playerPane.getChildren().remove(((Entity) component).getCanvas());
		} else if(component instanceof Bullet) {
			bulletList.remove((Bullet) component);
			bulletPane.getChildren().remove(((Entity) component).getCanvas());
		} else if(component instanceof HpBar) {
			hpBarPane.getChildren().remove(((HpBar) component).getCanvas()); 
		} else if(component instanceof Food) {
			foodList.remove((Food) component);
			foodPane.getChildren().remove(((Food) component).getCanvas());
		} else if(component instanceof Identity) {
			identityPane.getChildren().remove(((Identity) component).getCanvas());
		}
	}
	
	public void setEnding(String mode) {		// Victory / Defeat
		endPane.getChildren().clear();
		
		Canvas icon = new Canvas(Main.SCREEN_SIZE, Main.SCREEN_SIZE);
		icon.setScaleX(0.0);
		icon.setScaleY(0.0);
		icon.setOpacity(0);
		
		endPane.getChildren().add(icon);
		
		GraphicsContext gc = icon.getGraphicsContext2D();
		gc.translate(Main.SCREEN_SIZE / 2, Main.SCREEN_SIZE / 2);
		gc.drawImage(new Image("image/" + mode + ".png"), -300, -300, 600, 600);
		
		playResultSound(mode);
		
		Timeline zoom = new Timeline(new KeyFrame(Duration.millis(10), event -> {
			icon.setScaleX(icon.getScaleX() + 0.01);
			icon.setScaleY(icon.getScaleY() + 0.01);
			icon.setOpacity(icon.getOpacity() + 0.01);
		}));
		zoom.setCycleCount(100);
		zoom.setOnFinished(event -> {
			Timeline delay = new Timeline(new KeyFrame(Duration.seconds(3.5), e1 -> {}));
			delay.setCycleCount(1);
			delay.setOnFinished(e2 -> {
				gc.setFont(Font.font("Telugu MN", 20));
				gc.setTextAlign(TextAlignment.CENTER);
				gc.fillText("Press [ENTER] to continue", 0, Main.SCREEN_SIZE / 2 - 150);
				RankingComponent.getInstance().reset();
				GameHandler.allowChangeScene();
			});
			delay.playFromStart();
		});
		zoom.playFromStart();
	}
	
	public void playBGM() {
		int tmp;
		while(true) {
			tmp = (new Random()).nextInt(MAX_TRACK_NUMBER);
			if(trackNumber != tmp) {
				trackNumber = tmp;
				break;
			}
		}
		
		gameMP = new MediaPlayer(GAME_SOUND[trackNumber]);
		gameMP.setMute(SceneManager.isMuted());
		gameMP.setCycleCount(1);
		gameMP.setOnEndOfMedia(() -> {
			playBGM();
		});
		gameMP.play();
	}
	
	public void playShakeSound() {
		shakeMP = new MediaPlayer(SHAKE_SOUND);
		shakeMP.setMute(SceneManager.isMuted());
		shakeMP.setCycleCount(1);
		shakeMP.play();
	}
	
	public void playResultSound(String mode) {
		resultMP = new MediaPlayer(RESULT_SOUND[mode.equals("Victory")? 0: 1]);
		resultMP.setMute(SceneManager.isMuted());
		resultMP.setCycleCount(1);
		resultMP.play();
	}
	
	public void stopBGM() {
		gameMP.stop();
	}
	
	public void setMute(boolean isMuted) {
		gameMP.setMute(isMuted);
		shakeMP.setMute(isMuted);
		resultMP.setMute(isMuted);
	}
	
	public void stopSound() {
		stopBGM();
		
		shakeMP.stop();
		resultMP.stop();
	}
	
	public void reset() {
		endPane = new Pane();
		classPane = new ClassPane();
		buffPane = new BuffPane();
		debuffPane = new BuffPane();
		statusPane = new StatusPane();
		skillPane = new SkillPane();
		expBar = new ExperienceBar();
		minimap = new Minimap();
		identityPane = new Pane();
		hpBarPane = new Pane();
		playerPane = new Pane();
		towerPane = new Pane();
		bulletPane = new Pane();
		foodPane = new Pane();
		boundaryPane = new Pane();
		
		playerList = new ArrayList<Novice>();
		botList = new ArrayList<Bot>();
		towerList = new ArrayList<Tower>();
		bulletList = new ArrayList<Bullet>();
		foodList = new ArrayList<Food>();
		threadList = new ArrayList<Thread>();
		
		grid = new Canvas(GRID_SIZE * (GRID_NUMBER + 4), GRID_SIZE * (GRID_NUMBER + 4));
		grid.setTranslateX(-2 * GRID_SIZE);
		grid.setTranslateY(-2 * GRID_SIZE);
		GraphicsContext gc = grid.getGraphicsContext2D();
		gc.setStroke(Color.gray(0.95));
		gc.setLineWidth(1);
		for(int i = 0; i < GRID_NUMBER + 4; ++i) {
			for(int j = 0; j < GRID_NUMBER + 4; ++j) {
				grid.getGraphicsContext2D().strokeRect(GRID_SIZE * i, GRID_SIZE * j, GRID_SIZE, GRID_SIZE);
			}
		}
		
		addBoundary();
	}
	
	public static GameComponent getInstance() {
		return instance;
	}
	
	public void setPlayer(Novice player) {
		this.player = player;
	}
	
	public Pair getShift() {
		return player.getRefPoint();
	}
	
	public Novice getPlayer() {
		return player;
	}
	
	public ArrayList<Novice> getPlayerList() {
		return playerList;
	}
	
	public ArrayList<Bot> getBotList() {
		return botList;
	}
	
	public ArrayList<Tower> getTowerList() {
		return towerList;
	}
	
	public ArrayList<Bullet> getBulletList() {
		return bulletList;
	}
	
	public ArrayList<Food> getFoodList() {
		return foodList;
	}
	
	public ArrayList<Thread> getThreadList() {
		return threadList;
	}
	
	public Pane getEndPane() {
		return endPane;
	}
	
	public BloodPane getBloodPane() {
		return bloodPane;
	}
	
	public BuffPane getBuffPane() {
		return buffPane;
	}
	
	public BuffPane getDebuffPane() {
		return debuffPane;
	}
	
	public StatusPane getStatusPane() {
		return statusPane;
	}
	
	public SkillPane getSkillPane() {
		return skillPane;
	}
	
	public ClassPane getClassPane() {
		return classPane;
	}
	
	public ExperienceBar getExperienceBar() {
		return expBar;
	}
	
	public Minimap getMinimap() {
		return minimap;
	}
	
	public Pane getIdentityPane() {
		return identityPane;
	}
	
	public Pane getHpBarPane() {
		return hpBarPane;
	}
	
	public Pane getPlayerPane() {
		return playerPane;
	}
	
	public Pane getTowerPane() {
		return towerPane;
	}
	
	public Pane getBulletPane() {
		return bulletPane;
	}
	
	public Pane getFoodPane() {
		return foodPane;
	}
	
	public Pane getBoundaryPane() {
		return boundaryPane;
	}
	
	public Canvas[] getBoundaryList() {
		return boundaryList;
	}
	
	public Canvas getGrid() {
		return grid;
	}
}
