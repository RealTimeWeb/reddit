package reddit;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractReddit {
		
	protected static boolean local;
	public boolean isLocal() {
		return local;
	}
	public void setLocal(boolean local) {
		this.local = local;
	}
	
	public static void main(String[] args) {
		Reddit normal = Reddit.getInstance();
		normal.setLocal(true);
		normal.getPosts("virginiatechd", SortMode.TOP, new PostListener() {

			@Override
			public void onSuccess(ArrayList<Post> posts) {
				System.out.println(posts);
				if (!posts.isEmpty()) {
					posts.get(0).getComments(SortMode.NEW, new CommentListener() {

						@Override
						public void onSuccess(ArrayList<Comment> comments) {
							System.out.println("There are "+comments.size()+" top-level comments");
						}
					});
				}
			}
			
		});

		/*StructuredReddit snormal = StructuredReddit.getInstance();
		snormal.setLocal(true);
		snormal.getPosts("virginiatech", SortMode.TOP, new StructuredPostListener() {

			@Override
			public void onSuccess(ArrayList<Object> posts) {
				System.out.println(posts);
			}
			
		});*/
	}
}
