// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
var app = angular.module('starter', ['ionic']);

app.run(function ($ionicPlatform, $ionicPopup) {
  $ionicPlatform.ready(function () {
    if (window.cordova && window.cordova.plugins.Keyboard) {
      // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
      // for form inputs)
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);

      // Don't remove this line unless you know what you are doing. It stops the viewport
      // from snapping when text inputs are focused. Ionic handles this internally for
      // a much nicer keyboard experience.
      cordova.plugins.Keyboard.disableScroll(true);
    }
    if (window.StatusBar) {
      StatusBar.styleDefault();
    }

    var showConfirm = function (title, msg) {
      var confirmPopup = $ionicPopup.confirm({
        title: title,
        template: msg
      });

      confirmPopup.then(function (res) {
        if (res) {
          console.log('You are sure');
        } else {
          console.log('You are not sure');
        }
      });
    };


    if (window.LocationPlugin) {

      // Location Listener

      // window.LocationPlugin.setLocationListener(1,
      //   function (location) {
      //     console.log(location);
      //   }, function (errMsg) {
      //     console.err(errMsg);
      //   });


      /*
       window.LocationPlugin.setStopListener(function () {
       var title = "ACHTUNG!";
       var msg = "STILL STAND WURDE ERKANNT";
       console.log(msg);
       showConfirm(title, msg);
       // A confirm dialog

       }, function (errMsg) {
       console.log(errMsg);
       })
       */

    }
    else {
      console.error("LocationPlugin is unavailable!");
    }


  });
});

app.controller('main', ['$scope', function ($scope, $ionicPopup) {

  console.log("on init main controller");

  $scope.distance = "Get Total";
  //get locationList
  /*
   window.LocationPlugin.getLocationsList(function (list) {
   console.log("on get location list : --> ");
   console.log(list);
   }, function (reason) {
   console.error(reason);
   }, {clear: true});
   */

  // window.LocationPlugin.registerGPSProviderListener(function (isLocationEnabled) {
  //   console.log("Is Location (GPS) enbaled ? --> " + isLocationEnabled);
  //   console.log(isLocationEnabled);
  // });

  $scope.startService = function () {
    window.LocationPlugin.startService(function () {
        console.log("Location service has been just started");
      }, function (reason) {
        console.error("Error - LocationPlugin: " + reason);
      },
      {
        // returnType: "json",
        minTime: 5000,
        minDistance: 0,
        minAccuracy: 50000,
        locationMaxAge: 20,
        locationRequestDelay: 5000
      }
    );
  };

  $scope.stopService = function () {
    window.LocationPlugin.stopService(function () {
      console.log("Location Service has been successfully stopped");
    }, function (err) {
      console.error(err);
    })
  };

  $scope.getLocation = function () {
    window.LocationPlugin.getLocation(function (location) {
      console.log(location);
    }, function (err) {
      console.error(err);
    })
  };

  $scope.setListener = function () {
    window.LocationPlugin.setLocationListener(function (location) {
      console.log(location);
    }, function (err) {
      console.error(err);
    })
  };

  $scope.getLocationList = function () {
    window.LocationPlugin.getLocationsList(function (list) {
      console.log("on get location list : --> ");
      console.log(list);
    }, function (reason) {
      console.error(reason);
    }, {reset: false}); //, {clear: true}
  };

  $scope.clearLocationList = function () {
    window.LocationPlugin.clearLocationsList(function (msg) {
      console.log("on clearing location list : --> ");
      console.log(msg);
    }, function (reason) {
      console.error(reason);
    }); //, {clear: true}
  };

  $scope.enableMapping = function () {
    window.LocationPlugin.enableMapping(function () {
      console.log("oenable Mapping");
    }, function (error) {
      console.error("on runTotalDistanceCalculator error:", error);
    })
  };

  $scope.setStopID = function (id) {
    window.LocationPlugin.setStopID(function () {
        console.log("set stop id --> " + id);
      }, function () {
      },
      {stopID: id}
    );
  };

  $scope.getStopID = function (id) {
    window.LocationPlugin.getStopDistance(function (result) {
        console.log("id = " + id + " --> " + result + " m");
      }, function (error) {
        console.error(error);
      },
      {stopID: id, clear: false}
    );
  };


  $scope.getKeySet = function () {
    window.LoctionPlugin.getKeySet(function (result) {
      console.log("KEY SET : " + result);
    }, function (error) {
      console.error("Error: " + error);
    })
  };

  $scope.getLastStopID = function () {
    window.LocationPlugin.getLastStopID(function (result) {
      console.log("LAST STOP ID =  " + result);
    }, function (error) {
      console.error("Error: " + error);
    })
  };

  $scope.getTotalNET = function () {
    window.LocationPlugin.getTotalDistance(function (distance) {
      console.log("total distance NET = " + distance);
      $scope.distance = distance;
    }, function (error) {
      console.error("Error: " + error);
    }, {reset: false, clean: true});
  };

  $scope.getTotal = function () {
    window.LocationPlugin.getTotalDistance(function (distance) {
      console.log("total distance = " + distance);
      $scope.distance = distance;
    }, function (error) {
      console.error("Error: " + error);
    }, {reset: true});
  };

  $scope.takePhoto = function () {
    window.CameraPlugin.takePhoto(function (base64) {
      console.log("taking photo success --> " + base64);
    }, function (error) {
      console.error(error);
    }, true);
  };
}]);
