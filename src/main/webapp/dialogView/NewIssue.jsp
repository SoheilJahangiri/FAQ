<%--
  Created by IntelliJ IDEA.
  User: jahangiri
  Date: 07/12/2017
  Time: 03:23 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>{{issuePageTitle}}</title>
</head>
<body>
    <input type="hidden" ng-model="subjectID">
    <input type="hidden" ng-model="sid">
    <div class="container issuePage" ng-controller="FAQIssuerController">
        <div class="issue">
            <span>موضوع {{issueOperation}}:</span>
            <input type="text" ng-model="issueSubject">
        </div>
        <div class="solutions">
            <span>راه حل:</span>
            <textarea class="txtArea" ng-model="issueSolutions">

            </textarea>
        </div>
        <input class="btn" type="button" ng-click="submitNewIssue()" value="انجام">
        <span ng-bind="submitStatus"></span>
    </div>
</body>
</html>
