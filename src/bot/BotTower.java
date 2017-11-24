package bot;

import java.util.Random;

import entity.job.Novice;
import entity.tower.Tower;
import main.game.GameComponent;
import entity.bullet.Bullet;
import utility.Pair;

public class BotTower {
	public static void update() {
		for(Tower tower : GameComponent.getInstance().getTowerList()) {
			eachUpdate(tower);
		}
	}
	
	protected static void eachUpdate(Tower tower) {
		Random rand = new Random();
		double distance = Double.MAX_VALUE;
		Pair res = null;
		
		for(Novice novice : GameComponent.getInstance().getPlayerList()) {
			//check range of bullet
			if(distance > tower.getRefPoint().distance(novice.getRefPoint()) && novice.getSide() != tower.getSide() 
					&& tower.getRefPoint().distance(novice.getRefPoint()) <= Tower.BULLET_SPEED * Bullet.LIFE_DURATION) {
				distance = tower.getRefPoint().distance(novice.getRefPoint());
				res = novice.getRefPoint();
			}
		}
		
		if(res != null) {
			double dir = Math.atan2(res.second - tower.getRefPoint().second, res.first - tower.getRefPoint().first);
			
			dir = dir * 180.0 / Math.PI;
			
			tower.setDirection(dir);
			tower.rotate();
			tower.shoot();
		}
	}
}
