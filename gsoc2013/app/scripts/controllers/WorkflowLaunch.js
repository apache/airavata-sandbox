/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * 'License'); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * 'AS IS' BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
'use strict';

/*
 * Controller for workflow related task, edit properties, launch workflow.
 *
 */
angular.module('WebUI').controller('WorkflowLaunch', function ($scope, $http, MessageQueue) {

  //To enable the Cross Origin Resource Sharing in application
  $http.defaults.useXDomain = true;

  // Data required to launch experiment
  $scope.isWorkflowOpen=true;
  $scope.isWorkflowRunning=false;
  $scope.workflowParams={
    'workflowInputs':[
      {"id":"x","name": "X","dataType":"int","value":0},
      {"id":"y","name": "Y","dataType":"int","value":0}
    ],
    'experimentName':'',
    'workflowInterpreterURL':'',
    'gFacURL':''
  };

  //Data required for workflow properties
  $scope.workflowId=1;
  $scope.workflowProperties={
    'name':'',
    'templateId':'',
    'instanceId':'',
    'description':'',
    'metadata':''
  }

  //Initial Values of workflow properties
  $scope.workflowProperties.name='Workflow'+$scope.workflowId;
  $scope.workflowProperties.templateId='http://extreme.indiana.edu/lead/workflow/'+$scope.workflowProperties.name;
  $scope.workflowProperties.metadata="<appinfo xmlns="+"http://www.w3.org/2001/XMLSchema"+"></appinfo>";

  //get the inputs for the open workflow to launch run Workflow
  $scope.getWorkflowInputs=function($scope){
    // Airavata Client API call to get number of inputs for given template id
    // TODO: Call some API method to make HTTP request
  };

  //validate the inputs
  $scope.validateInputs=function($scope){
    // Validate the launch workflow parameters
    // TODO: Call some API method to make HTTP request

  };

  // Callback to launch experiment
  $scope.runExperiment = function () {
    // Test connection to register new user
    // TODO: Call some API method to make HTTP request

    // Alert user to the successful creation and on success notify all other controlers
    if ($scope.launchWorkflowForm.$valid){
      MessageQueue.publish('alerts', [{
        'head': 'Experiment ' + $scope.workflowParams.experimentName +' was Launched!',
        'msg': 'Experiment was launched successfully.',
        'type': 'success'
      }]);
    }
    else{
      MessageQueue.publish('alerts', [{
        'head': 'Validation Error',
        'msg': 'There is error in form validation.',
        'type': 'error'
      }]);
    };
  }

  // Callback to edit workflow properties
  $scope.saveWorkflowProperties = function () {
    // Test connection to register new user
    // TODO: Call some API method to make HTTP request

    // Alert user to the successful creation and on success notify all other controlers
    if ($scope.workflowPropertiesForm.$valid){
      MessageQueue.publish('alerts', [{
        'head': 'Workflow ' + $scope.workflowProperties.name +' was edited!',
        'msg': 'workflow properties are saved.',
        'type': 'success'
      }]);
    }
    else{
      MessageQueue.publish('alerts', [{
        'head': 'Validation Error',
        'msg': 'There is error in form validation.',
        'type': 'error'
      }]);
    };
  }
});
