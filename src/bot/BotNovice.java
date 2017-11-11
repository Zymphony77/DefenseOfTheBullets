package bot;

import entity.Entity;
import entity.food.Food;
import entity.job.Novice;
import main.Component;
import utility.Pair;
import utility.Side;

public class BotNovice extends Bot{

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
		//find everything in range of bot
		findEntityInRange();
		
		if(super.player.getLevel() < 10){ // level less than >> 10 Farm!!!!!!!!!!
			farm();
		}
		else {
			
		}
	}
	
	private int checkCoordinate(Entity a, Entity b) {
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
	
	private void move(int dir) {
		if(player.getSide() == Side.BLUE) {
			// 0 = LeftTop 1 = RightTop 2 = LeftBottom 3 = RightBottom
		}
		else {
			// 3 = LeftTop 2 = RightTop 1 = LeftBottom 0 = RightBottom
		}
	}

	@Override
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
		
		if(bulletList.isEmpty()) {
			//check tower in range if tower in range, then Novice shouldn't be here.
			double moveConst = 1.75; //move heuristic number
			
			//if x-coordinate > 30% and y-coordinate > 30% and 0 isn't player.Side >> LT
			//player will not move if RT have many foods. it can go to LT and then go to RT in next move.
			
			
			//if x-coordinate > 30% and 0 isn't player.Side >> check(LT, LB)
			//player will not move if R have many foods.
			
			
			//if y-coordinate > 30% and 0 isn't player.Side >> check(LT, RT)
			//player will not move if B have many foods.
			
			
			if(player.getSide() == Side.RED) {
				food[3] = (int)(food[3] * moveConst);
			}else {
				food[0] = (int)(food[0] * moveConst);
			}
			
			// ..0..
			// 3.4.1
			// ..2..
			
			if(isTowerInRange()) { //tower in range. It doesn't detect player.side color.
				//if tower is 0
					//if tower is 0 and 0 is Yellow check(LT, RT, LB)
					//if tower is 0 and 3 or 4 is BLUE check(LT, LB*moveConst)
					//if tower is 0 and 0 is Red and 1 and 4 is Yellow check(LT,LB);
				
			}
			else {
				int tmpForMove;
				for(int i = 0; i < 4; i++){
					boolean chk = true;
					for(int j = 0; j < 4; j++) {
						if(food[i] < food[j]){
							chk = false;
							break;
						}
					}
					if(chk == true) {
						tmpForMove = i;
					}
				}
				
				///move to direction tmpForMove
				
				///choose target to closest food
				
			}
		}
		else {
			
		}
	}

	@Override
	protected void upgradeSkill() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void upgradeStatus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void upgradeClass() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void chooseTarget() {
		// TODO Auto-generated method stub
		
	}
	

}
