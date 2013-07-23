package reddit;

import java.util.ArrayList;

/**
 * The callback class for calling getPosts. By making an anonymous subclass of this, you can specify behavior on success or failure.
 * @author acbart
 *
 */
public abstract class PostListener extends ErrorListener {
	public abstract void onSuccess(ArrayList<Post> posts);
}
