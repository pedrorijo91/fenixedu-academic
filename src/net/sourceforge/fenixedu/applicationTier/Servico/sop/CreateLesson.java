/*
 * CreateLesson.java
 *
 * Created on 2003/08/12
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o CreateLesson.
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidTimeIntervalServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonServiceResult;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftServiceResult;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IRoomOccupation;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import net.sourceforge.fenixedu.util.TipoAula;
import net.sourceforge.fenixedu.util.beanUtils.FenixPropertyUtils;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CreateLesson implements IService {

    public InfoLessonServiceResult run(InfoLesson infoLesson, InfoShift infoShift)
            throws FenixServiceException, ExcepcaoPersistencia {
        InfoLessonServiceResult result = null;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IRoom sala = sp.getISalaPersistente().readByName(infoLesson.getInfoSala().getNome());

        IRoomOccupation roomOccupation = Cloner.copyInfoRoomOccupation2RoomOccupation(infoLesson
                .getInfoRoomOccupation());
        roomOccupation.setRoom(sala);
        InfoPeriod infoPeriod = infoLesson.getInfoRoomOccupation().getInfoPeriod();
        IPeriod period = Cloner.copyInfoPeriod2IPeriod(infoPeriod);
        roomOccupation.setPeriod(period);

        IShift shift = Cloner.copyInfoShift2Shift(infoLesson.getInfoShift());
        IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoLesson
                .getInfoShift().getInfoDisciplinaExecucao().getInfoExecutionPeriod());

        ILesson aula = new Lesson(infoLesson.getDiaSemana(), infoLesson.getInicio(), infoLesson.getFim(),
                infoLesson.getTipo(), sala, roomOccupation, shift);
        result = validTimeInterval(aula);
        if (result.getMessageType() == 1) {
            throw new InvalidTimeIntervalServiceException();
        }
        boolean resultB = validNoInterceptingLesson(aula.getRoomOccupation());
        if (result.isSUCESS() && resultB) {
            try {
                InfoShiftServiceResult infoShiftServiceResult = valid(shift, aula);
                if (infoShiftServiceResult.isSUCESS()) {
                    ILesson aula2 = new Lesson();
                    sp.getIAulaPersistente().simpleLockWrite(aula2);

                    sp.getIPersistentRoomOccupation().simpleLockWrite(roomOccupation);

                    try {
                        FenixPropertyUtils.copyProperties(aula2, aula);
                    } catch (Exception e) {
                        throw new FenixServiceException(e);
                    }
                    aula2.setShift(shift);
                    aula2.setExecutionPeriod(executionPeriod);
                    aula2.setSala(aula.getSala());
                    aula2.setRoomOccupation(roomOccupation);
                } else {
                    throw new InvalidLoadException(infoShiftServiceResult.toString());
                }
            } catch (ExistingPersistentException ex) {
                throw new ExistingServiceException(ex);
            }
        } else {
            result.setMessageType(2);
        }

        return result;
    }

    /**
     * @param aula
     * @return InfoLessonServiceResult
     */

    private boolean validNoInterceptingLesson(IRoomOccupation roomOccupation)
            throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            List roomOccupationInDBList = sp.getIPersistentRoomOccupation().readAll();

            Iterator iter = roomOccupationInDBList.iterator();
            while (iter.hasNext()) {
                IRoomOccupation roomOccupationInDB = (IRoomOccupation) iter.next();
                if (roomOccupation.roomOccupationForDateAndTime(roomOccupationInDB)) {
                    return false;
                }
            }
            return true;
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        /*
         * try { ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
         * IAulaPersistente persistentLesson = sp.getIAulaPersistente(); List
         * lessonMatchList = persistentLesson.readLessonsInBroadPeriod(lesson,
         * null, executionPeriod); if (lessonMatchList.size() > 0) { if
         * (lessonMatchList.contains(lesson)) { throw new
         * ExistingServiceException(); } else { throw new
         * InterceptingServiceException(); } } else { return true; } } catch
         * (ExcepcaoPersistencia e) { return false; }
         */
    }

    private InfoLessonServiceResult validTimeInterval(ILesson lesson) {
        InfoLessonServiceResult result = new InfoLessonServiceResult();
        if (lesson.getInicio().getTime().getTime() >= lesson.getFim().getTime().getTime()) {
            result.setMessageType(InfoLessonServiceResult.INVALID_TIME_INTERVAL);
        }
        return result;
    }

    private InfoShiftServiceResult valid(IShift shift, ILesson lesson) throws ExcepcaoPersistencia {
        InfoShiftServiceResult result = new InfoShiftServiceResult();
        result.setMessageType(InfoShiftServiceResult.SUCESS);
        double hours = getTotalHoursOfShiftType(shift);
        double lessonDuration = (getLessonDurationInMinutes(lesson).doubleValue()) / 60;
        if (shift.getTipo().equals(new TipoAula(TipoAula.TEORICA))) {
            if (hours == shift.getDisciplinaExecucao().getTheoreticalHours().doubleValue())
                result.setMessageType(InfoShiftServiceResult.THEORETICAL_HOURS_LIMIT_REACHED);
            else if ((hours + lessonDuration) > shift.getDisciplinaExecucao().getTheoreticalHours()
                    .doubleValue())
                result.setMessageType(InfoShiftServiceResult.THEORETICAL_HOURS_LIMIT_EXCEEDED);
        } else if (shift.getTipo().equals(new TipoAula(TipoAula.PRATICA))) {
            if (hours == shift.getDisciplinaExecucao().getPraticalHours().doubleValue())
                result.setMessageType(InfoShiftServiceResult.PRATICAL_HOURS_LIMIT_REACHED);
            else if ((hours + lessonDuration) > shift.getDisciplinaExecucao().getPraticalHours()
                    .doubleValue())
                result.setMessageType(InfoShiftServiceResult.PRATICAL_HOURS_LIMIT_EXCEEDED);
        } else if (shift.getTipo().equals(new TipoAula(TipoAula.TEORICO_PRATICA))) {
            if (hours == shift.getDisciplinaExecucao().getTheoPratHours().doubleValue())
                result.setMessageType(InfoShiftServiceResult.THEO_PRAT_HOURS_LIMIT_REACHED);
            else if ((hours + lessonDuration) > shift.getDisciplinaExecucao().getTheoPratHours()
                    .doubleValue())
                result.setMessageType(InfoShiftServiceResult.THEO_PRAT_HOURS_LIMIT_EXCEEDED);
        } else if (shift.getTipo().equals(new TipoAula(TipoAula.LABORATORIAL))) {
            if (hours == shift.getDisciplinaExecucao().getLabHours().doubleValue())
                result.setMessageType(InfoShiftServiceResult.LAB_HOURS_LIMIT_REACHED);
            else if ((hours + lessonDuration) > shift.getDisciplinaExecucao().getLabHours()
                    .doubleValue())
                result.setMessageType(InfoShiftServiceResult.LAB_HOURS_LIMIT_EXCEEDED);
        }
        return result;
    }

    private double getTotalHoursOfShiftType(IShift shift) throws ExcepcaoPersistencia {
        /*
         * IShift shiftCriteria = new Shift();
         * shiftCriteria.setNome(shift.getNome());
         * shiftCriteria.setDisciplinaExecucao(shift.getDisciplinaExecucao());
         * 
         * List lessonsOfShiftType = SuportePersistenteOJB .getInstance()
         * .getITurnoAulaPersistente() .readLessonsByShift(shiftCriteria);
         * 
         * ILesson lesson = null; double duration = 0; for (int i = 0; i <
         * lessonsOfShiftType.size(); i++) { lesson = ((ITurnoAula)
         * lessonsOfShiftType.get(i)).getAula(); try { lesson.getIdInternal();
         * duration += (getLessonDurationInMinutes(lesson).doubleValue() / 60); }
         * catch (Exception ex) { // all is ok // the lesson contained a proxy
         * to null. } } return duration;
         */
        ILesson lesson = null;
        double duration = 0;
        List associatedLessons = shift.getAssociatedLessons();
        if (associatedLessons == null) {
            associatedLessons = PersistenceSupportFactory.getDefaultPersistenceSupport().getIAulaPersistente()
                    .readLessonsByShift(shift);
            shift.setAssociatedLessons(associatedLessons);
        }
        for (int i = 0; i < associatedLessons.size(); i++) {
            lesson = (ILesson) associatedLessons.get(i);
            try {
                lesson.getIdInternal();
                duration += (getLessonDurationInMinutes(lesson).doubleValue() / 60);
            } catch (Exception ex) {
                // all is ok
                // the lesson contained a proxy to null.
            }
        }
        return duration;
    }

    private Integer getLessonDurationInMinutes(ILesson lesson) {
        int beginHour = lesson.getInicio().get(Calendar.HOUR_OF_DAY);
        int beginMinutes = lesson.getInicio().get(Calendar.MINUTE);
        int endHour = lesson.getFim().get(Calendar.HOUR_OF_DAY);
        int endMinutes = lesson.getFim().get(Calendar.MINUTE);
        int duration = 0;
        duration = (endHour - beginHour) * 60 + (endMinutes - beginMinutes);
        return new Integer(duration);
    }

    /**
     * To change the template for this generated type comment go to
     * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
     */
    public class InvalidLoadException extends FenixServiceException {
        /**
         * 
         */
        private InvalidLoadException() {
            super();
        }

        /**
         * @param s
         */
        InvalidLoadException(String s) {
            super(s);
        }
    }
}