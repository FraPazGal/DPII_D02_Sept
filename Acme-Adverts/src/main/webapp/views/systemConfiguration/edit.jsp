<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<security:authorize access="hasRole('ADMIN')">
	<script type='text/javascript'>
		function addFields() {
			// Container <div> where dynamic content will be placed
			var container = document.getElementById("container");
			// Create an <input> element, set its type and name attributes
			var input = document.createElement("input");
			input.type = "text";
			input.name = "makers";
			container.appendChild(input);
		}
	</script>
	<form:form modelAttribute="systemConfiguration"
		action="systemConfiguration/edit.do">


		<form:hidden path="id" />
		<form:hidden path="version" />

		<acme:textbox code="system.name" path="systemName" />
		<br>
		<acme:textbox code="system.bannerURL" path="banner" />
		<br>
		<acme:textbox code="system.countrycode" path="countryCode" />
		<br>
		<div>
			<form:label path="timeResultsCached">
					<spring:message code="system.resultscached" />: 
					</form:label>
			<form:input type="number" path="timeResultsCached" />
			<p>
				<form:errors path="timeResultsCached" cssClass="error" />
			</p>
		</div>
		<br>
		<div>
			<form:label path="maxResults">
					<spring:message code="system.resultspersearch" />: 
					</form:label>
			<form:input type="number" path="maxResults" />
			<p>
				<form:errors path="maxResults" cssClass="error" />
			</p>
		</div>
		<br>
		<p>
			<spring:message code="system.makers" />
			:
			<button type="button" onClick="addFields()">
				<spring:message code="config.add" />
			</button>
		<div id="container"></div>

		<jstl:forEach items="${systemConfiguration.makers}" var="item">
			<input name="makers" value="${item}" />
		</jstl:forEach>
		<form:errors path="makers" cssClass="error" />
		<br>
		<br>

		<strong><spring:message code="welcome.es" /></strong>
		<br>
		<input type="text" name="welcomeES" id="welcomeES" size="150%"
			value="${welcome.get('SP')}"
			placeholder="<spring:message code='welcome.es' />">

		<br>
		<br>
		<strong><spring:message code="welcome.en" /></strong>
		<br>
		<input type="text" name="welcomeEN" id="welcomeEN" size="150%"
			value="${welcome.get('EN')}"
			placeholder="<spring:message code='welcome.en' />">

		<br>
		<br>
		<form:errors cssClass="error" path="welcomeMessage" />
		<br />

		<strong><spring:message code="breach.es" /></strong>
		<br>
		<input type="text" name="breachES" id="breachES" size="150%"
			value="${breach.get('SP')}"
			placeholder="<spring:message code='breach.es' />">

		<br>
		<br>
		<strong><spring:message code="breach.en" /></strong>
		<br>
		<input type="text" name="breachEN" id="breachEN" size="150%"
			value="${breach.get('EN')}"
			placeholder="<spring:message code='breach.en' />">

		<br>
		<br>
		<form:errors cssClass="error" path="breachNotification" />
		<br />

		<form:errors cssClass="error" code="${message}" />
		<acme:submit code="system.save" name="save" />
		<acme:cancel code="system.cancel" url="systemConfiguration/display.do" />
	</form:form>
	<br>

</security:authorize>
