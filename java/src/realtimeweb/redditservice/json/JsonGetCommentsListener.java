package realtimeweb.redditservice.json;

import java.util.ArrayList;
import java.util.HashMap;
import realtimeweb.redditservice.domain.Comment;
/**
 * 
 */
public interface JsonGetCommentsListener {
	/**
	 * 
	 * @param data
	 */
	public abstract void getCommentsCompleted(String data);
	/**
	 * 
	 * @param error
	 */
	public abstract void getCommentsFailed(Exception error);
}
