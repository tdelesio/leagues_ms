//http://www.nfl.com/liveupdate/scorestrip/ss.xml
(function () {
	var app = angular.module('admin', ['ui.router']);
	
	app.config(function($stateProvider, $urlRouterProvider) {
		  //
		  // For any unmatched url, redirect to /state1
		  $urlRouterProvider.otherwise("/setup");
		  //
		  // Now set up the states
		  $stateProvider
		    .state('setup', {
		      url: "/setup",
		      templateUrl: "setupWeek.html"
		    })
		    .state('create', {
		      url: "/create",
		      templateUrl: "createWeek.html"
		    })
		    .state('leagues', {
		      url: "/leagues",
		      templateUrl: "createLeague.html"
		    })
		   .state('seasons', {
		      url: "/seasons",
		      templateUrl: "createSeasons.html"
		    })
		});
	
	app.directive('chrome', function() {
		return {
			restrict: 'E',
			templateUrl: 'partials/chrome.html'
		};
	});
	
	app.directive('createWeek', function() {
		return {
			restrict: 'E',
			templateUrl: 'partials/createWeek.html'
		};
	});
	
	app.directive('createGame', function() {
		return {
			restrict: 'E',
			templateUrl: 'partials/createGame.html'
		};
	});
	
	app.directive('createLeague', function() {
		return {
			restrict: 'E',
			templateUrl: 'partials/createLeague.html'
		};
	});
	
	app.directive('createSeason', function() {
		return {
			restrict: 'E',
			templateUrl: 'partials/createSeason.html'
		};
	});
	
	app.directive('games', function() {
		return {
			restrict: 'E',
			templateUrl: 'partials/games.html'
		};
	});
	
	app.directive('leagues', function() {
		return {
			restrict: 'E',
			templateUrl: 'partials/leagues.html'
		};
	});
	
	app.directive('seasons', function() {
		return {
			restrict: 'E',
			templateUrl: 'partials/seasons.html'
		};
	});
	
	app.directive('weeks', function() {
		return {
			restrict: 'E',
			templateUrl: 'partials/weeks.html'
		};
	});
	
	app.directive('editGame', function() {
		return {
			restrict: 'E',
			templateUrl: 'partials/editGame.html'
		};
	});
	
	app.directive('playersInLeague', function() {
		return {
			restrict: 'E',
			templateUrl: 'partials/playersInLeague.html'
		};
	});
	
	
	app.factory('leagueService', function ($http, $log) {
	$log.debug('leagueService');
		var service =  {
			getLeagues: function() {
				return $http.get('/admin/leagues/').then(function(result) {
			           return result.data;
			       });
			},
		
			getGames: function(seasonId) {
			return $http.get('/admin/games/weekid/'+seasonId).success(function(result) {
		           return result.data;
		       });
			}
		}
		return service;
	});
	
	app.controller('ChromeController', function ($http, $scope) {
		$http.get('/admin/user').success(function(data) {
			$scope.user = data.name;
		});
		
		$scope.logout = function () {
//			   console.log("I am here"+JSON.stringify($location));
			   $http.post('/admin/logout', {}).success(function() {
				   				console.log("logout sucess...");
			        }).error(function(data) {
			          console.log("Logout failed");
			          
			        });
			   
			    }
	});
	
	//***************  Season  **************************
	app.controller('CreateSeasonController', function ($scope, $http, $window, $log, leagueService) {
		
		$scope.showseasons=true;
		$scope.season={};
		$scope.season.leagueType = "pickem";
		$scope.season.leagueTypes="pickem";
		$scope.season.startYear = 2017;
		$scope.season.endYear = 2018;
		
		$http.get('/admin/leagues/seasons/current').success(function(data) {
			$scope.seasons = data;
			if (data[0] === undefined)
				$scope.showseasons=false;
		});
		
		$http.get('/admin/leagues/types').success(function(data) {
			if (data[0] )
				$scope.season.leagueTypes=data;
		});
		
		this.deleteSeason = function(season) {
			$log.debug("CreateSeasonController:deleteSeason: season.id="+season.id);
			
			if (confirm("Are yoou sure you want to delete season?")) {
		    
				var url = '/admin/leagues/seasons/'+season.id;
				$http({
					method : "DELETE",
					url : url,
					contentType : "application/json",
					dataType : "json",
				}).success(function(res) { 
					
					$http.get('/admin/leagues/seasons/current').success(function(data) {
						$scope.seasons = data;
						if (data[0] === undefined)
							$scope.showseasons=false;
					});
					
				}).error(function(res) {
					alert('fail');
				});
			
		 
		    }
		}
		
		this.addSeason = function() {

		
			$log.debug("CreateSeasonController:addSeason");
			
			$http({
				method : "POST",
				url : '/admin/leagues/seasons/',
				contentType : "application/json",
				dataType : "json",
				data : JSON.stringify($scope.season)
			}).success(function(res) { 
				
//				$scope.showgames = true;
//				$http.get('/admin/leagues/seasons/current').success(function(data) {
//					$scope.seasons = data;
//				});
				$window.location.href = '/admin/#/leagues';
				
			}).error(function(res) {
				alert('fail');
			});
		}
	});
	
	//***************  LEAGUES  **************************
	app.controller('CreateLeagueController', function ($scope, $http, $window, $log, leagueService) {
	
		$scope.league = {};
		$scope.season = {};
		$scope.showgames=true;
		$scope.hideplayers=false;
		
		leagueService.getLeagues().then(function(data) {
			$scope.leagues = data;
		});
		
		$http.get('/admin/leagues/seasons/current').success(function(data) {
			$scope.seasons = data;
			if (data[0] === undefined)
				$scope.showgames=false;
			else 
			 $scope.league.seasonId = data[0].id;
		});
		
		$scope.season.startYear = 2016;
		$scope.season.endYear = 2017;
		$scope.season.leagueType = "pickem";
		
		
		this.addLeague = function() {

			$log.debug("CreateLeagueController:addLeague");
			
			$http({
				method : "POST",
				url : '/admin/leagues/',
				contentType : "application/json",
				dataType : "json",
				data : JSON.stringify($scope.league)
			}).success(function(res) { 
				
				leagueService.getLeagues().then(function(data) {
					$scope.leagues = data;
				});
				
			}).error(function(res) {
				alert('fail');
			});
		}
		
		$scope.showPlayers = function(leagueId) {
			
			$log.debug("CreateLeagueController:showPlayers: leagueId="+leagueId);
			$scope.hideplayers=true;
			
			$http.get('/admin/leagues/player/leagueid/'+leagueId).success(function(data) {
				$scope.players = data;
				
			});
		}
		
	
		
		$scope.deleteLeague = function(leagueId) {
			$log.debug("CreateLeagueController:deleteLeague: leagueId="+leagueId);
			$scope.hideplayers=true;
			
			if (confirm("Are you sure you want to delete league?")) {
		    
				var url = '/admin/leagues/'+leagueId;
				$http({
					method : "DELETE",
					url : url,
					contentType : "application/json",
					dataType : "json",
				}).success(function(res) { 
					
					leagueService.getLeagues().then(function(data) {
						$scope.leagues = data;
					});
					
				}).error(function(res) {
					alert('fail');
				});
			
		 
		    }
		}
		
		$scope.dummyLeague = function () {
			$http({
				method : "POST",
				url : '/admin/dummy',
				contentType : "application/json",
				dataType : "json"
//					,
//				data : JSON.stringify($scope.league)
			}).success(function(res) { 
				
				leagueService.getLeagues().then(function(data) {
					$scope.leagues = data;
				});
				
			}).error(function(res) {
				alert('fail');
			});
		}
		
		$scope.randomLeague = function () {
			$http({
				method : "POST",
				url : '/admin/random',
				contentType : "application/json",
				dataType : "json"
//					,
//				data : JSON.stringify($scope.league)
			}).success(function(res) { 
				
				leagueService.getLeagues().then(function(data) {
					$scope.leagues = data;
				});
				
			}).error(function(res) {
				alert('fail');
			});
		}
		
		
			
	});
	
	//***************  WEEKS  *************************
	app.controller('CreateWeekController', function ($scope, $http, $window, $log, leagueService) {
		$scope.week = {};
		$scope.weeks = {};
//		leagueService.getLeagues().then(function(data) {
//			$scope.leagues = data;
//			$scope.week.seasonId = data[0].seasonId;
//		});
		
		$http.get('/admin/leagues/seasons/current').success(function(data) {
			$scope.seasons = data;
			$scope.week.seasonId = data[0].id;
			
			var url = '/admin/weeks/seasonid/'+data[0].id;
			$http.get(url).success(function(data) {
				$scope.weeks = data;
			});
		});
		
		
		
		this.autoWeek = function(week) {
			
			if (week === undefined) {
				week = {};
				week.seasonId = $scope.week.seasonId;
			}
			
			$log.debug('autoWeek: week='+JSON.stringify(week));
			
			$http({
				method : "POST",
//				method : "GET",
//				beforeSend: function (request) {
//			        request.setRequestHeader(header, token);
//			     },
				url : '/admin/games/autosetup',
//				url : '/games/role',
				contentType : "application/json",
				dataType : "json",
				//data : $('form').serializeObject(),
				data : JSON.stringify(week)
			}).success(function(res) { 
				$window.location.href = 'index.html';
			}).error(function(res) {
				alert('fail');
			});
		}
		
		this.addWeek = function(week) {

			$http({
				method : "POST",
//				beforeSend: function (request) {
//			        request.setRequestHeader(header, token);
//			     },
				url : '/admin/weeks/',
				contentType : "application/json",
				dataType : "json",
				//data : $('form').serializeObject(),
				data : JSON.stringify($scope.week)
			}).success(function(res) { 
				
				$scope.createWeek.$setPristine();
				$scope.week = {};
				$window.location.href = 'index.html';
			}).error(function(res) {
				alert('fail');
			});
		}
		
//		$http.get('leagues/').success(function(data) {
//			$scope.leagues = data;
//		});
	});
	
	//***************  GAMES  **************************
	app.controller('SetupWeekController', function ($scope, $http, $log, $window, $rootScope, leagueService) {
		$log.debug('SetupWeekController');
		$scope.add_game_model = {};
		$scope.weeksSetup = false;
		$scope.showEdit = false;
		$scope.seasons = {};
		$scope.weeks = {};
		
		//set default spread
		$scope.add_game_model.spread = 3.5;
		
		var today = new Date(),
        dow = today.getDay(),
        toAdd = dow === 0 ? 0 : 7 - dow,
        thisSunday = new Date(),
        dateFormat = 'mm/dd/yy';
		
		thisSunday.setDate(thisSunday.getDate()+toAdd);
		$scope.add_game_model.gameStart = thisSunday;
		$scope.add_game_model.gameStart.setHours(13);
		$scope.add_game_model.gameStart.setMinutes(0);
		$scope.add_game_model.gameStart.setMilliseconds(0);
		$scope.add_game_model.gameStart.setSeconds(0);
//		$scope.add_game_model.time = new Date(1970, 0, 1, 13, 0, 0);
	    
		$scope.add_game_model.favHome = true;
		
//		leagueService.getLeagues().then(function(data) {
		$http.get('/admin/leagues/seasons/current').success(function(data) {
			$scope.seasons = data;
			
			$log.debug('SetupController:Leagues=' +JSON.stringify(data));
//			$http.get('leagues/').success(function(data) {
//			$scope.leagues = data;
			if (data[0] === undefined) {
				$window.location.href = '/admin/#/seasons';
				return;
			}	
				
			$scope.add_game_model.seasonId = data[0].id;
			
			$http.get('/admin/weeks/seasonid/'+$scope.add_game_model.seasonId).success(function(data) {
				
				$log.debug('SetupController:Weeks='+JSON.stringify(data))
				$scope.weeks = data;
				if (Object.keys(data).length > 0)
				{
					$scope.weeksSetup=true;
					$scope.add_game_model.weekId = data[0].id;
					
					leagueService.getGames($scope.add_game_model.weekId).then(function(data) {
						$log.debug('SetupController:Games='+JSON.stringify(data.data))
						$scope.games = data.data;
						
					});
				}
				else
				{
					$window.location.href = '/admin/#/create';
				}
			});
		});
	
		
		$http.get('/admin/teams/leaguetype/pickem').success(function(data) {
			$scope.teams = data;
			$scope.add_game_model.favId = data[0].id;
			$scope.add_game_model.dogId = data[0].id;
		});
		
//		$log.debug('AddGame='+JSON.stringify($scope.addgame));
		this.addGame = function(add_game_model) {

			var local_model = {};
			angular.copy(add_game_model, local_model);
			var currentDate = local_model.gameStart;
			var timezoneOffset = currentDate.getTimezoneOffset() * 60 * 1000;
			var localDate = new Date(currentDate.getTime() - timezoneOffset);
			local_model.gameStart = localDate;
			
//			local_model.gameStart = localDate.toISOString().replace('Z', '');
			
//			var date = local_model.date;
//			local_model.gameStart = date.getTime();
//			local_model.date = 
			$log.debug('AddGame='+JSON.stringify(local_model));
			
			$http({
				method : "POST",
				url : '/admin/games/',
				contentType : "application/json",
				dataType : "json",
				data : JSON.stringify(local_model)
			}).success(function(res) { 
//				$scope.local_model = {};
				//$scope.$digest();
				
				$scope.games.push(angular.copy(res));
			}).error(function(res) {
				alert('fail');
			});
	
		}
		
		this.showEditGame = function(game) {
			$log.debug('ShowEditGame: gameId='+game.id);
			$scope.edit_game_model = {};
			
			
			$http.get('/admin/games/'+game.id).success(function(data) {
				$log.debug('games/'+game.id+' = '+JSON.stringify(data));
				$scope.edit_game_model = {};
				
				var gameStart = new Date(data.gameStart*1000);
				$log.debug('gameStart='+gameStart)
				data.gameStart = {};
				
				$scope.edit_game_model.fav = {};
				$scope.edit_game_model.dog = {};
				
				$http.get('/admin/teams/'+data.favId).success(function(data) {
					$scope.edit_game_model.fav = data;
				});
				
				$http.get('/admin/teams/'+data.dogId).success(function(data) {
					$scope.edit_game_model.dog = data;
				});
				
				$scope.edit_game_model.gameStart = gameStart;
				
//				$scope.edit_game_model.fav.fullTeamName = data.favFullName;
//				$scope.edit_game_model.fav.id = data.favId;
//				$scope.edit_game_model.dog = {};
//				$scope.edit_game_model.dog.fullTeamName = data.dogFullName;
//				$scope.edit_game_model.dogId = data.dogId;
				$scope.edit_game_model.spread = data.spread;
				$scope.edit_game_model.favHome = data.favHome;
				$scope.edit_game_model.favScore = data.favScore;
				$scope.edit_game_model.dogScore = data.dogScore;
				$scope.edit_game_model.id = data.id;
//				$scope.edit_game_model.weekId = data.weekId;
//				$scope.edit_game_model.seasonId = data.seasonId;
				
				
				$scope.showEdit = true;
				
				$log.debug(JSON.stringify($scope.edit_game_model.fav));
			});
		};
		
		this.editGame = function(game) {
			
			
			game.favId = game.fav.id;
			game.dogId = game.dog.id;

			$log.debug('editGame game='+JSON.stringify(game));
			$http({
				method : "PUT",
				url : '/admin/games/',
				contentType : "application/json",
				dataType : "json",
				data : JSON.stringify(game)
			}).success(function(res) { 
//				
				
//				$scope.games.push(angular.copy(res));
				$scope.showEdit = false;
				leagueService.getGames($scope.add_game_model.weekId).then(function(data) {
					$log.debug('SetupController:editGame:Games='+JSON.stringify(data.data))
					$scope.games = data.data;
				
				});
				
			}).error(function(res) {
				alert('fail');
			});
		};
		
		this.autoScore = function(week) {
			
			var week = {};
			week.id = $scope.add_game_model.weekId;
			$log.debug('autoScore: week='+JSON.stringify(week));
			$http({
				method : "POST",
				url : '/admin/games/autoscore',
				contentType : "application/json",
				dataType : "json",
				data : JSON.stringify(week)
			}).success(function(res) { 
//				$window.location.href = 'index.html';
			}).error(function(res) {
				alert('fail');
			});
		}
		
		$scope.changeWeek = function() {
			
			//$log.debug("NavigationController:changeWeek $scope.nav.selectedWeekId="+$scope.nav.selectedWeekId);
			//makePickPageService.setWeek($scope.nav.selectedWeekId);
			$log.debug('weekChanged week='+$scope.add_game_model.weekId);
			$rootScope.$broadcast('weekChanged', $scope.add_game_model.weekId);
			
		};
		
		$scope.$on('weekChanged', function (events, args) {
			$log.debug('week='+args);
			
			leagueService.getGames(args).then(function(data) {
				$log.debug('SetupController:weekChanged='+JSON.stringify(data.data))
				$scope.games = data.data;
			
			});
			
		});
		
	
		
//		$http.get('teams/leaguetype/pickem').success(function(data) {
//			$scope.team = data;
//		});
	})
	
	
	
	
})();