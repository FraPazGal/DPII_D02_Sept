<%--
 * header.jsp
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

<div>
	<a href="#"><img src="${banner}" alt="${sysName}"
		style="margin-bottom: 0.5em;" /></a>
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<li><a class="fNiv"><spring:message
					code="master.page.package" /></a>
			<ul>
				<li class="arrow"></li>
				<li><a href="finder/anon/search.do"><spring:message
							code="master.page.search" /></a></li>
				<security:authorize access="isAnonymous()">
					<li><a href="package/listAll.do"><spring:message
								code="master.page.package.list" /></a></li>
				</security:authorize>
				<security:authorize access="hasAnyRole('CUSTOMER,ADMIN')">
					<li><a href="package/listAll.do"><spring:message
								code="master.page.package.list" /></a></li>
				</security:authorize>

				<security:authorize access="hasRole('MANAGER')">
					<li><a href="package/manager/list.do"><spring:message
								code="master.page.package.list" /></a></li>
					<li><a href="package/manager/create.do"><spring:message
								code="master.page.package.create" /></a></li>
				</security:authorize>
			</ul></li>
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message
						code="master.page.administrator" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="manager/administrator/register.do"><spring:message
								code="master.page.manager.register" /></a></li>
					<li><a href="administrator/administrator/register.do"><spring:message
								code="master.page.administrator.register" /></a></li>

				</ul></li>
		</security:authorize>
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message
						code="master.page.configuration" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="systemConfiguration/display.do"><spring:message
								code="master.page.configuration.display" /></a></li>
					<li><a href="administrator/statistics.do"><spring:message
								code="master.page.configuration.statistics" /></a></li>
				</ul></li>
		</security:authorize>
		<security:authorize access="hasAnyRole('MANAGER','CUSTOMER')">
			<li><a class="fNiv"><spring:message
						code="master.page.request" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="request/list.do"><spring:message
								code="master.page.request.list" /></a></li>
					<li><a href="contract/list.do"><spring:message
								code="master.page.request.contract.list" /></a></li>
				</ul></li>
		</security:authorize>

		<security:authorize access="isAnonymous()">
			<li><a class="fNiv"><spring:message
						code="master.page.profile" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="security/login.do"><spring:message
								code="master.page.login" /></a></li>
					<li><a href="customer/customer/register.do"><spring:message
								code="master.page.customer.register" /></a></li>
				</ul></li>
		</security:authorize>

		<security:authorize access="isAuthenticated()">
			<li><a class="fNiv"> <spring:message
						code="master.page.profile" /> (<security:authentication
						property="principal.username" />)
			</a>
				<ul>
					<li class="arrow"></li>
					<security:authorize access="hasRole('ADMIN')">
						<li><a href="administrator/display.do"><spring:message
									code="actor.view" /></a></li>
						<li><a href="administrator/export.do"><spring:message
									code="export" /></a></li>
						<li><a href="administrator/administrator/edit.do"><spring:message
									code="master.page.actor.edit" /></a></li>

					</security:authorize>
					<security:authorize access="hasRole('CUSTOMER')">
						<li><a href="finder/customer/list.do"><spring:message
									code="master.page.finder" /></a></li>
						<li><a href="customer/display.do"><spring:message
									code="actor.view" /></a></li>
						<li><a href="customer/export.do"><spring:message
									code="export" /></a></li>
						<li><a href="customer/customer/edit.do"><spring:message
									code="master.page.actor.edit" /></a></li>
					</security:authorize>

					<security:authorize access="hasRole('MANAGER')">
						<li><a href="finder/manager/display.do"><spring:message
									code="master.page.finder.statistic" /></a></li>
						<li><a href="manager/export.do"><spring:message
									code="export" /></a></li>
						<li><a href="manager/display.do"><spring:message
									code="actor.view" /></a></li>
						<li><a href="manager/manager/edit.do"><spring:message
									code="master.page.actor.edit" /></a></li>
					</security:authorize>

					<li><a href="j_spring_security_logout"><spring:message
								code="master.page.logout" /> </a></li>
				</ul>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>
<div>

	<jstl:if
		test="${pageContext.response.locale.language == 'es' and not empty breachNotification.get('SP')}">
		<h2>
			<strong style="color: red;"><jstl:out
					value="${breachNotification.get('SP')}">
				</jstl:out></strong> <br>
		</h2>
	</jstl:if>
	<jstl:if
		test="${pageContext.response.locale.language == 'en' and not empty breachNotification.get('EN')}">
		<h2>
			<strong style="color: red;"> <jstl:out
					value="${breachNotification.get('EN')}">
				</jstl:out>
			</strong> <br>

		</h2>
	</jstl:if>
</div>
