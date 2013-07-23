package realtimeweb.redditservice.main;

import realtimeweb.redditservice.json.JsonRedditService;
import realtimeweb.redditservice.regular.RedditService;
import realtimeweb.redditservice.structured.StructuredRedditService;

public class RedditTest {
	public static void main(String[] args) {
		RedditService json = RedditService.getInstance();
		json.connect();
		try {
			System.out.println(json.getComments("all", json.getPosts("all", "top").get(0).getId(), "top").get(0).getAuthor());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
