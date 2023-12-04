/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.tipoonere;

import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.result.CustomJSONResult;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Distinta;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;

/**
 * Classe di Action per l'aggiornamento del tipoOnere, relativa alle funzionalit&agrave; di entrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/11/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(GenericAggiornaTipoOnereAction.MODEL_SESSION_NAME)
public class AggiornaTipoOnereEntrataAction extends GenericAggiornaTipoOnereAction {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6289993902261898062L;
	
	/**
	 * Preparazione per il metodo {@link #inserisciCausale()}.
	 */
	public void prepareInserisciCausale() {
		model.setAccertamento(null);
		model.setSubAccertamento(null);
		if(model.getCapitoloEntrataGestione() != null) {
			model.getCapitoloEntrataGestione().setAnnoCapitolo(null);
			model.getCapitoloEntrataGestione().setNumeroCapitolo(null);
			model.getCapitoloEntrataGestione().setNumeroArticolo(null);
			model.getCapitoloEntrataGestione().setNumeroUEB(null);
		}
		//SIAC-5060
		model.setDistinta(null);
	}
	
	
	
	/**
	 * Inserisce la causale.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisciCausale() {
		final String methodName = "inserisciCausale";
		try {
			validationInserisciCausale();
		} catch(ParamValidationException pve) {
			log.debug(methodName, "Presenti errori di validazione: " + pve.getMessage());
			// Ho degli errori di validazione. Non ha senso proseguire
			return INPUT;
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Presenti errori nell'invocazione dei servizi: " + wsife.getMessage());
			// Ho degli errori di validazione. Non ha senso proseguire
			return INPUT;
		}
		
		// Creazione di una causale di entrata a partire dal model
		model.addCausaleEntrata();
		return SUCCESS;
	}
	
	/**
	 * Validazione per l'inserimento della causale.
	 * 
	 * @throws ParamValidationException             in caso di errori nella validazione
	 * @throws WebServiceInvocationFailureException in caso di errori nell'invocazione dei servizi
	 */
	private void validationInserisciCausale() throws WebServiceInvocationFailureException {
		Accertamento accertamento = model.getAccertamento();
		SubAccertamento subAccertamento = model.getSubAccertamento();
		
		CapitoloEntrataGestione capitoloEntrataGestione = model.getCapitoloEntrataGestione();
		//controlli sulla correttezza formale dei parametri
		checkCondition(accertamento != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Accertamento"), true);
		checkCondition(accertamento.getAnnoMovimento() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno accertamento"));
		checkCondition(accertamento.getAnnoMovimento() <= model.getAnnoEsercizioInt().intValue(),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno accertamento", "sono ammessi solo movimenti di gestione o residui"));
		checkCondition(accertamento.getNumeroBigDecimal() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero accertamento"));
		
		if(hasErrori()) {
			throw new ParamValidationException("Errore nei parametri di input");
		}
		
		//CONTROLLI SUL MOVIMENTO GESTIONE
		controllaAccertamentoNonGiaPresente(accertamento, subAccertamento);
		
		RicercaAccertamentoPerChiaveOttimizzatoResponse response = ricercaAccertamento();
		accertamento = response.getAccertamento();
		// Sono richiesti solo movimenti di gestione in stato operativo DEFINITIVO oppure relativi sub, sempre in stato operativo DEFINITIVO
		// Puo' capitare di avere il movimento di gestione in stato 'N – definitivo non utilizzabile'  ed i sub in stato DEFINITIVO.
		checkCondition(StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(accertamento.getStatoOperativoMovimentoGestioneEntrata())
				|| (StatoOperativoMovimentoGestione.DEFINITIVO_NON_LIQUIDABILE.getCodice().equals(accertamento.getStatoOperativoMovimentoGestioneEntrata())
					&& accertamento.getSubAccertamenti() != null && !accertamento.getSubAccertamenti().isEmpty()),
				ErroreFin.ACCERTAMENTO_NON_IN_STATO_DEFINITIVO.getErrore(""), true);
		// Controllo il subaccertamento
		subAccertamento = checkPresenzaSubAccertamentiNonAnnullati(response.getElencoSubAccertamentiTuttiConSoloGliIds(), accertamento, subAccertamento);
		
		//CONTROLLI SUL CAPITOLO
		// Se gia' presente il capitolo, il movimento di gestione deve appartenere al capitolo indicato. Altrimenti inviare un errore.
		capitoloEntrataGestione = checkCapitoloEntrataGestione(capitoloEntrataGestione);
		CapitoloEntrataGestione cegMovimento = subAccertamento != null && subAccertamento.getCapitoloEntrataGestione() != null
				? subAccertamento.getCapitoloEntrataGestione()
				: accertamento.getCapitoloEntrataGestione();
		checkCondition(capitoloEntrataGestione == null || (cegMovimento != null && cegMovimento.getUid() == capitoloEntrataGestione.getUid()),
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("il capitolo selezionato e il capitolo dell'accertamento devono coincidere"), true);
		if(capitoloEntrataGestione == null && cegMovimento != null) {
			// Se non ancora presente, con l'inserimento in tabella del movimento di gestione popolare la sezione capitolo con il capitolo associato allo stesso.
			capitoloEntrataGestione = cegMovimento;
		}
		
		checkDistinta();
		
		// Impostazione dei dati nel model
		model.setAccertamento(accertamento);
		model.setSubAccertamento(subAccertamento);
		model.setCapitoloEntrataGestione(capitoloEntrataGestione);
	}



	/**
	 * Controlla la distinta.
	 */
	private void checkDistinta() {
		Distinta ds = model.getDistinta();
		if(!idEntitaPresente(ds)){
			//non ho digitato la distinta
			return;
		}
		CodificaFin codificaFIN = new CodificaFin();
		codificaFIN.setUid(ds.getUid());
		codificaFIN = ComparatorUtils.searchByUid(model.getListaDistinta(), codificaFIN);
		if(codificaFIN == null){
			//la distinta digitata non e' presente in archivio.
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("distinta", "uid: " + ds.getUid()));
			return;
		}
		ds.setCodice(codificaFIN.getCodice());
		ds.setDescrizione(codificaFIN.getDescrizione());			

	}

	/**
	 * Controlla che l'accertamento non sia gi&agrave; presente nella lista.
	 * 
	 * @param accertamento    l'accertamento da controllare
	 * @param subAccertamento il subaccertamento da controllare
	 */
	private void controllaAccertamentoNonGiaPresente(Accertamento accertamento, SubAccertamento subAccertamento) {
		final String methodName = "controllaAccertamentoNonGiaPresente";
		List<CausaleEntrata> list = model.getListaCausaleEntrata();
		for(CausaleEntrata ce : list) {
			Accertamento a = ce.getAccertamento();
			SubAccertamento sa = ce.getSubAccertamento();
//			boolean giaPresente = (a.getAnnoMovimento() == accertamento.getAnnoMovimento())
//					&& (a.getNumero().equals(accertamento.getNumero()))
//					&& (subAccertamento == null || sa == null || sa.getNumero().equals(subAccertamento.getNumero()));
//			checkCondition(!giaPresente, ErroreCore.RELAZIONE_GIA_PRESENTE.getErrore(), true);
			//CORREZIONE SONAR
			boolean accertamentoGiaPresente = (a.getAnnoMovimento() == accertamento.getAnnoMovimento()) && (a.getNumeroBigDecimal().equals(accertamento.getNumeroBigDecimal()));
			boolean subAccertamentoGiaPresente = subAccertamento == null || sa == null || sa.getNumeroBigDecimal().equals(subAccertamento.getNumeroBigDecimal());
			
			checkCondition(!(accertamentoGiaPresente && subAccertamentoGiaPresente), ErroreCore.RELAZIONE_GIA_PRESENTE.getErrore(), true);
		}
		log.debug(methodName, "Nessun collegamento con l'accertamento selezionato ancora presente. Proseguo con la validazione");
	}

	/**
	 * Ricerca l'accertamento dal servizio
	 * 
	 * @return la response
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errori nell'invocazione dei servizi
	 */
	private RicercaAccertamentoPerChiaveOttimizzatoResponse ricercaAccertamento() throws WebServiceInvocationFailureException {
		//carico da servizio l'accertamento digitato dall'utente
		RicercaAccertamentoPerChiaveOttimizzato request = model.creaRequestRicercaAccertamentoPerChiaveOttimizzato();
		logServiceRequest(request);
		RicercaAccertamentoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaAccertamentoPerChiaveOttimizzato.class, response));
		}
		if(response.getAccertamento() == null) {
			//l'accertametno digitato non e' presente in archivio
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Accertamento", request.getpRicercaAccertamentoK().getAnnoEsercizio() + "/"
					+ request.getpRicercaAccertamentoK().getAnnoAccertamento() + "/" + request.getpRicercaAccertamentoK().getNumeroAccertamento()));
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaAccertamentoPerChiaveOttimizzato.class, response));
		}
		return response;
	}
	
	/**
	 * Controlla la presenza di eventuali subaccertamenti non annullati collegati all'accertamento. Se l'accertamento ha dei sub in stato diverso da annullato,
	 * allora l'utente deve necessariamente aver digitato nel campo "subaccertamento" uno di questi. Se invece l'utente non ha digitato il sub, allora l'accertamento non deve avere dei subaccertamenti.
	 * 
	 * @param listaSub        la lista dei sub 
	 * @param accertamento    l'accertamento
	 * @param subAccertamento il subaccertamento
	 * 
	 * @return il subaccertamento nella lista, se presente
	 */
	private SubAccertamento checkPresenzaSubAccertamentiNonAnnullati(List<SubAccertamento> listaSub, Accertamento accertamento, SubAccertamento subAccertamento) {
		final String methodName = "checkPresenzaSubAccertamentiNonAnnullati";
		boolean subAccertamentoDigitato = subAccertamento != null && subAccertamento.getNumeroBigDecimal() != null;
		log.debug(methodName, "Subaccertamento digitato? " + subAccertamentoDigitato + (subAccertamentoDigitato ? " (numero :" + subAccertamento + ")" : ""));
		
		boolean accertamentoSenzaSub = listaSub == null || listaSub.isEmpty();
		log.debug(methodName, "Accertamento senza sub? " + accertamentoSenzaSub + (!accertamentoSenzaSub ? " (numero sub :" + listaSub.size() + ")" : ""));
		
		// Se l'operatore seleziona un movimento di gestione con sub NON ANNULLATI e' fornito il seguente messaggio al fine di bloccarne la selezione
		checkCondition(subAccertamentoDigitato || accertamentoSenzaSub, ErroreFin.ACCERTAMENTO_CON_SUBACCERTAMENTI.getErrore(), true);
		
		if(!subAccertamentoDigitato && accertamentoSenzaSub) {
			log.debug(methodName, "Nessun subaccertamento selezionato. L'accertamento non ha inoltre sub: esco");
			return null;
		}
		
		//l'operatore ha selezionato un sub: controllo che questo sia effettivamente tra i sub dell'accertamento
		SubAccertamento subAccertamentoTrovato = ComparatorUtils.findByNumeroMovimentoGestione(accertamento.getElencoSubAccertamenti(), subAccertamento);
		boolean isSubAccertamentoTrovato = !accertamentoSenzaSub && subAccertamentoTrovato != null && subAccertamentoTrovato.getNumeroBigDecimal() != null;
		log.debug(methodName, "Subaccertamento trovato? " + isSubAccertamentoTrovato + (isSubAccertamentoTrovato ? " (uid :" + subAccertamentoTrovato.getUid() + ")" : ""));
		
		//ho due casi: O l'operatore ha digitato un accertamento e questo e' presente tra i subaccertamenti caricati da servizio, OPPURE l'operatore non ha digitato il sub e l'accertamento e' senza sub
		boolean resultingCondition = (subAccertamentoDigitato && isSubAccertamentoTrovato) || (!subAccertamentoDigitato && accertamentoSenzaSub);
		log.debug(methodName, "Condizione di controllo: (subAccertamentoDigitato && isSubAccertamentoTrovato) || (!subAccertamentoDigitato && accertamentoSenzaSub) = "
				+ "(" + subAccertamentoDigitato + " && " + isSubAccertamentoTrovato + ") || (" + !subAccertamentoDigitato + " && " + accertamentoSenzaSub + ") = "
				+ resultingCondition);
		
		checkCondition(resultingCondition, ErroreCore.ENTITA_NON_TROVATA.getErrore("SubAccertamento",
				accertamento.getAnnoMovimento() + "/" + accertamento.getNumeroBigDecimal() + "-" + subAccertamento.getNumeroBigDecimal()), true);
		
		return subAccertamentoTrovato;
	}
	
	/**
	 * Preparazione per il metodo {@link #eliminaCausale()}.
	 */
	public void prepareEliminaCausale() {
		model.setRow(null);
	}
	
	/**
	 * Elimina la causale.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String eliminaCausale() {
		final String methodName = "eliminaCausale";
		try {
			validazioneEliminaCausale();
		} catch(ParamValidationException pve) {
			log.debug(methodName, "Presenti errori di validazione: " + pve.getMessage());
			// Ho degli errori di validazione. Non ha senso proseguire
			return INPUT;
		}
		int row = model.getRow().intValue();
		// Rimuovo l'oggetto dalla lista
		CausaleEntrata causaleEntrata = model.getListaCausaleEntrata().remove(row);
		// Ne imposto la data di fine validita
		Date now = new Date();
		causaleEntrata.setDataFineValidita(now);
		causaleEntrata.setDataScadenza(now);
		causaleEntrata.setDataFineValiditaCausale(now);
		// cancello i dati affinché non vengano popolate nuovamente le r
		causaleEntrata.setAccertamento(null);
		causaleEntrata.setAttoAmministrativo(null);
		causaleEntrata.setCapitoloEntrataGestione(null);
		causaleEntrata.setSoggetto(null);
		causaleEntrata.setStrutturaAmministrativoContabile(null);
		causaleEntrata.setSubAccertamento(null);
		
		// Imposto la causale nella lista delle causali da eliminare
		model.getListaCausaleEntrataDaEliminare().add(causaleEntrata);
		log.debug(methodName, "Eliminata la causale alla riga " + row);
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #eliminaCausale()}.
	 */
	private void validazioneEliminaCausale() {
		checkCondition(model.getRow() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Riga da cancellare"), true);
		int row = model.getRow().intValue();
		int size = model.getListaCausaleEntrata().size();
		// Controllo che la riga fornita sia all'interno della lista
		checkCondition(row > -1 && row < size,
			ErroreCore.FORMATO_NON_VALIDO.getErrore("riga da cancellare", "deve essere compreso tra 0 e " + size + ", fornito il valore " + row),
			true);
	}
	
	/**
	 * Classe di Result specifica per l'aggiornamento del tipoOnere di entrata.
	 * 
	 * @author Marchino Alessandro
	 * @version 1.0.0 - 04/nov/2014
	 *
	 */
	public static class AggiornaTipoOnereEntrataJSONResult extends CustomJSONResult {
		/** Per la serializzazione */
		private static final long serialVersionUID = 4119258569947370423L;
		/** Propriet&agrave; da includere nel JSON creato */
		private static final String INCLUDE_PROPERTIES = "errori.*, informazioni.*, listaCausaleEntrata.*, capitoloEntrataGestione.*, soggetto.*, totaleCausaliCollegate";

		/** Empty default constructor */
		public AggiornaTipoOnereEntrataJSONResult() {
			super();
			setIncludeProperties(INCLUDE_PROPERTIES);
		}
	}
}
