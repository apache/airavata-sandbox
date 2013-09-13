package org.apache.airavata.service;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.airavata.service.utils.ServiceUtils;
import org.apache.airavata.service.utils.help.HTMLHelpData;
import org.apache.airavata.service.utils.help.HelpData;
import org.apache.airavata.service.utils.help.MethodUtils;
import org.apache.airavata.service.utils.path.ApplicationPath;
import org.apache.airavata.service.utils.path.ExperimentPath;
import org.apache.airavata.service.utils.path.MainHelpPath;

/**
 * curl -X PUT http://127.0.0.1:9090/orders-server/orders/1?customer_name=bob
 * curl -X GET http://127.0.0.1:9090/orders-server/orders/1 curl -X GET
 * http://127.0.0.1:9090/orders-server/orders/list
 */

@Path(MainHelpPath.SERVICE_PATH)
public class HelpService {
	@Context
	UriInfo uriInfo;
	
	@Path(MainHelpPath.ENTRY)
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String add() throws URISyntaxException {
		URI url = ServiceUtils.getServiceOperationURIFromHelpURI(uriInfo);
		HelpData helpData = new HTMLHelpData("Airavata Mock API", "Welcome to Airavata API!!!");
		helpData.getParameters().put("<a href='"+url.toString()+ApplicationPath.SERVICE_PATH+ApplicationPath.ADD_APPLICATION_HELP+"'>New Application</a>", "Add new application to Airavata system");
		helpData.getParameters().put("<a href='"+url.toString()+ExperimentPath.SERVICE_PATH+ExperimentPath.ADD_TEMPLATE_HELP+"'>New Experiment Template</a>", "Add new application to Airavata system");
		helpData.getParameters().put("<a href='"+url.toString()+ExperimentPath.SERVICE_PATH+ExperimentPath.RUN_EXPERIMENTS_HELP+"'>Launch Experiment</a>", "Launch an experiment from a experiment template in Airavata system");
		helpData.getParameters().put("<a href='"+url.toString()+ExperimentPath.SERVICE_PATH+ExperimentPath.GET_RESULTS_HELP+"'>Get Experiment Results</a>", "Return the results of launching the experiment");
		helpData.getParameters().put("<a href='"+url.toString()+ExperimentPath.SERVICE_PATH+ExperimentPath.LIST_TEMPLATES_HELP+"'>Experiment Template List</a>", "List of templates available.");
		helpData.getParameters().put("<a href='"+url.toString()+ExperimentPath.SERVICE_PATH+ExperimentPath.LIST_EXPERIMENTS_HELP+"'>Experiments</a>", "List of experiments available.");
		return MethodUtils.getHelpString(helpData);
	}
	

}