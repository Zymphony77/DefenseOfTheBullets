package buff;

import javafx.scene.image.Image;

import entity.job.*;

public class FrenzyBuff extends Buff {
	private static final Image IMAGE = new Image("resource/image/FrenzyIcon.png");
	private double reloadFactor;
	
	public FrenzyBuff(Novice player, double reloadFactor) {
		super(player, BuffType.BUFF);
		this.reloadFactor = reloadFactor;

		activateBuff();
	}
	
	public void drawEffect() {
		player.setReloadDone((int) (player.getReloadDone() / reloadFactor));
		player.setDamageFactor(player.getDamageFactor() * 1.25);
	}
	
	public void undrawEffect() {
		player.setReloadDone((int) (player.getReloadDone() * reloadFactor));
		player.setDamageFactor(player.getDamageFactor() / 1.25);
	}
	
	public Image getImage() {
		return IMAGE;
	}
}
