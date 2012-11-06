package org.apache.airavata.services.registry.rest.resourcemappings;

import org.apache.airavata.registry.api.AiravataExperiment;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ExperimentList {
    private AiravataExperiment[] experiments = null;

    public ExperimentList() {
    }

    public AiravataExperiment[] getExperiments() {
        return experiments;
    }

    public void setExperiments(AiravataExperiment[] experiments) {
        this.experiments = experiments;
    }
}
