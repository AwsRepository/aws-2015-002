angular.module('myServiceModule', ['ngResource']).factory('Serie', function($resource) {
	return $resource('http://aws-002.appspot.com/series/:id?user=goldUser');

}).controller('MyController', ['$scope', 'Serie',
function($scope, Serie) {

	//GET ALL
	//Vista de TODOS los elementos
	var sT = Serie.query(function() {
		// Do something with the plain result
	});

	$scope.series = sT;

	$scope.updated = function() {

		var sT = Serie.query(function() {
			// Do something with the plain result
		});

		$scope.series = sT;
	};

	//DELETE
	$scope.clickDelete = function() {

		$scope.entry = Serie.get({
			id : $scope.inDelete
		}, function() {
			
		});
		
		$scope.entry.$delete(function() {
				//gone forever!
				alert('Bye bye!');
			});

	};

	//GET
	$scope.clickGet = function() {

		$scope.entry = Serie.get({
			id : $scope.inGet
		}, function() {

		});
		$scope.salidaGet = $scope.entry;

	};

	//POST
	$scope.clickPost = function() {

		$scope.entry = new Serie();
		//You can instantiate resource class

		$scope.entry.title = $scope.fTitle;
		$scope.entry.creator = $scope.fCreator;
		$scope.entry.seasons = $scope.fSeasons;
		$scope.entry.episodes = $scope.fEpisodes;
		$scope.entry.year = $scope.fYear;

		Serie.save($scope.entry, function() {
			alert('Creado!');

		});

	};

}]);
