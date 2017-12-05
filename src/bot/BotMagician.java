package bot;

import entity.job.Magician;

public class BotMagician extends BotNovice{
	
	private static final int[] upStatus = new int[] {3, 4, 3, 4, 3, 4, 3, 4, 3, 4, 5, 5, 5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 1, 1, 1, 1, 5, 5, 5, 1, 1, 1, 2, 2, 2, 5, 5, 5};
	private static final int[] upSkill = new int[] {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4};
	private int iteratorSkill;
	private int iteratorStatus;
	private boolean activateBuff;
	
	public BotMagician(Magician player) {
		super(player);
		activateBuff = false;
		iteratorSkill = 0;
		iteratorStatus = 0;
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		//upgradeSkill
		upgradeSkill();
		
		if(activateBuff == false) {
			activateBuff = true;
			player.useSkill(3);
		}
		
		super.move();
	}

	@Override
	protected void upgradeSkill() {
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
		try {
			if(player.getExperience().getPointStatus() > 0)
				player.upgradeStatus(upStatus[iteratorStatus++]);
		}catch(IndexOutOfBoundsException e) {
			iteratorStatus = 0;
		}catch(Exception e) {
			iteratorStatus = 0;
		}
	}
	
}
