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


describe('WebUI', function() {
  beforeEach(module('WebUI'));

  describe('Controller: HostDescriptor', function(){
    it('should be defined',
      inject(function ($controller, $rootScope) {
        var scope = $rootScope.$new(),  MessageQueue = {};
        var HostDescriptor = $controller('HostDescriptor', { $scope: scope,MessageQueue : MessageQueue});
        expect(HostDescriptor).toBeDefined();
      }));
  });

  describe('Controller: ApplicationDescriptor', function(){
    it('should be defined',
      inject(function ($controller, $rootScope) {
        var scope = $rootScope.$new(),  MessageQueue = {};
        var ApplicationDescriptor = $controller('ApplicationDescriptor', { $scope: scope,MessageQueue : MessageQueue});
        expect(ApplicationDescriptor).toBeDefined();
      }));
  });

  describe('Controller: FileTransfer', function(){
    it('should be defined',
      inject(function ($controller, $rootScope) {
        var scope = $rootScope.$new(),  MessageQueue = {};
        var FileTransfer = $controller('FileTransfer', { $scope: scope,MessageQueue : MessageQueue});
        expect(FileTransfer).toBeDefined();
      }));
  });

  describe('Controller: WorkflowLaunch', function(){
    it('should be defined',
      inject(function ($controller, $rootScope) {
        var scope = $rootScope.$new(),  MessageQueue = {};
        var WorkflowLaunch = $controller('WorkflowLaunch', { $scope: scope,MessageQueue : MessageQueue});
        expect(WorkflowLaunch).toBeDefined();
      }));
  });

  describe('Controller: WorkflowMonitoring', function(){
    it('should be defined',
      inject(function ($controller, $rootScope) {
        var scope = $rootScope.$new(),  MessageQueue = {};
        var WorkflowMonitoring = $controller('WorkflowMonitoring', { $scope: scope,MessageQueue : MessageQueue});
        expect(WorkflowMonitoring).toBeDefined();
      }));
  });

  describe('Controller: WorkflowCreator', function(){
    it('should be defined',
      inject(function ($controller, $rootScope) {
        var scope = $rootScope.$new(),  MessageQueue = {};
        var WorkflowCreator = $controller('WorkflowCreator', { $scope: scope,MessageQueue : MessageQueue});
        expect(WorkflowCreator).toBeDefined();
      }));
  });

  describe('Controller: Alert', function(){
    it('should be defined',
      inject(function ($controller, $rootScope) {
        var scope = $rootScope.$new(),  MessageQueue = {};
        var Alert = $controller('Alert', { $scope: scope,MessageQueue : MessageQueue});
        expect(Alert).toBeDefined();
      }));
  });

  describe('Controller: RegistryConnection', function(){
    it('should be defined',
      inject(function ($controller, $rootScope) {
        var scope = $rootScope.$new(),  MessageQueue = {};
        var RegistryConnection = $controller('RegistryConnection', { $scope: scope,MessageQueue : MessageQueue});
        expect(RegistryConnection).toBeDefined();
      }));
  });

});