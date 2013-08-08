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

var app = angular.module('WebUI', ['ui.state']);

//_______________________________________________________________________________
// Configuration
//_______________________________________________________________________________

app.config(function ($stateProvider, $routeProvider) { // Setup views in the webapp
  //To enable the Cross Origin Resource Sharing in application
  //$httpProvider.defaults.useXDomain = true;
  //delete $httpProvider.defaults.headers.common['X-Requested-With'];

  // Views corresponding to the state of the application when just loaded
  $stateProvider.state('index', {
    url: '',
    views: {
      'navbar': {
        templateUrl: 'views/index.navbar.html'
      },
      'sidenavbar': {
        templateUrl: 'views/index.sidenavbar.html'
      },
      'canvas': {
        templateUrl: 'views/index.canvas.html'
      }
    }
  });
  // Views corresponding to the worklow creation interface
  $stateProvider.state('create', {
    url: '/create',
    views: {
      'navbar': {
        templateUrl: 'views/index.navbar.html'
      },
      'sidenavbar': {
        templateUrl: 'views/create.sidenavbar.html'
      },
      'canvas': {
        templateUrl: 'views/create.canvas.html'
      }
    }
  });
  // Views corresponding to the workflow execution interface
  $stateProvider.state('exec', {
    url: '/exec',
    views: {
      'navbar': {
        templateUrl: 'views/exec.navbar.html'
      },
      'sidenavbar': {
        templateUrl: 'views/exec.sidenavbar.html'
      },
      'canvas': {
        templateUrl: 'views/exec.canvas.html'
      }
    }
  })
  .state('exec.addhost', {
    url:'/addHost',
      views:{
        'builder':{
          templateUrl: 'views/builder.addHost.html'
      }
    }
  })
  .state('exec.launchWorkflow', {
    url:'/launchWorkflow',
    views:{
      'builder':{
        templateUrl: 'views/builder.launchWorkflow.html'
      }
    }
  })
  .state('exec.globusFileTransfer', {
    url:'/globusFileTransfer',
    views:{
      'builder':{
        templateUrl: 'views/builder.globusFileTransfer.html'
      }
    }
  })
  .state('exec.workflowMonitoring', {
      url:'/workflowMonitoring',
      views:{
        'builder':{
          templateUrl: 'views/builder.workflowMonitoring.html'
        }
      }
    })
  .state('exec.workflowProperties', {
    url:'/workflowProperties',
    views:{
      'builder':{
        templateUrl: 'views/builder.workflowProperties.html'
      }
    }
  })
  .state('exec.registerApplication', {
    url:'/registerApplication',
    views:{
      'builder':{
        templateUrl: 'views/builder.registerApplication.html'
      }
    }
  });

  /*
  $stateProvider.state('contacts', {
    templateUrl: function (stateParams){
        return '/partials/contacts.' + stateParams.filterBy + '.html';
    },
    controller: 'ContactsCtrl'
  });
  */
  // Views corresponding to the workflow monitoring interface
  $stateProvider.state('monitor', {
    url: '/monitor',
    views: {
      'navbar': {
        templateUrl: 'views/exec.navbar.html'
      },
      'sidenavbar': {
        templateUrl: 'views/monitor.sidenavbar.html'
      },
      'canvas': {
        templateUrl: 'views/monitor.canvas.html'
      }
    }
  });
});

//_______________________________________________________________________________
// Services
//_______________________________________________________________________________

app.factory('MessageQueue', function () { // A message queue to communicate between different controllers
  // Inspired from https://github.com/phiggins42/bloody-jquery-plugins/blob/master/pubsub.js
  var cache = {};
  return {
    // Publish a message to the queue's specified topic
    // All subscribed listeners are notified of pusblication
    publish: function (topic, args) {
      cache[topic] && $.each(cache[topic], function () {
        this.apply(null, args || []);
      });
    },

    // Subscribe to a specific topic in the queue and specify the callback that needs to be called
    // on publication in that topic
    subscribe: function (topic, callback) {
      if (!cache[topic]) {
        cache[topic] = [];
      }
      cache[topic].push(callback);
      return [topic, callback];
    },

    // Remove a listener from the list of callback functions
    unsubscribe: function (handle) {
      var t = handle[0];
      cache[t] && $.each(cache[t], function (idx) {
        if (this === handle[1]) {
          cache[t].splice(idx, 1);
        }
      });
    }
  };
});

//_______________________________________________________________________________
// Directives
//_______________________________________________________________________________

app.directive('markdown', function ($compile, $http) { // Render markdown in HTML`
  var converter = new Showdown.converter();
  return {
    restrict: 'E',
    replace: true,
    link: function (scope, element, attrs) {
      if ('src' in attrs) {
        attrs.$observe('src', function (value) {
          if (value) {
            $http.get(attrs.src).then(function (data) {
              element.html(converter.makeHtml(data.data));
            });
          }
        });
      } else {
        element.html(converter.makeHtml(element.text()));
      }
    }
  };
});
