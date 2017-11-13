package bot;

import entity.Entity;
import entity.job.Novice;
import main.Component;
import main.Main;
import utility.Pair;
import utility.Side;

public class Utility{
	Novice player;
	
	public Utility(Novice player) {
		this.player = player;
	}
	
	protected int positionXInGrid(double x) {
		int shiftX = (int)Math.floor(getRef(player, player).first) - Main.SCREEN_SIZE/2;
		return (int)x - shiftX;
	}
	
	protected int positionYInGrid(double y) {
		int shiftY = (int)Math.floor(getRef(player, player).second) - Main.SCREEN_SIZE/2;
		return (int)(y - shiftY);
	}
	
	protected double positionXInReal(int x) {
		int shiftX = (int)Math.floor(getRef(player, player).first) - Main.SCREEN_SIZE/2;
		return x + shiftX;
	}
	
	protected double positionYInReal(int y) {
		int shiftY = (int)Math.floor(getRef(player, player).second) - Main.SCREEN_SIZE/2;
		return y + shiftY;
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
		return (x <= Bot.VISION && y <= Bot.VISION);
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
	
	protected static Pair flip(Pair pair) {
		return new Pair(Component.MAX_SIZE - pair.first, Component.MAX_SIZE - pair.second);
	}
	
	protected int checkDirection(int dir) {
		return dir;
	}
	
	protected int checkCoordinate(Entity a, Entity b) {
		if(a.getSide() == Side.BLUE) {
			double x = a.getRefPoint().first - b.getRefPoint().first;
			double y = a.getRefPoint().second - b.getRefPoint().second;
			
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
		else {
			// rotate map 180 degree
			double x = a.getRefPoint().first - b.getRefPoint().first;
			double y = a.getRefPoint().second - b.getRefPoint().second;
			if(x >= 0 && y >= 0){
				return 3;
			}else if(x < 0 && y >= 0) {
				return 2;
			}else if(x >= 0 && y < 0) {
				return 1;
			}else {
				return 0;
			}
		}
	}
	
	protected boolean canMoveWithDestination(Pair destination, int prevDirection) {
		int x = positionXInGrid(destination.first), y = positionYInGrid(destination.second);
		if(Bot.grid[x][y] != null && Bot.grid[x][y].isChk()) {
			prevDirection = Bot.grid[x][y].getFirstDirection();
			return true;
		}
		for(int i = 0; i < 8; i++) {
			int newX = x + Bot.newPosition[i][0], newY = y + Bot.newPosition[i][1];
			if(newX >= 0 && newX <= Main.SCREEN_SIZE && newY >= 0 && newY <= Main.SCREEN_SIZE && Bot.grid[newX][newY] != null && Bot.grid[newX][newY].isChk()) {
				prevDirection = Bot.grid[newX][newY].getFirstDirection();
				return true;
			}
		}
		prevDirection = -1;
		destination = null;
		return false;
	}
	
	protected boolean isTowerInRange() {
		return !Bot.towerList.isEmpty();
	}
	
	protected boolean isHitTheWall(int dir) {
		if(dir == 7 || dir == 0 || dir == 1) {
			if(getRef(player, player).second <= 30) {
				return true;
			}else {
				return false;
			}
		}else if(dir >= 5 && dir <= 7) {
			if(getRef(player, player).first <= 30) {
				return true;
			}else {
				return false;
			}
		}else if(dir >= 3 && dir <= 5) {
			if(getRef(player, player).second >= Component.MAX_SIZE - 30) {
				return true;
			}else {
				return false;
			}
		}else{
			if(getRef(player, player).first >= Component.MAX_SIZE - 30) {
				return true;
			}else {
				return false;
			}
		}
	}
	
	protected int getDirectionAdjacent(int dir, int prevDirection) {
		prevDirection = -1;
		for(int i = 0; i < 8; i++) {
			int newx = 375 + Bot.newPosition[i][0], newy = 375 + Bot.newPosition[i][1];
			if(Bot.grid[newx][newy] != null && Bot.grid[newx][newy].isChk()) {
				prevDirection = Bot.grid[newx][newy].getFirstDirection();
			}
		}
		int newx = 375 + Bot.newPosition[dir][0], newy = 375 + Bot.newPosition[dir][1];
		if(Bot.grid[newx][newy] != null && Bot.grid[newx][newy].isChk()) {
			prevDirection = Bot.grid[newx][newy].getFirstDirection();
		}
		return prevDirection;
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
