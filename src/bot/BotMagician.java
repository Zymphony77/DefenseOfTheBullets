package bot;

import entity.job.Magician;

public class BotMagician extends BotNovice{
	
	private static final int[] upSkill = new int[] {3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
	private int iteratorSkill;
	private boolean activateBuff;
	
	public BotMagician(Magician player) {
		super(player);
		activateBuff = false;
		iteratorSkill = 0;
		
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
	
	
}
