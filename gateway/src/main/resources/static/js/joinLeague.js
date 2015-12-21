(function () {
	var app = angular.module('joinleague', []);
	
	app.controller('JoinLeagueController', function ($scope, $http, $window, $log) {
//		$scope.usererror = {};
		
		$http.get('/user').success(function(data) {
			$scope.user = data.name;
		});
		
		this.joinLeague = function() {
		
				$log.debug('JoinLeagueController:joinLeague playerleague='+JSON.stringify($scope.playerleague));
				$http({
					method : 'POST',
					url : '/leagues/player',
					contentType : "application/json",
					dataType : "json",
					data : JSON.stringify($scope.playerleague)
				}).success(function(res) { 
	
					$window.location.href = '/index.html';
					
				}).error(function(res) {
					$scope.usererror='Could not join league.  Check to make sure league name is correct.';
				});
			
		};	
	});
})();