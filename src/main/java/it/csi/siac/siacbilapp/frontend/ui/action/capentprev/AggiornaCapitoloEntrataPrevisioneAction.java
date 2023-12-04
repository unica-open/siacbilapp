/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capentprev;

import java.math.BigDecimal;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import xyz.timedrain.arianna.plugin.BreadCrumbTrail;
import xyz.timedrain.arianna.plugin.Crumb;
/*
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.softwareforge.struts2.breadcrumb.BreadCrumbTrail;
import org.softwareforge.struts2.breadcrumb.Crumb;
*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.commons.CapitoloEntrataAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capentprev.AggiornaCapitoloEntrataPrevisioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamentoCapitoloEntrata;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamentoFactory;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloPerAggiornamentoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloPerAggiornamentoCapitoloResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.CategoriaCapitoloEnum;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonapp.handler.session.CommonSessionParameter;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;

/**
 * Classe di Action per la gestione del Capitolo di Entrata Previsione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 02/08/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaCapitoloEntrataPrevisioneAction extends CapitoloEntrataAction<AggiornaCapitoloEntrataPrevisioneModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4839435200517465772L;
	
	@Autowired private transient CapitoloEntrataPrevisioneService capitoloEntrataPrevisioneService;
	
	/** Indica se sia necessario tornare indietro di due crumbs a termine dell'aggiornamento */
	private boolean tornareDiDueIndietroDopoAggiornamento;
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		
		// SIAC-4308
		checkIsDecentrato();
		
		caricaListaCodifiche(BilConstants.CODICE_CAPITOLO_ENTRATA_PREVISIONE);
		ControllaClassificatoriModificabiliCapitoloResponse responseControlloClassificatoriEditabili = 
				sessionHandler.getParametroXmlType(BilSessionParameter.EDITABILITA_CLASSIFICATORI, ControllaClassificatoriModificabiliCapitoloResponse.class);
		
		// Il controllo è necessario in caso di errore
		if(responseControlloClassificatoriEditabili != null) {
			log.debug(methodName, "Valuto l'editabilita dei campi");
			model.valutaModificabilitaClassificatori(responseControlloClassificatoriEditabili, false);
		}
		
		ControllaAttributiModificabiliCapitoloResponse responseControlloAttributiEditabili = 
				sessionHandler.getParametroXmlType(BilSessionParameter.EDITABILITA_ATTRIBUTI, ControllaAttributiModificabiliCapitoloResponse.class);
		
		// Il controllo è necessario in caso di errore
		if(responseControlloAttributiEditabili != null) {
			log.debug(methodName, "Valuto l'editabilita dei campi");
			model.valutaModificabilitaAttributi(responseControlloAttributiEditabili, false);
		}
		
		log.debugEnd(methodName, "");
	}
	
	/**
	 * Controllo sul decentrato
	 */
	private void checkIsDecentrato() {
		final String methodName = "checkIsDecentrato";
		Boolean decentrato = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.CAPITOLO_USCITA_PREVISIONE_AGGIORNA_DECENTRATO, sessionHandler.getAzioniConsentite());
		log.debug(methodName, "Decentrato? " + decentrato);
		model.setDecentrato(decentrato);
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		String methodName = "execute";
		
		checkCasoDUsoApplicabile(model.getTitolo());
		log.debug(methodName, "Capitolo da aggiornare - uid: " + model.getUidDaAggiornare());

		/* Ricerca del capitolo */
		log.debug(methodName, "Creazione della request");
		RicercaDettaglioCapitoloEntrataPrevisione requestRicercaDettaglio = model.creaRequestRicercaDettaglioCapitoloEntrataPrevisione();
		logServiceRequest(requestRicercaDettaglio);
		
		log.debug(methodName, "Richiamo il WebService di ricerca dettaglio");
		RicercaDettaglioCapitoloEntrataPrevisioneResponse responseRicercaDettaglio = capitoloEntrataPrevisioneService.ricercaDettaglioCapitoloEntrataPrevisione(requestRicercaDettaglio);
		log.debug(methodName, "Richiamato il WebService di ricerca dettaglio");
		logServiceResponse(responseRicercaDettaglio);
		
		if(responseRicercaDettaglio.hasErrori()) {
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, responseRicercaDettaglio);
			return INPUT;
		}
		log.debug(methodName, "Impostazione dei dati nel model");
		model.impostaDatiDaResponse(responseRicercaDettaglio);
		log.debug(methodName, "Caricamento delle ulteriori liste");
		caricaListaCodificheAggiornamento();

		
		StatoOperativoElementoDiBilancio stato = responseRicercaDettaglio.getCapitoloEntrataPrevisione().getStatoOperativoElementoDiBilancio();
		if (!StatoOperativoElementoDiBilancio.VALIDO.equals(stato) && !StatoOperativoElementoDiBilancio.PROVVISORIO.equals(stato)){
			addErrore(ErroreBil.CAPITOLO_NON_AGGIORNABILE_PERCHE_STATO_INCONGRUENTE.getErrore(""));
			return INPUT;
		}
		
		//TODO: martellata, per favore mettere a posto
		if(StatoOperativoElementoDiBilancio.VALIDO.equals(stato)){
		
			RicercaVariazioniCapitoloPerAggiornamentoCapitolo requestRicercaVariazioni = model.creaRequestRicercaVariazioniCapitoloPerAggiornamentoCapitolo(model.getUidDaAggiornare());
			logServiceRequest(requestRicercaVariazioni);
			RicercaVariazioniCapitoloPerAggiornamentoCapitoloResponse responseRicercaVariazioni = capitoloService.ricercaVariazioniCapitoloPerAggiornamentoCapitolo(requestRicercaVariazioni);
			logServiceResponse(responseRicercaVariazioni);
			
			if(responseRicercaVariazioni.hasErrori()) {
				log.debug(methodName, createErrorInServiceInvocationString(RicercaVariazioniCapitoloPerAggiornamentoCapitolo.class, responseRicercaVariazioni));
				addErrori(responseRicercaVariazioni);
				return INPUT;
			}
			
			if(responseRicercaVariazioni.getNumeroVariazioniImporti() > 0 || responseRicercaVariazioni.getNumeroVariazioniCodifiche() > 0) {
				String str = getStringaErroreVariazioniNonDefinitiveCollegate(responseRicercaVariazioni.getVariazioneImportiNumero(), responseRicercaVariazioni.getVariazioneCodificheNumero());
				addMessaggio(ErroreBil.CAPITOLO_CON_VARIAZIONI_NON_DEFINITIVE_COLLEGATE.getErrore(str));
				//return INPUT;
			}
		}
		
		log.debug(methodName, "Metto in sessione gli elementi già presenti nel model");
		sessionHandler.setParametro(BilSessionParameter.CLASSIFICATORI_AGGIORNAMENTO, ClassificatoreAggiornamentoFactory.getInstance(model));
		
		ControllaClassificatoriModificabiliCapitoloResponse responseClassificatoriModificabili = 
				ottieniResponseControllaClassificatoriModificabiliCapitolo(model.getCapitoloEntrataPrevisione());
		
		log.debug(methodName, "Valuto l'editabilita dei campi");
		model.valutaModificabilitaClassificatori(responseClassificatoriModificabili, false);
		
		ControllaAttributiModificabiliCapitoloResponse responseAttributiModificabili =
				ottieniResponseControllaAttributiModificabiliCapitolo(model.getCapitoloEntrataPrevisione());
		log.debug(methodName, "Valuto l'editabilita degli attributi");
		model.valutaModificabilitaAttributi(responseAttributiModificabili, false);
		
		// Imposto la response per i classificatori modificabili in sessione
		sessionHandler.setParametroXmlType(BilSessionParameter.EDITABILITA_CLASSIFICATORI, responseClassificatoriModificabili);
		
		return SUCCESS;
	}
	//SIAC-8003
	public boolean checkCapitoloCategoria()
	{
		if(CategoriaCapitoloEnum.categoriaIsFPV(model.getCapitoloEntrataPrevisione().getCategoriaCapitolo().getCodice()) 
			|| CategoriaCapitoloEnum.AAM.getCodice().equals(model.getCapitoloEntrataPrevisione().getCategoriaCapitolo().getCodice())) {
			 return true;
		}else {
			return false;
		}
	}
	/**
	 * Aggiorna il Capitolo di Entrata Previsione.
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String aggiornaCDU() {
		final String methodName = "aggiornaCDU";
		/* Valorizzo i classificatori a partire dalla sessione */
		log.debug(methodName, "Valorizzo i classificatori");
		
		// Carico i dati dalla sessione
		log.debug(methodName, "Caricamento dei dati originali dalla sessione");
		ClassificatoreAggiornamentoCapitoloEntrata classificatoreAggiornamento = sessionHandler.getParametro(BilSessionParameter.CLASSIFICATORI_AGGIORNAMENTO, ClassificatoreAggiornamentoCapitoloEntrata.class);
		
		// SIAC-5582: Forzo il flag accertato per cassa a false nel caso in cui NON sia gestibile
		if(!model.isFlagAccertatoPerCassaVisibile()) {
			model.getCapitoloEntrataPrevisione().setFlagAccertatoPerCassa(Boolean.FALSE);
		}
		
		//task-244
		if(model.getCapitoloEntrataPrevisione().getCategoriaCapitolo().getUid() >0)
			model.getCapitoloEntrataPrevisione().setCategoriaCapitolo(caricaCategoriaCapitolo(model.getCapitoloEntrataPrevisione().getCategoriaCapitolo().getUid()));
						
		//task-244
		if(CategoriaCapitoloEnum.categoriaIsFPV(model.getCapitoloEntrataPrevisione().getCategoriaCapitolo().getCodice()) || 
				model.getCapitoloEntrataPrevisione().getCategoriaCapitolo().getCodice().equals(CategoriaCapitoloEnum.AAM.getCodice()) ) {
			model.getCapitoloEntrataPrevisione().setFlagImpegnabile(null);
		}
		
		log.debug(methodName, "Creazione della request");
		AggiornaCapitoloDiEntrataPrevisione request = model.creaRequestAggiornaCapitoloDiEntrataPrevisione(classificatoreAggiornamento);
		logServiceRequest(request);
		
		log.debug(methodName,"Richiamo il WebService di aggiornamento");
		AggiornaCapitoloDiEntrataPrevisioneResponse response = capitoloEntrataPrevisioneService.aggiornaCapitoloDiEntrataPrevisione(request);
		log.debug(methodName,"Richiamato il WebService di aggiornamento");
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.error(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			caricaListaCodificheAggiornamento();
			return INPUT;
		}
		log.debug(methodName, "Capitolo di Entrata di Previsione aggiornato correttamente");

		/* Ripulisco la sessione */
		// Mantengo i risultati di ricerca per tornare alla pagina dei risultati
		// Mantengo la posizione di rientro
		sessionHandler.cleanSafelyExcluding(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_ENTRATA_PREVISIONE, BilSessionParameter.RIENTRO_POSIZIONE_START);
		
		/* 
		 * Imposto in sessione il dato di rientro dall'aggiornamento, 
		 * utile nel caso in cui si debba rientrare sui risultati di ricerca
		 */
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, "S");
		
		// Ripopolo gli eventuali campi che sono disabilitati
		model.setParametriDisabilitati(classificatoreAggiornamento);
		
		/* Imposta la necessita' di tornare indietro due volte a termine dell'esecuzione */
		if(!model.isDaAggiornamento()) {
			tornareDiDueIndietroDopoAggiornamento = true;
		}
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		
		return SUCCESS;
	}
	
	@Override
	public void validate() {
		CapitoloEntrataPrevisione cep = model.getCapitoloEntrataPrevisione();
		if(cep != null && !model.isGestioneUEB()){
			cep.setNumeroUEB(Integer.valueOf(1));
		}
		// Carico i classificatori
		model.caricaClassificatoriDaSessione(sessionHandler);
		// Controllo che il capitolo ci sia
		checkNotNull(cep, "Capitolo", true);
		
		checkNotNull(cep.getNumeroCapitolo(), "Capitolo");
		checkNotNull(cep.getNumeroArticolo(), "Articolo");
		checkNotNull(cep.getNumeroUEB(), "UEB");
		checkNotNullNorEmpty(cep.getDescrizione(), "Descrizione");
		checkCondition(!model.isCategoriaCapitoloEditabile() || (cep.getCategoriaCapitolo() != null && cep.getCategoriaCapitolo().getUid() != 0), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo capitolo"));
		
		// CR-2204: controllo la classificazione di bilancio solo per i tipi standard
		if(isCategoriaCapitoloStandard(cep.getCategoriaCapitolo())) {
			checkCondition(!model.isTitoloEntrataEditabile() || (model.getTitoloEntrata() != null && model.getTitoloEntrata().getUid() != 0), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Titolo"));
			checkCondition(!model.isTipologiaTitoloEditabile() || (model.getTipologiaTitolo() != null && model.getTipologiaTitolo().getUid() != 0), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipologia"));
			checkCondition(!model.isCategoriaTipologiaTitoloEditabile() || (model.getCategoriaTipologiaTitolo() != null && model.getCategoriaTipologiaTitolo().getUid() != 0), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Categoria"));
			checkCondition(!model.isElementoPianoDeiContiEditabile() || (model.getElementoPianoDeiConti() != null && model.getElementoPianoDeiConti().getUid() != 0), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Elemento del Piano dei Conti"));
		}
		
		checkCondition(!model.isStrutturaAmministrativoContabileEditabile() || (model.getStrutturaAmministrativoContabile() != null && model.getStrutturaAmministrativoContabile().getUid() != 0),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Struttura Amministrativa Responsabile"));
		
		validaImportoCapitoloEntrataPrevisione();
	}
	
	/**
	 * Unifica la validazione per gli Importi del Capitolo di Entrata Previsione.
	 */
	private void validaImportoCapitoloEntrataPrevisione() {
		validaImportoCapitolo(model.getImportiCapitoloEntrataPrevisione0(), 0, true, true);
		validaImportoCapitolo(model.getImportiCapitoloEntrataPrevisione1(), 1, false, true);
		validaImportoCapitolo(model.getImportiCapitoloEntrataPrevisione2(), 2, false, true);
	}
	
	/**
	 * Ottiene il crumb precedente a partire dallo stack dei breadcrumbs.
	 * 
	 * @return the previousCrumb
	 */
	@Override
	public Crumb getPreviousCrumb() {
		// Fallback in caso di mancata necessità
		if(!tornareDiDueIndietroDopoAggiornamento) {
			return super.getPreviousCrumb();
		}
		
		final String methodName = "getPreviousAction";
		// Il valore di default quando non vi sono precedenti actions è null
		// Qualora sulla pagina si trovi un null, l'indietro deve redirigere verso il cruscotto
		Crumb previousCrumb = null;
		BreadCrumbTrail trail = sessionHandler.getParametro(CommonSessionParameter.BREADCRUMB_TRAIL, BreadCrumbTrail.class);
		try {
			int numeroCrumbs = trail.getCrumbs().size();
			// Ottengo DUE crumb precedenti rispetto a quello attuale
			previousCrumb = trail.getCrumbs().get(numeroCrumbs - 3);
		} catch(Exception e) {
			// Non dovrebbe succedere
			log.debug(methodName, "Il trail delle azioni precedenti non contiene sufficienti crumbs");
		}
		return previousCrumb;
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioCompatibile = 
				FaseBilancio.PREVISIONE.equals(faseBilancio) ||
				FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio);
		
		if(!faseDiBilancioCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	/**
	 * Metodo di utilit&agrave; per la validazione degli importi per il Capitolo.
	 * 
	 * @param importi                l'Importo da validare
	 * @param annoRispettoAEsercizio lo scostamento dell'anno dell'Importo rispetto all'anno di esercizio
	 * @param controlloGlobale       variabile che definisce se effettuare un controllo su tutti i parametri o solo su alcuni
	 * @param forceCassaCoherence    forza la coerenza dell'importo di cassa: <code>cassa &le; competenza + residuo</code>
	 */
	@Override
	protected void validaImportoCapitolo(ImportiCapitolo importi, int annoRispettoAEsercizio, boolean controlloGlobale, boolean forceCassaCoherence) {
		int anno = model.getAnnoEsercizioInt() + annoRispettoAEsercizio;
		if(importi == null) {
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Competenza per anno " + anno));
			if(controlloGlobale) {
				addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Residui per anno " + anno));
				addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Cassa per anno " + anno));
			}
			return;
		}
		
		// Gli importi sono valorizzati
		checkImporto(importi.getStanziamento(), "Competenza", anno);
		if(controlloGlobale) {
			checkImporto(importi.getStanziamentoResiduo(), "Residui", anno);
			checkImporto(importi.getStanziamentoCassa(), "Cassa", anno);
			if(forceCassaCoherence && importi.getStanziamento() != null && importi.getStanziamentoResiduo() != null && importi.getStanziamentoCassa() != null
					&& (importi.getStanziamentoResiduo().compareTo(BigDecimal.ZERO)>0 || importi.getStanziamento().compareTo(BigDecimal.ZERO)>0 )) {
				checkCondition(importi.getStanziamentoCassa().subtract(importi.getStanziamento()).subtract(importi.getStanziamentoResiduo()).signum() <= 0,
						ErroreCore.VALORE_NON_CONSENTITO.getErrore("Cassa per anno " + anno, ": deve essere inferiore o uguale alla somma di competenza e residuo ("
							+ FormatUtils.formatCurrency(importi.getStanziamento().add(importi.getStanziamentoResiduo())) + ")"));
			}
		}
	}
}
