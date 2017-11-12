package utility;

import main.*;
import entity.*;
import entity.bullet.*;

public class TemporaryOak {
	public boolean willCollide(Entity entity, Pair position, double time) {
		for(Bullet each: Component.getInstance().getBulletList()) {
			if(each.getLifeCycleCount() + time * Main.FRAME_RATE > Bullet.MAX_LIFE_CYCLE) {
				continue;
			}
			
			double posx = each.getRefPoint().first;
			double posy = each.getRefPoint().second;
			
			posx += Math.cos(Math.toRadians(each.getDirection())) * each.getSpeed() * time / 1000;
			posy += Math.sin(Math.toRadians(each.getDirection())) * each.getSpeed() * time / 1000;
			
			if(entity.getRadius() + each.getRadius() > position.distance(new Pair(posx, posy))) {
				return true;
			}
		}
	}
}
