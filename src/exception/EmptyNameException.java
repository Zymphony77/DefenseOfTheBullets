package exception;

public class EmptyNameException extends Exception {
	@Override
	public String getMessage() {
		return "The name should not be null.";
	}
}
