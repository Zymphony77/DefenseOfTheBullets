package bot;

import entity.job.Novice;
import utility.Job;

public class BotNovice extends Bot{
	
	public BotNovice(Novice player)
	{
		super.player = player;
		chkMove = false;
		job = Job.NOVICE; //job will change if it have other jobs.
		prevDirection = 3;
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
		//find everything in range of bot
		findEntityInRange();
		
		//update Grid
		updateGrid();
		
		if(destination != null && !isVisible(destination)) {
			destination = null;
		}
		
		if(destination != null && canMoveWithDestination()) {
			moveWithDestination();
		}	
		else if(player.getHp() < player.getMaxHp() * 0.3) {
			escape();
		}
		else if(player.getLevel() < 10){ // level less than >> 10 Farm!!!!!!!!!!
			farm();
		}
		else if(player.getLevel() < 30){
			
		}
		else {
			war();
		}
		
		///choose target to closest food
		target = chooseClosestTarget();
		
		//change direction to target//
		//////////////////////////////
		if(target != null) {
			changeDirectionToTarget(target.getRefPoint());
		}
		
		player.rotate();
		player.shoot();
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
