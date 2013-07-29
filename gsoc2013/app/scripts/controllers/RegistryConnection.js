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

// Controller to connect to an Airavata registry
angular.module('WebUI').controller('RegistryConnection', function ($scope,$http, MessageQueue) {
  //$httpProvider.defaults.useXDomain = true;
  $http.defaults.useXDomain = true;
  // Data required to connect to the registry
  $scope.registry =  {
    'url': 'http://localhost:8080/airavata-registry/api',
    'gateway': 'default',
    'username': 'admin',
    'password': 'admin'
  };
  // Data required to add new user to the registry
  $scope.newUser={
    'username':'',
    'password':'',
    'confirmPassword':''
  };

  // Callback to test connection
  $scope.connectToRegistry = function () {
    // Test connection to registry
    //$scope.basicRegistry= new BasicRegistry();
    //$scope.res=$scope.basicRegistry.setAiravataUser($scope.registry.username);
    $http({ // Load the initial data
      url: 'http://localhost:8080/airavata-registry/api/basicregistry/set/serviceURL'+'?connectionurl='+$scope.registry.url,
      method: 'GET',
      headers:{'Authorization': 'Basic YWRtaW46YWRtaW4=',
          'Accept': '*/*', 'Content-Type': 'application/x-www-form-urlencoded'}
    }).success(function (data) {
      $scope.res = data;
    }).error(function(data, status) {
      //alert(data)
      $scope.res = data || 'Request failed';
      $scope.status = status;
    });

    // Alert user to the successful connection and on success notify all other controlers
    MessageQueue.publish('registry', $scope.registry);
    MessageQueue.publish('alerts',[ {
      'head': 'Conntected',
      'msg': 'Successful connection made to '+$scope.res,
      'type': 'success'
    }]);
  };

  // Callback to Register New User
  $scope.newUserRegistry = function () {
    // Test connection to register new user
    // TODO: Call some API method to make HTTP request
    // Alert user to the successful creation and on success notify all other controlers
    $('#createNweUserModal').modal('hide');
    MessageQueue.publish('new user', $scope.newUser);
    MessageQueue.publish('alerts', [{
      'head': 'New User Added',
      'msg': $scope.newUser.username +' added successfully',
      'type': 'success'
    }]);
  };
});
