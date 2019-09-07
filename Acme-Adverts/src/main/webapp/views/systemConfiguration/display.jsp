<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('ADMIN')">

	<jstl:choose>
		<jstl:when test="${err}">
			<p>
				<jstl:out value="${errMsg.getMessage()}" />
			</p>
		</jstl:when>
		<jstl:otherwise>

			<table class="displayStyle">

				<tr>
					<td><spring:message code="system.name" />:</td>
					<td><jstl:out value="${config.systemName}" /></td>
				</tr>

				<tr>
					<td><spring:message code="system.bannerURL" />:</td>
					<td><a href="${config.banner}"><jstl:out
								value="${config.banner}" /></a></td>
				</tr>

				<tr>
					<td><spring:message code="system.resultscached" />:</td>
					<td><jstl:out value="${config.timeResultsCached}" /></td>
				</tr>

				<tr>
					<td><spring:message code="system.resultspersearch" />:</td>
					<td><jstl:out value="${config.maxResults}" /></td>
				</tr>

				<tr>
					<td><spring:message code="system.countrycode" />:</td>
					<td><jstl:out value="${config.countryCode}" /></td>
				</tr>

				<tr>		
					<td><spring:message code="system.makers" />:</td>
					<td><jstl:out value="${config.makers}" /></td>
				</tr>

			</table>

			<jstl:if test="${not empty welcome}">
		<table class="displayStyle">

			<tr>
				<td><strong><spring:message code="welcome.es" /></strong></td>
				<td><jstl:out value="${welcome.get('SP')}" /></td>
			</tr>

			<tr>
				<td><strong><spring:message code="welcome.en" /></strong></td>
				<td><jstl:out value="${welcome.get('EN')}" /></td>
			</tr>

		</table>
	</jstl:if>

			<jstl:if test="${not empty welcome}">
		<table class="displayStyle">

			<tr>
				<td><strong><spring:message code="breach.es" /></strong></td>
				<td><jstl:out value="${breach.get('SP')}" /></td>
			</tr>

			<tr>
				<td><strong><spring:message code="breach.en" /></strong></td>
				<td><jstl:out value="${breach.get('EN')}" /></td>
			</tr>

		</table>
	</jstl:if>

			<input type="button" name="edit"
				value='<spring:message code="system.edit"/>'
				onclick="redirect: location.href = 'systemConfiguration/edit.do?systemconfigurationID=${config.id}';" />

		</jstl:otherwise>
	</jstl:choose>


</security:authorize>