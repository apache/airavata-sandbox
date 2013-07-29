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

'use strict';

// Controller to show alerts to screen
// An alert is structured as {"msg": "...", "head": "...", "type": "fromBootstrap"}
// All alerts are communicated through the MessageQueue service under the topic 'alerts'
angular.module('WebUI').controller('Alert', function ($scope, MessageQueue) {
  // Local copy of all alerts
  $scope.alerts = [];

  // Subscribe to the MessageQueue topic "alerts"
  MessageQueue.subscribe('alerts', function(alert) {
    $scope.alerts.push(alert);
  });

  // Remove the local copy of the alert when it is closed
  $scope.closeAlert = function(index) {
    $scope.alerts.splice(index, 1);
  };
});
