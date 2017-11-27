package buff;

import javafx.application.Platform;
import javafx.scene.image.Image;

import main.Main;
import entity.job.*;
import skill.*;

public class InvincibleBuff extends Buff implements Expirable {
	private static final Image IMAGE = new Image("resource/image/InvincibleIcon.png");
	
	private int maxDuration;
	private int duration;
	
	private double opacity = 1;
	private double opacityChange = -0.01;
	private Thread blinking = new Thread(new Runnable() {
		@Override
		public void run() {
			try {
				for(int i = 0; i < 1250; ++i) {
					opacity += opacityChange;
					
					if(opacity < 0.01) {
						opacityChange = 0.01;
					} else if(opacity > 0.99) {
						opacityChange = -0.01;
					}
					Platform.runLater(new Runnable(){
						@Override
						public void run() {
							player.getCanvas().setOpacity(opacity);
						}
					});
		
					Thread.sleep(5);
				}
			} catch(InterruptedException e) {
				System.out.println("IN");
				Platform.runLater(new Runnable(){
					@Override
					public void run() {
						player.getCanvas().setOpacity(1.0);
					}
				});
			}
			System.out.println("END");
		}
	});
	
	public InvincibleBuff(Novice player) {
		super(player, BuffType.BUFF);
		
		maxDuration = 5 * Main.FRAME_RATE;
		duration = maxDuration;
		
		blinking.start();
		activateBuff();
	}
	
	public void update() {
		--duration;
		
		if(duration <= 0) {
			System.out.println("INTERRUPT");
			blinking.interrupt();
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