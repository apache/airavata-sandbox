package org.apache.airavata.k8s.api.server.model.process;

import javax.persistence.*;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
@Entity
@Table(name = "PROCESS_BOOTSTRAP_DATA")
public class ProcessBootstrapData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private ProcessModel processModel;

    @Column(name = "DATA_KEY")
    private String key;

    @Column(name = "DATA_VALUE")
    private String value;

    public long getId() {
        return id;
    }

    public ProcessBootstrapData setId(long id) {
        this.id = id;
        return this;
    }

    public ProcessModel getProcessModel() {
        return processModel;
    }

    public ProcessBootstrapData setProcessModel(ProcessModel processModel) {
        this.processModel = processModel;
        return this;
    }

    public String getKey() {
        return key;
    }

    public ProcessBootstrapData setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public ProcessBootstrapData setValue(String value) {
        this.value = value;
        return this;
    }
}
