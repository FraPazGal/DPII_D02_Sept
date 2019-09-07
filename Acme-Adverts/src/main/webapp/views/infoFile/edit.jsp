<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form modelAttribute="infoFile" action="infoFile/edit.do" id="form">
	<fieldset>
		<br>
		<form:hidden path="id" />
		<form:hidden path="contract" />

		<acme:textbox code="infoFile.title" path="title" size="85px" /><br/> <br/>
		<acme:textarea code="infoFile.text" path="text" cols="60px" rows="6"/><br/>

	</fieldset>
	<br />
	<acme:submit code="file.save" name="save" />&nbsp;
	<acme:cancel code="file.cancel" url="contract/display.do?Id=${infoFile.contract.request.id }" />
	<br />
</form:form>
