'use strict';

var app = angular.module('WebUI', ['ui.state']);

// Configure the routing information for the webapp
app.config(function ($stateProvider, $routeProvider) {
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
  // Views corresponding to the workflow execution and monitoring interface
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
  });
});

// Render markdown in the HTML page
// Use as - <markdown>....</markdown> or <markdown src="foo.md"></markdown>
app.directive("markdown", function ($compile, $http, $interpolate) {
  var converter = new Showdown.converter();
  return {
    restrict: 'E',
    replace: true,
    link: function (scope, element, attrs) {
      if ("src" in attrs) {
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
