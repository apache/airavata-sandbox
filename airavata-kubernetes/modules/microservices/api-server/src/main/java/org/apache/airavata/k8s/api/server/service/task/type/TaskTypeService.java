package org.apache.airavata.k8s.api.server.service.task.type;

import org.apache.airavata.k8s.api.resources.task.type.TaskTypeResource;
import org.apache.airavata.k8s.api.server.model.task.type.TaskInputType;
import org.apache.airavata.k8s.api.server.model.task.type.TaskModelType;
import org.apache.airavata.k8s.api.server.model.task.type.TaskOutPortType;
import org.apache.airavata.k8s.api.server.model.task.type.TaskOutputType;
import org.apache.airavata.k8s.api.server.repository.task.type.TaskInputTypeRepository;
import org.apache.airavata.k8s.api.server.repository.task.type.TaskOutPortTypeRepository;
import org.apache.airavata.k8s.api.server.repository.task.type.TaskOutputTypeRepository;
import org.apache.airavata.k8s.api.server.repository.task.type.TaskTypeRepository;
import org.apache.airavata.k8s.api.server.service.util.ToResourceUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
@Service
public class TaskTypeService {

    private TaskTypeRepository taskTypeRepository;
    private TaskInputTypeRepository taskInputTypeRepository;
    private TaskOutputTypeRepository taskOutputTypeRepository;
    private TaskOutPortTypeRepository taskOutPortTypeRepository;

    public TaskTypeService(TaskTypeRepository taskTypeRepository,
                           TaskInputTypeRepository taskInputTypeRepository,
                           TaskOutputTypeRepository taskOutputTypeRepository,
                           TaskOutPortTypeRepository taskOutPortTypeRepository) {

        this.taskTypeRepository = taskTypeRepository;
        this.taskInputTypeRepository = taskInputTypeRepository;
        this.taskOutputTypeRepository = taskOutputTypeRepository;
        this.taskOutPortTypeRepository = taskOutPortTypeRepository;
    }

    public long create(TaskTypeResource resource) {

        Optional<TaskModelType> taskType = taskTypeRepository.findByName(resource.getName());

        if (taskType.isPresent()) {
            return taskType.get().getId();
        }

        TaskModelType taskModelType = new TaskModelType();
        taskModelType.setName(resource.getName());
        taskModelType.setTopicName(resource.getTopicName());
        taskModelType.setIcon(resource.getIcon());
        TaskModelType savedTaskType = taskTypeRepository.save(taskModelType);

        Optional.ofNullable(resource.getInputTypes()).ifPresent(inputs -> inputs.forEach(input -> {
            TaskInputType inputType = new TaskInputType();
            inputType.setName(input.getName());
            inputType.setType(input.getType());
            inputType.setDefaultValue(input.getDefaultValue());
            inputType.setTaskModelType(savedTaskType);
            taskInputTypeRepository.save(inputType);
        }));

        Optional.ofNullable(resource.getOutputTypes()).ifPresent(outputs -> outputs.forEach(output -> {
            TaskOutputType outputType = new TaskOutputType();
            outputType.setName(output.getName());
            outputType.setType(output.getType());
            outputType.setTaskModelType(savedTaskType);
            taskOutputTypeRepository.save(outputType);
        }));

        Optional.ofNullable(resource.getOutPorts()).ifPresent(outPorts -> outPorts.forEach(outPort -> {
            TaskOutPortType outPortType = new TaskOutPortType();
            outPortType.setName(outPort.getName());
            outPortType.setOrder(outPort.getOrder());
            outPortType.setTaskModelType(savedTaskType);
            taskOutPortTypeRepository.save(outPortType);
        }));

        return savedTaskType.getId();
    }

    public List<TaskTypeResource> getAll() {
        List<TaskTypeResource> types = new ArrayList<>();
        Optional.ofNullable(taskTypeRepository.findAll())
                .ifPresent(taskModelTypes -> taskModelTypes.forEach(taskModelType -> {
                    types.add(ToResourceUtil.toResource(taskModelType).get());
                }));
        return types;
    }
}
