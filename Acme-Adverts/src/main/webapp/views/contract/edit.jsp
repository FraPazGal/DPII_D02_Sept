<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('MANAGER')">
	<form:form modelAttribute="contract" action="contract/edit.do"
		id="form">
		<fieldset>
			<br>
			<form:hidden path="id" />
			<form:hidden path="version" />
			<form:hidden path="request" />
			
			<acme:textarea code="contract.text" path="text" cols="70px" rows="15"/><br/>
		</fieldset>
		<br />
		<acme:submit code="contract.save" name="save" />&nbsp;
		<acme:cancel code="form.cancel" url="contract/display.do?Id=${contract.request.id }" />
	</form:form>
	
</security:authorize>
