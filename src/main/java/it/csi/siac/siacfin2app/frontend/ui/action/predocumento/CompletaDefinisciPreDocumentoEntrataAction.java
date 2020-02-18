/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.predocumento;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.predocumento.CompletaDefinisciPreDocumentoEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaDefiniscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPreDocumentoEntrataPerStato;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPreDocumentoEntrataPerStatoResponse;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe di action per la ricerca del PreDocumento di Entrata per il completamento e la definizione
 * 
 * @author Marchino Alessandro
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class CompletaDefinisciPreDocumentoEntrataAction extends GenericPreDocumentoEntrataAction<CompletaDefinisciPreDocumentoEntrataModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3049741139669487436L;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		
		// Caricamento liste
		try {
			caricaListaTipoCausale();
			caricaListaCausaleEntrata();
			caricaListaTipoAtto();
			caricaListaClasseSoggetto();
		} catch(WebServiceInvocationFailureException e) {
			log.error("prepare", "Errore nell'invocazione del caricamento di una lista: " + e.getMessage());
		} finally {
			checkMetodoConclusoSenzaErrori();
		}
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// Imposto i valori di default
		return SUCCESS;
	}
	
	/**
	 * Completa e definisce
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String completaDefinisci() {
		final String methodName = "ricerca";
		
		CompletaDefiniscePreDocumentoEntrata req = model.creaRequestCompletaDefiniscePreDocumentoEntrata();
		AsyncServiceResponse res = preDocumentoEntrataService.completaDefiniscePreDocumentoEntrataAsync(wrapRequestToAsync(req));
		
		if (res.hasErrori()) {
			log.info(methodName, "Fallimento nella chiamata al servizio di ricerca sintetica");
			addErrori(res);
			return INPUT;
		}
		
		addInformazione(ErroreCore.ELABORAZIONE_ASINCRONA_AVVIATA.getErrore("completamento e definizione della predisposizione di incasso", ""));
		return SUCCESS;
	}
	
	/**
	 * Validazione per il completamento e la definizione del preDocumento.
	 */
	public void validateCompletaDefinisci() {
		boolean formValido =
				checkCampoValorizzato(model.getDataCompetenzaDa(), "Data da") ||
				checkCampoValorizzato(model.getDataCompetenzaA(), "Data a") ||
				checkPresenzaIdEntita(model.getCausaleEntrata());
		
		if(!formValido) {
			addErrore(ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		}
		checkCondition(model.getDataCompetenzaDa() == null || model.getDataCompetenzaA() == null
				|| !model.getDataCompetenzaA().before(model.getDataCompetenzaDa()),
				ErroreCore.VALORE_NON_VALIDO.getErrore("Data competenza", "la data di competenza da non deve essere inferiore la data di competenza a"));
		
		checkAttoAmministrativo();
		checkAccertamento();
		checkSoggetto();
		checkCoerenzaSoggettoAccertamento();
	}

	/**
	 * Controllo del provvedimento
	 */
	private void checkAttoAmministrativo() {
		// richiamo il metodo di validazione
		validazioneAttoAmministrativo();
		
		AttoAmministrativo aa = model.getAttoAmministrativo();
		if(aa != null && aa.getUid() != 0) {
			model.setTipoAtto(aa.getTipoAtto());
			model.setStrutturaAmministrativoContabileAttoAmministrativo(aa.getStrutturaAmmContabile());
		}
	}

	/**
	 * Controllo di esistenza dell'accertamento
	 */
	private void checkAccertamento() {
		Accertamento accertamento = model.getMovimentoGestione();
		checkCondition(accertamento != null && accertamento.getAnnoMovimento() != 0 && accertamento.getNumero() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Accertamento"));
		validazioneAccertamentoSubAccertamento();
	}

	/**
	 * Controllo di esistenza del soggetto
	 */
	private void checkSoggetto() {
		Soggetto soggetto = model.getSoggetto();
		checkCondition(soggetto != null && StringUtils.isNotBlank(soggetto.getCodiceSoggetto()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Soggetto"));
		validazioneSoggetto();
	}

	/**
	 * Controllo di coerenza tra accertamento e soggetto
	 */
	private void checkCoerenzaSoggettoAccertamento() {
		controlloConguenzaSoggettoMovimentoGestione(model.getSoggetto(), model.getMovimentoGestione(), model.getSubMovimentoGestione(),
				"predisposizione di incasso", "accertamento");
	}
	
	@Override
	protected void checkDisponibilitaAccertamentoSubaccertamento() {
		// Vuoto
	}
	
	/**-
	 * Ricerca dei totali
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String cercaTotali() {
		final String methodName = "cercaTotali";
		// Invocazione del servizio
		RicercaTotaliPreDocumentoEntrataPerStato req = model.creaRequestRicercaTotaliPreDocumentoEntrataPerStato();
		RicercaTotaliPreDocumentoEntrataPerStatoResponse res = preDocumentoEntrataService.ricercaTotaliPreDocumentoEntrataPerStato(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			// Si sono verificati degli errori: esco.
			addErrori(res);
			log.debug(methodName, "esecuzione del servizio RicercaTotaliPreDocumentoEntrataPerStato terminata con errori.");
			return INPUT;
		}
		impostaTotaliElenco(res);
		
		return SUCCESS;
	}
	
	/**
	 * Imposta totali elenco.
	 *
	 * @param res la response da cui ottenere i dati
	 */
	public void impostaTotaliElenco(RicercaTotaliPreDocumentoEntrataPerStatoResponse res) {
		Map<StatoOperativoPreDocumento, BigDecimal> importiPreDocumenti = res.getImportiPreDocumenti();
		Map<StatoOperativoPreDocumento, Long> numeroPreDocumenti = res.getNumeroPreDocumenti();

		// Importi e numeri...
		
		// Incompleto
		model.setNumeroPreDocumentiIncompleti(defaultToZero(numeroPreDocumenti.get(StatoOperativoPreDocumento.INCOMPLETO)));
		model.setImportoPreDocumentiIncompleti(defaultToZero(importiPreDocumenti.get(StatoOperativoPreDocumento.INCOMPLETO)));
		// Completi
		model.setNumeroPreDocumentiCompleti(defaultToZero(numeroPreDocumenti.get(StatoOperativoPreDocumento.COMPLETO)));
		model.setImportoPreDocumentiCompleti(defaultToZero(importiPreDocumenti.get(StatoOperativoPreDocumento.COMPLETO)));
		// Annullati e definiti
		model.setNumeroPreDocumentiAnnullatiDefiniti(numeroPerStati(numeroPreDocumenti, StatoOperativoPreDocumento.ANNULLATO, StatoOperativoPreDocumento.DEFINITO));
		model.setImportoPreDocumentiAnnullatiDefiniti(importoPerStati(importiPreDocumenti, StatoOperativoPreDocumento.ANNULLATO, StatoOperativoPreDocumento.DEFINITO));
		// Totale
		model.setImportoPreDocumentiTotale(importoPerStati(importiPreDocumenti, StatoOperativoPreDocumento.values()));
		model.setNumeroPreDocumentiTotale(numeroPerStati(numeroPreDocumenti, StatoOperativoPreDocumento.values()));
	}

	/**
	 * Importo dei predoc per gli stati forniti
	 * @param importiPreDocumenti gli importi
	 * @param stati gli stati
	 * @return gli importi
	 */
	private BigDecimal importoPerStati(Map<StatoOperativoPreDocumento, BigDecimal> importiPreDocumenti, StatoOperativoPreDocumento... stati) {
		BigDecimal importoTotale = BigDecimal.ZERO;
		Collection<StatoOperativoPreDocumento> statiToCheck = Arrays.asList(stati);
		for(Entry<StatoOperativoPreDocumento, BigDecimal> entry : importiPreDocumenti.entrySet()) {
			if(statiToCheck.contains(entry.getKey())) {
				importoTotale = importoTotale.add(defaultToZero(entry.getValue()));
			}
		}
		return importoTotale;
	}
	
	/**
	 * Numero dei predoc per gli stati forniti
	 * @param numeroPreDocumenti il numero
	 * @param stati gli stati
	 * @return il numero
	 */
	private Long numeroPerStati(Map<StatoOperativoPreDocumento, Long> numeroPreDocumenti, StatoOperativoPreDocumento... stati) {
		long numeroPredoc = 0;
		Collection<StatoOperativoPreDocumento> statiToCheck = Arrays.asList(stati);
		for(Entry<StatoOperativoPreDocumento, Long> entry : numeroPreDocumenti.entrySet()) {
			if(statiToCheck.contains(entry.getKey())) {
				numeroPredoc += defaultToZero(entry.getValue()).longValue();
			}
		}
		return Long.valueOf(numeroPredoc);
	}
	
	/**
	 * Imposta il valore fornendo un default a zero
	 * @param value il valore da impostare
	 * @return il valore se non null; zero altrimento
	 */
	private Long defaultToZero(Long value) {
		return value != null ? value : Long.valueOf(0L);
	}
	
	/**
	 * Imposta il valore fornendo un default a zero
	 * @param value il valore da impostare
	 * @return il valore se non null; zero altrimento
	 */
	private BigDecimal defaultToZero(BigDecimal value) {
		return value != null ? value : BigDecimal.ZERO;
	}
	
	/**
	 * Validazione per la ricerca dei totali
	 */
	public void validateCercaTotali() {
		boolean formValido =
				checkCampoValorizzato(model.getDataCompetenzaDa(), "Data da") ||
				checkCampoValorizzato(model.getDataCompetenzaA(), "Data a") ||
				checkPresenzaIdEntita(model.getCausaleEntrata());
		
		if(!formValido) {
			addErrore(ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		}
	}
	
}
