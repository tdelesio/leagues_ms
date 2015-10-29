(function () {
	var app = angular.module('app', ['ui.router']);
	
	app.config(function($stateProvider, $urlRouterProvider) {
		  //
		  // For any unmatched url, redirect to /state1
		  $urlRouterProvider.otherwise("/make");
		  //
		  // Now set up the states
		  $stateProvider
		    .state('make', {
		      url: "/make",
		      templateUrl: "makePicks.html"
		    })
		    .state('view', {
		      url: "/view",
		      templateUrl: "viewPicks.html"
		    })
		    .state('winsummary', {
		      url: "/winsummary",
		      templateUrl: "winsummary.html"
		    })
		});
	
	app.directive('settings', function() {
		return {
			restrict: 'E',
			templateUrl: 'partials/settings.html'
		};
	});
	
	app.directive('navigation', function() {
		return {
			restrict: 'E',
			templateUrl: 'partials/navigation.html'
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
	
	app.controller('NavigationController', function ($scope, $http, $window, $log, leagueService) {
		
		$scope.league = {};
		$scope.week = {};
		
		$http.get('/user').success(function(data) {
			$scope.username = data.name;
		});

		
		//get the leagues
		leagueService.getLeagues().then(function(data) {
			
			$log.debug('SettingsController:Leagues=' +JSON.stringify(data));

			//set them into the leagues values
			$scope.leagues = data;
			$scope.league.seasonId = data[0].seasonId;
			
			$http.get('/weeks/seasonid/'+$scope.league.seasonId).success(function(data) {
				
				$log.debug('SettingsController:Weeks='+JSON.stringify(data))
				$scope.weeks = data;
				$scope.week.weekId = data[0].id;
			});
		});
		
		this.settings = function getSettings() {
			/* CLOSE PANEL */
			if ($("#settings-panel").css("margin-top") == "0px") {
				$("#settings-panel").css("margin-top", "-150px");
				$(".nav").css("margin-top", "0px");
				$("#results").css("padding-top", "90px");
				$("li#nav-settings a").css("color", "#FFB415");
				/* OPEN PANEL */
			} else {
				$("#settings-panel").css("margin-top", "0px");
				$(".nav").css("margin-top", "150px");
				$("#results").css("padding-top", "240px");
				$("li#nav-settings a").css("color", "#FFFFFF");
			}
		}
	});

	app.controller('MakePicksController', function ($scope, $http, $window, $log) { 
		
		
//		var pickmap = {};
//		var points = 0;
//		var gt = ""
//		
//		_.each(picks,function(pick,i) {
//			pickmap[pick.game.id] = pick;
//		});
//
//		_.each(games,function(game, i) {
//
//			
//		var tz = moment(game.gameStart).tz("America/New_York");
//		var gsFormatted = tz.format("dddd, MMMM Do YYYY, h:mm A z");
//
//			if(gt != game.gameStartDisplay) {	%>
//				<div class="gametime"><%=gsFormatted%></div>
//			<% }
//
//			gt = game.gameStartDisplay;
//		//DETERMINE IF WIN OR NOT
//			var pick;
//			if (pickmap[game.id]) {
//				pick = pickmap[game.id];
//		//increament the pouints
//				points += pick.winValue;
//			}
		
	});
	
})();