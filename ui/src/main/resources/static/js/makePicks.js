(function () {
	var app = angular.module('makepicks', ['leagueservice']);
	
	app.controller('MakePicksController', function ($scope, $http, $window, $log, leagueService) { 
		
		
		$scope.pickMap = {};
		$scope.gameMetaMap = {};
		$scope.showGS = true;
		
		//get the games for the week and league
		
		$scope.$on('weekLoaded', function (event) {
			$log.debug(event);
			$log.debug('MakePickController week='+JSON.stringify($scope.week));
			
			//load all the games
			leagueService.getGames($scope.week.weekId).then(function(data) {
				$log.debug('MakePickController:games='+JSON.stringify(data.data))
				$scope.games = data.data;
				
				
			});
			
			//load all the picks
			leagueService.getMyPicks($scope.week.weekId).then(function(data) {
				$log.debug('MakePickController:picks='+JSON.stringify(data.data))
				$scope.pickMap = data.data;
			});
		
			
		});
		
		$scope.makePick = function(gameid, teamid, pickid) {
		
			$log.debug("MakePicksController:makePick gameId="+gameid+" teamid="+teamid+" pickId="+pickid);
			
				var local_model = {};
			
				local_model.teamId = teamid;
				local_model.gameid = gameid;
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
	
					$scope.pickMap[gameId] = res;
					
				}).error(function(res) {
					alert('fail');
				});
			
		};	
	});
})();	