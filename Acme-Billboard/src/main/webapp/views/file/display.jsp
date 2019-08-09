<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<!-- Actor Attributes -->
<fieldset style="width: 35%">
	<div style="float: left;">
		<div>
			<strong><spring:message code="file.location" />: </strong>
			<jstl:out value="${file.location}" />
		</div>
		<br />

	</div>
	<div style="float: right;">
		<img style="width: 200px; height: 200px" src="${file.image}"
			alt="Image">
	</div>
</fieldset>

<br />
