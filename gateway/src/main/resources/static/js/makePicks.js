(function () {
	var app = angular.module('makepicks', ['leagueservice']);
	
	app.directive("pickRow", function () {
		return {
			restrict: 'E',
//			templateUrl: 'partials/makePickRow.html', 
			link : function(scope, element, attrs) {
				
//				console.log('attrs.teamid='+attrs.teamid);
				
				//team that is being checked
				var teamId;
				if (attrs.teamid === 'fav')
					teamId = scope.game.favId;
				else
					teamId = scope.game.dogId;
				
				//set local variable for pickedTeam, nil if none
				var pickedTeamIdForGame, pickIdForGame, pickMadeForGame;
				if (scope.page.picks[scope.game.id]!=undefined) {
					//check to see if a pick has been made for the game
					pickedTeamIdForGame = scope.page.picks[scope.game.id].teamId;
					pickIdForGame = scope.page.picks[scope.game.id].id;
					pickMadeForGame = true;
				}
				else {
					pickedTeamIdForGame = "nil";
					pickIdForGame = "nil";
					pickMadeForGame = false;
				}
				
				//set game winner
				var teamIdForGameWinner = scope.game.gameWinner;
					
				//set the double pickId
				var doublePickId = scope.page.doublePick.pickId;
				
				var gamestatus, teamstatus;
				
//				console.log('teamId='+teamId);
//				console.log('scope.game.hasGameStarted='+scope.game.hasGameStarted);
//				console.log('scope.game.id='+scope.game.id);
//				console.log('scope.page.picks='+JSON.stringify(scope.page.picks));
//				console.log('scope.page.picks[scope.game.id]='+JSON.stringify(scope.page.picks[scope.game.id]));
//				if (scope.page.picks[scope.game.id] != undefined)
//				{
//					console.log('scope.page.picks[scope.game.id].teamId='+scope.page.picks[scope.game.id].teamId);
//					console.log(scope.page.picks[scope.game.id]!=undefined && scope.page.picks[scope.game.id].teamId === teamId);
//				}
//				console.log('scope.game.gameWinner='+scope.game.gameWinner);
//				console.log('scope.page.doublePick.pickId='+scope.page.doublePick.pickId);
				
				//has game started?
				if (scope.game.hasGameStarted) {
					//game started
					
					//is team picked?
					if (teamId === pickedTeamIdForGame) {
						//picked team matches compared team

						teamstatus = "pick";
					} else {
						//normal pick lose
						teamstatus = "not_pick";
					}
				
					//has game ended?
					if (scope.game.hasScoresEntered) {
						//game is over
						
						//is picked team the winner?
						if (pickedTeamIdForGame === teamIdForGameWinner) {
							//is double pick?
							if (doublePickId === pickIdForGame) {
								//team is winner and is double
								gamestatus = "double_won";
							} else {
								//team is winner
								gamestatus = "won";
							}
						} else {
							if (doublePickId === pickIdForGame) {
								//team is loser and dobule
								gamestatus = "double_loss";
								
							} else {
								//team is loser
								gamestatus = "loss";
							}
						}
							
						
						
					} else {
						//game is not over
						
						//has pick been made for game?
						if (pickMadeForGame) {
							
							if (doublePickId === pickIdForGame) {
								//pick is a double pick
								
								gamestatus = "locked_double_pick";
							} else {
								//pick not double pick
								
								gamestatus = "locked_pick";
							}
				
						} else {
							gamestatus = "no_pick";
							teamstatus = "not_pick";
						}
					}
				
					
				} else {
					//game has not started
					
					//has pick been made for game?
					if (pickMadeForGame) {
						
						if (doublePickId === pickIdForGame) {
							//pick is a double pick
							
							gamestatus = "double_pick";
						} else {
							//pick not double pick
							
							gamestatus = "pick";
						}
			
					} else {
						gamestatus = "open";
					}
					
					
					//is team picked?
					if (pickedTeamIdForGame === teamId) {
						//team picked
						
						//is double pick?
						if (doublePickId === pickIdForGame) {
							//pick is double pick
							
							teamstatus = "double";
						
						} else {
							//pick is normal pick
							
							//has double pick game started?
							if (scope.page.doublePick.hasDoubleGameStarted) {
								
								teamstatus = "pick"
							} else {
								teamstatus = "doubleable";
							}
								
							
						}
					} else {
						
						//team not picked
						if (pickMadeForGame && pickedTeamIdForGame != teamId) {
							//show game, link to update pick
							teamstatus = "opponent";
						} else {
							//show game, link to make pick
							teamstatus = "unpicked";
						}
					}
				}
				
				scope.gamestatus = gamestatus;
				
				
				//set the fav and dog status to decide how to handle linking
				if (attrs.teamid === 'fav') {
					scope.favstatus = teamstatus;
				}
				else {
					scope.dogstatus = teamstatus;
				}
				
				//set the game status for display
				
				console.log('teamId='+teamId+' pickedTeam='+pickedTeamIdForGame+' gameWinner='+teamIdForGameWinner+' gamestatus='+scope.gamestatus+' teamstatus='+teamstatus);
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