package bot;

import entity.job.Tank;

public class BotTank extends BotNovice{
	
	private static final int[] upStatus = new int[] {1, 4, 1, 4, 1, 4, 1, 4, 1, 4, 0, 1, 0, 1, 2, 0, 1, 2, 1, 2, 1, 2, 1, 1, 1, 1, 1, 5, 5, 5, 5, 1, 1, 0, 0, 0, 5, 5, 0, 0};
	private static final int[] upSkill = new int[] {3, 3, 3, 3, 4, 3, 3, 3, 3, 4, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4};
	private int iteratorSkill;
	private int iteratorStatus;
	
	public BotTank(Tank player) {
		super(player);
		iteratorSkill = 0;
		iteratorStatus = 0;
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		//upgradeSkill
		upgradeSkill();
		
		player.useSkill(3);
		if(player.getSkillList().get(3).getLevel() > 1) {
			player.useSkill(4);
		}
		
		if(player.getLevel() >= 30) {
			defenseTower();
		}
		else {
			super.move();
		}
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
