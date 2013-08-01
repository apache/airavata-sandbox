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
 * Controller for globus file transfer
 *
 */
angular.module('WebUI').controller('FileTransfer', function ($scope, $http, MessageQueue) {

  //To enable the Cross Origin Resource Sharing in application
  $http.defaults.useXDomain = true;

  // Data required for globus file tranfer
  $scope.fileTransfer={
    'userName':'',
    'caFile':'',
    'certificateFile':'',
    'keyFile':'',
    'sourceEndpoint':'',
    'sourceFilePath':'',
    'destinationEndPoint':'',
    'destinationFilePath':''
  };
  // Callback to Add Globus File Transfer Details
  $scope.saveGlobusFileTransfer = function () {
    // Test connection to save globus file transfer details
    // TODO: Call some API method to make HTTP request

    // Alert user to the successful creation and on success notify all other controlers
    if ($scope.fileTransferForm.$valid){
      MessageQueue.publish('alerts', [{
        'head': 'Host ' + $scope.fileTransfer.userName +' was added!',
        'msg': 'File transfer details has been added to the registry.',
        'type': 'success'
      }]);
    }
    else{
      MessageQueue.publish('alerts', [{
        'head': 'Validation Error',
        'msg': 'There is error in File Transfer Form validation.',
        'type': 'error'
      }]);
    };
  }
});

