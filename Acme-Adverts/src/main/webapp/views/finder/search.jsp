<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<security:authorize access="hasRole('CUSTOMER')">
	<jstl:if test="${empty anon}">

		<form:form action="finder/customer/search.do" modelAttribute="finder">

			<form:hidden path="id" />
			<form:hidden path="version" />


			<form:label path="keyWord">
				<spring:message code="finder.keyWord" />:
		</form:label>
			<form:input path="keyWord" />
			<form:errors cssClass="error" path="keyWord" />
			<br />
			<br />

			<form:label path="minDate">
				<spring:message code="finder.minDate" />:
		</form:label>
			<form:input path="minDate" placeholder="dd/MM/yyyy HH:mm" />
			<form:errors cssClass="error" path="minDate" />


			<form:label path="maxDate">
				<spring:message code="finder.maxDate" />:
		</form:label>
			<form:input path="maxDate" placeholder="dd/MM/yyyy HH:mm" />
			<form:errors cssClass="error" path="maxDate" />
			<br />
			<br />

			<form:label path="minPrice">
				<spring:message code="finder.minPrice" />:
		</form:label>
			<form:input path="minPrice" placeholder="1000.00" />
			<form:errors cssClass="error" path="minPrice" />
			<br />
			<br />
			<form:label path="maxPrice">
				<spring:message code="finder.maxPrice" />:
		</form:label>
			<form:input path="maxPrice" placeholder="1000.00" />
			<form:errors cssClass="error" path="maxPrice" />
			<br />
			<br />

			<input type="submit" name="save" id="save"
				value="<spring:message code="finder.showResults" />" />
		
	&#160;
		<jstl:if test="${finder.id!=0}">
				<input type="submit" name="delete" id="delete"
					value='<spring:message code="finder.delete"/>' />
			</jstl:if>

		</form:form>

		<jstl:if test="${not empty packages}">
			<display:table name="packages" id="row"
				requestURI="finder/customer/list.do" pagesize="5" class="displaytag">

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
						<a href="request/create.do?Id=${row.id}"> <spring:message
								code="request.create" />
						</a>
					</jstl:if>
				</display:column>
				<display:column>
					<jstl:if test="${row.finalMode == true}">
						<a href="package/display.do?Id=${row.id}"> <spring:message
								code="package.display" />
						</a>
					</jstl:if>
				</display:column>
			</display:table>
		</jstl:if>
	</jstl:if>

</security:authorize>

<jstl:if test="${not empty anon}">

	<form>
		<b><spring:message code="finder.keyWord.search" />:&#160;</b> <input id="test"
			type="text" name="keyWord" size="20" />

		<script>
			var keyWord = "";
			document.getElementById("test").value = keyWord;
		</script>

		<input type="submit" value="Search" name="submit" />

	</form>

	<br>
	<br>
	<jstl:if test="${not empty packages}">
		<display:table name="packages" id="row"
			requestURI="finder/anon/search.do" pagesize="5" class="displaytag">

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
					<security:authorize access="hasRole('MANAGER')">
						<jstl:if test="${actor eq row.manager }">
							<a href="request/listPackage.do?Id=${row.id}"> <spring:message
									code="request.list" />
							</a>
						</jstl:if>
					</security:authorize>
				</jstl:if>
			</display:column>
			<display:column>
				<jstl:if test="${row.finalMode == true}">
					<a href="package/display.do?Id=${row.id}"> <spring:message
							code="package.display" />
					</a>
				</jstl:if>
			</display:column>
			<display:column>
				<jstl:if test="${row.finalMode == false and actor eq row.manager}">
					<a href="package/manager/edit.do?Id=${row.id}"> <spring:message
							code="package.edit" />
					</a>
				</jstl:if>
			</display:column>
			<display:column>
				<jstl:if test="${row.finalMode == false and actor eq row.manager}">
					<a href="package/manager/delete.do?Id=${row.id}"> <spring:message
							code="package.delete" />
					</a>
				</jstl:if>
			</display:column>
		</display:table>
	</jstl:if>
</jstl:if>

