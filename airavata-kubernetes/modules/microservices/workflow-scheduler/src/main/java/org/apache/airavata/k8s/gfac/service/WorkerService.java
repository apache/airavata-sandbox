/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.airavata.k8s.gfac.service;

import org.apache.airavata.k8s.api.resources.process.ProcessResource;
import org.apache.airavata.k8s.api.resources.task.TaskDagResource;
import org.apache.airavata.k8s.api.resources.task.TaskResource;
import org.apache.airavata.k8s.gfac.core.HelixWorkflowManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
@Service
public class WorkerService {

    private final RestTemplate restTemplate;
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Value("${api.server.url}")
    private String apiServerUrl;

    @Value("${zookeeper.connection.url}")
    private String zkConnectionString;

    @Value("${helix.cluster.name}")
    private String helixClusterName;

    @Value("${instance.name}")
    private String instanceName;

    public WorkerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void launchProcess(long processId) {
        System.out.println("Launching process " + processId);
        ProcessResource processResource = this.restTemplate.getForObject("http://" + apiServerUrl + "/process/" + processId,
                ProcessResource.class);
        List<TaskResource> taskResources = processResource.getTasks();

        Set<TaskDagResource> takDagSet = this.restTemplate.exchange("http://" + apiServerUrl + "/task/dag/"
                + processId, HttpMethod.GET, null, new ParameterizedTypeReference<Set<TaskDagResource>>() {})
                .getBody();

        final Map<Long, Long> edgeMap = new HashMap<>();
        Optional.ofNullable(takDagSet)
                .ifPresent(dags -> dags.forEach(dag ->
                        edgeMap.put(dag.getSourceOutPort().getId(), dag.getTargetTask().getId())));

        System.out.println("Starting to execute process " + processId);
        //ProcessLifeCycleManager manager =
        //        new ProcessLifeCycleManager(processId, taskResources, edgeMap, kafkaSender, restTemplate, apiServerUrl);

        //manager.init();
        //manager.start();

        //processLifecycleStore.put(processId, manager);

        final HelixWorkflowManager helixWorkflowManager = new HelixWorkflowManager(processId, taskResources,
                processResource.getProcessBootstrapData(), edgeMap,
                restTemplate, apiServerUrl,
                zkConnectionString, helixClusterName, instanceName);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                helixWorkflowManager.launchWorkflow();
            }
        });
    }
}
