package org.apache.airavata.core.application;

public class LocalApplicationDescriptor extends ApplicationDescriptor {
	private String executablePath;
	private String scratchLocation;
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
