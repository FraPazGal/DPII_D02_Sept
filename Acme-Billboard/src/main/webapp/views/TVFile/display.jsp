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
		<br>
		<div>
			<strong><spring:message code="TVFile.broadcasterName" />: </strong>
			<jstl:out value="${TVFile.broadcasterName}" />
		</div>
		<br />
		<div>
			<strong><spring:message code="TVFile.schedule" />: </strong>
			<jstl:out value="${TVFile.schedule}" />
		</div>
		<br>
		<div>
			<strong><spring:message code="TVFile.video" />: </strong>
			<jstl:out value="${TVFile.video}" />
		</div>
		<br />
	</div>
</fieldset>
<br>
<acme:cancel code="file.back" url="contract/display.do?Id=${TVFile.contract.id }" />

<br />
