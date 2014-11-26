package realtimeweb.redditservice.domain;


import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * 
 */
public class Post {
	
	
	private int ups;
	private int downs;
	private int created;    //epoch time, save as long, convert to date
	private String subreddit;
	private String id;
	private String title;
	private String author;
	private boolean isSelf;
	private boolean isNsfw;
	private String content;    //if is self then use data->url
	private String permalink;    //prepend reddit.com to this
	
	
	/**
	 * The number of upvotes associated with this Post.
	
	 * @return int
	 */
	public int getUps() {
		return this.ups;
	}
	
	/**
	 * 
	 * @param ups The number of upvotes associated with this Post.
	 */
	public void setUps(int ups) {
		this.ups = ups;
	}
	
	/**
	 * The number of downvotes associated with this Post.
	
	 * @return int
	 */
	public int getDowns() {
		return this.downs;
	}
	
	/**
	 * 
	 * @param downs The number of downvotes associated with this Post.
	 */
	public void setDowns(int downs) {
		this.downs = downs;
	}
	
	/**
	 * The date that this Post was created.
	
	 * @return int
	 */
	public int getCreated() {
		return this.created;
	}
	
	/**
	 * 
	 * @param created The date that this Post was created.
	 */
	public void setCreated(int created) {
		this.created = created;
	}
	
	/**
	 * The subreddit that this Post was made in.
	
	 * @return String
	 */
	public String getSubreddit() {
		return this.subreddit;
	}
	
	/**
	 * 
	 * @param subreddit The subreddit that this Post was made in.
	 */
	public void setSubreddit(String subreddit) {
		this.subreddit = subreddit;
	}
	
	/**
	 * A unique ID for this Post. A combination of letters, numbers, and dashes.
	
	 * @return String
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * 
	 * @param id A unique ID for this Post. A combination of letters, numbers, and dashes.
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * The title of this Post.
	
	 * @return String
	 */
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * 
	 * @param title The title of this Post.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * The username of the author of this Post.
	
	 * @return String
	 */
	public String getAuthor() {
		return this.author;
	}
	
	/**
	 * 
	 * @param author The username of the author of this Post.
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	
	/**
	 * Whether or not this Post was text (True), or a URL (False).
	
	 * @return boolean
	 */
	public boolean getIsSelf() {
		return this.isSelf;
	}
	
	/**
	 * 
	 * @param isSelf Whether or not this Post was text (True), or a URL (False).
	 */
	public void setIsSelf(boolean isSelf) {
		this.isSelf = isSelf;
	}
	
	/**
	 * Whether or not this Post is Not Safe for Work (NSFW).
	
	 * @return boolean
	 */
	public boolean getIsNsfw() {
		return this.isNsfw;
	}
	
	/**
	 * 
	 * @param isNsfw Whether or not this Post is Not Safe for Work (NSFW).
	 */
	public void setIsNsfw(boolean isNsfw) {
		this.isNsfw = isNsfw;
	}
	
	/**
	 * The text of the post, or a url if it is not a self Post.
	
	 * @return String
	 */
	public String getContent() {
		return this.content;
	}
	
	/**
	 * 
	 * @param content The text of the post, or a url if it is not a self Post.
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * A permanent url that directs to this Post.
	
	 * @return String
	 */
	public String getPermalink() {
		return this.permalink;
	}
	
	/**
	 * 
	 * @param permalink A permanent url that directs to this Post.
	 */
	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}
	
	
	
	/**
	 * 
	
	 * @return String
	 */
	public String toString() {
		return "Post[" + ups + ", " + downs + ", " + created + ", " + subreddit + ", " + id + ", " + title + ", " + author + ", " + isSelf + ", " + isNsfw + ", " + content + ", " + permalink + "]";
	}
	
	/**
	 * Internal constructor to create a Post from a Json representation.
	 * @param json The raw json data that will be parsed.
	 * @param gson The Gson parser. See <a href='https://code.google.com/p/google-gson/'>https://code.google.com/p/google-gson/</a> for more information.
	 * @return 
	 */
	public  Post(JsonObject json, Gson gson) {
		this.ups = json.get("data").getAsJsonObject().get("ups").getAsInt();
		this.downs = json.get("data").getAsJsonObject().get("downs").getAsInt();
		this.created = json.get("data").getAsJsonObject().get("created").getAsInt();
		this.subreddit = json.get("data").getAsJsonObject().get("subreddit").getAsString();
		this.id = json.get("data").getAsJsonObject().get("id").getAsString();
		this.title = json.get("data").getAsJsonObject().get("title").getAsString();
		this.author = json.get("data").getAsJsonObject().get("author").getAsString();
		this.isSelf = json.get("data").getAsJsonObject().get("is_self").getAsBoolean();
		this.isNsfw = json.get("data").getAsJsonObject().get("over_18").getAsBoolean();
		this.content = json.get("data").getAsJsonObject().get("selftext").getAsString();
		this.permalink = json.get("data").getAsJsonObject().get("permalink").getAsString();
	}
	
	/**
	 * Regular constructor to create a Post.
	 * @param ups The number of upvotes associated with this Post.
	 * @param downs The number of downvotes associated with this Post.
	 * @param created The date that this Post was created.
	 * @param subreddit The subreddit that this Post was made in.
	 * @param id A unique ID for this Post. A combination of letters, numbers, and dashes.
	 * @param title The title of this Post.
	 * @param author The username of the author of this Post.
	 * @param isSelf Whether or not this Post was text (True), or a URL (False).
	 * @param isNsfw Whether or not this Post is Not Safe for Work (NSFW).
	 * @param content The text of the post, or a url if it is not a self Post.
	 * @param permalink A permanent url that directs to this Post.
	 * @return 
	 */
	public  Post(int ups, int downs, int created, String subreddit, String id, String title, String author, boolean isSelf, boolean isNsfw, String content, String permalink) {
		this.ups = ups;
		this.downs = downs;
		this.created = created;
		this.subreddit = subreddit;
		this.id = id;
		this.title = title;
		this.author = author;
		this.isSelf = isSelf;
		this.isNsfw = isNsfw;
		this.content = content;
		this.permalink = permalink;
	}
	
}
