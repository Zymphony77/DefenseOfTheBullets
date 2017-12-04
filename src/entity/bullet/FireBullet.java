package entity.bullet;

import javafx.scene.image.Image;

import entity.Entity;
import entity.property.Side;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.game.GameComponent;
import utility.Pair;

public class FireBullet extends Bullet {
	private static final Image BACKGROUND = new Image("resource/image/FireOrb.png"); 
	private static final int RADIUS = 7;
	
	protected double burnDamage;
	
	public FireBullet(Entity shooter, Pair refPoint, double maxHp, double direction, double attack, 
			double speed, Side side, double burnDamage) {
		super(shooter, refPoint, maxHp, direction, attack, speed, side);
		this.burnDamage = burnDamage;
	}
	
	@Override
	public void draw() {
		canvas.setWidth(2*RADIUS);
		canvas.setHeight(2*RADIUS);
		
		changeCenter(GameComponent.getInstance().getPlayer().getRefPoint());
		
		GraphicsContext gc = this.getCanvas().getGraphicsContext2D();
		
		gc.drawImage(BACKGROUND, 0, 0, 2*RADIUS, 2*RADIUS);
		
		if(side == Side.RED) {
			gc.setFill(Color.ORANGERED);
		} else if(side == Side.BLUE) {
			gc.setFill(Color.CORNFLOWERBLUE);
		} else {
			gc.setFill(Color.GOLD);
		}
		
		gc.fillOval(1, 1, 2*RADIUS - 2, 2*RADIUS - 2);
	}
	
	@Override
	public int getRadius() {
		return RADIUS;
	}
}
