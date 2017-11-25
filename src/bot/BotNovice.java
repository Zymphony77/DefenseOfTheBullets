package bot;

import entity.job.Novice;
import utility.Job;

public class BotNovice extends Bot{
	
	//STR = 1 VIT = 2 DEX = 3 INT = 4 AGI = 5 LUK = 6
	
	private static final int[] upStatus = new int[] {2, 2, 0, 1, 1, 0, 4, 4, 2, 2, 0, 1, 1, 0, 4, 4, 2, 2, 0, 1, 1, 0, 4, 4, 2, 2, 0, 1, 1, 0, 4, 4, 2, 2, 0, 1, 1, 0, 4, 4, 2, 2, 0, 1, 1, 0, 4, 4, 5, 5, 5, 5, 5, 5, 5};
	private static final int[] upSkill = new int[] {2, 1, 2, 1, 2, 1, 2, 1, 2, 1};
	private int cntStatus;
	private int cntSkill;
	
	public BotNovice(Novice player)
	{
		super(player);
		cntStatus = 0;
		cntSkill = 0;
		job = Job.NOVICE; //job will change if it have other jobs.
		prevDirection = 7;
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
		if(player.isDead()) {
			cntStatus = 0;
			System.out.println("DEAD!!!!");
			return;
		}
		
		///CHEAT
		if(player.getExperience().getLevel() < 45)
			player.gainExp(300);
		
		if(!player.isMoving())
			player.useSkill(2);
		
		//upgrade
		upgradeStatus();
		
		//find everything in range of bot
		findEntityInRange();
		
		//update Grid
		updateGrid();
		
		if(destination != null && !utility.isVisible(destination)) {
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
			farm();
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
		if(player.getExperience().getSkillPoint() > 0)
			player.upgradeSkill(upSkill[cntSkill++]);
	}

	@Override
	protected void upgradeStatus() {
		// TODO Auto-generated method stub
		if(player.getExperience().getPointStatus() > 0 && cntStatus < upStatus.length) {
			player.upgradeStatus(upStatus[cntStatus++]);
		}
	}

	@Override
	protected void upgradeJob() {
		// TODO Auto-generated method stub
	}
	

}
