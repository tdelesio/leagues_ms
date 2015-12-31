(function () {
	var app = angular.module('makepicks', ['leagueservice']);
	
	app.directive("pickRow", ['makePickPageService', function (makePickPageService) {
		return {
			restrict: 'E',
//			templateUrl: 'partials/makePickRow.html', 
//			scope: {
//				page: "=",
//				game: "=",
//				status: "=",
//				addgamestatus: "@"
//					
//			controller: ['$scope', '$http', 'makePickPageService', function($scope, $http, makePickPageService) {
//				makePickPageService.getPage().then(function(data) {
//					$scope.page = data;
//				});
//			}],
			link : function(scope, element, attrs) {
				
//				scope.$watch('page', function () {
//					
//					if (scope.page) {
//						console.log(JSON.stringify(scope.page));
//						console.log(JSON.stringify(scope.game));
				makePickPageService.loadStatus(scope);
				
				
			}
		}
	}]);
	
	//return $http.get('/makepicks').success(function(result);
	app.factory('makePickPageService', ['$q', '$http', function($q, $http) {

//		  var _deferred = $q.defer();
//
//		  $http.get('/makepicks/leagueid/'++/'weekid/'+)
//		        .success(function(data, status, headers, config) {
//		           // this callback will be called asynchronously
//		           // when the response is available
//
//		           _deferred.resolve(data);
//
//		         })
//		        .error(function(data, status, headers, config) {
//		           // called asynchronously if an error occurs
//		           // or server returns response with an error status.
//
//		           _deferred.reject("Error");
//		        });
//		    
//
		  return {
//		    getPage: function(){
//		      return _deferred.promise;
//		    },
//		    
//		    getPage: function(leagueid, weekid) {
//		    	 $http.get('/makepicks/leagueid/'+leagueid+/'weekid/'+weekid).success(function (result) {
//		    		 
//		    	 })
//		    },
		    
		    loadStatus: function(scope) {
				var favId = scope.game.favId;
				var dogId = scope.game.dogId;
				
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
				var doublePickId = "na";
				var hasDoubleGameStarted = false;
				if (scope.page.doublePick != null) {
					doublePickId = scope.page.doublePick.pickId;
					hasDoubleGameStarted = scope.page.doublePick.hasDoubleGameStarted
				}
				
				//console.log('doublePickId='+doublePickId);
				
				var gamestatus, favstatus, dogstatus;
				
				//has game started?
				if (scope.game.hasGameStarted) {
					//game started
					
					//is team picked?
					if (favId === pickedTeamIdForGame) {
						//picked team matches compared team
						
						if (doublePickId === pickIdForGame) {
							favstatus = "double"
						} else {
							favstatus = "pick";
						}
					} else {
						//normal pick lose
						favstatus = "not_pick";
					}
					
					if (dogId === pickedTeamIdForGame) {
						//picked team matches compared team

						if (doublePickId === pickIdForGame) {
							dogstatus = "double"
						} else {
							dogstatus = "pick";
						}
					} else {
						//normal pick lose
						dogstatus = "not_pick";
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
							favstatus = "not_pick";
							dogstatus = "not_pick";
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
					if (pickedTeamIdForGame === favId) {
						//team picked
						
						//is double pick?
						if (doublePickId === pickIdForGame) {
							//pick is double pick
							
							favstatus = "double";
							dogstatus = "opponent";
						
						} else {
							//pick is normal pick
							
							//has double pick game started?
							if (hasDoubleGameStarted) {
								
								favstatus = "pick"
								dogstatus = "opponent";
							} else {
								favstatus = "doubleable";
								dogstatus = "opponent";
							}
						}
					} else if (pickedTeamIdForGame === dogId) {
						//is double pick?
						if (doublePickId === pickIdForGame) {
							//pick is double pick
							
							dogstatus = "double";
							favstatus = "opponent";
						
						} else {
							//pick is normal pick
							
							//has double pick game started?
							if (hasDoubleGameStarted) {
								
								dogstatus = "pick"
								favstatus = "opponent";
							} else {
								dogstatus = "doubleable";
								favstatus = "opponent";
							}
						}
						
					} else {
						dogstatus = "unpicked";
						favstatus = "unpicked";
					}
					
				}
				scope.gamestatus = {};
				scope.teamstatus = {};
				
				scope.status.gamestatus[scope.game.id] = gamestatus;
				//scope.addgamestatus(scope.game.id, gamestatus);
				scope.status.teamstatus[favId] = favstatus;
				scope.status.teamstatus[dogId] = dogstatus;
				
//				console.log('favId='+favId+' dogId='+dogId+' pickedTeam='+pickedTeamIdForGame+' gameWinner='+teamIdForGameWinner+' gamestatus='+scope.status.gamestatus[scope.game.id]+' favstatus='+scope.status.teamstatus[favId]+' dogstatus='+scope.status.teamstatus[dogId]);

			}
		  }

		}]);
	
	app.controller('MakePicksController', function ($scope, $rootScope, $http, $window, $log, makePickPageService, leagueService) { 
		
		$scope.page = {};
		$scope.status = {};
		$scope.status.gamestatus = {};
		$scope.status.teamstatus = {};

//		makePickPageService.getPage().then(function(data) {
		leagueService.loadHeader().then(function (data) {
			$http.get('/makepicks/leagueid/'+$rootScope.leagueId+'/weekid/'+$rootScope.weekId).success(function(data) {	
				$log.debug('MakePickController:loadMakePicks='+JSON.stringify(data))
				$scope.page = data;
			})
		});
//			,
//		function (response) {
//			$scope.error = "Could not load page.  Please try again later."
//		});
		
		
		$scope.$on('weekChanged', function (events, args) {
			
//			$scope.status.gamestatus = {};
//			$scope.status.teamstatus = {};	
			
			$log.debug('weekChanged: week='+args);
			
			$rootScope.weekId = args;
			
			$http.get('/makepicks/leagueid/'+$rootScope.leagueId+'/weekid/'+$rootScope.weekId)
	        .success(function(data) {
//	        	$log.debug('MakePicksController:changeWeek='+JSON.stringify(data))
	        	$scope.page = data;
	        	
	        	var arrayLength = data.games.length;
	        	for (var i = 0; i < arrayLength; i++) {
	        		$scope.game=data.games[i];
	        		makePickPageService.loadStatus($scope);
	        	    }
	        	
	        	
	        });
		});
		
				
		$scope.makePick = function(game, rowid, $index) {
		
			
			var gameid = game.id;
			var teamid;
			if (rowid === 'fav')
				teamid = game.favId;
			else
				teamid = game.dogId;
			var pick = $scope.page.picks[gameid];
			
			$log.debug("MakePicksController:makePick gameId="+gameid+" teamid="+teamid+" pick="+JSON.stringify(pick));
			
				var local_model = {};
			
				local_model.teamId = teamid;
				local_model.gameId = gameid;
				local_model.weekId = $scope.week.weekId;
				local_model.leagueId = $scope.league.id;
				
				var doubleSubmission = false;
				var url = '/picks/';
				if (pick == undefined || pick.id == undefined)
					method = "POST";
				else
				{
					method = "PUT";
					
					//check to see if the game that was clicked is the one that was already click (making double)
					if (pick.gameId === gameid && teamid === pick.teamId) { 
						url = '/picks/double';
						local_model.pickId = pick.id;
						doubleSubmission = true;
					}
					else {
						//it's not so this means they are switching off their double
						local_model.id = pick.id;
					}
				}
				
				$log.debug("MakePicksController:makePick submittedModel="+JSON.stringify(local_model));
				
				$http({
					method : method,
					url : url,
					contentType : "application/json",
					dataType : "json",
					data : JSON.stringify(local_model)
				}).success(function(res) { 
	
					$scope.error = undefined;
					
					if ($scope.page.picks == undefined)
						$scope.page.picks = {};
					
					if (doubleSubmission) {
						if (res.previousDoubleGameId) {
							//remove the previous x2
							$log.debug("setting team to doubleable = "+$scope.page.picks[res.previousDoubleGameId].teamId);
							$scope.status.teamstatus[$scope.page.picks[res.previousDoubleGameId].teamId]='doubleable';
						}
							
						$log.debug("setting team to double = "+$scope.page.picks[res.gameId].teamId);	
						$scope.status.teamstatus[$scope.page.picks[res.gameId].teamId]='double';
						
					} else {
						if ($scope.page.picks[gameid]) { 
							angular.copy(res, $scope.page.picks[gameid]);
						} else {
							$scope.page.picks[gameid] = res;
						}
						
						if (game.favId === res.teamId) {
							$scope.status.teamstatus[game.favId]='doubleable';
							$scope.status.teamstatus[game.dogId]='opponent';
						} else {
							$scope.status.teamstatus[game.favId]='opponent';
							$scope.status.teamstatus[game.dogId]='doubleable';
						}
					}
					
					
					
					
				}).error(function(res) {
					$scope.error = "Pick was not made, please retry.";
				});
			
		};	
	});
})();	