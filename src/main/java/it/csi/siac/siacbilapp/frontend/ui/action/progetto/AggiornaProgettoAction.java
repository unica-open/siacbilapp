/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.progetto;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.struts2.interceptor.validation.SkipValidation;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.progetto.AggiornaProgettoModel;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.progetto.ElementoVersioneCronoprogramma;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.progetto.ElementoVersioneCronoprogrammaFactory;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoComplessivo;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoComplessivoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoEntrata;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoEntrataResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoSpesa;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloFondoPluriennaleVincolatoSpesaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CambiaFlagUsatoPerFpvCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.CambiaFlagUsatoPerFpvCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.OttieniFondoPluriennaleVincolatoCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.OttieniFondoPluriennaleVincolatoCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCronoprogrammaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioProgetto;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioProgettoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.progetto.CalcoloProspettoRiassuntivoCronoprogrammaAggiorna;
import it.csi.siac.siacbilser.frontend.webservice.msg.progetto.CalcoloProspettoRiassuntivoCronoprogrammaAggiornaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.progetto.CalcoloProspettoRiassuntivoCronoprogrammaConsulta;
import it.csi.siac.siacbilser.frontend.webservice.msg.progetto.CalcoloProspettoRiassuntivoCronoprogrammaConsultaResponse;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.FondoPluriennaleVincolatoCronoprogramma;
import it.csi.siac.siacbilser.model.FondoPluriennaleVincolatoEntrata;
import it.csi.siac.siacbilser.model.FondoPluriennaleVincolatoUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.ProspettoRiassuntivoCronoprogramma;
import it.csi.siac.siacbilser.model.StatoOperativoCronoprogramma;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.ParametroConfigurazioneEnteEnum;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;

/**
 * Classe di Action per la gestione della consultazione del Progetto.
 * 
 * @author Osorio Alessandra,Ahmad Nazha
 * @version 1.0.1 - 24/03/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class AggiornaProgettoAction extends GenericProgettoAction<AggiornaProgettoModel> {

	/** Per la serialiazzazione */
	private static final long serialVersionUID = 7033998987854718167L;

	@Override
	public void prepare() throws Exception {
		// Pulisco errori ed informazioni
		cleanErroriMessaggiInformazioni();
	}

	@Override
	public void prepareExecute() throws Exception {
		setModel(null);
		super.prepare();
		caricaListeCodifiche();
		caricaListaModalitaAffidamento();
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		checkDecentrato();

		// Controllo se l'azione precedente sia stata un successo, abbia messaggi o errori
		leggiEventualiErroriMessaggiInformazioniAzionePrecedente();

		sessionHandler.setParametro(BilSessionParameter.MODEL_AGGIORNA_CRONOPROGRAMMA, null);
		sessionHandler.setParametro(BilSessionParameter.MODEL_INSERISCI_CRONOPROGRAMMA, null);

		checkCasoDUsoApplicabile(model.getTitolo());

		log.debug(methodName, "Ricerco il progetto");
		RicercaDettaglioProgetto request = model.creaRequestRicercaDettaglioProgetto();
		RicercaDettaglioProgettoResponse response = progettoService.ricercaDettaglioProgetto(request);

		if (response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioProgetto.class, response));
			addErrori(response);
			return INPUT;
		}

		log.debug(methodName, "Progetto ottenuto");
		impostaDatiProgetto(response.getProgetto());

		List<Cronoprogramma> listaCronoprogrammi;

		try {
			listaCronoprogrammi = ricercaCronoprogrammi(model.getProgetto());
		} catch (WebServiceInvocationFailureException e) {
			log.error(methodName, e.getMessage());
			return INPUT;
		}

		// Imposto i dati nel model
		model.impostaWrappersCronoprogrammi(listaCronoprogrammi, sessionHandler.getAzioniConsentite(), response.getProgetto().getTipoProgetto());
		
		aggiungiCronoprogrammaDaGestione(sessionHandler.getAzioniConsentite());
		return SUCCESS;
	}
	
	/**
	 * Controlla se l'utente &eacute; decentrato
	 */
	private void checkDecentrato() {
		boolean isDecentrato = AzioniConsentiteFactory.isConsentito(AzioneConsentitaEnum.PROGETTO_AGGIORNA_DECENTRATO, sessionHandler.getAzioniConsentite());
		model.setDecentrato(isDecentrato);
	}

	/**
	 * Impostazione dei dati del progetto
	 * @param progetto il progetto con i dati da impostare
	 */
	private void impostaDatiProgetto(Progetto progetto) {
		//SIAC-6255, per il pregresso
		if(progetto.getTipoProgetto() == null) {
			progetto.setTipoProgetto(getTipoProgettoByFaseBilancio());
		}
		
		model.setProgetto(progetto);
		model.setModificabileInvestimentoInCorsoDiDefinizione(Boolean.TRUE.equals(progetto.getInvestimentoInCorsoDiDefinizione()));
		
		model.setAttoAmministrativo(progetto.getAttoAmministrativo());
	}

	/**
	 * Aggiunge all'elenco dei cronoprogrammi quello relativo alla riga 'Da Gestione'.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite 
	 * modificato ----> prima era 'Da getBilancio() ora e' da gestione'
	 */
	private void aggiungiCronoprogrammaDaGestione(List<AzioneConsentita> listaAzioniConsentite) {
		String methodName = "aggiungiCronoprogrammaDaGestione";
		ElementoVersioneCronoprogramma evc = new ElementoVersioneCronoprogramma();
		evc.setVersione("Da Gestione");
		evc.setDescrizione("Cronoprogramma ottenuto dagli accertamenti ed impegni afferenti il progetto");
		evc.setUid(-1);
		ElementoVersioneCronoprogrammaFactory.impostaAzioniCronoprogrammaGestione(evc, listaAzioniConsentite);
		
		CalcoloProspettoRiassuntivoCronoprogrammaAggiorna request = model.creaRequestCalcoloProspettoRiassuntivoCronoprogrammaAggiorna(model.getUidDaAggiornare());
		logServiceRequest(request);

		CalcoloProspettoRiassuntivoCronoprogrammaAggiornaResponse response = progettoService.calcoloProspettoRiassuntivoCronoprogrammaAggiorna(request);
		logServiceResponse(response);

		if (response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(CalcoloProspettoRiassuntivoCronoprogrammaAggiorna.class, response));
			addErrori(response);
			return;
		}
		
		List<ProspettoRiassuntivoCronoprogramma> result = response.getListaProspettoRiassuntivoCronoprogramma();
		if(result != null){
			for(ProspettoRiassuntivoCronoprogramma prc : result){
				if(prc.getAnno().intValue() == model.getAnnoEsercizioInt().intValue()){
					evc.setCompetenzaSpesaAnno0(prc.getTotaliSpese());
					//SIAC-8847
					evc.setCompetenzaEntrataAnno0(prc.getTotaliEntrate());
				}
				if(prc.getAnno().intValue() == model.getAnnoEsercizioInt().intValue()+1){
					evc.setCompetenzaSpesaAnno1(prc.getTotaliSpese());
					//SIAC-8847
					evc.setCompetenzaEntrataAnno1(prc.getTotaliEntrate());
				}
				if(prc.getAnno().intValue() == model.getAnnoEsercizioInt().intValue()+2){
					evc.setCompetenzaSpesaAnno2(prc.getTotaliSpese());
					//SIAC-8847
					evc.setCompetenzaEntrataAnno2(prc.getTotaliEntrate());
				}
				if(prc.getAnno().intValue() > model.getAnnoEsercizioInt().intValue()+2){
					evc.setCompetenzaSpesaAnnoSucc(evc.getCompetenzaSpesaAnnoSucc().add(prc.getTotaliSpese()));
					//SIAC-8847
					evc.setCompetenzaEntrataAnnoSucc(evc.getCompetenzaEntrataAnnoSucc().add(prc.getTotaliEntrate()));
				}
			}
			
		}
		
		model.getListaElementiVersioneCronoprogramma().add(0, evc);
	}
		

	/**
	 * Preparazione per il metodo {@link #aggiornaProgetto()}.
	 */
	public void prepareAggiornaProgetto(){
		model.getProgetto().setRilevanteFPV(null);
		model.getProgetto().setInvestimentoInCorsoDiDefinizione(null);
		model.getProgetto().setTipoAmbito(null);
		model.setAttoAmministrativo(null);
	}
	
	/**
	 * Metodo per l'aggiornamento del Progetto.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaProgetto() {
		final String methodName = "aggiornaProgetto";
		log.debug(methodName, "Aggiornamento del progetto");

		try {
			// Aggiornamento
			effettuaAggiornamento();
			// Ricerco il provvedimento per reimpostare il flag
			ricaricaProvvedimento();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			return INPUT;
		}
		impostaInformazioneSuccesso();

		// Imposto il parametro di rientro a TRUE
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		
		return SUCCESS;
	}

	/**
	 * Effettua l'aggiornamento del progetto
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	private void effettuaAggiornamento() throws WebServiceInvocationFailureException  {
		AggiornaAnagraficaProgetto req = model.creaRequestAggiornaAnagraficaProgetto();
		
		//SIAC-8900
		req.setByPassFaseBilancioProgetto(isBilancioEsercizioProvvisorio());
		
		AggiornaAnagraficaProgettoResponse res = progettoService.aggiornaAnagraficaProgetto(req);

		checkServiceResponse(AggiornaAnagraficaProgetto.class, res);
		
		model.setModificabileInvestimentoInCorsoDiDefinizione(Boolean.TRUE.equals(res.getProgetto().getInvestimentoInCorsoDiDefinizione()));
	}
 
	public boolean disabilitaAggiornamentoCampi() throws WebServiceInvocationFailureException {
		return Boolean.TRUE.equals(Boolean.parseBoolean(getParametroConfigurazioneEnte(
				ParametroConfigurazioneEnteEnum.OOPP_PROGETTO_AGGIORNA_DISABILITA_CAMPI)));
	}

	private void preserveFieldsFromUpdate() {
		RicercaDettaglioProgetto ricercaDettaglioProgetto = model.creaRequestRicercaDettaglioProgetto();
		ricercaDettaglioProgetto.setChiaveProgetto(model.getProgetto().getUid());
		RicercaDettaglioProgettoResponse ricercaDettaglioProgettoResponse = progettoService.ricercaDettaglioProgetto(ricercaDettaglioProgetto);

		Progetto progetto = ricercaDettaglioProgettoResponse.getProgetto();
		Progetto modelProgetto = model.getProgetto();

		modelProgetto.setNote(progetto.getNote());
		modelProgetto.setCodice(progetto.getCodice());
		modelProgetto.setCup(progetto.getCup());
		modelProgetto.setValoreComplessivo(progetto.getValoreComplessivo());
		modelProgetto.setDescrizione(progetto.getDescrizione());
		modelProgetto.setTipoAmbito(progetto.getTipoAmbito());
		modelProgetto.setResponsabileUnico(progetto.getResponsabileUnico());
		modelProgetto.setStrutturaAmministrativoContabile(progetto.getStrutturaAmministrativoContabile());
	}

	/**
	 * Ricarica il provvedimento per ovviare a potenziali problemi sul cambio dei flag
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	private void ricaricaProvvedimento() throws WebServiceInvocationFailureException {
		final String methodName = "ricaricaProvvedimento";
		RicercaProvvedimento req = model.creaRequestRicercaProvvedimento();
		RicercaProvvedimentoResponse res = provvedimentoService.ricercaProvvedimento(req);
		if (res.hasErrori()) {
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaProvvedimento.class, res));
		}
		if(res.getListaAttiAmministrativi().isEmpty()) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Atto amministrativo", "uid " + model.getAttoAmministrativo().getUid()));
			throw new WebServiceInvocationFailureException("Nessun provvedimento reperito per uid " + model.getAttoAmministrativo().getUid());
		}
		AttoAmministrativo aa = res.getListaAttiAmministrativi().get(0);
		model.setAttoAmministrativo(aa);
		model.setUidProvvedimento(aa.getUid());
		model.getProgetto().setAttoAmministrativo(aa);
		
		log.debug(methodName, "Progetto aggiornato");
	}

	/**
	 * Redirezione verso l'inserimento del cronoprogramma.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	@SkipValidation
	public String inserimentoCronoprogramma() {
		return SUCCESS;
	}

	/**
	 * Metodo per la lettura dei cronoprogrammi associati al progetto.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	@SkipValidation
	public String leggiCronoprogrammiAssociati() {
		String methodName = "leggiCronoprogrammiAssociati";
		log.debug(methodName, "getListaElementiVersioneCronoprogramma.size()=" + model.getListaElementiVersioneCronoprogramma().size());

		return SUCCESS;
	}

	/**
	 * Annulla il cronoprogramma fornito dalla pagina.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String annullaCronoprogramma() {
		final String methodName = "annullaCronoprogramma";
		log.debug(methodName, "annullamento cronoprogramma ");
		AnnullaCronoprogramma requestAnnulla = model.creaRequestAnnullaCronoprogramma();
		AnnullaCronoprogrammaResponse responseAnnulla = progettoService.annullaCronoprogramma(requestAnnulla);

		if (checkErroriResponse(responseAnnulla, methodName)) {
			return SUCCESS;
		}

		// Ricerco nuovamente i cronoprogrammi collegati
		List<Cronoprogramma> listaCronoprogramma;
		try {
			listaCronoprogramma = ricercaCronoprogrammi(model.getProgetto());
		} catch (WebServiceInvocationFailureException wsife) {
			log.error(methodName, wsife.getMessage());
			return SUCCESS;
		}

		model.impostaWrappersCronoprogrammi(listaCronoprogramma, sessionHandler.getAzioniConsentite(), model.getProgetto().getTipoProgetto());
		impostaInformazioneSuccesso();

		return SUCCESS;
	}

	/**
	 * Consulta il cronoprogramma fornito dalla pagina.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String consultaCronoprogramma() {
		final String methodName = "consultaCronoprogramma";

		RicercaDettaglioCronoprogramma request = model.creaRequestRicercaDettaglioCronoprogramma();
		RicercaDettaglioCronoprogrammaResponse response = progettoService.ricercaDettaglioCronoprogramma(request);

		if (checkErroriResponse(response, methodName)) {
			return INPUT;
		}
		//SIAC-6812: commento perche' possibile che venga cambiata idea
//		if(Boolean.TRUE.equals(response.getCronoprogramma().getCronoprogrammaDaDefinire())) {
//			log.info(methodName, "Cronoprogramma da definire: non premetto la visualizzazione dei totali");
//			addErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il cronoprogramma e' da definire, la consultazione degli importi non e' disponibile"));
//			return INPUT;
//		}

		model.popolaMappaTotaliCronoprogramma(response.getCronoprogramma());

		return SUCCESS;
	}

	/**
	 * Consulta il cronoprogramma fornito dalla pagina.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String consultaCronoprogrammaGestione() {
		// implementata e modificata il 23/03/2015 ahmad
		final String methodName = "consultaCronoprogrammaGestione";
		log.debug(methodName, "Consultazione cronoprogrammaGestione");

	
		CalcoloProspettoRiassuntivoCronoprogrammaConsulta request = model.creaRequestCalcoloProspettoRiassuntivoCronoprogrammaConsulta();
		

		CalcoloProspettoRiassuntivoCronoprogrammaConsultaResponse response = progettoService.calcoloProspettoRiassuntivoCronoprogrammaConsulta(request);

		if (response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(CalcoloProspettoRiassuntivoCronoprogrammaConsulta.class, response));
			addErrori(response);
			return SUCCESS;
		}

		model.setListaProspettoRiassuntivoCronoprogramma(response.getListaProspettoRiassuntivoCronoprogramma());

		log.debug(methodName, "Consultazione effettuata con successo");
		return SUCCESS;
	}

	/**
	 * Chiama il servizio e imposta nel model i valori di FPV entrata, FPV spesa ed FPV complessivo.
	 * @return il risultato dell'invocazione
	 */
	public String popolaFondoPluriennaleVincolato() {
		final String methodName = "popolaFondoPluriennaleVincolato";
		log.debug(methodName, "Calcolo del Fondo Pluriennale Vincolato (fpv entrata e fpv spesa suddivisi per anno).");
		//SIAC-6937
		Bilancio bil = model.getBilancio();
		FaseBilancio fb;
		if (bil.getFaseEStatoAttualeBilancio() == null || bil.getFaseEStatoAttualeBilancio().getFaseBilancio() == null){
			fb = caricaFaseBilancio();
		    FaseEStatoAttualeBilancio fsb = new FaseEStatoAttualeBilancio();
		    fsb.setFaseBilancio(fb);
		    bil.setFaseEStatoAttualeBilancio(fsb);
		    model.setBilancio(bil);
		}

		OttieniFondoPluriennaleVincolatoCronoprogramma request = model.creaRequestOttieniFondoPluriennaleVincolatoCronoprogramma();
		logServiceRequest(request);

		OttieniFondoPluriennaleVincolatoCronoprogrammaResponse response = progettoService.ottieniFondoPluriennaleVincolatoCronoprogramma(request);
		logServiceResponse(response);

		if (response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(OttieniFondoPluriennaleVincolatoCronoprogramma.class, response));
			addErrori(response);
			return SUCCESS;
		}
		model.setListaFondoPluriennaleVincolatoUscitaCronoprogramma(response.getListaFondoPluriennaleVincolatoUscitaCronoprogramma());
		model.setListaFondoPluriennaleVincolatoEntrata(response.getListaFondoPluriennaleVincolatoEntrata());
		model.setMappaFondoPluriennaleVincolatoPerAnno(calcolaFondoPluriennaleVincolatoTotale(response.getListaFondoPluriennaleVincolatoEntrata(), response.getListaFondoPluriennaleVincolatoUscitaCronoprogramma()));
		
		log.debug(methodName, "Lettura FPV avvenuta con successo");
		return SUCCESS;
	}
	
	/**
	 * Calcolo del fondo pluriennale vincolato totale
	 * @param listaFondoPluriennaleVincolatoEntrata i fondi di entrata
	 * @param listaFondoPluriennaleVincolatoUscitaCronoprogramma i fondi di uscita
	 * @return la mappa dei totali del fondo pluriennale per anno
	 */
	private Map<Integer, FondoPluriennaleVincolatoCronoprogramma[]> calcolaFondoPluriennaleVincolatoTotale(List<FondoPluriennaleVincolatoEntrata> listaFondoPluriennaleVincolatoEntrata, List<FondoPluriennaleVincolatoUscitaCronoprogramma> listaFondoPluriennaleVincolatoUscitaCronoprogramma){
		Map<Integer, FondoPluriennaleVincolatoCronoprogramma[]> map =  new TreeMap<Integer, FondoPluriennaleVincolatoCronoprogramma[]>();
		
		// Creo i fondi di entrata
		for (FondoPluriennaleVincolatoEntrata fpvE : listaFondoPluriennaleVincolatoEntrata) {
			Integer anno = fpvE.getAnno();
			FondoPluriennaleVincolatoCronoprogramma[] arr = map.get(anno);
			
			FondoPluriennaleVincolatoEntrata fpvENellaMappa;
			
			if(arr == null) {
				arr = new FondoPluriennaleVincolatoCronoprogramma[2];
				map.put(anno, arr);
				
				FondoPluriennaleVincolatoUscitaCronoprogramma fpvU = new FondoPluriennaleVincolatoUscitaCronoprogramma();
				fpvU.setAnno(anno);
				arr[1] = fpvU;
				
				fpvENellaMappa = new FondoPluriennaleVincolatoEntrata();
				fpvENellaMappa.setAnno(anno);
				arr[0] = fpvENellaMappa;
			} else {
				fpvENellaMappa = (FondoPluriennaleVincolatoEntrata) arr[0];
			}
			
			fpvENellaMappa.addImporto(fpvE.getImporto());
			fpvENellaMappa.addImportoFPV(fpvE.getImportoFPV());
		}
		
		// Creo i fondi di uscita
		for (FondoPluriennaleVincolatoUscitaCronoprogramma fpvU : listaFondoPluriennaleVincolatoUscitaCronoprogramma) {
			if(fpvU.getImportoFPV() == null) {
				continue;
			}
			Integer anno = fpvU.getAnno();
			FondoPluriennaleVincolatoCronoprogramma[] arr = map.get(anno);
			
			FondoPluriennaleVincolatoUscitaCronoprogramma fpvUNellaMappa;
			
			if(arr == null) {
				arr = new FondoPluriennaleVincolatoCronoprogramma[2];
				map.put(anno, arr);
				
				FondoPluriennaleVincolatoEntrata fpvE = new FondoPluriennaleVincolatoEntrata();
				fpvE.setAnno(anno);
				arr[0] = fpvE;
				
				fpvUNellaMappa = new FondoPluriennaleVincolatoUscitaCronoprogramma();
				fpvUNellaMappa.setAnno(anno);
				arr[1] = fpvUNellaMappa;
			} else {
				fpvUNellaMappa = (FondoPluriennaleVincolatoUscitaCronoprogramma) arr[1];
			}
			
			fpvUNellaMappa.addImporto(fpvU.getImporto());
			fpvUNellaMappa.addImportoFPV(fpvU.getImportoFPV());
			
		}
		return map;
	}

	/**
	 * Consulta il prospetto dei totali dell'FPV.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String calcolaFondoPluriennaleVincolatoComplessivo() {
		return SUCCESS;
	}

	/**
	 * Consulta il prospetto dell'FPV Entrata.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String calcolaFondoPluriennaleVincolatoEntrata() {
		return SUCCESS;
	}

	/**
	 * Consulta il prospetto dell'FPV Uscita.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String calcolaFondoPluriennaleVincolatoUscita() {
		return SUCCESS;
	}

	/* Validazioni */

	/**
	 * Validazione per il metodo di aggiornamento.
	 * @throws WebServiceInvocationFailureException 
	 */
	public void validateAggiornaProgetto() throws WebServiceInvocationFailureException {
		// SIAC-8703
		if (disabilitaAggiornamentoCampi()) {
			preserveFieldsFromUpdate();
		}

		Progetto p = model.getProgetto();
		checkNotNull(p, "progetto");
		
		checkNotNullNorEmpty(p.getCodice(), "Codice");
		checkNotNull(p.getDescrizione(), "Descrizione");
		checkNotNull(p.getStatoOperativoProgetto(), "Stato");
		checkNotNullNorInvalidUid(model.getAttoAmministrativo(), "Provvedimento");
		checkCondition(!model.isProvvedimentoValorizzato() || !"ANNULLATO".equalsIgnoreCase(model.getAttoAmministrativo().getStatoOperativo()), ErroreAtt.PROVVEDIMENTO_ANNULLATO.getErrore());
		//SIAC-6255
		//SIAC-8900
		caricaFaseBilancio();
		TipoProgetto tipoProgetto = getTipoProgettoByFaseBilancio();
		checkCondition(tipoProgetto.equals(p.getTipoProgetto()) , ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo progetto, atteso " + tipoProgetto.getDescrizione()));
	}

	/* Metodi di utilita' */

	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		final String codiceAzione = AzioneConsentitaEnum.PROGETTO_AGGIORNA.getNomeAzione();
		List<AzioneConsentita> list = sessionHandler.getAzioniConsentite();
		for (AzioneConsentita azioneConsentita : list) {
			if (azioneConsentita.getAzione().getNome().equalsIgnoreCase(codiceAzione)) {
				// Sono a posto
				return;
			}
		}
		// Non mi è consentito giungere all'azione
		Errore errore = ErroreCore.TIPO_AZIONE_NON_SUPPORTATA.getErrore("gestione progetto");
		throw new GenericFrontEndMessagesException(errore.getTesto(), GenericFrontEndMessagesException.Level.ERROR);
	}

	// parte aggiunta ahmad 19/03/2015

	/**
	 * Metodo per il popolamento del FondoPluriennaleVincolato (viene caricato sul cronoprogramma da gestione ).
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String popolaFondoPluriennaleVincolatoComplessivoDaGestioneDaBilancio() {
		final String methodName = "popolaFondoPluriennaleVincolatoComplessivoDaGestioneDaBilancio";
		log.debug(methodName, "Calcolo del Fondo Pluriennale Vincolato Complessivo DaGestioneDaBilancio(function fnc_siac_fpv_totale)");

		//SIAC-6937 NUOVO 
		Bilancio bil = model.getBilancio();
		FaseBilancio fb;
		if (bil.getFaseEStatoAttualeBilancio() == null || bil.getFaseEStatoAttualeBilancio().getFaseBilancio() == null){
			fb = caricaFaseBilancio();
		    FaseEStatoAttualeBilancio fsb = new FaseEStatoAttualeBilancio();
		    fsb.setFaseBilancio(fb);
		    bil.setFaseEStatoAttualeBilancio(fsb);
		    model.setBilancio(bil);
		}
		
		CalcoloFondoPluriennaleVincolatoComplessivo request = model.creaRequestCalcoloFondoPluriennaleVincolatoComplessivo();
		logServiceRequest(request);

		CalcoloFondoPluriennaleVincolatoComplessivoResponse response = progettoService.calcoloFondoPluriennaleVincolatoComplessivo(request);
		logServiceResponse(response);

		if (response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(CalcoloFondoPluriennaleVincolatoComplessivo.class, response));
			addErrori(response);
			return SUCCESS;
		}

		model.setListaFondoPluriennaleVincolatoTotale(response.getListaFondoPluriennaleVincolatoTotale());

		log.debug(methodName, "Lettura FPV complessivo da gestione da bilancio con successo");
		return SUCCESS;
	}

	/**
	 * Metodo per il popolamento del FondoPluriennaleVincolato (viene caricato sul cronoprogramma da gestione).
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String popolaFondoPluriennaleVincolatoEntrataDaGestioneDaBilancio() {
		final String methodName = "popolaFondoPluriennaleVincolatoEntrataDaGestioneDaBilancio";
		log.debug(methodName, "Calcolo del Fondo Pluriennale Vincolato Entrata DaGestioneDaBilancio(function fnc_siac_fpv_entrata)");

		CalcoloFondoPluriennaleVincolatoEntrata request = model.creaRequestCalcoloFondoPluriennaleVincolatoEntrata();
		logServiceRequest(request);

		CalcoloFondoPluriennaleVincolatoEntrataResponse response = progettoService.calcoloFondoPluriennaleVincolatoEntrata(request);
		logServiceResponse(response);

		if (response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(CalcoloFondoPluriennaleVincolatoEntrata.class, response));
			addErrori(response);
			return SUCCESS;
		}

		model.setListaRiepilogoFondoPluriennaleVincolatoDaGestioneDaBilancioPerEntrate(response.getListaFondoPluriennaleVincolatoEntrata());

		log.debug(methodName, "Lettura FPV Entrata da gestione da bilancio con successo");
		return SUCCESS;
	}

	/**
	 * Metodo per il popolamento del FondoPluriennaleVincolato (viene caricato sul cronoprogramma da gestione).
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String popolaFondoPluriennaleVincolatoUscitaDaGestioneDaBilancio() {
		final String methodName = "popolaFondoPluriennaleVincolatoUscitaDaGestioneDaBilancio";
		log.debug(methodName, "Calcolo del Fondo Pluriennale Vincolato uscita DaGestioneDaBilancio (function fnc_siac_fpv_spesa)");

		CalcoloFondoPluriennaleVincolatoSpesa request = model.creaRequestCalcoloFondoPluriennaleVincolatoSpesa();
		logServiceRequest(request);

		CalcoloFondoPluriennaleVincolatoSpesaResponse response = progettoService.calcoloFondoPluriennaleVincolatoSpesa(request);
		logServiceResponse(response);

		if (response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(CalcoloFondoPluriennaleVincolatoSpesa.class, response));
			addErrori(response);
			return SUCCESS;
		}

		model.setListaFondoPluriennaleVincolatoUscitaCronoprogrammaDaGestioneDaBilancio(response.getListaFondoPluriennaleVincolatoUscitaCronoprogramma());

		log.debug(methodName, "Lettura FPV spesa da gestione da bilancio con successo");
		return SUCCESS;
	}

	/**
	 * Preparazione per il metodo {@link #settaCronoprogrammaComeUsatoPerFPV()}.
	 */
	public void prepareSettaCronoprogrammaComeUsatoPerFPV() {
		model.setUidCronoprogrammaDaSettareComeUsatoPerCalcoloFPV(null);
	}
	
	/**
	 * setta il cronoprogramma come usato per fpv ---->cambio il flag e richiama il servizio di aggiornamento
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String settaCronoprogrammaComeUsatoPerFPV() {
		final String methodName = "settaCronoprogrammaComeUsatoPerFPV";
		log.debug(methodName, "settaggio cronoprogramma come usato per fpv");
		log.debug(methodName, "uid cronoprogramma da settare " + model.getUidCronoprogrammaDaSettareComeUsatoPerCalcoloFPV());
		
		model.getCronoprogrammaDaAggiornare().setUsatoPerFpv(Boolean.TRUE);
		model.getCronoprogrammaDaAggiornare().setUsatoPerFpvProv(Boolean.TRUE);
		
		return chiamaCambiaFlagPerFpvCronoprogramma(methodName);
	}
	
	/**
	 * Preparazione per il metodo {@link #disassociaCronoprogrammaComeUsatoPerFPV()}.
	 */
	public void prepareDisassociaCronoprogrammaComeUsatoPerFPV() {
		model.setUidCronoprogrammaDaDisassociareComeUsatoPerCalcoloFPV(null);
	}
	
	/**
	 * disassocia il cronoprogramma da come usato per fpv ---->cambio il flag e richiama il servizio di aggiornamento
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String disassociaCronoprogrammaComeUsatoPerFPV() {
		final String methodName = "disassociaCronoprogrammaComeUsatoPerFPV";
		log.debug(methodName, "disassocia cronoprogramma come usato per fpv");
		log.debug(methodName, "uid cronoprogramma da settare " + model.getUidCronoprogrammaDaSettareComeUsatoPerCalcoloFPV());
		
		model.getCronoprogrammaDaAggiornare().setUsatoPerFpv(Boolean.FALSE);
		model.getCronoprogrammaDaAggiornare().setUsatoPerFpvProv(Boolean.FALSE);
		
		return chiamaCambiaFlagPerFpvCronoprogramma(methodName);
	}
	
	/**
	 * Prepare simula FPV.
	 */
	public void prepareSimulaFPV() {
		model.setUidCronoprogrammaDaSettareComeUsatoPerCalcoloFPV(null);
	}
	
	/**
	 * Validate simula FPV.
	 */
	public void validateSimulaFPV() {
		Cronoprogramma cronoprogramma = innerValidateFPV();
		checkCondition(!Boolean.TRUE.equals(cronoprogramma.getUsatoPerFpvProv()),
			ErroreBil.OPERAZIONE_NON_POSSIBILE.getErrore("il cronoprogramma &egrave; gi&agrave; stato utilizzato per simulare il calcolo fpv"));
		
		// Imposto il dato nel model
		if(!hasErrori()) {
			model.setCronoprogrammaDaAggiornare(cronoprogramma);
		}
	}

	/**
	 * @return
	 */
	private Cronoprogramma innerValidateFPV() {
		checkNotNull(model.getUidCronoprogrammaDaSettareComeUsatoPerCalcoloFPV(), "Cronoprogramma", true);
		
		Cronoprogramma cronoprogramma = new Cronoprogramma();
		cronoprogramma.setUid(model.getUidCronoprogrammaDaSettareComeUsatoPerCalcoloFPV().intValue());
		
		// Cerco nella lista
		cronoprogramma = ComparatorUtils.searchByUidEventuallyNull(model.getListaCronoprogrammiCollegatiAlProgetto(), cronoprogramma);
		checkNotNullNorInvalidUid(cronoprogramma, "Cronoprogramma", true);
		
		checkCondition(StatoOperativoCronoprogramma.VALIDO.equals(cronoprogramma.getStatoOperativoCronoprogramma()),
				ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Stato del cronoprogramma ("+ cronoprogramma.getStatoOperativoCronoprogramma() + ") non congruente."));
		checkCondition(!Boolean.TRUE.equals(cronoprogramma.getUsatoPerFpv()),
				ErroreBil.CRONOPROGRAMMA_CHE_NON_SI_PUO_USARE_PER_IL_CALCOLO_FPV_PERCHE_GIA_UTILIZZATO_NEL_CALCOLO_FPV.getErrore());
		return cronoprogramma;
	}
	
	
	/**
	 * @return
	 */
	private Cronoprogramma innerValidateDisassociaFPV() {
		checkNotNull(model.getUidCronoprogrammaDaDisassociareComeUsatoPerCalcoloFPV(), "Cronoprogramma", true);
		
		Cronoprogramma cronoprogramma = new Cronoprogramma();
		cronoprogramma.setUid(model.getUidCronoprogrammaDaDisassociareComeUsatoPerCalcoloFPV().intValue());
		
		// Cerco nella lista
		cronoprogramma = ComparatorUtils.searchByUidEventuallyNull(model.getListaCronoprogrammiCollegatiAlProgetto(), cronoprogramma);
		checkNotNullNorInvalidUid(cronoprogramma, "Cronoprogramma", true);
		
		checkCondition(StatoOperativoCronoprogramma.VALIDO.equals(cronoprogramma.getStatoOperativoCronoprogramma()),
				ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Stato del cronoprogramma ("+ cronoprogramma.getStatoOperativoCronoprogramma() + ") non congruente."));
		checkCondition(Boolean.TRUE.equals(cronoprogramma.getUsatoPerFpv()),
				ErroreBil.CRONOPROGRAMMA_CHE_NON_SI_PUO_USARE_PER_IL_DISASSOCIA_FPV.getErrore());
		
		// imposto setUidCronoprogrammaDaSettareComeUsatoPerCalcoloFPV
		model.setUidCronoprogrammaDaSettareComeUsatoPerCalcoloFPV(model.getUidCronoprogrammaDaDisassociareComeUsatoPerCalcoloFPV());
		return cronoprogramma;
	}
	
	/**
	 * setta il cronoprogramma come usato per fpv ---->cambio il flag e richiama il servizio di aggiornamento
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String simulaFPV() {
		final String methodName = "simulaFPV";
		
		model.getCronoprogrammaDaAggiornare().setUsatoPerFpvProv(Boolean.TRUE);
		model.getCronoprogrammaDaAggiornare().setUsatoPerFpv(null);
		
		return chiamaCambiaFlagPerFpvCronoprogramma(methodName);
	}

	
	/**
	 * Prepare simula FPV.
	 */
	public void prepareAnnullaSimulaFPV() {
		model.setUidCronoprogrammaDaSettareComeUsatoPerCalcoloFPV(null);
	}
	
	/**
	 * Validate simula FPV.
	 */
	public void validateAnnullaSimulaFPV() {
		Cronoprogramma cronoprogramma = innerValidateFPV();
		checkCondition(Boolean.TRUE.equals(cronoprogramma.getUsatoPerFpvProv()),
			ErroreBil.OPERAZIONE_NON_POSSIBILE.getErrore("il cronoprogramma non e' mai stato utilizzato per simulare il calcolo fpv"));
		
		// Imposto il dato nel model
		if(!hasErrori()) {
			model.setCronoprogrammaDaAggiornare(cronoprogramma);
		}
	}

	/**
	 * setta il cronoprogramma come usato per fpv ---->cambio il flag e richiama il servizio di aggiornamento
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String annullaSimulaFPV() {
		final String methodName = "simulaFPV";
		
		model.getCronoprogrammaDaAggiornare().setUsatoPerFpvProv(Boolean.FALSE);
		model.getCronoprogrammaDaAggiornare().setUsatoPerFpv(null);
		
		return chiamaCambiaFlagPerFpvCronoprogramma(methodName);
	}
	/**
	 * @param methodName
	 * @return
	 */
	private String chiamaCambiaFlagPerFpvCronoprogramma(final String methodName) {
		CambiaFlagUsatoPerFpvCronoprogramma requestCambiaFlag = model.creaRequestCambiaFlagUsatoPerFpvCronoprogramma();
		logServiceRequest(requestCambiaFlag);
		CambiaFlagUsatoPerFpvCronoprogrammaResponse responseCambiaFlag = progettoService.cambiaFlagUsatoPerFpvCronoprogramma(requestCambiaFlag);
		logServiceResponse(responseCambiaFlag);

		if(checkErroriResponse(responseCambiaFlag, methodName)) {
			return SUCCESS;
		}
		
		// Ricerco nuovamente i cronoprogrammi collegati
		List<Cronoprogramma> listaCronoprogramma;
		try {
			listaCronoprogramma = ricercaCronoprogrammi(model.getProgetto());
		} catch (WebServiceInvocationFailureException wsife) {
			log.error(methodName, wsife.getMessage());
			return SUCCESS;
		}
		
		model.impostaWrappersCronoprogrammi(listaCronoprogramma, sessionHandler.getAzioniConsentite(),model.getProgetto().getTipoProgetto());
		impostaInformazioneSuccesso();
		return SUCCESS;
	}

	/**
	 * Validazione per il metodo {@link #settaCronoprogrammaComeUsatoPerFPV()}.
	 * <br/>
	 * Il cronoprogramma &eacute; associabile solo se &eacute; valido (<code>Cronoprogramma.StatoOperativo = V</code>) e se non &eacute; gi&agrave; associato ad un FPV
	 * (<code>Cronoprogramma.usato per calcolo FPV = 'No'</code>).
	 * <br/>
	 * Nel caso in cui sia stato usato per il calcolo FPV il sistema deve segnalare il messaggio <code>&lt;BIL_ERR_0110, Cronoprogramma che non si pu&oacute; usare per il calcolo FPV
	 * perch&eacute; gi&agrave; utilizzato nel calcolo FPV&gt;</code>.
	 * <br/>
	 * Non deve essere presente un altro cronoprogramma nell'anno di esercizio gi&agrave; etichettato come utilizzato per il calcolo FPV, nel caso visualizzare il messaggio:
	 * <code>&lt;BIL_ERR_0111, Scelta del cronoprogramma non possibile: &eacute; gi&agrave; presente un cronoprogramma usato per il calcolo FPV&gt;</code>.
	 */
	public void validateSettaCronoprogrammaComeUsatoPerFPV() {
		Cronoprogramma cronoprogramma = innerValidateFPV();
		
		checkNessunAltroCronoprogrammaUsatoPerFPV(cronoprogramma);
		
		// Imposto il dato nel model
		if(!hasErrori()) {
			model.setCronoprogrammaDaAggiornare(cronoprogramma);
		}
		
	}


	/** 
	 * Validazione per il metodo {@link #disassociaCronoprogrammaComeUsatoPerFPV()}.
	 * <br/>
	 * Il cronoprogramma &eacute; disassociabile solo se &eacute; gi&agrave; associato ad un FPV
	 * (<code>Cronoprogramma.usato per calcolo FPV = 'Si'</code>).
	 * SIAC-8870
	 * <br/>
	 */
	public void validateDisassociaCronoprogrammaComeUsatoPerFPV() {
		Cronoprogramma cronoprogramma = innerValidateDisassociaFPV();
		
		// Imposto il dato nel model
		if(!hasErrori()) {
			model.setCronoprogrammaDaAggiornare(cronoprogramma);
		}
		
	}
	
	/**
	 * Controlla che nessun altro cronoprogramma sia utilizzato per l'FPV.
	 * 
	 * @param cronoprogramma il cronoprogramma da controllare
	 */
	private void checkNessunAltroCronoprogrammaUsatoPerFPV(Cronoprogramma cronoprogramma) {
		int anno = model.getAnnoEsercizioInt().intValue();
		
		for(Cronoprogramma c : model.getListaCronoprogrammiCollegatiAlProgetto()) {
			checkCondition(c.getUid() == cronoprogramma.getUid()
					|| c.getBilancio() == null
					|| c.getBilancio().getAnno() != anno
					|| !Boolean.TRUE.equals(c.getUsatoPerFpv()),
				ErroreBil.ESISTE_GIA_UN_CRONOPROGRAMMA_USATO_PER_IL_CALCOLO_FPV.getErrore());
		}
	}
	
}
