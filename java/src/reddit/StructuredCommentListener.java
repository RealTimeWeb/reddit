package reddit;

import java.util.ArrayList;

public abstract class StructuredCommentListener extends ErrorListener {
	public abstract void onSuccess(ArrayList<Object> comments);
}
