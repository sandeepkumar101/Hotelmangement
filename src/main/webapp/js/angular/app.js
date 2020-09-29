'use strict';

// create the module and name it trackingApp
        // also include ngRoute for all our routing needs
    var trackingApp = angular.module('trackingApp', ['ngRoute',
                                                     'ngCookies',
                                                     'ngResource',
                                                     'ngSanitize']);
    
    trackingApp.service('sharedProperties', function() {
        var apiKey = "";
        var apiSecret = "";
        var encodedAuth = "";
        var oauthToken = "";
        var userName = "";

        return {
            getApiKey: function() {
                return apiKey;
            },
            setApiKey: function(value) {
                apiKey = value;
            },
            getApiSecret: function() {
                return apiSecret;
            },
            setApiSecret: function(value) {
                apiSecret = value;
            },
            getEncodedAuth: function() {
                return encodedAuth;
            },
            setEncodedAuth: function(value) {
                encodedAuth = value;
            },
            getOauthToken: function() {
                return oauthToken;
            },
            setOauthToken: function(value) {
                oauthToken = value;
            },
            getUsername: function() {
                return userName;
            },
            setUsername: function(value) {
                userName = value;
            }
        };
    });
    
    
    trackingApp.controller('loginController', ["$scope", "$window", "$http", 'sharedProperties', function($scope, $window, $http, sharedProperties) {

        $scope.submitFunction = function() {

            var username = $scope.username;
            var password = $scope.password;

            //Try to log in to account
            $http({method: "POST", url: '/HotelManagement /user/login',
                data: {'username': username, 'password': password}
            }).success(function(data, status, headers, config) {
                    $window.location.href = "/HotelManagement /welcome.html";

                }).
                error(function(data, status, headers, config) {
                    $window.alert("Wrong username/password. Please try again");
                });
        }
    }]);

    
    var dashboardApp = angular.module('dashboardApp', ['ngRoute',
                                                     'ngCookies',
                                                     'ngResource',
                                                     'ngSanitize']);
    
    
 // configure our routes
    dashboardApp.config(function($routeProvider, $locationProvider, $httpProvider) {
       	
        $routeProvider
            // route for the home page
            .when('/', {
                templateUrl : 'pages/home.html',
                controller  : 'mainController'
            })

            // route for the about page
            .when('/map', {
                templateUrl : 'pages/map.html',
                controller  : 'mapController'
            })
        
            $locationProvider.html5Mode({
            	  enabled: true,
            	  requireBase: false
            	});
    });
    
    
    