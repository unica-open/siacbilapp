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
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe di Action per l'aggiornamento del tipoOnere, relativa alle funzionalit&agrave; di spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/11/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(GenericAggiornaTipoOnereAction.MODEL_SESSION_NAME)
public class AggiornaTipoOnereSpesaAction extends GenericAggiornaTipoOnereAction {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5047328366639871766L;
	
	/**
	 * Preparazione per il metodo {@link #inserisciCausale()}.
	 */
	public void prepareInserisciCausale() {
		model.setImpegno(null);
		model.setSubImpegno(null);
		
		model.setSoggetto(null);
		
		if(model.getCapitoloUscitaGestione() != null) {
			model.getCapitoloUscitaGestione().setAnnoCapitolo(null);
			model.getCapitoloUscitaGestione().setNumeroCapitolo(null);
			model.getCapitoloUscitaGestione().setNumeroArticolo(null);
			model.getCapitoloUscitaGestione().setNumeroUEB(null);
		}
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
		
		// Impostazione della causale nella lista
		model.addCausaleSpesa();
		return SUCCESS;
	}
	
	/**
	 * Validazione per l'inserimento della causale.
	 * 
	 * @throws ParamValidationException             in caso di errori nella validazione
	 * @throws WebServiceInvocationFailureException in caso di errori nell'invocazione dei servizi
	 */
	private void validationInserisciCausale() throws WebServiceInvocationFailureException {
		Impegno impegno = model.getImpegno();
		SubImpegno subImpegno = model.getSubImpegno();
		
		CapitoloUscitaGestione capitoloUscitaGestione = model.getCapitoloUscitaGestione();
		Soggetto soggetto = model.getSoggetto();
		
		checkCondition(impegno != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Impegno"), true);
		checkCondition(impegno.getAnnoMovimento() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno impegno"));
		checkCondition(impegno.getAnnoMovimento() <= model.getAnnoEsercizioInt().intValue(),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno impegno", "sono ammessi solo movimenti di gestione o residui"));
		checkCondition(impegno.getNumeroBigDecimal() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero impegno"));
		
		if(hasErrori()) {
			return;
		}
		
		controllaImpegnoNonGiaPresente(impegno, subImpegno);
		
		RicercaImpegnoPerChiaveOttimizzatoResponse response = ricercaImpegno();
		impegno = response.getImpegno();
		// Sono richiesti solo movimenti di gestione in stato operativo DEFINITIVO oppure relativi sub, sempre in stato operativo DEFINITIVO
		// Puo' capitare di avere il movimento di gestione in stato 'N – definitivo non utilizzabile'  ed i sub in stato DEFINITIVO.
		checkCondition(StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(impegno.getStatoOperativoMovimentoGestioneSpesa())
				|| (StatoOperativoMovimentoGestione.DEFINITIVO_NON_LIQUIDABILE.getCodice().equals(impegno.getStatoOperativoMovimentoGestioneSpesa())
					&& impegno.getElencoSubImpegni() != null && !impegno.getElencoSubImpegni().isEmpty()),
				ErroreFin.IMPEGNO_NON_IN_STATO_DEFINITIVO.getErrore(), true);
		// Controllo il subaccertamento
		subImpegno = checkPresenzaSubImpegniNonAnnullati(response.getElencoSubImpegniTuttiConSoloGliIds(), impegno, subImpegno);
		// Se gia' presente il capitolo, il movimento di gestione deve appartenere al capitolo indicato. Altrimenti inviare un errore.
		capitoloUscitaGestione = checkCapitoloUscitaGestione(capitoloUscitaGestione);
		CapitoloUscitaGestione cugMovimento = subImpegno != null && subImpegno.getCapitoloUscitaGestione() != null
				? subImpegno.getCapitoloUscitaGestione()
				: impegno.getCapitoloUscitaGestione();
		checkCondition(capitoloUscitaGestione == null || (cugMovimento != null && cugMovimento.getUid() == capitoloUscitaGestione.getUid()),
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("il capitolo selezionato e il capitolo dell'impegno devono coincidere"), true);
		if(capitoloUscitaGestione == null && cugMovimento != null) {
			// Se non ancora presente, con l'inserimento in tabella del movimento di gestione popolare la sezione capitolo con il capitolo associato allo stesso.
			capitoloUscitaGestione = cugMovimento;
		}
		
		soggetto = checkSoggetto(soggetto);
		Soggetto soggettoMovimento = subImpegno != null ? subImpegno.getSoggetto() : impegno.getSoggetto();
		checkCoerenzaSoggetto(soggetto, subImpegno != null ? subImpegno : impegno);
		if(soggetto == null && soggettoMovimento != null) {
			// Se non ancora presente, con l'inserimento in tabella del soggetto popolare i campi.
			soggetto = soggettoMovimento;
		}
		
		// Impostazione dei dati nel model
		model.setImpegno(impegno);
		model.setSubImpegno(subImpegno);
		model.setCapitoloUscitaGestione(capitoloUscitaGestione);
		model.setSoggetto(soggetto);
	}
	
	/**
	 * Controlla che l'impegno non sia gi&agrave; presente nella lista.
	 * 
	 * @param impegno    l'impegno da controllare
	 * @param subImpegno il subimpegno da controllare
	 */
	private void controllaImpegnoNonGiaPresente(Impegno impegno, SubImpegno subImpegno) {
		final String methodName = "controllaImpegnoNonGiaPresente";
		List<CausaleSpesa> list = model.getListaCausaleSpesa();
		for(CausaleSpesa cs : list) {
			Impegno i = cs.getImpegno();
			SubImpegno si = cs.getSubImpegno();
			boolean giaPresente = (i.getAnnoMovimento() == impegno.getAnnoMovimento())
					&& (i.getNumeroBigDecimal().equals(impegno.getNumeroBigDecimal()))
					&& (subImpegno == null || si == null || si.getNumeroBigDecimal().equals(subImpegno.getNumeroBigDecimal()));
			checkCondition(!giaPresente, ErroreCore.RELAZIONE_GIA_PRESENTE.getErrore(), true);
		}
		log.debug(methodName, "Nessun collegamento con l'impegno selezionato ancora presente. Proseguo con la validazione");
	}
	
	/**
	 * Ricerca l'impegno dal servizio
	 * 
	 * @return la response
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errori nell'invocazione dei servizi
	 */
	private RicercaImpegnoPerChiaveOttimizzatoResponse ricercaImpegno() throws WebServiceInvocationFailureException {
		RicercaImpegnoPerChiaveOttimizzato request = model.creaRequestRicercaImpegnoPerChiaveOttimizzato();
		logServiceRequest(request);
		RicercaImpegnoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaImpegnoPerChiaveOttimizzato.class, response));
		}
		if(response.getImpegno() == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Impegno", request.getpRicercaImpegnoK().getAnnoEsercizio() + "/"
					+ request.getpRicercaImpegnoK().getAnnoImpegno() + "/" + request.getpRicercaImpegnoK().getNumeroImpegno()));
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaImpegnoPerChiaveOttimizzato.class, response));
		}
		return response;
	}
	
	/**
	 * Controlla la presenza di eventuali subimpegni non annullati collegati all'impegno.
	 * 
	 * @param listaSub   la lista dei sub
	 * @param impegno    l'impegno
	 * @param subImpegno il subimpegno
	 * 
	 * @return il subaccertamento nella lista, se presente
	 */
	private SubImpegno checkPresenzaSubImpegniNonAnnullati(List<SubImpegno> listaSub, Impegno impegno, SubImpegno subImpegno) {
		final String methodName="checkPresenzaSubImpegniNonAnnullati";
		boolean subImpegnoDigitato = subImpegno != null && subImpegno.getNumeroBigDecimal() != null;
		boolean impegnoSenzaSub = listaSub == null || listaSub.isEmpty();
		
		log.debug(methodName, "Subimpegno digitato? " + subImpegnoDigitato + ". L'impegno ha dei subimpegni? " + !impegnoSenzaSub);
		// Se l'operatore seleziona un movimento di gestione con sub NON ANNULLATI e' fornito il seguente messaggio al fine di bloccarne la selezione
		checkCondition(subImpegnoDigitato || impegnoSenzaSub, ErroreFin.IMPEGNO_CON_SUBIMPEGNI_VALIDI.getErrore(), true);
		if(!subImpegnoDigitato) {
			return null;
		}
		
		SubImpegno subImpegnoTrovato = ComparatorUtils.findByNumeroMovimentoGestione(impegno.getElencoSubImpegni(), subImpegno);
		boolean isSubImpegnoTrovato = !impegnoSenzaSub && subImpegnoTrovato !=null && subImpegnoTrovato.getNumeroBigDecimal() != null;
		
		boolean isSubImpegnoCorretto = (subImpegnoDigitato && isSubImpegnoTrovato) || (!subImpegnoDigitato && impegnoSenzaSub);
		log.debug(methodName, "Condizione di superamento dei controlli del subImpegno: (subImpegnoDigitato && isSubImpegnoTrovato) || (!subImpegnoDigitato && impegnoSenzaSub) = "
				+ "(" + subImpegnoDigitato + " && " + isSubImpegnoTrovato + ") || (" + !subImpegnoDigitato + " && " + impegnoSenzaSub + ") = "
				+ isSubImpegnoCorretto);
		checkCondition(isSubImpegnoCorretto, ErroreCore.ENTITA_NON_TROVATA.getErrore("SubImpegno", impegno.getAnnoMovimento() + "/" + impegno.getNumeroBigDecimal() + "-" + subImpegno.getNumeroBigDecimal()), true);
		
		return subImpegnoTrovato;
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
		CausaleSpesa causaleSpesa = model.getListaCausaleSpesa().remove(row);
		// Ne imposto la data di fine validita
		Date now = new Date();
		causaleSpesa.setDataFineValidita(now);
		causaleSpesa.setDataScadenza(now);
		causaleSpesa.setDataFineValiditaCausale(now);
		// cancello i dati affinché non vengano popolate nuovamente le r
		causaleSpesa.setAttoAmministrativo(null);
		causaleSpesa.setCapitoloUscitaGestione(null);
		causaleSpesa.setSubImpegno(null);
		causaleSpesa.setImpegno(null);
		causaleSpesa.setSedeSecondariaSoggetto(null);
		causaleSpesa.setSoggetto(null);
		causaleSpesa.setStrutturaAmministrativoContabile(null);
		causaleSpesa.setModalitaPagamentoSoggetto(null);
		// Imposto la causale nella lista delle causali da eliminare
		model.getListaCausaleSpesaDaEliminare().add(causaleSpesa);
		log.debug(methodName, "Eliminata la causale alla riga " + row);
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #eliminaCausale()}.
	 */
	private void validazioneEliminaCausale() {
		checkCondition(model.getRow() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Riga da cancellare"), true);
		int row = model.getRow().intValue();
		int size = model.getListaCausaleSpesa().size();
		// Controllo che la riga fornita sia all'interno della lista
		checkCondition(row > -1 && row < size,
			ErroreCore.FORMATO_NON_VALIDO.getErrore("riga da cancellare", "deve essere compreso tra 0 e " + size + ", fornito il valore " + row),
			true);
	}
	
	/**
	 * Classe di Result specifica per l'aggiornamento del tipoOnere di spesa.
	 * 
	 * @author Marchino Alessandro
	 * @version 1.0.0 - 04/nov/2014
	 *
	 */
	public static class AggiornaTipoOnereSpesaJSONResult extends CustomJSONResult {
		/** Per la serializzazione */
		private static final long serialVersionUID = 4119258569947370423L;
		/** Propriet&agrave; da includere nel JSON creato */
		private static final String INCLUDE_PROPERTIES = "errori.*, informazioni.*, listaCausaleSpesa.*, capitoloUscitaGestione.*, soggetto.*, totaleCausaliCollegate";

		/** Empty default constructor */
		public AggiornaTipoOnereSpesaJSONResult() {
			super();
			setIncludeProperties(INCLUDE_PROPERTIES);
		}
	}
}
