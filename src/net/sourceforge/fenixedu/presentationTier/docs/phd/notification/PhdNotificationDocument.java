package net.sourceforge.fenixedu.presentationTier.docs.phd.notification;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.postingRules.FixedAmountPR;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.notification.PhdNotification;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class PhdNotificationDocument extends FenixReport {

    private static final String DATE_FORMAT_PT = "dd/MM/yyyy";

    private static final String DATE_FORMAT_EN = "yyyy/MM/dd";

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private DomainReference<PhdNotification> notification;

    private Language language;

    public PhdNotificationDocument(PhdNotification notification, Language language) {
	setNotification(notification);
	setLanguage(language);
	fillReport();
    }

    private PhdNotification getNotification() {
	return (this.notification != null) ? this.notification.getObject() : null;
    }

    private void setNotification(PhdNotification notification) {
	this.notification = (notification != null) ? new DomainReference<PhdNotification>(notification) : null;
    }

    public Language getLanguage() {
	return language;
    }

    public void setLanguage(Language language) {
	this.language = language;
    }

    @Override
    protected void fillReport() {

	final PhdProgramCandidacyProcess candidacyProcess = getNotification().getCandidacyProcess();
	final Person person = candidacyProcess.getPerson();
	final PhdIndividualProgramProcess individualProgramProcess = candidacyProcess.getIndividualProgramProcess();

	addParameter("administrativeOfficeCoordinator", AdministrativeOffice.readByAdministrativeOfficeType(
		AdministrativeOfficeType.MASTER_DEGREE).getUnit().getActiveUnitCoordinator().getFirstAndLastName());

	addParameter("name", person.getName());
	addParameter("address", person.getAddress());
	addParameter("areaCode", person.getAreaCode());
	addParameter("areaOfAreaCode", person.getAreaOfAreaCode());
	addParameter("programName", individualProgramProcess.getPhdProgram().getName().getContent(getLanguage()));

	addParameter("processNumber", individualProgramProcess.getProcessNumber());

	final LocalDate whenRatified = candidacyProcess.getWhenRatified();

	addParameter("ratificationDate", whenRatified != null ? whenRatified.toString(getDateFormat()) : "");

	addParameter("insuranceFee", getInsuranceFee(individualProgramProcess));
	addParameter("registrationFee", getRegistrationFee(individualProgramProcess, whenRatified));

	addParameter("date", new LocalDate().toString(getDateFormat()));
	addParameter("notificationNumber", getNotification().getNotificationNumber());
    }

    private String getDateFormat() {
	return getLanguage() == Language.pt ? DATE_FORMAT_PT : DATE_FORMAT_EN;
    }

    private String getRegistrationFee(final PhdIndividualProgramProcess individualProgramProcess, final LocalDate whenRatified) {
	return whenRatified != null ? ((FixedAmountPR) individualProgramProcess.getPhdProgram().getServiceAgreementTemplate()
		.findPostingRuleByEventTypeAndDate(EventType.PHD_REGISTRATION_FEE, whenRatified.toDateTimeAtMidnight()))
		.getFixedAmount().toPlainString() : "";
    }

    private String getInsuranceFee(final PhdIndividualProgramProcess individualProgramProcess) {
	return ((FixedAmountPR) RootDomainObject.getInstance().getInstitutionUnit().getUnitServiceAgreementTemplate()
		.findPostingRuleBy(EventType.INSURANCE,
			individualProgramProcess.getExecutionYear().getBeginDateYearMonthDay().toDateTimeAtMidnight(),
			individualProgramProcess.getExecutionYear().getEndDateYearMonthDay().toDateTimeAtMidnight()))
		.getFixedAmount().toPlainString();
    }

    @Override
    public String getReportFileName() {
	return "Notification-" + getNotification().getNotificationNumber().replace("/", "-") + "-"
		+ new DateTime().toString(YYYYMMDDHHMMSS);
    }

    @Override
    public String getReportTemplateKey() {
	return getClass().getName() + "." + getNotification().getType().name() + "." + getLanguage().name();
    }

}
