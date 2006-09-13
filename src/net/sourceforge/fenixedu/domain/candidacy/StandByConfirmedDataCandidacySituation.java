package net.sourceforge.fenixedu.domain.candidacy;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class StandByConfirmedDataCandidacySituation extends StandByConfirmedDataCandidacySituation_Base {

    public StandByConfirmedDataCandidacySituation(Candidacy candidacy) {
	super();
	setCandidacy(candidacy);
	Employee employee = AccessControl.getUserView().getPerson().getEmployee();
	if (employee == null) {
	    throw new DomainException("person is not an employee");
	}
	setEmployee(employee);
    }

    @Override
    public void checkConditionsToForward() {
	// TODO Auto-generated method stub

    }

    @Override
    public void nextState() {
	// TODO Auto-generated method stub

    }

    @Override
    public void checkConditionsToForward(String nextState) {
	// TODO Auto-generated method stub

    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
	return CandidacySituationType.STAND_BY_CONFIRMED_DATA;
    }

    @Override
    public Set<String> getValidNextStates() {
	Set<String> nextStates = new HashSet<String>();
	nextStates.add(CandidacySituationType.ADMITTED.toString());
	nextStates.add(CandidacySituationType.NOT_ADMITTED.toString());
	nextStates.add(CandidacySituationType.SUBSTITUTE.toString());	
	return nextStates;
    }

    @Override
    public void nextState(String nextState) {
	CandidacySituationType situationType = CandidacySituationType.valueOf(nextState);
	switch (situationType) {
	case ADMITTED:
	    new AdmittedCandidacySituation(this.getCandidacy());
	    break;
	case NOT_ADMITTED:
	    new NotAdmittedCandidacySituation(this.getCandidacy());
	    break;
	case SUBSTITUTE:
	    new SubstituteCandidacySituation(this.getCandidacy());
	    break;
	default:
	    break;
	}

    }

}
