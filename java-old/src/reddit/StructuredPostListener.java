package reddit;

import java.util.ArrayList;

public abstract class StructuredPostListener extends ErrorListener {
	public abstract void onSuccess(ArrayList<Object> posts);
}
