package exception;

public class LongNameException extends Exception {
	@Override
	public String getMessage() {
		return "The name should not be longer than 10 characters.";
	}
}
