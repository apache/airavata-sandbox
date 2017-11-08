package org.apache.airavata.k8s.api.server.controller;

import org.apache.airavata.k8s.api.resources.task.type.TaskTypeResource;
import org.apache.airavata.k8s.api.server.service.task.type.TaskTypeService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
@RestController
@RequestMapping(path="/taskType")
public class TaskTypeController {

    @Resource
    private TaskTypeService taskTypeService;

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TaskTypeResource> getAll() {
        return taskTypeService.getAll();
    }

    @PostMapping( path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public long createTask(@RequestBody TaskTypeResource resource) {
        return taskTypeService.create(resource);
    }
}
