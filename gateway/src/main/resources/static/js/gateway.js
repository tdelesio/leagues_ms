angular.module('gateway', []).config(function($httpProvider) {

	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

}).controller('navigation',

function($scope, $http, $window) {

	$http.get('user').success(function(data) {
		if (data.name) {
			$window.location.href = '/ui/index.html';
		} else {
			$window.location.href = '/login.html';
		}
		
	}).error(function() {
		$window.location.href = '/login.html';
	});

});