package net.sourceforge.fenixedu.util;

import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Executor;
import net.sourceforge.fenixedu.applicationTier.PersistenceException;
import net.sourceforge.fenixedu.applicationTier.Servico.assiduousness.ServicoAutorizacaoLer;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.ServicoSeguroEscreverPapelPessoa;
import net.sourceforge.fenixedu.applicationTier.Servico.person.ServicoSeguroLerTodasPessoas;
import net.sourceforge.fenixedu.domain.Person;

/**
 * 
 * @author Fernanda Quit�rio & Tania Pous�o
 */
public class EscreverPapelPessoa extends FenixUtil {

    public EscreverPapelPessoa() {
    }

    public static void main(String[] args) {

        // ler todas as pessoas existentes como funcionarios
        ServicoAutorizacaoLer servicoAutorizacaoLer = new ServicoAutorizacaoLer();
        ServicoSeguroLerTodasPessoas servicoSeguroLerTodasPessoas = new ServicoSeguroLerTodasPessoas(
                servicoAutorizacaoLer);
        try {
            Executor.getInstance().doIt(servicoSeguroLerTodasPessoas);
        } catch (NotExecuteException nee) {
            System.out.println(nee.getMessage());
        } catch (PersistenceException pe) {
            System.out.println(pe.getMessage());
        }

        ListIterator iteradorFuncionario = servicoSeguroLerTodasPessoas.getListaPessoas().listIterator();
        Person pessoa = null;
        while (iteradorFuncionario.hasNext()) {
            pessoa = (Person) iteradorFuncionario.next();

            // actualizar os papeis desta pessoa
            ServicoSeguroEscreverPapelPessoa servicoSeguroEscreverPapelPessoa = new ServicoSeguroEscreverPapelPessoa(
                    servicoAutorizacaoLer, pessoa);
            try {
                Executor.getInstance().doIt(servicoSeguroEscreverPapelPessoa);
            } catch (NotExecuteException nee) {
                System.out.println(nee.getMessage());
            } catch (PersistenceException pe) {
                System.out.println(pe.getMessage());
            }
        }
    }
}