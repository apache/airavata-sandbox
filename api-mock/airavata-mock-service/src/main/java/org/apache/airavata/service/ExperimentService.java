package org.apache.airavata.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.airavata.core.application.ExperimentData;
import org.apache.airavata.service.utils.ServiceUtils;
import org.apache.airavata.service.utils.help.HTMLHelpData;
import org.apache.airavata.service.utils.help.HelpData;
import org.apache.airavata.service.utils.help.MethodUtils;
import org.apache.airavata.service.utils.path.ExperimentPath;

@Path(ExperimentPath.SERVICE_PATH)
public class ExperimentService {
	private static Map<String,String> templates=new HashMap<String, String>();
	private static Map<String,String> experiments=new HashMap<String, String>();
	private static Map<String,ExperimentData> experimentData=new HashMap<String, ExperimentData>();
	@Context
	UriInfo uriInfo;
	
	@Path(ExperimentPath.ADD_TEMPLATE+"/{templateName}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String addTemplate(@PathParam("templateName") String templateId, @QueryParam("experimentTemplate") String experimentTemplate) {
		templates.put(templateId, experimentTemplate);
		String message=templateId+" added as an experiment template.";
		System.out.println(message);
		return templateId;
	}
	
	@Path(ExperimentPath.ADD_TEMPLATE_HELP)
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String addTemplateHelp() {
		HelpData helpData = new HTMLHelpData("Add Experiment","Add a experiment template (aka workflow) to Airavata");
		try {
			URI uri = ServiceUtils.getServiceOperationURIFromHelpURI(uriInfo);
			helpData.getSyntax().add(uri.toString()+"/&lttemplateName&gt?experimentTemplate=&ltTemplate_String&gt");
			helpData.getParameters().put("templateName", "Name of this experiment.");
			helpData.getParameters().put("experimentTemplate", "Describes the template for the experiment.");
//			helpData.getExamples().add(uri.toString()+"?application={%22applicationName%22:%22echoApp%22,%22inputs%22:[{%22name%22:%22input_val%22,%22value%22:%22test%22,%22type%22:%22STRING%22}],%22outputs%22:[{%22name%22:%22output_val%22,%22value%22:%22test%22,%22type%22:%22STRING%22}],%22executablePath%22:null,%22scratchLocation%22:null}");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return MethodUtils.getHelpString(helpData);
	}
	
	@Path(ExperimentPath.LIST_TEMPLATES)
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getTemplates() {
		return (Arrays.asList(templates.keySet().toArray(new String[]{})));
	}
	
	@Path(ExperimentPath.LIST_TEMPLATES_HELP)
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getTemplatesHelp() {
		HelpData helpData = new HTMLHelpData("List Experiment Templates","Return a list of registered experiment templates");
		try {
			URI uri = ServiceUtils.getServiceOperationURIFromHelpURI(uriInfo);
			helpData.getSyntax().add(uri.toString());
			helpData.getExamples().add(uri.toString());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return MethodUtils.getHelpString(helpData);
	}

	@Path(ExperimentPath.RUN_EXPERIMENTS+"/{templateName}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String runExperiment(@PathParam("templateName") String templateId, @QueryParam("experimentInput") String experimentInput) {
		if (!templates.containsKey(templateId)){
			throw new WebApplicationException(new Exception("The experiment template "+templateId+" does not exist!!!"));
		}
		UUID uuid = UUID.randomUUID();
		experiments.put(uuid.toString(), experimentInput);
		experimentData.put(uuid.toString(), new ExperimentData(uuid.toString(), templateId, Calendar.getInstance().getTime(), "test_data", experimentInput));
		String message="Experiment "+uuid.toString()+" is executing...";
		System.out.println(message);
		return uuid.toString();
	}
	
	@Path(ExperimentPath.RUN_EXPERIMENTS_HELP)
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String runExperimentHelp() {
		HelpData helpData = new HTMLHelpData("Launch Experiments","Provide input data and configuration data to instantiate an experiment from an experiment template");
		try {
			URI uri = ServiceUtils.getServiceOperationURIFromHelpURI(uriInfo);
			helpData.getSyntax().add(uri.toString()+"/&ltTemplateName&gt?experimentInput=&ltInputDataArray&gt");
			helpData.getParameters().put("TemplateName", "Name of the experiment template to instantiate.");
			helpData.getParameters().put("experimentInput", "List of input values to passed on to the intantiated experiment template");
			helpData.getParameters().put("<RETURN_VALUE>", "A unique id identifying the experiment launched");
			helpData.getExamples().add(uri.toString()+"/echo_workflow?experimentInput={\"list\":[\"msg_part1=Hello\",\"msg_part2=World\"]}");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return MethodUtils.getHelpString(helpData);
	}
	
	@Path(ExperimentPath.LIST_EXPERIMENTS)
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getExperiments() {
		return (Arrays.asList(experiments.keySet().toArray(new String[]{})));
	}
	
	
	@Path(ExperimentPath.LIST_EXPERIMENTS_HELP)
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getExperimentsHelp() {
		HelpData helpData = new HTMLHelpData("List Experiments Instantiated","Return a list of launched experiments");
		try {
			URI uri = ServiceUtils.getServiceOperationURIFromHelpURI(uriInfo);
			helpData.getSyntax().add(uri.toString());
			helpData.getExamples().add(uri.toString());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return MethodUtils.getHelpString(helpData);
	}
	
	@Path(ExperimentPath.GET_RESULTS+"/{experimentId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ExperimentData getExperimentData(@PathParam ("experimentId") String experimentId) {
		if (experimentData.containsKey(experimentId)){
			return experimentData.get(experimentId);
		}
		throw new WebApplicationException(new Exception("no data for experiment id "+experimentId));
	}
	
	@Path(ExperimentPath.GET_RESULTS_HELP)
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getExperimentDataHelp() {
		HelpData helpData = new HTMLHelpData("Get Experiment Results","Retrieve execution results of the experiment");
		try {
			URI uri = ServiceUtils.getServiceOperationURIFromHelpURI(uriInfo);
			helpData.getSyntax().add(uri.toString()+"/&ltExperimentId&gt");
			helpData.getParameters().put("ExperimentId","The id of the experiment");
			helpData.getExamples().add(uri.toString()+"/UUID1328414123o12o321");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return MethodUtils.getHelpString(helpData);
	}
}