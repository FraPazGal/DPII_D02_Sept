<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jstl:if test="${not empty files}">
	<display:table name="files" id="row" requestURI="${requestURI}"
		pagesize="5" class="displaytag">

		<!-- Attributes-->
		<display:column titleKey="file.location" sortable="true">
			<jstl:out value="${row.location}" />
		</display:column>

		<!-- Action links -->
		<display:column>
			<a href="file/display.do?Id=${row.id}"> <spring:message
					code="file.display" />
			</a>
		</display:column>
		<display:column>
			<jstl:if test="${empty row.contract.signedManager}">

				<a href="file/edit.do?Id=${row.id}"> <spring:message
						code="file.edit" />
				</a>
			</jstl:if>

		</display:column>
		<display:column>
			<jstl:if test="${empty row.contract.signedManager}">
				<a href="file/delete.do?Id=${row.id}"> <spring:message
						code="file.delete" />
				</a>
			</jstl:if>
		</display:column>
	</display:table>
</jstl:if>
