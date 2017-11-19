package environment;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import main.Main;
import skill.*;
import utility.Pair;

public class SkillIcon extends Canvas {
	private static final int ICON_SIZE = 40;
	private static final int TEXT_SIZE = 10;
	
	private Skill skill;
	
	public SkillIcon(Skill skill) {
		super(ICON_SIZE, ICON_SIZE + TEXT_SIZE);
		
		this.skill = skill;
		
		setTranslateX(getIconShift().first);
		setTranslateY(getIconShift().second);
		
		drawIcon();
	}
	
	public void drawIcon() {
		GraphicsContext gc = getGraphicsContext2D();
		gc.clearRect(0, 0, ICON_SIZE, ICON_SIZE);
		
		if(skill.isReady()) {
			gc.setFill(Color.LIGHTYELLOW);
			gc.fillOval(0, 0, ICON_SIZE, ICON_SIZE);
		} else {
			gc.setFill(Color.DARKGRAY);
			gc.fillOval(0, 0, ICON_SIZE, ICON_SIZE);
		}
		
		gc.drawImage(skill.getImage(), ICON_SIZE / 4, ICON_SIZE / 4, ICON_SIZE / 2, ICON_SIZE / 2);
		
		if(!skill.isReady() && skill instanceof ActiveSkill && skill.getLevel() > 0) {
			gc.setFill(Color.WHITE);
			gc.setFont(Font.font("Telugu MN", FontWeight.EXTRA_BOLD, 30));
			gc.setTextAlign(TextAlignment.CENTER);
			gc.setTextBaseline(VPos.CENTER);
			gc.fillText("" + (int) (((ActiveSkill) skill).getRemainingCooldown() / Main.FRAME_RATE + 1), 
					ICON_SIZE / 2, ICON_SIZE / 2);
		}
		
		gc.setFill(Color.BLACK);
		gc.setFont(new Font("Telugu MN", 8));
		gc.setTextBaseline(VPos.TOP);
		gc.setTextAlign(TextAlignment.LEFT);
		gc.fillText("" + skill.getPosition(), 0, 0);
		
		gc.setFont(new Font("Telugu MN", TEXT_SIZE));
		gc.setTextBaseline(VPos.CENTER);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.fillText(skill.toString(), ICON_SIZE / 2, ICON_SIZE + TEXT_SIZE / 2);
	}
	
	private Pair getIconShift() {
		return new Pair(Main.SCREEN_SIZE - ICON_SIZE - 5, 
				Minimap.MAP_SIZE + skill.getPosition() * (ICON_SIZE + 25) - ICON_SIZE);
	}
}
