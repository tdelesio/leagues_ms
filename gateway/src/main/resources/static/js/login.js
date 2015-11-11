(function () {
	var app = angular.module('login', ['ngCookies']);
	
//	app.factory('XSRFInterceptor', function ($cookies, $log) {
//
//	    var XSRFInterceptor = {
//
//	      request: function(config) {
//
//	        var token = $cookies.get('XSRF-TOKEN');
//	        $log.debug('token='+token);
//	        if (token) {
//	          config.headers['X-XSRF-TOKEN'] = token;
//	          $log.info("X-XSRF-TOKEN: " + token);
//	        }
//
//	        return config;
//	      }
//	    };
//	    return XSRFInterceptor;
//	 });
	
	app.config(['$httpProvider', function($httpProvider) {
		
//		$httpProvider.interceptors.push('XSRFInterceptor');
		
//	    $httpProvider.defaults.xsrfCookieName = 'csrftoken';
//	    $httpProvider.defaults.xsrfHeaderName = 'X-CSRF-TOKEN';
//		$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
//		$httpProvider.defaults.withCredentials = true;
	    
	}]);
	
	
	
	app.controller('LoginController', function ($scope, $http, $log, $rootScope, $location, $window) {
		
		
//		var authenticate = function(credentials, callback) {
//
////			$log.debug('callback='+callback);
////			$log.debug('location.path='+$location.absUrl());
//			var headers = credentials ? {
//				authorization : "Basic "
//						+ btoa(credentials.username + ":"
//								+ credentials.password)
//			} : {};
//
//			$http.get('user', {
//				headers : headers
//			}).success(function(data) {
//				if (data.name) {
//					$rootScope.authenticated = true;
//				} else {
//					$rootScope.authenticated = false;
//				}
//				callback && callback($rootScope.authenticated);
//			}).error(function() {
//				$rootScope.authenticated = false;
//				callback && callback(false);
//			});
//
//		}
//
//		authenticate();
//
//		$scope.credentials = {};
		$http.get('/user').success(function(data) {
			$scope.user = data.name;
		}).error(function() {
			$scope.user = {};
		});
		
		$scope.login = function() {
			
			$log.debug('in loginhttp, creditials='+JSON.stringify($scope.credentials));
			$http({
//				headers : headers,
				method : "POST",
				url : '/login',
//				url : 'http://localhost:9999/uaa/login',	
				data: $.param($scope.credentials),
			    headers : {'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8'}
//				contentType : "application/json",
//				contentType: "application/x-www-form-urlencoded; charset=UTF-8",
//				dataType : "json",
//				data : JSON.stringify(local_model)
//				data : JSON.stringify($scope.credentials)
			}).success(function(res) { 
				
				
				$http.get('user', {
//					headers : headers
				}).success(function(data) {
					if (data.name) {
						$rootScope.authenticated = true;
						$log.debug('login success');
					} else {
						$rootScope.authenticated = false;
						$log.debug('login failed');
					}
//					callback && callback(auth.authenticated);
					$window.location.href = '/ui/index.html';
				}).error(function() {
					$rootScope.authenticated = false;
					$log.debug('login failed');
					
//					auth.authenticated = false;
//					callback && callback(false);
				});
				
				
			}).error(function(res) {
				$log.debug("login failed");
				
//				auth.authenticated = false;
//				callback && callback(false);
			});
			
//			authenticate($scope.credentials, function(authenticated) {
//				if (authenticated) {
//					console.log("Login succeeded")
//					$location.path("/");
//					$scope.error = false;
//					$rootScope.authenticated = true;
//				} else {
//					console.log("Login failed")
//					$location.path("/login");
//					$scope.error = true;
//					$rootScope.authenticated = false;
//				}
//			})
		};
//
		$scope.logout = function() {
			$http.post('logout', {}).success(function() {
				$rootScope.authenticated = false;
				$location.path("/");
			}).error(function(data) {
				console.log("Logout failed")
				$rootScope.authenticated = false;
			});
		}
		
		
		
		var authenticate = function(credentials, callback) {

			
			$http({
//				headers : headers,
				method : "POST",
				url : '/login',
//				contentType : "application/json",
//				dataType : "json",
//				data : JSON.stringify(local_model)
				data : credentials
			}).success(function(res) { 
				
//				$log.debug("login success");						

				$http.get('user', {
//					headers : headers
				}).success(function(data) {
					if (data.name) {
						auth.authenticated = true;
					} else {
						auth.authenticated = false;
					}
					callback && callback(auth.authenticated);
					$location.path(auth.path==auth.loginPath ? auth.homePath : auth.path);
				}).error(function() {
					auth.authenticated = false;
//					callback && callback(false);
				});
				
				
			}).error(function(res) {
				$log.debug("login failed");
				
				auth.authenticated = false;
				callback && callback(false);
			});
		}
	});
})();	