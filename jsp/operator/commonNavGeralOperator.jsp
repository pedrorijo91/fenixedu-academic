<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<bean:define id="userView" name="<%= SessionConstants.U_VIEW %>" scope="session"/>
<ul id="navgeral">
	<logic:iterate id="role" name="userView" property="roles">
		<bean:define id="bundleKeyPageName">
			<bean:write name="role" property="pageNameProperty"/>.name
		</bean:define>
		<bean:define id="link">
			<%= request.getContextPath() %>/dotIstPortal.do?prefix=<bean:write name="role" property="portalSubApplication"/>&amp;page=<bean:write name="role" property="page"/>
		</bean:define>
	<li><logic:equal name="role" property="portalSubApplication" value="/operator">
	    	<html:link href='<%= link %>' styleClass="active">
	    	<bean:message name="bundleKeyPageName" bundle="PORTAL_RESOURCES"/></html:link></li>
		</logic:equal>
		<logic:notEqual name="role" property="portalSubApplication" value="/operator">
	    	<html:link href='<%= link %>'>
			<bean:message name="bundleKeyPageName" bundle="PORTAL_RESOURCES"/></html:link></li>
		</logic:notEqual>
</logic:iterate>
</ul>
