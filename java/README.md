JReddit
=======

A Java API for accessing Reddit

Overview
========
This API will allow you to access Reddit from within Java. Specifically, you can download information about 

* Posts (links and text submitted by Reddit users), and 
* Comments (replies to Posts written by Reddit users). 

Data returned using the API can either be 

* raw JSON strings, 
* a HashMap-based dictionary, or 
* Java classes (Comment and Post). 

In addition, if your internet connection is not stable, or you want your API calls to be idempotent, you can use locally cached data for testing.

Getting Started
===============
You should begin by adding JReddit.jar to your classpath.

Querying Reddit
===============
Begin by getting an instance of the Reddit API:
```java
public class JRedditTest {
	public static void main(String[] args) {
		Reddit reddit = Reddit.getInstance();
		// OR
		// StructuredReddit reddit = StructuredReddit.getInstance();
		// OR
		// JsonReddit reddit = JsonReddit.getInstance();
	}
}
```

You can now either get Posts or Comments. The data returned from these two functions will be passed into a Listener that you specify. When you getPosts, you can filter by subreddit (or you might choose to use "all"). You also sort results, e.g. by newest, by top, by most controversial.

```java
public class JRedditTest {
	public static void main(String[] args) {
		Reddit reddit = Reddit.getInstance();
		reddit.getPosts("virginiatech", SortMode.TOP, new PostListener() {
			public void onSuccess(ArrayList<Post> posts) {  
				System.out.println(posts);
			}
		}
	}
}
```

You can get the comments from a post very directly.

```java
public class JRedditTest {
	public static void main(String[] args) {
		Reddit reddit = Reddit.getInstance();
		reddit.getPosts("virginiatech", SortMode.TOP, new PostListener() {
			public void onSuccess(ArrayList<Post> posts) {  
				if (!posts.isEmpty()) {
					posts.get(0).getComments(SortMode.NEW, new CommentListener() {
						public void onSuccess(ArrayList<Comment> comments) {
							System.out.println("There are "+comments.size()+" top-level comments");
						}
					});
				}
			}
		}
	}
}
```

Exceptions
==========
Using real-time web data can result in a number of exceptions. If you want to do something specifc when exceptions occur, you can override the onFailure callback.

```java
public class JRedditTest {
	public static void main(String[] args) {
		Reddit reddit = Reddit.getInstance();
		reddit.getPosts("virginiatech", SortMode.TOP, new PostListener() {
			public void onSuccess(ArrayList<Post> posts) {  
				System.out.println(posts);
			}
			public void onFailure(Exception e) {
				System.out.println("Oh no, something has gone wrong!");
			}
		}
	}
}
```

Note that you don't need to override onFailure; the default action is to simply print the error to the terminal. However, you must always override onSuccess.

Cache
=====
Using the cache is simple.

```java
public class JRedditTest {
	public static void main(String[] args) {
		Reddit reddit = Reddit.getInstance();
		
		reddit.setLocal(true); // USE THE CACHE!
    
		reddit.getPosts("virginiatech", SortMode.TOP, new PostListener() {
			public void onSuccess(ArrayList<Post> posts) {  
				System.out.println(posts);
			}
		}
	}
}
```

If the data isn't able to be found in the cache, then an empty list will be returned.
