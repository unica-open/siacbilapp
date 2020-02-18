/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.predocumento;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipologieClassificatori;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipologieClassificatoriResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ContoCorrentePredocumentoEntrata;
import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.predocumento.GenericPreDocumentoEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoCausale;
import it.csi.siac.siacfin2ser.model.TipoCausale;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.ProvvisorioService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassaResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa;

/**
 * Classe di action generica per il PreDocumento di Entrata
 * 
 * @author Marchino Alessandro,Nazha Ahmad
 * @version 1.0.0 - 15/04/2014
 * @version 1.0.1 - 11/06/2015
 * @param <M> la parametrizzazione del model
 */
public class GenericPreDocumentoEntrataAction<M extends GenericPreDocumentoEntrataModel> extends GenericPreDocumentoAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5753785789350319842L;
	
	/** Serviz&icirc; del predocumento di entrata */
	@Autowired protected transient PreDocumentoEntrataService preDocumentoEntrataService;
	/** Serviz&icirc; del capitolo di entrata */
	@Autowired protected transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	/** Serviz&icirc; del provvisorio */
	@Autowired protected transient ProvvisorioService provvisorioService;

	/**
	 * Effettua una validazione del Capitolo fornito in input.
	 * 
	 * @return <code>true</code> se la validazione &eacute; andata a buon fine; <code>false</code> in caso contrario
	 */
	protected boolean validazioneCapitolo() {
		CapitoloEntrataGestione capitoloEntrataGestione = model.getCapitolo();
		// Se non ho il capitolo, sono a posto
		if(capitoloEntrataGestione == null
			|| (
				// Gestione UEB: posso avere un qualsiasi campo
				model.isGestioneUEB()
				&& capitoloEntrataGestione.getNumeroCapitolo() == null
				&& capitoloEntrataGestione.getNumeroArticolo() == null
				&& capitoloEntrataGestione.getNumeroUEB() == null
			) || (
				// Gestione NoUEB: ignoro il caso in cui ho solo l'UEB
					capitoloEntrataGestione.getNumeroCapitolo() == null
				&& capitoloEntrataGestione.getNumeroArticolo() == null
			)) {
			return false;
		}
		
		RicercaSinteticaCapitoloEntrataGestione request = model.creaRequestRicercaSinteticaCapitoloEntrataGestione();
		logServiceRequest(request);
		RicercaSinteticaCapitoloEntrataGestioneResponse response = capitoloEntrataGestioneService.ricercaSinteticaCapitoloEntrataGestione(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return false;
		}
		
		StringBuilder classificazioneCapitolo = new StringBuilder()
				.append(capitoloEntrataGestione.getAnnoCapitolo())
				.append("/")
				.append(capitoloEntrataGestione.getNumeroCapitolo())
				.append("/")
				.append(capitoloEntrataGestione.getNumeroArticolo());
		if(model.isGestioneUEB()) {
			classificazioneCapitolo
				.append("/")
				.append(capitoloEntrataGestione.getNumeroUEB());
		}
		
		int totaleElementi = response.getTotaleElementi();
		checkCondition(totaleElementi > 0, ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo", classificazioneCapitolo.toString()));
		checkCondition(totaleElementi < 2, ErroreFin.OGGETTO_NON_UNIVOCO.getErrore("Capitolo"));
		
		if(totaleElementi == 1) {
			// Imposto i dati del capitolo
			model.setCapitolo(response.getCapitoli().get(0));
		}
		return true;
	}
	
	/**
	 * Effettua una validazione dell'accertamento e del subaccertamento forniti in input.
	 * 
	 * @return <code>true</code> se la validazione &eacute; andata a buon fine; <code>false</code> in caso contrario
	 */
	protected boolean validazioneAccertamentoSubAccertamento() {
		final String methodName = "validazioneAccertamentoSubAccertamento";
		Accertamento accertamento = model.getMovimentoGestione();
		SubAccertamento subAccertamento = model.getSubMovimentoGestione();
		
		if(accertamento == null || (
				accertamento.getAnnoMovimento() == 0 || accertamento.getNumero() == null
				)) {
			return false;
		}
		
		RicercaAccertamentoPerChiaveOttimizzato request = model.creaRequestRicercaAccertamentoPerChiaveOttimizzato();
		logServiceRequest(request);
		RicercaAccertamentoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errori nell'invocazione della ricercaAccertamentoPerChiaveOttimizzato");
			addErrori(response);
			return false;
		}
		if(response.isFallimento() || response.getAccertamento() == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Accertamento", accertamento.getAnnoMovimento()+"/"+accertamento.getNumero()));
			return false;
		}
		
		accertamento = response.getAccertamento();
		
		model.setMovimentoGestione(accertamento);
		
		if(subAccertamento != null && subAccertamento.getNumero() != null) {
			BigDecimal numero = subAccertamento.getNumero();
			// Controlli di validità sull'impegno
			subAccertamento = findSubAccertamentoLegatoAccertamentoByNumero(response.getAccertamento(), subAccertamento);
			if(subAccertamento == null) {
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Subaccertamento", accertamento.getAnnoMovimento() + "/" + accertamento.getNumero() + "-" + numero));
				return true;
			}
			model.setSubMovimentoGestione(subAccertamento);
			// Controllo stato
			checkCondition(StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(subAccertamento.getStatoOperativoMovimentoGestioneEntrata()), ErroreFin.SUBACCERTAMENTO_NON_IN_STATO_DEFINITIVO.getErrore(""));
			
		} else {
			// Controlli di validità sul subimpegno
			checkCondition(StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(accertamento.getStatoOperativoMovimentoGestioneEntrata()), ErroreFin.ACCERTAMENTO_NON_IN_STATO_DEFINITIVO.getErrore(""));
		}
		
		// Controllo anno
		checkCondition(accertamento.getAnnoMovimento() <= model.getAnnoEsercizioInt().intValue(), 
				ErroreCore.FORMATO_NON_VALIDO.getErrore("subaccertamento", "l'anno deve essere non superiore all'anno di esercizio"));
		
		checkDisponibilitaAccertamentoSubaccertamento();
		
		return true;
	}
	/**
	 * Effettua i controlli necessari sulla disponibilit&agrave; del movimento di gestione che si vuole associare al predocumento.
	 * <br/>
	 * CR 4310: Indicando su una predisposizione di incasso (in inserimento o aggiornamento) l'accertamento (o il sub)
	 * attualmente la mancata disponibilit&agrave; blocca l'operazione.
	 * <br/>
	 * Si richiede, solo in caso di ACCERTAMENTO DI COMPETENZA, di non bloccare ma, dopo una conferma dell'operatore,
	 * effettuare l'operazione previo inserimento di una modifica di accertamento/subaccertamento a copertura dell'importo.
	 * <br/>
	 * CR-5041: se l'utente &eacute; profilato con l'azione OP-ENT-PreDocNoModAcc
	 */
	protected void checkDisponibilitaAccertamentoSubaccertamento() {
		// CR-4310
		BigDecimal disponibilitaIncassare = model.getSubMovimentoGestione() != null && model.getSubMovimentoGestione().getUid() != 0
				? model.getSubMovimentoGestione().getDisponibilitaIncassare()
				: model.getMovimentoGestione().getDisponibilitaIncassare();
		BigDecimal importoPredoc = model.getPreDocumento().getImporto();
		if(importoPredoc == null || disponibilitaIncassare.compareTo(importoPredoc) >= 0 || Boolean.TRUE.equals(model.getForzaDisponibilitaAccertamento())) {
			// la disponibilita' e' sufficiente o da non considerare: resetto tutte le informazioni relative ad un'eventuale precedente richiesta di conferma 
			model.setMessaggioRichiestaConfermaProsecuzione("");
			return;
		}

		String msgOperazione = model.getPreDocumento().getUid() != 0 ? "Aggiornamento" : "Inserimento";
		// SIAC-5041: se ho l'azione OP-ENT-PreDocNoModAcc non posso fare la modifica
		boolean modificaNonConsentita = AzioniConsentiteFactory.isConsentito(AzioniConsentite.PREDOCUMENTO_ENTRATA_MODIFICA_ACC_NON_AMMESSA, sessionHandler.getAzioniConsentite());
		if(modificaNonConsentita || model.getMovimentoGestione().getAnnoMovimento() < model.getAnnoEsercizioInt().intValue()){
			//L'accertamento e' residuo. la disponibilita non puo essere adeguata.
			addErrore(ErroreFin.DISPONIBILITA_INSUFFICIENTE_MOVIMENTO.getErrore(msgOperazione + " predisposizione incasso", "Accertamento"));
			return;
		}
				
		//Accertamento di competenza e disponibilita' insufficiente: Imposto il messaggio di richiesta da mostrare all'utente (utilizzato poi messaggio nell'execute)
		model.setMessaggioRichiestaConfermaProsecuzione(ErroreFin.DISPONIBILITA_INSUFFICIENTE_MOVIMENTO.getErrore(msgOperazione + " predisposizione incasso", "accertamento", "E' possibile adeguare l'importo.").getDescrizione()
				+ "</br> Si desidera continuare?");
	}

	/**
	 * Trova il subAccertamento nell'elenco degli subaccertamenti dell'accertamento, se presente.
	 * 
	 * @param accertamento    l'accertamento tra i cui subAccertamenti trovare quello fornito
	 * @param subAccertamento il subaccertamento da cercare
	 * 
	 * @return il subaccertamento legato, se presente; <code>null</code> in caso contrario
	 */
	private SubAccertamento findSubAccertamentoLegatoAccertamentoByNumero(Accertamento accertamento, SubAccertamento subAccertamento) {
		SubAccertamento result = null;
		if(accertamento.getElencoSubAccertamenti() != null) {
			for(SubAccertamento s : accertamento.getElencoSubAccertamenti()) {
				if(s.getNumero().compareTo(subAccertamento.getNumero()) == 0) {
					result = s;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Controlla la congruenza tra il capitolo e l'impegno.
	 */
	protected void controlloCongruenzaCapitoloAccertamento() {
		CapitoloEntrataGestione capitoloEntrataGestione = model.getCapitolo();
		Accertamento accertamento = model.getMovimentoGestione();
		// Se non ho capitolo o impegno, sono a posto
		if(capitoloEntrataGestione == null || accertamento == null  || capitoloEntrataGestione.getUid() == 0 || accertamento.getUid() == 0) {
			return;
		}
		
		CapitoloEntrataGestione capitoloEntrataGestioneAccertamento = accertamento.getCapitoloEntrataGestione();
		checkCondition(capitoloEntrataGestioneAccertamento == null || (
				capitoloEntrataGestione.getNumeroCapitolo().equals(capitoloEntrataGestioneAccertamento.getNumeroCapitolo()) &&
				capitoloEntrataGestione.getNumeroArticolo().equals(capitoloEntrataGestioneAccertamento.getNumeroArticolo()) &&
				capitoloEntrataGestione.getNumeroUEB().equals(capitoloEntrataGestioneAccertamento.getNumeroUEB())
			), ErroreCore.VALORE_NON_VALIDO.getErrore("capitolo", "in quanto non corrisponde al capitolo dell'accertamento"));
	}
	
	/**
	 * Carica la lista del Tipo Causale.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio fallisca
	 */
	protected void caricaListaTipoCausale() throws WebServiceInvocationFailureException {
		List<TipoCausale> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_CAUSALE_ENTRATA);
		if(listaInSessione == null) {
			LeggiTipiCausaleEntrata request = model.creaRequestLeggiTipiCausaleEntrata();
			logServiceRequest(request);
			LeggiTipiCausaleEntrataResponse response = preDocumentoEntrataService.leggiTipiCausaleEntrata(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException("caricaListaTipoCausale");
			}
			
			listaInSessione = response.getTipiCausale();
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_CAUSALE_ENTRATA, listaInSessione);
		}
		
		model.setListaTipoCausale(listaInSessione);
	}
	
	/**
	 * Carica la lista della Causale Entrata.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio fallisca
	 */
	protected void caricaListaCausaleEntrata() throws WebServiceInvocationFailureException {
		List<CausaleEntrata> list = sessionHandler.getParametro(getParametroListaCausale());
		
		// Prima controllo se la il tipoCausale sia presente
		if(model.getTipoCausale() != null && model.getTipoCausale().getUid() != 0 && (list == null || list.isEmpty())) {
			RicercaSinteticaCausaleEntrata request = model.creaRequestRicercaSinteticaCausaleEntrata();
			logServiceRequest(request);
			RicercaSinteticaCausaleEntrataResponse response = preDocumentoEntrataService.ricercaSinteticaCausaleEntrata(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException("caricaListaCausaleEntrata");
			}
			list = response.getCausaliEntrata();
		}
		sessionHandler.setParametro(getParametroListaCausale(), list);
		model.setListaCausaleEntrata(list);
		
		// SIAC-4492: Devo aggiungere la causale del predoc se necessario
		addCausaleAnnullataPredoc();
	}

	/**
	 * Ottiene il parametro di sessione per la lista delle causali
	 * @return il parametro in cui sono salvati i dati della lista della causale
	 */
	protected BilSessionParameter getParametroListaCausale() {
		return BilSessionParameter.LISTA_CAUSALE_ENTRATA;
	}
	
	/**
	 * Aggiunge la causale annullata del predocumento
	 */
	protected void addCausaleAnnullataPredoc() {
		final String methodName = "addCausaleAnnullataPredoc";
		if(model.getCausaleOriginale() == null || model.getCausaleOriginale().getUid() == 0) {
			log.debug(methodName, "Causale originale non valorizzata: non aggiungo nulla");
			return;
		}
		CausaleEntrata ce = model.getCausaleOriginale();
		if(!StatoOperativoCausale.ANNULLATA.equals(ce.getStatoOperativoCausale())) {
			log.debug(methodName, "Causale non annullata: non aggiungo nella lista");
			return;
		}
		List<CausaleEntrata> list = model.getListaCausaleEntrata();
		CausaleEntrata ceInList = ComparatorUtils.searchByUidEventuallyNull(list, ce);
		if(ceInList != null) {
			log.debug(methodName, "Causale gia' in lista: non aggiungo");
			return;
		}
		log.debug(methodName, "Devo aggiungere la causale nella lista");
		list.add(ce);
	}
	
	/**
	 * Carica la lista del Conto Tesoreria.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio fallisca
	 */
	protected void caricaListaContoCorrente() throws WebServiceInvocationFailureException {
		//CR-4483
		List<ContoCorrentePredocumentoEntrata> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_CONTO_CORRENTE);
		if(listaInSessione == null) {
			LeggiClassificatoriByTipologieClassificatori request = model.creaRequestLeggiClassificatoriByTipologieClassificatori();
			logServiceRequest(request);
			LeggiClassificatoriByTipologieClassificatoriResponse response = classificatoreBilService.leggiClassificatoriByTipologieClassificatori(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException("caricaListaContoCorrente");
			}
			
			listaInSessione = response.extractByClass(ContoCorrentePredocumentoEntrata.class);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CONTO_CORRENTE, listaInSessione);
		}
		
		model.setListaContoCorrente(listaInSessione);
	}

	/**
	 * richiama la ricerca 
	 */
	protected void validazioneProvvisorioDiCassaPredocumentoDiEntrata(){
		if(model.getProvvisorioCassa() == null || model.getProvvisorioCassa().getNumero() == null || model.getProvvisorioCassa().getAnno() == null) {
			return;
		}
		
		final String methodName = "validazioneProvvisorioDiCassaPredocumentoDiEntrata";

		model.getProvvisorioCassa().setTipoProvvisorioDiCassa(TipoProvvisorioDiCassa.E);
		RicercaProvvisoriDiCassa request = model.creaRequestRicercaProvvisorioDiCassa();
		logServiceRequest(request);
		RicercaProvvisoriDiCassaResponse response = provvisorioService.ricercaProvvisoriDiCassa(request);
		logServiceResponse(response);

		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return;
		}

		checkCondition(response.getElencoProvvisoriDiCassa() != null && !response.getElencoProvvisoriDiCassa().isEmpty(), ErroreFin.DATI_PROVVISORIO_QUIETANZA_ERRATI.getErrore(),true);
		
		ProvvisorioDiCassa provvisorioDiCassaFromResponse = response.getElencoProvvisoriDiCassa().get(0);
		checkCondition(provvisorioDiCassaFromResponse != null, ErroreFin.DATI_PROVVISORIO_QUIETANZA_ERRATI.getErrore(),true);

		checkCondition(TipoProvvisorioDiCassa.E.equals(provvisorioDiCassaFromResponse.getTipoProvvisorioDiCassa())
				&& provvisorioDiCassaFromResponse.getDataRegolarizzazione() == null
				&& provvisorioDiCassaFromResponse.getDataAnnullamento() == null, ErroreFin.DATI_PROVVISORIO_QUIETANZA_ERRATI.getErrore());
		String keyProvvisorio = provvisorioDiCassaFromResponse.getAnno() +" - " + provvisorioDiCassaFromResponse.getNumero();
		model.setProvvisorioCassa(provvisorioDiCassaFromResponse);
		checkCondition(isProvvisorioDiCassaRegolarizzabile(model.getPreDocumento().getImporto()),
				ErroreFin.PROVVISORIO_NON_REGOLARIZZABILE.getErrore("inserimento","predisposizione di incasso", keyProvvisorio,
						"L'importo della predisposizione supera l'importo da regolarizzare del provvisorio"));
	}
	
	/**
	 * Imposta nel model i dati necessari ad un eventuale richiesta di conferma da parte dell'utente a partire dai dati ottenuti dal servizio. Viene anche gestita l'aggiunta degli errori da mostrare a video.
	 * @param serviceResponse la response da cui partire
	 * */
	protected void impostaErroriEDatiPerConfermaUtente(ServiceResponse serviceResponse) {
		final String methodName = "impostaDatiPerConfermaUtente";
		Boolean isAccertamentoResiduo = model.getMovimentoGestione()!= null &&  model.getMovimentoGestione().getUid()!= 0 && (model.getMovimentoGestione().getAnnoMovimento() == model.getAnnoEsercizioInt().intValue());
		// richiediConferma = true se: 1)si e' verificato l'errore di disponibilita accertamento insufficiente, 2) l'accertamento non e' residuo, 3) l'utente non ha ancora confermato l'eventuale adeguamento di disponibilita
		model.setRichiediConfermaUtente(!isAccertamentoResiduo && !model.getForzaDisponibilitaAccertamento() && serviceResponse.verificatoErrore(ErroreFin.DISPONIBILITA_INSUFFICIENTE_MOVIMENTO.getCodice()) && !model.getForzaDisponibilitaAccertamento());
		log.debug(methodName, "Invocazione del servizio di inserimento del predocumento di entrata terminata con fallimento");
		if(!model.getRichiediConfermaUtente()){
			addErrori(serviceResponse);
			return;
		}
		
		for(Errore e : serviceResponse.getErrori()){
			if(ErroreFin.DISPONIBILITA_INSUFFICIENTE_MOVIMENTO.getCodice().equals(e.getCodice())){
				StringBuilder messaggioRichiestaConfermaProsecuzione = new StringBuilder();
				messaggioRichiestaConfermaProsecuzione.append(e.getDescrizione()).append(" <br/> Si desidera adeguare l'importo del movimento?");
				model.setMessaggioRichiestaConfermaProsecuzione(messaggioRichiestaConfermaProsecuzione.toString());
				continue;
			}
			addErrore(e);
		}
	}
}
