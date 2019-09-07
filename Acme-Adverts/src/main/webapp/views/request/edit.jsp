<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasAnyRole('CUSTOMER','MANAGER')">
	<script type='text/javascript'>
		function addFields() {
			// Container <div> where dynamic content will be placed
			var container = document.getElementById("container");
			// Create an <input> element, set its type and name attributes
			var input = document.createElement("input");
			input.type = "text";
			input.name = "comments";
			container.appendChild(input);
		}
	</script>
	<form:form modelAttribute="request" action="request/edit.do" id="form">
		<fieldset>
			<br>
			<form:hidden path="id" />
			<form:hidden path="pack" />

			<security:authorize access="hasRole('CUSTOMER')">
			
				<acme:textarea code="request.commentsCustomer" path="commentsCustomer" cols="60px" rows="6"/><br/>
			
			</security:authorize>

			<security:authorize access="hasRole('MANAGER')">
			
				<acme:textarea code="request.commentsManager" path="commentsManager" cols="60px" rows="6"/><br/>
				
			</security:authorize>

		</fieldset>
		<br />

		<security:authorize access="hasRole('CUSTOMER')">
			<acme:submit code="request.save" name="save" />&nbsp;
		</security:authorize>

		<security:authorize access="hasRole('MANAGER')">

			<jstl:if test="${request.status == 'PENDING'}">
				<acme:submit code="request.approved" name="save" />&nbsp;
			</jstl:if>
			<script>
				function isEmpty() {
					var str = document.getElementById("commentsManager").value;
					if (!str.replace(/\s+/, '').length) { return confirm('<spring:message code="request.confirm.reject"/>'); }
				}
			</script>
			<!-- onclick="return confirm('<spring:message code="request.confirm.reject"/>')" 
		onsubmit="isEmpty()"-->
			<button type="submit" name="reject" onclick="return isEmpty()">
				<spring:message code="request.rejected" />
			</button>
		</security:authorize>


	<acme:cancel code="form.cancel" url="/" />
		<br />
	</form:form>
</security:authorize>
