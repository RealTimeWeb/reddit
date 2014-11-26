package realtimeweb.redditservice.main;

import realtimeweb.redditservice.json.JsonRedditService;
import realtimeweb.redditservice.regular.RedditService;
import realtimeweb.redditservice.structured.StructuredRedditService;

public class RedditTest {
	public static void main(String[] args) {
		RedditService regular = RedditService.getInstance();
		regular.connect();
		try {
			System.out.println(regular.getComments("all", regular.getPosts("all", "top").get(0).getId(), "top").get(0).getAuthor());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JsonRedditService json = JsonRedditService.getInstance();
		json.connect();
		try {
			System.out.println(json.getPosts("all", "top"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StructuredRedditService structured = StructuredRedditService.getInstance();
		structured.connect();
		try {
			System.out.println(structured.getPosts("all", "top").get("kind"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
