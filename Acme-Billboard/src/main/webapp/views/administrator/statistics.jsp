<%--
 * action-2.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('ADMIN')">

	<table class="displayStyle" style="width: 50%">
		<tr>
			<th colspan="2"><spring:message
					code="administrator.statistics.C" /></th>
		</tr>
		<tr>
			<td><spring:message code="administrator.maxRequestPerManager" /></td>
			<td style="text-align: right">${maxRequestPerManager}</td>
		</tr>
		<tr>
			<td><spring:message code="administrator.minRequestPerManager" /></td>
			<td style="text-align: right">${minRequestPerManager}</td>
		</tr>
		<tr>
			<td><spring:message code="administrator.avgRequestPerManager" /></td>
			<td style="text-align: right">${avgRequestPerManager}</td>
		</tr>
		<tr>
			<td><spring:message code="administrator.sttdevRequestPerManager" /></td>
			<td style="text-align: right">${sttdevRequestPerManager}</td>
		</tr>
	</table>

	<table class="displayStyle" style="width: 50%">
		<tr>
			<td><spring:message
					code="administrator.avgRequestPerManagerPending" /></td>
			<td style="text-align: right">${avgRequestPerManagerPending}</td>
		</tr>
		<tr>
			<td><spring:message
					code="administrator.maxRequestPerManagerPending" /></td>
			<td style="text-align: right">${maxRequestPerManagerPending}</td>
		</tr>
		<tr>
			<td><spring:message
					code="administrator.minRequestPerManagerPending" /></td>
			<td style="text-align: right">${minRequestPerManagerPending}</td>
		</tr>
		<tr>
			<td><spring:message
					code="administrator.sttdevRequestPerManagerPending" /></td>
			<td style="text-align: right">${sttdevRequestPerManagerPending}</td>
		</tr>
	</table>

	<table class="displayStyle" style="width: 50%">
		<tr>
			<td><spring:message code="administrator.avgRequestPerCustomer" /></td>
			<td style="text-align: right">${avgRequestPerCustomer}</td>
		</tr>
		<tr>
			<td><spring:message code="administrator.maxRequestPerCustomer" /></td>
			<td style="text-align: right">${maxRequestPerCustomer}</td>
		</tr>
		<tr>
			<td><spring:message code="administrator.minRequestPerCustomer" /></td>
			<td style="text-align: right">${minRequestPerCustomer}</td>
		</tr>
		<tr>
			<td><spring:message
					code="administrator.sttdevRequestPerCustomer" /></td>
			<td style="text-align: right">${sttdevRequestPerCustomer}</td>
		</tr>

	</table>

	<table class="displayStyle" style="width: 50%">
		<tr>
			<td><spring:message
					code="administrator.avgRequestPerCustomerPending" /></td>
			<td style="text-align: right">${avgRequestPerCustomerPending}</td>
		</tr>
		<tr>
			<td><spring:message
					code="administrator.maxRequestPerCustomerPending" /></td>
			<td style="text-align: right">${maxRequestPerCustomerPending}</td>
		</tr>
		<tr>
			<td><spring:message
					code="administrator.minRequestPerCustomerPending" /></td>
			<td style="text-align: right">${minRequestPerCustomerPending}</td>
		</tr>
		<tr>
			<td><spring:message
					code="administrator.sttdevRequestPerCustomerPending" /></td>
			<td style="text-align: right">${sttdevRequestPerCustomerPending}</td>
		</tr>

	</table>

	<table class="displayStyle" style="width: 50%">
		<tr>
			<th colspan="2"><spring:message
					code="administrator.top10manager" /></th>
		</tr>
		<jstl:forEach items="${managers}" var="item">
			<tr>
				<td><spring:message code="administrator.ManagerUsername" /></td>
				<td style="text-align: right"> ${item.userAccount.username}</td>
			</tr>
		</jstl:forEach>
	</table>

	<table class="displayStyle" style="width: 50%">
		<tr>
			<th colspan="2"><spring:message
					code="administrator.statistics.B" /></th>
		</tr>
		<tr>
			<td><spring:message code="administrator.MaxPackagePerFinder" /></td>
			<td style="text-align: right">${statsFinder[0]}</td>
		</tr>
		<tr>
			<td><spring:message code="administrator.MinPackagePerFinder" /></td>
			<td style="text-align: right">${statsFinder[1]}</td>
		</tr>
		<tr>
			<td><spring:message code="administrator.AvgPackagePerFinder" /></td>
			<td style="text-align: right">${statsFinder[2]}</td>
		</tr>
		<tr>
			<td><spring:message code="administrator.stdevPackagePerFinder" /></td>
			<td style="text-align: right">${statsFinder[3]}</td>
		</tr>

		<tr>
			<td><spring:message code="administrator.emptyFinder" /></td>
			<td style="text-align: right">${ratioFinders}</td>
		</tr>

	</table>

</security:authorize>