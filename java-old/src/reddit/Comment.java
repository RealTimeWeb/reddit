package reddit;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


/**
 * A Comment is a reply to either a Post or another Comment on Reddit.
 * @author acbart
 *
 */
public class Comment {
	private int ups;
	private int downs;
	private String subreddit;
	private String id;
	private String body;
	private String bodyHtml;
	private Date created;
	private String author;
	private ArrayList<Comment> replies; 
	
	/**
	 * Returns a string representation of this Comment (specifically, the author and it's ID) 
	 */
	public String toString() {
		return "Comment:"+author+"["+id+"]";
	}
	
	Comment(HashMap<String, Object> json) {
		HashMap<String, Object> rawData = (HashMap<String, Object>) json.get("data");
		this.ups = Integer.parseInt(rawData.get("ups").toString());
		this.downs = Integer.parseInt(rawData.get("downs").toString());
		this.created= new java.util.Date(new BigDecimal(rawData.get("created").toString()).longValue());
		this.subreddit = rawData.get("subreddit").toString();
		this.id = rawData.get("id").toString();
		this.author = rawData.get("author").toString();
		this.body = rawData.get("body").toString();
		this.bodyHtml= rawData.get("body_html").toString();
		this.replies = new ArrayList<Comment>();
		
		if (!(rawData.get("replies") instanceof String)) {
			// Recursively generate all the replies for this Comment
			HashMap<String, Object> rawReplies = (HashMap<String, Object>) rawData.get("replies");
			rawData = (HashMap<String, Object>) rawReplies.get("data");
			ArrayList<HashMap<String, Object>> children = (ArrayList<HashMap<String, Object>>) rawData.get("children");
			for (HashMap<String, Object> child : children) {
				this.replies.add(new Comment(child));
			}
		}
	}

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
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @return the bodyHtml
	 */
	public String getBodyHtml() {
		return bodyHtml;
	}

	/**
	 * @return the createdTimestamp
	 */
	public Date getCreatedTimestamp() {
		return created;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @return the replies
	 */
	public ArrayList<Comment> getReplies() {
		return (ArrayList<Comment>) replies.clone();
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
	 * @param body the body to set
	 */
	void setBody(String body) {
		this.body = body;
	}

	/**
	 * @param bodyHtml the bodyHtml to set
	 */
	void setBodyHtml(String bodyHtml) {
		this.bodyHtml = bodyHtml;
	}

	/**
	 * @param createdTimestamp the createdTimestamp to set
	 */
	void setCreatedTimestamp(Date createdTimestamp) {
		this.created = createdTimestamp;
	}

	/**
	 * @param author the author to set
	 */
	void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @param replies the replies to set
	 */
	void setReplies(ArrayList<Comment> replies) {
		this.replies = replies;
	}
	
	Object getAsMap() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("ups", ups);
		map.put("downs", downs);
		map.put("subreddit", subreddit);
		map.put("id", id);
		map.put("body", body);
		map.put("body", bodyHtml);
		map.put("created", created);
		map.put("author", author);
		
		ArrayList<Object> repliesList = new ArrayList<Object>();
		for (Comment comment : replies) {
			repliesList.add(comment.getAsMap());
		}
		map.put("replies", repliesList);

		return map;		
	}
}
