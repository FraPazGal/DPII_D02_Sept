<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form modelAttribute="file" action="file/edit.do" id="form">
	<fieldset>
		<br>
		<form:hidden path="id" />
		<form:hidden path="contract" />

		<form:label path="location">
			<spring:message code="file.location" />:*</form:label>
		<form:input type="text" path="location" />
		<form:errors path="location" cssClass="error" />
		<br />

		<form:label path="image">
			<spring:message code="file.image" />:*</form:label>
		<form:input type="text" path="image" />
		<form:errors path="image" cssClass="error" />
		<br />

	</fieldset>
	<br />
	<acme:submit code="file.save" name="save" />&nbsp;
		
	<acme:cancel code="file.back" url="file/list.do" />
	<br />
</form:form>
<jstl:if test="${file.id != 0}">

	<button onClick="window.location.href='file/delete.do?Id=${file.id}'">
		<spring:message code="file.delete" />
	</button>
</jstl:if>
