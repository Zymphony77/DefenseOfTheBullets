package bot;

import entity.job.Magician;
import skill.*;

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
		
		if(activateBuff == false && ((ActiveSkill) player.getSkillList().get(2)).isReady()) {
			activateBuff = true;
			player.useSkill(3);
		}
		
		super.move();
	}

	@Override
	protected void upgradeSkill() {
		try {
			if(player.getExperience().getSkillPoint() > 0 && iteratorSkill < upSkill.length) {
				player.upgradeSkill(upSkill[iteratorSkill++]);
			}
		}catch(IndexOutOfBoundsException e) {
			iteratorSkill = 0;
			System.out.println("BugMagicianSkill 1" + player.getLevel());
		}catch(Exception e) {
			iteratorSkill = 0;
			System.out.println("BugMagicianSkill 2" + player.getLevel());
		}
	}
	
	@Override
	protected void upgradeStatus() {
		try {
			if(player.getExperience().getPointStatus() > 0 && iteratorStatus < upStatus.length) {
				player.upgradeStatus(upStatus[iteratorStatus++]);
			}
		}catch(IndexOutOfBoundsException e) {
			iteratorStatus = 0;
			System.out.println("BugMagicianStatus 1" + player.getLevel());
		}catch(Exception e) {
			System.out.println("BugMagicianStatus 2" + player.getLevel());
		}
	}
	
}
