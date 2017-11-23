package buff;

public interface Expirable {
	public void update();
	public int getMaxDuration();
	public int getDuration();
}
