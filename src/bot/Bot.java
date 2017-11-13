package bot;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import entity.Entity;
import entity.bullet.Bullet;
import entity.food.Food;
import entity.job.Novice;
import entity.tower.Tower;
import main.Component;
import main.Main;
import utility.Grid;
import utility.Job;
import utility.Pair;
import utility.Side;

public abstract class Bot {
	protected Novice player;
	protected boolean chkMove = false;
	protected Job job;
	protected ArrayList<Novice> playerFriendList = new ArrayList<Novice>();
	protected ArrayList<Novice> playerEnemiesList = new ArrayList<Novice>();
	protected ArrayList<Bullet> bulletList = new ArrayList<Bullet>();
	protected ArrayList<Tower> towerList = new ArrayList<Tower>();
	protected ArrayList<Food> foodList = new ArrayList<Food>();
	protected Grid[][] grid = new Grid[Main.SCREEN_SIZE + 10][Main.SCREEN_SIZE + 10];
	protected int[] oppositeDirection = new int[] {4, 5, 6, 7, 0, 1, 2, 3};
	protected int[] change8to4 = new int[] {0, 1, 1, 2, 2, 3, 3, 0};
	protected int[] change4to8 = new int[] {7, 1, 3, 5};
	
	public abstract void move();
	protected abstract void upgradeSkill();
	protected abstract void upgradeStatus();
	protected abstract void upgradeJob();
	
	private static final double VISION = Main.SCREEN_SIZE/2.0;
	private static final int NUMBER_OF_CHANGE_POSITION = 300; // Expected Value is 5 seconds to change;
	private static final int SAFETY_ZONE = (int)(Component.MAX_SIZE * 0.1);
	private static final double MOVE_HEURISTIC = 2.6; //move heuristic number
	
	protected int prevDirection = -1;
	protected Pair destination;
	protected boolean chkDestination = false;
	protected Entity target = null;

	protected Queue<Grid> priorityQueue = new PriorityQueue<>();
	private int[][] newPosition = new int[][] {{-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1} ,{-1, -1}};
	
	Random rand = new Random();
	
	protected static Comparator<Grid> idComparator = new Comparator<Grid>(){
		
		@Override
		public int compare(Grid c1, Grid c2) {
			return (int) (c1.getTime() - c2.getTime());
        }
	};
	
	protected boolean willCollide(Entity entity, Pair position, double time) {
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
		return false;
	}
	
	protected boolean isVisible(Pair tmp) {
		double x = Math.abs(tmp.first - player.getRefPoint().first);
		double y = Math.abs(tmp.second - player.getRefPoint().second);
		return (x <= VISION && y <= VISION);
	}
	
	protected int positionXInGrid(double x) {
		int shiftX = (int)Math.floor(player.getRefPoint().first) - Main.SCREEN_SIZE/2;
		return (int)x - shiftX;
	}
	
	protected int positionYInGrid(double y) {
		int shiftY = (int)Math.floor(player.getRefPoint().second) - Main.SCREEN_SIZE/2;
		return (int)(y - shiftY);
	}
	
	protected double positionXInReal(int x) {
		int shiftX = (int)Math.floor(player.getRefPoint().first) - Main.SCREEN_SIZE/2;
		return x + shiftX;
	}
	
	protected double positionYInReal(int y) {
		int shiftY = (int)Math.floor(player.getRefPoint().second) - Main.SCREEN_SIZE/2;
		return y + shiftY;
	}
	
	protected void updateGrid() {
		double timeForOnePixel = 1.0/player.getSpeed();
		
		Grid tmp = new Grid((int) Math.floor(player.getRefPoint().first), (int) Math.floor(player.getRefPoint().second), 0.0, true, -1);
		Grid newTmp;
		priorityQueue.add(tmp);
		grid[positionXInGrid(tmp.getX())][positionYInGrid(tmp.getY())] = new Grid(tmp);
		while(!priorityQueue.isEmpty()) {
			tmp = priorityQueue.poll();
			double time = timeForOnePixel + tmp.getTime();
			for(int i = 0; i < 8; i++) {
				if(tmp.getTime() == 0.0)
					newTmp = new Grid(tmp.getX() + newPosition[i][0], tmp.getY() + newPosition[i][1], tmp.getTime() + time, false, i);
				else
					newTmp = new Grid(tmp.getX() + newPosition[i][0], tmp.getY() + newPosition[i][1], tmp.getTime() + time, false, tmp.getFirstDirection());
				if(newTmp.getX() < 0 || newTmp.getX() > Main.SCREEN_SIZE || newTmp.getY() < 0 || newTmp.getY() > Main.SCREEN_SIZE) {
					continue;
				}
				if(willCollide(player, new Pair((double)newTmp.getX(), (double)newTmp.getY()), newTmp.getTime()) || (grid[positionXInGrid(newTmp.getX())][positionYInGrid(newTmp.getY())] != null && grid[positionXInGrid(newTmp.getX())][positionYInGrid(newTmp.getY())].isChk() == true)) {
					continue;
				}
				newTmp.setChk(true);
				grid[newTmp.getX()][newTmp.getY()] = new Grid(newTmp);
				priorityQueue.add(newTmp);
			}
		}
	}
	
	protected void findEntityInRange()
	{
		for (Novice tmp : Component.getInstance().getPlayerList()) {
		    if(isVisible(tmp.getRefPoint()) && tmp.getSide() != player.getSide()){
		    		playerEnemiesList.add(tmp);
		    }
		    else {
		    		playerFriendList.add(tmp);
		    }
		}
		
		for (Bullet tmp : Component.getInstance().getBulletList()) {
			if(isVisible(tmp.getRefPoint()) && tmp.getSide() != player.getSide()){
		    		bulletList.add(tmp);
		    }
		}
		
		for (Food tmp : Component.getInstance().getFoodList()) {
			if(isVisible(tmp.getRefPoint())) {
				foodList.add(tmp);
			}
		}
		
		for (Tower tmp : Component.getInstance().getTowerList()) {
			if(isVisible(tmp.getRefPoint()) && tmp.getSide() != player.getSide()) {
				towerList.add(tmp);
			}
		}
	}
	
	protected void move(int dir) {
		
		if(dir == -1) {
			System.out.println(" Bug move(dir)!!!! ");
		}
		
		if(player.getSide() == Side.BLUE) {
			// 7.0.1
			// 6...2
			// 5.4.3
			while(isHitTheWall(dir)) {
				dir = rand.nextInt(8);
			}
			player.setMoving((dir + 6)%8 * 45);
		}
		else {
			// 3.4.5
			// 2...6
			// 1.0.7
			while(isHitTheWall(dir)) {
				dir = rand.nextInt(8);
			}
			player.setMoving((oppositeDirection[dir] + 6)%8 * 45);
		}
		
		prevDirection = dir;
	}
	
	protected int checkDirection(int dir) {
		if(player.getSide() == Side.BLUE) {
			return dir;
		}else {
			return oppositeDirection[dir];
		}
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
			
			if(player.getSide() == Side.BLUE) {
				return tmp;
			}else {
				return oppositeDirection[tmp];
			}
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
	
	protected boolean isTowerInRange() {
		return !towerList.isEmpty();
	}
	
	protected boolean isHitTheWall(int dir) {
		if(dir == 7 || dir == 0 || dir == 1) {
			if(player.getRefPoint().second <= 2) {
				return true;
			}else {
				return false;
			}
		}else if(dir >= 5 && dir <= 7) {
			if(player.getRefPoint().first <= 2) {
				return true;
			}else {
				return false;
			}
		}else if(dir >= 3 && dir <= 5) {
			if(player.getRefPoint().second >= Component.MAX_SIZE - 2) {
				return true;
			}else {
				return false;
			}
		}else{
			if(player.getRefPoint().first >= Component.MAX_SIZE - 2) {
				return true;
			}else {
				return false;
			}
		}
	}
	
	protected Entity chooseClosestTarget() {

		double distance = Double.MAX_VALUE;
		Entity ans = null;
		
		for(Bullet bullet : bulletList) {
			if(distance > player.getRefPoint().distance(bullet.getRefPoint())) {
				ans = bullet;
				distance = player.getRefPoint().distance(bullet.getRefPoint());
			}
		}
		
		for(Tower tower : towerList) {
			if(distance > player.getRefPoint().distance(tower.getRefPoint())) {
				ans = tower;
				distance = player.getRefPoint().distance(tower.getRefPoint());
			}
		}
		
		for(Novice novice : playerEnemiesList) {
			if(distance > player.getRefPoint().distance(novice.getRefPoint())) {
				ans = novice;
				distance = player.getRefPoint().distance(novice.getRefPoint());
			}
		}
		
		for(Food food : foodList) {
			if(distance > player.getRefPoint().distance(food.getRefPoint())) {
				ans = food;
				distance = player.getRefPoint().distance(food.getRefPoint());
			}
		}
		
		return ans; 
	}
	
	protected void hueristicFood(int[] food) {
		food[7] += food[0] + food[6];
		food[7] *= MOVE_HEURISTIC * MOVE_HEURISTIC;
		food[1] *= MOVE_HEURISTIC;
		food[5] *= MOVE_HEURISTIC;
	}
	
	protected void farm() {

		int[] food = new int[8];
		int[] enemies = new int[8];
		
		for(Food tmp : foodList) {
			int chk = checkCoordinate(player, tmp);
			food[chk]++;
		}
		
		hueristicFood(food);
		
		for(Novice tmp : playerEnemiesList) {
			int chk = checkCoordinate(player, tmp);
			enemies[chk]++;
		}
		
		if(foodList.isEmpty() && bulletList.isEmpty() && playerEnemiesList.isEmpty()) {
			
			int tmpForMove; //Random direction for move novice.
			if(isTowerInRange()) {
				int tmp = checkCoordinate(player, towerList.get(0));
				move(oppositeDirection[tmp]);
			}else {
				if(prevDirection == -1 || rand.nextInt(NUMBER_OF_CHANGE_POSITION) == 0) {
					tmpForMove = rand.nextInt(8);
					while(isHitTheWall(tmpForMove)) {
						tmpForMove = rand.nextInt(8);
					}
					prevDirection = tmpForMove;
				}
				move(prevDirection);
			}
		}
		else if(bulletList.isEmpty() && playerEnemiesList.isEmpty()) {
			//check tower in range if tower in range, then Novice shouldn't be here.
			
			//if x-coordinate > 60% and y-coordinate > 60% and 0 isn't player.Side >> LT
			//player will not move if RT have many foods. it can go to LT and then go to RT in next move.
			if(player.getRefPoint().first >= Main.SCREEN_SIZE * 0.6 && player.getRefPoint().second >= Main.SCREEN_SIZE * 0.6) {
				food[3] = 0;
			}
			
			if(isTowerInRange()) {  //check tower in range.
				int tmp = checkCoordinate(player, towerList.get(0));
				move(oppositeDirection[tmp]);
			}
			else {
				int tmpForMove = 7;
				for(int i = 0; i < 8; i++){
					boolean chk = true;
					for(int j = 0; j < 8; j++) {
						if(food[i] < food[j]){
							chk = false;
							break;
						}
					}
					if(chk == true) {
						tmpForMove = i;
					}
				}
				
				move(tmpForMove);
			}
		}
		else {
			escapeWithBullet();
		}
	}

	protected Pair getDirectionWithArea(int area) {
		//return Pair of Destination for move.
		if(area == 0) {
			for(int i = 0; i <= VISION; i++) {
				if(grid[i][0] != null && grid[i][0].isChk()) {
					return new Pair(positionXInReal(i), positionYInReal(0));
				}
			}
			for(int j = 0; j <= VISION; j++) {
				if(grid[0][j] != null && grid[0][j].isChk()) {
					return new Pair(positionXInReal(0), positionYInReal(j));
				}
			}
		}else if(area == 1) {
			for(int i = (int) VISION ; i < Main.SCREEN_SIZE; i++) {
				if(grid[i][0] != null && grid[i][0].isChk()) {
					return new Pair(positionXInReal(i), positionYInReal(0));
				}
			}
			for(int j = 0; j < VISION; j++) {
				if(grid[Main.SCREEN_SIZE][j] != null && grid[Main.SCREEN_SIZE][j].isChk()) {
					return new Pair(positionXInReal(Main.SCREEN_SIZE), positionYInReal(j));
				}
			}
		}else if(area == 2) {
			for(int i = 0; i <= VISION; i++) {
				if(grid[i][Main.SCREEN_SIZE] != null && grid[i][Main.SCREEN_SIZE].isChk()) {
					return new Pair(positionXInReal(i), positionYInReal(Main.SCREEN_SIZE));
				}
			}
			for(int j = (int) VISION; j <= Main.SCREEN_SIZE; j++) {
				if(grid[0][j] != null && grid[0][j].isChk()) {
					return new Pair(positionXInReal(0), positionYInReal(j));
				}
			}
		}else {
			for(int i = (int) VISION ; i < Main.SCREEN_SIZE; i++) {
				if(grid[i][Main.SCREEN_SIZE] != null && grid[i][Main.SCREEN_SIZE].isChk()) {
					return new Pair(positionXInReal(i), positionYInReal(Main.SCREEN_SIZE));
				}
			}
			for(int j = (int) VISION; j <= Main.SCREEN_SIZE; j++) {
				if(grid[Main.SCREEN_SIZE][j] != null && grid[Main.SCREEN_SIZE][j].isChk()) {
					return new Pair(positionXInReal(Main.SCREEN_SIZE), positionYInReal(j));
				}
			}
		}
		return null;
	}
	
	protected int getDirectionAdjacent(int dir) {
		prevDirection = -1;
		for(int i = 0; i < 8; i++) {
			int newx = 375 + newPosition[i][0], newy = 375 + newPosition[i][1];
			if(grid[newx][newy] != null && grid[newx][newy].isChk()) {
				prevDirection = grid[newx][newy].getFirstDirection();
			}
		}
		int newx = 375 + newPosition[dir][0], newy = 375 + newPosition[dir][1];
		if(grid[newx][newy] != null && grid[newx][newy].isChk()) {
			prevDirection = grid[newx][newy].getFirstDirection();
		}
		return prevDirection;
	}
	
	protected void escapeWithBullet() {
		int[] bullet = new int[8];
		
		for(Bullet tmp : bulletList) {
			int chk = checkCoordinate(player, tmp);
			bullet[chk]++;
		}
		
		int min = Integer.MAX_VALUE;
		
		for(int i = 0; i < 8; i++) {
			if(min > bullet[i])
				min = i;
		}
		
		Pair res = getDirectionWithArea(change8to4[min]);
		
		for(int i = 0; i < 4; i++) {
			if(res == null) {
				res = getDirectionWithArea(change8to4[i]);
			}
		}
		
		if(res != null) {
			destination = res;
			if(canMoveWithDestination()) {
				move(prevDirection);
			}
		}
		else {
			prevDirection = getDirectionAdjacent(min);
			move(prevDirection);
		}
	}
	
	protected int getPositionInMap() {
		if(player.getRefPoint().first <= Component.MAX_SIZE/2.0 && player.getRefPoint().second <= Component.MAX_SIZE/2.0)
			return 0;
		else if(player.getRefPoint().first > Component.MAX_SIZE/2.0 && player.getRefPoint().second <= Component.MAX_SIZE/2.0)
			return 1;
		else if(player.getRefPoint().first <= Component.MAX_SIZE/2.0 && player.getRefPoint().second > Component.MAX_SIZE/2.0)
			return 2;
		return 3;
	}

	protected void escape() {
		if(!bulletList.isEmpty()) {
			escapeWithBullet();
		}
		else {
			if(player.getRefPoint().first < SAFETY_ZONE && player.getRefPoint().second < SAFETY_ZONE) {
				farm();
			}
			else if(player.getRefPoint().first < SAFETY_ZONE) {
				move(0);
			}
			else if(player.getRefPoint().second < SAFETY_ZONE) {
				move(6);
			}
			else {
				int positionInMap = getPositionInMap();
				if(positionInMap == 0) {
					move(7);
				}
				else if(positionInMap == 1) {
					move(0);
				}
				else if(positionInMap == 2) {
					move(6);
				}
				else {
					Pair res = getDirectionWithArea(0);
					if(res == null) {
						res = getDirectionWithArea(3);
					}
					if(res == null) {
						res = getDirectionWithArea(1);
					}
					if(res == null) {
						res = getDirectionWithArea(2);
					}
					destination = res;
					if(canMoveWithDestination()) {
						move(prevDirection);
					}
				}
			}
		}
	}
	
	protected boolean canMoveWithDestination() {
		int x = positionXInGrid(destination.first), y = positionYInGrid(destination.second);
		if(grid[x][y] != null && grid[x][y].isChk()) {
			prevDirection = grid[x][y].getFirstDirection();
			return true;
		}
		for(int i = 0; i < 8; i++) {
			int newX = x + newPosition[i][0], newY = y + newPosition[i][1];
			if(newX >= 0 && newX <= Main.SCREEN_SIZE && newY >= 0 && newY <= Main.SCREEN_SIZE && grid[newX][newY] != null && grid[newX][newY].isChk()) {
				prevDirection = grid[newX][newY].getFirstDirection();
				return true;
			}
		}
		prevDirection = -1;
		destination = null;
		return false;
	}
	
	protected void moveWithDestination() {
		move(prevDirection);
	}

	protected void defenceTower() {
		Tower tmp = null;
		double distance = Double.MAX_VALUE;
		for(Tower tower : Component.getInstance().getTowerList()) {
			if(tower.getSide() == player.getSide() && distance > player.getRefPoint().distance(tower.getRefPoint())) {
				distance = player.getRefPoint().distance(tower.getRefPoint());
				tmp = tower;
			}
		}
		if(tmp == null) {
			attackTower();
		}
		
		if(!isVisible(tmp.getRefPoint())) {
			int res = checkCoordinate(player, tmp);
			move(res);
		}else {
			///not done not done not done;
			int newX = positionXInGrid(tmp.getRefPoint().first), newY = positionYInGrid(tmp.getRefPoint().second);
			if(grid[newX][newY] != null && grid[newX][newY].isChk()) {
				prevDirection = grid[newX][newY].getFirstDirection();
				destination = tmp.getRefPoint();
				move(prevDirection);
			}
		}
	}

	protected void attackTower() {
		///not done not done not done :)
		Tower tmp = null;
		double distance = Double.MAX_VALUE;
		for(Tower tower : Component.getInstance().getTowerList()) {
			if(tower.getSide() == player.getSide() && distance > player.getRefPoint().distance(tower.getRefPoint())) {
				distance = player.getRefPoint().distance(tower.getRefPoint());
				tmp = tower;
			}
		}
		if(tmp == null) {
			attackTower();
		}
		
		if(!isVisible(tmp.getRefPoint())) {
			int res = checkCoordinate(player, tmp);
			
			
		}else {
			///not done not done not done;
			
		}
	}
	
	protected void war() {
		if(rand.nextInt(2) == 0) {
			attackTower();
		}else {
			defenceTower();
		}
	}
	
	protected void changeDirectionToTarget(Pair target) {
		double dir = Math.atan2(target.second - player.getRefPoint().second, target.first - player.getRefPoint().first);
		
		dir = dir * 180.0 / Math.PI;
		
		player.setDirection(dir);
	}
	
}
