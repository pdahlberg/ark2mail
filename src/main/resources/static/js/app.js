var app = angular.module('app', []);

app.config(function($logProvider) {
  $logProvider.debugEnabled(true);
});

app.controller('postcontroller', function($scope, $http, $location, $log) {
	$log.info("Starting 2");
	$scope.submitForm = function() {
		var url = $location.absUrl() + "api/deposit";
        $log.info("url: " + url);

		var config = {
                headers : {
                    'Accept': 'text/plain'
                }
        }
		var data = {
            sender: $scope.sender,
            receiver: $scope.receiverEmail,
            amount: 1.0 // parseFloat($scope.amount)
        };
        $log.info("post data: ", data);

		$http.post(url, data, config).then(function (response) {
			$scope.postResultMessage = response.data;
            $log.info("post response: ", response.data);
        }, function error(response) {
			$scope.postResultMessage = "Error with status: " +  response.statusText;
            $log.info("error response: ", response);
        });

		//$scope.sender = "";
		$scope.receiverEmail = "";
		$scope.amount = "";
	}
});

app.controller('getcontroller', function($scope, $http, $location) {
	$scope.getfunction = function(){
		var url = $location.absUrl() + "getallcustomer";

		$http.get(url).then(function (response) {
			$scope.response = response.data
		}, function error(response) {
			$scope.postResultMessage = "Error with status: " +  response.statusText;
		});
	}
});