package entity.property;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import main.Main;
import main.game.GameComponent;
import utility.Pair;
import entity.Entity;
import entity.job.*;

public class Identity {
	private Novice player;
	private Canvas canvas;
	
	public Identity(Novice player) {
		this.player = player;
		
		this.canvas = new Canvas(150, 25);
		draw();
	}
	
	public void draw() {
		canvas.getGraphicsContext2D().clearRect(0, 0, 150, 25);
		
		if(GameComponent.getInstance().getPlayer() != null) {
			changeCenter(GameComponent.getInstance().getPlayer().getRefPoint());
		}
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFont(Font.font("Telugu MN", 10));
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);
		
		gc.fillText("Level " + player.getLevel(), 75, 7);
		gc.fillText(player.getName(), 75, 18);
	}
	
	public void changeCenter(Pair center) {
		canvas.setTranslateX(player.getRefPoint().first - center.first 
				+ Main.SCREEN_SIZE / 2 - 75);
		canvas.setTranslateY(player.getRefPoint().second - center.second 
				+ Main.SCREEN_SIZE / 2 - player.getRadius() - 25);
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public void die() {
		canvas.setOpacity(0);
		GameComponent.getInstance().getIdentityPane().getChildren().remove(canvas);
	}
	
	public Novice getPlayer() {
		return player;
	}
}
