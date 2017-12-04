package bot;

import java.util.Comparator;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import entity.Entity;
import entity.bullet.Bullet;
import entity.food.Food;
import entity.job.Novice;
import entity.property.Side;
import entity.property.Status;
import entity.tower.Tower;
import main.Main;
import main.game.GameComponent;
import utility.Grid;
import utility.Job;
import utility.Pair;

public abstract class Bot {
	protected Novice player;
	protected boolean chkMove = false;
	protected Job job;
	protected static ArrayList<Novice> playerFriendList = new ArrayList<Novice>();
	protected static ArrayList<Novice> playerEnemiesList = new ArrayList<Novice>();
	protected static ArrayList<Bullet> bulletList = new ArrayList<Bullet>();
	protected static ArrayList<Tower> towerList = new ArrayList<Tower>();
	protected static ArrayList<Food> foodList = new ArrayList<Food>();
	protected int[] change8to4 = new int[] {0, 1, 1, 2, 2, 3, 3, 0, 4};
	protected int[] change4to8 = new int[] {7, 1, 3, 5, 0};
	
	public abstract void move();
	protected abstract void upgradeSkill();
	protected abstract void upgradeStatus();
	protected abstract void upgradeJob();
	
	protected static final double VISION = Main.SCREEN_SIZE/2.0;
	protected static final int SIZE_OF_GRID = 15;
	protected static final int NUMBER_OF_CHANGE_POSITION = Main.FRAME_RATE * 20; // Expected Value is 20 seconds to change;
	protected static final int SAFETY_ZONE = (int)(GameComponent.MAX_SIZE * 0.1);
	protected static final double MOVE_HEURISTIC = 2.6; //move heuristic number
	protected static int[] oppositeDirection = new int[] {4, 5, 6, 7, 0, 1, 2, 3, -1};
	protected static Grid[][] grid = new Grid[SIZE_OF_GRID + 3][SIZE_OF_GRID + 3];
	protected static final int GAP_WHEN_ATTACK_TOWER = 300;
	
	protected int status = 0; //none = 0, defense = 1, attack = 2, escape = 3, escape with bullet = 4, farm = 5; 
	protected int prevDirection = -1;
	protected Pair destination;
	protected boolean chkDestination = false;
	protected Entity target = null;

	protected Queue<Grid> queue = new LinkedBlockingQueue<Grid>();
	protected static int[][] newPosition = new int[][] {{0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0} ,{-1, -1}};
	
	Random rand = new Random();
	
	Utility utility;
	
	public Bot(Novice player) {
		this.player = player;
		this.chkMove = false;
		utility = new Utility(player);
	}
	
	protected void updateGrid() {
		double timeForOnePixel = 1.0/player.getSpeed() * Main.SCREEN_SIZE / SIZE_OF_GRID;
		
		for(int i = 0; i <= SIZE_OF_GRID; i++) {
			for(int j = 0; j <= SIZE_OF_GRID; j++) {
				grid[i][j] = null;
			}
		}
		
		Grid tmp = new Grid((int) Math.floor(utility.getRef(player, player).first), (int) Math.floor(utility.getRef(player, player).second), SIZE_OF_GRID/2, SIZE_OF_GRID/2, 0.0, true, -1);
		Grid newTmp;
		queue.add(tmp);
		grid[SIZE_OF_GRID/2][SIZE_OF_GRID/2] = new Grid(tmp);
		
		//System.out.println("tower: " + tmp.getX() + " " + tmp.getY());
		try {
			while(!queue.isEmpty()) {
				tmp = queue.poll();
				//System.out.println(" : == " +  tmp.getGridX() + " " + tmp.getGridY() + " time : " + tmp.getTime() + " First : "+  tmp.getFirstDirection());
				double time = timeForOnePixel + tmp.getTime();
				for(int i = 0; i < 8; i++) {
					if(tmp.getGridX() == SIZE_OF_GRID/2 && tmp.getGridY() == SIZE_OF_GRID/2) 
						newTmp = new Grid(tmp.getX() + newPosition[i][0] * Main.SCREEN_SIZE / SIZE_OF_GRID, tmp.getY() + newPosition[i][1] * Main.SCREEN_SIZE / SIZE_OF_GRID, tmp.getGridX() + newPosition[i][0], tmp.getGridY() + newPosition[i][1], time, false, i);
					else
						newTmp = new Grid(tmp.getX() + newPosition[i][0] * Main.SCREEN_SIZE / SIZE_OF_GRID, tmp.getY() + newPosition[i][1] * Main.SCREEN_SIZE / SIZE_OF_GRID, tmp.getGridX() + newPosition[i][0], tmp.getGridY() + newPosition[i][1], time, false, tmp.getFirstDirection());
		
					if(newTmp.getGridX() < 0 
							|| newTmp.getGridX() > SIZE_OF_GRID
							|| newTmp.getGridY() < 0 
							|| newTmp.getGridY() > SIZE_OF_GRID) {
						continue;
					}
					if(newTmp.getX() < 0  || newTmp.getX() >= GameComponent.MAX_SIZE
							|| newTmp.getY() < 0 || newTmp.getY() >= GameComponent.MAX_SIZE) {
						continue;
					}
					if(utility.willCollide(player, new Pair((double)newTmp.getX(), (double)newTmp.getY()), newTmp.getTime()) 
							|| grid[newTmp.getGridX()][newTmp.getGridY()] != null ) {
						continue;
					}
					newTmp.setChk(true);
					grid[newTmp.getGridX()][newTmp.getGridY()] = new Grid(newTmp);
					queue.add(newTmp);
				}
			}
		}catch(Exception e) {
			System.out.println("BUG on find best path!");
			e.printStackTrace();
		}
	}
	
	protected void findEntityInRange()
	{
		playerEnemiesList.clear();
		playerFriendList.clear();
		bulletList.clear();
		foodList.clear();
		towerList.clear();
		
		for (Novice tmp : GameComponent.getInstance().getPlayerList()) {
		    if(utility.isVisible(utility.getRef(player, tmp)) && tmp.getSide() != player.getSide()){
		    		playerEnemiesList.add(tmp);
		    }
		    else {
		    		playerFriendList.add(tmp);
		    }
		}
		
		for (Bullet tmp : GameComponent.getInstance().getBulletList()) {
			if(utility.isVisible(utility.getRef(player, tmp)) && tmp.getSide() != player.getSide()){
		    		bulletList.add(tmp);
		    }
		}
		
		for (Food tmp : GameComponent.getInstance().getFoodList()) {
			if(utility.isVisible(utility.getRef(player, tmp))) {
				foodList.add(tmp);
			}
		}
		
		for (Tower tmp : GameComponent.getInstance().getTowerList()) {
			if(utility.isVisible(utility.getRef(player, tmp)) && tmp.getSide() != player.getSide()) {
				towerList.add(tmp);
			}
		}
	}
	
	protected void move(int dir) {
		if(dir == -2) {
			player.stopMoving();
			player.useSkill(2);
			return;
		}
				
		if(dir == -1) {
			dir = prevDirection;
			if(prevDirection == -1 || prevDirection == -2) {
				int tmpForMove = rand.nextInt(8);
				while(utility.isHitTheWall(tmpForMove)) {
					tmpForMove = rand.nextInt(8);
				}
				dir = tmpForMove;
			}
			if(rand.nextInt(NUMBER_OF_CHANGE_POSITION) == 0) {
				dir = rand.nextInt(8);
				while(utility.isHitTheWall(dir)) {
					dir = rand.nextInt(8);
				}
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
		if(utility.getPositionInMap() == 0) {
			food[3] = (int) (food[3] * MOVE_HEURISTIC + 2);
		}else {
			food[7] *= MOVE_HEURISTIC;
			food[1] *= MOVE_HEURISTIC;
			food[5] *= MOVE_HEURISTIC;
		}
	}
	
	protected void farm() {
		
		int[] food = new int[9];
		int[] enemies = new int[9];
		
		for(Food tmp : foodList) {
			int chk = utility.checkCoordinate(player,  new Pair(utility.getRef(player, tmp)));
			food[chk]++;
		}
		
		hueristicFood(food);
		
		for(Novice tmp : playerEnemiesList) {
			int chk = utility.checkCoordinate(player,  new Pair(utility.getRef(player, tmp)));
			enemies[chk]++;
		}
		
		if(foodList.isEmpty()) {
			if(!bulletList.isEmpty()) {
				escapeWithBullet();
				return;
			}
			
			int tmpForMove = prevDirection;
			if(utility.isTowerInRange()) {
				int tmp = utility.checkCoordinate(player, new Pair(utility.getRef(player, towerList.get(0))));
				status = 5;
				move(oppositeDirection[tmp]);
			}else {
				if(tmpForMove == -1) {
					tmpForMove = rand.nextInt(8);
					System.out.println("change direction 1");
					while(utility.isHitTheWall(tmpForMove)) {
						tmpForMove = rand.nextInt(8);
					}
				}
				status = 5;
				move(tmpForMove);
			}
		}
		else {
			//check tower in range if tower in range, then Novice shouldn't be here.
			
			//if x-coordinate > 60% and y-coordinate > 60% and 0 isn't player.Side >> LT
			//player will not move if RT have many foods. it can go to LT and then go to RT in next move.
			if(utility.getRef(player, player).first >= GameComponent.MAX_SIZE * 0.75 && utility.getRef(player, player).second >= GameComponent.MAX_SIZE * 0.75) {
				food[3] = 0;
			}
			
			if(utility.isTowerInRange()) {  //check tower in range.
				int tmp = utility.checkCoordinate(player, new Pair(utility.getRef(player, towerList.get(0))));
				status = 3;
				move(oppositeDirection[tmp]);
			}
			else {
				int tmpForMove = prevDirection;
				for(int i = 0; i < 8; i++){
					if(food[i] > food[tmpForMove])
						tmpForMove = i;
				}
				status = 5;
				move(tmpForMove);
			}
		}
	}

	protected int getDirectionWithArea(int area) {
		if(area == 0) {
			for(int i = 0; i <= SIZE_OF_GRID/2; i++) {
				if(grid[i][0] != null && grid[i][0].isChk()) {
					return grid[i][0].getFirstDirection();
				}
			}
			for(int j = 0; j <= SIZE_OF_GRID/2; j++) {
				if(grid[0][j] != null && grid[0][j].isChk()) {
					return grid[0][j].getFirstDirection();
				}
			}
		}else if(area == 1) {
			for(int i = (int) SIZE_OF_GRID/2 ; i < SIZE_OF_GRID; i++) {
				if(grid[i][0] != null && grid[i][0].isChk()) {
					return grid[i][0].getFirstDirection();
				}
			}
			for(int j = 0; j < SIZE_OF_GRID/2; j++) {
				if(grid[SIZE_OF_GRID][j] != null && grid[SIZE_OF_GRID][j].isChk()) {
					return grid[SIZE_OF_GRID][j].getFirstDirection();
				}
			}
		}else if(area == 2) {
			for(int i = 0; i <= SIZE_OF_GRID/2; i++) {
				if(grid[i][SIZE_OF_GRID] != null && grid[i][SIZE_OF_GRID].isChk()) {
					return grid[i][SIZE_OF_GRID].getFirstDirection();
				}
			}
			for(int j = (int) SIZE_OF_GRID/2; j <= SIZE_OF_GRID; j++) {
				if(grid[0][j] != null && grid[0][j].isChk()) {
					return grid[0][j].getFirstDirection();
				}
			}
		}else if(area == 3){
			for(int i = (int) SIZE_OF_GRID/2 ; i < SIZE_OF_GRID; i++) {
				if(grid[i][SIZE_OF_GRID] != null && grid[i][SIZE_OF_GRID].isChk()) {
					return grid[i][SIZE_OF_GRID].getFirstDirection();
				}
			}
			for(int j = (int) SIZE_OF_GRID/2; j <= SIZE_OF_GRID; j++) {
				if(grid[SIZE_OF_GRID][j] != null && grid[SIZE_OF_GRID][j].isChk()) {
					return grid[SIZE_OF_GRID][j].getFirstDirection();
				}
			}
		}
		else if(area == 4){
			return -2;
		}
		return -1;
	}
	
	protected void escapeWithBullet() {

		/// if you don't want to change direction many time.
//		if(status == 4 && rand.nextInt(30) != 0) {
//			return;
//		}
		
		player.useSkill(1);

		int[] bullet = new int[9];
		
		for(Bullet tmp : bulletList) {
			int chk = utility.checkCoordinateForEscape(player, new Pair(utility.getRef(player, tmp)));
			bullet[chk]++;
		}
		
		for(Novice tmp : playerEnemiesList) {
			int chk = utility.checkCoordinateForEscape(player, new Pair(utility.getRef(player, tmp)));
			bullet[chk] += 5;
		}
		
		int max = 0;
		int tmpDir = rand.nextInt(8);
		for(int i = 0; i < 16; i++) {
			int j = rand.nextInt(8);
			if(max < bullet[j]) {
				max = bullet[j];
				tmpDir = j;
			}
		}
		
		int tmp = getDirectionWithArea(change8to4[oppositeDirection[tmpDir]]);
		
		if(!utility.isHitTheWall(tmp) && tmp != -1) {
			status = 4;
			move(tmp);
		}else {
			move(oppositeDirection[tmpDir]);
		}
	}

	protected void escape() {
		status = 3;
		
		try {
			if(!towerList.isEmpty()) {
				move(getDirectionWithArea(change8to4[oppositeDirection[utility.checkCoordinateForEscape(player, new Pair(utility.getRef(player, towerList.get(0)).first, utility.getRef(player, towerList.get(0)).second))]]));
			}
			else if(!bulletList.isEmpty()) {
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
						int res = getDirectionWithArea(0);
						move(res);
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void moveWithDestination(int dir) {
		move(dir);
	}

	protected void defenseTower() {
		status = 1;
		
		Tower tmp = null;
		double distance = Double.MAX_VALUE;
		for(Tower tower : GameComponent.getInstance().getTowerList()) {
			if(tower.getSide() == player.getSide() && distance > utility.getRef(player, player).distance(utility.getRef(player, tower))) {
				distance = utility.getRef(player, player).distance(utility.getRef(player, tower));
				tmp = tower;
			}
		}
		if(tmp == null) {
			attackTower();
			return;
		}
		
		int res = getDirectionWithArea(change8to4[utility.checkCoordinate(player, new Pair(utility.getRef(player, tmp)))]);
		move(res);
	}

	protected void attackTower() {
		///not done not done not done :)

		if(player.getHp() < player.getMaxHp() * 0.5) {
			destination = null;
			escape();
		}
		
		status = 2;
		
		Tower tmp = null;
		double distance = Double.MAX_VALUE;
		for(Tower tower : GameComponent.getInstance().getTowerList()) {
			if(tower.getSide() != player.getSide() && distance > utility.getRef(player, player).distance(utility.getRef(player, tower))) {
				distance = utility.getRef(player, player).distance(utility.getRef(player, tower));
				tmp = tower;
			}
		}
		if(tmp == null) {
			farm();
			return;
		}
		Pair position = new Pair(utility.getRef(player, tmp));
		position.first -= 250;
		position.second -= 250;
		int res = utility.checkCoordinate(player, position);
		move(res);
	}
	
	protected void war() {
		if(status == 0) {
			status = rand.nextInt(2) + 1; 
		}
		if(status == 1){
			defenseTower();
		}else {
			attackTower();
		}
	}
	
	public Novice getPlayer() {
		return player;
	}
}
