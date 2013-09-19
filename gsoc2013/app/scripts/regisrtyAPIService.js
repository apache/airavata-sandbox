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

//        url headers type data onSuccess

'use strict';

// Angular Service for the Registry API
angular.module('WebUI').factory('registryAPI', function($http){
  // Listen to all updates about updates from modules
  // TODO - Inter service communication how?
  var apiPath = '/airavata-registry/api';
  var setResourcePaths = function (url) { // This has to go into a callback for the global message queue
    apiPath = url + '/airavata-registry/api';
  };
  return {
    //=====================================================
    'setResourcePaths': setResourcePaths,

    //=====================================================
    'workflow': { // Registry API dealing with the workflow
      'user': { // UserWorkflow Registry
        'exists': function (name) { // Check if a workflow exists in registry
          var promise = $http.get({
            'url': apiPath + '/userwfregistry/workflow/exist',
            'params': {'workflowName': name}
          }).success(function (response) {
            return response.data.responseText;
          });
          return promise;
        },
        'getAll': function () { // Get a all workflows
          var promise = $http.get({
            'url': apiPath + '/userwfregistry/get/workflows'
          }).success(function (response) {
            return response.data.responseJSON;
          });
          return promise;
        },
        'add': function (name, desc) { // Add a workflow
          var promise = $http.post({
            'url': apiPath + '/userwfregistry/add/workflow' + '?workflowName=' + name + '&workflowGraphXml=' + desc
          }).success(function (response) {
            return response.data.responseText;
          });
          return promise;
        },
        'update': function (name, desc) { // Update an already added workflow
          var promise = $http.post({
            'url': apiPath + '/userwfregistry/update/workflow' + '?workflowName=' + name + '&workflowGraphXml=' + desc
          }).success(function (response) {
            return response.data.responseText;
          });
          return promise;
        },
        'delete': function (name) { // Delete a workflow from the registry
          var promise = $http.delete({
            'url': apiPath + '/userwfregistry/remove/workflow',
            'params': {'workflowName': name}
          }).success(function (response) {
            return response.data.responseText;
          });
          return promise;
        },
        'getGraphXML': function () { // Get a XML? description of graph
          var promise = $http.get({
            'url': apiPath + '/userwfregistry/get/workflowgraph',
            'params': {'workflowName': name}
          }).success(function (response) {
            return response.data.responseText;
          });
          return promise;
        }
      },
      //-----------------------------------------------------
      'publish': { // PublishWorkflow Registry
        'exists': function (name) { // Check if a workflow exists in registry
          var promise = $http.get({
            'url': apiPath + '/publishwfregistry/publishwf/exist',
            'params': {'workflowName': name}
          }).success(function (response) {
            return response.data.responseText;
          });
          return promise;
        },
        'getAll': function () { // Get a all workflows
          var promise = $http.get({
            'url': apiPath + '/publishwfregistry/get/publishworkflows'
          }).success(function (response) {
            return response.data.responseJSON;
          });
          return promise;
        },
        'getAllNames': function () { // Get names of all workflows
          var promise = $http.get({
            'url': apiPath + '/publishwfregistry/get/publishworkflownames'
          }).success(function (response) {
            return response.data.responseJSON;
          });
          return promise;
        },
        'add': function (name, pName) { // Publish a workflow
          var promise = $http.post({
            'url': apiPath + '/publishwfregistry/publish/workflow' + '?workflowName=' + name + '&publishWorkflowName=' + pName
          }).success(function (response) {
            return response.data.responseText;
          });
          return promise;
        },
        'addDefault': function (name) {
          var promise = $http.post({
            'url': apiPath + '/publishwfregistry/publish/workflow' + '?workflowName=' + name
          }).success(function (response) {
            return response.data.responseText;
          });
          return promise;
        },
        'delete': function (name) { // Delete a workflow from the registry
          var promise = $http.delete({
            'url': apiPath + '/publishwfregistry/remove/workflow',
            'params': {'workflowName': name}
          }).success(function (response) {
            return response.data.responseText;
          });
          return promise;
        },
        'getGraphXML': function () { // Get a XML? description of graph
          var promise = $http.get({
            'url': apiPath + '/publishwfregistry/get/publishworkflowgraph',
            'params': {'workflowName': name}
          }).success(function (response) {
            return response.data.responseText;
          });
          return promise;
        }
      }
    },

    //=====================================================
    'descriptor': { // Registry API dealing with the defined descriptors

    }
  };
});
