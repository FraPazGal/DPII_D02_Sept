<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<!-- Actor Attributes -->
<fieldset style="width: 35%">
	<div style="float: left;">
		<div>
			<br>
			<strong><spring:message code="billboardFile.location" />: </strong>
			<jstl:out value="${billboardFile.location}" />
		</div>
		<br />

	</div>
	<div style="float: right;">
		<img style="width: 200px; height: 200px" src="${billboardFile.image}"
			alt="Image">
	</div>
</fieldset>
<br>
<acme:cancel code="file.back" url="contract/display.do?Id=${billboardFile.contract.request.id }" />

<br />
