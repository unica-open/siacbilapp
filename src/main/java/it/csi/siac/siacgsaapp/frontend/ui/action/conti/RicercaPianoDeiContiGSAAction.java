/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.conti;

import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.conti.RicercaPianoDeiContiAction;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaContoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaContoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoFigli;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoFigliResponse;
import it.csi.siac.siacgsaapp.frontend.ui.model.conti.RicercaPianoDeiContiGSAModel;

/**
 * Classe di action per la ricerca di un conto
 * 
 * @version 1.0.0 - 09/03/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class RicercaPianoDeiContiGSAAction extends RicercaPianoDeiContiAction<RicercaPianoDeiContiGSAModel> {

	private static final long serialVersionUID = -6579757634075287520L;
	
	@Override
	public void prepare() throws Exception {
		cleanErroriMessaggiInformazioni();
		log.debug("prepare", "PREPARE");
	}
	
	/**
	 * Preaparazione del model.
	 * 
	 * @throws FrontEndBusinessException nel caso di errore nel caricamento del model
	 */
	private void prepareModel() throws FrontEndBusinessException {
		if(sessionHandler.getParametro(BilSessionParameter.RIENTRO) == null || !Boolean.FALSE.equals(sessionHandler.getParametro(BilSessionParameter.CONTO_DA_PULIRE))) {
			setModel(null);
		}
		try {
			super.prepare();
		} catch(Exception e) {
			throw new FrontEndBusinessException("Errore nell'impostazione del model", e);
		}
	}
	
	@Override
	public void prepareExecute() throws Exception {
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		prepareModel();
	}
	
	
	@Override
	public String execute() {
		checkCasoDUsoApplicabile(model.getTitolo());
		return SUCCESS;
	}
	
	
	/**
	 * Preparazione per il metodo {@link #landOnPage()}.
	 * 
	 * @throws FrontEndBusinessException nel caso di errore nel caricamento del model
	 */
	public void prepareLandOnPage() throws FrontEndBusinessException {
		prepareModel();
	}
	
	/**
	 * Caricamento delle variabili di utilit&agrave; e atterraggio sulla pagina.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String landOnPage(){
		caricaListaClassi();
		leggiEventualiErroriAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiInformazioniAzionePrecedente();
		return SUCCESS;
	}
	
	/** Ricerca del conto secondo i parametri passati in input
	 * 
	 * @return SUCCESS
	 */
	public String effettuaRicerca(){
		sessionHandler.setParametro(BilSessionParameter.CONTO_DA_PULIRE, null);
		String methodName = "effettuaRicerca";
		validaRicerca();
		if(hasErrori()){
			log.debug(methodName, "Validazione fallita");
			return SUCCESS;
		}
//		ricerca del conto
		RicercaSinteticaContoFigli reqRSCF = model.creaRequestRicercaSinteticaContoFigli();
		RicercaSinteticaContoFigliResponse resRSCF = contoService.ricercaSinteticaContoFigli(reqRSCF);
		if(resRSCF.hasErrori()){
			log.info(methodName, "Errori nella ricerca sintetica del conto");
			addErrori(resRSCF);
			return SUCCESS;
		}
		if(resRSCF.getContoPadre() == null) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return SUCCESS;
		}
		
		impostaDatiNelModel(resRSCF);
		sessionHandler.setParametro(BilSessionParameter.CONTO_DA_PULIRE, Boolean.FALSE);
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_CONTO, reqRSCF);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_FIGLI_CONTO, resRSCF.getContiFiglio());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		
		return SUCCESS;
	}
	
	/**
	 * Metodo per l'azione di redirezione all'inserimento di un conto figlio
	 * 
	 * @return SUCCESS
	 */
	public String inserisciFiglio(){
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	
	/**
	 * Metodo per l'azione di redirezione alla consultazione del conto
	 * 
	 * @return SUCCESS
	 */
	public String consulta(){
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	
	
	/**
	 * Metodo per l'azione di redirezione all'aggiornamento del conto
	 * 
	 * @return SUCCESS
	 */
	public String aggiorna(){
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	
	/**
	 * Annulla il conto selezionato
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String annulla(){
		AnnullaConto reqAC = model.creaRequestAnnullaConto();
		AnnullaContoResponse resAC = contoService.annullaConto(reqAC);
		if(resAC.hasErrori()){
			addErrori(resAC);
			model.setIsTabellaVisibile(Boolean.TRUE);
			return INPUT;
		}
		model.setIsTabellaVisibile(Boolean.FALSE);
		model.setConto(null);
		addInformazione(new Informazione("COR_INF_0006", "Operazione effettuata correttamente"));
		return SUCCESS;
	}
	
	/**
	 * Elimina il conto selezionato
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String elimina(){
		EliminaConto reqEC = model.creaRequestEliminaConto();
		EliminaContoResponse resEC = contoService.eliminaConto(reqEC);
		if(resEC.hasErrori()){
			addErrori(resEC);
			model.setIsTabellaVisibile(Boolean.TRUE);
			return INPUT;
		}
		model.setIsTabellaVisibile(Boolean.FALSE);
		model.setConto(null);
		addInformazione(new Informazione("COR_INF_0006", "Operazione effettuata correttamente"));
		return SUCCESS;
	}

	/**
	 * Controlla se la gestione sia consentita.
	 * 
	 * @return <code>true</code> se la gestione &eacute; consentita; <code>false</code> altrimenti
	 */
	@Override
	protected boolean isGestioneConsentita() {
		List<AzioneConsentita> listaAzioni = sessionHandler.getAzioniConsentite();
		Boolean azioneConsentita = AzioniConsentiteFactory.isConsentito(AzioniConsentite.PIANO_DEI_CONTI_GESTISCI_PIANO_DEI_CONTI_GSA, listaAzioni);
		FaseBilancio faseBilancio = sessionHandler.getParametro(BilSessionParameter.FASE_BILANCIO);
		return  Boolean.TRUE.equals(azioneConsentita) && !FaseBilancio.CHIUSO.equals(faseBilancio);
	}
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbasegengsaapp.frontend.ui.action.conti.BaseInserisciAggiornaPianoDeiContiAction#getBilSessionParameterListaClassiPiano()
	 */
	@Override
	protected BilSessionParameter getBilSessionParameterListaClassiPiano() {
		
		return BilSessionParameter.LISTA_CLASSE_PIANO_GSA;
	}

}
