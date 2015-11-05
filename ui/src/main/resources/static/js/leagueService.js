(function () {
	var app = angular.module('leagueservice', []);

	app.factory('leagueService', function ($http, $log) {
		$log.debug('leagueService');
			var service =  {
				getLeagues: function() {
					return $http.get('/leagues/').then(function(result) {
				           return result.data;
				       });
				},
			
				getGames: function(weekId) {
				
					$log.debug("leagueService:getGames weekId="+weekId);
					return $http.get('/games/weekid/'+weekId).success(function(result) {
				           return result.data;
				     });
				},
				
				getMyPicks: function(weekId) {
					$log.debug("leagueService:getPicks weekId="+weekId);
					
					return $http.get('/picks/self/weekid/'+weekId).success(function(result) {
				           return result.data;
				     });
				}
				
				
			}
			return service;
	});
})();	