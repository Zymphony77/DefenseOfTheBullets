package main.ranking;

import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Comparator;

import main.Main;
import main.game.GameComponent;
import entity.job.*;
import entity.property.*;
import utility.*;

public class RankingComponent {
	private static final class PlayerWithName {
		public static final double ICON_SIZE = Main.SCREEN_SIZE / 30.0;
		public static final HashMap<Job, Image> ICON = new HashMap<Job, Image>();
		
		private Novice player;
		private String name;
		
		static {
			ICON.put(Job.NOVICE, new Image("image/NoviceIcon.png"));
			ICON.put(Job.TANK, new Image("image/TankIcon.png"));
			ICON.put(Job.MAGICIAN, new Image("image/MagicianIcon.png"));
			ICON.put(Job.RANGER, new Image("image/RangerIcon.png"));
		}
		
		public PlayerWithName(Novice player, String name) {
			this.player = player;
			this.name = name;
		}
		
		public Novice getPlayer() {
			return player;
		}
		
		public int getScore() {
			return 2 * player.getKill() - 3 * player.getDeath();
		}
		
		public String getName() {
			return name;
		}
	}
	
	private static final RankingComponent instance = new RankingComponent();
	
	private Side winnerSide;
	private Pane backgroundPane;
	private Pane rankingPane;
	private Canvas background;
	
	private ArrayList<PlayerWithName> playerList;
	
	private Timeline mover;
	private int moveCount;
	
	public RankingComponent() {
		
		winnerSide = GameComponent.getInstance().getTowerList().get(0).getSide();
		
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
		
		rankingPane = new Pane();
		playerList = new ArrayList<PlayerWithName>();
		calculateRanking();
		drawRanking();
	}
	
	public void calculateRanking() {
		int cnt = 1;
		playerList.clear();
		
		for(Novice player: GameComponent.getInstance().getPlayerList()) {
			playerList.add(new PlayerWithName(player, player.getName()));
		}
		
		playerList.sort(new Comparator<PlayerWithName>() {
			@Override
			public int compare(PlayerWithName player1, PlayerWithName player2) {
				if(player1.getPlayer().getLevel() > player2.getPlayer().getLevel()) {
					return -1;
				} else if(player1.getPlayer().getLevel() < player2.getPlayer().getLevel()) {
					return 1;
				}  else if(player1.getScore() > player2.getScore()) {
					return -1;
				} else if(player1.getScore() < player2.getScore()) {
					return 1;
				} else if(player1.getPlayer().getKill() > player2.getPlayer().getKill()) {
					return -1;
				} else if(player1.getPlayer().getKill() < player2.getPlayer().getKill()) {
					return 1;
				} else if(player1.getPlayer().getDeath() < player2.getPlayer().getDeath()) {
					return -1;
				} else if(player1.getPlayer().getDeath() > player2.getPlayer().getDeath()) {
					return 1;
				} else if(player1.getPlayer().getSide() != player2.getPlayer().getSide()) {
					if(player1.getPlayer().getSide() == winnerSide) {
						return -1;
					} else {
						return 1;
					}
				}
				return 0;
			}
		});
	}
	
	public void drawRanking() {
		Canvas ranking = new Canvas(Main.SCREEN_SIZE, Main.SCREEN_SIZE);
		Canvas text = new Canvas(Main.SCREEN_SIZE, Main.SCREEN_SIZE);
		
		ranking.setOpacity(0.7);
		
		// Background
		GraphicsContext gc = ranking.getGraphicsContext2D();
		
		gc.setFill(Color.rgb(30, 30, 30));
		gc.fillRect(Main.SCREEN_SIZE / 20, Main.SCREEN_SIZE * 3 / 8.0, Main.SCREEN_SIZE * 9 / 10.0, Main.SCREEN_SIZE / 15.0);
		
		for(int i = 0; i < playerList.size(); ++i) {
			if(playerList.get(i).getPlayer().getSide() == Side.BLUE) {
				gc.setFill(Color.rgb(23, 74, 109));
			} else {
				gc.setFill(Color.rgb(78, 15, 15));
			}
			gc.fillRect(Main.SCREEN_SIZE / 20.0, Main.SCREEN_SIZE * 5 / 11.0 + (3 + Main.SCREEN_SIZE / 24.0) * i, 
					Main.SCREEN_SIZE * 9 / 10.0, Main.SCREEN_SIZE / 24.0);
		}
		
		//Table
		gc = text.getGraphicsContext2D();
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		
		//Table header
		gc.setFont(Font.font("Telugu MN", FontWeight.LIGHT, 20));
		gc.setFill(Color.gray(0.85));
		gc.fillText("#", Main.SCREEN_SIZE / 10.0, Main.SCREEN_SIZE * 49 / 120.0);
		gc.fillText("Job", Main.SCREEN_SIZE / 5.0, Main.SCREEN_SIZE * 49 / 120.0);
		gc.fillText("Name", Main.SCREEN_SIZE * 4 / 10.0, Main.SCREEN_SIZE * 49 / 120.0);
		gc.fillText("Lv.", Main.SCREEN_SIZE * 9 / 15.0, Main.SCREEN_SIZE * 49 / 120.0);
		gc.fillText("Kill", Main.SCREEN_SIZE * 11 / 15.0, Main.SCREEN_SIZE * 49 / 120.0);
		gc.fillText("Death", Main.SCREEN_SIZE * 13 / 15.0, Main.SCREEN_SIZE * 49 / 120.0);
		
		// Table Content
		gc.setFont(Font.font("Telugu MN", FontWeight.EXTRA_LIGHT, 16));
		for(int i = 0; i < playerList.size(); ++i) {
			double y = Main.SCREEN_SIZE * 5 / 11.0 + (3 + Main.SCREEN_SIZE / 24.0) * i + Main.SCREEN_SIZE / 48.0;
			gc.fillText("" + (i + 1), Main.SCREEN_SIZE / 10.0, y);
			gc.drawImage(PlayerWithName.ICON.get(playerList.get(i).getPlayer().getJob()), 
					Main.SCREEN_SIZE / 5.0 - PlayerWithName.ICON_SIZE / 2, y - Main.SCREEN_SIZE / 60.0, 
					PlayerWithName.ICON_SIZE, PlayerWithName.ICON_SIZE);
			gc.fillText(playerList.get(i).getName(), Main.SCREEN_SIZE * 4 / 10.0, y);
			gc.fillText("" + playerList.get(i).getPlayer().getLevel(), Main.SCREEN_SIZE * 9 / 15.0, y);
			gc.fillText("" + playerList.get(i).getPlayer().getKill(), Main.SCREEN_SIZE * 11 / 15.0, y);
			gc.fillText("" + playerList.get(i).getPlayer().getDeath(), Main.SCREEN_SIZE * 13 / 15.0, y);
		}
		
		// Footer
		gc.setFill(Color.BLACK);
		gc.setFont(Font.font("Telugu MN", 20));
		gc.setTextAlign(TextAlignment.CENTER);
		gc.fillText("Press [ENTER] to continue", Main.SCREEN_SIZE / 2, Main.SCREEN_SIZE * 24 / 25.0);
		
		rankingPane.getChildren().addAll(ranking, text);
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
	
	public void reset() {
		playerList.clear();
		rankingPane.getChildren().clear();
		calculateRanking();
		drawRanking();
	}
	
	public static RankingComponent getInstance() {
		return instance;
	}
	
	public Pane getBackgroundPane() {
		return backgroundPane;
	}
	
	public Pane getRankingPane() {
		return rankingPane;
	}
	
	public int getMoveCount() {
		return moveCount;
	}
}
