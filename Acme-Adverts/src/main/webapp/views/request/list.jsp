<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<link rel="stylesheet" type="text/css" href="styles/request.css">

<jstl:if test="${not empty requests}">
	<display:table name="requests" id="row" requestURI="${requestURI}"
		pagesize="5" class="displaytag">

		<!-- Attributes-->
		<display:column titleKey="request.status">
			<security:authorize access="hasRole('CUSTOMER')">
				<span class="${row.status}">
			</security:authorize>
			<jstl:out value="${row.status}" />
			<security:authorize access="hasRole('CUSTOMER')">
				</span>
			</security:authorize>
		</display:column>
		<display:column titleKey="request.customer">
			<security:authorize access="hasRole('CUSTOMER')">
				<span class="${row.status}">
			</security:authorize>
			<jstl:out value="${row.customer.userAccount.username}" />
			<security:authorize access="hasRole('CUSTOMER')">
				</span>
			</security:authorize>
		</display:column>
		<display:column titleKey="request.pack">
			<security:authorize access="hasRole('CUSTOMER')">
				<span class="${row.status}">
			</security:authorize>
			<jstl:out value="${row.pack.title}" />
			<security:authorize access="hasRole('CUSTOMER')">
				</span>
			</security:authorize>
		</display:column>

		<!-- Action links -->
		<display:column>
			<a href="request/display.do?Id=${row.id}"> <spring:message
					code="request.display" />
			</a>
		</display:column>
		<display:column>
			<jstl:if test="${row.status == 'APPROVED'}">
				<a href="contract/display.do?Id=${row.id}"> <spring:message
						code="request.contract.list" />
				</a>
			</jstl:if>
		</display:column>
		<security:authorize access="hasRole('MANAGER')">

			<display:column>
				<jstl:if test="${row.status == 'PENDING'}">

					<a href="request/edit.do?Id=${row.id}"> <spring:message
							code="request.edit" />
					</a>
				</jstl:if>

			</display:column>
		</security:authorize>
	</display:table>
</jstl:if>
