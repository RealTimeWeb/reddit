package realtimeweb.redditservice.util;

import java.util.ArrayList;

/**
 * This is a convenient class for converting a list of key-value pairs into a
 * nicely-formatted string.
 * 
 * @author acbart
 * 
 */
public class PropertyString {
	private String name;
	private ArrayList<String> properties;

	/**
	 * Create a new PropertyString with the given name.
	 * @param name
	 */
	public PropertyString(String name) {
		this.name = name;
		this.properties = new ArrayList<String>();
	}

	/**
	 * Add a new key-value property pair to the list of properties.
	 * @param property
	 * @param value
	 * @return
	 */
	public PropertyString add(String property, String value) {
		this.properties.add(property + "=" + value);
		return this;
	}

	/**
	 * Return a nicely-formatted string representation of this PropertyString.
	 */
	public String toString() {
		return name + "{" + Util.join(", ", properties) + "}";
	}
}
