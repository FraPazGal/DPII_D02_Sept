<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jstl:if test="${not empty packages}">
	<display:table name="packages" id="row" requestURI="${requestURI}"
		pagesize="5" class="displaytag">

		<!-- Attributes-->
		<display:column titleKey="package.ticker" sortable="true">
			<jstl:out value="${row.ticker}" />
		</display:column>
		<display:column titleKey="package.title" sortable="true">
			<jstl:out value="${row.title}" />
		</display:column>
		<display:column titleKey="package.startDate" sortable="true">
			<jstl:out value="${row.startDate}" />
		</display:column>
		<display:column titleKey="package.endDate" sortable="true">
			<jstl:out value="${row.endDate}" />
		</display:column>
		<display:column titleKey="package.price" sortable="true">
			<fmt:formatNumber maxFractionDigits="2" value="${row.price}" />
		</display:column>
		<display:column titleKey="package.manager" sortable="true">
			<jstl:out value="${row.manager.userAccount.username}" />
		</display:column>

		<!-- Action links -->
		<display:column>

			<jstl:if test="${row.finalMode == true}">
				<security:authorize access="hasRole('CUSTOMER')">
					<a href="request/create.do?Id=${row.id}"> <spring:message
							code="request.create" />
					</a>
				</security:authorize>
				<security:authorize access="hasRole('MANAGER')">
					<a href="request/listPackage.do?Id=${row.id}"> <spring:message
							code="request.list" />
					</a>
				</security:authorize>
			</jstl:if>
		</display:column>
		<display:column>
			<a href="package/display.do?Id=${row.id}"> <spring:message
					code="package.display" />
			</a>
		</display:column>
		<display:column>
			<jstl:if test="${row.finalMode == false}">
				<a href="package/manager/edit.do?Id=${row.id}"> <spring:message
						code="package.edit" />
				</a>
			</jstl:if>

		</display:column>
		<display:column>
			<jstl:if test="${row.finalMode == false}">
				<a href="package/manager/delete.do?Id=${row.id}"> <spring:message
						code="package.delete" />
				</a>
			</jstl:if>

		</display:column>
	</display:table>
</jstl:if>
