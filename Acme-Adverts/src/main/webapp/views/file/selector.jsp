<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>


<form:form modelAttribute="typeForm" action="file/selector.do" id="form">
	<fieldset style="width: 25%">
		<legend style="font-size: 21px">
			<spring:message code="file.selector.title" />
		</legend>
		<br>
		<form:hidden path="id" />
		<form:select path="type">
			<form:options items="${types}"/>
		</form:select>
	</fieldset>
	<br />
	
	<acme:submit code="file.select" name="select" />&nbsp;
	<acme:cancel code="file.back" url="contract/display.do?Id=${backId}" />
	<br />
</form:form>