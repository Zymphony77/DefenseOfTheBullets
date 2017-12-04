package environment;

import javafx.scene.layout.Pane;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import buff.DouAttBuff;
import entity.job.*;
import main.Main;
import skill.*;
import utility.Pair;

public class SkillIcon extends Pane {
	private static final int ICON_SIZE = 40;
	private static final int TEXT_SIZE = 10;
	private static final Image PLUS_IMAGE = new Image("resource/image/PlusIcon.png");
	
	private Novice player;
	private Skill skill;
	private Canvas icon;
	private Canvas upgrade;
	
	public SkillIcon(Novice player, Skill skill) {
		this.player = player;
		this.skill = skill;
		
		icon = new Canvas(ICON_SIZE + 5, ICON_SIZE + TEXT_SIZE);
		
		icon.setTranslateX(getIconShift().first);
		icon.setTranslateY(getIconShift().second);
		icon.setOpacity(0.75);
		
		upgrade = new Canvas(ICON_SIZE / 2, ICON_SIZE / 2);
		
		upgrade.setTranslateX(getIconShift().first - ICON_SIZE / 2 - 3);
		upgrade.setTranslateY(getIconShift().second + ICON_SIZE / 4);
		
		GraphicsContext gc = upgrade.getGraphicsContext2D();
		
		gc.setFill(Color.MOCCASIN);
		gc.fillOval(0, 0, ICON_SIZE / 2, ICON_SIZE / 2);
		gc.drawImage(PLUS_IMAGE, ICON_SIZE / 12, ICON_SIZE / 12, 
				ICON_SIZE / 3, ICON_SIZE / 3);
		
		drawIcon();
		undrawUpgrade();
		
		getChildren().addAll(icon, upgrade);
	}
	
	public void drawIcon() {
		GraphicsContext gc = icon.getGraphicsContext2D();
		gc.clearRect(0, 0, ICON_SIZE, ICON_SIZE);
		
		if(skill.isReady()) {
			gc.setFill(Color.MOCCASIN);
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
		
		double blockSize = ICON_SIZE / (skill.getMaxLevel() * 4.0);
		
		gc.setFill(Color.MOCCASIN);
		for(int i = 1; i <= skill.getLevel(); ++i) {
			gc.fillRect(ICON_SIZE + 2, ICON_SIZE - 4*blockSize*i, 3, 3*blockSize);
		}
		
		gc.setFill(Color.DARKGRAY);
		for(int i = skill.getLevel() + 1; i <= skill.getMaxLevel(); ++i) {
			gc.fillRect(ICON_SIZE + 2, ICON_SIZE - 4*blockSize*i, 3, 3*blockSize);
		}
		
		gc.setFill(Color.BLACK);
		gc.setFont(new Font("Telugu MN", 10));
		gc.setTextBaseline(VPos.TOP);
		gc.setTextAlign(TextAlignment.LEFT);
		gc.fillText("" + skill.getPosition(), 0, 0);
		
		gc.setFont(new Font("Telugu MN", TEXT_SIZE));
		gc.setTextBaseline(VPos.CENTER);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.fillText(skill.toString(), ICON_SIZE / 2, ICON_SIZE + TEXT_SIZE / 2);
	}
	
	public void drawUpgrade() {
		upgrade.setOpacity(0.75);
		upgrade.setOnMouseClicked(event -> {
			if(event.getButton() == MouseButton.PRIMARY) {
				if(player.getExperience().getSkillPoint() <= 0) {
					undrawUpgrade();
					return;
				}
				player.getExperience().decreaseSkillPoint();
				skill.upgrade();
				if(skill instanceof DouAtt) {
					player.addBuff(new DouAttBuff(player, skill.getLevel()));
				}
				if(!skill.isUpgradable()) {
					undrawUpgrade();
				}
			}
		});
	}
	
	public void undrawUpgrade() {
		upgrade.setOpacity(0);
		upgrade.setOnMouseClicked(event -> {});
	}
	
	private Pair getIconShift() {
		return new Pair(Main.SCREEN_SIZE - ICON_SIZE - 10, 
				Minimap.MAP_SIZE + skill.getPosition() * (ICON_SIZE + 20) - ICON_SIZE);
	}
	
	public boolean isUpgradable() {
		return skill.isUpgradable();
	}
}
