package exception;

public class DeleteNullException extends Exception {
	@Override
	public String getMessage() {
		return "No characters to be deleted.";
	}
}
