package reddit;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


class Requests {
	/**
	 * Make a GET request of a RESTful server, return the results 
	 * @param request
	 * @param urlParameters
	 * @return
	 * @throws IOException
	 */
	static String get(String request, String urlParameters) throws IOException {
	    // i.e.: request = "http://example.com/index.php?param1=a&param2=b&param3=c";
		request = request + "?" + urlParameters;
	    URL url = new URL(request); 
	    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	    connection.setDoOutput(true); 
	    connection.setInstanceFollowRedirects(false); 
	    connection.setRequestMethod("GET"); 
	    connection.setRequestProperty("Content-Type", "text/plain"); 
	    connection.setRequestProperty("charset", "utf-8");
	    connection.connect();
	    
	    StringBuilder response = new StringBuilder();
	    String line;
	    BufferedReader reader;
	    
	    if (connection.getResponseCode() == 200) {
	    	reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	    } else {
	    	reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
	    }
		while ((line = reader.readLine()) != null) {
			response.append(line);
		}
		reader.close();
		
		connection.disconnect();
		return response.toString();
	}
	
	/**
	 * Make a POST request to a RESTful server
	 * Source: http://stackoverflow.com/questions/4205980/java-sending-http-parameters-via-post-method-easily
	 * @param request
	 * @param urlParameters
	 * @return
	 * @throws IOException 
	 */
	static String post(String request, String urlParameters ) throws IOException {
		URL url = new URL(request); 
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();           
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setInstanceFollowRedirects(false); 
		connection.setRequestMethod("POST"); 
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
		connection.setRequestProperty("charset", "utf-8");
		connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
		connection.setUseCaches (false);

		DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
		wr.writeBytes(urlParameters);
		wr.flush();
		
		StringBuilder response = new StringBuilder();
	    String line;
	    
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		while ((line = reader.readLine()) != null) {
			response.append(line);
		}
		
		wr.close();
		reader.close();
		connection.disconnect();
		return response.toString();
	}

	/**
	 * Turn a key-value pair into a properly formatted GET url argument
	 * @param key
	 * @param value
	 * @return
	 */
	static String makeParameter(String key, Object value) {
		return key + "=" + value.toString();
	}
}
