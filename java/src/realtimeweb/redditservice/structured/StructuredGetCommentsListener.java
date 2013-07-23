package realtimeweb.redditservice.structured;

import java.util.ArrayList;
import java.util.HashMap;
import realtimeweb.redditservice.domain.Comment;
/**
 * 
 */
public interface StructuredGetCommentsListener {
	/**
	 * 
	 * @param data
	 */
	public abstract void getCommentsCompleted(ArrayList<Object> data);
	/**
	 * 
	 * @param error
	 */
	public abstract void getCommentsFailed(Exception error);
}
