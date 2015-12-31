(function () {
	var app = angular.module('navigation', ['leagueservice']);

	
	app.controller('NavigationController', function ($scope, $rootScope, $http, $window, $log, $location, leagueService) {
		
		$scope.nav = {};
		$scope.week = {};
		$scope.league = {};
		$scope.headerIsLoaded = false;
		
		leagueService.loadHeader().then(function (data) {
			
			$log.debug('NavigationController:loadHeader:then data='+JSON.stringify(data));
			//set values in parent scope
			$rootScope.weekId = data.data.defaultWeekId;
			$rootScope.leagueId = data.data.defaultLeagueId;
			$rootScope.seasonId = data.data.defaultSeasonId;
			
			//initialize them for dropdown
			$scope.week.weekId = data.data.defaultWeekId;
			$scope.league.leagueId = data.data.defaultLeagueId;
			
			//set the dropdowns values
			$scope.nav = data.data;
			
			$rootScope.headerIsLoaded = true;
		});
				
		$scope.$on('leagueChanged', function(events, args) {
		
			$log.debug('NavigationController:on:leagueChanged: leagueId='+args);
			
			//reload the week dropdown list
			$http.get('/weeks/leagueid/'+args).success(function(result) {
					
				//get the first week in the list and set it
				var selectedWeek = result[0].id;
					
				//set the selected values
				$rootScope.weekId = selectedWeek;
				$rootScope.leagueId = args;
				
				//change the week dropdown
				$scope.nav.weeks=result;
				
				//initialize the dropdown to the right selected value
				$scope.week.weekId = selectedWeek;
					
				//let everyone know week changed	
				$rootScope.$broadcast('weekChanged', selectedWeek);
		     });
			
		});
	
	
		$scope.changeWeek = function() {
			$log.debug('NavigationController:changeWeek: scope.week.weekId='+$scope.week.weekId);
			$rootScope.$broadcast('weekChanged', $scope.week.weekId);
			
		};
		
		$scope.changeLeague = function() {
			$log.debug('NavigationController:changeLeague: league='+$scope.league.leagueId);
			$rootScope.$broadcast('leagueChanged', $scope.league.leagueId);
		};	
		
		$scope.logout = function() {
			  $http.post('/logout', {}).success(function() {
//			$http.post('/login?logout', {}).success(function() {
//			$http.post('/oauth/token/revoke', {}).success(function() {

			    $location.path("/");
			  }).error(function(data) {
			    $rootScope.authenticated = false;
			  });
			}
		
//		$scope.$watch('page', function () {
//			$log.debug(JSON.stringify($scope.page));
//			$scope.nav = $scope.page.nav;
//			$scope.week.weekId = $scope.page.nav.selectedWeekId;
//		});
		
//		$scope.$on('pageLoaded', function (event) {
//			$scope.nav = $rootScope.nav;
//			$scope.week.weekId = $rootScope.nav.selectedWeekId;
//		});
		
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