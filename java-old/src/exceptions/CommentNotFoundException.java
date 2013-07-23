package exceptions;

public class CommentNotFoundException extends RedditException {

	public CommentNotFoundException(String error) {
		super(error);
		NAME = "The Comment Could Not Be Found";
	}

}
