package bot;

import entity.job.Ranger;

public class BotRanger extends BotNovice{
	
	private static final int[] upSkill = new int[] {4, 4, 4, 4, 3, 4, 3, 3, 3, 4};
	private int iteratorSkill;
	
	public BotRanger(Ranger player) {
		super(player);
		iteratorSkill = 0;
		
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
	
	
}
