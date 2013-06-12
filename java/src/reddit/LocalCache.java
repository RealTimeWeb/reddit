package reddit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.json.simple.parser.ParseException;

class LocalCache {
	
	private static LocalCache instance;
	private static HashMap<String, Object> cache;

	/*
	 * Protected Constructor to defeat instantiation Common Singleton pattern
	 * (google for more info!)
	 */
	protected LocalCache() {
		BufferedReader rawCache = new BufferedReader(new InputStreamReader(LocalCache.class.getResourceAsStream("../data/local.json")));
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			while ( (line = rawCache.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			System.err.println("JReddit internal error. Could not find the local.json file! Please report this error and the following error.");
			e.printStackTrace();
		}
		String json = sb.toString();
		
		try {
			cache = (HashMap<String, Object>) (JsonConverter
					.convertToHashMap(json));
		} catch (ParseException e) {
			System.err.println("JReddit internal error. Could not parse the local.json file! Please report this error and the following error.");
			e.printStackTrace();
		}
	}

	/**
	 * Get the global instance of the Reddit connection.
	 * 
	 * @return
	 */
	public static HashMap<String, Object> getInstance() {
		if (instance == null) {
			synchronized (LocalCache.class) {
				if (instance == null) {
					instance = new LocalCache();
				}
			}
		}
		return instance.getCache();
	}

	private HashMap<String, Object> getCache() {
		return cache;
	}
}
