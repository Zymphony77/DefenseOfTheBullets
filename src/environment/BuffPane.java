package environment;

import javafx.scene.layout.Pane;

import java.util.ArrayList;

import entity.job.*;
import buff.*;

public class BuffPane extends Pane {
	private ArrayList<BuffIcon> iconList;
	
	public BuffPane() {
		iconList = new ArrayList<BuffIcon>();
	}
	
	public void update() {
		for(BuffIcon icon: iconList) {
			if(icon.getBuff() instanceof Expirable) {
				icon.update();
			}
		}
		
		for(BuffIcon icon: iconList) {
			if(!icon.getBuff().isActive()) {
				getChildren().remove(icon);
			}
		}
		
		iconList.removeIf(icon -> {
			return !icon.getBuff().isActive();
		});
		
		for(int i = 0; i < iconList.size(); ++i) {
			iconList.get(i).setPosition(i);
		}
	}
	
	public void addBuff(Buff buff) {
		BuffIcon icon = new BuffIcon(buff, iconList.size());
		iconList.add(icon);
		getChildren().add(icon);
	}
	
	public void removeBuff(Buff buff) {
		for(BuffIcon icon: iconList) {
			if(icon.getBuff().getClass() == buff.getClass()) {
				icon.clear();
				getChildren().remove(icon);
				iconList.remove(icon);
				break;
			}
		}
	}
	
	public void clear() {
		for(BuffIcon icon: iconList) {
			icon.getBuff().deactivateBuff();
		}
		iconList.clear();
		getChildren().clear();
	}
}
