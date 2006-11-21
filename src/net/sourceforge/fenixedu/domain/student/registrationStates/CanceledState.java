package net.sourceforge.fenixedu.domain.student.registrationStates;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CanceledState extends CanceledState_Base {

    public CanceledState(Registration registration, Person person, DateTime dateTime) {
	super();
	init(registration, person, dateTime);
    }

    public void checkConditionsToForward() {
	throw new DomainException("error.impossible.to.forward.from.canceled");
    }

    public void checkConditionsToForward(String nextState) {
	throw new DomainException("error.impossible.to.forward.from.canceled");
    }

    public Set<String> getValidNextStates() {
	return new HashSet<String>();
    }

    public void nextState() {
	throw new DomainException("error.impossible.to.forward.from.canceled");
    }

    public void nextState(String nextState) {
	throw new DomainException("error.impossible.to.forward.from.canceled");
    }

    @Override
    public RegistrationStateType getStateType() {
	return RegistrationStateType.CANCELED;
    }

}
