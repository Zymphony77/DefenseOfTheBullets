package utility;

public enum Job {
	NOVICE		("Novice"), 
	TANK			("Tank"), 
	MAGICIAN		("Magician"), 
	RANGER		("Ranger"), 
	GUARDIAN		("Guardian"), 
	PALADIN		("Paladin"), 
	PYROMANCER	("Pyromancer"), 
	CRYOMANCER	("Cryomancer"), 
	CANNONNEER	("Cannonneer"), 
	SNIPER		("Sniper");
	
	private String name;
	
	private Job(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
