var cms = angular.module('cms', [])

cms.controller('CMSController', ['$scope','$http', function($scope, $http) {

  $http.get("rest").then(function(response){
    angular.extend($scope, response.data)
  })

}]);