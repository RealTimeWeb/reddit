package reddit;

import java.util.ArrayList;

/**
 * The callback class for calling getComments. By making an anonymous subclass of this, you can specify behavior on success or failure.
 * @author acbart
 *
 */
public abstract class JsonCommentListener extends ErrorListener{
	public abstract void onSuccess(String comments);
}
