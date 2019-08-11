<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form modelAttribute="socialNetworkFile" action="socialNetworkFile/edit.do" id="form">
	<fieldset>
		<br>
		<form:hidden path="id" />
		<form:hidden path="contract" />

		<acme:textbox code="socialNetworkFile.banner" path="banner" size="100px" /><br/> <br/>
		<acme:textbox code="socialNetworkFile.target" path="target" size="100px" /><br/>

	</fieldset>
	<br />
	<acme:submit code="file.save" name="save" />&nbsp;
	<acme:cancel code="file.back" url="contract/display.do?Id=${socialNetworkFile.contract.id }" />
	<br />
</form:form>
