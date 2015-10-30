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
	
	app.directive('games', function() {
		return {
			restrict: 'E',
			templateUrl: 'partials/games.html'
		};
	});
	
	app.directive('editGame', function() {
		return {
			restrict: 'E',
			templateUrl: 'partials/editGame.html'
		};
	});
	
	
	app.factory('leagueService', function ($http, $log) {
	$log.debug('leagueService');
		var service =  {
			getLeagues: function() {
				return $http.get('/leagues/').then(function(result) {
			           return result.data;
			       });
			},
		
			getGames: function(seasonId) {
			return $http.get('/games/weekid/'+seasonId).success(function(result) {
		           return result.data;
		       });
			}
		}
		return service;
	});
	
	app.controller('ChromeController', function ($http, $scope) {
		$http.get('/user').success(function(data) {
			$scope.user = data.name;
		});
	});
	
	app.controller('SetupWeekController', function ($scope, $http, $log, leagueService) {
		$log.debug('SetupWeekController');
		$scope.add_game_model = {};
		$scope.weeksSetup = false;
		$scope.showEdit = false;
		
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
		
		leagueService.getLeagues().then(function(data) {
			
			$log.debug('SetupController:Leagues=' +JSON.stringify(data));
//			$http.get('leagues/').success(function(data) {
			$scope.leagues = data;
				
				$scope.add_game_model.seasonId = data[0].seasonId;
				
				$http.get('/weeks/seasonid/'+$scope.add_game_model.seasonId).success(function(data) {
				
				
					$log.debug('SetupController:Weeks='+JSON.stringify(data))
					$scope.weeks = data;
					if (Object.keys(data).length > 0)
					{
						$scope.weeksSetup=true;
						$scope.add_game_model.weekId = data[0].id;
					
//						$http.get('games/weekid/'+$scope.add_game_model.weekId).success(function(data) {
						leagueService.getGames($scope.add_game_model.weekId).then(function(data) {
							$log.debug('SetupController:Games='+JSON.stringify(data.data))
							$scope.games = data.data;
						
						});
					}
				});
		});
	
		
		$http.get('/teams/leaguetype/pickem').success(function(data) {
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
				url : '/games/',
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
			
			
			
			$http.get('/games/'+game.id).success(function(data) {
				$log.debug('games/'+game.id+' = '+JSON.stringify(data));
				$scope.edit_game_model = {};
				
				var gameStart = new Date(data.gameStart*1000);
				$log.debug('gameStart='+gameStart)
				data.gameStart = {};
				
				
				$scope.edit_game_model.gameStart = gameStart;
				$scope.edit_game_model.favId = data.favId;
				$scope.edit_game_model.dogId = data.dogId;
				$scope.edit_game_model.spread = data.spread;
				$scope.edit_game_model.favHome = data.favHome;
				$scope.edit_game_model.favScore = data.favScore;
				$scope.edit_game_model.dogScore = data.dogScore;
				$scope.edit_game_model.id = data.id;
//				$scope.edit_game_model.weekId = data.weekId;
//				$scope.edit_game_model.seasonId = data.seasonId;
				
				$scope.showEdit = true;
			});
		};
		
		this.editGame = function(game) {
						
			$http({
				method : "PUT",
				url : '/games/',
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
		
	
		
//		$http.get('teams/leaguetype/pickem').success(function(data) {
//			$scope.team = data;
//		});
	})
	
	app.controller('CreateWeekController', function ($scope, $http, $window, $log, leagueService) {
		$scope.week = {};
		
		leagueService.getLeagues().then(function(data) {
			$scope.leagues = data;
			$scope.week.seasonId = data[0].seasonId;
		});
		
		this.autoWeek = function(week) {
			$log.debug('autoWeek: week='+JSON.stringify(week));
			$http({
				method : "POST",
//				beforeSend: function (request) {
//			        request.setRequestHeader(header, token);
//			     },
				url : '/games/autosetup',
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
				url : '/weeks/',
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
	
})();