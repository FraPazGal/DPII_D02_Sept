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
	<form:form modelAttribute="packageForm" action="package/manager/edit.do"
		id="form">

		<fieldset>
			<br>
			<form:hidden path="id" />
			<form:hidden path="finalMode" />

			<div>
				<p>
					<form:errors path="title" cssClass="error" />
				</p>
				<strong><form:label path="title">
						<spring:message code="package.title" />:* 
					</form:label></strong>
				<form:input path="title" />
			</div>

			<div>
				<p>
					<form:errors path="description" cssClass="error" />
				</p>
				<strong><form:label path="description">
						<spring:message code="package.description" />:* 
					</form:label></strong>
				<form:input path="description" />
			</div>

			<div>
				<p>
					<form:errors path="startDate" cssClass="error" />
				</p>
				<strong><form:label path="startDate">
						<spring:message code="package.startDate" />:* 
					</form:label></strong>
				<form:input path="startDate" />
			</div>

			<div>
				<p>
					<form:errors path="endDate" cssClass="error" />
				</p>
				<strong><form:label path="endDate">
						<spring:message code="package.endDate" />:* 
					</form:label></strong>
				<form:input path="endDate" />
			</div>
			<div>
				<p>
					<form:errors path="photo" cssClass="error" />
				</p>
				<strong><form:label path="photo">
						<spring:message code="package.photo" />:  
					</form:label></strong>
				<form:input path="photo" />
			</div>

			<div>
				<p>
					<form:errors path="price" cssClass="error" />
				</p>
				<strong><form:label path="price">
						<spring:message code="package.price" />:* 
					</form:label></strong>
				<form:input path="price" />
			</div>

			<br />
		</fieldset>
		<br />
		<br />
		<jstl:if test="${packageForm.finalMode == false || packageForm.id == 0}">
			<acme:submit code="package.save" name="save" />&nbsp;
			<acme:submit code="package.save.final" name="saveFinal" />&nbsp; 
		</jstl:if>

		<br />
	</form:form>
	<jstl:if test="${packageForm.id != 0}">
		<jstl:if test="${packageForm.finalMode == false }">
			<button
				onClick="window.location.href='package/manager/delete.do?Id=${packageForm.id}'">
				<spring:message code="package.delete" />
			</button>
		</jstl:if>
	</jstl:if>
	<acme:cancel code="package.back" url="package/manager/list.do" />
</security:authorize>
