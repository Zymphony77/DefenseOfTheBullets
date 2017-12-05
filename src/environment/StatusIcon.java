package environment;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import main.Main;
import entity.job.*;
import entity.property.*;
import utility.*;

public class StatusIcon extends Pane {
	private static final int MAX_WIDTH = 120;
	private static final int MAX_HEIGHT = 12;
	private static final int ICON_SIZE = 10;
	private static final Image PLUS_IMAGE = new Image("image/PlusIcon.png");
	
	private Novice player;
	private int position;
	
	private Canvas icon;
	private Canvas upgrade;
	
	public StatusIcon(Novice player, int position) {
		this.player = player;
		this.position = position;
		
		icon = new Canvas(MAX_WIDTH + 30, MAX_HEIGHT + 4);
		icon.setTranslateX(getIconShift().first);
		icon.setTranslateY(getIconShift().second);
		icon.setOpacity(0.75);
		
		upgrade = new Canvas(ICON_SIZE, ICON_SIZE);
		
		upgrade.setTranslateX(MAX_WIDTH + 35);
		upgrade.setTranslateY(getIconShift().second + MAX_HEIGHT / 2 - ICON_SIZE / 2);
		
		GraphicsContext gc = upgrade.getGraphicsContext2D();
		
		gc.setFill(Color.MOCCASIN);
		gc.fillOval(0, 0, ICON_SIZE, ICON_SIZE);
		gc.drawImage(PLUS_IMAGE, 0, 0, ICON_SIZE, ICON_SIZE);
		
		drawIcon();
		undrawUpgrade();
		
		getChildren().addAll(icon, upgrade);
	}
	
	public void drawIcon() {
		GraphicsContext gc = icon.getGraphicsContext2D();
		
		gc.setFill(Color.BLACK);
		gc.setFont(Font.font("Telugu MN", FontWeight.EXTRA_BOLD, 10));
		gc.setTextBaseline(VPos.CENTER);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.fillText(Status.getName(position), 12, MAX_HEIGHT / 2);
		
		double blockSize = (double) (MAX_WIDTH - 30) / Status.MAX_STATUS;
		gc.setFill(Color.MOCCASIN);
		for(int i = 0; i < player.getStatus().getStatus(position); ++i) {
			gc.fillRect(30 + i * (blockSize + 2), MAX_HEIGHT / 4, blockSize,  MAX_HEIGHT / 2);
		}
		gc.setFill(Color.DARKGRAY);
		for(int i = player.getStatus().getStatus(position); i < Status.MAX_STATUS; ++i) {
			gc.fillRect(30 + i * (blockSize + 2), MAX_HEIGHT / 4, blockSize,  MAX_HEIGHT / 2);
		}
	}
	
	public void drawUpgrade() {
		upgrade.setOpacity(0.75);
		upgrade.setOnMouseClicked(event -> {
			
			player.upgradeStatus(position);
			if(player.getStatus().getStatus(position) >= Status.MAX_STATUS) {
				undrawUpgrade();
			}
		});
	}
	
	public void undrawUpgrade() {
		upgrade.setOpacity(0);
		upgrade.setOnMouseClicked(event -> {});
	}
	
	public boolean isUpgradable() {
		return player.getStatus().canUpgradeStatus(position);
	}
	
	public Pair getIconShift() {
		return new Pair(5, Main.SCREEN_SIZE - ExperienceBar.MAX_HEIGHT - 10 - (6 - position) * (MAX_HEIGHT + 4));
	}
}
