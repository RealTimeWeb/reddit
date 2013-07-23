package reddit;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.simple.JSONValue;

import exceptions.RedditException;



/**
 * A post is content submitted to Reddit. It can either be a "self-post" (just text) or a URL.
 * @author acbart
 *
 */
public class Post {
	/**
	 * Returns a string representation of this Post (specifically, the title and ID)
	 */
	public String toString() {
		return this.title + "["+this.id+"]";
	}
	
	/**
	 * Gets the comments for this Post.
	 * 
	 * @param post The Post you want Comments from.
	 * @param sort The order in which you want the results to be sorted.
	 * @param callback A function that will process the results or errors.
	 * @throws RedditException
	 */
	public void getComments(final SortMode sort, final CommentListener callback) {
		Reddit.getInstance().getComments(this.subreddit, this.id, sort, callback);
	}
	
	public Post(HashMap<String, Object> json) {
		HashMap<String, Object> rawData = (HashMap<String, Object>) json.get("data");
		this.ups = Integer.parseInt(rawData.get("ups").toString());
		this.downs = Integer.parseInt(rawData.get("downs").toString());
		this.created= new java.util.Date(new BigDecimal(rawData.get("created").toString()).longValue());
		this.subreddit = rawData.get("subreddit").toString();
		this.id = rawData.get("id").toString();
		this.title = rawData.get("title").toString();
		this.author = rawData.get("author").toString();
		this.isSelf = ( "true" == rawData.get("is_self").toString() ? true : false);
		this.isNsfw = ( "true" == rawData.get("over_18").toString() ? true : false);
		if (this.isSelf) {
			this.content = rawData.get("selftext").toString();
		} else {
			this.content = rawData.get("url").toString();
		}
		
		this.setPermalink("reddit.com"+rawData.get("permalink").toString());
	}
	private int ups;
	private int downs;
	private String subreddit;
	private String id;
	private String title;
	private String content;
	private boolean isSelf;
	private boolean isNsfw;
	private Date created;
	private String author;
	private String permalink;
	/**
	 * @return the ups
	 */
	public int getUps() {
		return ups;
	}
	/**
	 * @return the downs
	 */
	public int getDowns() {
		return downs;
	}
	/**
	 * @return the subreddit
	 */
	public String getSubreddit() {
		return subreddit;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @return the isSelf
	 */
	public boolean isSelf() {
		return isSelf;
	}
	/**
	 * @return the nsfw
	 */
	public boolean isNsfw() {
		return isNsfw;
	}
	/**
	 * @return the createdTimestamp
	 */
	public Date getCreated() {
		return created;
	}
	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * @param ups the ups to set
	 */
	void setUps(int ups) {
		this.ups = ups;
	}
	/**
	 * @param downs the downs to set
	 */
	void setDowns(int downs) {
		this.downs = downs;
	}
	/**
	 * @param subreddit the subreddit to set
	 */
	void setSubreddit(String subreddit) {
		this.subreddit = subreddit;
	}
	/**
	 * @param id the id to set
	 */
	void setId(String id) {
		this.id = id;
	}
	/**
	 * @param title the title to set
	 */
	void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @param content the content to set
	 */
	void setContent(String content) {
		this.content = content;
	}
	/**
	 * @param isSelf the isSelf to set
	 */
	void setSelf(boolean isSelf) {
		this.isSelf = isSelf;
	}
	/**
	 * @param nsfw the nsfw to set
	 */
	void setNsfw(boolean nsfw) {
		this.isNsfw = nsfw;
	}
	/**
	 * @param createdTimestamp the createdTimestamp to set
	 */
	void setCreated(Date createdTimestamp) {
		this.created = createdTimestamp;
	}
	/**
	 * @param author the author to set
	 */
	void setAuthor(String author) {
		this.author = author;
	}
	public String getPermalink() {
		return permalink;
	}
	void setPermalink(String permalink) {
		this.permalink = permalink;
	}

	Object getAsMap() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("ups", ups);
		map.put("downs", downs);
		map.put("subreddit", subreddit);
		map.put("id", id);
		map.put("title", title);
		map.put("content", content);
		map.put("is_self", isSelf);
		map.put("is_nsfw", isNsfw);
		map.put("created", created);
		map.put("author", author);
		map.put("permalink", permalink);
		return map;
	}
}
