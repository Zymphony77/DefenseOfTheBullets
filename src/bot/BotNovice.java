package bot;

import entity.job.Novice;
import main.game.GameHandler;
import utility.Job;

public class BotNovice extends Bot{
	
	//STR = 1 VIT = 2 DEX = 3 INT = 4 AGI = 5 LUK = 6
	
	private static final int[] upStatus = new int[] {2, 2, 0, 1, 1, 0, 4, 4, 2, 2, 0, 1, 1, 0, 4, 4, 2, 2, 0, 1, 1, 0, 4, 4, 2, 2, 0, 1, 1, 0, 4, 4, 2, 2, 0, 1, 1, 0, 4, 4, 2, 2, 0, 1, 1, 0, 4, 4, 5, 5, 5, 5, 5, 5, 5};
	private static final int[] upSkill = new int[] {2, 1, 2, 1, 2, 1, 2, 1, 2, 1};
	private int iteratorStatus;
	private int iteratorSkill;
	
	public BotNovice(Novice player)
	{
		super(player);
		iteratorStatus = 0;
		iteratorSkill = 0;
		job = Job.NOVICE; //job will change if it have other jobs.
		prevDirection = 7;
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
		if(player.isDead()) {
			iteratorStatus = 0;
			return;
		}
		
		if(player.getLevel() > 10 && player.getJob() == Job.NOVICE) {
			upgradeJob();
		}
		
		//upgrade
		upgradeStatus();
		
		//find everything in range of bot
		findEntityInRange();
		
		//update Grid
		updateGrid();
		
		//upgrade Skill
		upgradeSkill();
		
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
		
		if(player.getHp() < player.getMaxHp() * 0.6) {
			escape();
		}
		else if(player.getLevel() < 30){
			farm();
		}
		else if(!player.isMoving()) {
			///choose target to closest food
			target = chooseClosestTarget();
			
			//change direction to target\\
			if(target != null) {
				utility.changeDirectionToTarget(utility.getRef(player, target));
				player.rotate();
				player.shoot();
			}else {
				war();
			}
			return;
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
		try {
			if(player.getExperience().getSkillPoint() > 0)
				player.upgradeSkill(upSkill[iteratorSkill++]);
		}catch(IndexOutOfBoundsException e) {
			iteratorSkill = 0;
		}catch(Exception e) {
		}
	}

	@Override
	protected void upgradeStatus() {
		// TODO Auto-generated method stub
		try {
			if(player.getExperience().getPointStatus() > 0 && iteratorStatus < upStatus.length) {
				player.upgradeStatus(upStatus[iteratorStatus++]);
			}
		}catch(IndexOutOfBoundsException e) {
			iteratorStatus = 0;
		}catch(Exception e) {
		}
	}

	@Override
	protected void upgradeJob() {
		// TODO Auto-generated method stub
		int tmp = rand.nextInt(3);
		if(tmp == 0) {
			player.requestChangeJob(Job.TANK);
		} else if(tmp == 1) {
			player.requestChangeJob(Job.MAGICIAN);
		} else {
			player.requestChangeJob(Job.RANGER);
		}
	}
	

}
