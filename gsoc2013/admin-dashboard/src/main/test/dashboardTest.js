'use strict';

describe('adminMonitorApp', function() {
  describe('controllers', function() {
    beforeEach(module('controllers'));
	describe('DateDialogController', function(){
      it('should be defined',
	    inject(function($rootScope, $controller) {
          var scope = $rootScope.$new(), dialog = {};
          var ctrl = $controller("DateDialogController", {$scope : scope, dialog : dialog});
          expect(ctrl).toBeDefined();
      }));
    });
	
	describe('LoginCtrl', function(){
      it('should be defined',
	    inject(function($rootScope, $controller) {
          var scope = $rootScope.$new(), user = {}, server = {};
          var ctrl = $controller("LoginCtrl", {$scope : scope, user : user, server : server});
          expect(ctrl).toBeDefined();
      }));
    });
	
	describe('ExperimentCtrl', function(){
      it('should be defined',
	    inject(function($rootScope, $controller, $location, $routeParams) {
          var scope = $rootScope.$new();
          var ctrl = $controller("ExperimentCtrl", {$scope : scope, $location : $location, $routeParams : $routeParams, Experiment : {}, Workflow : {}, User : {}});
          expect(ctrl).toBeDefined();
      }));
    });
	
	describe('ProjectCtrl', function(){
      it('should be defined',
	    inject(function($rootScope, $controller, $httpBackend) {
          var scope = $rootScope.$new();
		  $httpBackend.when('GET', 'api/projectregistry/get/projects').respond({"workspaceProjects":[{"projectName":"default","gateway":{"gatewayName":"default"},"airavataUser":{"userName":"blah user"}}]});
          var ctrl = $controller("ProjectCtrl", {$scope : scope, project : {}});
          expect(ctrl).toBeDefined();
      }));
    });
	
	describe('WorkflowCtrl', function(){
      it('should be defined',
	    inject(function($rootScope, $controller, $location, $routeParams) {
          var scope = $rootScope.$new();
          var ctrl = $controller("WorkflowCtrl", {$scope : scope, $location : $location, $routeParams : $routeParams, Experiment : {}, Workflow : {}, User : {}});
          expect(ctrl).toBeDefined();
      }));
    });
	
  });
  
  describe('services', function() {
    var project,experiment,workflow, backend;
    beforeEach( function() {
	  module('services');
	  inject(function(Project,Experiment,Workflow,$httpBackend) {
	    //Mock Responses
	    $httpBackend.when('GET', 'api/projectregistry/get/projects').respond({"workspaceProjects":[{"projectName":"default","gateway":{"gatewayName":"default"},"airavataUser":{"userName":"blah user"}}]});
		$httpBackend.when('GET', 'api/userwfregistry/get/workflows').respond({"workflowList":[{"workflowName":"Dihedral-Optimization-Workflow","workflowGraph":"<xwf:workflow xwf:version=\"0.8\" xmlns:xwf=\"http://airavata.apache.org/xbaya/xwf\">\n  <xgr:graph xgr:version=\"0.8\" xgr:type=\"ws\" xmlns:xgr=\"http://airavata.apache.org/xbaya/graph\">\n    <xgr:id>Dihedral_Optimization_Workflow</xgr:id>\n    <xgr:name>Dihedral-Optimization-Workflow</xgr:name>\n    <xgr:description></xgr:description>\n    <xgr:metadata>\n      <appinfo xmlns=\"http://www.w3.org/2001/XMLSchema\">\n\n</appinfo>\n    </xgr:metadata>\n    <xgr:node xgr:type=\"ws\">\n      <xgr:id>Prepare_Model_Reference_Data_invoke</xgr:id>\n      79Lw4YPS9euH0pQ0HRJS1sv+/fv\r\nucFB/r\nqwlPEA8AAAAAAAAAAAAAwD8BQTwAAAAAAAAAAAAAAATxAAAAAAAAAAAAAAAQxAMAAAAAAAAAAAAA\r\nQBAPAAAAAAAAAAAAAACu3/8HEK5g4Oe7Z1UAAAAASUVORK5CYII=\r\n</xwf:image>\n</xwf:workflow>"}]});
		$httpBackend.when('GET', 'api/provenanceregistry/get/experiment?experimentId=exp1').respond({"executionStatus":null,"user":"user blah","output":null,"experimentId":"blah tmpl","metadata":null,"workflowInstanceName":null,"workflowInstanceDataList":[{"workflowInstance":{"experimentId":"blah tmpl","workflowExecutionId":"blah tmpl","templateName":"tmplate"},"workflowInstanceStatus":{"executionStatus":"UNKNOWN","statusUpdateTime":1373576118000,"workflowInstance":{"experimentId":"blah tmpl","workflowExecutionId":"blah tmpl","templateName":"blah tmpl"}},"nodeDataList":[{"workflowInstanceNode":{"workflowInstance":{"experimentId":"blah tmpl","workflowExecutionId":"blah tmpl","templateName":"blah tmpl"},"nodeId":null,"originalNodeID":null,"executionIndex":0},"inputData":null,"outputData":null,"input":null,"output":null,"status":{"executionStatus":"UNKNOWN","statusUpdateTime":1379801737868,"workflowInstanceNode":{"workflowInstance":{"experimentId":"blah tmpl","workflowExecutionId":"blah tmpl","templateName":"blah tmpl"},"nodeId":null,"originalNodeID":null,"executionIndex":0}},"type":"UNKNOWN","experimentId":"blah exp","workflowExecutionId":"blah wrkflw","nodeId":null}]}],"lazyLoaded":false});
		$httpBackend.when('GET', 'api/experimentregistry/get/experiments/all').respond({"experiments":[{"experimentId":"Blah 1","submittedDate":1373576118000,"user":{"userName":"user blah"},"project":{"projectName":"default","gateway":{"gatewayName":"default"},"airavataUser":{"userName":"user blah"}},"gateway":{"gatewayName":"default"}}]});
		$httpBackend.when('GET', 'api/provenanceregistry/get/experiment/user?username=user1').respond({"experiments":[{"experimentId":"Blah 1","submittedDate":1373576118000,"user":{"userName":"user blah"},"project":{"projectName":"default","gateway":{"gatewayName":"default"},"airavataUser":{"userName":"user blah"}},"gateway":{"gatewayName":"default"}}]});
		$httpBackend.when('GET', 'api/provenanceregistry/workflow/errors?experimentId=exp1&workflowInstanceId=wrkflw1').respond({"WorkflowExecutionErrorList":[{"@class":"org.apache.airavata.registry.api.workflow.NodeExecutionError","source":"NODE","errorTime":1378760979000,"errorCode":null,"errorMessage":"Standard output is empty.","errorDescription":"Standard output is empty.","errorLocation":null,"actionTaken":null,"errorReported":null,"errorReference":0,"experimentId":"blah exp","workflowInstanceId":"blah wrkflw"}]});
		$httpBackend.when('GET', 'api/provenanceregistry/node/errors?experimentId=exp1&workflowInstanceId=wrkflw1&nodeId=node1').respond({"nodeExecutionErrorList":[{"@class":"org.apache.airavata.registry.api.workflow.NodeExecutionError","source":"NODE","errorTime":1378760979000,"errorCode":null,"errorMessage":"Standard output is empty.","errorDescription":"Standard output is empty.","errorLocation":null,"actionTaken":null,"errorReported":null,"errorReference":0,"experimentId":"blah exp","workflowInstanceId":"blah wrkflw","nodeId":"Prepare_Model_Reference_Data_invoke"}]});
	    project = Project;
		experiment = Experiment;
		workflow = Workflow;
		backend = $httpBackend;
	  });
	});
	
	describe('Project Service', function() {
	  it('should have an getAll function', function () { 
        expect(angular.isFunction(project.getAll)).toBe(true);
      });
	});
	
	describe('Experiment Service', function() {
	  it('should have an getAll function', function () { 
        expect(angular.isFunction(experiment.getAll)).toBe(true);
      });
	  it('should return a not response for getAll function', function () { 
        var hi = experiment.getAll();
		console.log(hi);
      });
	  it('should have an getByUser function', function () { 
        expect(angular.isFunction(experiment.getByUser)).toBe(true);
      });
	  it('should have an getById function', function () { 
        expect(angular.isFunction(experiment.getById)).toBe(true);
      });
	  it('should have an search function', function () { 
        expect(angular.isFunction(experiment.search)).toBe(true);
      });
	});
	
	describe('Workflow Service', function() {
	  it('should have an getAll function', function () { 
        expect(angular.isFunction(workflow.getAll)).toBe(true);
      });
	  it('should have an getWorkflowExecutionErrors function', function () { 
        expect(angular.isFunction(workflow.getWorkflowExecutionErrors)).toBe(true);
      });
	  it('should have an getNodeExecutionErrors function', function () { 
        expect(angular.isFunction(workflow.getNodeExecutionErrors)).toBe(true);
      });
	});
  });
  
  describe('config', function() {
    var user,server;
    beforeEach( function() {
	  module('config');
	  inject(function(User,Server) {
	    user = User;
		server = Server;
	  });
	});
	
	describe('User Service', function() {
	  it('should have an getAuthHeader function', function () { 
        expect(angular.isFunction(user.getAuthHeader)).toBe(true);
      });
	  it('should have an clearCredentials function', function () { 
        expect(angular.isFunction(user.clearCredentials)).toBe(true);
      });
	  it('should have an getUsername function', function () { 
        expect(angular.isFunction(user.getUsername)).toBe(true);
      });
	  it('should have an login function', function () { 
        expect(angular.isFunction(user.login)).toBe(true);
      });
	  it('should have an getAll function', function () { 
        expect(angular.isFunction(user.getAll)).toBe(true);
      });
	});
	
	describe('Server Service', function() {
	  it('should have an setEndpoint function', function () { 
        expect(angular.isFunction(server.setEndpoint)).toBe(true);
      });
	  it('should have an clearEndpoint function', function () { 
        expect(angular.isFunction(server.clearEndpoint)).toBe(true);
      });
	  it('should have an getEndpoint function', function () { 
        expect(angular.isFunction(server.getEndpoint)).toBe(true);
      });
	});
  });
});