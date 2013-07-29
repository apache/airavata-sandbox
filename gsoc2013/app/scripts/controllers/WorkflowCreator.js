/**
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 'License'); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 'AS IS' BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */

'use strict';

// Controller to create and edit workflows
angular.module('WebUI').controller('WorkflowCreator', function ($scope, MessageQueue) {
	// The WorkflowUI instance tied to the controller
	$scope.workflowUI = new BandIt('editor', 1200, 800);
  $scope.workflowUI.setOptions({
    'store': 'none'
  });
  $scope.workflowUI.setDefaultProperties({
    'command': '',
    'store': 'false',
    'queue': '',
    'description': ''
  });

  // Active button states
  $scope.buttonState = {
    'link': '',
    'delete': '',
    'move': 'active',
    'group': ''
  };

  // Add the first node
  $scope.workflowUI.add('root', { fill: '#6E6E6E' });
  var updatemode = function (mode) {
    $scope.workflowUI.setMode(mode);
    switch(mode) {
    case 0: // Move
      $scope.buttonState.move = 'active';
      $scope.buttonState.link = $scope.buttonState.delete = $scope.buttonState.group = '';
      break;
    case 1: // Link
      $scope.buttonState.link = 'active';
      $scope.buttonState.move = $scope.buttonState.delete = $scope.buttonState.group = '';
      break;
    case 2: // Delete
      $scope.buttonState.delete = 'active';
      $scope.buttonState.link = $scope.buttonState.move = $scope.buttonState.group = '';
      break;
    case 3: // Group
      $scope.buttonState.group = 'active';
      $scope.buttonState.link = $scope.buttonState.delete = $scope.buttonState.move = '';
      break;
    }
  };

  // Functions relating to the creation menubar
  $scope.addNode = function () {
    updatemode(0);
    $scope.workflowUI.add();
  };
  $scope.addContainer = function () {
    updatemode(0);
    $scope.workflowUI.addContainer();
  };
  $scope.move = function () {
    updatemode(0);
  };
  $scope.link = function () {
    updatemode(1);
  };
  $scope.delete = function () {
    updatemode(2);
  };
  $scope.group = function () {
    updatemode(3);
  };
  $scope.undo = function () {
    $scope.workflowUI.undo();
  };
  $scope.redo = function () {
    $scope.workflowUI.redo();
  };
  $scope.clean = function () {
    $scope.workflowUI.clean();
  };
  $scope.zoomIn = function () {
    $scope.workflowUI.zoomIn();
  };
  $scope.zoomOut = function () {
    $scope.workflowUI.zoomOut();
  };
  $scope.panLeft = function () {
    $scope.workflowUI.moveLeft(10);
  };
  $scope.panRight = function () {
    $scope.workflowUI.moveRight(10);
  };
  $scope.panUp = function () {
    $scope.workflowUI.moveUp(10);
  };
  $scope.panDown = function () {
    $scope.workflowUI.moveDown(10);
  };
});
