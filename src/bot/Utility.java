package bot;

import java.util.Random;

import entity.Entity;
import entity.bullet.Bullet;
import entity.job.Novice;
import main.Component;
import main.Main;
import utility.Pair;
import utility.Side;

public class Utility{
	Novice player;
	Random rand = new Random();
	
	public Utility(Novice player) {
		this.player = player;
	}
	
	protected boolean willCollide(Entity entity, Pair position, double time) {
		for(Bullet each: Component.getInstance().getBulletList()) {
			if(each.getLifeCycleCount() + time * Main.FRAME_RATE > Bullet.MAX_LIFE_CYCLE) {
				continue;
			}
			
			if(each.getSide() == entity.getSide())
				continue;
			
			double posx = getRef(player, each).first;
			double posy = getRef(player, each).second;
			
			posx += Math.cos(Math.toRadians(each.getDirection())) * each.getSpeed() * time / 1000;
			posy += Math.sin(Math.toRadians(each.getDirection())) * each.getSpeed() * time / 1000;
			
			if(entity.getRadius() + each.getRadius() > position.distance(new Pair(posx, posy))) {
				return true;
			}
		}
		return false;
	}
	
	protected int positionXInGrid(double x) {
		double shiftX = Math.floor(getRef(player, player).first) - (double)Main.SCREEN_SIZE / 2.0;
		return (int)((x - shiftX) * (double)Bot.SIZE_OF_GRID / (double)Main.SCREEN_SIZE);
	}
	
	protected int positionYInGrid(double y) {
		double shiftY = Math.floor(getRef(player, player).second) - (double)Main.SCREEN_SIZE / 2.0;
		return (int)((y - shiftY) * (double)Bot.SIZE_OF_GRID / (double)Main.SCREEN_SIZE);
	}
	
	protected double positionXInReal(int x) {
		int shiftX = (int)Math.floor(getRef(player, player).first) - Main.SCREEN_SIZE/2;
		return x * Main.SCREEN_SIZE / Bot.SIZE_OF_GRID + shiftX;
	}
	
	protected double positionYInReal(int y) {
		int shiftY = (int)Math.floor(getRef(player, player).second) - Main.SCREEN_SIZE/2;
		return y * Main.SCREEN_SIZE / Bot.SIZE_OF_GRID + shiftY;
	}
	
	protected Pair nextFrame(double direction) {
		double posx = getRef(player, player).first;
		double posy = getRef(player, player).second;
		
		posx += Math.cos(Math.toRadians(direction)) * player.getSpeed();
		posy += Math.sin(Math.toRadians(direction)) * player.getSpeed();
		
		return new Pair(posx, posy);
	}
	
	protected boolean isVisible(Pair tmp) {
		double x = Math.abs(tmp.first - getRef(player, player).first);
		double y = Math.abs(tmp.second - getRef(player, player).second);
		if(x <= Bot.VISION && y <= Bot.VISION)
			return true;
		return false;
	}
	
	protected void changeDirectionToTarget(Pair target) {
		double dir = Math.atan2(target.second - getRef(player, player).second, target.first - getRef(player, player).first);
		
		dir = dir * 180.0 / Math.PI;
		
		if(player.getSide() == Side.BLUE)
			player.setDirection(dir);
		else
			player.setDirection(dir + 180.0);
	}
	
	protected Pair getRef(Entity reference, Entity entity) {
		if(reference.getSide() == Side.BLUE) {
			return entity.getRefPoint();
		}
		return new Pair(Component.MAX_SIZE - entity.getRefPoint().first, 
				Component.MAX_SIZE - entity.getRefPoint().second);
	}
	
	protected Pair getRef(Entity reference, Pair pair) {
		if(reference.getSide() == Side.BLUE) {
			return pair;
		}
		return new Pair(Component.MAX_SIZE - pair.first, 
				Component.MAX_SIZE - pair.second);
	}
	
	protected static Pair flip(Pair pair) {
		return new Pair(Component.MAX_SIZE - pair.first, Component.MAX_SIZE - pair.second);
	}
	
	protected int checkDirection(int dir) {
		return dir;
	}
	
	protected int checkCoordinate(Entity a, Entity b) {
		double x = getRef(player, a).first - getRef(player, b).first;
		double y = getRef(player, a).second - getRef(player, b).second;
		
		int tmp;
		if(x > 0 && y > 0){
			tmp = 7;
		}else if(y > 0 && x == 0) {
			tmp = 0;
		}else if(x < 0 && y > 0) {
			tmp = 1;
		}else if(x < 0 && y == 0) {
			tmp = 2;
		}else if(x < 0 && y < 0) {
			tmp = 3;
		}else if(x == 0 && y < 0) {
			tmp = 4;
		}else if(x > 0 && y < 0){
			tmp = 5;
		}else {
			tmp = 6;
		}
		
		return tmp;
	}
	
	protected int canMoveWithDestination(Pair destination, int prevDirection) {
		System.out.println("canMoveWithDestination");
		
		if(destination == null) {
			System.out.println("Destination is null.");
			return -1;
		}
		
		int x = positionXInGrid(destination.first), y = positionYInGrid(destination.second);
		System.out.println("Position In GRID: " + x + " " + y);
		if(x < 0 || x > Main.SCREEN_SIZE || y < 0 || y > Main.SCREEN_SIZE) {
			destination = null;
			return -1;
		}
		if(Bot.grid[x][y] != null && Bot.grid[x][y].isChk() && rand.nextInt(200) != 0) {
			prevDirection = Bot.grid[x][y].getFirstDirection();
			destination = new Pair(Bot.grid[x][y].getX(), Bot.grid[x][y].getY());
			if(!isHitTheWall(prevDirection)) {
				System.out.println("canMoveWithDestination = true");
				return prevDirection;
			}
		}
		destination = null;
		return -1;
	}
	
	protected boolean isTowerInRange() {
		return !Bot.towerList.isEmpty();
	}
	
	protected boolean isHitTheWall(int dir) {
		if(dir == 7 || dir == 0 || dir == 1) {
			if(getRef(player, player).second <= 0) {
				return true;
			}else {
				return false;
			}
		}else if(dir >= 5 && dir <= 7) {
			if(getRef(player, player).first <= 0) {
				return true;
			}else {
				return false;
			}
		}else if(dir >= 3 && dir <= 5) {
			if(getRef(player, player).second >= Component.MAX_SIZE) {
				return true;
			}else {
				return false;
			}
		}else{
			if(getRef(player, player).first >= Component.MAX_SIZE) {
				return true;
			}else {
				return false;
			}
		}
	}
	
	protected int getDirectionAdjacent(int prevDirection) {
		System.out.println("getDirectionAdjacent");
		int newx = Bot.SIZE_OF_GRID/2 + Bot.newPosition[prevDirection][0], newy = Bot.SIZE_OF_GRID/2 + Bot.newPosition[prevDirection][1];
		if(newx < 0 || newx > Bot.SIZE_OF_GRID || newy < 0 || newy > Bot.SIZE_OF_GRID) {
			if(Bot.grid[newx][newy] != null && Bot.grid[newx][newy].isChk() && !isHitTheWall(prevDirection)) {
				return prevDirection;
			}
		}
		return -1;
	}
	
	protected int getPositionInMap() {
		if(getRef(player, player).first <= Component.MAX_SIZE/2.0 && getRef(player, player).second <= Component.MAX_SIZE/2.0)
			return 0;
		else if(getRef(player, player).first > Component.MAX_SIZE/2.0 && getRef(player, player).second <= Component.MAX_SIZE/2.0)
			return 1;
		else if(getRef(player, player).first <= Component.MAX_SIZE/2.0 && getRef(player, player).second > Component.MAX_SIZE/2.0)
			return 2;
		return 3;
	}
}
