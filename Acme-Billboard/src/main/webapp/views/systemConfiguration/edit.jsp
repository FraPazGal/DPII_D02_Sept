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
	<form:form action="systemConfiguration/edit.do"
		modelAttribute="sysConfig" methodParam="post">

		<form:hidden path="id" />
		<form:hidden path="version" />

		<acme:textbox code="system.name" path="systemName" />
		<br>
		<br>

		<acme:textbox code="system.bannerURL" path="banner" />
		<br>
		<br>

		<acme:textbox code="system.resultscached" path="timeResultsCached"
			size="5%" codeErr="timeErr" />
		<br>
		<br>

		<acme:textbox code="system.resultspersearch" path="maxResults"
			size="5%" codeErr="maxErr" />
		<br>
		<br>

		<acme:textbox code="system.countrycode" path="countryCode" />
		<br>
		<br>

		<acme:textbox code="system.VAT" path="VAT" />
		<form:errors path="VAT" cssClass="error" />
		<br>
		<br>

		<p>
			<spring:message code="system.makers" />
			:
			<button type="button" onClick="addFields()">
				<spring:message code="system.makers" />
			</button>
		<div id="container"></div>

		<jstl:forEach items="${sysConfig.makers}" var="item">
			<input name="makers" value="${item }" />
		</jstl:forEach>
		<form:errors path="makers" cssClass="error" />
		<br>
		<br>
		<p>
			<spring:message code="wel.name.es" />
		</p>
		<input type="text" name="nameES" id="nameES" size="100%"
			value="${sysConfig.welcomeMessage.get('SP')}"
			placeholder="<spring:message code='sysconfig.edit.welcome.message.es' />"
			required>

		<form:errors cssClass="error" path="welcomeMessage" />
		<br />
		<br />

		<p>
			<spring:message code="wel.name.en" />
		</p>
		<input type="text" name="nameEN" id="nameEN" size="100%"
			value="${sysConfig.welcomeMessage.get('EN')}"
			placeholder="<spring:message code='sysconfig.edit.welcome.message.en' />"
			required>
		<form:errors cssClass="error" path="welcomeMessage" />
		<br />
		<br />

		<!-- BRECHA -->
		<p>
			<spring:message code="breachNotification.name.es" />
		</p>
		<input type="text" name="nEs" id="nEs" size="100%"
			value="${sysConfig.breachNotification.get('Español')}">

		<form:errors cssClass="error" path="breachNotification" />
		<br />
		<br />

		<p>
			<spring:message code="breachNotification.name.en" />
		</p>
		<input type="text" name="nEn" id="nEn" size="100%"
			value="${sysConfig.breachNotification.get('English')}">
		<form:errors cssClass="error" path="breachNotification" />
		<br />
		<br />

		<acme:submit code="system.save" name="save" />&nbsp;
		<acme:cancel code="system.cancel"
			url="systemConfiguration/display.do" />
		<br />
		<br />

		<jstl:if test="${not empty errMessages}">
			<jstl:forEach items="${errMessages}" var="err">
				<div class="error">
					<spring:message code="${err}" />
					<br />
				</div>
			</jstl:forEach>
		</jstl:if>
	</form:form>

</security:authorize>