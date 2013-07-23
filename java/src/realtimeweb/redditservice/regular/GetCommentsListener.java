package realtimeweb.redditservice.regular;

import java.util.ArrayList;
import java.util.HashMap;
import realtimeweb.redditservice.domain.Comment;
/**
 * 
 */
public interface GetCommentsListener {
	/**
	 * 
	 * @param data
	 */
	public abstract void getCommentsCompleted(ArrayList<Comment> data);
	/**
	 * 
	 * @param error
	 */
	public abstract void getCommentsFailed(Exception error);
}
