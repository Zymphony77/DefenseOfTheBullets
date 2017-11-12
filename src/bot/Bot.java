package bot;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import entity.Entity;
import entity.bullet.Bullet;
import entity.food.Food;
import entity.job.Novice;
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
	protected LinkedList<Novice> playerFriendList = new LinkedList<Novice>();
	protected LinkedList<Novice> playerEnemiesList = new LinkedList<Novice>();
	protected LinkedList<Bullet> bulletList = new LinkedList<Bullet>();
	protected LinkedList<Food> foodList = new LinkedList<Food>();
	protected Grid[][] grid = new Grid[Main.SCREEN_SIZE + 10][Main.SCREEN_SIZE + 10];
	
	public abstract void move();
	protected abstract void upgradeSkill();
	protected abstract void upgradeStatus();
	protected abstract void upgradeClass();
	
	private static final double vision = Main.SCREEN_SIZE/2.0;
	protected int prevDirection;
	protected Pair destination;
	protected boolean chkDestination = false;
	protected Entity target = null;
	
	protected Queue<Grid> priorityQueue = new PriorityQueue<>();
	
	public static Comparator<Grid> idComparator = new Comparator<Grid>(){
		
		@Override
		public int compare(Grid c1, Grid c2) {
			return (int) (c1.getTime() - c2.getTime());
        }
	};
	
	public void updateGrid() {
		double shiftX = Math.floor(player.getRefPoint().first) - Main.SCREEN_SIZE/2, shiftY = Math.floor(player.getRefPoint().second) - Main.SCREEN_SIZE/2;
		Grid tmp = new Grid((int) Math.floor(player.getRefPoint().first), (int) Math.floor(player.getRefPoint().second), 0.0, true);
		priorityQueue.add(tmp);
		grid[tmp.getX()][tmp.getY()] = tmp;
		while(!priorityQueue.isEmpty()) {
			tmp = priorityQueue.poll();
			
		}
	}
	
	public void findEntityInRange()
	{
		for (Novice tmp : Component.getInstance().getPlayerList()) {
			double x = Math.abs(tmp.getRefPoint().first - player.getRefPoint().first);
			double y = Math.abs(tmp.getRefPoint().second - player.getRefPoint().second);
		    if(x <= vision && y <= vision && tmp.getSide() != player.getSide()){
		    		playerEnemiesList.add(tmp);
		    }
		    else {
		    		playerFriendList.add(tmp);
		    }
		}
		
		for (Bullet tmp : Component.getInstance().getBulletList()) {
			double x = Math.abs(tmp.getRefPoint().first - player.getRefPoint().first);
			double y = Math.abs(tmp.getRefPoint().second - player.getRefPoint().second);
			if(x <= vision && y <= vision && tmp.getSide() != player.getSide()){
		    		bulletList.add(tmp);
		    }
		}
		
		for (Food tmp : Component.getInstance().getFoodList()) {
			double x = Math.abs(tmp.getRefPoint().first - player.getRefPoint().first);
			double y = Math.abs(tmp.getRefPoint().second - player.getRefPoint().second);
			if(x <= vision && y <= vision) {
				foodList.add(tmp);
			}
		}
	}
	
	protected void move(int dir) {
		if(player.getSide() == Side.BLUE) {
			// 0 = LeftTop 1 = RightTop 2 = LeftBottom 3 = RightBottom
		}
		else {
			// 3 = LeftTop 2 = RightTop 1 = LeftBottom 0 = RightBottom
		}
	}
	
	protected int checkDirection(int dir) {
		if(player.getSide() == Side.BLUE) {
			return dir;
		}else {
			return 3 - dir;
		}
	}
	
	protected int checkCoordinate(Entity a, Entity b) {
		if(a.getSide() == Side.BLUE) {
			double x = a.getRefPoint().first - b.getRefPoint().first;
			double y = a.getRefPoint().second - b.getRefPoint().second;
			if(x >= 0 && y >= 0){
				return 0;
			}else if(x < 0 && y >= 0) {
				return 1;
			}else if(x >= 0 && y < 0) {
				return 2;
			}else {
				return 3;
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
		
		/////////////////////////////////////////////////////////////////
		/////////////////////////check check ////////////////////////////
		/////////////////////////////////////////////////////////////////
		
		return false;
	}
	
	protected boolean isHitTheWall(int dir) {
		
		/////////////////////////////////////////////////////////////////
		/////////////////////////check check ////////////////////////////
		/////////////////////////////////////////////////////////////////
		
		return false;
	}
	
	protected Entity chooseClosestTarget(int type) {
		//type 0 is Food and 1 is enemy

		double distance = Double.MAX_VALUE;
		Entity ans = null;
		
		if(type == 0) {
			for(Food food : foodList) {
				if(distance > player.getRefPoint().distance(food.getRefPoint())) {
					ans = food;
					distance = player.getRefPoint().distance(food.getRefPoint());
				}
			}
		}
		else {
			for(Novice enemy : playerEnemiesList) {
				if(distance > player.getRefPoint().distance(enemy.getRefPoint())) {
					ans = enemy;
					distance = player.getRefPoint().distance(enemy.getRefPoint());
				}
			}
		}
		
		return ans; 
	}
	
	protected void farm() {
		// TODO Auto-generated method stub
		// 0 = LeftTop 1 = RightTop 2 = LeftBottom 3 = RightBottom
		int[] food = new int[4];
		int[] enemies = new int[4];
		
		for(Food tmp : foodList) {
			int chk = checkCoordinate(player, tmp);
			food[chk]++;
		}
		
		for(Novice tmp : playerEnemiesList) {
			int chk = checkCoordinate(player, tmp);
			enemies[chk]++;
		}
		
		if(foodList.isEmpty()) {
			Random rand = new Random();
			
			////Not done
			
			int tmpForMove; //Random direction for move novice.
			if(isTowerInRange()) {
				if(prevDirection == -1) {
					
				}else {
					
				}
			}else {
				if(prevDirection == -1) {
					tmpForMove = rand.nextInt(8);
					if(isHitTheWall(tmpForMove)) {
						/////////////////////////////
					}else {
						tmpForMove = checkDirection(tmpForMove);
						prevDirection = tmpForMove;
					}
				}else {
					tmpForMove = checkDirection(prevDirection);
				}
			}
		}
		else if(bulletList.isEmpty()) {
			//check tower in range if tower in range, then Novice shouldn't be here.
			double moveConst = 2.25; //move heuristic number
			
			//if x-coordinate > 50% and y-coordinate > 30% and 0 isn't player.Side >> LT
			//player will not move if RT have many foods. it can go to LT and then go to RT in next move.
			
			
			//if x-coordinate > 50% and 0 isn't player.Side >> check(LT, LB)
			//player will not move if R have many foods.
			
			
			//if y-coordinate > 50% and 0 isn't player.Side >> check(LT, RT)
			//player will not move if B have many foods.
			
			if(isTowerInRange()) { //tower in range.
				//if tower in LT (check RT, LB)
				//if tower in RT (check LT, LB)
				//if tower in RB (check LT, RT, LB)
				//if tower in LB (check LT * moveConst, RT * moveConst, RB)
			}
			else {
				int tmpForMove = 0;
				for(int i = 0; i < 4; i++){
					boolean chk = true;
					for(int j = 0; j < 4; j++) {
						if(food[i] < food[j]){
							chk = false;
							break;
						}
					}
					if(chk == true) {
							tmpForMove = checkDirection(i);
					}
				}
				
				///move to direction tmpForMove
				move(tmpForMove);
				
				///choose target to closest food
				target = chooseClosestTarget(0);
				
				//change direction to target//
				//////////////////////////////
			}
		}
		else {
			escapeWithBullet();
		}
	}

	private void escapeWithBullet() {
		/// Not done :)
		
	}
	
	protected void escape() {
		// TODO Auto-generated method stub
		
	}

	protected void defenceTower() {
		// TODO Auto-generated method stub
		
	}

	protected void attackTower() {
		// TODO Auto-generated method stub
		
	}
}
