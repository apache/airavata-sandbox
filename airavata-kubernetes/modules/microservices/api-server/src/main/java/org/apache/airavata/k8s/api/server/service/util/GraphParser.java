package org.apache.airavata.k8s.api.server.service.util;

import org.apache.airavata.k8s.api.resources.task.TaskInputResource;
import org.apache.airavata.k8s.api.resources.task.TaskOutPortResource;
import org.apache.airavata.k8s.api.resources.task.TaskOutputResource;
import org.apache.airavata.k8s.api.resources.task.TaskResource;
import org.apache.airavata.k8s.api.resources.task.type.TaskTypeResource;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.persistence.criteria.CriteriaBuilder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class GraphParser {

    public static ParseResult parse(String content) throws Exception {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new InputSource(new StringReader(content)));
        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("root");
        Node rootNode = nList.item(0);

        NodeList elementNodeList = rootNode.getChildNodes();

        ParseResult result = new ParseResult();

        for (int temp = 0; temp < elementNodeList.getLength(); temp++) {

            Node nNode = elementNodeList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                if ("ProcessingElement".equals(eElement.getTagName())) {
                    TaskResource taskResource = new TaskResource();
                    NamedNodeMap attributes = eElement.getAttributes();
                    int id = -1;
                    for (int i = 0; i < attributes.getLength(); i++) {
                        Node attr = attributes.item(i);

                        if ("name".equals(attr.getNodeName())) {
                            taskResource.setName(attr.getNodeValue());

                        } else if ("Type".equals(attr.getNodeName())) {
                            taskResource.setTaskType(new TaskTypeResource().setId(Long.parseLong(attr.getNodeValue())));

                        } else if (attr.getNodeName().startsWith("in-") || attr.getNodeName().startsWith("out-")) {

                        } else if ("id".equals(attr.getNodeName())) {
                            id = Integer.parseInt(attr.getNodeValue());
                            taskResource.setReferenceId(id);

                        } else if (attr.getNodeName().startsWith("output-")) {
                            TaskOutputResource outputResource = new TaskOutputResource();
                            outputResource.setName(attr.getNodeName().substring(7));
                            outputResource.setValue(attr.getNodeValue());

                        } else {
                            TaskInputResource inputResource = new TaskInputResource();
                            inputResource.setName(attr.getNodeName());
                            inputResource.setValue(attr.getNodeValue());
                            taskResource.getInputs().add(inputResource);
                        }

                    }
                    Task task = new Task();
                    task.id = id;
                    task.taskResource = taskResource;
                    result.tasks.put(id, task);

                } else if ("Operation".equals(eElement.getTagName())) {
                    NamedNodeMap attributes = eElement.getAttributes();
                    int id = -1;
                    String name = null;
                    for (int i = 0; i < attributes.getLength(); i++) {
                        Node attr = attributes.item(i);

                        if ("name".equals(attr.getNodeName())) {
                            name = attr.getNodeValue();
                        } else if ("id".equals(attr.getNodeName())) {
                            id = Integer.parseInt(attr.getNodeValue());
                        }

                    }

                    Operation operation = new Operation();
                    operation.id = id;
                    operation.name = name;
                    result.operations.put(id, operation);
                } else if ("InPort".equals(eElement.getTagName())) {

                    int id = Integer.parseInt(eElement.getAttribute("id"));
                    String name = eElement.getAttribute("name");

                    Element mxCell = null;

                    NodeList childNodes = eElement.getChildNodes();
                    for (int i = 0 ; i < childNodes.getLength(); i++) {
                        Node tempInPort = childNodes.item(i);
                        if (tempInPort.getNodeType() == Node.ELEMENT_NODE) {
                            Element tmpInElement = (Element) tempInPort;
                            if ("mxCell".equals(tmpInElement.getTagName())) {
                                mxCell = tmpInElement;
                                break;
                            }
                        }
                    }

                    int parentId = Integer.parseInt(mxCell.getAttribute("parent"));

                    InPort inPort = new InPort();
                    inPort.id = id;
                    inPort.name = name;
                    result.portCache.put(id, inPort);

                    if (result.tasks.containsKey(parentId)) {
                        inPort.parentTask = result.tasks.get(parentId);
                        result.tasks.get(parentId).inPorts.put(id, inPort);
                    } else if (result.operations.containsKey(parentId)) {
                        inPort.parentOperation = result.operations.get(parentId);
                        result.operations.get(parentId).inPorts.put(id, inPort);
                    } else {
                        throw new Exception("Failed to find parent id " + parentId + " for in port " + id);
                    }

                } else if ("OutPort".equals(eElement.getTagName())) {

                    int id = Integer.parseInt(eElement.getAttribute("id"));
                    String name = eElement.getAttribute("name");

                    NamedNodeMap attributes = eElement.getAttributes();
                    Element mxCell = null;

                    NodeList childNodes = eElement.getChildNodes();
                    for (int i = 0 ; i < childNodes.getLength(); i++) {
                        Node tempOutPort = childNodes.item(i);
                        if (tempOutPort.getNodeType() == Node.ELEMENT_NODE) {
                            Element tmpOutElement = (Element) tempOutPort;
                            if ("mxCell".equals(tmpOutElement.getTagName())) {
                                mxCell = tmpOutElement;
                                break;
                            }
                        }
                    }

                    int parentId = Integer.parseInt(mxCell.getAttribute("parent"));

                    OutPort outPort = new OutPort();
                    outPort.id = id;
                    outPort.name = name;
                    result.portCache.put(id, outPort);

                    if (result.tasks.containsKey(parentId)) {
                        outPort.parentTask = result.tasks.get(parentId);
                        result.tasks.get(parentId).outPorts.put(id, outPort);
                        result.tasks.get(parentId).getTaskResource().getOutPorts()
                                .add(new TaskOutPortResource()
                                        .setName(outPort.name)
                                        .setReferenceId(outPort.id));

                    } else if (result.operations.containsKey(parentId)) {
                        outPort.parentOperation = result.operations.get(parentId);
                        result.operations.get(parentId).outPorts.put(id, outPort);
                    } else {
                        throw new Exception("Failed to find parent id " + parentId + " for out port " + id);
                    }

                } else if ("mxCell".equals(eElement.getTagName())) {
                    if (eElement.hasAttribute("edge") && eElement.hasAttribute("source") && eElement.hasAttribute("target")) {
                        int sourceId = Integer.parseInt(eElement.getAttribute("source"));
                        int targetId = Integer.parseInt(eElement.getAttribute("target"));

                        if (result.portCache.containsKey(targetId) && result.portCache.containsKey(sourceId)) {
                            InPort inPort = (InPort) result.portCache.get(targetId);
                            OutPort outPort = (OutPort) result.portCache.get(sourceId);
                            outPort.nextPort = inPort;
                            inPort.previousPorts.put(outPort.id, outPort);
                            result.edgeCache.put(outPort, inPort);
                        } else {
                            throw new Exception("Invalid connection with source id " + sourceId + " target id " + targetId);
                        }
                    }
                }
            }
        }

        return result;
    }

    public static class ParseResult {


        private Map<Integer, Task> tasks = new HashMap<>();
        private Map<Integer, Operation> operations = new HashMap<>();
        private Map<Integer, Object> portCache = new HashMap<>();
        private Map<OutPort, InPort> edgeCache = new HashMap<>();

        public Map<Integer, Task> getTasks() {
            return tasks;
        }

        public ParseResult setTasks(Map<Integer, Task> tasks) {
            this.tasks = tasks;
            return this;
        }

        public Map<Integer, Operation> getOperations() {
            return operations;
        }

        public ParseResult setOperations(Map<Integer, Operation> operations) {
            this.operations = operations;
            return this;
        }

        public Map<Integer, Object> getPortCache() {
            return portCache;
        }

        public ParseResult setPortCache(Map<Integer, Object> portCache) {
            this.portCache = portCache;
            return this;
        }

        public Map<OutPort, InPort> getEdgeCache() {
            return edgeCache;
        }

        public ParseResult setEdgeCache(Map<OutPort, InPort> edgeCache) {
            this.edgeCache = edgeCache;
            return this;
        }
    }


    public static class Task {
        private int id;
        private TaskResource taskResource;
        private Map<Integer, InPort> inPorts = new HashMap<>();
        private Map<Integer, OutPort> outPorts = new HashMap<>();

        public int getId() {
            return id;
        }

        public Task setId(int id) {
            this.id = id;
            return this;
        }

        public TaskResource getTaskResource() {
            return taskResource;
        }

        public Task setTaskResource(TaskResource taskResource) {
            this.taskResource = taskResource;
            return this;
        }

        public Map<Integer, InPort> getInPorts() {
            return inPorts;
        }

        public Task setInPorts(Map<Integer, InPort> inPorts) {
            this.inPorts = inPorts;
            return this;
        }

        public Map<Integer, OutPort> getOutPorts() {
            return outPorts;
        }

        public Task setOutPorts(Map<Integer, OutPort> outPorts) {
            this.outPorts = outPorts;
            return this;
        }
    }

    public static class OutPort {
        private InPort nextPort;
        private Task parentTask;
        private Operation parentOperation;
        private String name;
        private int id;

        public InPort getNextPort() {
            return nextPort;
        }

        public OutPort setNextPort(InPort nextPort) {
            this.nextPort = nextPort;
            return this;
        }

        public Task getParentTask() {
            return parentTask;
        }

        public OutPort setParentTask(Task parentTask) {
            this.parentTask = parentTask;
            return this;
        }

        public Operation getParentOperation() {
            return parentOperation;
        }

        public OutPort setParentOperation(Operation parentOperation) {
            this.parentOperation = parentOperation;
            return this;
        }

        public String getName() {
            return name;
        }

        public OutPort setName(String name) {
            this.name = name;
            return this;
        }

        public int getId() {
            return id;
        }

        public OutPort setId(int id) {
            this.id = id;
            return this;
        }
    }

    public static class InPort {
        private Map<Integer, OutPort> previousPorts = new HashMap<>();
        private Task parentTask;
        private Operation parentOperation;
        private String name;
        private int id;

        public Map<Integer, OutPort> getPreviousPorts() {
            return previousPorts;
        }

        public InPort setPreviousPorts(Map<Integer, OutPort> previousPorts) {
            this.previousPorts = previousPorts;
            return this;
        }

        public Task getParentTask() {
            return parentTask;
        }

        public InPort setParentTask(Task parentTask) {
            this.parentTask = parentTask;
            return this;
        }

        public Operation getParentOperation() {
            return parentOperation;
        }

        public InPort setParentOperation(Operation parentOperation) {
            this.parentOperation = parentOperation;
            return this;
        }

        public String getName() {
            return name;
        }

        public InPort setName(String name) {
            this.name = name;
            return this;
        }

        public int getId() {
            return id;
        }

        public InPort setId(int id) {
            this.id = id;
            return this;
        }
    }

    public static class Operation {
        private int id;
        private String name;
        private Map<Integer, InPort> inPorts = new HashMap<>();
        private Map<Integer, OutPort> outPorts = new HashMap<>();

        public int getId() {
            return id;
        }

        public Operation setId(int id) {
            this.id = id;
            return this;
        }

        public String getName() {
            return name;
        }

        public Operation setName(String name) {
            this.name = name;
            return this;
        }

        public Map<Integer, InPort> getInPorts() {
            return inPorts;
        }

        public Operation setInPorts(Map<Integer, InPort> inPorts) {
            this.inPorts = inPorts;
            return this;
        }

        public Map<Integer, OutPort> getOutPorts() {
            return outPorts;
        }

        public Operation setOutPorts(Map<Integer, OutPort> outPorts) {
            this.outPorts = outPorts;
            return this;
        }
    }

}
