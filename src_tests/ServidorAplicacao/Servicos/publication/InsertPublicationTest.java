/*
 * Created on 27/Out/2003
 *
 */

package net.sourceforge.fenixedu.applicationTier.Servicos.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAuthor;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.publication.InsertPublication;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceTestCase;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Carlos Pereira & Francisco Passos
 *  
 */

public class InsertPublicationTest extends ServiceTestCase {

    /**
     * @param testName
     */
    public InsertPublicationTest(java.lang.String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "InsertPublication";
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see ServiceNeedsAuthenticationTestCase#getApplication()
     */
    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }


    protected String getDataSetFilePath() {
        return "etc/testDataSetForPublications.xml";
    }

    /** ********** Inicio dos testes ao servi�o************* */

    /*
     * Successfully inserts a publication
     */
    public void testInsertPublicationOK() {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            
            List infoAuthorsList = new ArrayList();
            InfoAuthor infoAuthor1 = new InfoAuthor();
            infoAuthor1.setIdInternal(new Integer(2));
            infoAuthor1.setAuthor("Yombero");
            
            //Another Author
            InfoAuthor infoAuthor2 = new InfoAuthor();
            infoAuthor2.setIdInternal(new Integer(4));
            infoAuthor2.setAuthor("NewAuthor");
            
            //Another Author
            InfoAuthor infoAuthor3 = new InfoAuthor();
            infoAuthor3.setIdInternal(new Integer(5));
            infoAuthor3.setKeyPerson(new Integer(2));
            
            InfoPerson infoPerson = new InfoPerson ();
            infoPerson.setIdInternal(new Integer(2));
            infoPerson.setUsername("mau");
            infoPerson.setNumeroDocumentoIdentificacao("222222222");
            infoPerson.setPassword("pass");
            
            infoAuthor3.setInfoPessoa(infoPerson);
            
            infoAuthorsList.add(infoAuthor1);
            infoAuthorsList.add(infoAuthor2);
            infoAuthorsList.add(infoAuthor3);
            
            InfoPublication infoPublication = new InfoPublication();
            infoPublication.setIdInternal(new Integer(6));
            infoPublication.setTitle("Publication6");
            infoPublication.setVolume("2");
            infoPublication.setYear("2000");
            infoPublication.setKeyPublicationType(new Integer(1));
            infoPublication.setInfoPublicationAuthors(infoAuthorsList);
            
            sp.iniciarTransaccao();
            InsertPublication ip = new InsertPublication();
            ip.run(infoPublication);
            sp.confirmarTransaccao();
            
            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets_templates/servicos/publication/testInsertPublicationOKExpectedDataSet.xml");

            System.out.println(getNameOfServiceToBeTested() + " was SUCCESSFULLY run by test: "
                    + "testInsertPublicationOK");
        } catch (Exception e) {
            fail("Inserting Publication " + e);
        }
    }
 
}