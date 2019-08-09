<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasAnyRole('CUSTOMER','MANAGER')">
	<script type='text/javascript'>
		function addFields() {
			// Container <div> where dynamic content will be placed
			var container = document.getElementById("container");
			// Create an <input> element, set its type and name attributes
			var input = document.createElement("input");
			input.type = "text";
			input.name = "comments";
			container.appendChild(input);
		}
	</script>
	<form:form modelAttribute="request" action="request/edit.do" id="form">
		<fieldset>
			<br>
			<form:hidden path="id" />
			<form:hidden path="pack" />

			<security:authorize access="hasRole('CUSTOMER')">
				<form:label path="commentsCustomer">
					<spring:message code="request.commentsCustomer" />:*</form:label>
				<form:textarea type="text" path="commentsCustomer" />
				<form:errors path="commentsCustomer" cssClass="error" />
				<br />
				<br />
			</security:authorize>

			<security:authorize access="hasRole('MANAGER')">
				<form:label path="commentsManager">
					<spring:message code="request.commentsManager" />:*</form:label>
				<form:textarea path="commentsManager" />
				<form:errors path="commentsManager" cssClass="error" />
				<br />
			</security:authorize>

		</fieldset>
		<br />
		<acme:submit code="request.save" name="save" />&nbsp;
		<acme:cancel code="request.back" url="request/list.do" />
		<br />
	</form:form>
</security:authorize>
