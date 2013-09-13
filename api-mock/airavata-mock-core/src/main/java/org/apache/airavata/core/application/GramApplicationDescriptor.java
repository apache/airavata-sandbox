package org.apache.airavata.core.application;

public class GramApplicationDescriptor extends ApplicationDescriptor {
	private String executablePath;
	private String scratchLocation;
	private String gramHost;
	private String gridFTPEndpoint;
	
	public String getGramHost() {
		return gramHost;
	}
	public void setGramHost(String gramHost) {
		this.gramHost = gramHost;
	}
	public String getGridFTPEndpoint() {
		return gridFTPEndpoint;
	}
	public void setGridFTPEndpoint(String gridFTPEndpoint) {
		this.gridFTPEndpoint = gridFTPEndpoint;
	}
	public String getExecutablePath() {
		return executablePath;
	}
	public void setExecutablePath(String executablePath) {
		this.executablePath = executablePath;
	}
	public String getScratchLocation() {
		return scratchLocation;
	}
	public void setScratchLocation(String scratchLocation) {
		this.scratchLocation = scratchLocation;
	}
	

}
