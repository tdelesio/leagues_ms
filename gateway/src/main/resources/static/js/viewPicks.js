(function () {
	var app = angular.module('viewpicks', ['leagueservice']);
	
	app.controller('ViewPicksController', function ($scope, $rootScope, $http, $window, $log, $location, makePickPageService, leagueService) {
	
		leagueService.loadHeader().then(function (data) {
			
		$scope.rows = {};
		
			 $http.get('/viewpicks/leagueid/'+$rootScope.leagueId+'/weekid/'+$rootScope.weekId)
		        .success(function(data, status, headers, config) {
		        	$log.debug('ViewPicksController page='+JSON.stringify(data));
		        	
		        	$scope.rows = data;
		        });
		});
		
		$scope.$on('weekChanged', function (events, args) {
			
			$log.debug('weekChanged: week='+args);
			
			$rootScope.weekId = args;
			
			$http.get('/viewpicks/leagueid/'+$rootScope.leagueId+'/weekid/'+$rootScope.weekId)
	        .success(function(data) {

	        	$scope.rows = data;
	        	
	        });
		});
				
	});
	
})();	