/*
 * CriarTurmaServicosTest.java
 * JUnit based test
 *
 * Created on 27 de Outubro de 2002, 18:43
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.sop;

/**
 * 
 * @author tfc130
 */
import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ICurso;
import net.sourceforge.fenixedu.domain.ICursoExecucao;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.ITurma;
import net.sourceforge.fenixedu.domain.Turma;
import net.sourceforge.fenixedu.applicationTier.Servicos.TestCaseCreateServices;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

public class CriarTurmaServicosTest extends TestCaseCreateServices {

    private InfoClass infoClass = null;

    public CriarTurmaServicosTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(CriarTurmaServicosTest.class);

        return suite;
    }

    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
        super.tearDown();
    }

    protected String getNameOfServiceToBeTested() {
        return "CriarTurma";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        this.ligarSuportePersistente(false);

        Object argsCriarTurma[] = { this.infoClass };

        return argsCriarTurma;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        this.ligarSuportePersistente(true);

        Object argsCriarTurma[] = { this.infoClass };

        return argsCriarTurma;
    }

    protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }

    private void ligarSuportePersistente(boolean existing) {

        ISuportePersistente sp = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            sp.iniciarTransaccao();

            ICursoPersistente icp = sp.getICursoPersistente();
            ICurso ic = icp.readBySigla("LEIC");

            IPersistentDegreeCurricularPlan ipccp = sp.getIPersistentDegreeCurricularPlan();
            IDegreeCurricularPlan ipcc = ipccp.readByNameAndDegree("plano1", ic);

            IPersistentExecutionYear ieyp = sp.getIPersistentExecutionYear();
            IExecutionYear iey = ieyp.readExecutionYearByName("2002/2003");

            IPersistentExecutionDegree icep = sp.getIPersistentExecutionDegree();
            ICursoExecucao ice = icep.readByDegreeCurricularPlanAndExecutionYear(ipcc, iey);

            IPersistentExecutionPeriod iepp = sp.getIPersistentExecutionPeriod();
            IExecutionPeriod iep = iepp.readByNameAndExecutionYear("2� Semestre", iey);

            ITurmaPersistente turmaPersistente = sp.getITurmaPersistente();
            ITurma turma = null;
            if (existing) {
                turma = turmaPersistente.readByNameAndExecutionDegreeAndExecutionPeriod("turma413", ice,
                        iep);
            } else {
                turma = new Turma("turma1", new Integer(1), ice, iep);
            }

            this.infoClass = Cloner.copyClass2InfoClass(turma);

            sp.confirmarTransaccao();

        } catch (ExcepcaoPersistencia excepcao) {
            try {
                sp.cancelarTransaccao();
            } catch (ExcepcaoPersistencia ex) {
                fail("ligarSuportePersistente: cancelarTransaccao");
            }
            fail("ligarSuportePersistente: confirmarTransaccao");
        }
    }
}