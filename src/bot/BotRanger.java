package bot;

import entity.job.Ranger;

public class BotRanger extends BotNovice{
	
	private static final int[] upStatus = new int[] {2, 4, 2, 4, 2, 4, 2, 4, 2, 4, 5, 0, 5, 0, 1, 5, 2, 1, 5, 2, 1, 5, 5, 5, 2, 1, 1, 5, 5, 5, 5, 1, 1, 0, 0, 0, 5, 5, 0, 0};
	private static final int[] upSkill = new int[] {3, 3, 3, 3, 3, 4, 4, 4, 4, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4};
	private int iteratorSkill;
	private int iteratorStatus;
	
	public BotRanger(Ranger player) {
		super(player);
		iteratorSkill = 0;
		iteratorStatus = 0;
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		//upgradeSkill
		upgradeSkill();
		
		if(iteratorSkill > 4) {
			player.useSkill(4);
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
		}
	}
	
	
}
