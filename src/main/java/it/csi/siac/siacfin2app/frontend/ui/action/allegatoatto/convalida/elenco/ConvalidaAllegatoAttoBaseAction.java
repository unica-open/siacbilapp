/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto.convalida.elenco;

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
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto.GenericAllegatoAttoAction;
import it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.ConvalidaAllegatoAttoModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Classe di Action per la convalida dell'AllegatoAtto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/10/2014
 * @version 1.0.1 - 24/10/2014 - Trasformata in classe base per l'invocazione
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(ConvalidaAllegatoAttoBaseAction.MODEL_SESSION_NAME)
public class ConvalidaAllegatoAttoBaseAction extends GenericAllegatoAttoAction<ConvalidaAllegatoAttoModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2000836998347176437L;
	
	/** Nome del model per la sessione */
	protected static final String MODEL_SESSION_NAME = "ConvalidaAllegatoAtto";
	
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
		checkCasoDUsoApplicabile(GenericAllegatoAttoAction.CDU_CONVALIDA_ALLEGATO);
		// Caricamento liste
		try {
			log.debug(methodName, "Caricamento liste classificatori");
			caricaListaTipoAtto();
		} catch (WebServiceInvocationFailureException e) {
			log.error(methodName, "Errore nell'invocazione del caricamento di una lista: " + e.getMessage());
		}
		checkMetodoConclusoSenzaErrori();
		model.setConvalidaManuale(model.isGestioneManualeOrdinativo());

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
	 * Completamento dello step1.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String completeStep1() {
		return SUCCESS;
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
		
		checkAllegatoAtto(aa.getAllegatoAtto());
	}
	
	/**
	 * Controlla l'esistenza dell'allegato atto collegato all'atto amministrativo.
	 * @param allegatoAtto l'allegatoAtto
	 */
	private void checkAllegatoAtto(AllegatoAtto allegatoAtto) {
		final String methodName = "checkAllegatoAtto";
		RicercaDettaglioAllegatoAttoResponse response = null;
		try {
			response = ricercaDettaglioAllegatoAtto(allegatoAtto);
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Errore nell'invocazione del servizio: " + wsife.getMessage());
			return;
		}
		// Valuto l'allegato
		AllegatoAtto aa = response.getAllegatoAtto();
		checkCondition(!StatoOperativoAllegatoAtto.RIFIUTATO.equals(aa.getStatoOperativoAllegatoAtto())
				&& !StatoOperativoAllegatoAtto.ANNULLATO.equals(aa.getStatoOperativoAllegatoAtto()),
				ErroreCore.ENTITA_INESISTENTE.getErrore("Allegato per l'atto ", aa.getAttoAmministrativo().getAnno() + "/" + aa.getAttoAmministrativo().getNumero()), true);
		
		checkCondition(StatoOperativoAllegatoAtto.COMPLETATO.equals(aa.getStatoOperativoAllegatoAtto())
				|| StatoOperativoAllegatoAtto.PARZIALMENTE_CONVALIDATO.equals(aa.getStatoOperativoAllegatoAtto()),
				ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Allegato atto", aa.getStatoOperativoAllegatoAtto().getDescrizione()));
		// Se lo StatoOperativoElaborazioniAsincrone e' IN ELABORAZIONE viene emesso l'errore
		// <COR_ERR_0036: Elemento gia' in elaborazione (entita' = 'Allegato all'atto', chiave=TIPO PROVV.)
		// TODO
		
		model.setAllegatoAtto(aa);
		model.setListaElencoDocumentiAllegato(aa.getElenchiDocumentiAllegato());
	}

	/**
	 * Ricerca il dettaglio dell'atto amministrativo fornito in input.
	 * 
	 * @param allegatoAtto l'atto da ricercare
	 * @return la response della ricerca di dettaglio
	 * @throws WebServiceInvocationFailureException nel caso di fallimento nell'invocazione del servizio
	 */
	protected RicercaDettaglioAllegatoAttoResponse ricercaDettaglioAllegatoAtto(AllegatoAtto allegatoAtto) throws WebServiceInvocationFailureException {
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
	
	/**
	 * Preparazione per il metodo {@link #completeStep1DaRicercaAllegatoAtto()}
	 * @throws FrontEndBusinessException in caso di eccezione nella preparazione della action
	 */
	public void prepareCompleteStep1DaRicercaAllegatoAtto() throws FrontEndBusinessException {
		setModel(null);
		try {
			super.prepare();
		} catch (Exception e) {
			throw new FrontEndBusinessException("Errore nella preparazione della convalida allegato atto: " + e.getMessage(), e);
		}
	}

	/**
	 * Completamento dello step1 a partire dalla ricerca dell'allegato atto
	 * @return una stringa corrispondente al risultato dell'invocazione
	 * @see #completeStep1()
	 */
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String completeStep1DaRicercaAllegatoAtto() {
		// Il breadcrumb e' necessario per la gestione dell'ancora
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #completeStep1DaRicercaAllegatoAtto()}.
	 */
	public void validateCompleteStep1DaRicercaAllegatoAtto() {
		final String methodName = "validateCompleteStep1DaRicercaAllegatoAtto";
		checkCondition(model.getUidAllegatoAtto() != null && model.getUidAllegatoAtto().intValue() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("allegato"), true);
		
		// Controllo sull'esistenza dell'allegato atto
		AllegatoAtto allegatoAtto = new AllegatoAtto();
		allegatoAtto.setUid(model.getUidAllegatoAtto().intValue());
		try {
			checkAllegatoAtto(allegatoAtto);
		} catch (ParamValidationException pve) {
			log.debug(methodName, "Errore nella validazione dell'allegato atto: " + pve.getMessage());
			return;
		}
		allegatoAtto = model.getAllegatoAtto();
		model.setAttoAmministrativo(allegatoAtto.getAttoAmministrativo());
		model.setStrutturaAmministrativoContabile(allegatoAtto.getAttoAmministrativo().getStrutturaAmmContabile());
	}

}
