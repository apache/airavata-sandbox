package org.apache.airavata.k8s.api.resources.task.type;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class TaskOutPortTypeResource {

    private long id;
    private String name;
    private int order = 0;

    public long getId() {
        return id;
    }

    public TaskOutPortTypeResource setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TaskOutPortTypeResource setName(String name) {
        this.name = name;
        return this;
    }

    public int getOrder() {
        return order;
    }

    public TaskOutPortTypeResource setOrder(int order) {
        this.order = order;
        return this;
    }
}
