/**
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */

/*
 *
 * Controllers for the execution interface
 *
 */

// The angular module for the execution interface which depends on Angular-UI
var executionInterface = angular.module('executionInterface',['ui']).
    config(function($routeProvider) {
        $routeProvider.
            when('/', {controller:executionInterfaceCntl, templateUrl:'launchWorkflow.html'});
    });

//Main Controller for execution Interface
var executionInterfaceCntl=function ($scope,$routeParams,$http) {

    //Variables related to Run Workflow

    $scope.isWorkflowOpen=true;
    $scope.isWorkflowRunning=false;
    $scope.workflowInputs=[];
    $scope.experimentName='';
    $scope.workflowInterpreterURL='';
    $scope.gfacURL='';
    $scope.templateId=$routeParams.templateId;

    //$scope.workflowInputs.push({"id":$scope.experimentName,"name":$scope.experimentName,"datatype":"","value":"Experiment Name"});
    //$scope.workflowInputs.push({"id":$scope.workflowInterpreterURL,"name":$scope.workflowInterpreterURL,"datatype":"","value":"Workflow Interpreter URL"});
    //$scope.workflowInputs.push({"id":$scope.gfacURL,"name":$scope.gfacURL,"datatype":"","value":"gfacURL"});

    //get the inputs for the open workflow to render run workflow modal
    $scope.getWorkflowInputs=function($scope){
          //Airavata Client API call to get number of inputs for given template id
    };

    //validate the inputs
    $scope.validateInputs=function($scope){

    };

    //run experiment
    $scope.runExperiment=function($scope){
         //Airavata client API call to run experiment with
    };

}
