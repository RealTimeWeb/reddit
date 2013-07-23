package realtimeweb.redditservice.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * Contains several functions that seriously should have been added to Java
 * core. Or maybe I should have used Guava.
 * 
 * @author acbart
 * 
 */
public class Util {

	/**
	 * Concatenate a list of objects into a single string (using their
	 * "toString" method), joining them with a ",".
	 * 
	 * @param objects
	 *            A list of objects to concatenate.
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String join(List objects) {
		return join(",", objects);
	}

	/**
	 * Concatenate a list of objects into a single string (using their
	 * "toString" method), joining them with a specified glue string.
	 * 
	 * @param glue
	 *            A string to use to join the objects.
	 * @param objects
	 *            A list of objects to concatenate.
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String join(String glue, List objects) {
		StringBuilder sb = new StringBuilder();
		for (Object o : objects) {
			if (sb.length() > 0)
				sb.append(glue);
			sb.append(o.toString());
		}
		return sb.toString();
	}

	/**
	 * Change the precision of a double value, according to the new mantissa.
	 * 
	 * @param d
	 *            The double value you want re-precisioned.
	 * @param mantissa
	 *            The number of digits after the decimal place.
	 * @return
	 */
	public static double formatDecimals(double d, int mantissa) {
		String m = "";
		for (int i = 0; i < mantissa; i++) {
			m += "#";
		}
		DecimalFormat decimalForm = new DecimalFormat("#." + m);
		return Double.valueOf(decimalForm.format(d));
	}
	
	/**
	 * Convert an InputStream to a string.
	 * http://stackoverflow.com/a/309718
	 * @param is
	 * @param bufferSize
	 * @return
	 * @throws IOException 
	 */
	public static String slurp(final InputStream is, final int bufferSize) throws IOException
	{
	  final char[] buffer = new char[bufferSize];
	  final StringBuilder out = new StringBuilder();
	    final Reader in = new InputStreamReader(is, "UTF-8");
	    try {
	      for (;;) {
	        int rsz = in.read(buffer, 0, buffer.length);
	        if (rsz < 0)
	          break;
	        out.append(buffer, 0, rsz);
	      }
	    }
	    finally {
	      in.close();
	    }
	  return out.toString();
	}
	
	
	public static String get(String url, HashMap<String, String> query_args) throws IllegalStateException, IOException, URISyntaxException {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet service= new HttpGet(url);
		
		// Build query string
		URIBuilder ub = new URIBuilder(service.getURI());
		for (Entry<String, String> qa : query_args.entrySet()) {
			ub.addParameter(qa.getKey(), qa.getValue());
		}
		URI uri = ub.build();
		((HttpRequestBase) service).setURI(uri);
		
		// Execute get request
		HttpResponse response = httpclient.execute(service);
		return EntityUtils.toString(response.getEntity());
	}
	
	public static String post(String url, Map<String, String> body_args) throws IllegalStateException, IOException, URISyntaxException {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost service= new HttpPost(url);
		
		// Build body
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (Entry<String, String> ba : body_args.entrySet()) {
			nameValuePairs.add(new BasicNameValuePair(ba.getKey(), ba.getValue()));
		}
		service.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		
		// Execute post request
		HttpResponse response = httpclient.execute(service);
		return EntityUtils.toString(response.getEntity());
	}

	@SuppressWarnings("rawtypes")
	public static String hashRequest(String url, Map<String, String> query_arguments) {
		String queryStrings = "";
		if (query_arguments != null) {
			queryStrings = (new TreeMap(query_arguments)).toString();
		}
		return url + "%" + queryStrings;
	}
}
