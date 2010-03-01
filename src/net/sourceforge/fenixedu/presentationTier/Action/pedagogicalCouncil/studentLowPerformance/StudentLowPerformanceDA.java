package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.TutorshipStudentLowPerformanceQueueJob;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/studentLowPerformance", module = "pedagogicalCouncil")
@Forwards( { @Forward(name = "viewStudentsState", path = "/pedagogicalCouncil/tutorship/viewStudentsState.jsp"),
	@Forward(name = "viewStudentsState", path = "/pedagogicalCouncil/tutorship/viewStudentsState.jsp") })
public class StudentLowPerformanceDA extends FenixDispatchAction {

    protected final String PRESCRIPTION_BEAN = "prescriptionBean";

    public ActionForward viewStudentsState(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	PrescriptionBean prescriptionBean = getContextBean(request);
	if (prescriptionBean.getSelectedPrescriptionEnum() == null) {
	    return viewJobs(mapping, actionForm, request, response, null);
	}
	TutorshipStudentLowPerformanceQueueJob job = TutorshipStudentLowPerformanceQueueJob
		.createTutorshipStudentLowPerformanceQueueJob(prescriptionBean.getSelectedPrescriptionEnum());
	return viewJobs(mapping, actionForm, request, response, job);

    }

    private PrescriptionBean getContextBean(HttpServletRequest request) {
	PrescriptionBean bean = (PrescriptionBean) getRenderedObject(PRESCRIPTION_BEAN);
	RenderUtils.invalidateViewState(PRESCRIPTION_BEAN);
	if (bean == null) {
	    return new PrescriptionBean(null);
	}
	return bean;
    }

    public ActionForward cancelQueuedJob(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	cancelQueuedJob(request);
	return viewJobs(mapping, actionForm, request, response, null);
    }

    public ActionForward resendJob(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	resendJob(request);
	return viewJobs(mapping, actionForm, request, response, null);
    }

    @Service
    private void cancelQueuedJob(HttpServletRequest request) {
	int oid = Integer.valueOf(request.getParameter("id"));
	for (QueueJob queueJob : rootDomainObject.getQueueJob()) {
	    if (queueJob.getIdInternal() == oid) {
		List<QueueJob> undoneJobs = rootDomainObject.getQueueJobUndone();
		undoneJobs.remove(queueJob);
	    }
	}
    }

    @Service
    private void resendJob(HttpServletRequest request) {
	int oid = Integer.valueOf(request.getParameter("id"));
	for (QueueJob queueJob : rootDomainObject.getQueueJob()) {
	    if (queueJob.getIdInternal() == oid) {
		List<QueueJob> undoneJobs = rootDomainObject.getQueueJobUndone();
		undoneJobs.add(queueJob);
	    }
	}
    }

    public List<QueueJob> getLatestJobs() {
	return (QueueJob.getAllJobsForClassOrSubClass(TutorshipStudentLowPerformanceQueueJob.class, 5));

    }

    private ActionForward viewJobs(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response, TutorshipStudentLowPerformanceQueueJob job) throws Exception {

	PrescriptionBean prescriptionBean = getContextBean(request);
	request.setAttribute("job", job);
	request.setAttribute("queueJobList", getLatestJobs());
	request.setAttribute(PRESCRIPTION_BEAN, prescriptionBean);
	return mapping.findForward("viewStudentsState");
    }

}