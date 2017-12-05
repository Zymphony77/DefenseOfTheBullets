package environment;

import java.util.ArrayList;

import buff.Buff;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import utility.Job;

public class ClassPane extends Pane{
	private ArrayList<ClassIcon> classList;
	private boolean isShowing;
	
	public ClassPane() {
		classList = new ArrayList<ClassIcon>();
		isShowing = false;
	}
	
	public void showClassPane(){
		addClass(Job.TANK);
		addClass(Job.MAGICIAN);
		addClass(Job.RANGER);
		addInComponent();
		isShowing = true;
	}
	
	public void addClass(Job job) {
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
		isShowing = false;
	}
	
	public boolean isShowing() {
		return isShowing;
	}
}
