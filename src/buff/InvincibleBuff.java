package buff;

import javafx.scene.image.Image;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import main.Main;
import entity.job.*;
import skill.*;

public class InvincibleBuff extends Buff implements Expirable {
	private static final Image IMAGE = new Image("resource/image/InvincibleIcon.png");
	
	private int maxDuration;
	private int duration;
	
	private double opacity = 1;
	private double opacityChange = -0.01;
	private Timeline blinking;
	
	public InvincibleBuff(Novice player) {
		super(player, BuffType.BUFF);
		
		maxDuration = 5 * Main.FRAME_RATE;
		duration = maxDuration;
		
		opacity = 1;
		opacityChange = -0.01;
		blinking = new Timeline(new KeyFrame(Duration.millis(5), event -> {
			opacity += opacityChange;
			
			if(opacity < 0.005) {
				opacityChange = 0.01;
			} else if(opacity > 0.995) {
				opacityChange = -0.01;
			}
			
			player.getCanvas().setOpacity(opacity);
		}));
		blinking.setOnFinished(event -> {
			player.getCanvas().setOpacity(1);
		});
		blinking.setCycleCount(1000);
		blinking.playFromStart();
		activateBuff();
	}
	
	public void update() {
		--duration;
		
		if(duration <= 0) {
			deactivateBuff();
		}
	}
	
	public void drawEffect() {
		player.setAttack(0);
		player.setDamageFactor(0);
	}
	
	public void undrawEffect() {
		// nothing to do here
		// recalculated in Novice
	}
	
	public int getMaxDuration() {
		return maxDuration;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public Image getImage() {
		return IMAGE;
	}
	
	public Skill getSkill() {
		return null;
	}
}
