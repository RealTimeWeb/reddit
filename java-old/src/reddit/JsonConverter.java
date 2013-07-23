package reddit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Used internally to convert between JSON and a Structured Representation
 * @author acbart
 *
 */
class JsonConverter {
	private static ContainerFactory CONTAINER_FACTORY = new ContainerFactory(){
	    public List creatArrayContainer() {
		      return new ArrayList();
		    }
	
		    public Map createObjectContainer() {
		      return new HashMap();
		    }
		                        
		  };
	

	static HashMap<String, Object> convertToHashMap(String input) throws ParseException {
		return (HashMap<String, Object>) (new JSONParser()).parse(input, CONTAINER_FACTORY);
	}
	
	static ArrayList<Object> convertToArrayList(String input) throws ParseException {
		return (ArrayList<Object>) (new JSONParser()).parse(input, CONTAINER_FACTORY);
	}
}
