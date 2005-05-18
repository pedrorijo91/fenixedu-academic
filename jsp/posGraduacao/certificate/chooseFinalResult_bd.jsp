<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="label.certificate.finalResult.create" /></h2>
<span class="error"><html:errors/></span>
<br />
	<table>
		<html:form action="/chooseFinalResultInfoAction?method=choose">
   	  	<html:hidden property="page" value="1"/>
	<!-- Requester Number -->
 		<tr>
        	 <td><bean:message key="label.masterDegree.administrativeOffice.requesterNumber"/>: </td>
         	<td><html:text property="requesterNumber" value=""/></td>
       	</tr>
  	<!-- Graduation Type --> 
       	<tr>
        	 <td><bean:message key="label.masterDegree.administrativeOffice.graduationType"/>: </td>
         	 <td>
         	 	<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization" excludedFields="INTEGRATED_MASTER_DEGREE" bundle="ENUMERATION_RESOURCES"/>
            	<html:select property="graduationType">
                	<html:option key="dropDown.Default" value=""/>
               		<html:options collection="values" property="value" labelProperty="label" />
            	</html:select>          
        	 </td>
        </tr>
	</table>
<br />
<html:submit value="Submeter" styleClass="inputbutton" property="ok"/>   
</html:form>