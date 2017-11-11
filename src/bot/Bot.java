package bot;

import java.util.LinkedList;

import entity.Entity;
import entity.bullet.Bullet;
import entity.food.Food;
import entity.job.Novice;
import main.Component;
import main.Main;
import utility.Job;

public abstract class Bot {
	protected Entity player;
	protected int target;
	protected boolean chkMove = false;
	protected Job job;
	protected LinkedList<Novice> playerFriendList = new LinkedList<Novice>();
	protected LinkedList<Novice> playerEnemiesList = new LinkedList<Novice>();
	protected LinkedList<Bullet> bulletList = new LinkedList<Bullet>();
	protected LinkedList<Food> foodList = new LinkedList<Food>();
	
	public abstract void move();
	protected abstract void farm();
	protected abstract void upgradeSkill();
	protected abstract void upgradeStatus();
	protected abstract void upgradeClass();
	protected abstract void chooseTarget();
	
	public void findEntityInRange()
	{
		for (Novice tmp : Component.getInstance().getPlayerList()) {
			double x = Math.abs(tmp.getRefPoint().first - player.getRefPoint().first);
			double y = Math.abs(tmp.getRefPoint().second - player.getRefPoint().second);
		    if(x < Main.SCREEN_SIZE/2.0 && y < Main.SCREEN_SIZE/2.0 && tmp.getSide() != player.getSide()){
		    		playerEnemiesList.add(tmp);
		    }
		    else {
		    		playerFriendList.add(tmp);
		    }
		}
		
		for (Bullet tmp : Component.getInstance().getBulletList()) {
			double x = Math.abs(tmp.getRefPoint().first - player.getRefPoint().first);
			double y = Math.abs(tmp.getRefPoint().second - player.getRefPoint().second);
			if(x < Main.SCREEN_SIZE/2.0 && y < Main.SCREEN_SIZE/2.0 && tmp.getSide() != player.getSide()){
		    		bulletList.add(tmp);
		    }
		}
		
		for (Food tmp : Component.getInstance().getFoodList()) {
			double x = Math.abs(tmp.getRefPoint().first - player.getRefPoint().first);
			double y = Math.abs(tmp.getRefPoint().second - player.getRefPoint().second);
			if(x < Main.SCREEN_SIZE/2.0 && y < Main.SCREEN_SIZE/2.0) {
				foodList.add(tmp);
			}
		}
	}
}
