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
 * Controller for workflow monitoring.
 *
 */
angular.module('WebUI').controller('WorkflowMonitoring', function ($scope, $http, MessageQueue) {

  //To enable the Cross Origin Resource Sharing in application
  $http.defaults.useXDomain = true;

  // Data required for notification configuration
  $scope.notification={
    'brokerURL':'http://10.1.66.76:8080/airavata-server/services/EventingService',
    'topic':'',
    'messageBoxURL':'http://10.1.66.76:8080/airavata-server/services/MsgBoxService',
    'pullMode':''
  }

  // Callback to configure notification
  $scope.configureNotification = function () {
    // Test connection for configuration
    // TODO: Call some API method to make HTTP request

    // Alert user to the successful configuration and on success notify all other controlers
    if ($scope.notificationConfigurationForm.$valid){
      MessageQueue.publish('alerts', [{
        'head': 'Notification Topic ' + $scope.notification.topic +' was configured!',
        'msg': 'Notification nonfigured successfully.',
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
