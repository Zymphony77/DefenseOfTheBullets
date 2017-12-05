package bot;

import entity.job.Tank;

public class BotTank extends BotNovice{
	
	private static final int[] upSkill = new int[] {3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
	private int cntSkill;
	
	public BotTank(Tank player) {
		super(player);
		cntSkill = 0;
		
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		//upgradeSkill
		upgradeSkill();
		
		player.useSkill(3);
		
		super.move();
	}

	@Override
	protected void upgradeSkill() {
		try {
			if(player.getExperience().getSkillPoint() > 0)
				player.upgradeSkill(upSkill[cntSkill++]);
		}catch(IndexOutOfBoundsException e) {
			e.printStackTrace();
			cntSkill = 0;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
