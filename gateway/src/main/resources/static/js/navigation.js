(function () {
	var app = angular.module('navigation', []);

	app.controller('NavigationController', function ($scope, $rootScope, $http, $window, $log, makePickPageService) {
		
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