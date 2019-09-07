<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<!-- Actor Attributes -->
<spring:message code="date.dateFormat" var="format" /> 
<fieldset style="width: 35%">
	<div style="float: left;">
		<div>
			<strong><spring:message code="package.title" />: </strong>
			<jstl:out value="${pack.title}" />
		</div>
		<br />
		<div>
			<strong><spring:message code="package.description" />: </strong>
			<jstl:out value="${pack.description}" />
		</div>
		<br />
		<div>
			<strong><spring:message code="package.startDate" />: </strong>
			<fmt:formatDate pattern="${format }" value="${pack.startDate}" />
		</div>
		<br />
		<div>
			<strong><spring:message code="package.endDate" />: </strong>
			<fmt:formatDate pattern="${format }" value="${pack.endDate}" />
		</div>
		<br />
		<div>
			<strong><spring:message code="package.price" />: </strong>
			<jstl:out value="${pack.price}" />
		</div>
		<br />
		<div>
			<strong><spring:message code="package.ticker" />: </strong>
			<jstl:out value="${pack.ticker}" />
		</div>
		<br />

	</div>
	<div style="float: right;">
		<img style="width: 200px; height: 200px" src="${pack.photo}"
			alt="Package photo">
	</div>
</fieldset>
<br />
