<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<link rel="stylesheet" type="text/css" href="styles/contract.css">

<security:authorize access="hasAnyRole('CUSTOMER','MANAGER')">

	<jstl:if test="${not empty contracts}">
		<display:table name="contracts" id="row" requestURI="${requestURI}"
			pagesize="5" class="displaytag">
			<!-- Attributes-->
			<display:column titleKey="contract.package" sortable="true">
				<security:authorize access="hasRole('CUSTOMER')">
					<span class="${empty row.signedCustomer }">
				</security:authorize>
				<jstl:out value="${row.request.pack.title}" />
				</span>
			</display:column>
			<display:column titleKey="contract.manager" sortable="true">
				<security:authorize access="hasRole('CUSTOMER')">
					<span class="${empty row.signedCustomer }">
				</security:authorize>
				<jstl:out value="${row.request.pack.manager.userAccount.username}" />
				</span>
			</display:column>
			<display:column titleKey="contract.customer" sortable="true">
				<security:authorize access="hasRole('CUSTOMER')">
					<span class="${empty row.signedCustomer }">
				</security:authorize>
				<jstl:out value="${row.request.customer.userAccount.username}" />
				</span>
			</display:column>
			<!-- Action links -->
			<display:column>
				<a href="contract/display.do?Id=${row.id}"> <spring:message
						code="contract.display" />
				</a>
			</display:column>
		</display:table>
	</jstl:if>
</security:authorize>