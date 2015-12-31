(function () {
	var app = angular.module('app', ['ui.router', 'navigation', 'makepicks', 'viewpicks']);
	
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
	
	app.directive('makePicks', function() {
		return {
			restrict: 'E',
			templateUrl: 'partials/makePicks.html'
//			link : function(scope, element, attrs) {
//				
//				scope.$on('weekChanged', function (events, args) {
//					console.log('weekChanged');
//				});
//			}
		};
	});
	
	app.directive('viewPicks', function() {
		return {
			restrict: 'E',
			templateUrl: 'partials/viewPicks.html'
		};
	});
	

		
	
})();