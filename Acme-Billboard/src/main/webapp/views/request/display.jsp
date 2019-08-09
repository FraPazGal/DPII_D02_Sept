<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<!-- Actor Attributes -->
<security:authorize access="hasAnyRole('CUSTOMER','MANAGER')">

<fieldset style="width: 35%">
	<div style="float: left;">
		<div>
			<strong><spring:message code="request.status" />: </strong>
			<jstl:out value="${request.status}" />
		</div>
		<br />
		<div>
			<strong><spring:message code="request.commentsCustomer" />:
			</strong>
			<jstl:forEach items="${commentC}" var="item">
				<jstl:out value="${item }" />
				<br />
			</jstl:forEach>
		</div>
		<br />
		<div>
			<strong><spring:message code="request.commentsManager" />:
			</strong>
			<jstl:forEach items="${commentM}" var="item">
				<jstl:out value="${item }" />
				<br />
			</jstl:forEach>
		</div>
		<br />
		<div>
			<strong><spring:message code="request.customer" />: </strong>
			<jstl:out value="${request.customer.userAccount.username}" />
		</div>
		<br />
		<div>
			<strong><spring:message code="request.pack" />: </strong>
			<jstl:out value="${request.pack.title}" />
		</div>
		<br />

	</div>
</fieldset>
<br />
</security:authorize>
