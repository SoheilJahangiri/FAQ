<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <title>FBTI FAQ MVC Solution</title>
    <link href="resources/css/main.css" rel="stylesheet"/>
    <link href="resources/css/Accordion.css" rel="stylesheet"/>
    <link href="resources/css/ngDialog.min.css" rel="stylesheet"/>
    <link href="resources/css/ngDialog-theme-default.min.css" rel="stylesheet"/>
    <link href="resources/css/ngDialog-theme-plain.min.css" rel="stylesheet"/>
    <link href="resources/css/ngDialog-custom-width.css" rel="stylesheet"/>
    <link href="resources/lib/font-awesome-4.7.0/css/font-awesome.min.css" rel="stylesheet"/>

</head>
<body>
<div class="container" ng-app="FAQApplication" ng-controller="FAQController">
    <div id="ribbon">
        <div class="category">
            <input type="text" id="txt_search" ng-change="operation.preSearch()" ng-model="operation.searchText">
        </div>
        <div class="operationCategory">
            <input type="button" id="btn_newIssue" value="مورد جدید" ng-click="operation.newIssue()">
        </div>
    </div>
    <div id="result" ng-repeat="x in data">
        <button class="accordion"><i class="questionMark fa fa-question-circle-o fa-lg" aria-hidden="true"></i> {{x.subject}}</button>
        <div class="panel" ng-repeat="y in x.solutions">
            <div class="editable">
                <span ng-click="editIssue(x.subjectID,y.sid,x.subject,y.solutionText)" class="icon"><i class="editIcon fa fa-pencil-square-o" aria-hidden="true"></i></span><%-- |--%>
                <%--<span ng-click="scoreUp(y.sid)" class="icon"><i class="nokie fa fa-thumbs-o-down" aria-hidden="true"></i></span>
                <span ng-click="scoreDown(y.sid)" class="icon"><i class="okie fa fa-thumbs-o-up" aria-hidden="true"></i></span>--%>

            </div>
            <pre class="solutionText"><i class="answerMark fa fa-info-circle fa-lg" aria-hidden="true"></i><br/>{{y.solutionText}}</pre>
        </div>
    </div>
</div>
<script src="resources/js/angular.min.js" type="text/javascript"></script>
<script src="resources/js/angular-route.min.js" type="text/javascript"></script>
<script src="resources/js/ngDialog.min.js" type="text/javascript"></script>
<script src="resources/js/FAQModule.js" type="text/javascript"></script>
<script src="resources/js/Accordion.js" type="text/javascript"></script>
<script type="text/javascript">
    var app = angular.module('FAQApplication',['ngRoute','FAQ']);
    app.config(function($routeProvider,$httpProvider) {
        $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
    });
</script>
</body>
</html>
