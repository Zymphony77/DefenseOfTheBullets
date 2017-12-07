package bot;

import entity.job.Tank;
import skill.*;

public class BotTank extends BotNovice{
	
	private static final int[] upStatus = new int[] {1, 4, 1, 4, 0, 4, 1, 4, 0, 4, 0, 1, 0, 1, 2, 0, 1, 2, 1, 2, 1, 2, 0, 1, 0, 1, 1, 5, 5, 5, 5, 1, 1, 0, 1, 1, 5, 5, 0, 0};
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
		
		if(((ActiveSkill) player.getSkillList().get(2)).isReady()) {
			player.useSkill(3);
		}
		if(((ActiveSkill) player.getSkillList().get(3)).isReady()) {
			player.useSkill(4);
		}
		
		super.move();
		
		if(player.getLevel() >= 35 && player.getHp() >= 0.5 * player.getMaxHp()) {
			defenseTower();
		}
	}

	@Override
	protected void upgradeSkill() {
		try {
			if(player.getExperience().getSkillPoint() > 0 && iteratorSkill < upSkill.length) {
				player.upgradeSkill(upSkill[iteratorSkill++]);
			}
		}catch(IndexOutOfBoundsException e) {
			System.out.println("BugTankSkill 1" + player.getLevel());
			iteratorSkill = 0;
		}catch(Exception e) {
			System.out.println("BugTankSkill 2" + player.getLevel());
			iteratorSkill = 0;
		}
	}
	
	@Override
	protected void upgradeStatus() {
		try {
			if(player.getExperience().getPointStatus() > 0 && iteratorStatus < upStatus.length) {
				player.upgradeStatus(upStatus[iteratorStatus++]);
			}
		}catch(IndexOutOfBoundsException e) {
			System.out.println("BugTankStatus 1" + player.getLevel());
			iteratorStatus = 0;
		}catch(Exception e) {
			System.out.println("BugTankStatus 2" + player.getLevel());
			iteratorStatus = 0;
		}
	}
	
}
