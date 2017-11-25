package buff;

import javafx.scene.image.Image;
import main.Main;
import entity.job.*;

public class InvincibleBuff extends Buff implements Expirable {
	private static final Image IMAGE = new Image("resource/image/InvincibleIcon.png");
	
	private int maxDuration;
	private int duration;
	
	public InvincibleBuff(Novice player) {
		super(player, BuffType.BUFF);
		
		maxDuration = 5 * Main.FRAME_RATE;
		duration = maxDuration;
		
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
}
