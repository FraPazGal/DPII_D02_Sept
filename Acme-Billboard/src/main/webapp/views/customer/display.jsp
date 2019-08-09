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
	<legend style="font-size: 21px">
		<spring:message code="actor.personalData" />
	</legend>

	<div style="float: left;">

		<div>
			<strong><spring:message code="actor.name" />: </strong>
			<jstl:out value="${customer.name}" />
		</div>
		<br />
		<div>
			<strong><spring:message code="actor.surname" />: </strong>
			<jstl:out value="${customer.surname}" />
		</div>
		<br />
		<div>
			<strong><spring:message code="actor.middlename" />: </strong>
			<jstl:out value="${customer.middleName}" />
		</div>
		<br />
		<div>
			<strong><spring:message code="actor.email" />: </strong>
			<jstl:out value="${customer.email}" />
		</div>
		<br />
		<div>
			<strong><spring:message code="actor.phone" />: </strong>
			<jstl:out value="${customer.phoneNumber}" />
		</div>
		<br />
		<div>
			<strong><spring:message code="actor.address" />: </strong>
			<jstl:out value="${customer.address}" />
		</div>
		<br />

		<div>
			<strong><spring:message code="customer.vat" />: </strong>
			<jstl:out value="${customer.vat}" />
		</div>
		<br />
	</div>
	<div style="float: right;">
		<img style="width: 200px; height: 200px" src="${customer.photo}"
			alt="User photo">
	</div>
</fieldset>
<br />
<br />