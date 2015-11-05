(function () {
	var app = angular.module('navigation', []);

	app.controller('NavigationController', function ($scope, $rootScope, $http, $window, $log, leagueService) {
		
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
			
				$rootScope.$broadcast('weekLoaded');
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

})();