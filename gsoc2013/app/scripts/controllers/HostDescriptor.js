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
 * Controller for descriptors registry resource , deals with host,service and application descriptors
 *
 */
angular.module('WebUI').controller('HostDescriptor', function ($scope, $http, MessageQueue) {

  //To enable the Cross Origin Resource Sharing in application
  $http.defaults.useXDomain = true;
  // Resource Protocols available for the host
  $scope.protocols=[
    {name:'Local',configuration:false},
    {name:'SSH',configuration:false},
    {name:'Globus',configuration:true},
    {name:'Unicore',configuration:true},
    {name:'Amazone EC2',configuration:false},
    {name:'Hadoop',configuration:false}
  ];
  // Data required to add new host descriptor to the registry
  $scope.addHost={
    'hostId':'',
    'hostAddress':'',
    'resourceProtocol':'',
    'gramEndpoint':'',
    'gridFTPEndpoint':''
  };
  // Callback to Add Host Descriptor to Registry
  $scope.saveHostDescriptor = function () {
    // Test connection to register new user
    // TODO: Call some API method to make HTTP request

    // Alert user to the successful creation and on success notify all other controlers
    if ($scope.addhostForm.$valid){
      MessageQueue.publish('alerts', [{
        'head': 'Host ' + $scope.addHost.hostId +' was added!',
        'msg': 'A new Host Descriptor has been added to the registry.',
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