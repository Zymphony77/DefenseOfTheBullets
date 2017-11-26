package main.ranking;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Comparator;

import main.Main;
import main.game.GameComponent;
import entity.job.*;
import entity.property.*;

public class RankingComponent {
	private static final class PlayerWithName {
		private Novice player;
		private String name;
		
		public PlayerWithName(Novice player, String name) {
			this.player = player;
			this.name = name;
		}
		
		public Novice getPlayer() {
			return player;
		}
		
		public String getName() {
			return name;
		}
	}
	
	private static final RankingComponent instance = new RankingComponent();
	
	private Side winnerSide;
	private Pane backgroundPane;
	private Pane rankingPane;
	
	private ArrayList<PlayerWithName> winnerList;
	private ArrayList<PlayerWithName> loserList;
	
	public RankingComponent() {
		winnerSide = GameComponent.getInstance().getTowerList().get(0).getSide();
		
		backgroundPane = new Pane();
		
		Canvas background = new Canvas(Main.SCREEN_SIZE, Main.SCREEN_SIZE);
		
		GraphicsContext gc = background.getGraphicsContext2D();
		gc.setFill(Color.gray(0.7));
		gc.fillRect(0, 0, Main.SCREEN_SIZE, Main.SCREEN_SIZE);
		
		Canvas logoBig = new Canvas(Main.SCREEN_SIZE, Main.SCREEN_SIZE);
		logoBig.setOpacity(0.08);
		
		gc = logoBig.getGraphicsContext2D();
		gc.drawImage(new Image("resource/image/GameLogo.png"), Main.SCREEN_SIZE / 2 - 450, 35, 900, 300 * 900 / 1100);
		
		Canvas logoSmall = new Canvas(Main.SCREEN_SIZE, Main.SCREEN_SIZE);
		
		gc = logoSmall.getGraphicsContext2D();
		gc.drawImage(new Image("resource/image/GameLogo.png"), Main.SCREEN_SIZE / 2 - 350, 75, 700, 300 * 700 / 1100);
		
		backgroundPane.getChildren().addAll(background, logoBig, logoSmall);
		
		rankingPane = new Pane();
		winnerList = new ArrayList<PlayerWithName>();
		loserList = new ArrayList<PlayerWithName>();
		calculateRanking();
		drawRanking();
	}
	
	public void calculateRanking() {
		int cnt = 1;
		for(Novice player: GameComponent.getInstance().getPlayerList()) {
			if(player.getSide() == winnerSide) {
				winnerList.add(new PlayerWithName(player, player.isPlayer()? GameComponent.getInstance().getPlayerName(): "Homemade AI #" + (cnt++)));
			} else {
				loserList.add(new PlayerWithName(player, player.isPlayer()? GameComponent.getInstance().getPlayerName(): "Homemade AI #" + (cnt++)));
			}
		}
		
		Comparator<PlayerWithName> comparator = new Comparator<PlayerWithName>() {
			@Override
			public int compare(PlayerWithName player1, PlayerWithName player2) {
				if(player1.getPlayer().getLevel() < player2.getPlayer().getLevel()) {
					return 1;
				} else if(player1.getPlayer().getLevel() > player2.getPlayer().getLevel()) {
					return -1;
				} else {
					return 0;
				}
			}
		};
		
		winnerList.sort(comparator);
		loserList.sort(comparator);
	}
	
	public void drawRanking() {
		Canvas ranking = new Canvas(Main.SCREEN_SIZE, Main.SCREEN_SIZE);
		
		GraphicsContext gc = ranking.getGraphicsContext2D();
		
		gc.setFill(Color.DIMGRAY);
		gc.fillRect(20, 295, 710, 390);
		
		if(winnerSide == Side.BLUE) {
			gc.setFill(Color.rgb(130, 220, 255));
		} else {
			gc.setFill(Color.rgb(255, 180, 155));
		}
		gc.fillRect(25, 300, 348, 98);
		gc.fillRect(25, 402, 348, 278);
		
		if(winnerSide == Side.RED) {
			gc.setFill(Color.rgb(130, 220, 255));
		} else {
			gc.setFill(Color.rgb(255, 180, 155));
		}
		gc.fillRect(377, 300, 348, 98);
		gc.fillRect(377, 402, 348, 278);
		
		gc.setFill(Color.BLACK);
		gc.setFont(Font.font("Telugu MN", FontWeight.EXTRA_BOLD, 40));
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		
		gc.fillText("VICTORY", 199, 349);
		gc.fillText("DEFEAT", 551, 349);
		
		gc.setFont(Font.font("Telugu MN", FontWeight.EXTRA_BOLD, 20));
		for(int i = 0; i < 5; ++i) {
			if(winnerSide == Side.BLUE) {
				gc.setFill(Color.CORNFLOWERBLUE);
			} else {
				gc.setFill(Color.ORANGERED);
			}
			gc.setTextAlign(TextAlignment.LEFT);
			gc.fillText(winnerList.get(i).getName(), 50, 450 + 50*i);
			gc.fillText("Lv.", 275, 450 + 50*i);
			gc.setTextAlign(TextAlignment.RIGHT);
			gc.fillText("" + winnerList.get(i).getPlayer().getLevel(), 325, 450 + 50*i);
		}
		
		for(int i = 0; i < 5; ++i) {
			if(winnerSide == Side.RED) {
				gc.setFill(Color.CORNFLOWERBLUE);
			} else {
				gc.setFill(Color.ORANGERED);
			}
			gc.setTextAlign(TextAlignment.LEFT);
			gc.fillText(loserList.get(i).getName(), 425, 450 + 50*i);
			gc.fillText("Lv.", 650, 450 + 50*i);
			gc.setTextAlign(TextAlignment.RIGHT);
			gc.fillText("" + winnerList.get(i).getPlayer().getLevel(), 700, 450 + 50*i);
		}
		
		gc.setFill(Color.BLACK);
		gc.setFont(Font.font("Telugu MN", 20));
		gc.setTextAlign(TextAlignment.CENTER);
		gc.fillText("Press [ENTER] to continue", Main.SCREEN_SIZE / 2, Main.SCREEN_SIZE - 35);
		
		rankingPane.getChildren().add(ranking);
	}
	
	public void reset() {
		winnerList.clear();
		loserList.clear();
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
}