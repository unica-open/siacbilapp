/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capuscprev;

import java.math.BigDecimal;
import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.softwareforge.struts2.breadcrumb.BreadCrumbTrail;
import org.softwareforge.struts2.breadcrumb.Crumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.commons.CapitoloUscitaAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capuscprev.AggiornaCapitoloUscitaPrevisioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamentoCapitoloUscita;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamentoFactory;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.ComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloPerAggiornamentoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloPerAggiornamentoCapitoloResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonapp.handler.session.CommonSessionParameter;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaImpegniSubImpegni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaImpegniSubimpegniResponse;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaImpSub;

/**
 * Classe di Action per la gestione del Capitolo di Uscita Previsione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 12/07/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaCapitoloUscitaPrevisioneAction extends CapitoloUscitaAction<AggiornaCapitoloUscitaPrevisioneModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6562542408458341509L;
	
	@Autowired private transient CapitoloUscitaPrevisioneService capitoloUscitaPrevisioneService;
	
	@Autowired private transient ComponenteImportiCapitoloService componenteImportiCapitoloService;
	
	@Autowired private transient MovimentoGestioneService movimentoGestioneService;
	
	/** Indica se sia necessario tornare indietro di due crumbs a termine dell'aggiornamento */
	private boolean tornareDiDueIndietroDopoAggiornamento;
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		
		// SIAC-4308: controllo sul decentrato
		checkIsDecentrato();
		
		caricaListaCodifiche(BilConstants.CODICE_CAPITOLO_USCITA_PREVISIONE);
		ControllaClassificatoriModificabiliCapitoloResponse responseControlloClassificatoriEditabili = sessionHandler.getParametroXmlType(BilSessionParameter.EDITABILITA_CLASSIFICATORI, ControllaClassificatoriModificabiliCapitoloResponse.class);
		ControllaAttributiModificabiliCapitoloResponse responseControlloAttributiEditabili = sessionHandler.getParametroXmlType(BilSessionParameter.EDITABILITA_ATTRIBUTI, ControllaAttributiModificabiliCapitoloResponse.class);
		
		// Il controllo è necessario in caso di errore
		if(responseControlloClassificatoriEditabili != null) {
			log.debug(methodName, "Valuto l'editabilita dei campi");
			model.valutaModificabilitaClassificatori(responseControlloClassificatoriEditabili, false);
		}
		if(responseControlloAttributiEditabili != null) {
			log.debug(methodName, "Valuto l'editabilita degli attributi");
			model.valutaModificabilitaAttributi(responseControlloAttributiEditabili, false);
		}
		
		log.debugEnd(methodName, "");
	}

	/**
	 * Controllo sul decentrato
	 */
	private void checkIsDecentrato() {
		final String methodName = "checkIsDecentrato";
		Boolean decentrato = AzioniConsentiteFactory.isConsentito(AzioniConsentite.CAPITOLO_USCITA_PREVISIONE_AGGIORNA_DECENTRATO, sessionHandler.getAzioniConsentite());
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
		RicercaDettaglioCapitoloUscitaPrevisione requestRicercaDettaglio = model.creaRequestRicercaDettaglioCapitoloUscitaPrevisione();
		logServiceRequest(requestRicercaDettaglio);
		
		
		
		log.debug(methodName, "Richiamo il WebService di ricerca dettaglio");
		RicercaDettaglioCapitoloUscitaPrevisioneResponse responseRicercaDettaglio = capitoloUscitaPrevisioneService.ricercaDettaglioCapitoloUscitaPrevisione(requestRicercaDettaglio);
		log.debug(methodName, "Richiamato il WebService di ricerca dettaglio");
		logServiceResponse(responseRicercaDettaglio);
		
		if(responseRicercaDettaglio.hasErrori()) {
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, responseRicercaDettaglio);
			return INPUT;
		}
		
		//SIAC-6884 mi serve per la visualizzazione delle componenti
		if(responseRicercaDettaglio.getCapitoloUscitaPrevisione()!= null &&
				responseRicercaDettaglio.getCapitoloUscitaPrevisione().getClassificatoriGenerici() != null &&
				!responseRicercaDettaglio.getCapitoloUscitaPrevisione().getClassificatoriGenerici().isEmpty()){		
			
			for(ClassificatoreGenerico cg : responseRicercaDettaglio.getCapitoloUscitaPrevisione().getClassificatoriGenerici()){
				if(cg.getTipoClassificatore().getCodice().equals(TipologiaClassificatore.CLASSIFICATORE_3.name())){
					model.setCapitoloFondino(cg.getCodice().equals("01") ? true : false);
				}				
			}
		}else{
			model.setCapitoloFondino(false);
		}

//		//RICERCA SE IL CAPITOLO HA IMPEGNI SIAC-6884 PROVA. DA TOGLIERE DALLA ACTION LA COSTRUZIONE DELLA REQUEST FIXME e TODO
//		RicercaSinteticaImpegniSubImpegni request = new RicercaSinteticaImpegniSubImpegni();
//		request.setAnnoBilancio(responseRicercaDettaglio.getCapitoloUscitaPrevisione().getAnnoCapitolo());
//		request.setEnte(sessionHandler.getEnte());
//		request.setRichiedente(sessionHandler.getRichiedente());		
//		ParametroRicercaImpSub parametroRicercaImpSub = new ParametroRicercaImpSub();
//		parametroRicercaImpSub.setUidCapitolo(responseRicercaDettaglio.getCapitoloUscitaPrevisione().getUid());
//		parametroRicercaImpSub.setAnnoEsercizio(responseRicercaDettaglio.getCapitoloUscitaPrevisione().getAnnoCapitolo());
//		request.setParametroRicercaImpSub(parametroRicercaImpSub);
//		request.setNumPagina(1);
//		request.setNumRisultatiPerPagina(1);
//		RicercaSinteticaImpegniSubimpegniResponse responseTotaleImpegniCapitolo = movimentoGestioneService.ricercaSinteticaImpegniSubimpegni(request);
//		//qui bisognerebbe reindirizzare all'input in quanto non è possibile validare la JSP se il servizio fallisce.
		
		log.debug(methodName, "Impostazione dei dati nel model");
		model.impostaDatiDaResponse(responseRicercaDettaglio);
		log.debug(methodName, "Caricamento delle ulteriori liste");
		caricaListaCodificheAggiornamento();
		
		 StatoOperativoElementoDiBilancio stato = responseRicercaDettaglio.getCapitoloUscitaPrevisione().getStatoOperativoElementoDiBilancio();
		if (!StatoOperativoElementoDiBilancio.VALIDO.equals(stato ) && !StatoOperativoElementoDiBilancio.PROVVISORIO.equals(stato )){
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
				log.debug(methodName, createErrorInServiceInvocationString(requestRicercaVariazioni, responseRicercaVariazioni));
				addErrori(responseRicercaVariazioni);
				return INPUT;
			}
			
			// Se ci sono variazioni collegate, esco subito
			if(responseRicercaVariazioni.getNumeroVariazioniImporti() > 0 || responseRicercaVariazioni.getNumeroVariazioniCodifiche() > 0) {
				String str = getStringaErroreVariazioniNonDefinitiveCollegate(responseRicercaVariazioni.getVariazioneImportiNumero(), responseRicercaVariazioni.getVariazioneCodificheNumero());
				addMessaggio(ErroreBil.CAPITOLO_CON_VARIAZIONI_NON_DEFINITIVE_COLLEGATE.getErrore(str));
				//return INPUT;
			}
		}
		
		if(model.getCapitoloUscitaPrevisione().getNumeroArticolo() == null) {
			log.debug(methodName, "Caricamento dei valori di default");
			model.getCapitoloUscitaPrevisione().setNumeroArticolo(0);
		}
		
		log.debug(methodName, "Metto in sessione gli elementi già presenti nel model");
		sessionHandler.setParametro(BilSessionParameter.CLASSIFICATORI_AGGIORNAMENTO, ClassificatoreAggiornamentoFactory.getInstance(model));
		
		// Controllo per la prima volta l'aggiornabilità dei campi
		ControllaClassificatoriModificabiliCapitoloResponse responseClassificatoriModificabili = ottieniResponseControllaClassificatoriModificabiliCapitolo(model.getCapitoloUscitaPrevisione());
		ControllaAttributiModificabiliCapitoloResponse responseAttributiModificabili = ottieniResponseControllaAttributiModificabiliCapitolo(model.getCapitoloUscitaPrevisione());
		
		log.debug(methodName, "Valuto l'editabilita dei campi");
		
		model.valutaModificabilitaClassificatori(responseClassificatoriModificabili, false);
		
		model.valutaModificabilitaAttributi(responseAttributiModificabili, false);
		
		// Imposto la response per i classificatori modificabili in sessione
		sessionHandler.setParametroXmlType(BilSessionParameter.EDITABILITA_CLASSIFICATORI, responseClassificatoriModificabili);
		sessionHandler.setParametroXmlType(BilSessionParameter.EDITABILITA_ATTRIBUTI, responseAttributiModificabili);
		
		return SUCCESS;
	}
	
	

	/**
	 * Aggiorna il Capitolo di Uscita Previsione
	 * 
	 * @return la String corrispondente al risultato della Action
	 */
	public String aggiornaCDU() {
		final String methodName = "aggiornaCDU";
		
		/* Valorizzo i classificatori a partire dalla sessione */
		log.debug(methodName, "Valorizzo i classificatori");
		model.caricaClassificatoriDaSessione(sessionHandler);
		
		// Carico i dati dalla sessione
		log.debug(methodName, "Caricamento dei dati originali dalla sessione");
		ClassificatoreAggiornamentoCapitoloUscita classificatoreAggiornamento = sessionHandler.getParametro(BilSessionParameter.CLASSIFICATORI_AGGIORNAMENTO, ClassificatoreAggiornamentoCapitoloUscita.class);
		
		log.debug(methodName, "Creazione della request");
		AggiornaCapitoloDiUscitaPrevisione request = model.creaRequestAggiornaCapitoloDiUscitaPrevisione(classificatoreAggiornamento);
		logServiceRequest(request);
		
		//SIAC-6884 controllo impegni
		RicercaSinteticaImpegniSubImpegni request1 = new RicercaSinteticaImpegniSubImpegni();
		request1.setAnnoBilancio(model.getCapitoloUscitaPrevisione().getBilancioAnno());
		request1.setEnte(sessionHandler.getEnte());
		request1.setRichiedente(sessionHandler.getRichiedente());		
		ParametroRicercaImpSub parametroRicercaImpSub = new ParametroRicercaImpSub();
		parametroRicercaImpSub.setUidCapitolo(model.getCapitoloUscitaPrevisione().getUid());
		parametroRicercaImpSub.setAnnoEsercizio(model.getCapitoloUscitaPrevisione().getAnnoCapitolo());
		request1.setParametroRicercaImpSub(parametroRicercaImpSub);
		request1.setNumPagina(1);
		request1.setNumRisultatiPerPagina(1);
		RicercaSinteticaImpegniSubimpegniResponse responseTotaleImpegniCapitolo = movimentoGestioneService.ricercaSinteticaImpegniSubimpegni(request1);
		//qui bisognerebbe reindirizzare all'input in quanto non è possibile validare la JSP se il servizio fallisce.
		if (responseTotaleImpegniCapitolo.getListaImpegni() !=null && responseTotaleImpegniCapitolo.getListaImpegni().size()>0) {
			
			String str = 	"collegati: " +responseTotaleImpegniCapitolo.getListaImpegni().size();
			//TODO 28/11/2019 MODIFICARE MESSAGGIO DI ERRORE
			addMessaggio(ErroreBil.CAPITOLO_CON_VARIAZIONI_NON_DEFINITIVE_COLLEGATE.getErrore(str));
			caricaListaCodificheAggiornamento();
			return INPUT;
		}
		
		
		
		
		
		
		
		
		
		
		log.debug(methodName,"Richiamo il WebService di aggiornamento");
		AggiornaCapitoloDiUscitaPrevisioneResponse response = capitoloUscitaPrevisioneService.aggiornaCapitoloDiUscitaPrevisione(request);
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
		
		//SIAC-6884 mi serve per la visualizzazione delle componenti
		if(response.getCapitoloUscitaPrevisione()!= null &&
				response.getCapitoloUscitaPrevisione().getClassificatoriGenerici() != null &&
				!response.getCapitoloUscitaPrevisione().getClassificatoriGenerici().isEmpty()){		
			for(ClassificatoreGenerico cg : response.getCapitoloUscitaPrevisione().getClassificatoriGenerici()){
				if(cg.getTipoClassificatore().getCodice().equals(TipologiaClassificatore.CLASSIFICATORE_3.name())){
					model.setCapitoloFondino(cg.getCodice().equals("01") ? true : false);
				}				
			}
		}else{
			model.setCapitoloFondino(false);
		}
				
		
		log.debug(methodName, "Capitolo di Uscita di Previsione aggiornato correttamente");
		
		/* Ripulisco la sessione */
		// Mantengo i risultati di ricerca per tornare alla pagina dei risultati
		// Mantengo la posizione di rientro
		sessionHandler.cleanSafelyExcluding(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_USCITA_PREVISIONE, BilSessionParameter.RIENTRO_POSIZIONE_START);
		
		/* 
		 * Imposto in sessione il dato di rientro dall'aggiornamento, 
		 * utile nel caso in cui si debba rientrare sui risultati di ricerca
		 */
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, "S");
		
		/* Aggiungo l'informazione di completamento */
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		
		// Ripopolo gli eventuali campi che sono disabilitati
		model.setParametriDisabilitati(classificatoreAggiornamento);
		
		/* Imposta la necessita' di tornare indietro due volte a termine dell'esecuzione */
		if(!model.isDaAggiornamento()) {
			tornareDiDueIndietroDopoAggiornamento = true;
		}
		return SUCCESS;
	}
	
	@Override
	public void validate() {
		CapitoloUscitaPrevisione cup = model.getCapitoloUscitaPrevisione();
		if(cup != null && !model.isGestioneUEB()){
			cup.setNumeroUEB(Integer.valueOf(1));
		}
		// Controllo che il capitolo ci sia
		checkNotNull(cup, "Capitolo", true);
		
		checkNotNull(cup.getNumeroCapitolo(), "Capitolo");
		checkNotNull(cup.getNumeroArticolo(), "Articolo");
		checkNotNull(cup.getNumeroUEB(), "UEB");
		checkNotNullNorEmpty(cup.getDescrizione(), "Descrizione");
		checkCondition(!model.isCategoriaCapitoloEditabile() || (cup.getCategoriaCapitolo() != null && cup.getCategoriaCapitolo().getUid() != 0), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo capitolo"));
		
		// CR-2204: controllo la classificazione di bilancio solo per i tipi standard
		// CR-5007: controllo la classificazione di bilancio solo per i tipi FondoPluriennaleVincolato
		if(isCategoriaCapitoloStandard(cup.getCategoriaCapitolo()) || isCategoriaCapitoloFondoPluriennaleVincolato(cup.getCategoriaCapitolo())) {
			checkCondition(!model.isMissioneEditabile() || (model.getMissione() != null && model.getMissione().getUid() != 0), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Missione"));
			checkCondition(!model.isProgrammaEditabile() || (model.getProgramma() != null && model.getProgramma().getUid() != 0), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Programma"));
			checkCondition(!model.isTitoloSpesaEditabile() || (model.getTitoloSpesa() != null && model.getTitoloSpesa().getUid() != 0), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Titolo"));
			checkCondition(!model.isMacroaggregatoEditabile() || (model.getMacroaggregato() != null && model.getMacroaggregato().getUid() != 0), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Macroaggregato"));
			checkCondition(!model.isElementoPianoDeiContiEditabile() || (model.getElementoPianoDeiConti() != null && model.getElementoPianoDeiConti().getUid() != 0), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Elemento del Piano dei Conti"));
		}
		
		checkCondition(!model.isStrutturaAmministrativoContabileEditabile() || (model.getStrutturaAmministrativoContabile() != null && model.getStrutturaAmministrativoContabile().getUid() != 0),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Struttura Amministrativa Responsabile"));
		
		//validaImportoCapitoloUscitaPrevisione();
	}
	
	/**
	 * Unifica la validazione per gli Importi del Capitolo di Uscita Previsione.
	 */
	private void validaImportoCapitoloUscitaPrevisione() {
		validaImportoCapitolo(model.getImportiCapitoloUscitaPrevisione0(), 0, true, true);
		validaImportoCapitolo(model.getImportiCapitoloUscitaPrevisione1(), 1, false, true);
		validaImportoCapitolo(model.getImportiCapitoloUscitaPrevisione2(), 2, false, true);
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
						ErroreCore.VALORE_NON_VALIDO.getErrore("Cassa per anno " + anno, ": deve essere inferiore o uguale alla somma di competenza e residuo ("
							+ FormatUtils.formatCurrency(importi.getStanziamento().add(importi.getStanziamentoResiduo())) + ")"));
			}
		}
	}
	
	//SIAC-6884
	private boolean checkStanziamentiNegativi(List<ImportiCapitoloUP> listUP) {
		for(ImportiCapitoloUP icup : listUP){
			if(icup.getStanziamento().compareTo(BigDecimal.ZERO)<0){
				return true;
			}
		}
		return false;
	}
	
	
	
	
}
