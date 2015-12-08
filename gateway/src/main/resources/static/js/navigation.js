(function () {
	var app = angular.module('navigation', []);

//	app.config(function($httpProvider) {
//		$httpProvider.interceptors.push('authInterceptor');
//	});
	
//	app.factory('authInterceptor', [
//	                                "$q", "$window", "$location", "session", function($q, $window, $location, session) {
//	                                    return {
//	                                      request: function(config) {
//	                                        config.headers = config.headers || {};
//	                                        config.headers.Authorization = 'Bearer ' + session.get('token'); // add your token from your service or whatever
//	                                        return config;
//	                                      },
//	                                      response: function(response) {
//	                                        return response || $q.when(response);
//	                                      },
//	                                      responseError: function(rejection) {
//	                                        // your error handler
//	                                      }
//	                                    };
//	                                  }
//	                                ]);
	
	app.controller('NavigationController', function ($scope, $rootScope, $http, $window, $log, $location, makePickPageService) {
		
		$scope.nav = {};
		$scope.week = {};
		
		makePickPageService.getPage().then(function(data) {
//			$log.debug('NavigationController:getPage='+JSON.stringify(data))
			$scope.nav = data.nav; 
			$scope.week.weekId = data.nav.selectedWeekId;
		});
		
		$scope.changeWeek = function() {
			
			
			
			//$log.debug("NavigationController:changeWeek $scope.nav.selectedWeekId="+$scope.nav.selectedWeekId);
			//makePickPageService.setWeek($scope.nav.selectedWeekId);
			$log.debug('weekChanged week='+$scope.week.weekId);
			$rootScope.$broadcast('weekChanged', $scope.week.weekId);
			
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