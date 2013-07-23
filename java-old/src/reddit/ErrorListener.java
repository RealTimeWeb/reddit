package reddit;

public class ErrorListener {
	public void onFailure(Exception exception) {
		System.err.println(exception);
	}
}
