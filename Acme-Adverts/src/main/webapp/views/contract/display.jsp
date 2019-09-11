<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>


<spring:message code="date.dateFormat" var="format" /> 
<security:authorize access="hasRole('MANAGER')">
	<spring:message code="date.dateFormat" var="format" /> 
	<fieldset style="width: 35%">
		<div style="float: left;">
			<div>
				<strong><spring:message code="contract.signedCustomer" />:</strong>
				<fmt:formatDate pattern="${format }" value="${contract.signedCustomer}" />
			</div>
			<br />
			<div>
				<strong><spring:message code="contract.signedManager" />:</strong>
				<fmt:formatDate pattern="${format }" value="${contract.signedManager}" />
			</div>
			<br />
			<div>
				<strong><spring:message code="contract.text" />: </strong>
				<jstl:out value="${contract.text}" />
			</div>
			<br />
			<div>
				<strong><spring:message code="contract.customer" />: </strong>
				<jstl:out value="${contract.request.customer.userAccount.username}" />
			</div>
			<br />
			<div>
				<strong><spring:message code="contract.package" />: </strong>
				<jstl:out value="${contract.request.pack.title}" />
			</div>
			<br />

		</div>
	</fieldset>
	<jstl:if test="${empty contract.signedManager}">
		<br>
		
		<input type="button"
		onclick="redirect: location.href = 'contract/edit.do?Id=${contract.request.id}';"
		value="<spring:message code='contract.edit' />" />&nbsp;
		
		<jstl:if test="${not empty billboardFiles or not empty infoFiles or not empty radioFiles or not empty TVFiles or not empty socialNetworkFile}">
			<input type="button"
			onclick="redirect: location.href = 'contract/sign.do?Id=${contract.id}';"
			value="<spring:message code='contract.sign' />" /><br><br>
		</jstl:if>
		
		<input type="button"
		onclick="redirect: location.href = 'file/selector.do?contractId=${contract.id}';"
		value="<spring:message code='file.create' />" /><br>
		
	</jstl:if>

	<jstl:if test="${errorFile eq 'errorFile'}">
		<a class="error"><spring:message code="file.error.one" /></a>
	</jstl:if>
	<br>
</security:authorize>

<security:authorize access="hasRole('CUSTOMER')">
	<jstl:if test="${not empty contract.signedManager}">
		<!-- Actor Attributes -->
		<fieldset style="width: 35%">
			<div style="float: left;">
				<div>
					<strong><spring:message code="contract.signedCustomer" />:</strong>
					<fmt:formatDate pattern="${format }" value="${contract.signedCustomer}" />
				</div>
				<br />
				<div>
					<strong><spring:message code="contract.signedManager" />:</strong>
					<fmt:formatDate pattern="${format }" value="${contract.signedManager}" />
				</div>
				<br />
				<div>
					<strong><spring:message code="contract.text" />: </strong>
					<jstl:out value="${contract.text}" />
				</div>
				<br />
				<div>
					<strong><spring:message code="contract.hash" />: </strong>
					<jstl:out value="${contract.hash}" />
				</div>
				<br />
				<div>
					<strong><spring:message code="contract.customer" />: </strong>
					<jstl:out value="${contract.request.customer.userAccount.username}" />
				</div>
				<br />
				<div>
					<strong><spring:message code="contract.package" />: </strong>
					<jstl:out value="${contract.request.pack.title}" />
				</div>
				<br />

			</div>
		</fieldset>
		<jstl:if test="${empty contract.signedCustomer}">
			<br>
			<input type="button"
				onclick="redirect: location.href = 'contract/sign.do?Id=${contract.id}';"
				value="<spring:message code='contract.sign' />" /><br>
		</jstl:if>
		<br />
	</jstl:if>
	<jstl:if test="${empty contract.signedManager}">
		<spring:message code="contract.not.sign" />
	</jstl:if>

</security:authorize>

<security:authorize access="hasAnyRole('MANAGER')">

	<!-- BILLBOARD FILES -->
	<jstl:if test="${not empty billboardFiles and empty contract.signedManager}">
		<h2><spring:message  code="billboardFile.list"/></h2>
		<display:table name="billboardFiles" id="billboardFile" pagesize="5" class="displaytag"
			requestURI="/contract/display.do?Id=${contract.id}">
			
			<!-- Attributes-->
			<display:column titleKey="billboardFile.location" sortable="true">
				<jstl:out value="${billboardFile.location}" />
			</display:column>
	
			<!-- Action links -->
			<display:column>
				<a href="billboardFile/display.do?Id=${billboardFile.id}"> <spring:message
						code="file.display" />
				</a>
			</display:column>
			<display:column>
				<a href="billboardFile/edit.do?Id=${billboardFile.id}"> <spring:message
						code="file.edit" />
				</a>
			</display:column>
			<display:column>
				<a href="billboardFile/delete.do?Id=${billboardFile.id}"> <spring:message
						code="file.delete" />
				</a>
			</display:column>
		</display:table>
		<br>
	</jstl:if>
	<jstl:if test="${not empty billboardFiles and not empty contract.signedManager}">
		<h2><spring:message code="billboardFile.list" /></h2>
		<display:table name="billboardFiles" id="billboardFile" pagesize="5" class="displaytag"
			requestURI="/contract/display.do?Id=${contract.id}">
			
			<!-- Attributes-->
			<display:column titleKey="billboardFile.location" sortable="true">
				<jstl:out value="${billboardFile.location}" />
			</display:column>
	
			<!-- Action links -->
			<display:column>
				<a href="billboardFile/display.do?Id=${billboardFile.id}"> <spring:message
						code="file.display" />
				</a>
			</display:column>
		</display:table>
		<br>
	</jstl:if>
	
	<!-- INFO FILES -->
	<jstl:if test="${not empty infoFiles and empty contract.signedManager}">
		<h2><spring:message code="infoFile.list" /></h2>
		<display:table name="infoFiles" id="infoFile" pagesize="5" class="displaytag"
			requestURI="/contract/display.do?Id=${contract.id}">
			
			<!-- Attributes-->
			<display:column titleKey="infoFile.title" sortable="true">
				<jstl:out value="${infoFile.title}" />
			</display:column>
	
			<!-- Action links -->
			<display:column>
				<a href="infoFile/display.do?Id=${infoFile.id}"> <spring:message
						code="file.display" />
				</a>
			</display:column>
			<display:column>
				<a href="infoFile/edit.do?Id=${infoFile.id}"> <spring:message
						code="file.edit" />
				</a>
			</display:column>
			<display:column>
				<a href="infoFile/delete.do?Id=${infoFile.id}"> <spring:message
						code="file.delete" />
				</a>
			</display:column>
		</display:table>
		<br>
	</jstl:if>
	<jstl:if test="${not empty infoFiles and not empty contract.signedManager}">
		<h2><spring:message code="infoFile.list" /></h2>
		<display:table name="infoFiles" id="infoFile" pagesize="5" class="displaytag"
			requestURI="/contract/display.do?Id=${contract.id}">
			
			<!-- Attributes-->
			<display:column titleKey="infoFile.title" sortable="true">
				<jstl:out value="${infoFile.title}" />
			</display:column>
	
			<!-- Action links -->
			<display:column>
				<a href="infoFile/display.do?Id=${infoFile.id}"> <spring:message
						code="file.display" />
				</a>
			</display:column>
		</display:table>
		<br>
	</jstl:if>
	
	<!-- TV FILES -->
	<jstl:if test="${not empty TVFiles and empty contract.signedManager}">
		<h2><spring:message code="TVFile.list" /></h2>
		<display:table name="TVFiles" id="TVFile" pagesize="5" class="displaytag"
			requestURI="/contract/display.do?Id=${contract.id}">
			
			<!-- Attributes-->
			<display:column titleKey="TVFile.broadcasterName" sortable="true">
				<jstl:out value="${TVFile.broadcasterName}" />
			</display:column>
	
			<!-- Action links -->
			<display:column>
				<a href="TVFile/display.do?Id=${TVFile.id}"> <spring:message
						code="file.display" />
				</a>
			</display:column>
			<display:column>
				<a href="TVFile/edit.do?Id=${TVFile.id}"> <spring:message
						code="file.edit" />
				</a>
			</display:column>
			<display:column>
				<a href="TVFile/delete.do?Id=${TVFile.id}"> <spring:message
						code="file.delete" />
				</a>
			</display:column>
		</display:table>
		<br>
	</jstl:if>
	<jstl:if test="${not empty TVFiles and not empty contract.signedManager}">
		<h2><spring:message code="TVFile.list" /></h2>
		<display:table name="TVFiles" id="TVFile" pagesize="5" class="displaytag"
			requestURI="/contract/display.do?Id=${contract.id}">
			
			<!-- Attributes-->
			<display:column titleKey="TVFile.broadcasterName" sortable="true">
				<jstl:out value="${TVFile.broadcasterName}" />
			</display:column>
	
			<!-- Action links -->
			<display:column>
				<a href="TVFile/display.do?Id=${TVFile.id}"> <spring:message
						code="file.display" />
				</a>
			</display:column>
		</display:table>
		<br>
	</jstl:if>
	
	<!-- RADIO FILES -->
	<jstl:if test="${not empty radioFiles and empty contract.signedManager}">
		<h2><spring:message code="radioFile.list" /></h2>
		<display:table name="radioFiles" id="radioFile" pagesize="5" class="displaytag"
			requestURI="/contract/display.do?Id=${contract.id}">
			
			<!-- Attributes-->
			<display:column titleKey="radioFile.broadcasterName" sortable="true">
				<jstl:out value="${radioFile.broadcasterName}" />
			</display:column>
	
			<!-- Action links -->
			<display:column>
				<a href="radioFile/display.do?Id=${radioFile.id}"> <spring:message
						code="file.display" />
				</a>
			</display:column>
			<display:column>
				<a href="radioFile/edit.do?Id=${radioFile.id}"> <spring:message
						code="file.edit" />
				</a>
			</display:column>
			<display:column>
				<a href="radioFile/delete.do?Id=${radioFile.id}"> <spring:message
						code="file.delete" />
				</a>
			</display:column>
		</display:table>
		<br>
	</jstl:if>
	<jstl:if test="${not empty radioFiles and not empty contract.signedManager}">
		<h2><spring:message code="radioFile.list" /></h2>
		<display:table name="radioFiles" id="radioFile" pagesize="5" class="displaytag"
			requestURI="/contract/display.do?Id=${contract.id}">
			
			<!-- Attributes-->
			<display:column titleKey="radioFile.broadcasterName" sortable="true">
				<jstl:out value="${radioFile.broadcasterName}" />
			</display:column>
	
			<!-- Action links -->
			<display:column>
				<a href="radioFile/display.do?Id=${radioFile.id}"> <spring:message
						code="file.display" />
				</a>
			</display:column>
		</display:table>
		<br>
	</jstl:if>
	
	<!-- SOCIAL NETWORK FILES -->
	<jstl:if test="${not empty socialNetworkFiles and empty contract.signedManager}">
		<h2><spring:message code="socialNetworkFile.list" /></h2>
		<display:table name="socialNetworkFiles" id="socialNetworkFile" pagesize="5" class="displaytag"
			requestURI="/contract/display.do?Id=${contract.id}">
			
			<!-- Attributes-->
			<display:column titleKey="socialNetworkFile.target" sortable="true">
				<jstl:out value="${socialNetworkFile.target}" />
			</display:column>
	
			<!-- Action links -->
			<display:column>
				<a href="socialNetworkFile/display.do?Id=${socialNetworkFile.id}"> <spring:message
						code="file.display" />
				</a>
			</display:column>
			<display:column>
				<a href="socialNetworkFile/edit.do?Id=${socialNetworkFile.id}"> <spring:message
						code="file.edit" />
				</a>
			</display:column>
			<display:column>
				<a href="socialNetworkFile/delete.do?Id=${socialNetworkFile.id}"> <spring:message
						code="file.delete" />
				</a>
			</display:column>
		</display:table>
		<br>
	</jstl:if>
	<jstl:if test="${not empty socialNetworkFiles and not empty contract.signedManager}">
		<h2><spring:message code="socialNetworkFile.list" /></h2>
		<display:table name="socialNetworkFiles" id="socialNetworkFile" pagesize="5" class="displaytag"
			requestURI="/contract/display.do?Id=${contract.id}">
			
			<!-- Attributes-->
			<display:column titleKey="socialNetworkFile.target" sortable="true">
				<jstl:out value="${socialNetworkFile.target}" />
			</display:column>
	
			<!-- Action links -->
			<display:column>
				<a href="socialNetworkFile/display.do?Id=${socialNetworkFile.id}"> <spring:message
						code="file.display" />
				</a>
			</display:column>
		</display:table>
		<br>
	</jstl:if>

</security:authorize>

<security:authorize access="hasAnyRole('CUSTOMER')">

	<!-- BILLBOARD FILES -->
	<jstl:if test="${not empty billboardFiles and not empty contract.signedManager}">
		<h2><spring:message code="billboardFile.list" /></h2>
		<display:table name="billboardFiles" id="billboardFile" pagesize="5" class="displaytag"
			requestURI="/contract/display.do?Id=${contract.id}">
			
			<!-- Attributes-->
			<display:column titleKey="billboardFile.location" sortable="true">
				<jstl:out value="${billboardFile.location}" />
			</display:column>
	
			<!-- Action links -->
			<display:column>
				<a href="billboardFile/display.do?Id=${billboardFile.id}"> <spring:message
						code="file.display" />
				</a>
			</display:column>
		</display:table>
		<br>
	</jstl:if>
	
	<!-- INFO FILES -->
	<jstl:if test="${not empty infoFiles and not empty contract.signedManager}">
		<h2><spring:message code="infoFile.list" /></h2>
		<display:table name="infoFiles" id="infoFile" pagesize="5" class="displaytag"
			requestURI="/contract/display.do?Id=${contract.id}">
			
			<!-- Attributes-->
			<display:column titleKey="infoFile.title" sortable="true">
				<jstl:out value="${infoFile.title}" />
			</display:column>
	
			<!-- Action links -->
			<display:column>
				<a href="infoFile/display.do?Id=${infoFile.id}"> <spring:message
						code="file.display" />
				</a>
			</display:column>
		</display:table>
		<br>
	</jstl:if>
	
	<!-- TV FILES -->
	<jstl:if test="${not empty TVFiles and not empty contract.signedManager}">
		<h2><spring:message code="TVFile.list" /></h2>
		<display:table name="TVFiles" id="TVFile" pagesize="5" class="displaytag"
			requestURI="/contract/display.do?Id=${contract.id}">
			
			<!-- Attributes-->
			<display:column titleKey="TVFile.broadcasterName" sortable="true">
				<jstl:out value="${TVFile.broadcasterName}" />
			</display:column>
	
			<!-- Action links -->
			<display:column>
				<a href="TVFile/display.do?Id=${TVFile.id}"> <spring:message
						code="file.display" />
				</a>
			</display:column>
		</display:table>
		<br>
	</jstl:if>
	
	<!-- RADIO FILES -->
	<jstl:if test="${not empty radioFiles and not empty contract.signedManager}">
		<h2><spring:message code="radioFile.list" /></h2>
		<display:table name="radioFiles" id="radioFile" pagesize="5" class="displaytag"
			requestURI="/contract/display.do?Id=${contract.id}">
			
			<!-- Attributes-->
			<display:column titleKey="radioFile.broadcasterName" sortable="true">
				<jstl:out value="${radioFile.broadcasterName}" />
			</display:column>
	
			<!-- Action links -->
			<display:column>
				<a href="radioFile/display.do?Id=${radioFile.id}"> <spring:message
						code="file.display" />
				</a>
			</display:column>
		</display:table>
		<br>
	</jstl:if>
	
	<!-- SOCIAL NETWORK FILES -->
	<jstl:if test="${not empty socialNetworkFiles and not empty contract.signedManager}">
		<h2><spring:message code="socialNetworkFile.list" /></h2>
		<display:table name="socialNetworkFiles" id="socialNetworkFile" pagesize="5" class="displaytag"
			requestURI="/contract/display.do?Id=${contract.id}">
			
			<!-- Attributes-->
			<display:column titleKey="socialNetworkFile.target" sortable="true">
				<jstl:out value="${socialNetworkFile.target}" />
			</display:column>
	
			<!-- Action links -->
			<display:column>
				<a href="socialNetworkFile/display.do?Id=${socialNetworkFile.id}"> <spring:message
						code="file.display" />
				</a>
			</display:column>
		</display:table>
		<br>
	</jstl:if>

</security:authorize>

