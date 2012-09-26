package org.apache.airavata.services.registry.rest.resourcemappings;


import org.apache.airavata.registry.api.AiravataExperiment;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ExperimentList {
    AiravataExperiment[] experiments = null;

    public AiravataExperiment[] getExperiments() {
        return experiments;
    }

    public void setExperiments(AiravataExperiment[] experiments) {
        this.experiments = experiments;
    }
}
