package org.apache.airavata.jobsubmission.gram;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.airavata.jobsubmission.utils.ServiceConstants;

public class ExectionContext {

	private String workingDir;
	private String tmpDir;
	private String stdOut;
	private String stderr;
	private String host;
	private String executable;
	private ArrayList<String[]> env;
	private String inputDataDir;
	private String outputDataDir;
	private boolean parameterNamesNeeded = false;
	private String stdIn;
	private String stdoutStr;
	private String stderrStr;
	private String queue;
	private Integer maxWallTime;
	private Integer pcount;
	private String projectName;
	private Integer minMemory;
	private Integer hostCount;
	private String jobType;
	private String arguments;
	private String gridFTPServerSource;
	private String sourcedataLocation;
	private String gridFTPServerDest;
	private String destdataLocation;
	
	

	public static final String PROPERTY_FILE = "gramclient.properties";

	public ExectionContext() throws IOException {
		loadConfigration();
	}

	private void loadConfigration() throws IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream propertyStream = classLoader.getResourceAsStream(PROPERTY_FILE);

		Properties properties = new Properties();
		if (propertyStream != null) {
			properties.load(propertyStream);
			String gateway = properties.getProperty(ServiceConstants.GATEWAY);
			String exec = properties.getProperty(ServiceConstants.EXECUTION);
			String args = properties.getProperty(ServiceConstants.ARGUMENTS);
			String queueName = properties.getProperty(ServiceConstants.QUEUE);
			String pn = properties.getProperty(ServiceConstants.PROJECT_NUMBER);
			String jt = properties.getProperty(ServiceConstants.JOB_TYPE);
			String mwt = properties.getProperty(ServiceConstants.MAX_WALL_TIME);
			String pc = properties.getProperty(ServiceConstants.PCOUNT);
			String hc = properties.getProperty(ServiceConstants.HOSTCOUNT);
			String gridFTPServerSource =  properties.getProperty(ServiceConstants.GRIDFTPSERVERSOURCE);
			String gridFTPSourcePath =  properties.getProperty(ServiceConstants.GRIDFTPSOURCEPATH);
			String gridFTPServerDest =  properties.getProperty(ServiceConstants.GRIDFTPSERVERDEST);
			String gridFTPDestPath =  properties.getProperty(ServiceConstants.GRIDFTPDESTPATH);
			if (gateway != null) {
				this.host = gateway;
			}
			if (exec != null) {
				this.executable = exec;
			}
			if (args != null) {
				this.arguments = args;
			}
			if (queueName != null) {
				this.queue = queueName;
			}
			if (pn != null) {
				this.projectName = pn;
			}
			if (jt != null) {
				this.jobType = jt;
			}
			if (mwt != null) {
				try {
					this.maxWallTime = Integer.parseInt(mwt);
				} catch (NumberFormatException e) {
					this.maxWallTime = 1;
				}
			}
			if (pc != null) {
				try {
					this.pcount = Integer.parseInt(pc);
				} catch (NumberFormatException e) {
					this.pcount = 1;
				}
			}
			if (hc != null) {
				try {
					this.hostCount = Integer.parseInt(hc);
				} catch (NumberFormatException e) {
					this.hostCount = 1;
				}
			}
			if(gridFTPServerSource != null && !gridFTPServerSource.isEmpty()){
				this.gridFTPServerSource = gridFTPServerSource;
			}
			if(gridFTPSourcePath != null && !gridFTPSourcePath.isEmpty()){
				this.sourcedataLocation = gridFTPSourcePath;
			}
			if(gridFTPServerDest != null && !gridFTPServerDest.isEmpty()){
				this.gridFTPServerDest = gridFTPServerDest;
			}
			if(gridFTPDestPath != null && !gridFTPDestPath.isEmpty()){
				this.destdataLocation = gridFTPDestPath;
			}
			
		}
	}

	/**
	 * @return the workingDir
	 */
	public String getWorkingDir() {
		return workingDir;
	}

	/**
	 * @param workingDir
	 *            the workingDir to set
	 */
	public void setWorkingDir(String workingDir) {
		this.workingDir = workingDir;
	}

	/**
	 * @return the tmpDir
	 */
	public String getTmpDir() {
		return tmpDir;
	}

	/**
	 * @param tmpDir
	 *            the tmpDir to set
	 */
	public void setTmpDir(String tmpDir) {
		this.tmpDir = tmpDir;
	}

	/**
	 * @return the stdOut
	 */
	public String getStdOut() {
		return stdOut;
	}

	/**
	 * @param stdOut
	 *            the stdOut to set
	 */
	public void setStdOut(String stdOut) {
		this.stdOut = stdOut;
	}

	/**
	 * @return the stderr
	 */
	public String getStderr() {
		return stderr;
	}

	/**
	 * @param stderr
	 *            the stderr to set
	 */
	public void setStderr(String stderr) {
		this.stderr = stderr;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the executable
	 */
	public String getExecutable() {
		return executable;
	}

	/**
	 * @param executable
	 *            the executable to set
	 */
	public void setExecutable(String executable) {
		this.executable = executable;
	}

	/**
	 * @return the env
	 */
	public ArrayList<String[]> getEnv() {
		return env;
	}

	/**
	 * @param env
	 *            the env to set
	 */
	public void setEnv(ArrayList<String[]> env) {
		this.env = env;
	}

	/**
	 * @return the inputDataDir
	 */
	public String getInputDataDir() {
		return inputDataDir;
	}

	/**
	 * @param inputDataDir
	 *            the inputDataDir to set
	 */
	public void setInputDataDir(String inputDataDir) {
		this.inputDataDir = inputDataDir;
	}

	/**
	 * @return the outputDataDir
	 */
	public String getOutputDataDir() {
		return outputDataDir;
	}

	/**
	 * @param outputDataDir
	 *            the outputDataDir to set
	 */
	public void setOutputDataDir(String outputDataDir) {
		this.outputDataDir = outputDataDir;
	}

	/**
	 * @return the parameterNamesNeeded
	 */
	public boolean isParameterNamesNeeded() {
		return parameterNamesNeeded;
	}

	/**
	 * @param parameterNamesNeeded
	 *            the parameterNamesNeeded to set
	 */
	public void setParameterNamesNeeded(boolean parameterNamesNeeded) {
		this.parameterNamesNeeded = parameterNamesNeeded;
	}

	/**
	 * @return the stdIn
	 */
	public String getStdIn() {
		return stdIn;
	}

	/**
	 * @param stdIn
	 *            the stdIn to set
	 */
	public void setStdIn(String stdIn) {
		this.stdIn = stdIn;
	}

	/**
	 * @return the stdoutStr
	 */
	public String getStdoutStr() {
		return stdoutStr;
	}

	/**
	 * @param stdoutStr
	 *            the stdoutStr to set
	 */
	public void setStdoutStr(String stdoutStr) {
		this.stdoutStr = stdoutStr;
	}

	/**
	 * @return the stderrStr
	 */
	public String getStderrStr() {
		return stderrStr;
	}

	/**
	 * @param stderrStr
	 *            the stderrStr to set
	 */
	public void setStderrStr(String stderrStr) {
		this.stderrStr = stderrStr;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public String getQueue() {
		return queue;
	}

	public void setMaxWallTime(Integer maxWallTime) {
		this.maxWallTime = maxWallTime;
	}

	public Integer getMaxWallTime() {
		return maxWallTime;
	}

	/**
	 * @return the pcount
	 */
	public Integer getPcount() {
		return pcount;
	}

	/**
	 * @param pcount
	 *            the pcount to set
	 */
	public void setPcount(Integer pcount) {
		this.pcount = pcount;
	}

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName
	 *            the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @return the minMemory
	 */
	public Integer getMinMemory() {
		return minMemory;
	}

	/**
	 * @param minMemory
	 *            the minMemory to set
	 */
	public void setMinMemory(Integer minMemory) {
		this.minMemory = minMemory;
	}

	/**
	 * @return the hostCount
	 */
	public Integer getHostCount() {
		return hostCount;
	}

	/**
	 * @param hostCount
	 *            the hostCount to set
	 */
	public void setHostCount(Integer hostCount) {
		this.hostCount = hostCount;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getJobType() {
		return jobType;
	}

	public void setArguments(String arguments) {
		this.arguments = arguments;
	}

	public String getArguments() {
		return arguments;
	}

	public String getGridFTPServerSource() {
		return gridFTPServerSource;
	}

	public void setGridFTPServerSource(String gridFTPServerSource) {
		this.gridFTPServerSource = gridFTPServerSource;
	}

	public String getSourcedataLocation() {
		return sourcedataLocation;
	}

	public void setSourcedataLocation(String sourcedataLocation) {
		this.sourcedataLocation = sourcedataLocation;
	}

	public String getGridFTPServerDest() {
		return gridFTPServerDest;
	}

	public void setGridFTPServerDest(String gridFTPServerDest) {
		this.gridFTPServerDest = gridFTPServerDest;
	}

	public String getDestdataLocation() {
		return destdataLocation;
	}

	public void setDestdataLocation(String destdataLocation) {
		this.destdataLocation = destdataLocation;
	}
}
