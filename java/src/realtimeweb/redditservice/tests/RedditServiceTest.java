package realtimeweb.redditservice.tests;

import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.junit.Test;

import realtimeweb.redditservice.RedditService;
import realtimeweb.redditservice.domain.Comment;
import realtimeweb.redditservice.domain.Post;
import realtimeweb.stickyweb.EditableCache;
import realtimeweb.stickyweb.exceptions.StickyWebDataSourceNotFoundException;
import realtimeweb.stickyweb.exceptions.StickyWebDataSourceParseException;
import realtimeweb.stickyweb.exceptions.StickyWebInternetException;
import realtimeweb.stickyweb.exceptions.StickyWebInvalidPostArguments;
import realtimeweb.stickyweb.exceptions.StickyWebInvalidQueryString;
import realtimeweb.stickyweb.exceptions.StickyWebLoadDataSourceException;
import realtimeweb.stickyweb.exceptions.StickyWebNotInCacheException;

public class RedditServiceTest {
	
	private void assertPostNotNull(Post p){
			System.out.println(p);
			assertNotNull(p.getAuthor());
			assertNotNull(p.getContent());
			assertNotNull(p.getPermalink());
			assertNotNull(p.getIsNsfw());
			assertNotNull(p.getIsSelf());
			assertNotNull(p.getTitle());
			assertNotNull(p.getId());
			assertNotNull(p.getSubreddit());
			assertNotNull(p.getCreated());
			assertNotNull(p.getDowns());
			assertNotNull(p.getUps());
	}
	
	private void assertCommentNotNull(Comment c) {
		assertNotNull(c.getAuthor());
		assertNotNull(c.getId());
		assertNotNull(c.getSubreddit());
		assertNotNull(c.getCreated());
		assertNotNull(c.getDowns());
		assertNotNull(c.getUps());
	}

	@Test
	public void testPostRedditServiceOnline() {
		RedditService redditService = new RedditService();
		ArrayList<Post> posts = redditService.getPosts("programming", "top");
		for(Post p : posts){
			assertPostNotNull(p);
		}
		
		ArrayList<Comment> comments = redditService.getComments("programming","2osw9r","top" );
		for(Comment c: comments){
			System.out.println(c);
			assertCommentNotNull(c);
		}
	}

	
	
	
	@Test
	public void testRedditServiceCache() {
		RedditService redditService = new RedditService();
		EditableCache recording = new EditableCache();
		String[][] postRequests = {{"programming","top"},{"compsci","new"}};
		String[][] commentRequests = {{"programming","2osw9r","top"},{"compsci","2orf6g","new"}};
		for (String[] request : postRequests) {
			try {
				recording.addData(redditService.getPostsRequest(request[0], request[1]));
				
			} catch (StickyWebNotInCacheException | StickyWebInternetException
					| StickyWebInvalidQueryString
					| StickyWebInvalidPostArguments e) {
				e.printStackTrace();
			}
		}
		
		for (String[] request : commentRequests) {
			try {
				recording.addData(redditService.getCommentsRequest(request[0], request[1], request[2] ));
				
			} catch (StickyWebNotInCacheException | StickyWebInternetException
					| StickyWebInvalidQueryString
					| StickyWebInvalidPostArguments e) {
				e.printStackTrace();
			}
		}
		
		try {
			recording.saveToStream(new FileOutputStream("test-cache.json"));
		} catch (StickyWebDataSourceNotFoundException
				| StickyWebDataSourceParseException
				| StickyWebLoadDataSourceException | FileNotFoundException e) {
			e.printStackTrace();
		}
		
		RedditService redditServiceFromCache = new RedditService("test-cache.json");
		
		
		for (String[] request : postRequests) {
			ArrayList<Post> posts = redditServiceFromCache.getPosts(request[0], request[1]);
			for(Post p : posts){
				assertPostNotNull(p);
			}
		}
		
		for (String[] request : commentRequests) {
			ArrayList<Comment> comments = redditServiceFromCache.getComments(request[0], request[1], request[2]);
			for(Comment c : comments){
				assertCommentNotNull(c);
			}
		}
		
	}
	
}
