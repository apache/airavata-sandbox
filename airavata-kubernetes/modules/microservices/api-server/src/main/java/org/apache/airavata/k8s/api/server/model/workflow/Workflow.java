package org.apache.airavata.k8s.api.server.model.workflow;

import org.apache.airavata.k8s.api.server.model.process.ProcessModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
@Entity
public class Workflow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @Lob
    @Column(length = 1000000, name = "CONTENT")
    @Basic(fetch = FetchType.LAZY)
    private byte[] workFlowGraph;


    @OneToMany(mappedBy = "workflow", cascade = CascadeType.ALL)
    private List<ProcessModel> processes = new ArrayList<>();

    public long getId() {
        return id;
    }

    public Workflow setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Workflow setName(String name) {
        this.name = name;
        return this;
    }


    public byte[] getWorkFlowGraph() {
        return workFlowGraph;
    }

    public Workflow setWorkFlowGraph(byte[] workFlowGraph) {
        this.workFlowGraph = workFlowGraph;
        return this;
    }

    public List<ProcessModel> getProcesses() {
        return processes;
    }

    public Workflow setProcesses(List<ProcessModel> processes) {
        this.processes = processes;
        return this;
    }
}
