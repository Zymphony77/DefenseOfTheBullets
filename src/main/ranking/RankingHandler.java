package main.ranking;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.SceneManager;
import main.game.GameComponent;

public class RankingHandler {
	public static void keyPressed(KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER) {
			// Start MenuSound
			SceneManager.setMenuScene();
			
			GameComponent.getInstance().reset();
		}
	}
}
