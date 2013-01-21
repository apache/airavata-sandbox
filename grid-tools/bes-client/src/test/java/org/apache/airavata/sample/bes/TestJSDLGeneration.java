package org.apache.airavata.sample.bes;

import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ggf.schemas.jsdl.x2005.x11.jsdl.ApplicationDocument;
import org.ggf.schemas.jsdl.x2005.x11.jsdl.ApplicationType;
import org.ggf.schemas.jsdl.x2005.x11.jsdl.CreationFlagEnumeration;
import org.ggf.schemas.jsdl.x2005.x11.jsdl.DataStagingDocument;
import org.ggf.schemas.jsdl.x2005.x11.jsdl.DataStagingType;
import org.ggf.schemas.jsdl.x2005.x11.jsdl.JobDefinitionDocument;
import org.ggf.schemas.jsdl.x2005.x11.jsdl.JobDescriptionType;
import org.ggf.schemas.jsdl.x2005.x11.jsdl.ResourcesType;
import org.ggf.schemas.jsdl.x2005.x11.jsdl.SourceTargetType;
import org.ggf.schemas.jsdl.x2005.x11.jsdlPosix.POSIXApplicationDocument;
import org.ggf.schemas.jsdl.x2005.x11.jsdlPosix.POSIXApplicationType;
import org.junit.Test;

import de.fzj.unicore.wsrflite.xmlbeans.WSUtilities;


/**
 * This test case creates a sample jsdl instance
 * containing Application, Arguments, Source, and Target Elements
 *  
 * */

public class TestJSDLGeneration {
	
	@Test
	public void testSimpleJSDLInstance(){
		
		JobDefinitionDocument jdd = JobDefinitionDocument.Factory.newInstance();
		JobDescriptionType jobDescType = jdd.addNewJobDefinition().addNewJobDescription();
		
		// setting application details
		ApplicationDocument appDoc = ApplicationDocument.Factory.newInstance();
		ApplicationType app = appDoc.addNewApplication();
		app.setApplicationName("gnuplot");
		WSUtilities.append(posixElement(), appDoc);
		jobDescType.setApplication(appDoc.getApplication());
		
		// setting resource details
		ResourcesType resType = jobDescType.addNewResources();
		resType.addNewIndividualPhysicalMemory().addNewLowerBoundedRange().setDoubleValue(20197152.0);
		resType.addNewTotalCPUCount().addNewExact().setDoubleValue(1.0);
		
		
		// setting simple data staging details
		newDataStagingElement(jobDescType.addNewDataStaging(), true, "control.txt");
		newDataStagingElement(jobDescType.addNewDataStaging(), true, "input.dat");
		newDataStagingElement(jobDescType.addNewDataStaging(), false, "output1.png");
		
		// setting hpcp-file staging profile data staging details and credentials
		newHPCPFSPDataStagingElement(jobDescType.addNewDataStaging(), true, "control.txt", "u1", "p1");
		newHPCPFSPDataStagingElement(jobDescType.addNewDataStaging(), true, "input.dat", "u1", "p1");
		newHPCPFSPDataStagingElement(jobDescType.addNewDataStaging(), false, "output1.png", "u1", "p1");
		newHPCPFSPDataStagingElement(jobDescType.addNewDataStaging(), false, "extendedoutput.png", "u1", "p1");
		
		assertTrue(HPCPUtils.extractUsernamePassword(jdd)!=null);
		
		System.out.println(jdd);
		
	}
	
	private POSIXApplicationDocument posixElement(){
		POSIXApplicationDocument posixDoc = POSIXApplicationDocument.Factory.newInstance();
		POSIXApplicationType posixType = posixDoc.addNewPOSIXApplication();
		posixType.addNewExecutable().setStringValue("/usr/bin/gnuplot");
		posixType.addNewArgument().setStringValue("control.lst");
		posixType.addNewArgument().setStringValue("input.dat");
		
		//if they both are not set, then the target unicore server will name them stdout and stderr without i.e. '.txt'
		posixType.addNewOutput().setStringValue("stdout.txt");
		posixType.addNewError().setStringValue("stderr.txt");
		
		return posixDoc;
	}
	
	private void newDataStagingElement(DataStagingType dsType, boolean isSource, String fileName){
		dsType.setFileName(fileName);
		dsType.setCreationFlag(CreationFlagEnumeration.OVERWRITE);
		dsType.setDeleteOnTermination(true);
		if(isSource)
		  dsType.addNewSource().setURI("http://foo.bar.com/~me/"+fileName);
		else
		  dsType.addNewTarget().setURI("http://foo.bar.com/~me/"+fileName);
	}	
	
	
	private void newHPCPFSPDataStagingElement(final DataStagingType dsType, boolean isSource, String fileName, String userName, String password){
		dsType.setFileName(fileName);
		dsType.setCreationFlag(CreationFlagEnumeration.OVERWRITE);
		dsType.setDeleteOnTermination(true);
		SourceTargetType sourceTarget = null;
		
		if(isSource)
		  sourceTarget = dsType.addNewSource();
		else
		  sourceTarget = dsType.addNewTarget();
		
		sourceTarget.setURI("http://foo.bar.com/~me/"+fileName);
		
		DataStagingDocument dsDoc = DataStagingDocument.Factory.newInstance();
		dsDoc.setDataStaging(dsType);
		
		WSUtilities.append(HPCPUtils.createCredentialsElement(userName, password), dsDoc);
		
		dsType.set(dsDoc.getDataStaging());
	}	

	
	public int count(String word, String line){
	    Pattern pattern = Pattern.compile(word);
	    Matcher matcher = pattern.matcher(line);
	    int counter = 0;
	    while (matcher.find())
	        counter++;
	    return counter;
	}
	
}
