package org.apache.airavata.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.airavata.core.application.ApplicationDescriptor;
import org.apache.airavata.core.application.ApplicationParameter;
import org.apache.airavata.core.application.LocalApplicationDescriptor;
import org.apache.airavata.core.application.ParameterType;
import org.apache.airavata.service.utils.ServiceUtils;
import org.apache.airavata.service.utils.help.HTMLHelpData;
import org.apache.airavata.service.utils.help.HelpData;
import org.apache.airavata.service.utils.help.MethodUtils;
import org.apache.airavata.service.utils.json.ConversionUtils;
import org.apache.airavata.service.utils.model.ApplicationDescriptorJSONFacotry;
import org.apache.airavata.service.utils.model.DataList;
import org.apache.airavata.service.utils.path.ApplicationPath;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * curl -X PUT http://127.0.0.1:9090/orders-server/orders/1?customer_name=bob
 * curl -X GET http://127.0.0.1:9090/orders-server/orders/1 curl -X GET
 * http://127.0.0.1:9090/orders-server/orders/list
 */

@Path(ApplicationPath.SERVICE_PATH)
public class ApplicationService {
	@Context
	UriInfo uriInfo;
	
	@Path(ApplicationPath.ADD_APPLICATION)
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String add(@QueryParam("application") String application) {
		ApplicationDescriptor obj;
		try {
			obj = ConversionUtils.getJavaObject(application, ApplicationDescriptorJSONFacotry.getInstance().getTypes(), ApplicationDescriptor.class);
			String message=obj.getApplicationName()+" application added.";
			System.out.println(message);
			return obj.getApplicationName();
		} catch (Exception e) {
			throw new WebApplicationException(e);
		}
	}
	
	@Path(ApplicationPath.ADD_APPLICATION_HELP)
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String showHelp() {
		HelpData helpData = new HTMLHelpData("New Application","Add the details of how to access an application from Airavata");
		try {
			URI uri = ServiceUtils.getServiceOperationURIFromHelpURI(uriInfo);
			helpData.getSyntax().add(uri.toString()+"?application=&ltJSONString&gt");
			helpData.getParameters().put("application", "Describes the application access data in JSON format. The supported JSON types are listed in the 'Notes' section.");
			List<Class<?>> types = ApplicationDescriptorJSONFacotry.getInstance().getTypes();
			for (Class<?> cl : types) {
				String help="";
				help+="<h3>"+ApplicationDescriptorJSONFacotry.getInstance().getTypeName(cl)+"</h3>\n";
				help+="\t "+ApplicationDescriptorJSONFacotry.getInstance().getTypeDescription(cl)+"<br />\n";
				help+="\t JSON template:\n"+"\t\t"+ApplicationDescriptorJSONFacotry.getInstance().getJSONTypeTemplate(cl)+"\n";
				helpData.getNotes().add(help);
			}
			helpData.getExamples().add(uri.toString()+"?application={%22applicationName%22:%22echoApp%22,%22inputs%22:[{%22name%22:%22input_val%22,%22value%22:%22test%22,%22type%22:%22STRING%22}],%22outputs%22:[{%22name%22:%22output_val%22,%22value%22:%22test%22,%22type%22:%22STRING%22}],%22executablePath%22:null,%22scratchLocation%22:null}");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("help called");
		return MethodUtils.getHelpString(helpData);
	}

	public static void main(String[] args) throws JsonGenerationException,
			JsonMappingException, IOException {
		ApplicationDescriptor aa = new LocalApplicationDescriptor();
		aa.setApplicationName("echoApp");
		aa.getInputs().add(
				new ApplicationParameter("input_val", "test",
						ParameterType.STRING));
		aa.getOutputs().add(
				new ApplicationParameter("output_val", "test",
						ParameterType.STRING));
		ObjectMapper mapper = new ObjectMapper();
		String s = mapper.writeValueAsString(aa);
		System.out.println(s);
		DataList d = new DataList();
		ArrayList<String> list = new ArrayList<String>();
		list.add("msg_part1=Hello");
		list.add("msg_part2=World");
		d.setList(list);
		System.out.println(mapper.writeValueAsString(d));
		// A bb = mapper.readValue(s, AA.class);
		// System.out.println(bb.getValue());
	}

	public static interface A {
		public String getValue();

		public void setValue(String value);
	}

	public static class AA implements A {
		private String value;

		@Override
		public String getValue() {
			return value;
		}

		@Override
		public void setValue(String value) {
			this.value = value;
		}

	}
}