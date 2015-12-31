(function () {
	var app = angular.module('register', []);
	
	app.controller('RegisterController', function ($scope, $http, $window, $log) {
//		$scope.usererror = {};
		
	
		$scope.register = function() {
		
				$log.debug('RegisterController:register player='+JSON.stringify($scope.player));
				$http({
					method : 'POST',
					url : '/register/auth/players/',
					contentType : "application/json",
					dataType : "json",
					data : JSON.stringify($scope.player)
				}).success(function(res) { 
	
					$window.location.href = '/index.html';
					
				}).error(function(res) {
					$scope.usererror='Could not register.';
				});
			
		};	
	});
})();