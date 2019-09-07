<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<script>
	function checkPhone(msg) {
		var phone = document.getElementById("phoneNumber").value;
		var pattern = /^(((([+][1-9]{1}[0-9]{0,2}[\s]){0,1}([(][1-9]{1}[0-9]{0,2}[)][\s]){0,1})){0,1}([0-9]{4}){1}([0-9]{0,}))$/;
		var pat = pattern.test(phone);
		if (pat) {
			return true;
		} else {
			return confirm(msg);
		}
	}
</script>
<spring:message code="phone.confirmation" var="confirmTelephone" />
<security:authorize access="hasRole('MANAGER')">
	<form:form modelAttribute="editionFormObject"
		action="manager/manager/edit.do"
		onsubmit="javascript: return checkPhone('${confirmTelephone}');">
		<form:hidden path="id" />
		<!-- Actor Attributes -->
		<fieldset>
			<legend style="font-size: 21px">
				<spring:message code="actor.personalData" />
			</legend>
			<div>
				<p>
					<form:errors path="name" cssClass="error" />
				</p>
				<strong><form:label path="name">
						<spring:message code="actor.name" />
					</form:label></strong>
				<form:input path="name" />
			</div>
			<div>
				<p>
					<form:errors path="surname" cssClass="error" />
				</p>
				<strong><form:label path="surname">
						<spring:message code="actor.surname" />
					</form:label></strong>
				<form:input path="surname" />
			</div>
			<br />
			<div>
				<p>
					<form:errors path="middlename" cssClass="error" />
				</p>
				<strong><form:label path="middlename">
						<spring:message code="actor.middlename" />
					</form:label></strong>
				<form:input path="middlename" />
			</div>
			<br />
			<div>
				<form:errors path="photo" cssClass="error">
					<p class="error">
						<spring:message code="photo.error" />
					</p>
				</form:errors>
				<strong><form:label path="photo">
						<spring:message code="actor.photo" />
					</form:label></strong>
				<form:input path="photo" />
			</div>
			<br />
			<div>
				<form:errors path="email" cssClass="error">
					<p class="error">
						<spring:message code="email.error" />
					</p>
				</form:errors>
				<strong><form:label path="email">
						<spring:message code="actor.email" />
					</form:label></strong>
				<form:input path="email" />
			</div>
			<div>
				<p>
					<form:errors path="phoneNumber" cssClass="error" />
				</p>
				<strong><form:label path="phoneNumber">
						<spring:message code="actor.phone" />
					</form:label></strong>
				<form:input path="phoneNumber" />
			</div>
			<div>
				<p>
					<form:errors path="address" cssClass="error" />
				</p>
				<strong><form:label path="address">
						<spring:message code="actor.address" />
					</form:label></strong>
				<form:input path="address" />
			</div>
			<br />
		</fieldset>
		<br />
		<br />
		<!-- Buttons -->
		<input type="submit" name="save"
			value="<spring:message code="form.save" />" />
		<jstl:if test="${count > 1}">
			<input type="submit" name="deleteManager"
				value="<spring:message code="actor.delete"/>"
				onclick="return confirm('<spring:message code="actor.confirm.delete"/>')" />
		</jstl:if>
		<button type="button" onclick="javascript: relativeRedir('/')">
			<spring:message code="form.cancel" />
		</button>
	</form:form>
		<a href="manager/delete.do"
		onclick="return confirm('<spring:message code="actor.confirm.delete"/>')">
		<spring:message code="actor.delete" /> </a>
</security:authorize>