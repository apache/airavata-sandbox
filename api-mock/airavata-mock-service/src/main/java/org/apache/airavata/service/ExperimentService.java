package org.apache.airavata.service;

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
import javax.ws.rs.core.MediaType;

import org.apache.airavata.core.application.ExperimentData;

@Path("/experiments/")
public class ExperimentService {
	private static Map<String,String> templates=new HashMap<String, String>();
	private static Map<String,String> experiments=new HashMap<String, String>();
	private static Map<String,ExperimentData> experimentData=new HashMap<String, ExperimentData>();

	@Path("add/template/{templateId}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String addTemplate(@PathParam("templateId") String templateId, @QueryParam("experimentTemplate") String experimentTemplate) {
		templates.put(templateId, experimentTemplate);
		String message=templateId+" added as an experiment template.";
		System.out.println(message);
		return templateId;
	}
	
	@Path("list/templates")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getTemplates() {
		return (Arrays.asList(templates.keySet().toArray(new String[]{})));
	}

	@Path("run/{templateId}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String runExperiment(@PathParam("templateId") String templateId, @QueryParam("experimentInput") String experimentInput) {
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
	
	@Path("list/experiments")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getExperiments() {
		return (Arrays.asList(experiments.keySet().toArray(new String[]{})));
	}
	
	@Path("results/{experimentId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ExperimentData getExperimentData(@PathParam ("experimentId") String experimentId) {
		if (experimentData.containsKey(experimentId)){
			return experimentData.get(experimentId);
		}
		throw new WebApplicationException(new Exception("no data for experiment id "+experimentId));
	}
}