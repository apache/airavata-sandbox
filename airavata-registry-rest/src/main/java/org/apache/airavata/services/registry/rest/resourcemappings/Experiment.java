package org.apache.airavata.services.registry.rest.resourcemappings;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public class Experiment {
    private String experimentId;
    private String user;
    private String project;
    private Date date;
    private String gatewayName;

    public Experiment() {
    }

    public String getExperimentId() {
        return experimentId;
    }

    public String getUser() {
        return user;
    }

    public String getProject() {
        return project;
    }

    public Date getDate() {
        return date;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    public void setExperimentId(String experimentId) {
        this.experimentId = experimentId;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setGatewayName(String gatewayName) {
        this.gatewayName = gatewayName;
    }
}
