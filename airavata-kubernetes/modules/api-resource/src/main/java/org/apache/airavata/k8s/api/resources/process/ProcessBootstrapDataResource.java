package org.apache.airavata.k8s.api.resources.process;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class ProcessBootstrapDataResource {
    private long id;
    private String key;
    private String value;

    public long getId() {
        return id;
    }

    public ProcessBootstrapDataResource setId(long id) {
        this.id = id;
        return this;
    }

    public String getKey() {
        return key;
    }

    public ProcessBootstrapDataResource setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public ProcessBootstrapDataResource setValue(String value) {
        this.value = value;
        return this;
    }
}
