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
 * Controller for Application Descriptors registry resource
 *
 */
angular.module('WebUI').controller('ApplicationDescriptor', function ($scope, $http, MessageQueue) {

  //To enable the Cross Origin Resource Sharing in application
  $http.defaults.useXDomain = true;

  // Data to control the forms
  $scope.parameterIO = ['input','output'];
  $scope.parameterType = [
    'String','Double','Integer','Float','Boolean','File',
    'StringArray','DoubleArray','IntegerArray','FloatArray','BooleanArray',
    'StdOut','StdErr','FileArray','DataId','DataIdArray','URIArray','URI'
  ];
  $scope.regAppFormLegend = 'Register Application';
  $scope.newAppDeployFromLegend = 'New Application Deployment';
  $scope.hpcConfigFormLegend = 'HPC Configuration Options';
  $scope.advancedFileStaging = false;

  // hide/show the form for application registration,deployment and hpc configuration
  $scope.isNewDeployment = false;
  $scope.isHPCConfiguration = false;
  $scope.isApplicationRegister = true;
  $scope.isEditApplication = false;
  $scope.isCancel = false;

  // Data required to register new application
  $scope.serviceDescriptor = {
    'serviceName':'',
    'serviceDescription':'',
    'parameters':[{io:'',name:'',type:'',description:''}],      //array of object of parameter
    'applicationDescriptors':[]     //array of applicationDescriptor object
  };
  $scope.applicationDescriptor = {
    'hostDescName':'',
    'executablePath':'',
    'workingDir':'',
    'jobType':'',
    'projectNumber':'',
    'projectDescription':'',
    'queueName':'',
    'maxWallTime':'',
    'cpuCount':'',
    'nodeCount':'',
    'processorsPerNode':'',
    'minMemory':'',
    'maxMemory':'',
    'inputDir':'',
    'outputDir':'',
    'staticWorkingDir':'',
    'stdIn':'',
    'stdOut':'',
    'stdError':'',
    'environmentVariables':[{name:'',value:''}]         //array of object of (name,value) pair
  };
  $scope.prevApplicationDescriptor;
  $scope.applicationHost=['LocalHost','lonestar','stampede','trestles'];
  $scope.jobTypes=['openMP','mpi','serial'];

  // Callback to Add New Application Deployment
  $scope.addApplicationDeployment = function() {
    if ($scope.isCancel) {
      var appDesc = angular.copy($scope.prevApplicationDescriptor);      //Json object are referenced object
      $scope.isCancel = false;
    }
    else {
      var appDesc = angular.copy($scope.applicationDescriptor);
    }
    $scope.serviceDescriptor.applicationDescriptors.push(appDesc);
    $scope.index=$scope.applicationHost.indexOf($scope.applicationDescriptor.hostDescName);
    $scope.applicationHost.splice( $scope.index,1);
    //$scope.applicationDescriptor={};
    $scope.isApplicationRegister=true;
    $scope.isNewDeployment=false;
  };
  // Callback to Delete Application Deployment
  $scope.deleteApplicationDeployment = function(index) {
    var hostDescName = $scope.serviceDescriptor.applicationDescriptors[index].hostDescName;
    $scope.applicationHost.push(hostDescName);
    $scope.serviceDescriptor.applicationDescriptors.splice(index, 1);
  };

  // Callback to Edit Application Deployment
  $scope.editApplicationDeployment = function(index) {
    $scope.isEditApplication=true;
    $scope.applicationDescriptor = $scope.serviceDescriptor.applicationDescriptors[index];
    $scope.prevApplicationDescriptor=angular.copy($scope.applicationDescriptor);
    $scope.deleteApplicationDeployment(index);
    $scope.isNewDeployment=true;
  };
  // Callback to Cancel Application Deployment
  $scope. cancelApplicationDeployment = function(index) {
    if ($scope.isEditApplication) {
      $scope.isCancel=true;
      $scope.addApplicationDeployment();
      $scope.isEditApplication=false;
    }
    $scope.isNewDeployment=false;
  };


  // Callback to Add Application Descriptor to Registry
  $scope.saveApplicationDescriptor = function () {
    // Test connection to save Application Descriptor
    // TODO: Call some API method to make HTTP request

    // Alert user to the successful creation and on success notify all other controllers
    if ($scope.registerApplicationForm.$valid){
      MessageQueue.publish('alerts', [{
        'head': $scope.serviceDescriptor.serviceName + ' service was registered!',
        'msg': 'Total Parameters: ' + $scope.serviceDescriptor.parameters.length + ' Total Deployments: ' + $scope.serviceDescriptor.applicationDescriptors.length + ' added successfully.',
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
