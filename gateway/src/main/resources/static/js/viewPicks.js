(function () {
	var app = angular.module('viewpicks', ['leagueservice']);
	
	app.controller('ViewPicksController', function ($scope, $rootScope, $http, $window, $log, $location, makePickPageService) {
	
		
		 $http.get('/leaders/week/leagueid/'+$rootScope.leagueId+'/weekid/'+$rootScope.weekId)
	        .success(function(data, status, headers, config) {
	        	$log.debug('ViewPicksController page='+JSON.stringify(data));
	        });
				
	});
	
})();	