/**
 * Created by jahangiri on 07/12/2017.
 */
angular.module("FAQ",['ngDialog'])
    .run(function($rootScope){

    })
    .controller('FAQController',['$scope','$rootScope','$http','ngDialog',function($scope,$rootScope,$http,ngDialog) {
        $scope.operation = {
            searchText: null,
            timer: null,
            counter: 10,
            timeOut: null,
            alreadySetTimeOut: false,
            preSearch: function () {
                if ($scope.operation.searchText && $scope.operation.searchText.trim() != "") {
                    $scope.countDown = "";
                    if (!$scope.operation.alreadySetTimeOut) {
                        $scope.operation.timeOut = setTimeout(function () {
                            $scope.search();
                        }, 2500)
                        $scope.operation.alreadySetTimeOut = true;
                    }
                    if ($scope.operation.alreadySetTimeOut && $scope.operation.timeOut != null) {
                        if ($scope.operation.timeOut != null) {
                            clearTimeout($scope.operation.timeOut);
                            $scope.operation.timeOut = setTimeout(function () {
                                $scope.operation.search();
                            }, 2500)
                        }
                    }
                }
            },
            search:function() {
                $scope.data = [{}];
                if ($scope.operation.searchText && $scope.operation.searchText.trim() != "")
                    var formData = {
                        "criteria": $scope.operation.searchText
                    };
                var response = $http.post('search', formData).then(
                    function successResponse(response) {
                        $scope.data = response.data;
                    }, function errorResponse(response) {
                        /*alert("search failed. with reason:" + response.data);*/
                    }
                );
            },
            newIssue: function () {
                ngDialog.open({
                    template: 'dialogView/NewIssue.jsp',
                    controller: 'FAQIssuerController',
                    className: 'ngdialog-theme-default',
                    rootScope: $scope
                });
            }
        };
        $scope.editIssue=function(subjectID,solutionID,subject,solution) {
            $scope.subjectID=subjectID;
            $scope.sid=solutionID;
            $scope.subject=subject;
            $scope.solution=solution;
            ngDialog.open({
                template: 'dialogView/NewIssue.jsp',
                className: 'ngdialog-theme-default',
                scope: $scope
            });
        }
        $scope.scoreUp=function(solutionID) {
            var formData = {
                "solutionText":"",
                "sid": $scope.sid,
            };

            var response = $http.post('scoreUp', formData).then(
                function successResponse(response) {
                    if (response.data == true)
                        alert("Score Submitted");
                }, function errorResponse(response) {
                    alert("failure");
                }
            );
        }
        $scope.scoreDown=function(solutionID) {
            var formData = {
                "solutionText":"",
                "sid": $scope.sid,
            };

            var response = $http.post('scoreDown', formData).then(
                function successResponse(response) {
                    if (response.data == true)
                        alert("Score Submitted");
                }, function errorResponse(response) {
                    alert("failure");
                }
            );
        }
    }])
    .controller('FAQIssuerController',function($scope,$rootScope,$http) {
        $scope.submitStatus = "";
        var sid=$scope.sid;
        if (sid) {
            $scope.issuePageTitle = "ویرایش موضوع";
            $scope.issueOperation = "مورد ویرایش";
            $scope.subjectID=$scope.subjectID;
            $scope.sid = sid;
            $scope.issueSubject = $scope.subject;
            $scope.issueSolutions = $scope.solution;
        } else {
            $scope.issuePageTitle="موضوع جدید";
            $scope.issueOperation="جدید";
            $scope.subjectID="";
            $scope.sid = "";
            $scope.issueSubject = "";
            $scope.issueSolutions = "";
        }

        $scope.submitNewIssue = function () {
            var formData = {
                "subjectID": $scope.subjectID,
                "sid": $scope.sid,
                "subject": $scope.issueSubject,
                "solution": $scope.issueSolutions
            };

            var response = $http.post('issuer', formData).then(
                function successResponse(response) {
                    if(response.data==true)
                        location.reload();
                }, function errorResponse(response) {
                    alert("failure");
                }
            );
        }
    });
