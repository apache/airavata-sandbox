package org.apache.airavata.service.utils.model;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

public interface JSONObjectFactory {
	public List<Class<?>> getTypes();
	public String getTypeName(Class<?> cl);
	public String getTypeDescription(Class<?> cl);
	public String getJSONTypeTemplate(Class<?> cl) throws JsonGenerationException, JsonMappingException, IOException;

}
