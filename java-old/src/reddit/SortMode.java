package reddit;

/**
 * The different choices for sorting results from the Reddit Service
 * <ul>
 * <li><b>Top</b> is a listing of the highest scoring submissions to Reddit, and
 * comments on submissions, regardless of their age.</li>
 * <li><b>New</b> shows the most recent ones first, regardless of their score.</li>
 * <li><b>Hot</b> is a combination of the two, and the default sort order for
 * links.</li>
 * <li><b>Best</b> is a somewhat complicated metric ( explained here) that tends
 * to magically put the best comments first. However, it is not available for
 * Posts, only Comments; using it with a Post will return the TOP results.</li>
 * <li><b>Controversial</b> is where you will find the most controversial
 * submissions and comments.</li>
 * <li><b>Random</b> will simply return results at random.</li>
 * </ul>
 * 
 * @author acbart
 * 
 */
public enum SortMode {
	TOP, NEW, RISING, HOT, CONTROVERSIAL, OLD, RANDOM, BEST;

	public String toString() {
		switch (this) {
		case TOP:
			return "top";
		case BEST:
			return "best";
		case NEW:
		case RISING:
			return "new";
		case HOT:
			return "hot";
		case CONTROVERSIAL:
			return "controversial";
		case OLD:
			return "old";
		case RANDOM:
			return "random";
		}
		return "new";
	}

	String getUrlArguments() {
		if (this == NEW) {
			return Requests.makeParameter("sort", "new");
		} else if (this == RISING) {
			return Requests.makeParameter("sort", "rising");
		} else {
			return "";
		}
	}
}
