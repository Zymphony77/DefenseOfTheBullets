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
		
		if(super.player.getLevel() < 10){ // level less than >> 10 Farm!!!!!!!!!!
			farm();
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
	

}
