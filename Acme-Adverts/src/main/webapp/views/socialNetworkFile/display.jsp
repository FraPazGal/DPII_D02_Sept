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
			<strong><spring:message code="socialNetworkFile.target" />: </strong>
			<a href="${socialNetworkFile.target}" target="_blank"><jstl:out value="${socialNetworkFile.target}" /></a>
		</div>
		<br />

	</div>
	<div style="float: right;">
		<a href="${socialNetworkFile.target}" target="_blank">
			<img style="width: 200px; height: 200px" src="${socialNetworkFile.banner}" 
				alt="Image">
		</a>
	</div>
</fieldset>
<br>
<acme:cancel code="file.back" url="contract/display.do?Id=${socialNetworkFile.contract.request.id }" />

<br />
