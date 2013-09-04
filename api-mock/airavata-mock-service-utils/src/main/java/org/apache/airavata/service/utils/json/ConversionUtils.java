package org.apache.airavata.service.utils.json;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class ConversionUtils {
	@SuppressWarnings("unchecked")
	public static <T> T getJavaObject(String jsonString,
			List<Class<?>> referenceClasses, Class<?> T) throws Exception {
		Object obj = getJavaObject(jsonString, referenceClasses);
		try {
			T.cast(obj);
			return (T)obj;
		} catch (ClassCastException e) {
			throw new Exception("Object is not of the type "+T.getCanonicalName());
		}
	}

	public static String getJSONString(Object o) throws JsonGenerationException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(o);
	}
	public static Object getJavaObject(String jsonString,
			List<Class<?>> referenceClasses) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Object obj = null;
		for (Class<?> c : referenceClasses) {
			try {
				obj = mapper.readValue(jsonString, c);
				break;
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (obj==null){
			throw new Exception("Invalid JSON String");
		}
		return obj;
	}
}
