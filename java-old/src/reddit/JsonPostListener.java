package reddit;

public abstract class JsonPostListener extends ErrorListener {
	public abstract void onSuccess(String posts);
}
