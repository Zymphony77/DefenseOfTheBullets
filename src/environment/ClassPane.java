package environment;

import java.util.ArrayList;

import buff.Buff;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class ClassPane extends Pane{
	ArrayList<ClassIcon> classList;
	
	public ClassPane() {
		classList = new ArrayList<ClassIcon>();
	}
	
	public void showClassPane(){
		addBuff("tank");
		addBuff("mage");
		addBuff("ranger");
		addInComponent();
	}
	
	public void addBuff(String job) {
		ClassIcon icon = new ClassIcon(job, classList.size());
		classList.add(icon);
	}
	
	public void addInComponent() {
		for(int i = classList.size() - 1; i >= 0; --i) {
			getChildren().add(classList.get(i));
		}
	}
	
	public void clear() {
		classList.clear();
		getChildren().clear();
	}
}
