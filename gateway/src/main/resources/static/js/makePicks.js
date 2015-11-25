(function () {
	var app = angular.module('makepicks', ['leagueservice']);
	
	app.directive("pickRow", function () {
		return {
			restrict: 'E',
//			templateUrl: 'partials/makePickRow.html', 
			link : function(scope, element, attrs) {
				
//				console.log('attrs.teamid='+attrs.teamid);
				
				var teamRowId;
				if (attrs.teamid === 'fav')
					teamRowId = scope.game.favId;
				else
					teamRowId = scope.game.dogId;
				
				var status="";
				
				console.log('teamRowId='+teamRowId);
//				console.log('scope.game.hasGameStarted='+scope.game.hasGameStarted);
//				console.log('scope.game.id='+scope.game.id);
//				console.log('scope.page.picks='+JSON.stringify(scope.page.picks));
//				console.log('scope.page.picks[scope.game.id]='+JSON.stringify(scope.page.picks[scope.game.id]));
				if (scope.page.picks[scope.game.id] != undefined)
				{
					console.log('scope.page.picks[scope.game.id].teamId='+scope.page.picks[scope.game.id].teamId);
					console.log(scope.page.picks[scope.game.id]!=undefined && scope.page.picks[scope.game.id].teamId === teamRowId);
				}
//				console.log('scope.game.gameWinner='+scope.game.gameWinner);
//				console.log('scope.page.doublePick.pickId='+scope.page.doublePick.pickId);
				
				if (scope.game.hasGameStarted) {
					//game started
					
					//is team picked?
					if (scope.page.picks[scope.game.id]!=undefined && scope.page.picks[scope.game.id].teamId === teamRowId) {
						//team picked
						if (scope.game.gameWinner === scope.page.picks[scope.game.id].teamId) {
							//team won
							
							//is double pick?
							if (scope.page.doublePick.pickId === scope.page.picks[scope.game.id].id) {
								//double pick won
								status = "double_won";
							} else {
								//normal pick won
								status = "won";
							}
						} else {
							//team lost
							
							//is double pick?
							if (scope.page.picks[scope.game.id]!=undefined && scope.page.doublePick.pickId === scope.page.picks[scope.game.id].id) {
								//double pick lose
								status = "double_loss";
							} else {
								//normal pick lose
								status = "loss";
							}
						}
					} else {
						//no team picked
						//normal pick lose
						status = "loss";
					}
				} else {
					//game has not started
					
					//is team picked?
					if (scope.page.picks[scope.game.id]!=undefined && scope.page.picks[scope.game.id].teamId === teamRowId) {
						//team picked
						
						//is double pick?
						if (scope.page.picks[scope.game.id]!=undefined && scope.page.doublePick.pickId === scope.page.picks[scope.game.id].id) {
							//show double pick
							
							status = "double_pick"
						} else {
							//show pick, link to make double
							status = "picked";
						}
					} else {
						//team not picked
						if (scope.page.picks[scope.game.id] != undefined) {
							//show game, link to update pick
							status = "opponent_picked";
						} else {
							//show game, link to make pick
							status = "unpicked";
						}
					}
				}
				
				if (attrs.teamid === 'fav')
					scope.favstatus = status;
				else
					scope.dogstatus = status;
			}
		}
	});
	
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