/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto.associa.movimento;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.result.CustomJSONResult;
import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.util.helper.VerificaBloccoRORHelper;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;

/**
 * Classe di Action per l'associazione tra l'impegno e allegato atto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(AssociaMovimentoAllegatoAttoBaseAction.MODEL_SESSION_NAME)
public class AssociaImpegnoAllegatoAttoAction extends AssociaMovimentoAllegatoAttoBaseAction {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4003826887316718469L;
	/** Pattern per il CIG */
	private static final Pattern CIG_PATTERN = Pattern.compile("[A-Z0-9]{10}");
	/** Pattern per il Cup */
	private static final Pattern CUP_PATTERN = Pattern.compile("[A-Z][A-Z0-9]{2}[A-Z][A-Z0-9]{11}");	
	/**
	 * Preparazione per il metodo {@link #addImpegno()}.
	 */
	public void prepareAddImpegno() {
		model.setSubdocumentoSpesa(null);
		model.setProseguireConElaborazione(Boolean.FALSE);
	}
	
	/**
	 * Aggiunge un Impegno al model.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String addImpegno() {
		final String methodName = "addImpegno";
		if(hasMessaggi()) {
			log.debug(methodName, "Richiesta di conferma per la validazione");
			return INPUT;
		}
		log.debug(methodName, "Impegno valido per l'insermento: lo aggiungo alla lista");
		model.aggiungiSubdocumentoSpesaAllaLista();
		// Pulisco il model
		model.setSubdocumentoSpesa(null);
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * calcola il totale dei subdocumenti gia inseriti con su un determinato impegno
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String calcolaDisponibilitaGiaImpegnata(){
		BigDecimal totaleSuImpegno = BigDecimal.ZERO;
		int uidMovimento = model.getUidMovimento() != null ? model.getUidMovimento().intValue() : 0;
		for(SubdocumentoSpesa s : model.getListaSubdocumentoSpesa()){
			if(uidMovimento == s.getImpegno().getUid() && 
				( (model.getNumeroSubmovimento() == null && (s.getSubImpegno() == null || s.getSubImpegno().getNumeroBigDecimal() == null))
							 || 
				 (model.getNumeroSubmovimento() != null && s.getSubImpegno() != null && model.getNumeroSubmovimento().equals(s.getSubImpegno().getNumeroBigDecimal()))
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
	 * Validazione per l'aggiunzione dell'impegno.
	 */
	public void validateAddImpegno() {
		// Controllo di avere l'accertamento
		if(model.getSubdocumentoSpesa() == null || model.getSubdocumentoSpesa().getImpegno() == null) {
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Impegno"));
			return;
		}
		SubdocumentoSpesa ss = model.getSubdocumentoSpesa();
		Impegno impegno = ss.getImpegno();
		checkCondition(impegno.getAnnoMovimento() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno"));
		//SIAC-7470: rimosso il seguente controllo al fine di effettuare i controlli su bloccoROR che prevedono l'inserimento di un  movimento
		//di annualità antecedente all'esercizio
		//01/04/2020: il controllo è stato ripristinato, verrà gestito tramite le azioni (ne è stata creata una nuova "...noResImp") per evitare il conflitto 
		//CR-4440
		if(impegno.getAnnoMovimento() != 0 && !isMovgestAssociabileDaUtente( impegno)){
			addErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Account utente non abilitato alla gestione dei Residui negli Elenchi."));
			return;
		}
		
		checkCondition(impegno.getAnnoMovimento() <= model.getAnnoEsercizioInt(),
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("l'anno del movimento deve essere minore o uguale all'anno di esercizio"));
		
		
		checkNotNull(impegno.getNumeroBigDecimal(), "Numero");
		checkNotNull(ss.getImporto(), "Importo in atto");
		checkCondition(ss.getImporto() == null || ss.getImporto().signum() > 0,
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("l'importo in atto deve essere positivo"));
		String cig = ss.getCig();
		String cup = ss.getCup();
		
		// Check CIG-CUP
		checkCorrettezzaFormatoCigECup(cig, cup);
		
		if(!hasErrori()) {
			// Posso fare altri controlli
			validationImpegno(cig, cup);
		}
	}

	/**
	 * Validazione logica dell'impegno.
	 * 
	 * @param cig il CIG da controllare
	 * @param cup il CUP da controllare
	 */
	private void validationImpegno(String cig, String cup) {
		final String methodName = "validationImpegno";
		RicercaImpegnoPerChiaveOttimizzatoResponse response = ricercaImpegno();
 		Impegno impegno = response.getImpegno();
		if(impegno == null){
			return;
		}
		
		BigDecimal disponibilita = impegno.getDisponibilitaLiquidare();
		checkCondition(impegno.isValidato(), ErroreFin.IMPEGNO_NON_IN_STATO_DEFINITIVO_NON_LIQUIDABILE.getErrore("impegno non validato"));
		
		boolean controlloCigCupDaEffettuare = true;
		if(response.getElencoSubImpegniTuttiConSoloGliIds() != null && !response.getElencoSubImpegniTuttiConSoloGliIds().isEmpty()) {
			// Lo stato del movimento deve essere D-DEFINITIVO o N - NON LIQUIDABILE
			// Controllo spostato sotto in quanto, se non dovessi avere subimpegni, il controllo sarebbe differente
			checkCondition(StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(impegno.getStatoOperativoMovimentoGestioneSpesa())
					|| StatoOperativoMovimentoGestione.DEFINITIVO_NON_LIQUIDABILE.getCodice().equals(impegno.getStatoOperativoMovimentoGestioneSpesa()),
					ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("impegno", impegno.getStatoOperativoMovimentoGestioneSpesa()));
			controlloCigCupDaEffettuare = false;
			log.debug(methodName, "Necessario validare subImpegno");
			disponibilita = validationSubImpegno(impegno.getElencoSubImpegni(), cig, cup);
		} else {
			// 2.10.1
			checkCondition(hasErrori() || StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(impegno.getStatoOperativoMovimentoGestioneSpesa()),
					ErroreFin.IMPEGNO_NON_IN_STATO_DEFINITIVO.getErrore());
			checkCoerenzaSoggettoMovimento(model.getSoggetto(), impegno, "impegno", model.getProseguireConElaborazione());
			
		}
		// L'importo in atto deve essere minore o uguale all'importo disponibile a liquidare per le spese e al disponibile ad incassare per le entrate
		BigDecimal importoDaVerificare = calcolaImportoSubdocumentiSuImpegno(impegno);
		checkCondition(hasErrori() || importoDaVerificare.compareTo(disponibilita) <= 0,
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("l'importo in atto deve essere minore o uguale all'importo disponibile"));
		

		if(controlloCigCupDaEffettuare) {
			validazioneCigCup(cig, cup, impegno);
		}
		try {
			// Inserimento
			checkProvvisorio(model.getSubdocumentoSpesa().getProvvisorioCassa(), -1);
		} catch (ParamValidationException pve) {
			log.info(methodName, "Fallita validazione sul provvisorio di cassa");
		}
		
		//SIAC-7470 bloccoROR: devo controllare l'impegno prima di associarlo
		boolean result = VerificaBloccoRORHelper.escludiImpegnoPerBloccoROR(sessionHandler.getAzioniConsentite(), impegno, model.getAnnoEsercizioInt());
		if(result){
			checkCondition(!result, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Impegno/sub impegno residuo non utilizzabile"));
		}else if(impegno.getElencoSubImpegni() != null && !impegno.getElencoSubImpegni().isEmpty()){
			for(int k = 0; k < impegno.getElencoSubImpegni().size(); k++){
				result = VerificaBloccoRORHelper.escludiImpegnoPerBloccoROR(sessionHandler.getAzioniConsentite(), impegno.getElencoSubImpegni().get(k), model.getAnnoEsercizioInt());
				if(result)
					break;
			}
			if(result){
				checkCondition(!result, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Impegno/sub impegno residuo non utilizzabile"));
			}
		}
		
		
		// Se ho errori, esco
		if(hasErrori() || hasMessaggi()) {
			throw new ParamValidationException();
		}
		
		model.getSubdocumentoSpesa().setImpegno(impegno);
		// Imposto la sede secondaria del soggetto se valorizzata (campo opzionale)
		if(model.getSedeSecondariaSoggetto() != null && model.getSedeSecondariaSoggetto().getUid() != 0) {
			model.getSubdocumentoSpesa().setSedeSecondariaSoggetto(model.getSedeSecondariaSoggetto());
		}
		// Imposto la modalita di pagamento del soggetto
		model.getSubdocumentoSpesa().setModalitaPagamentoSoggetto(model.getModalitaPagamentoSoggetto());
		// Ripristino il valore di default
		model.setProseguireConElaborazione(Boolean.FALSE);
	}
	

	/**
	 * Calcola l'importo dei subdocumenti afferenti a un dato impegno
	 * @param impegno l'impegno da considerare
	 * @return il totale afferente l'impegno
	 */
	private BigDecimal calcolaImportoSubdocumentiSuImpegno(Impegno impegno) {
		String methodName = "calcolaImportoSubdocumentiSuImpegno";
		SubdocumentoSpesa subdoc = model.getSubdocumentoSpesa();
		BigDecimal totaleSuImpegno = subdoc.getImporto();
		log.debug(methodName, "importo del subdoc: " + totaleSuImpegno);
		List<SubdocumentoSpesa> listaSubdoc = model.getListaSubdocumentoSpesa();
		SubImpegno subImpegno = subdoc.getSubImpegno();
		for(SubdocumentoSpesa s : listaSubdoc){
			if(isMovimentoCoerente(impegno, subImpegno, s.getImpegno(), s.getSubImpegno())) {
				totaleSuImpegno = totaleSuImpegno.add(s.getImporto());
			}
		}
		return totaleSuImpegno;
	}
	
	/**
	 * Ricerca dell'impegno per chiave
	 * @return la response dell'impegno
	 */
	private RicercaImpegnoPerChiaveOttimizzatoResponse ricercaImpegno() {
		final String methodName = "ricercaImpegno";
		RicercaImpegnoPerChiaveOttimizzato request = model.creaRequestRicercaImpegnoPerChiaveOttimizzato();
		logServiceRequest(request);
		RicercaImpegnoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaImpegnoPerChiaveOttimizzato.class, response));
			addErrori(response);
			return response;
		}
		if(response.isFallimento()) {
			log.info(methodName, "Fallimento nell'invocazione del servizio " + RicercaImpegnoPerChiave.class.getSimpleName());
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore(createErrorInServiceInvocationString(RicercaImpegnoPerChiaveOttimizzato.class, response)));
		}
		
		return response;
	}

	/**
	 * Valida il subimpegno.
	 * 
	 * @param subimpegni i subimpegno da cui estrarre il valore corretto
	 * @param cig il CIG da controllare
	 * @param cup il CUP da controllare
	 * 
	 * @return la disponibilit&agrave; relativa al subimpegno selezionato
	 */
	private BigDecimal validationSubImpegno(List<SubImpegno> subimpegni, String cig, String cup) {
		final String methodName = "validationSubImpegno";
		// Se un movimento ha dei sub si deve necessariamente indicarne uno
		checkCondition(model.getSubdocumentoSpesa().getSubImpegno() != null && model.getSubdocumentoSpesa().getSubImpegno().getNumeroBigDecimal() != null,
				ErroreFin.IMPEGNO_CON_SUBIMPEGNI.getErrore());
		BigDecimal disponibilita = BigDecimal.ZERO;
		// Se ho errori, esco subito. In caso contrario, continuo con i controlli
		if(!hasErrori()) {
			SubImpegno subImpegno = null;
			BigDecimal numeroSubImpegno = model.getSubdocumentoSpesa().getSubImpegno().getNumeroBigDecimal();
			for(SubImpegno si : subimpegni) {
				if(si.getNumeroBigDecimal().compareTo(numeroSubImpegno) == 0) {
					subImpegno = si;
					break;
				}
			}
			if(log.isDebugEnabled()) {
				log.debug(methodName, "Subimpegno ricercato nell'elenco tramite numero " + numeroSubImpegno + ": "
						+ (subImpegno == null ? "null" : subImpegno.getUid()));
			}
			checkCondition(subImpegno != null, ErroreFin.IMPEGNO_CON_SUBIMPEGNI.getErrore());
			disponibilita = BigDecimal.ZERO;
			
			if(subImpegno != null) {
				// Il sub deve essere in stato D-DEFINITIVO
				checkCondition(StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(subImpegno.getStatoOperativoMovimentoGestioneSpesa()),
						ErroreFin.SUBIMPEGNO_NON_IN_STATO_DEFINITIVO.getErrore());
				checkCondition(subImpegno.getSoggetto() != null && model.getSoggetto().getUid() == subImpegno.getSoggetto().getUid(),
						ErroreFin.SOGGETTO_DIVERSO_DA_QUELLO_DEL_MOVIMENTO.getErrore("subimpegno"));
				
				validazioneCigCup(cig, cup, subImpegno);
				
				disponibilita = subImpegno.getDisponibilitaLiquidare();
			}
			
			// Imposto i dati nel model
			model.getSubdocumentoSpesa().setSubImpegno(subImpegno);
		}
		return disponibilita;
	}
	
	/**
	 * Validazione del CIG e del CUP.
	 * 
	 * @param cig     il cig fornito
	 * @param cup     il cup fornito
	 * @param impegno l'impegno rispetto cui controllare
	 */
	private void validazioneCigCup(String cig, String cup, Impegno impegno) {
		// Validazione CIG-CUP
		checkCorrettezzaFormatoCigECup(cig, cup);		
		if(!Boolean.TRUE.equals(model.getProseguireConElaborazione())) {
			warnCondition((StringUtils.isBlank(cig) || cig.equalsIgnoreCase(impegno.getCig()))
					&& (StringUtils.isBlank(cup) || cup.equalsIgnoreCase(impegno.getCup())),
					ErroreFin.CIG_CUP_DIFFERENTI_DA_QUELLI_DEL_MOVIMENTO_SELEZIONATO.getErrore());
		}
		
		// SIAC-5311 SIOPE+
		// Assenza CIG: Si', se il Tipo debito Siope del movimento selezionato (impegno o subimpegno) e' di tipo COMMERCIALE e non e' presente il campo CIG.
		checkSiope(cig, impegno);
	}
	
	/**
	 * Controllo dei dati del SIOPE+
	 * @param cig il cig
	 * @param impegno l'impegno
	 */
	private void checkSiope(String cig,Impegno impegno) {
		final String methodName = "checkSiope"; 
		
		boolean cigValorizzato = StringUtils.isNotBlank(cig);
		boolean motivoAssenzaValorizzato = model.getSubdocumentoSpesa().getSiopeAssenzaMotivazione() != null && model.getSubdocumentoSpesa().getSiopeAssenzaMotivazione().getUid() != 0;
		SiopeTipoDebito siopeTipoDebito =  impegno.getSiopeTipoDebito();
		if(siopeTipoDebito == null || siopeTipoDebito.getUid() == 0) {
			log.debug(methodName, "tipo debito siope non presente.");
			return;
		}
		
		//cig e cup non possono essere entrambi valorizzati
		checkCondition(!(cigValorizzato && motivoAssenzaValorizzato), ErroreCore.FORMATO_NON_VALIDO.getErrore("cig o motivo assenza cig", "non possono essere entrambi valorizzati."));
		
		//se tipo debito siope commerciale, almeno uno dei due e' obbligatorio
		checkCondition( !BilConstants.CODICE_SIOPE_DEBITO_TIPO_COMMERCIALE.getConstant().equals(siopeTipoDebito.getCodice())
				|| cigValorizzato
				|| motivoAssenzaValorizzato,
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("cig o motivo di assenza CIG"));
		checkMotivoAssenzaSiope();
	}

	/**
	 * 
	 */
	private void checkMotivoAssenzaSiope() {
		final String methodName = "checkMotivoAssenzaSiope";
		SiopeAssenzaMotivazione assenzaMotivazioneWithCodice = ComparatorUtils.searchByUidEventuallyNull(model.getListaSiopeAssenzaMotivazione(), model.getSubdocumentoSpesa().getSiopeAssenzaMotivazione());
		if(assenzaMotivazioneWithCodice == null || assenzaMotivazioneWithCodice.getUid() == 0) {
			//se non ho specificato un motivo di assenza cig, non posso e non devo controllarlo
			log.debug(methodName, "motivo assenza cig non presente: non devo effettuare controlli.");
			return;
		}
		//SIAC-5526	
		String siopeAssenzaDaDefinire = BilConstants.CODICE_SIOPE_ASSENZA_MOTIVAZIONE_DA_DEFINIRE_IN_LIQUIDAZIONE.getConstant();
		checkCondition(!siopeAssenzaDaDefinire.equals(assenzaMotivazioneWithCodice.getCodice()), 
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("motivo assenza cig :"  + siopeAssenzaDaDefinire,"selezionare una motivazione valida"));
		//SIAC-5565
		String siopeAssenzaInCorsoDiDefinizione = BilConstants.CODICE_SIOPE_ASSENZA_MOTIVAZIONE_IN_CORSO_DEFINIZIONE.getConstant();
		checkCondition(!siopeAssenzaInCorsoDiDefinizione.equals(assenzaMotivazioneWithCodice.getCodice()), 
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("motivo assenza cig " + siopeAssenzaInCorsoDiDefinizione,"selezionare una motivazione valida"));
	}
	

	
	
	/**
	 * Preparazione per il metodo {@link #removeImpegno()}.
	 */
	public void prepareRemoveImpegno() {
		model.setRow(null);
	}
	
	/**
	 * Rimuove un Impegno dal model.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String removeImpegno() {
		final String methodName = "removeImpegno";
		int row = model.getRow().intValue();
		model.getListaSubdocumentoSpesa().remove(row);
		log.debug(methodName, "Impegno rimosso");
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Validazione per la rimozione dell'impegno.
	 */
	public void validateRemoveImpegno() {
		final String methodName = "validateRemoveImpegno";
		Integer rowObj = model.getRow();
		log.debug(methodName, "Rimozione dell'impegno alla riga " + rowObj);
		// Se non passo la riga, me ne esco
		if(rowObj == null) {
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Impegno"));
			return;
		}
		int row = rowObj.intValue();
		List<SubdocumentoSpesa> list = model.getListaSubdocumentoSpesa();
		if(row < 0 || row >= list.size()) {
			log.debug(methodName, "Riga non valida: " + row + " non e' compreso tra 0 e " + list.size());
			addErrore(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Riga da eliminare", "deve essere compreso tra 0 e " + list.size()));
		}
	}
	
	/**
	 * Preparazione per il metodo {@link #alterImpegno()}.
	 */
	public void prepareAlterImpegno() {
		model.setProseguireConElaborazione(Boolean.FALSE);
		model.setSubdocumentoSpesa(null);
	}
	
	/**
	 * Modifica un impegno presente nel model.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String alterImpegno() {
		final String methodName = "alterImpegno";
		if(hasMessaggi()) {
			log.debug(methodName, "Richiesta di conferma per la validazione");
			return INPUT;
		}
		int row = model.getRow().intValue();
		populateSubdocumentoSpesa(model.getSubdocumentoSpesa(), model.getListaSubdocumentoSpesa().get(row));
		log.debug(methodName, "Sostituisco il dato della riga " + row + " con quello nuovo");
		model.getListaSubdocumentoSpesa().set(row, model.getSubdocumentoSpesa());
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Validazione per l'aggiornamento dell'impegno.
	 */
	public void validateAlterImpegno() {
		final String methodName = "validateAlterImpegno";
		
		// Inizializzo l'originale
		Integer rowObj = model.getRow();
		if(rowObj == null) {
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Impegno"));
			return;
		}
		int row = rowObj.intValue();
		List<SubdocumentoSpesa> list = model.getListaSubdocumentoSpesa();
		if(row < 0 || row >= list.size()) {
			log.debug(methodName, "Riga non valida: " + row + " non e' compreso tra 0 e " + list.size());
			addErrore(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Riga da aggiornare", "deve essere compreso tra 0 e " + list.size()));
			return;
		}
		
		SubdocumentoSpesa ss = model.getSubdocumentoSpesa();
		SubdocumentoSpesa ssInLista = list.get(row);
		
		// Validazione del dato
		checkNotNull(ss.getImporto(), "importo in atto");
		checkCondition(ss.getImporto() == null || ss.getImporto().signum() > 0,
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("l'importo in atto deve essere positivo"));
		
		BigDecimal disponibilitaLiquidare = ssInLista.getSubImpegno() != null && ssInLista.getSubImpegno().getUid() != 0
				? ssInLista.getSubImpegno().getDisponibilitaLiquidare()
				: ssInLista.getImpegno().getDisponibilitaLiquidare();
		
		// Al peggio, la disponibilita' e' 0
		disponibilitaLiquidare = disponibilitaLiquidare != null ? disponibilitaLiquidare : BigDecimal.ZERO;
		
		checkCondition(ss.getImporto() == null || ss.getImporto().compareTo(disponibilitaLiquidare) <= 0,
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("l'importo in atto (" + FormatUtils.formatCurrency(ss.getImporto())
					+ ") deve essere minore o uguale all'importo disponibile (" + FormatUtils.formatCurrency(disponibilitaLiquidare) + ")"));
		
		checkProvvisorio(ssInLista.getProvvisorioCassa(), row);
		
		checkSiope(ss.getCig(), ssInLista.getSubImpegno() != null && ssInLista.getSubImpegno().getUid() != 0 ? ssInLista.getSubImpegno() : ssInLista.getImpegno());
	}

	@Override
	protected void addMessaggioForTipoMovimento() {
		addMessaggio(ErroreFin.PRESENZA_CLASSIFICAZIONE_SOGGETTO.getErrore("impegno"));
	}
	
	/**
	 * Popolazione del subdocumento di spesa.
	 * 
	 * @param subdocumento    il nuovo subdocumento
	 * @param subdocumentoOld il vecchio subdocumento
	 */
	private void populateSubdocumentoSpesa(SubdocumentoSpesa subdocumento, SubdocumentoSpesa subdocumentoOld) {
		subdocumento.setImpegno(subdocumentoOld.getImpegno());
		subdocumento.setSubImpegno(subdocumentoOld.getSubImpegno());
		subdocumento.setSiopeTipoDebito(subdocumentoOld.getSiopeTipoDebito());
		
		SiopeAssenzaMotivazione siopeAssenzaMotivazione = ComparatorUtils.searchByUidEventuallyNull(model.getListaSiopeAssenzaMotivazione(), subdocumento.getSiopeAssenzaMotivazione());
		subdocumento.setSiopeAssenzaMotivazione(siopeAssenzaMotivazione);
		
		subdocumento.setSedeSecondariaSoggetto(subdocumentoOld.getSedeSecondariaSoggetto());
		subdocumento.setModalitaPagamentoSoggetto(subdocumentoOld.getModalitaPagamentoSoggetto());
	}
	
	/**
	 * Classe di Result per semplificare la destione delle propriet&agrave; serializzate.
	 * 
	 * @author Marchino Alessandro
	 * @version 1.0.0 - 08/10/2014
	 *
	 */
	public static class AssociaImpegnoAllegatoAttoJSONResult extends CustomJSONResult {
		
		/** For serialization purpose */
		private static final long serialVersionUID = 139076566797519229L;
		private static final String INCLUDE_PROPERTIES = "errori.*,messaggi.*,informazioni.*,listaSubdocumentoSpesa.*,totaleSpesa";

		/** Empty default constructor */
		public AssociaImpegnoAllegatoAttoJSONResult() {
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
			model.getSubdocumentoSpesa().setProvvisorioCassa(null);
			return;
		}
		
		if(pdc.getNumero() == null || pdc.getAnno() == null){
			addErrore(ErroreCore.FORMATO_NON_VALIDO.getErrore("numero e anno provvisorio di cassa", ": non possono essere valorizzati singolarmente"));
			return;
		}
		
		ProvvisorioDiCassa provvisorioDiCassa = pdc;
		
		if(provvisorioDiCassa.getUid() == 0) {
			provvisorioDiCassa = ricercaProvvisorioDiCassaSpesa();
		}
		
		checkCondition(provvisorioDiCassa != null , ErroreCore.ENTITA_NON_TROVATA.getErrore("provvisorio di cassa", pdc.getAnno()
				 +  "/" + pdc.getNumero()), true);
		
		BigDecimal importoDaVerificare = calcolaImportoSubdocumentiSuProvvisorioDiCassa(provvisorioDiCassa, row);
		checkCondition(importoDaVerificare.compareTo(provvisorioDiCassa.getImportoDaRegolarizzare()) <= 0,
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("l'importo in atto deve essere minore o uguale all'importo da regolarizzare sul provvisorio di cassa"));
		
		model.getSubdocumentoSpesa().setProvvisorioCassa(provvisorioDiCassa);
		
	}
	
	/**
	 * Calcola l'importo dei subdocumenti afferenti a un dato provvisorio di cassa
	 * @param provvisorioDiCassa da considerare
	 * @param row la riga dell'oggetto su cui si sta operando, -1 se l'oggetto non e' ancora inserito
	 * @return il totale afferente il provvisorio di cassa
	 */
	private BigDecimal calcolaImportoSubdocumentiSuProvvisorioDiCassa(ProvvisorioDiCassa provvisorioDiCassa, int row) {
		String methodName = "calcolaImportoSubdocumentiSuProvvisorioDiCassa";
		BigDecimal totaleSuProvvisorioDiCassa = model.getSubdocumentoSpesa().getImporto();
		int rowTmp = 0;
		
		for(Iterator<SubdocumentoSpesa> iter = model.getListaSubdocumentoSpesa().iterator(); iter.hasNext(); rowTmp++) {
			SubdocumentoSpesa s = iter.next();
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
	 * Controlla che il formato di CIG e CUP sia corretto (10 caratteri per il cig, 15 caratteri di cui il primo e il quarto lettara per il cup)
	 * @param cig il CIG da controllare
	 * @param cup il CUP da controllare
	 * */	
	protected void checkCorrettezzaFormatoCigECup(String cig, String cup) {
		if(!(StringUtils.isBlank(cig) || CIG_PATTERN.matcher(cig).matches())){
			addErrore(ErroreCore.FORMATO_NON_VALIDO.getErrore("CIG", "Deve essere composto da dieci caratteri"));
			model.setProseguireConElaborazione(Boolean.TRUE);
		}
		if(!(StringUtils.isBlank(cup) || CUP_PATTERN.matcher(cup).matches())){
			addErrore(ErroreCore.FORMATO_NON_VALIDO.getErrore("CUP", "Deve essere composto da 15 caratteri, il primo e il quarto dei quali devono essere una lettera"));
			model.setProseguireConElaborazione(Boolean.TRUE);
		}
	}
	
}
