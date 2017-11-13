package bot;

import java.util.Comparator;
import java.util.HashSet;
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
	protected static ArrayList<Novice> playerFriendList = new ArrayList<Novice>();
	protected static ArrayList<Novice> playerEnemiesList = new ArrayList<Novice>();
	protected static ArrayList<Bullet> bulletList = new ArrayList<Bullet>();
	protected static ArrayList<Tower> towerList = new ArrayList<Tower>();
	protected static ArrayList<Food> foodList = new ArrayList<Food>();
	protected static Grid[][] grid = new Grid[Main.SCREEN_SIZE + 10][Main.SCREEN_SIZE + 10];
	protected int[] change8to4 = new int[] {0, 1, 1, 2, 2, 3, 3, 0};
	protected int[] change4to8 = new int[] {7, 1, 3, 5};
	
	public abstract void move();
	protected abstract void upgradeSkill();
	protected abstract void upgradeStatus();
	protected abstract void upgradeJob();
	
	protected static final double VISION = Main.SCREEN_SIZE/2.0;
	protected static final int NUMBER_OF_CHANGE_POSITION = 300; // Expected Value is 5 seconds to change;
	protected static final int SAFETY_ZONE = (int)(Component.MAX_SIZE * 0.1);
	protected static final double MOVE_HEURISTIC = 2.6; //move heuristic number
	protected static int[] oppositeDirection = new int[] {4, 5, 6, 7, 0, 1, 2, 3};
	
	protected int prevDirection = -1;
	protected Pair destination;
	protected boolean chkDestination = false;
	protected Entity target = null;

	protected Queue<Grid> priorityQueue = new PriorityQueue<>();
	protected static int[][] newPosition = new int[][] {{-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1} ,{-1, -1}};
	
	Random rand = new Random();
	
	Utility utility;
	
	public Bot(Novice player) {
		this.player = player;
		this.chkMove = false;
		utility = new Utility(player);
	}
	
	protected boolean willCollide(Entity entity, Pair position, double time) {
		for(Bullet each: Component.getInstance().getBulletList()) {
			if(each.getLifeCycleCount() + time * Main.FRAME_RATE > Bullet.MAX_LIFE_CYCLE) {
				continue;
			}
			
			double posx = utility.getRef(player, each).first;
			double posy = utility.getRef(player, each).second;
			
			posx += Math.cos(Math.toRadians(each.getDirection())) * each.getSpeed() * time / 1000;
			posy += Math.sin(Math.toRadians(each.getDirection())) * each.getSpeed() * time / 1000;
			
			if(entity.getRadius() + each.getRadius() > position.distance(new Pair(posx, posy))) {
				return true;
			}
		}
		return false;
	}
	
	protected void sGrid() {
		double timeForOnePixel = 1.0/player.getSpeed();
		
		Grid tmp = new Grid((int) Math.floor(utility.getRef(player, player).first), (int) Math.floor(utility.getRef(player, player).second), 0.0, true, -1);
		Grid newTmp;
		priorityQueue.add(tmp);
		grid[utility.positionXInGrid(tmp.getX())][utility.positionYInGrid(tmp.getY())] = new Grid(tmp);
		for(int i = 0; i <= Main.SCREEN_SIZE; i++) {
			for(int j = 0; j <= Main.SCREEN_SIZE; j++) {
				grid[i][j] = null;
			}
		}
		
		System.out.println(tmp.getX() + " " + tmp.getY());
		while(!priorityQueue.isEmpty()) {
			tmp = priorityQueue.poll();
			double time = timeForOnePixel + tmp.getTime();
			for(int i = 0; i < 8; i++) {
				if(tmp.getTime() == 0.0)
					newTmp = new Grid(tmp.getX() + newPosition[i][0], tmp.getY() + newPosition[i][1], tmp.getTime() + time, false, i);
				else
					newTmp = new Grid(tmp.getX() + newPosition[i][0], tmp.getY() + newPosition[i][1], tmp.getTime() + time, false, tmp.getFirstDirection());
				if(utility.positionXInGrid(newTmp.getX()) < 0 
						|| utility.positionXInGrid(newTmp.getX()) > Main.SCREEN_SIZE 
						|| utility.positionYInGrid(newTmp.getY()) < 0 
						|| utility.positionYInGrid(newTmp.getY()) > Main.SCREEN_SIZE) {
					continue;
				}
				if(willCollide(player, new Pair((double)newTmp.getX(), (double)newTmp.getY()), newTmp.getTime()) 
						|| grid[utility.positionXInGrid(newTmp.getX())][utility.positionYInGrid(newTmp.getY())] != null ) {
					continue;
				}
				newTmp.setChk(true);
				grid[utility.positionXInGrid(newTmp.getX())][utility.positionYInGrid(newTmp.getY())] = new Grid(newTmp);
				//System.out.println(newTmp.getX() + " " + newTmp.getY());
				priorityQueue.add(newTmp);
			}
		}
		System.out.println("-------------------------------");
	}
	
	protected void findEntityInRange()
	{
		playerEnemiesList.clear();
		playerFriendList.clear();
		bulletList.clear();
		foodList.clear();
		towerList.clear();
		
		for (Novice tmp : Component.getInstance().getPlayerList()) {
		    if(utility.isVisible(utility.getRef(player, tmp)) && tmp.getSide() != player.getSide()){
		    		playerEnemiesList.add(tmp);
		    }
		    else {
		    		playerFriendList.add(tmp);
		    }
		}
		
		for (Bullet tmp : Component.getInstance().getBulletList()) {
			if(utility.isVisible(utility.getRef(player, tmp)) && tmp.getSide() != player.getSide()){
		    		bulletList.add(tmp);
		    }
		}
		
		for (Food tmp : Component.getInstance().getFoodList()) {
			if(utility.isVisible(utility.getRef(player, tmp))) {
				foodList.add(tmp);
			}
		}
		
		for (Tower tmp : Component.getInstance().getTowerList()) {
			if(utility.isVisible(utility.getRef(player, tmp)) && tmp.getSide() != player.getSide()) {
				towerList.add(tmp);
			}
		}
	}
	
	protected void move(int dir) {
		
		if(dir == -1) {
			System.out.println(" Bug move(dir)!!!! ");
			dir = prevDirection;
			if(prevDirection == -1 || rand.nextInt(NUMBER_OF_CHANGE_POSITION) == 0) {
				System.out.println("Bugg" + prevDirection);
				int tmpForMove = rand.nextInt(8);
				while(utility.isHitTheWall(tmpForMove)) {
					tmpForMove = rand.nextInt(8);
				}
				dir = tmpForMove;
			}
		}
		
		if(player.getSide() == Side.BLUE) {
			// 7.0.1
			// 6...2
			// 5.4.3
			while(utility.isHitTheWall(dir)) {
				dir = rand.nextInt(8);
			}
			player.setMoving((dir + 6)%8 * 45);
		}
		else {
			// 3.4.5
			// 2...6
			// 1.0.7
			while(utility.isHitTheWall(dir)) {
				dir = rand.nextInt(8);
			}
			//System.out.println(dir + " " + utility.getRef(player, player).first + " " + utility.getRef(player, player).second);
			player.setMoving((oppositeDirection[dir] + 6)%8 * 45);
		}
		
		prevDirection = dir;
	}
	
	protected Entity chooseClosestTarget() {

		double distance = Double.MAX_VALUE;
		Entity ans = null;
		
		for(Bullet bullet : bulletList) {
			if(distance > utility.getRef(player, player).distance(utility.getRef(player, bullet))) {
				ans = bullet;
				distance = utility.getRef(player, player).distance(utility.getRef(player, bullet));
			}
		}
		
		for(Tower tower : towerList) {
			if(distance > utility.getRef(player, player).distance(utility.getRef(player, tower))) {
				ans = tower;
				distance = utility.getRef(player, player).distance(utility.getRef(player, tower));
			}
		}
		
		for(Novice novice : playerEnemiesList) {
			if(distance > utility.getRef(player, player).distance(utility.getRef(player, novice))) {
				ans = novice;
				distance = utility.getRef(player, player).distance(utility.getRef(player, novice));
			}
		}
		
		for(Food food : foodList) {
			if(distance > utility.getRef(player, player).distance(utility.getRef(player, food))) {
				ans = food;
				distance = utility.getRef(player, player).distance(utility.getRef(player, food));
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
			int chk = utility.checkCoordinate(player, tmp);
			food[chk]++;
		}
		
		hueristicFood(food);
		
		for(Novice tmp : playerEnemiesList) {
			int chk = utility.checkCoordinate(player, tmp);
			enemies[chk]++;
		}
		
		if(foodList.isEmpty() && bulletList.isEmpty()) {
			int tmpForMove = prevDirection; //Random direction for move novice.
			if(utility.isTowerInRange()) {
				int tmp = utility.checkCoordinate(player, towerList.get(0));
				move(oppositeDirection[tmp]);
			}else {
				if(tmpForMove == -1 || rand.nextInt(NUMBER_OF_CHANGE_POSITION) == 0 || utility.isHitTheWall(tmpForMove)) {
					tmpForMove = rand.nextInt(8);
					while(utility.isHitTheWall(tmpForMove)) {
						tmpForMove = rand.nextInt(8);
					}
				}
				move(tmpForMove);
			}
		}
		else if(bulletList.isEmpty() && playerEnemiesList.isEmpty()) {
			//check tower in range if tower in range, then Novice shouldn't be here.
			
			//if x-coordinate > 60% and y-coordinate > 60% and 0 isn't player.Side >> LT
			//player will not move if RT have many foods. it can go to LT and then go to RT in next move.
			if(utility.getRef(player, player).first >= Main.SCREEN_SIZE * 0.6 && utility.getRef(player, player).second >= Main.SCREEN_SIZE * 0.6) {
				food[3] = 0;
			}
			
			if(utility.isTowerInRange()) {  //check tower in range.
				int tmp = utility.checkCoordinate(player, towerList.get(0));
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
					return new Pair(utility.positionXInReal(i), utility.positionYInReal(0));
				}
			}
			for(int j = 0; j <= VISION; j++) {
				if(grid[0][j] != null && grid[0][j].isChk()) {
					return new Pair(utility.positionXInReal(0), utility.positionYInReal(j));
				}
			}
		}else if(area == 1) {
			for(int i = (int) VISION ; i < Main.SCREEN_SIZE; i++) {
				if(grid[i][0] != null && grid[i][0].isChk()) {
					return new Pair(utility.positionXInReal(i), utility.positionYInReal(0));
				}
			}
			for(int j = 0; j < VISION; j++) {
				if(grid[Main.SCREEN_SIZE][j] != null && grid[Main.SCREEN_SIZE][j].isChk()) {
					return new Pair(utility.positionXInReal(Main.SCREEN_SIZE), utility.positionYInReal(j));
				}
			}
		}else if(area == 2) {
			for(int i = 0; i <= VISION; i++) {
				if(grid[i][Main.SCREEN_SIZE] != null && grid[i][Main.SCREEN_SIZE].isChk()) {
					return new Pair(utility.positionXInReal(i), utility.positionYInReal(Main.SCREEN_SIZE));
				}
			}
			for(int j = (int) VISION; j <= Main.SCREEN_SIZE; j++) {
				if(grid[0][j] != null && grid[0][j].isChk()) {
					return new Pair(utility.positionXInReal(0), utility.positionYInReal(j));
				}
			}
		}else {
			for(int i = (int) VISION ; i < Main.SCREEN_SIZE; i++) {
				if(grid[i][Main.SCREEN_SIZE] != null && grid[i][Main.SCREEN_SIZE].isChk()) {
					return new Pair(utility.positionXInReal(i), utility.positionYInReal(Main.SCREEN_SIZE));
				}
			}
			for(int j = (int) VISION; j <= Main.SCREEN_SIZE; j++) {
				if(grid[Main.SCREEN_SIZE][j] != null && grid[Main.SCREEN_SIZE][j].isChk()) {
					return new Pair(utility.positionXInReal(Main.SCREEN_SIZE), utility.positionYInReal(j));
				}
			}
		}
		return null;
	}
	
	protected void escapeWithBullet() {
		int[] bullet = new int[8];
		
		for(Bullet tmp : bulletList) {
			int chk = utility.checkCoordinate(player, tmp);
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
			if(utility.canMoveWithDestination(destination, prevDirection)) {
				move(prevDirection);
			}
		}
		else {
			int dir = utility.getDirectionAdjacent(min, prevDirection);
			move(dir);
		}
	}

	protected void escape() {
		if(!bulletList.isEmpty()) {
			escapeWithBullet();
		}
		else {
			if(utility.getRef(player, player).first < SAFETY_ZONE && utility.getRef(player, player).second < SAFETY_ZONE) {
				farm();
			}
			else if(utility.getRef(player, player).first < SAFETY_ZONE) {
				move(0);
			}
			else if(utility.getRef(player, player).second < SAFETY_ZONE) {
				move(6);
			}
			else {
				int positionInMap = utility.getPositionInMap();
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
					if(utility.canMoveWithDestination(destination, prevDirection)) {
						move(prevDirection);
					}
				}
			}
		}
	}
	
	protected void moveWithDestination() {
		move(prevDirection);
	}

	protected void defenceTower() {
		Tower tmp = null;
		double distance = Double.MAX_VALUE;
		for(Tower tower : Component.getInstance().getTowerList()) {
			if(tower.getSide() == player.getSide() && distance > utility.getRef(player, player).distance(utility.getRef(player, tower))) {
				distance = utility.getRef(player, player).distance(utility.getRef(player, tower));
				tmp = tower;
			}
		}
		if(tmp == null) {
			attackTower();
		}
		
		if(!utility.isVisible(utility.getRef(player, tmp))) {
			int res = utility.checkCoordinate(player, tmp);
			move(res);
		}else {
			///not done not done not done;
			int newX = utility.positionXInGrid(utility.getRef(player, tmp).first), newY = utility.positionYInGrid(utility.getRef(player, tmp).second);
			if(grid[newX][newY] != null && grid[newX][newY].isChk()) {
				int res = grid[newX][newY].getFirstDirection();
				destination = utility.getRef(player, tmp);
				move(res);
			}
		}
	}

	protected void attackTower() {
		///not done not done not done :)
		Tower tmp = null;
		double distance = Double.MAX_VALUE;
		for(Tower tower : Component.getInstance().getTowerList()) {
			if(tower.getSide() == player.getSide() && distance > utility.getRef(player, player).distance(utility.getRef(player, tower))) {
				distance = utility.getRef(player, player).distance(utility.getRef(player, tower));
				tmp = tower;
			}
		}
		if(tmp == null) {
			attackTower();
		}
		
		if(!utility.isVisible(utility.getRef(player, tmp))) {
			int res = utility.checkCoordinate(player, tmp);
			
			
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
	
	public Novice getPlayer() {
		return player;
	}
}
