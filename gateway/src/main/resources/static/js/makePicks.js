(function () {
	var app = angular.module('makepicks', ['leagueservice']);
	
	app.controller('MakePicksController', function ($scope, $rootScope, $http, $window, $log, leagueService) { 
		
		$scope.page = {};
		
		
		leagueService.loadMakePicksPage().then(function(data) {
			$log.debug('MakePickController:loadMakePicks='+JSON.stringify(data.data))
			$scope.page = data.data;
			$rootScope.nav = data.data.nav;
			$rootScope.$broadcast('pageLoaded');
			
		});
		
		$scope.makePick = function(gameid, teamid, pickid) {
		
			$log.debug("MakePicksController:makePick gameId="+gameid+" teamid="+teamid+" pickId="+pickid);
			
				var local_model = {};
			
				local_model.teamId = teamid;
				local_model.gameId = gameid;
				local_model.weekId = $scope.week.weekId;
				
				if (pickid == undefined)
					method = "POST";
				else
					method = "PUT";
				
				$log.debug("MakePicksController:makePick submittedModel="+JSON.stringify(local_model));
				
				$http({
					method : method,
					url : '/picks/',
					contentType : "application/json",
					dataType : "json",
					data : JSON.stringify(local_model)
				}).success(function(res) { 
	
					if ($scope.page.picks == undefined)
						$scope.page.picks = {};
					
					$scope.page.picks[gameid] = res;
					
				}).error(function(res) {
					alert('fail');
				});
			
		};	
	});
})();	