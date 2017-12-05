package environment;

import javafx.scene.layout.Pane;

import java.util.ArrayList;

import entity.job.*;
//import entity.property.*;

public class StatusPane extends Pane {
	private Novice player;
	private ArrayList<StatusIcon> iconList;
	
	public StatusPane() {
		iconList = new ArrayList<StatusIcon>();
	}
	
	public void update() {
		for(StatusIcon icon: iconList) {
			icon.drawIcon();
		}
	}
	
	public void clear() {
		iconList.clear();
		getChildren().clear();
	}
	
	public void setPlayer(Novice player) {
		clear();
		this.player = player;
		
		// Fill backwards to avoid "Overlapping Canvases"
		for(int i = 5; i >= 0; --i) {
			StatusIcon icon = new StatusIcon(player, i);
			iconList.add(icon);
			getChildren().add(icon);
			if(icon.isUpgradable()) {
				icon.drawUpgrade();
			} else {
				icon.undrawUpgrade();
			}
		}
	}
	
	public ArrayList<StatusIcon> getIconList() {
		return iconList;
	}
}
