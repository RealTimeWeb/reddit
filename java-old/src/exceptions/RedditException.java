package exceptions;

public class RedditException extends Exception {
	private String error;
	protected String NAME = "Generic Reddit Exception";

	public RedditException(String error) {
		this.error = error;
	}
	
	public String toString() {
		return NAME + ": "+this.error;
	}
}
