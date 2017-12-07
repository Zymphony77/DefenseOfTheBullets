package main.ranking;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.SceneManager;
import main.game.GameComponent;
import main.menu.MenuComponent;

public class RankingHandler {
	private static boolean isMPressed = false;
	
	public static void keyPressed(KeyEvent event) {
		if(event.getCode() == KeyCode.M && !isMPressed) {
			isMPressed = true;
			SceneManager.setMuted(!SceneManager.isMuted());
		}
		
		if(event.getCode() == KeyCode.ENTER) {
			// Start MenuSound
			RankingComponent.getInstance().stopBackground();
			MenuComponent.getInstance().setMoveCount(RankingComponent.getInstance().getMoveCount());
			MenuComponent.getInstance().moveBackground();
			SceneManager.setMenuScene();
			
			GameComponent.getInstance().reset();
		}
	}
	
	public static void keyReleased(KeyEvent event) {
		if(event.getCode() == KeyCode.M) {
			isMPressed = false;
		}
	}
}
