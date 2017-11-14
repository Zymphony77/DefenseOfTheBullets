package bot;

import entity.job.Novice;
import utility.Job;

public class BotNovice extends Bot{
	
	public BotNovice(Novice player)
	{
		super(player);
		job = Job.NOVICE; //job will change if it have other jobs.
		prevDirection = 7;
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
		//find everything in range of bot
		findEntityInRange();
		
		//update Grid
		updateGrid();
		
		if(destination != null && !utility.isVisible(destination)) {
			System.out.println("desitnation can't be visible" + utility.isVisible(destination));
			destination = null;
		}
		else if(destination != null){
			int dir = utility.canMoveWithDestination(destination, prevDirection);
			
			if(destination != null && dir != -1) {
				moveWithDestination(dir);
				return;
			}
		}
		
		if(player.getHp() < player.getMaxHp() * 0.3) {
			escape();
		}
		else if(player.getLevel() < 10){ // level less than >> 10 Farm!!!!!!!!!!
			//farm();
			defenseTower();
		}
		else if(player.getLevel() < 30){
			farm();
		}
		else {
			war();
		}
		
		///choose target to closest food
		target = chooseClosestTarget();
		
		//change direction to target//
		if(target != null) {
			utility.changeDirectionToTarget(utility.getRef(player, target));
			player.rotate();
			player.shoot();
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
	protected void upgradeJob() {
		// TODO Auto-generated method stub
		
	}
	

}
