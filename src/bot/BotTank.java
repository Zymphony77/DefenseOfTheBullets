package bot;

import entity.job.Tank;

public class BotTank extends BotNovice{
	
	private static final int[] upSkill = new int[] {3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
	private int iteratorSkill;
	
	public BotTank(Tank player) {
		super(player);
		iteratorSkill = 0;
		
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		//upgradeSkill
		upgradeSkill();
		
		player.useSkill(3);
		
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
	
	
}
