'use strict';

var dashboardApp = angular.module('dashboardApp', ['ngRoute',
                                                     'ngCookies',
                                                     'ngResource',
                                                     'ngSanitize', 'leaflet-directive', 'timepickerPop']);
    
    
 // configure our routes
dashboardApp.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
        when('/', {
            templateUrl: 'home.html',
            controller: 'DashboardController'
        }).
          when('/welcome', {
            templateUrl: 'home.html',
            controller: 'HomeController'
        }).
          when('/home.html', {
            templateUrl: 'home.html',
            controller: 'HomeController'
        }).
          when('/welcome.html', {
            templateUrl: 'home.html',
            controller: 'DashboardController'
        }).
        when('/editProfile.html', {
          templateUrl: 'editProfile.html',
          controller: 'EditProfileController'
        }).
        when('/compliantMessage.html', {
                  templateUrl: 'compliantMessage.html',
                  controller: 'compliantMessageController'
                }).
        when('/settings.html', {
                  templateUrl: 'settings.html',
                  controller: 'SettingsController'
                }).
        when('/viewAllAlerts.html', {
                  templateUrl: 'viewAllAlerts.html',
                  controller: 'ViewAllAlertsController'
                }).
        otherwise({
          redirectTo: '/errorPage.html'
        });
}]);

dashboardApp.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.interceptors.push(function ($q, $rootScope, $log) {
        return {
            'request': function (config) {
            	var time = javaRest.get_iso_date();
            	var nonce = makeRandomString();
                config.headers = {
    	        	'Authorization' : getAuthToken(config.url, time, nonce),
    	            'x-authorization-date' : time,
    	            'nonce' : nonce
    	            }
                return config;
            }
        }
    });
    //$httpProvider.defaults.headers.post['Content-Type'] = undefined;
}]);

function makeRandomString() {
    return Math.random().toString(36).substring(2, 15) +
        Math.random().toString(36).substring(2, 15);
}

function getAuthToken(Url, time, nonce)
{
	
	//var string_to_hash = javaRest.cookie.get('token') + ':' + Url+javaRest.cookie.get('userId') + ',GET,' + time + "," + nonce;
	var string_to_hash = javaRest.cookie.get('token') + ':' + Url + ',GET,' + time + "," + nonce;
	var authorization = javaRest.cookie.get('userId') + ':' + javaRest.hash(string_to_hash);	
	return authorization;
}

function getPostAuthToken(Url, time, nonce)
{
	
	//var string_to_hash = javaRest.cookie.get('token') + ':' + Url+javaRest.cookie.get('userId') + ',POST,' + time + "," + nonce;
	var string_to_hash = javaRest.cookie.get('token') + ':' + Url+javaRest.cookie.get('userId') + ',POST,' + time + "," + nonce;
	var authorization = javaRest.cookie.get('userId') + ':' + javaRest.hash(string_to_hash);	
	return authorization;
}

function getPutAuthToken(Url, time, nonce)
{
	
	//var string_to_hash = javaRest.cookie.get('token') + ':' + Url+javaRest.cookie.get('userId') + ',POST,' + time + "," + nonce;
	var string_to_hash = javaRest.cookie.get('token') + ':' + Url+javaRest.cookie.get('userId') + ',PUT,' + time + "," + nonce;
	var authorization = javaRest.cookie.get('userId') + ':' + javaRest.hash(string_to_hash);	
	return authorization;
}

/*
myServices.factory('Auth', ['$resource',
            function($resource){
              return {
                Login: $resource(serviceURL + 'login', {}, { go: { method:'POST', isArray: false }}),
                Logout: $resource(serviceURL + 'logout', {}, { go: { method:'POST', isArray: false }}),
                Register: $resource(serviceURL + 'register', {}, { go: { method:'POST', isArray: false }}),
              };
            }
]);
//Auth.Login.go({ username: $scope.username, password: $scope.password })//from controller

 */

dashboardApp.controller('UserController', function($scope, $rootScope, $resource) {
	if($(".wrapper").hasClass('active'))
	{
		$("[data-toggle='offcanvas']").click();
	}
	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();
	$scope.user = $resource('user/:userId',{userId: javaRest.cookie.get('userId')}, {
	    get: {
	        method: 'GET',
	        isArray: false,
	        headers: {
	        	'Authorization' : getAuthToken("user/", time, nonce),
	            'x-authorization-date' : time,
	            'nonce' : nonce}
	    	}
		}).get();
	
	$scope.user.$promise.then(function(user) {	
            $rootScope.homeUserName = $scope.user.firstName+' '+$scope.user.lastName;
            $rootScope.userHomefirstName = $scope.user.firstName;
            
            $rootScope.userHomeImage = "img/avatar5.png";
            if($scope.user.userImage!=undefined && $scope.user.userImage!=null && $scope.user.userImage!='null' && $scope.user.userImage!='')
            {
                $rootScope.userHomeImage = "uploads/userImages/"+$scope.user.userImage;
            }
            
	});
        
        $rootScope.gotoURL = function ( path ) {
	  $location.path( path );
	};
	
});

dashboardApp.controller('DashboardController', function($scope, $rootScope, $resource, $compile, $http) {
	if($(".wrapper").hasClass('active'))
	{
		$("[data-toggle='offcanvas']").click();
	}
	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();
		$scope.dashboardLinks = $resource('user/getUserDashboard/:userId',{userId: javaRest.cookie.get('userId')}, {
		    get: {
		        method: 'GET',
		        isArray: false,
		        headers: {
		        	'Authorization' : getAuthToken("user/getUserDashboard/", time, nonce),
		            'x-authorization-date' : time,
		            'nonce' : nonce}
		    	}
			}).get();
		
		$scope.dashboardLinks.$promise.then(function(dashboardLinks) {
		$rootScope.dashboardLinks = $scope.dashboardLinks;
		
		$rootScope.getClass = function getClass(idx) {
	    	if($rootScope.dashboardLinks.linksNameList[idx]=='Room Details') {
				return "treeview";
			}
			else {
				return "";
			}
		};
		
		$rootScope.getLink = function getLink(idx) {
	    	if($rootScope.dashboardLinks.linksNameList[idx]=='Room Details') {
				//return "#student.html";
                                return $rootScope.dashboardLinks.linksList[idx];
			}
			else {
				return $rootScope.dashboardLinks.linksList[idx];
			}
		};
		
		$rootScope.openMenu = function openMenu() {
			//$(".treeview").tree();
			//$(".sidebar .treeview").tree();
		};
		
		$rootScope.logout = function logout() {
                        javaRest.cookie.remove('token');
                        javaRest.cookie.remove('userId');
                        javaRest.cookie.remove('email');
                        store.clear();
                        window.location = 'index.html';
		};
	});
		
            var req = {
                    url: 'user/userHome/'+javaRest.cookie.get('userId'),
                    method: 'GET',
                    headers: {
                            'Authorization' : getAuthToken("user/userHome/", time, nonce),
                            'x-authorization-date' : time,
                            'nonce' : nonce},
                            data:{}
            };
            $http(req).then(function(response){
                    var linkingFunction = $compile(response.data);
                    var elem = linkingFunction($scope);
                    //alert(response.data);
                    //$rootScope.userSidebarLinks = elem;

                    $(".sidebar").append(elem);
                    $(".treeview").tree();
            }, 
            function(){
                    //alert("Error::");
            });
		
});

dashboardApp.controller('HomeController', function($scope, $rootScope, $resource, $compile, $http) {
	if($(".wrapper").hasClass('active'))
	{
		$("[data-toggle='offcanvas']").click();
	}
	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();
		$scope.dashboardLinks = $resource('user/getUserDashboard/:userId',{userId: javaRest.cookie.get('userId')}, {
		    get: {
		        method: 'GET',
		        isArray: false,
		        headers: {
		        	'Authorization' : getAuthToken("user/getUserDashboard/", time, nonce),
		            'x-authorization-date' : time,
		            'nonce' : nonce}
		    	}
			}).get();
		
		$scope.dashboardLinks.$promise.then(function(dashboardLinks) {
		$rootScope.dashboardLinks = $scope.dashboardLinks;
		
		$rootScope.getClass = function getClass(idx) {
	    	if($rootScope.dashboardLinks.linksNameList[idx]=='Room Details') {
				return "treeview";
			}
			else {
				return "";
			}
		};
		
		$rootScope.getLink = function getLink(idx) {
	    	if($rootScope.dashboardLinks.linksNameList[idx]=='Room Details') {
				//return "#student.html";
                                return $rootScope.dashboardLinks.linksList[idx];
			}
			else {
				return $rootScope.dashboardLinks.linksList[idx];
			}
		};
		
		$rootScope.openMenu = function openMenu() {
			//$(".treeview").tree();
			//$(".sidebar .treeview").tree();
		};
		
		$rootScope.logout = function logout() {
                          javaRest.cookie.remove('token')
			  javaRest.cookie.remove('userId')
			  javaRest.cookie.remove('email')
			  store.clear()
			  window.location = 'index.html'
		};
	});
});

dashboardApp.controller('AlertsController', function($scope, $rootScope, $resource, $interval) {
	if($(".wrapper").hasClass('active'))
	{
		$("[data-toggle='offcanvas']").click();
	}
	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();
	
	var refreshAlerts = function(){
		$scope.alerts = $resource('alerts/:userId',{userId: javaRest.cookie.get('userId')}, {
		    get: {
		        method: 'GET',
		        isArray: false,
		        headers: {
                            'Authorization' : getAuthToken("alerts/", time, nonce),
		            'x-authorization-date' : time,
		            'nonce' : nonce}
		    	}
			}).get();
		
		$scope.alerts.$promise.then(function(alerts) {
                    
                    //alert($scope.alerts.totalElements);
                    //alert( $scope.alerts.content);

                    $rootScope.alerts = $scope.alerts;
	    	
		});
	}
	
	refreshAlerts();
	$interval(refreshAlerts, 60000);
	
});

dashboardApp.controller('ViewAllAlertsController', function($scope, $rootScope, $resource, $interval) {
	if($(".wrapper").hasClass('active'))
	{
		$("[data-toggle='offcanvas']").click();
	}
	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();
	
	
        $scope.alerts = $resource('alerts/getAllAlerts/:userId',{userId: javaRest.cookie.get('userId')}, {
            get: {
                method: 'GET',
                isArray: true,
                headers: {
                    'Authorization' : getAuthToken("alerts/getAllAlerts/", time, nonce),
                    'x-authorization-date' : time,
                    'nonce' : nonce}
                }
                }).get();

        $scope.alerts.$promise.then(function(alerts) {

            //alert($scope.alerts.totalElements);
            //alert( $scope.alerts.content);

            //$rootScope.alerts = $scope.alerts;

        });
	
	
	
	
});
    
dashboardApp.controller('EditProfileController', function($scope, $rootScope, $resource, $timeout) {
	if($(".wrapper").hasClass('active'))
	{
		$("[data-toggle='offcanvas']").click();
	}
	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();
	$scope.userResource = $resource('user/:userId',{userId: javaRest.cookie.get('userId')}, {
	    get: {
	        method: 'GET',
	        isArray: false,
	        headers: {
	        	'Authorization' : getAuthToken("user/", time, nonce),
	            'x-authorization-date' : time,
	            'nonce' : nonce}
	    	
		}
	   });
	$scope.user = $scope.userResource.get();
	$scope.user.$promise.then(function(user) {
	    	$scope.firstName = $scope.user.firstName;
	    	$scope.lastName = $scope.user.lastName;
	    	$scope.mobile = $scope.user.mobile;
	    	$scope.emailAddress = $scope.user.emailAddress;
	    	$scope.houseNo = $scope.user.houseNo;
	    	$scope.address = $scope.user.address;
	    	$scope.city = $scope.user.city;
	    	$scope.state = $scope.user.state;
	    	$scope.pinCode = $scope.user.pinCode;
	    	//$scope.userPhoto = '';
	});
	
        $scope.saveProfile = function saveProfile() {
        $scope.updateUserResource = $resource('user/saveUserDetails/:userId',{userId: javaRest.cookie.get('userId')}, {
        save: {
                method: 'POST',
                isArray: false,
                headers: {
                        'Authorization' : getPostAuthToken("user/saveUserDetails/", time, nonce),
                    'x-authorization-date' : time,
                    'nonce' : nonce,
                    'Content-Type' : undefined
                    },
                transformRequest: function(data, headersGetter) {
                    var headers = headersGetter();
                    var time = javaRest.get_iso_date();
                    var nonce = makeRandomString();
                    headers['Content-Type'] = undefined;
                    headers['Authorization'] = getPostAuthToken("user/saveUserDetails/", time, nonce);
                    headers['x-authorization-date'] = time;
                    headers['nonce'] = nonce;
                    var formFields = angular.toJson(data, true);
                    //alert(angular.toJson(data, true));
                    /*
                        if (data === undefined)
                          return data;

                        var fd = new FormData();
                        fd.append("userPhoto", document.getElementsByName("userPhoto")[0]);
                        angular.forEach(data, function(value, key) {
                           if (value instanceof FileList) {
                                if (value.length == 1) {
                                    fd.append(key, value[0]);
                                } else {
                                    angular.forEach(value, function(file, index) {
                                        fd.append(key + '_' + index, file);
                                    });
                                }
                           } else {
                               fd.append(key, value);
                           }
                        });
                    */

                    var form = $('form')[0];
                    var fd = new FormData(form);
                    fd.append("userDetails", formFields);
                    return fd;
              }
            }	
        });
		
        $scope.user.firstName = $scope.firstName;
    	$scope.user.lastName = $scope.lastName;
    	$scope.user.mobile = $scope.mobile;
    	$scope.user.emailAddress = $scope.emailAddress;
    	$scope.user.houseNo = $scope.houseNo;
    	$scope.user.address = $scope.address;
    	$scope.user.city = $scope.city;
    	$scope.user.state = $scope.state;
    	$scope.user.pinCode = $scope.pinCode;
		
        $scope.updateUserResource.save({ userId:javaRest.cookie.get('userId') }, $scope.user, 
                function(resp, headers){
                    //success callback
                    //console.log(resp);
                    $scope.successTextAlert = "Saved";
                    $scope.showSuccessAlert = true;

                    // switch flag
                    $scope.switchBool = function (value) {
                        $scope[value] = !$scope[value];
                    };
                    $rootScope.homeUserName = resp.firstName+' '+ resp.lastName;
                    $rootScope.userHomefirstName =  resp.firstName;
                    //$scope.userImage = "img/avatar5.png";
                    if(resp.userImage!=undefined && resp.userImage!=null && resp.userImage!='null' && resp.userImage!='')
                    {
                        $rootScope.userImage = "uploads/userImages/"+resp.userImage;
                    }
                    
                    $timeout(function() {
                        $scope.showSuccessAlert = false;
                    }, 5000);
              },
              function(err){
                // error callback
                    //console.log(err);
                    $scope.successTextAlert = "Error";
                    $scope.showSuccessAlert = true;

                    // switch flag
                    $scope.switchBool = function (value) {
                        $scope[value] = !$scope[value];
                    };
              });
    };
     
});

dashboardApp.controller('SettingsController', function($scope, $rootScope, $resource, filterFilter, $timeout) {
	if($(".wrapper").hasClass('active'))
	{
		$("[data-toggle='offcanvas']").click();
	}
	var time = javaRest.get_iso_date();
	var nonce = makeRandomString();
        
        //$scope.time1 = new Date();
	//$scope.time2 = new Date();
	
        //$scope.time2.setHours(7, 30);
        $scope.enableNotification = true;
        
        $scope.selectPickTimeStart = new Date();
        $scope.selectPickTimeEnd = new Date();
        
        $scope.selectDropTimeStart = new Date();
        $scope.selectDropTimeEnd = new Date();
        
        $scope.daysSelected = [{ name: 'Mon', selected: false },
                                { name: 'Tue', selected: false },
                                { name: 'Wed', selected: false },
                                { name: 'Thu', selected: false },
                                { name: 'Fri', selected: false },
                                { name: 'Sat', selected: false },
                                { name: 'Sun', selected: false }
                               ];
                                               
            // selected Days
            $scope.daysSelection = [];

            // helper method to get selected Days
            $scope.selectedDays = function selectedDays() {
              return filterFilter($scope.daysSelected, { selected: true });
            };

            // watch Days for changes
            $scope.$watch('daysSelected|filter:{selected:true}', function (nv) {
              $scope.daysSelection = nv.map(function (day) {
                return day.name;
              });
            }, true);
            

        $scope.typesSelected = [{ name: 'Arrival', selected: false },
                                { name: 'Departure', selected: false },
                                { name: 'Breakdown', selected: false },
                                { name: 'Long Halt', selected: false },
                                { name: 'Emergency', selected: false }
                               ];
                                               
        // selected Types
        $scope.typesSelection = [];

        // helper method to get selected Types
        $scope.selectedTypes = function selectedTypes() {
          return filterFilter($scope.typesSelected, { selected: true });
        };

        // watch Types for changes
        $scope.$watch('typesSelected|filter:{selected:true}', function (nv) {
          $scope.typesSelection = nv.map(function (type) {
            return type.name;
          });
        }, true);
        
        
        
	$scope.showMeridian = true;	
	$scope.disabled = false;
        
        $scope.showPickTimeModal = false;
        $scope.showDropTimeModal = false;
        $scope.showDaysModal = false;
        $scope.showTypeModal = false;
        
        /*
            $scope.pickTimePopUp = function(){
                $scope.showPickTimeModal = !$scope.showPickTimeModal;
            };

            $scope.dropTimePopUp = function(){
                $scope.showDropTimeModal = !$scope.showDropTimeModal;
            };

            $scope.daysPopUp = function(){
                $scope.showDaysModal = !$scope.showDaysModal;
            };

            $scope.typesPopUp = function(){
                $scope.showTypeModal = !$scope.showTypeModal;
            };
        */
        
	$scope.notificationResource = $resource('user/getUserNotificationSettings/:userId',{userId: javaRest.cookie.get('userId')}, {
	    get: {
	        method: 'GET',
	        isArray: false,
	        headers: {
	        	'Authorization' : getAuthToken("user/getUserNotificationSettings/", time, nonce),
	            'x-authorization-date' : time,
	            'nonce' : nonce}
	    	
		}
	   });
	$scope.userNotificationSettings = $scope.notificationResource.get();
	$scope.userNotificationSettings.$promise.then(function(userNotificationSettings) {
	    	
                var pStartDate = $filter('date')(new Date("January 01, 2000 "+userNotificationSettings.pickStartTime), 'hh:mm a', '');
                var pEndDate = $filter('date')(new Date("January 01, 2000 "+userNotificationSettings.pickEndTime), 'hh:mm a', '');
                var dStartDate = $filter('date')(new Date("January 01, 2000 "+userNotificationSettings.dropStartTime), 'hh:mm a', '');
                var dEndDate = $filter('date')(new Date("January 01, 2000 "+userNotificationSettings.dropEndTime), 'hh:mm a', '');
                
                $scope.selectPickTimeStart = new Date("January 01, 2000 "+userNotificationSettings.pickStartTime);
                $scope.selectPickTimeEnd = new Date("January 01, 2000 "+userNotificationSettings.pickEndTime);
                $scope.selectDropTimeStart = new Date("January 01, 2000 "+userNotificationSettings.dropStartTime);
                $scope.selectDropTimeEnd = new Date("January 01, 2000 "+userNotificationSettings.dropEndTime);

                $scope.pickTime= pStartDate+" - "+pEndDate;
                $scope.dropTime= dStartDate+" - "+dEndDate;
                
                $scope.daysSelection= userNotificationSettings.activeDays;
                $scope.typesSelection= userNotificationSettings.alertTypes;
                
                for(var l=0; l<$scope.daysSelected.length; l++) {
                    if(userNotificationSettings.activeDays.indexOf($scope.daysSelected[l].name)>=0) {
                            $scope.daysSelected[l].selected = true;
                    }
                }
                for(var l=0; l<$scope.typesSelected.length; l++) {
                    if(userNotificationSettings.alertTypes.indexOf($scope.typesSelected[l].name)>=0) {
                            $scope.typesSelected[l].selected = true;
                    }
                }
	});
        
        
        
        
        $scope.userNotificationResource = $resource('user/updateNotificationSettings/:userId',{userId: javaRest.cookie.get('userId')}, {
		save: {
                        method: 'PUT',
		        isArray: false,
		        headers: {
                            'Authorization' : getPutAuthToken("user/updateNotificationSettings/", time, nonce),
		            'x-authorization-date' : time,
		            'nonce' : nonce,
		            'Content-Type' : 'application/json'
		           },				
                        transformRequest: function(data, headersGetter) {
                                var headers = headersGetter();
                                var time = javaRest.get_iso_date();
                                var nonce = makeRandomString();
                                headers['Content-Type'] = "application/json";
                                headers['Authorization'] = getPutAuthToken("user/updateNotificationSettings/", time, nonce);
                                headers['x-authorization-date'] = time;
                                headers['nonce'] = nonce;
                                /*
                                var alertConfigData = '{"studentId":"'+data.studentId+'", "presentFlag":"'+data.presentFlag+ '"}';
                                return alertConfigData;
                                */
                               return JSON.stringify(data);
                          }
		    }
        });
        
        $scope.updateUserNotification = function updateUserNotification(alertConfigType) {
            
                $scope.alertConfigData = {};
                $scope.alertConfigData.pickStartTime = this.selectPickTimeStart;
                $scope.alertConfigData.pickEndTime = this.selectPickTimeEnd;
                
                $scope.alertConfigData.dropStartTime = this.selectDropTimeStart;
                $scope.alertConfigData.dropEndTime = this.selectDropTimeEnd;
                
                $scope.alertConfigData.activeDays = this.daysSelection;
                $scope.alertConfigData.alertTypes = this.typesSelection;
                
                $scope.alertConfigData.alertUpdateType = alertConfigType;
                
                if($scope.enableNotification)
                {
                    $scope.alertConfigData.activeTime=1;
                }
                else
                {
                    $scope.alertConfigData.activeTime=0;
                }
                
                $scope.userNotificationResource.save({ userId:javaRest.cookie.get('userId') }, $scope.alertConfigData, 
                        function(resp, headers){
                            //success callback
                            //console.log(resp);
                            $scope.successTextAlert = "Saved";
                            $scope.showSuccessAlert = true;
                            
                            // switch flag
                            $scope.switchBool = function (value) {
                                $scope[value] = !$scope[value];
                            };
                            
                            if(alertConfigType=='pickTime') {
                                var pStartDate = $filter('date')($scope.selectPickTimeStart, 'hh:mm a', '');
                                var pEndDate = $filter('date')($scope.selectPickTimeEnd, 'hh:mm a', '');
                                $scope.pickTime= pStartDate+" - "+pEndDate;
                                $('#pickTimePopUp').modal('hide');
                                //$('#pickTimePopUp').hide();
                                //$('.modal-backdrop').hide();
                            } else if(alertConfigType=='dropTime') {
                                var dStartDate = $filter('date')($scope.selectDropTimeStart, 'hh:mm a', '');
                                var dEndDate = $filter('date')($scope.selectDropTimeEnd, 'hh:mm a', '');
                                $scope.dropTime= dStartDate+" - "+dEndDate;
                                $('#dropTimePopUp').modal('hide');
                                //$('#dropTimePopUp').hide();
                                //$('.modal-backdrop').hide();
                            } else if(alertConfigType=='alertDays') {
                                $('#daysPopUp').modal('hide');
                                //$('#daysPopUp').hide();
                                //$('.modal-backdrop').hide();
                            } else if(alertConfigType=='alertTypesSel') {
                                $('#typesPopUp').modal('hide');
                                //$('#typesPopUp').hide();
                                //$('.modal-backdrop').hide();
                            }
                            
                            $timeout(function() {
                                $scope.showSuccessAlert = false;
                            }, 5000);
                      },
                      function(err){
                        // error callback
                            //console.log(err);
                            $scope.successTextAlert = "Error";
                            $scope.showSuccessAlert = true;

                            // switch flag
                            $scope.switchBool = function (value) {
                                $scope[value] = !$scope[value];
                            };
                      });
          }
        
        
});


dashboardApp.directive('modal', function () {
    return {
      template: '<div class="modal fade">' + 
          '<div class="modal-dialog">' + 
            '<div class="modal-content">' + 
              '<div class="modal-header">' + 
                '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>' + 
                '<h4 class="modal-title">{{ title }}</h4>' + 
              '</div>' + 
              '<div class="modal-body" ng-transclude></div>' + 
            '</div>' + 
          '</div>' + 
        '</div>',
      restrict: 'E',
      transclude: true,
      replace:true,
      scope:true,
      link: function postLink(scope, element, attrs) {
        scope.title = attrs.title;
        
        scope.$watch(attrs.visible, function(value){
          if(value == true)
            $(element).modal('show');
          else
            $(element).modal('hide');
        });

        $(element).on('shown.bs.modal', function(){
          scope.$apply(function(){
            scope.$parent[attrs.visible] = true;
          });
        });

        $(element).on('hidden.bs.modal', function(){
          scope.$apply(function(){
            scope.$parent[attrs.visible] = false;
          });
        });
      }
    };
  });

function replaceAll(str, find, replace) {
	  return str.replace(new RegExp(escapeRegExp(find), 'g'), replace);
}

function escapeRegExp(str) {
    return str.replace(/([.*+?^=!:${}()|\[\]\/\\])/g, "\\$1");
}
