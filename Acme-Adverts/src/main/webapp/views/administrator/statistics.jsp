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
			<th colspan="2"><spring:message code="administrator.FilesPerContract" /></th>
		</tr>
		<jstl:choose>
			<jstl:when test="${statsFilesPerContract[0] ne null}">
				<tr>
					<td><spring:message code="administrator.MaxFilesPerContract" /></td>
					<td style="text-align: right">${statsFilesPerContract[0]}</td>
				</tr>
				<tr>
					<td><spring:message code="administrator.MinFilesPerContract" /></td>
					<td style="text-align: right">${statsFilesPerContract[1]}</td>
				</tr>
				<tr>
					<td><spring:message code="administrator.AvgFilesPerContract" /></td>
					<td style="text-align: right">${statsFilesPerContract[2]}</td>
				</tr>
				<tr>
					<td><spring:message code="administrator.stdevFilesPerContract" /></td>
					<td style="text-align: right">${statsFilesPerContract[3]}</td>
				</tr>
			</jstl:when>
			<jstl:otherwise>
				<tr><td><br></td></tr>
				<tr>
					<td><spring:message code="administrator.statistics.noData" /></td>
				</tr>
			</jstl:otherwise>
		</jstl:choose>
	</table>

	<table class="displayStyle" style="width: 50%">
		<tr>
			<th colspan="2"><spring:message code="administrator.BillboardFilesPerContract" /></th>
		</tr>
		<jstl:choose>
			<jstl:when test="${statsBillboardFilesPerContract[0] ne null}">
				<tr>
					<td><spring:message code="administrator.MaxBillboardFilesPerContract" /></td>
					<td style="text-align: right">${statsBillboardFilesPerContract[0]}</td>
				</tr>
				<tr>
					<td><spring:message code="administrator.MinBillboardFilesPerContract" /></td>
					<td style="text-align: right">${statsBillboardFilesPerContract[1]}</td>
				</tr>
				<tr>
					<td><spring:message code="administrator.AvgBillboardFilesPerContract" /></td>
					<td style="text-align: right">${statsBillboardFilesPerContract[2]}</td>
				</tr>
				<tr>
					<td><spring:message code="administrator.stdevBillboardFilesPerContract" /></td>
					<td style="text-align: right">${statsBillboardFilesPerContract[3]}</td>
				</tr>
			</jstl:when>
			<jstl:otherwise>
				<tr><td><br></td></tr>
				<tr>
					<td><spring:message code="administrator.statistics.noData" /></td>
				</tr>
			</jstl:otherwise>
		</jstl:choose>
		
	</table>
	
	<table class="displayStyle" style="width: 50%">
		<tr>
			<th colspan="2"><spring:message code="administrator.RadioFilesPerContract" /></th>
		</tr>
		<jstl:choose>
			<jstl:when test="${statsRadioFilesPerContract[0] ne null}">
				<tr>
					<td><spring:message code="administrator.MaxRadioFilesPerContract" /></td>
					<td style="text-align: right">${statsRadioFilesPerContract[0]}</td>
				</tr>
				<tr>
					<td><spring:message code="administrator.MinRadioFilesPerContract" /></td>
					<td style="text-align: right">${statsRadioFilesPerContract[1]}</td>
				</tr>
				<tr>
					<td><spring:message code="administrator.AvgRadioFilesPerContract" /></td>
					<td style="text-align: right">${statsRadioFilesPerContract[2]}</td>
				</tr>
				<tr>
					<td><spring:message code="administrator.stdevRadioFilesPerContract" /></td>
					<td style="text-align: right">${statsRadioFilesPerContract[3]}</td>
				</tr>
			</jstl:when>
			<jstl:otherwise>
				<tr><td><br></td></tr>
				<tr>
					<td><spring:message code="administrator.statistics.noData" /></td>
				</tr>
			</jstl:otherwise>
		</jstl:choose>
	</table>
	
	<table class="displayStyle" style="width: 50%">
		<tr>
			<th colspan="2"><spring:message code="administrator.TVFilesPerContract" /></th>
		</tr>
		<jstl:choose>
			<jstl:when test="${statsTVFilesPerContract[0] ne null}">
				<tr>
					<td><spring:message code="administrator.MaxTVFilesPerContract" /></td>
					<td style="text-align: right">${statsTVFilesPerContract[0]}</td>
				</tr>
				<tr>
					<td><spring:message code="administrator.MinTVFilesPerContract" /></td>
					<td style="text-align: right">${statsTVFilesPerContract[1]}</td>
				</tr>
				<tr>
					<td><spring:message code="administrator.AvgTVFilesPerContract" /></td>
					<td style="text-align: right">${statsTVFilesPerContract[2]}</td>
				</tr>
				<tr>
					<td><spring:message code="administrator.stdevTVFilesPerContract" /></td>
					<td style="text-align: right">${statsTVFilesPerContract[3]}</td>
				</tr>
			</jstl:when>
			<jstl:otherwise>
				<tr><td><br></td></tr>
				<tr>
					<td><spring:message code="administrator.statistics.noData" /></td>
				</tr>
			</jstl:otherwise>
		</jstl:choose>
	</table>
	
	<table class="displayStyle" style="width: 50%">
		<tr>
			<th colspan="2"><spring:message code="administrator.SNFilesPerContract" /></th>
		</tr>
		<jstl:choose>
			<jstl:when test="${statsTVFilesPerContract[0] ne null}">
				<tr>
					<td><spring:message code="administrator.MaxSNFilesPerContract" /></td>
					<td style="text-align: right">${statsSNFilesPerContract[0]}</td>
				</tr>
				<tr>
					<td><spring:message code="administrator.MinSNFilesPerContract" /></td>
					<td style="text-align: right">${statsSNFilesPerContract[1]}</td>
				</tr>
				<tr>
					<td><spring:message code="administrator.AvgSNFilesPerContract" /></td>
					<td style="text-align: right">${statsSNFilesPerContract[2]}</td>
				</tr>
				<tr>
					<td><spring:message code="administrator.stdevSNFilesPerContract" /></td>
					<td style="text-align: right">${statsSNFilesPerContract[3]}</td>
				</tr>
			</jstl:when>
			<jstl:otherwise>
				<tr><td><br></td></tr>
				<tr>
					<td><spring:message code="administrator.statistics.noData" /></td>
				</tr>
			</jstl:otherwise>
		</jstl:choose>
	</table>
	
	<table class="displayStyle" style="width: 50%">
		<tr>
			<th colspan="2"><spring:message code="administrator.InfoFilesPerContract" /></th>
		</tr>
		<jstl:choose>
			<jstl:when test="${statsTVFilesPerContract[0] ne null}">
				<tr>
					<td><spring:message code="administrator.MaxInfoFilesPerContract" /></td>
					<td style="text-align: right">${statsInfoFilesPerContract[0]}</td>
				</tr>
				<tr>
					<td><spring:message code="administrator.MinInfoFilesPerContract" /></td>
					<td style="text-align: right">${statsInfoFilesPerContract[1]}</td>
				</tr>
				<tr>
					<td><spring:message code="administrator.AvgInfoFilesPerContract" /></td>
					<td style="text-align: right">${statsInfoFilesPerContract[2]}</td>
				</tr>
				<tr>
					<td><spring:message code="administrator.stdevInfoFilesPerContract" /></td>
					<td style="text-align: right">${statsInfoFilesPerContract[3]}</td>
				</tr>
			</jstl:when>
			<jstl:otherwise>
				<tr><td><br></td></tr>
				<tr>
					<td><spring:message code="administrator.statistics.noData" /></td>
				</tr>
			</jstl:otherwise>
		</jstl:choose>
	</table>

	<table class="displayStyle" style="width: 50%">
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