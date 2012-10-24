package org.apache.airavata.services.registry.rest.resourcemappings;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ExperimentList {
    private Experiment[] experiments = null;

    public ExperimentList() {
    }

    public Experiment[] getExperiments() {
        return experiments;
    }

    public void setExperiments(Experiment[] experiments) {
        this.experiments = experiments;
    }
}
