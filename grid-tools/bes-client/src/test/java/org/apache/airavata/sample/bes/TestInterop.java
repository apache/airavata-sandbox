package org.apache.airavata.sample.bes;

import org.apache.airavata.sample.bes.RunBESJob;
import org.junit.Test;



public class TestInterop {

	@Test
	public void testRunJob(){
		RunBESJob besJob = new RunBESJob();
		besJob.runJob();
	}
	
	
}
