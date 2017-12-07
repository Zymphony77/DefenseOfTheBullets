package entity.bullet;

import javafx.scene.image.Image;

import entity.Entity;
import entity.property.*;
import javafx.scene.canvas.GraphicsContext;
import main.game.GameComponent;
import utility.Pair;

public class IceBullet extends Bullet implements Rotatable {
	private static final Image RED_ORB = new Image("image/RedIceOrb.png");
	private static final Image BLUE_ORB = new Image("image/BlueIceOrb.png");
	public static final int RADIUS = 9;
	
	protected int rotateDirection;
	protected double slowFactor;
	
	public IceBullet(Entity shooter, Pair refPoint, double maxHp, double direction, double attack, 
			double speed, Side side, double slowFactor) {
		super(shooter, refPoint, maxHp, direction, attack, speed, side);
		this.slowFactor = slowFactor;
		rotateDirection = 0;
	}
	
	public void rotate() {
		rotateDirection = (rotateDirection + 11) % 360;
		canvas.setRotate(rotateDirection);
	}
	
	@Override
	public void draw() {
		int SIZE = 2*RADIUS;
		
		canvas.setWidth(SIZE);
		canvas.setHeight(SIZE);
		
		changeCenter(GameComponent.getInstance().getPlayer().getRefPoint());
		
		GraphicsContext gc = this.getCanvas().getGraphicsContext2D();
		
		if(side == Side.RED) {
			gc.drawImage(RED_ORB, 0, 0, SIZE, SIZE);
		} else {
			gc.drawImage(BLUE_ORB, 0, 0, SIZE, SIZE);
		}
	}
	
	@Override
	public int getRadius() {
		return RADIUS;
	}
	
	public double getSlowFactor() {
		return slowFactor;
	}
}
