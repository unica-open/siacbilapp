/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto.associa.movimento;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.result.CustomJSONResult;
import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.util.helper.VerificaBloccoRORHelper;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

/**
 * Classe di Action per l'associazione tra accertamento e allegato atto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(AssociaMovimentoAllegatoAttoBaseAction.MODEL_SESSION_NAME)
public class AssociaAccertamentoAllegatoAttoAction extends AssociaMovimentoAllegatoAttoBaseAction {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6675884812884895308L;

	/**
	 * Aggiunge un Accertamento al model.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String addAccertamento() {
		final String methodName = "addAccertamento";
		if(hasMessaggi()) {
			log.debug(methodName, "Errori di validazione dell'accertamento");
			return SUCCESS;
		}
		
		model.getListaSubdocumentoEntrata().add(0, model.getSubdocumentoEntrata());
		
		// Pulisco il model
		model.setSubdocumentoEntrata(null);
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * calcola il totale dei subdocumenti gia inseriti con su un determinato accertamento
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String calcolaDisponibilitaGiaImpegnata(){
		BigDecimal totaleSuImpegno = BigDecimal.ZERO;
		for(SubdocumentoEntrata s : model.getListaSubdocumentoEntrata()){
			if(model.getUidMovimento() == s.getAccertamento().getUid() &&  
				 ( (model.getNumeroSubmovimento() == null && (s.getSubAccertamento() == null || s.getSubAccertamento().getNumeroBigDecimal() == null))
					 || 
				   (model.getNumeroSubmovimento() != null && s.getSubAccertamento() != null && model.getNumeroSubmovimento().equals(s.getSubAccertamento().getNumeroBigDecimal()))
				 )
			  ){
				totaleSuImpegno = totaleSuImpegno.add(s.getImporto());
			}
		}
		model.setTotaleGiaImpegnato(totaleSuImpegno);
		model.setUidMovimento(null);
		model.setNumeroSubmovimento(null);
		return SUCCESS;
	}

	/**
	 * Preparazione per il metodo {@link #addAccertamento()}.
	 */
	public void prepareAddAccertamento() {
		model.setSubdocumentoEntrata(null);
		model.setProseguireConElaborazione(Boolean.FALSE);
	}


	
	/**
	 * Validazione per l'aggiunzione dell'accertamento.
	 */
	public void validateAddAccertamento() {
		// Controllo di avere l'accertamento
		checkCondition(model.getSubdocumentoEntrata() != null && model.getSubdocumentoEntrata().getAccertamento() != null,
			ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Accertamento"), true);
		
		SubdocumentoEntrata se = model.getSubdocumentoEntrata();
		Accertamento accertamento = se.getAccertamento();
		
		checkCondition(accertamento.getAnnoMovimento() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno"));
		//SIAC-7470: rimosso il seguente controllo al fine di effettuare i controlli su bloccoROR che prevedono l'inserimento di un  movimento
		//di annualità antecedente all'esercizio.
		//01/04/2020: il controllo è stato ripristinato, verrà gestito tramite le azioni (ne è stata creata una nuova "...noResImp") per evitare il conflitto 
		//CR-4440
		if(accertamento.getAnnoMovimento() != 0 && !isMovgestAssociabileDaUtente( accertamento)){
			addErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Account utente non abilitato alla gestione dei Residui negli Elenchi."));
			return;
		}
		
		
		// Minore o uguale all'anno di esercizio
		checkCondition(accertamento.getAnnoMovimento() <= model.getAnnoEsercizioInt(),
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("l'anno del movimento deve essere minore o uguale all'anno di esercizio"));
		checkNotNull(accertamento.getNumeroBigDecimal(), "Numero");
		checkNotNull(se.getImporto(), "Importo in atto");
		checkCondition(se.getImporto() == null || se.getImporto().signum() > 0,
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("l'importo in atto deve essere positivo"));
		
		if(!hasErrori()) {
			// Posso fare altri controlli
			validationAccertamento();
		}
	}

	/**
	 * Validazione logica dell'accertamento
	 */
	private void validationAccertamento() {
		final String methodName = "validationAccertamento";
		RicercaAccertamentoPerChiaveOttimizzatoResponse response = ricercaAccertamento();
		Accertamento accertamento = response.getAccertamento();
		if(accertamento == null) {
			return;
		}
		
		BigDecimal disponibilita = accertamento.getDisponibilitaIncassare();
		// Lo stato del movimento deve essere D-DEFINITIVO o N - NON LIQUIDABILE 
		checkCondition(StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(accertamento.getStatoOperativoMovimentoGestioneEntrata())
				|| StatoOperativoMovimentoGestione.DEFINITIVO_NON_LIQUIDABILE.getCodice().equals(accertamento.getStatoOperativoMovimentoGestioneEntrata()),
				ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("accertamento", accertamento.getStatoOperativoMovimentoGestioneEntrata()));
		if(response.getElencoSubAccertamentiTuttiConSoloGliIds() != null && !response.getElencoSubAccertamentiTuttiConSoloGliIds().isEmpty()) {
			log.debug(methodName, "Validazione dei subaccertamenti necessaria");
			disponibilita = validationSubAccertamento(accertamento.getSubAccertamenti());
		} else {
			checkCondition(hasErrori() || StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(accertamento.getStatoOperativoMovimentoGestioneEntrata()),
					ErroreFin.IMPEGNO_NON_IN_STATO_DEFINITIVO.getErrore());
			checkCoerenzaSoggettoMovimento(model.getSoggetto(), accertamento, "accertamento", model.getProseguireConElaborazione());
		}
		// L'importo in atto deve essere minore o uguale all'importo disponibile a liquidare per le spese e al disponibile ad incassare per le entrate
		BigDecimal importoDaVerificare = calcolaImportoSubdocumentiSuAccertamento(accertamento);
		checkCondition(disponibilita == null || importoDaVerificare.compareTo(disponibilita) <= 0,
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("l'importo in atto deve essere minore o uguale all'importo disponibile"));
		
		try {
			// Inserimento
			checkProvvisorio(model.getSubdocumentoEntrata().getProvvisorioCassa(), -1);
		} catch (ParamValidationException pve) {
			log.info(methodName, "Fallita validazione sul provvisorio di cassa");
		}
		
		//SIAC-7470 bloccoROR: devo controllare l'impegno prima di associarlo
		boolean result = VerificaBloccoRORHelper.escludiAccertamentoPerBloccoROR(sessionHandler.getAzioniConsentite(), accertamento, model.getAnnoEsercizioInt());
		if(result){
			checkCondition(!result, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Accertamento/sub accertamento residuo non utilizzabile"));
		}else if(accertamento.getElencoSubAccertamenti() != null && !accertamento.getElencoSubAccertamenti().isEmpty()){
			for(int k = 0; k < accertamento.getElencoSubAccertamenti().size(); k++){
				result = VerificaBloccoRORHelper.escludiAccertamentoPerBloccoROR(sessionHandler.getAzioniConsentite(), accertamento.getElencoSubAccertamenti().get(k), model.getAnnoEsercizioInt());
				if(result)
					break;
			}
			if(result){
				checkCondition(!result, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Accertamento/sub accertamento residuo non utilizzabile"));
			}
		}
		
		// Se ho errori, esco
		if(hasErrori()) {
			throw new ParamValidationException();
		}
		model.getSubdocumentoEntrata().setAccertamento(accertamento);
		model.setProseguireConElaborazione(Boolean.FALSE);
	}

	/**
	 * Ricerca dell'accertamento
	 * @return la response dell'accertamento
	 */
	private RicercaAccertamentoPerChiaveOttimizzatoResponse ricercaAccertamento() {
		final String methodName = "ricercaAccertamento";
		RicercaAccertamentoPerChiaveOttimizzato request = model.creaRequestRicercaAccertamentoPerChiaveOttimizzato();
		logServiceRequest(request);
		RicercaAccertamentoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaAccertamentoPerChiaveOttimizzato.class, response));
			addErrori(response);
			return response;
		}
		if(response.isFallimento()) {
			log.info(methodName, "Fallimento nell'invocazione del servizio RicercaAccertamentoPerChiaveOttimizzato");
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore(createErrorInServiceInvocationString(RicercaAccertamentoPerChiaveOttimizzato.class, response)));
		}
		return response;
	}

	/**
	 * Calcola l'importo dei subdocumenti afferenti a un dato accertamento
	 * @param accertamento l'accertamento da considerare
	 * @return il totale afferente l'accertamento
	 */
	private BigDecimal calcolaImportoSubdocumentiSuAccertamento(Accertamento accertamento) {
		String methodName = "calcolaImportoSubdocumentiSuAccertamento";
		SubdocumentoEntrata subdoc = model.getSubdocumentoEntrata();
		BigDecimal totaleSuAccertamento = subdoc.getImporto();
		List<SubdocumentoEntrata> listaSubdoc = model.getListaSubdocumentoEntrata();
		SubAccertamento subAccertamento = subdoc.getSubAccertamento();
		for(SubdocumentoEntrata s : listaSubdoc){
			if(isMovimentoCoerente(accertamento, subAccertamento, s.getAccertamento(), s.getSubAccertamento())) {
				log.debug(methodName, "ho trovato subdoc " + s.getUid() + " con lo stesso accertamento. Aggiungo importo: " + s.getImporto());
				totaleSuAccertamento = totaleSuAccertamento.add(s.getImporto());
			}
		}
		return totaleSuAccertamento;
	}
	
	/**
	 * Calcola l'importo dei subdocumenti afferenti a un dato provvisorio di cassa
	 * @param provvisorioDiCassa da considerare
	 * @param row la riga dell'oggetto su cui si sta operando, -1 se l'oggetto non e' ancora inserito
	 * @return il totale afferente il provvisorio di cassa
	 */
	private BigDecimal calcolaImportoSubdocumentiSuProvvisorioDiCassa(ProvvisorioDiCassa provvisorioDiCassa, int row) {
		String methodName = "calcolaImportoSubdocumentiSuProvvisorioDiCassa";
		BigDecimal totaleSuProvvisorioDiCassa = model.getSubdocumentoEntrata().getImporto();
		int rowTmp = 0;
		
		for(Iterator<SubdocumentoEntrata> iter = model.getListaSubdocumentoEntrata().iterator(); iter.hasNext(); rowTmp++) {
			SubdocumentoEntrata s = iter.next();
			if(isProvvisorioCoerente(provvisorioDiCassa, row, s.getProvvisorioCassa(), rowTmp)) {
				log.debug(methodName, "ho trovato subdoc " + s.getUid() + " con lo stesso provvisorio. Aggiungo importo: " + s.getImporto());
				totaleSuProvvisorioDiCassa = totaleSuProvvisorioDiCassa.add(s.getImporto());
			}
		}
		return totaleSuProvvisorioDiCassa;
	}
	
	/**
	 * Controlla se il provvisorio sia coerente.
	 * <br/>
	 * IL provvisorio &eacute; coerente se ha stesso uid ma riga differente.
	 * @param provvisorioDiCassa il provvisorio da controllare
	 * @param row la riga del dato in input
	 * @param provvisorioCassaSubdoc il provvisorio sulla quota
	 * @param rowTmp la riga della quota
	 * @return se il provvisorio sia coerente
	 */
	private boolean isProvvisorioCoerente(ProvvisorioDiCassa provvisorioDiCassa, int row, ProvvisorioDiCassa provvisorioCassaSubdoc, int rowTmp) {
		return provvisorioDiCassa != null && provvisorioCassaSubdoc != null && provvisorioDiCassa.getUid() == provvisorioCassaSubdoc.getUid()
				&& row != rowTmp;
	}

	/**
	 * Valida il subaccertamento.
	 * 
	 * @param subaccertamenti i subaccertamenti da cui estrarre il valore corretto
	 * 
	 * @return la disponibilit&agrave; relativa al subaccertamento selezionato
	 */
	private BigDecimal validationSubAccertamento(List<SubAccertamento> subaccertamenti) {
		final String methodName = "validationSubAccertamento";
		// Se un movimento ha dei sub si deve necessariamente indicarne uno
		checkCondition(model.getSubdocumentoEntrata().getSubAccertamento() != null && model.getSubdocumentoEntrata().getSubAccertamento().getNumeroBigDecimal() != null,
				ErroreFin.ACCERTAMENTO_CON_SUBACCERTAMENTI.getErrore());
		BigDecimal disponibilita = null;
		// Se ho errori, esco subito. In caso contrario, continuo con i controlli
		if(!hasErrori()) {
			SubAccertamento subAccertamento = null;
			BigDecimal numeroSubAccertamento = model.getSubdocumentoEntrata().getSubAccertamento().getNumeroBigDecimal();
			for(SubAccertamento sa : subaccertamenti) {
				if(sa.getNumeroBigDecimal().compareTo(numeroSubAccertamento) == 0) {
					subAccertamento = sa;
					break;
				}
			}
			if(log.isDebugEnabled()) {
				log.debug(methodName, "Subaccertamento ricercato nell'elenco tramite numero " + numeroSubAccertamento + ": "
						+ (subAccertamento == null ? "null" : subAccertamento.getUid()));
			}
			checkCondition(subAccertamento != null, ErroreFin.ACCERTAMENTO_CON_SUBACCERTAMENTI.getErrore());
			disponibilita = BigDecimal.ZERO;
			
			if(subAccertamento != null) {
				// Il sub deve essere in stato D-DEFINITIVO
				checkCondition(StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(subAccertamento.getStatoOperativoMovimentoGestioneEntrata()),
						ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("subaccertamento", subAccertamento.getStatoOperativoMovimentoGestioneEntrata()));
				
				checkCondition(subAccertamento.getSoggetto() != null && subAccertamento.getSoggetto().getUid() == model.getSoggetto().getUid(),
					ErroreFin.SOGGETTO_DIVERSO_DA_QUELLO_DEL_MOVIMENTO.getErrore("subaccertamento"));
				disponibilita = subAccertamento.getDisponibilitaIncassare();
			}
			
			// imposto i dati nel model
			model.getSubdocumentoEntrata().setSubAccertamento(subAccertamento);
		}
		return disponibilita;
	}
	
	/**
	 * Preparazione per il metodo {@link #removeAccertamento()}.
	 */
	public void prepareRemoveAccertamento() {
		model.setRow(null);
	}

	/**
	 * Rimuove un Accertamento dal model.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String removeAccertamento() {
		final String methodName = "removeAccertamento";
		int row = model.getRow().intValue();
		model.getListaSubdocumentoEntrata().remove(row);
		log.debug(methodName, "Accertamento rimosso");
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Validazione per la rimozione dell'accertamento.
	 */
	public void validateRemoveAccertamento() {
		final String methodName = "validateRemoveAccertamento";
		Integer rowObj = model.getRow();
		log.debug(methodName, "Rimozione dell'accertamento alla riga " + rowObj);
		// Se non passo la riga, me ne esco
		if(rowObj == null) {
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Accertamento"));
			return;
		}
		int row = rowObj.intValue();
		List<SubdocumentoEntrata> list = model.getListaSubdocumentoEntrata();
		if(row < 0 || row >= list.size()) {
			log.debug(methodName, "Riga non valida: " + row + " non e' compreso tra 0 e " + list.size());
			addErrore(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Riga da eliminare", "deve essere compreso tra 0 e " + list.size()));
		}
	}
	
	/**
	 * Preparazione per il metodo {@link #alterAccertamento()}.
	 */
	public void prepareAlterAccertamento() {
		model.setProseguireConElaborazione(Boolean.FALSE);
		model.setSubdocumentoEntrata(null);
	}
	
	/**
	 * Modifica un accertamento presente nel model.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String alterAccertamento() {
		final String methodName = "alterAccertamento";
		if(hasMessaggi()) {
			log.debug(methodName, "Errori di validazione del dato");
			return INPUT;
		}
		int row = model.getRow().intValue();
		populateSubdocumentoEntrata(model.getSubdocumentoEntrata(), model.getListaSubdocumentoEntrata().get(row));
		log.debug(methodName, "Sostituisco il dato della riga " + row + " con quello nuovo");
		model.getListaSubdocumentoEntrata().set(row, model.getSubdocumentoEntrata());
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Validazione per l'aggiornamento dell'accertamento.
	 */
	public void validateAlterAccertamento() {
		final String methodName = "validateAlterAccertamento";
		Integer rowObj = model.getRow();
		if(rowObj == null) {
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Accertamento"));
			return;
		}
		int row = rowObj.intValue();
		List<SubdocumentoEntrata> list = model.getListaSubdocumentoEntrata();
		if(row < 0 || row >= list.size()) {
			log.debug(methodName, "Riga non valida: " + row + " non e' compreso tra 0 e " + list.size());
			addErrore(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Riga da aggiornare", "deve essere compreso tra 0 e " + list.size()));
			return;
		}
		SubdocumentoEntrata se = model.getSubdocumentoEntrata();
		SubdocumentoEntrata seInLista = list.get(row);
		
		// Validazione del dato
		checkNotNull(se.getImporto(), "importo in atto");
		checkCondition(se.getImporto() == null || se.getImporto().signum() > 0,
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("l'importo in atto deve essere positivo"));
		
		BigDecimal disponibilitaIncassare = seInLista.getSubAccertamento() != null && seInLista.getSubAccertamento().getUid() != 0
				? seInLista.getSubAccertamento().getDisponibilitaIncassare()
				: seInLista.getAccertamento().getDisponibilitaIncassare();
		
		// Al peggio, la disponibilita' e' 0
		disponibilitaIncassare = disponibilitaIncassare != null ? disponibilitaIncassare : BigDecimal.ZERO;
		
		checkCondition(se.getImporto() == null || se.getImporto().compareTo(disponibilitaIncassare) <= 0,
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("l'importo in atto (" + FormatUtils.formatCurrency(se.getImporto())
					+ ") deve essere minore o uguale all'importo disponibile (" + FormatUtils.formatCurrency(disponibilitaIncassare) + ")"));
		
		checkProvvisorio(seInLista.getProvvisorioCassa(), row);
	}
	@Override
	protected void addMessaggioForTipoMovimento() {
		addMessaggio(ErroreFin.PRESENZA_CLASSIFICAZIONE_SOGGETTO.getErrore("accertamento"));
	}


	/**
	 * Popolazione del subdocumento di spesa.
	 * 
	 * @param subdocumento    il nuovo subdocumento
	 * @param subdocumentoOld il vecchio subdocumento
	 */
	private void populateSubdocumentoEntrata(SubdocumentoEntrata subdocumento, SubdocumentoEntrata subdocumentoOld) {
		subdocumento.setAccertamento(subdocumentoOld.getAccertamento());
		subdocumento.setSubAccertamento(subdocumentoOld.getSubAccertamento());
	}
	
	/**
	 * Classe di Result per semplificare la destione delle propriet&agrave; serializzate.
	 * 
	 * @author Marchino Alessandro
	 * @version 1.0.0 - 08/10/2014
	 *
	 */
	public static class AssociaAccertamentoAllegatoAttoJSONResult extends CustomJSONResult {
		
		/** For serialization purpose */
		private static final long serialVersionUID = 139076566797519229L;
		private static final String INCLUDE_PROPERTIES = "errori.*,messaggi.*,informazioni.*,listaSubdocumentoEntrata.*,totaleEntrata";

		/** Empty default constructor */
		public AssociaAccertamentoAllegatoAttoJSONResult() {
			super();
			setIncludeProperties(INCLUDE_PROPERTIES);
		}
	}
	/**
	 * Carica il provvisorio di cassa per la quota.
	 */
	private void checkProvvisorio(ProvvisorioDiCassa pdc, int row) {
		
		if(pdc == null){
			return;
		}
		if(pdc.getNumero() == null && pdc.getAnno() == null){
			model.getSubdocumentoEntrata().setProvvisorioCassa(null);
			return;
		}
		
		if(pdc.getNumero() == null || pdc.getAnno() == null){
			addErrore(ErroreCore.FORMATO_NON_VALIDO.getErrore("numero e anno provvisorio di cassa", ": non possono essere valorizzati singolarmente"));
			return;
		}
		
		ProvvisorioDiCassa provvisorioDiCassa = pdc;
		if(provvisorioDiCassa.getUid() == 0) {
			provvisorioDiCassa = ricercaProvvisorioDiCassaEntrata();
		}
		checkCondition(provvisorioDiCassa != null , ErroreCore.ENTITA_NON_TROVATA.getErrore("provvisorio di cassa", provvisorioDiCassa.getAnno()
				 +  "/" + provvisorioDiCassa.getNumero()), true);
		
		BigDecimal importoDaVerificare = calcolaImportoSubdocumentiSuProvvisorioDiCassa(provvisorioDiCassa, row);
		checkCondition(importoDaVerificare.compareTo(provvisorioDiCassa.getImportoDaRegolarizzare()) <= 0,
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("l'importo in atto deve essere minore o uguale all'importo da regolarizzare sul provvisorio di cassa"));
		
		model.getSubdocumentoEntrata().setProvvisorioCassa(provvisorioDiCassa);
		
	}
}
