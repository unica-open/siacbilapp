/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto.ritornacompleto;

import java.util.ArrayList;
import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto.GenericAllegatoAttoAction;
import it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.RiCompletaAllegatoAttoModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RiCompletaAllegatoAttoPerElenchi;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Classe di Action per riportare lo stato dell'AllegatoAtto a completato.
 * 
 * @author NAZHA AHMAD, Valentina
 * @version 1.0.0 - 14/03/2016
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession("RiCompletaAllegatoAtto")
public class RiCompletaAllegatoAttoElenchiAction extends GenericAllegatoAttoAction<RiCompletaAllegatoAttoModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4092961577429854314L;
	
	@Override
	public void prepare() throws Exception {
		// Pulisco i messaggi
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		// Inizializzo l'azione
		setModel(null);
		super.prepare();
	}

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		final String methodName = "execute";
		checkCasoDUsoApplicabile(GenericAllegatoAttoAction.CDU_RICOMPLETA_ALLEGATO);
		// Caricamento liste
		try {
			log.debug(methodName, "Caricamento liste classificatori");
			caricaListaTipoAtto();
		} catch (WebServiceInvocationFailureException e) {
			log.error(methodName, "Errore nell'invocazione del caricamento di una lista: " + e.getMessage());
		}
		checkMetodoConclusoSenzaErrori();
		return SUCCESS;
	}

	/**
	 * Ottiene le liste degli elenchi per le invocazioni AJAX.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListeElenchi() {
		// Segnaposto per le chiamate AJAX
		impostaRiCompletabiliENonElaborabili(model.getListaElencoDocumentiAllegato());
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #completeStep1()}.
	 */
	public void prepareCompleteStep1() {
		model.setAttoAmministrativo(null);
		model.setTipoAtto(null);
		model.setStrutturaAmministrativoContabile(null);
	}
	
	/**
	 * Validazione per il metodo {@link #completeStep1()}.
	 */
	public void validateCompleteStep1() {
		final String methodName = "validateCompleteStep1";
		AttoAmministrativo aa = model.getAttoAmministrativo();
		checkNotNull(aa, "Atto amministrativo", true);
		checkCondition(aa.getAnno() != 0 && aa.getNumero() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno e numero atto sono obbligatorii"));
		
		if(hasErrori()) {
			log.debug(methodName, "Errori nella validazione dell'atto amministrativo");
			return;
		}
		// Controllo sull'esistenza del provvedimento
		try {
			checkProvvedimentoEsistente();
		} catch (ParamValidationException pve) {
			log.debug(methodName, "Errore nella validazione del provvedimento: " + pve.getMessage());
		}
	}
	
	/**
	 * Completamento dello step1.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String completeStep1() {
		return SUCCESS;
	}
	
	/**
	 * prepare step2 
	 */
	public void prepareStep2(){
		model.setDisabledButtons(Boolean.FALSE);
	}
	
	/**
	 * Ingresso nello step2.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String step2() {
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #riCompletaElenco()}.
	 */
	public void prepareRiCompletaElenco() {
		model.setListaUid(new ArrayList<Integer>());
	}
	
	/**
	 * RiCompleta gli elenchi selezionati.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String riCompletaElenco() {
		final String methodName = "riCompletaElenco";
		
		RiCompletaAllegatoAttoPerElenchi request = model.creaRequestRiCompletaAllegatoAttoPerElenchi();
		logServiceRequest(request);

		AsyncServiceResponse response = allegatoAttoService.riCompletaAllegatoAttoPerElenchiAsync(wrapRequestToAsync(request));
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		log.debug(methodName, "Elaborazione attivata per gli elenchi " + model.getListaUid());
		addInformazione(ErroreCore.ELABORAZIONE_ASINCRONA_AVVIATA.getErrore("RiCompleta elenco allegato atto", ""));
		// Ricerco il dettaglio dell'allegato e ne ricalcolo i dati
		try {
			ottieniDettaglioAllegatoEdImpostaElenchi();
		} catch (WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Errore nell'invocazione del servizio: " + wsife.getMessage());
			return INPUT;
		}
		model.setDisabledButtons(Boolean.TRUE);
		return SUCCESS;
	}
	
	/**
	 * Ottiene il dettaglio dell'allegato ed imposta gli elenchi nel model.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void ottieniDettaglioAllegatoEdImpostaElenchi() throws WebServiceInvocationFailureException {
		RicercaDettaglioAllegatoAttoResponse response = ricercaDettaglioAllegatoAtto(model.getAllegatoAtto());
		AllegatoAtto aa = response.getAllegatoAtto();
		model.setAllegatoAtto(aa);
		model.setListaElencoDocumentiAllegato(aa.getElenchiDocumentiAllegato());
		impostaRiCompletabiliENonElaborabili(aa.getElenchiDocumentiAllegato());
	}
	
	/**
	 * Validazione per il metodo {@link #riCompletaElenco()}.
	 */
	public void validateRiCompletaElenco() {
		checkCondition(!model.getListaUid().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("elenco da RiCompletare"));
	}
	
	/**
	 * Controlla che il provvedimento sia esistente ed univoco.
	 */
	private void checkProvvedimentoEsistente() {
		final String methodName = "checkProvvedimentoEsistente";
		// Invocazione del servizio
		RicercaProvvedimento request = model.creaRequestRicercaProvvedimento();
		logServiceRequest(request);
		RicercaProvvedimentoResponse response = provvedimentoService.ricercaProvvedimento(request);
		logServiceResponse(response);
		
		// Controllo di non aver errori
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return;
		}
		// Controllo di aver almeno un provvedimento
		checkCondition(!response.getListaAttiAmministrativi().isEmpty(), ErroreAtt.PROVVEDIMENTO_INESISTENTE.getErrore(), true);
		// Controllo di avere al piu' un provvedimento
		checkUnicoAttoAmministrativo(response.getListaAttiAmministrativi(), model.getAttoAmministrativo().getStrutturaAmmContabile(), true);
		
		AttoAmministrativo aa = response.getListaAttiAmministrativi().get(0);
		checkCondition(StatoOperativoAtti.DEFINITIVO.equals(aa.getStatoOperativoAtti()),
				ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("Gestione Allegato atto", "Definitivo"));
		checkCondition(aa.getAllegatoAtto() != null, ErroreCore.ENTITA_INESISTENTE.getErrore("Allegato atto", aa.getAnno() + "/" + aa.getNumero()), true);
		// Imposto l'atto nel model
		model.setAttoAmministrativo(aa);
		model.setStrutturaAmministrativoContabile(aa.getStrutturaAmmContabile());
		
		checkAllegatoAtto();
	}
	
	/**
	 * Controlla l'esistenza dell'allegato atto collegato all'atto amministrativo.
	 */
	private void checkAllegatoAtto() {
		final String methodName = "checkAllegatoAtto";
		RicercaDettaglioAllegatoAttoResponse response = null;
		try {
			response = ricercaDettaglioAllegatoAtto(model.getAttoAmministrativo().getAllegatoAtto());
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Errore nell'invocazione del servizio: " + wsife.getMessage());
			return;
		}
		// Valuto l'allegato
		AllegatoAtto allegatoAtto = response.getAllegatoAtto();
		checkCondition(!StatoOperativoAllegatoAtto.RIFIUTATO.equals(allegatoAtto.getStatoOperativoAllegatoAtto())
				&& !StatoOperativoAllegatoAtto.ANNULLATO.equals(allegatoAtto.getStatoOperativoAllegatoAtto()),
				ErroreCore.ENTITA_INESISTENTE.getErrore("Allegato per l'atto ", model.getAttoAmministrativo().getAnno() +"/"+ model.getAttoAmministrativo().getNumero()), true);
		
		checkCondition(StatoOperativoAllegatoAtto.CONVALIDATO.equals(allegatoAtto.getStatoOperativoAllegatoAtto())
				|| StatoOperativoAllegatoAtto.PARZIALMENTE_CONVALIDATO.equals(allegatoAtto.getStatoOperativoAllegatoAtto()),
				ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Allegato atto", allegatoAtto.getStatoOperativoAllegatoAtto().getDescrizione()));
				
		model.setAllegatoAtto(allegatoAtto);
		model.setListaElencoDocumentiAllegato(allegatoAtto.getElenchiDocumentiAllegato());
	}
	
	/**
	 * Popola le tabelle degli elenchi RiCompletabili e non RiCompletabili.
	 * 
	 * @param list la lista tramite cui effettuare i popolamenti
	 */
	private void impostaRiCompletabiliENonElaborabili(List<ElencoDocumentiAllegato> list) {
		List<ElencoDocumentiAllegato> riCompletabili = new ArrayList<ElencoDocumentiAllegato>();
		List<ElencoDocumentiAllegato> nonRicompletabili = new ArrayList<ElencoDocumentiAllegato>();
		
		for(ElencoDocumentiAllegato eda : list) {
			if(isNonRiCompletabileElenco(eda)) {
				nonRicompletabili.add(eda);
			} else {
				riCompletabili.add(eda);
			}
		}
		
		// Imposto nel model
		model.setListaRiCompletabili(riCompletabili);
		model.setListaNonElaborabili(nonRicompletabili);
		// Computo i totali
		model.computeTotali();
	}

	/**
	 * L'elenco &eacute; convalidato ovvero non Ricompletabile se:
	 * <ul>
	 *     <li>
	 *         {@link ElencoDocumentiAllegato#getStatoOperativoElencoDocumenti() statoOperativoElenco}
	 *         {@link StatoOperativoElencoDocumenti#COMPLETATO COMPLETATO}
	 *     </li>
	 *     <li>
	 *         {@link ElencoDocumentiAllegato#getTotaleDaPagare() importoDaPagare} = 0
	 *         e {@link ElencoDocumentiAllegato#getTotaleDaIncassare() importoDaIncassare} = 0
	 *     </li>
	 * </ul>
	 * 
	 * @param eda l'elenco da controllare
	 * 
	 * @return true se l'elenco sia da apporre nella lista dei non convalidabili; <code>false</code> in caso contrario
	 */
	private boolean isNonRiCompletabileElenco(ElencoDocumentiAllegato eda) {
		return  !StatoOperativoElencoDocumenti.COMPLETATO.equals(eda.getStatoOperativoElencoDocumenti())
					|| (eda.getTotaleDaIncassareNotNull().signum() == 0 && eda.getTotaleDaPagareNotNull().signum() == 0);
	}
	
	/**
	 * Ricerca il dettaglio dell'atto amministrativo fornito in input.
	 * 
	 * @param allegatoAtto l'atto da ricercare
	 * @return la response della ricerca di dettaglio
	 * @throws WebServiceInvocationFailureException nel caso di fallimento nell'invocazione del servizio
	 */
	private RicercaDettaglioAllegatoAttoResponse ricercaDettaglioAllegatoAtto(AllegatoAtto allegatoAtto) throws WebServiceInvocationFailureException {
		RicercaDettaglioAllegatoAtto request = model.creaRequestRicercaDettaglioAllegatoAtto(allegatoAtto);
		logServiceRequest(request);
		RicercaDettaglioAllegatoAttoResponse response = allegatoAttoService.ricercaDettaglioAllegatoAtto(request);
		logServiceResponse(response);
		
		// Se ho errori, esco
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
		}
		return response;
	}
	
}
