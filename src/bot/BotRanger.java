package bot;

import entity.job.Tank;

public class BotRanger extends BotNovice{
	
	private static final int[] upSkill = new int[] {4, 4, 4, 4, 3, 4, 3, 3, 3, 4};
	private int cntSkill;
	
	public BotRanger(Tank player) {
		super(player);
		cntSkill = 0;
		
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		//upgradeSkill
		upgradeSkill();
		
		if(cntSkill > 4) {
			player.useSkill(4);
		}
		
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
