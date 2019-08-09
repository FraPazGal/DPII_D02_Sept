<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('MANAGER')">
	<fieldset style="width: 35%">
		<div style="float: left;">
			<div>
				<strong><spring:message code="contract.signedCustomer" />:
				</strong>
				<jstl:out value="${contract.signedCustomer}" />
			</div>
			<br />
			<div>
				<strong><spring:message code="contract.signedManager" />:
				</strong>
				<jstl:out value="${contract.signedManager}" />
			</div>
			<br />
			<div>
				<strong><spring:message code="contract.text" />: </strong>
				<jstl:out value="${contract.text}" />
			</div>
			<br />
			<div>
				<strong><spring:message code="contract.customer" />: </strong>
				<jstl:out value="${contract.request.customer.userAccount.username}" />
			</div>
			<br />
			<div>
				<strong><spring:message code="contract.package" />: </strong>
				<jstl:out value="${contract.request.pack.title}" />
			</div>
			<br />

		</div>
	</fieldset>
	<br />
	<jstl:if test="${empty contract.signedManager}">
		<a href="contract/edit.do?Id=${contract.request.id}"> <spring:message
				code="contract.edit" />
		</a>
		<br />

		<a href="contract/sign.do?Id=${contract.id}"> <spring:message
				code="contract.sign" />
		</a>
		<br />
		<a href="file/create.do?Id=${contract.id}"> <spring:message
				code="file.create" />
		</a>
	</jstl:if>
	<br />
	<a href="file/list.do?Id=${contract.id}"> <spring:message
			code="file.list" />
	</a>
	<br />
	<br />

	<jstl:if test="${errorFile eq 'errorFile'}">
		<a class="error"><spring:message code="file.error.one" /></a>
	</jstl:if>
</security:authorize>

<security:authorize access="hasRole('CUSTOMER')">
	<jstl:if test="${not empty contract.signedManager}">
		<!-- Actor Attributes -->
		<fieldset style="width: 35%">
			<div style="float: left;">
				<div>
					<strong><spring:message code="contract.signedCustomer" />:
					</strong>
					<jstl:out value="${contract.signedCustomer}" />
				</div>
				<br />
				<div>
					<strong><spring:message code="contract.signedManager" />:
					</strong>
					<jstl:out value="${contract.signedManager}" />
				</div>
				<br />
				<div>
					<strong><spring:message code="contract.text" />: </strong>
					<jstl:out value="${contract.text}" />
				</div>
				<br />
				<div>
					<strong><spring:message code="contract.hash" />: </strong>
					<jstl:out value="${contract.hash}" />
				</div>
				<br />
				<div>
					<strong><spring:message code="contract.customer" />: </strong>
					<jstl:out value="${contract.request.customer.userAccount.username}" />
				</div>
				<br />
				<div>
					<strong><spring:message code="contract.package" />: </strong>
					<jstl:out value="${contract.request.pack.title}" />
				</div>
				<br />

			</div>
		</fieldset>
		<jstl:if test="${empty contract.signedCustomer}">
			<a href="contract/sign.do?Id=${contract.id}"> <spring:message
					code="contract.sign" />
			</a>
		</jstl:if>
		<br />
		<a href="file/list.do?Id=${contract.id}"> <spring:message
				code="file.list" />
		</a>
		<br />
	</jstl:if>
	<jstl:if test="${empty contract.signedManager}">
		<spring:message code="contract.not.sign" />
	</jstl:if>

</security:authorize>
