package exception;

public class UnsupportedCharacterException extends Exception {
	public String text;
	public UnsupportedCharacterException(String text) {
		this.text = text;
	}
	
	@Override
	public String getMessage() {
		return "Character [" + text + "] is not allowed.";
	}
}
